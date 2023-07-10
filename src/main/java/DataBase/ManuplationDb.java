package DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import Table.WIFIINFORMATION;

public class ManuplationDb {

    public static void saveNearWifi(Integer xCoord, Integer yCoord) {
        try {
        	StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); // URL
            urlBuilder.append("/" + URLEncoder.encode("736b43586264696a39317455455551", "UTF-8")); /* 인증키 (sample 사용시에는 호출시 제한됩니다.) */
            urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8")); /* 요청파일타입 (xml, xmlf, xls, json) */
            urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo", "UTF-8")); /* 서비스명 (대소문자 구분 필수입니다.) */
            urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8")); /* 요청시작위치 (sample인증키 사용시 5이내 숫자) */
            urlBuilder.append("/" + URLEncoder.encode("1000", "UTF-8")); /* 요청종료위치 (sample인증키 사용시 5이상 숫자 선택 안 됨) */

            URL url = new URL(urlBuilder.toString());

            // API 호출
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // HTTP GET 요청 설정
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");

            int responseCode = connection.getResponseCode();

            BufferedReader rd;

            // 서비스코드가 정상이면
            if (responseCode == HttpURLConnection.HTTP_OK) {
                rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            connection.disconnect();
            
	        try {
		            JSONArray jsonArray = new JSONArray(sb.toString());
		            Connection connectionDb = ConnectionDb.getConnection();
		            String insertQuery = "INSERT INTO WIFIINFORMATION (DISTANCE, X_SWIFI_MGR_NO, X_SWIFI_WRDOFC, X_SWIFI_MAIN_NM, X_SWIFI_ADRES1, X_SWIFI_ADRES2, X_SWIFI_INSTL_FLOOR, X_SWIFI_INSTL_TY, X_SWIFI_INSTL_MBY, X_SWIFI_SVC_SE, X_SWIFI_CMCWR, X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR, X_SWIFI_REMARS3, LNT, LAT, WORK_DTTM) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		            PreparedStatement statement = connectionDb.prepareStatement(insertQuery);
		
		            for (int i = 0; i < jsonArray.length(); i++) {
		                JSONObject item = jsonArray.getJSONObject(i);
		                
		                double xCoord2 = Double.parseDouble(item.getString("LNT"));
		                double yCoord2 = Double.parseDouble(item.getString("LAT"));
		                double dLat = Math.toRadians(xCoord - xCoord2);
		                double dLon = Math.toRadians(yCoord - yCoord2);
		                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
		                        + Math.cos(Math.toRadians(xCoord)) * Math.cos(Math.toRadians(xCoord2))
		                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		                double distance = 6371 * c; 
		                
		                statement.setString(1, String.valueOf(distance));
		                statement.setString(2, item.getString("X_SWIFI_MGR_NO"));
		                statement.setString(3, item.getString("X_SWIFI_WRDOFC"));
		                statement.setString(4, item.getString("X_SWIFI_MAIN_NM"));
		                statement.setString(5, item.getString("X_SWIFI_ADRES1"));
		                statement.setString(6, item.getString("X_SWIFI_ADRES2"));
		                statement.setString(7, item.getString("X_SWIFI_INSTL_FLOOR"));
		                statement.setString(8, item.getString("X_SWIFI_INSTL_TY"));
		                statement.setString(9, item.getString("X_SWIFI_INSTL_MBY"));
		                statement.setString(10, item.getString("X_SWIFI_SVC_SE"));
		                statement.setString(11, item.getString("X_SWIFI_CMCWR"));
		                statement.setString(12, item.getString("X_SWIFI_CNSTC_YEAR"));
		                statement.setString(13, item.getString("X_SWIFI_INOUT_DOOR"));
		                statement.setString(14, item.getString("X_SWIFI_REMARS3"));
		                statement.setString(15, item.getString("LNT"));
		                statement.setString(16, item.getString("LAT"));
		                statement.setString(17, item.getString("WORK_DTTM"));
		                statement.addBatch();
	           		 }
	
	            	statement.executeBatch();
	            	
	            
		         } catch (Exception e) {
		             e.printStackTrace();
		             System.out.println("<p>Error occurred during data insertion: " + e.getMessage() + "</p>");
		         } finally {
		             ConnectionDb.closeConnection();
		         }
	        
	       } catch (Exception e) {
	    		e.printStackTrace();
	    		System.out.println("<p>Error occurred: " + e.getMessage() + "</p>");
		   }
    }
    
    
    
    public static void deleteWifiInformation() {
        try (Connection connection = ConnectionDb.getConnection()) {
            String deleteQuery = "DELETE FROM WIFIINFORMATION";
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static List<WIFIINFORMATION> selectNearWifi(String selectQuery) {
        List<WIFIINFORMATION> wifiList = new ArrayList<>();

        try (Connection connection = ConnectionDb.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	WIFIINFORMATION wifiInfo = new WIFIINFORMATION();
                wifiInfo.setDistance(resultSet.getString("DISTANCE"));
                wifiInfo.setMgrNo(resultSet.getString("X_SWIFI_MGR_NO"));
                wifiInfo.setWrdOfc(resultSet.getString("X_SWIFI_WRDOFC"));
                wifiInfo.setMainNm(resultSet.getString("X_SWIFI_MAIN_NM"));
                wifiInfo.setAdres1(resultSet.getString("X_SWIFI_ADRES1"));
                wifiInfo.setAdres2(resultSet.getString("X_SWIFI_ADRES2"));
                wifiInfo.setInstlFloor(resultSet.getString("X_SWIFI_INSTL_FLOOR"));
                wifiInfo.setInstlTy(resultSet.getString("X_SWIFI_INSTL_TY"));
                wifiInfo.setInstlMby(resultSet.getString("X_SWIFI_INSTL_MBY"));
                wifiInfo.setSvcSe(resultSet.getString("X_SWIFI_SVC_SE"));
                wifiInfo.setCmcWr(resultSet.getString("X_SWIFI_CMCWR"));
                wifiInfo.setCnstcYear(resultSet.getString("X_SWIFI_CNSTC_YEAR"));
                wifiInfo.setInOutDoor(resultSet.getString("X_SWIFI_INOUT_DOOR"));
                wifiInfo.setRemars3(resultSet.getString("X_SWIFI_REMARS3"));
                wifiInfo.setLnt(resultSet.getString("LNT"));
                wifiInfo.setLat(resultSet.getString("LAT"));
                wifiInfo.setWorkDttm(resultSet.getString("WORK_DTTM"));
                wifiList.add(wifiInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wifiList;
    }
}