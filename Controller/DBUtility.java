package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DBUtility {

	public static Connection getConnection() {
		Connection con=null;
		try {
			//1. Mysql database class load
			Class.forName("com.mysql.jdbc.Driver");
			//2. 주소, ID, PW를 통해서 접속요청
			con= DriverManager.getConnection("jdbc:mysql://192.168.0.190/bookdb", "root", "123456");
		} catch (Exception e) {
			e.printStackTrace();
			MainController.callAlert("연결 실패 : 데이타베이스 연결 문제가 있습니다.");
			return null;
		}
		return con;
	}
	
}
