package project_3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DBRevise implements MouseListener {

	String id = null;
	String pw, name, birth;

	JFrame frame;
	JPanel logPanel;
	JPanel logPanel1;
	JPanel logPanel2;
	JPanel logPanel3;
	JTextField idTf, pwTf, nameTf, birthTf = null;
	JButton okBtn;

	MsgeBox msgbox = new MsgeBox();

	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:oracle:thin:@localhost:1521:system";
	//위의 코드는 DBMS의 연결에 대한 url이다. localhost는 오라클이 설치된 IP Address이고, 
	//1521은 오라클 데이터베이스의 서버 포트이다. system은 오라클에 만들어 놓은 데이터베이스 이름이다.
	String sql = null;
	String sql2 = null;
	Properties info = null;
	Connection cnn = null;

	//사용자의 id를 받아와서 그것의 정보로 pw/name/birth 수정 및 삭제하기 위한 함수이다.
	void myInfo(String id) {
		this.id = id;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//오라클 드라이버를 로드하는 코드이다. JDBC를 이용해서 데이터베이스를 연결할 때 사용한다. 데이터베이스에 연결하기 전에 각각의 데이터베이스에 맞는 드라이버를 로드해야 한다. 
			//드라이버를 로드하기 위해서 아래와 같이 Class 클래스의 forName 메소드를 사용해야 한다.
			info = new Properties();
			info.setProperty("user", "system");
			info.setProperty("password", "j327532");
			//데이터를 저장하는 코드이다. “user”와 “password”는 저장 또는 검색을 위한 값이고, “system”과 “j327532”은 저장하고자 하는 문자열(text) 데이터이다.
			cnn = DriverManager.getConnection(url, info);
			//드라이버를 로드하고 DriverManager.getConnection() 메소드와 URL을 이용해서 연결을 설정하게 된다.
			stmt = cnn.createStatement();
			//데이터베이스로 SQL문을 보내기 위한 SQLServerStatement 개체를 만드는 것이다.

			sql = "select * from joinDB where id='" + id + "'";
			rs = stmt.executeQuery(sql);
			//java에서 조회한 결과값을 출력한다. 이 쿼리를 실행하면 ResultSet 타입으로 반환을 해주어 결과값을 저장할 수 있다.

			while (rs.next() == true) { //수정을 위해 id를 토대로 id 다음에 저장되어 있는 pw, name, birth 값을 찾아서 저장한다.
				pw = rs.getString(2);
				name = rs.getString(3);
				birth = rs.getString(4);
			}
		} catch (Exception ee) {
			System.out.println("문제있음");
			ee.printStackTrace();
		}

		frame = new JFrame("회원수정");
		logPanel = new JPanel();
		logPanel1 = new JPanel(new GridLayout(4, 1));
		logPanel2 = new JPanel(new GridLayout(4, 1));
		logPanel3 = new JPanel();

		JLabel idLabel = new JLabel(" I   D   ", JLabel.CENTER);
		JLabel pwLabel = new JLabel(" P  W  ", JLabel.CENTER);
		JLabel nameLabel = new JLabel("이 름", JLabel.CENTER);
		JLabel baLabel = new JLabel("생 년 월 일 ", JLabel.CENTER);
		logPanel1.add(idLabel);
		logPanel1.add(pwLabel);
		logPanel1.add(nameLabel);
		logPanel1.add(baLabel);

		idTf = new JTextField(20);
		idTf.setText(id);
		idTf.setEditable(false);
		pwTf = new JTextField(20);
		pwTf.setText(pw);
		nameTf = new JTextField(20);
		nameTf.setText(name);
		birthTf = new JTextField(20);
		birthTf.setText(birth);
		logPanel2.add(idTf);
		logPanel2.add(pwTf);
		logPanel2.add(nameTf);
		logPanel2.add(birthTf);

		frame.add(logPanel, BorderLayout.NORTH);
		frame.add(logPanel1, BorderLayout.WEST);
		frame.add(logPanel2, BorderLayout.CENTER);
		frame.add(logPanel3, BorderLayout.EAST);

		JPanel logPanel4 = new JPanel();
		JLabel askLabel = new JLabel("변경하시겠습니까?");
		okBtn = new JButton("확인");
		JButton cancleBtn = new JButton("취소");
		okBtn.addMouseListener(this); 		// addMouseListener이벤트
		logPanel4.add(askLabel);
		logPanel4.add(okBtn);
		logPanel4.add(cancleBtn);
		frame.add(logPanel4, BorderLayout.SOUTH);

		// 취소 버튼
		cancleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				dbClose();
			}
		});

		frame.setBounds(450, 250, 350, 200);
		frame.setResizable(false);
		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			//확인 버튼
			//아이디, 비밀번호, 이름, 생일을 변경할 때 칸이 비었는지 확인한다.
			if (e.getSource().equals(okBtn)) {
				if ((idTf.getText().isEmpty()) == true || (pwTf.getText().isEmpty()) == true
						|| (nameTf.getText().isEmpty()) || (birthTf.getText().isEmpty())) {
					msgbox.messageBox(logPanel3, "비어있는 칸이 존재합니다.");
				} else if ((birthTf.getText().length()) != 6) { //생년월일 서식(6글자)가 맞는지 확인한다
					msgbox.messageBox(logPanel3, "생년월일 서식이 잘못되었습니다.");
				} else { //아무런 이상이 없는 경우 새로운 정보로 업데이트한다.
					sql = "update joinDB set pw='" + pwTf.getText() + "',name='" + nameTf.getText() + "',birth='"
							+ birthTf.getText() + "' where id='" + id + "'";
					System.out.println(sql);
					stmt.executeUpdate(sql);
					msgbox.messageBox(logPanel3, "변경 되셨습니다.");
					frame.dispose(); // 창 닫기
					dbClose();
				}
			}

		} catch (Exception ee) {
			System.out.println("문제있음");
			ee.printStackTrace();
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public void dbClose() {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (cnn != null)
				cnn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
