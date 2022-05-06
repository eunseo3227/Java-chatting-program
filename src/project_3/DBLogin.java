package project_3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DBLogin {

	String id = null;
	String pw = null;

	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:oracle:thin:@localhost:1521:system";
	//위의 코드는 DBMS의 연결에 대한 url이다. localhost는 오라클이 설치된 IP Address이고, 1521은 오라클 데이터베이스의 서버 포트이다. 
	//system은 오라클에 만들어 놓은 데이터베이스 이름이다.
	String sql = null;
	Properties info = null;
	Connection cnn = null;

	int checkIDPW(String id, String pw) { //ID Password를 DB에서 불러와서 비교를 하여 ID와 PW가 일치한다면 로그인에 성공하도록 한다.
		this.id = id;
		this.pw = pw;
		int result = 1;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//위의 코드는 오라클 드라이버를 로드하는 코드이다. JDBC를 이용해서 데이터베이스를 연결할 때 사용한다. 데이터베이스에 연결하기 전에 각각의 데이터베이스에 맞는 드라이버를 로드해야 한다. 
			//드라이버를 로드하기 위해서 아래와 같이 Class 클래스의 forName 메소드를 사용해야 한다.
			info = new Properties();
			info.setProperty("user", "system");
			//데이터를 저장하는 코드이다. “user”는 저장 또는 검색을 위한 값이고, “system”은 저장하고자 하는 문자열(text) 데이터이다.
			info.setProperty("password", "j327532");
			cnn = DriverManager.getConnection(url, info); // 연결할 정보를 가지고있는 드라이버매니저를 던진다
			stmt = cnn.createStatement();
			//위의 코드는 데이터베이스로 SQL문을 보내기 위한 SQLServerStatement 개체를 만드는 것이다.

			sql = "select * from joinDB where id='" + id + "'";
			rs = stmt.executeQuery(sql);
			//java에서 조회한 결과값을 출력한다. 이 쿼리를 실행하면 ResultSet 타입으로 반환을 해주어 결과값을 저장할 수 있다. 아이디가 존재하는지 확인하고 비교하기 위해 아이디를 불러온다.

			if (rs.next() == false || (id.isEmpty()) == true) { //위 코드를 활용하여 id가 존재하고 있는지 확인한다.
				result = 1;
			} else {
				sql = "select * from (select * from joinDB where id='" + id + "')";
				rs = stmt.executeQuery(sql);
				while (rs.next() == true) { //테이블에서 해당 id를 찾고 id 다음 값이 입력된 pw와 같은지 비교를 한다. 같으면 로그인 성공이고, 아이디는 같고 pw가 다른 경우 1을 리턴한다.
					if (rs.getString(2).equals(pw)) { // pw와 같은지 비교
						result = 0; 		// 같으면 로그인 성공
					} else {				// 아이디는같고 pw가 다른경우
						result = 1;
					}
				}
			}
		} catch (Exception ee) { //예외처리를 하여 문제가 생기면 “문제있음”이 출력되도록 한다.
			System.out.println("문제있음");
			ee.printStackTrace();
		}
		return result;
	}
}
