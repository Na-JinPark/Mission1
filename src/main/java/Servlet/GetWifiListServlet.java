
package Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import Table.WIFIINFORMATION;
import DataBase.ManuplationDb;
/**
 * Servlet implementation class GetWifiListServlet
 */
@WebServlet("/GetWifiListServlet")
public class GetWifiListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetWifiListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        List<WIFIINFORMATION> wifiList = null;
        String query = request.getParameter("query");
        
        if (query.equals("selectNearWifi")) {
        	String selectQuery = "SELECT * FROM WIFIINFORMATION"; 
        	wifiList = ManuplationDb.selectNearWifi(selectQuery);
        	if (wifiList.isEmpty()) {
                ManuplationDb.deleteWifiInformation();
            }
        	Integer xCoord = Integer.parseInt(request.getParameter("xCoord"));
        	Integer yCoord = Integer.parseInt(request.getParameter("yCoord"));
        	ManuplationDb.saveNearWifi(xCoord, yCoord);
            selectQuery = "SELECT * FROM WIFIINFORMATION ORDER BY CUSTOMERID ASC"; 
        	wifiList = ManuplationDb.selectNearWifi(selectQuery);
        }else if(query.equals("selectWifi")) {
        	String selectQuery = "SELECT * FROM WIFIINFORMATION ORDER BY CUSTOMERID ASC"; 
        	wifiList = ManuplationDb.selectNearWifi(selectQuery);
        }

        try {
            
            JSONArray jsonArray = new JSONArray();
            for (WIFIINFORMATION wifi : wifiList) {
                JSONObject jsonWifi = new JSONObject();
                jsonWifi.put("distance", wifi.getDistance());
                jsonWifi.put("mgrNo", wifi.getMgrNo());
                jsonWifi.put("wrdOfc", wifi.getWrdOfc());
                jsonWifi.put("mainNm", wifi.getMainNm());
                jsonWifi.put("adres1", wifi.getAdres1());
                jsonWifi.put("adres2", wifi.getAdres2());
                jsonWifi.put("instlFloor", wifi.getInstlFloor());
                jsonWifi.put("instlTy", wifi.getInstlTy());
                jsonWifi.put("instlMby", wifi.getInstlMby());
                jsonWifi.put("svcSe", wifi.getSvcSe());
                jsonWifi.put("cmcWr", wifi.getCmcWr());
                jsonWifi.put("cnstcYear", wifi.getCnstcYear());
                jsonWifi.put("inOutDoor", wifi.getInOutDoor());
                jsonWifi.put("remars3", wifi.getRemars3());
                jsonWifi.put("lnt", wifi.getLnt());
                jsonWifi.put("lat", wifi.getLat());
                jsonWifi.put("workDttm", wifi.getWorkDttm());
                jsonArray.put(jsonWifi);
            }
            JSONObject responseData = new JSONObject();
            responseData.put("wifiList", jsonArray);

            out.print(responseData.toString());
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        } finally {
        	out.flush();
            out.close();
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
