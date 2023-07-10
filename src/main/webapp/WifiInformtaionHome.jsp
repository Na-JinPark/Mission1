<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.net.URL" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="DataBase.ConnectionDb" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Statement" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 구하기</title>
<style>
 table {
    width: 100%;
    border: 1px solid #444444;
    border-collapse: collapse;
  }
  th, td {
    border: 1px solid #444444;
    padding: 10px;
  }
  th{background-color:teal; color:white;}
  a:nth-child(n+2)::before {
    content: "|";
    margin-right: 10px;
}
</style>
</head>
<body>
 <script>
    function seelctNearWifi() {
    	 const xCoordInput = document.querySelector('input[name="LNT:"]');
    	 const yCoordInput = document.querySelector('input[name="LAT:"]');
    	 if (xCoordInput.value == 0 || yCoordInput.value == 0) {
    		 console.log("내 위치를 먼저 조회해주세요.");
    		 return;	 
    	 }
    		 
    	 
    	 const queryUrl = 'Mission1/GetWifiListServlet?query=selectNearWifi&xCoord=' + encodeURIComponent(xCoordInput.value) 
    			 			+ '&yCoord=' + encodeURIComponent(yCoordInput.value);

    		 fetch(queryUrl)
              .then(response => response.json())
              .then(data => {
                  // 서버로부터 받은 데이터를 처리하여 테이블에 추가하는 부분
                  const wifiList = data.wifiList;
                  const table = document.getElementById('wifiTable');
                  table.innerHTML = ''; // 기존 테이블 내용 초기화

                  for (const wifi of wifiList) {
                	    const row = table.insertRow();
                	    const distanceCell = row.insertCell();
                	    const mgrNoCell = row.insertCell();
                	    const jachiguCell = row.insertCell();
                	    const wifiNmCell = row.insertCell();
                	    const doromyungAddrCell = row.insertCell();
                	    const sangseAddrCell = row.insertCell();
                	    const instlFloorCell = row.insertCell();
                	    const instlTyCell = row.insertCell();
                	    const instlMbyCell = row.insertCell();
                	    const svcSeCell = row.insertCell();
                	    const mangTypeCell = row.insertCell();
                	    const instlYearCell = row.insertCell();
                	    const inOutDoorCell = row.insertCell();
                	    const wifiEnvCell = row.insertCell();
                	    const xCoordCell = row.insertCell();
                	    const yCoordCell = row.insertCell();
                	    const workDttmCell = row.insertCell();

                	    distanceCell.textContent = wifi.distance;
                	    mgrNoCell.textContent = wifi.mgrNo;
                	    jachiguCell.textContent = wifi.jachigu;
                	    wifiNmCell.textContent = wifi.wifiNm;
                	    doromyungAddrCell.textContent = wifi.doromyungAddr;
                	    sangseAddrCell.textContent = wifi.sangseAddr;
                	    instlFloorCell.textContent = wifi.instlFloor;
                	    instlTyCell.textContent = wifi.instlTy;
                	    instlMbyCell.textContent = wifi.instlMby;
                	    svcSeCell.textContent = wifi.svcSe;
                	    mangTypeCell.textContent = wifi.mangType;
                	    instlYearCell.textContent = wifi.instlYear;
                	    inOutDoorCell.textContent = wifi.inOutDoor;
                	    wifiEnvCell.textContent = wifi.wifiEnv;
                	    xCoordCell.textContent = wifi.xCoord;
                	    yCoordCell.textContent = wifi.yCoord;
                	    workDttmCell.textContent = wifi.workDttm;
                  }
              })
              .catch(error => {
            	    console.error('Error occurred:', error);
            	});
    	}
    
   	  function getMylocation() {
   		if (navigator.geolocation) {
   		    navigator.geolocation.getCurrentPosition(showPosition);
   		  } else {
   		    console.log("Geolocation is not supported by this browser.");
   		  }
   	  }
   	  
   	  function showPosition(position) {
   	    const xCoordInput = document.querySelector('input[name="LNT:"]');
   	    const yCoordInput = document.querySelector('input[name="LAT:"]');
      
   	    const latitude = position.coords.latitude;
   	    const longitude = position.coords.longitude;
      
   	    xCoordInput.value = latitude;
   	    yCoordInput.value = longitude;
   	  }
   </script>
   
	<h2>와이파이 정보 구하기</h2>
    <div>
        <a href="">홈</a>
        <a href="">위치 히스토리 목록</a>
        <a href="">Open API 와이파이 정보 가져오기</a>
    </div>
    <div style = "margin-top: 10px; margin-bottom: 10px;">
    	LNT:<input type="number" name="LNT:" value = 0.0>
        LAT:<input type="number" name="LAT:" value = 0.0>
        <button onclick="getMylocation()">내 위치 가져오기</button>
        <button onclick="seelctNearWifi()">내 위치 가져오기</button>
    </div>
    
<table id="wifiTable">
		<tr>
            <th>거리(Km)</th>
            <th>관리번호</th>
            <th>자치구</th>
            <th>와이파이명</th>
            <th>도로명주소</th>
            <th>상세주소</th>
            <th>설치위치(층)</th>
            <th>설치유형</th>
            <th>설치기관</th>
            <th>서비스구분</th>
            <th>망종류</th>
            <th>설치년도</th>
            <th>실내외구분</th>
            <th>WIFI접속환경</th>
            <th>X좌표</th>
            <th>Y좌표</th>
            <th>작업일자</th>
        </tr>
</table>
 
</body>
</html>