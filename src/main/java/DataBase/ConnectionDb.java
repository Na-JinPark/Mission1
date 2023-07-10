package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDb {

	private static Connection conn = null;
	
	public static void closeConnection() {
		if(conn != null) {
			try {
				conn.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static Connection getConnection() {
		if(conn == null) {
			try {
				String url = "jdbc:sqlite:Mission.db"; // 데이터베이스 파일 경로
		        conn = DriverManager.getConnection(url);
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
}
