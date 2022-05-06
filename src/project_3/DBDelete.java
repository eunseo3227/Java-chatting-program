package project_3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class DBDelete {
	MsgeBox msgbox = new MsgeBox();

	String id = null;

	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:oracle:thin:@localhost:1521:system";
	//위의 코드는 DBMS의 연결에 대한 url이다. localhost는 오라클이 설치된 IP Address이고, 1521은 오라클 데이터베이스의 서버 포트이다. 
	//system은 오라클에 만들어 놓은 데이터베이스 이름이다.
	String sql = null;
	String sql2 = null;
	Properties info = null;	//이 코드는 속성데이터(문자열, text)를 담는 클래스이다.
	Connection cnn = null;

	// id를 받아와서 그것의 정보로 pw/name/birth 삭제
	int InfoDel(String id) {
		int result = 0;
		this.id = id;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 알아서 들어간다..conn로
			//위의 코드는 오라클 드라이버를 로드하는 코드이다. JDBC를 이용해서 데이터베이스를 연결할 때 사용한다. 
			//데이터베이스에 연결하기 전에 각각의 데이터베이스에 맞는 드라이버를 로드해야 한다. 드라이버를 로드하기 위해서 아래와 같이 Class 클래스의 forName 메소드를 사용해야 한다.
			info = new Properties();
			info.setProperty("user", "system");	//데이터를 저장하는 코드이다. “user”는 저장 또는 검색을 위한 값이고, “system”은 저장하고자 하는 문자열(text) 데이터이다.
			info.setProperty("password", "j327532");
			cnn = DriverManager.getConnection(url, info); // 연결할 정보를 가지고있는 드라이버매니저를 던진다
			//위의 코드를 통해 드라이버를 로드하고 DriverManager.getConnection() 메소드와 URL을 이용해서 연결을 설정하게 된다.
			stmt = cnn.createStatement();	//이 코드는 데이터베이스로 SQL문을 보내기 위한 SQLServerStatement 개체를 만드는 것이다.

			sql = "delete from joinDB where id='" + id + "'";
			//ID가 같은 정보를 받아와 해당 pw와 name, birth 데이터를 삭제한다.
			stmt.executeUpdate(sql);
			//executeUpdate 메서드는 데이터베이스에서 데이터를 추가(Insert), 삭제(Delete), 수정(Update)하는 SQL문을 실행한다. 
			//메소드의 반환 값은 해당 SQL문 실행에 영향을 받는 행 수를 반환한다.

			sql = "select * from joinDB where id='" + id + "'";
			rs = stmt.executeQuery(sql);
			//executeQuery 메서드는 데이터베이스에서 데이터를 가져와서 결과 집합을 반환한다. 이 메서드는 Select 문에서만 실행하는 특징이 있다.
			if (rs.next() == true) { // 다음값의
				result = 0; // 실패
			} else {
				result = 1; // 성공
			}
		} catch (Exception ee) {
			System.out.println("문제있음");
			ee.printStackTrace();
		}

		return result;
	}

}
