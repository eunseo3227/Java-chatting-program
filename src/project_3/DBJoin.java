package project_3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.*;

public class DBJoin implements MouseListener {
	JFrame frame;
	JPanel logPanel;
	JPanel logPanel1;
	JPanel logPanel2;
	JPanel logPanel3;
	JTextField idTf, pwTf, nameTf, birthTf = null;
	JButton joinBtn, checkBt;
    ImageIcon icon;
    Color c = new Color(212, 244, 250);	//배경색 추가(하늘)
    Color c1 = new Color(255, 216, 216);	//배경색 추가(핑크)
    
	MsgeBox msgbox = new MsgeBox();

	void JoinDBPanel() {
		
		frame = new JFrame("야옹 채팅 - 회원가입");
		logPanel = new JPanel();
		logPanel1 = new JPanel(new GridLayout(4, 1));
		logPanel2 = new JPanel(new GridLayout(4, 1));
		logPanel3 = new JPanel();
		logPanel.setBackground(c1);	//배경색 추가
		logPanel1.setBackground(c);	//배경색 추가
		logPanel2.setBackground(c);	//배경색 추가
		logPanel3.setBackground(c);	//배경색 추가
		//회원가입 창은 GridLayout 클래스를 사용했다. 배경색은 따로 없었지만 setBackround를 사용하여 직접 지정했다.
		
		icon = new ImageIcon("icon2.png");
		frame.setIconImage(icon.getImage());//ImageIcon과 frame.setIconImage를 사용하여 타이틀바에 내가 원하는 아이콘 이미지를 넣었다.
		
		//회원가입 창에는 ID, Password, 이름, 생년월일을 입력하는 칸을 추가했다.
		JLabel idLabel = new JLabel(" I D ", JLabel.CENTER);
		JLabel pwLabel = new JLabel(" P W ", JLabel.CENTER);
		JLabel nameLabel = new JLabel("이 름", JLabel.CENTER);
		JLabel baLabel = new JLabel("생 년 월 일 ", JLabel.CENTER);
		logPanel1.add(idLabel);
		logPanel1.add(pwLabel);
		logPanel1.add(nameLabel);
		logPanel1.add(baLabel);

		idTf = new JTextField(20);
		idTf.addMouseListener(this);
		pwTf = new JTextField(20);
		pwTf.addMouseListener(this);
		nameTf = new JTextField(20);
		nameTf.addMouseListener(this);
		birthTf = new JTextField("ex)001124", 20);
		birthTf.addMouseListener(this);
		logPanel2.add(idTf);
		logPanel2.add(pwTf);
		logPanel2.add(nameTf);
		logPanel2.add(birthTf);

		//checkBt라는 ID Check 버튼을 제작하여 해당 아이디가 기존에 존재하는지 확인한다. 
		//checkBt는 addMouseListener 이벤트를 사용하여 버튼을 클릭하면 해당 이벤트가 실행된다.
		checkBt = new JButton("ID Check");
		logPanel3.add(checkBt, BorderLayout.NORTH);
		checkBt.addMouseListener(this); // addMouseListener이벤트

		frame.add(logPanel, BorderLayout.NORTH);
		frame.add(logPanel1, BorderLayout.WEST);
		frame.add(logPanel2, BorderLayout.CENTER);
		frame.add(logPanel3, BorderLayout.EAST);

		//마지막으로 가입버튼을 누르면 “가입하시겠습니까?” 라는 창과 함께 가입 또는 취소 버튼을 누를 수 있다. 
		//가입을 누르면 성공적으로 가입이 된다.
		//취소버튼을 누르면 해당 frame만 종료시키기 위해 dispose() 메소드가 사용된다.
		JPanel logPanel4 = new JPanel();
		logPanel4.setBackground(c1);	//배경색 추가
		JLabel askLabel = new JLabel("가입하시겠습니까?");
		joinBtn = new JButton("가입");
		// joinBtn.setEnabled(false);
		JButton cancleBtn = new JButton("취소");
		joinBtn.addMouseListener(this); // addMouseListener이벤트
		logPanel4.add(askLabel);
		logPanel4.add(joinBtn);
		logPanel4.add(cancleBtn);
		frame.add(logPanel4, BorderLayout.SOUTH);

		// if((idTf.getText().isEmpty())==true ||
		// (pwTf.getText().isEmpty())==true){ //왜안되지???
		// joinBtn.setEnabled(true);
		// }

		// 취소 버튼
		cancleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				dbClose();
			}
		});

		frame.setBounds(450, 250, 350, 200);  
		//setBounds() 메소드는 setBounds(가로위치, 세로위치, 가로길이, 세로길이);로 구성되어 있고 해당 frame의 위치를 결정한다.
		frame.setResizable(false);
		//setResizable(false);는 창의 크기를 조절할 수 없도록 하는 것이다.
		frame.setVisible(true);
		//setVisible() 메소드는 창을 화면에 나타낼 것인지 설정하는 것이다.
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 살리면 로그인창도 함께
		// 사라진다

	}// JoinDBPanel() end
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:oracle:thin:@localhost:1521:system";
	//DBMS의 연결에 대한 url이다. localhost는 오라클이 설치된 IP Address이고, 
	//1521은 오라클 데이터베이스의 서버 포트이다. system은 오라클에 만들어 놓은 데이터베이스 이름이다.
	String sql = null;
	Properties info = null;
	Connection cnn = null;

	@Override
	//mouseClicked는 MouseEvent e를 받아와 마우스가 클릭할 시 해당 기능 실행되도록 하는 함수이다.
	public void mouseClicked(MouseEvent e) {

		//if문을 활용하여 TextField를 클릭하면 기존에 입력되어 있던 예시를 지워준다.
		if (e.getSource().equals(idTf)) {
			idTf.setText("");
		} else if (e.getSource().equals(pwTf)) {
			pwTf.setText("");
		} else if (e.getSource().equals(nameTf)) {
			nameTf.setText("");
		} else if (e.getSource().equals(birthTf)) {
			birthTf.setText("");
		}

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//오라클 드라이버를 로드하는 코드이다. JDBC를 이용해서 데이터베이스를 연결할 때 사용한다. 
			//데이터베이스에 연결하기 전에 각각의 데이터베이스에 맞는 드라이버를 로드해야 한다. 
			//드라이버를 로드하기 위해서 아래와 같이 Class 클래스의 forName 메소드를 사용해야 한다.
			info = new Properties();
			info.setProperty("user", "system");
			//데이터를 저장하는 코드이다. “user”는 저장 또는 검색을 위한 값이고, “system”은 저장하고자 하는 문자열(text) 데이터이다.
			info.setProperty("password", "j327532");
			cnn = DriverManager.getConnection(url, info); // 연결할 정보를 가지고있는 드라이버매니저를 던진다
			//드라이버를 로드하고 DriverManager.getConnection() 메소드와 URL을 이용해서 연결을 설정하게 된다.
			stmt = cnn.createStatement();
			//데이터베이스로 SQL문을 보내기 위한 SQLServerStatement 개체를 만드는 것이다.

			// 테이블이 생성
			/*
			sql =
			"create table joinDB(id varchar2(20) primary key,pw varchar2(20) not null,name varchar2(30),barth number(6))"
			; stmt.execute(sql); System.out.println("테이블생성완료");
			*/

			// id 체크 버튼을 누르면 실행된다.
			if (e.getSource().equals(checkBt)) {
				sql = "select * from joinDB where id='" + idTf.getText() + "'";
				rs = stmt.executeQuery(sql);
				//java에서 조회한 결과값을 출력한다. 이 쿼리를 실행하면 ResultSet 타입으로 반환을 해주어 결과값을 저장할 수 있다.

				if (rs.next() == true || (idTf.getText().isEmpty()) == true) { //위의 코드를 이용하여 이미 id가 존재한다면 해당 id는 사용할 수 없다는 멘트가 나온다.
					msgbox.messageBox(logPanel3, "해당 ID는 사용이 불가능합니다. 다시 작성해주세요.");
				} else {
					msgbox.messageBox(logPanel3, "사용 가능한 ID 입니다.");
				}
			}

			// 가입 버튼
			if (e.getSource().equals(joinBtn)) {
				sql = "select * from joinDB where id='" + idTf.getText() + "'";

				rs = stmt.executeQuery(sql);
				//id 체크 버튼과 마찬가지로 rs = stmt.executeQuery(sql);을 활용하여 ResultSet 타입으로 반환을 해주어 결과값을 저장한다.

				if (rs.next() == true) { // 이미 id가 존재한다면
					msgbox.messageBox(logPanel3, "ID Check가 필요합니다.");

				} else if ((idTf.getText().isEmpty()) == true || (pwTf.getText().isEmpty()) == true
						|| (nameTf.getText().isEmpty()) || (birthTf.getText().isEmpty())) {		// id 혹은 pw 비어있을경우
					msgbox.messageBox(logPanel3, "비어있는 칸이 존재합니다.");
				} else if ((birthTf.getText().length()) != 6) {
					msgbox.messageBox(logPanel3, "생년월일 서식이 잘못되었습니다."); 	// 아닌경우
				} else {

					sql = "insert into joinDB values ('" + idTf.getText() + "','" + pwTf.getText() + "','"
							+ nameTf.getText() + "','" + birthTf.getText() + "')";
					stmt.executeUpdate(sql);
					msgbox.messageBox(logPanel3, "축하합니다.가입 되셨습니다.");
					frame.dispose(); // 창 닫기
					dbClose();
				}
				//위의 코드를 이용하여 이미 id가 존재한다면 ID Check가 필요하다는 메시지가 뜨도록 한다. 그 외에도 비어있는 칸이 있거나, 
				//생년월일 서식이 잘못된 경우 사용자가 수정할 수 있도록 경고 메시지가 뜬다.
				//형식에 잘 맞게 회원가입을 시도하였다면 sql 문을 통해 아이디, 비밀번호, 이름, 생년월일을 joinDB에 추가한다. 
				//그리고 가입 완료 메시지가 뜨도록 한다.
			}
		} catch (Exception ee) {
			System.out.println("문제있음");
			ee.printStackTrace();
		}
	}// mouseClicked 이벤트 end

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

}// class end
