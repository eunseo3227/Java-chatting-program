package project_3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ServerAddress extends JFrame {

	JButton confirmBtn;
	JTextField ipText;
	LoginUI loginUI;
    ImageIcon icon;
    Color c = new Color(178, 235, 244);	//배경색 추가

    // ServerAddress 생성자를 만든다. LoginUI를 매개변수로 전달 받는다. 그리고 initialize();를 입력하여 메소드를 실행시킨다.
	public ServerAddress(LoginUI loginUI) {
		this.loginUI = loginUI;
		initialize();
	}

	private void initialize() {
		
		icon = new ImageIcon("icon2.png");
		this.setIconImage(icon.getImage());// 타이틀 바에 이미지를 넣기 위해 ImageIcon을 사용한다. 그리고 setIconImage를 사용하여 이미지를 넣는다.
		setTitle("서버 아이피 주소 입력"); // 타이틀 제목을 “서버 아이피 주소 입력”으로 설정한다.
		setBounds(100, 100, 306, 95); // setBounds() 메소드는 창 크기를 조절하는 메소드로 setBounds(가로위치, 세로위치, 가로길이, 세로길이);로 구성되어 있다.
		setDefaultCloseOperation(EXIT_ON_CLOSE); // 프레임의 오른쪽 상단 x 버튼을 누르면 해당 프레임을 종료한다.
		getContentPane().setLayout(null); // 레이아웃을 설정하는 것이다.

		JPanel panel = new JPanel();
		panel.setBounds(12, 10, 266, 37);
		getContentPane().add(panel);
		getContentPane().setBackground(c);	//배경색 추가
		panel.setLayout(new BorderLayout(0, 0)); // 텍스트 크기
		// 텍스트를 입력하는 패널이다. 해당 패널의 위치, 너비, 높이를 지정하고 해당 패널을 컨텐츠팬에 붙인다. 
		// 배경색을 지정하고 배치관리자를 이용하여 배치 방법을 지정한다. 채팅 패널은 BorderLayout으로 설정하였다.

		// ip를 입력할 수 있는 JTextField이다. addKeyListener를 사용하여 엔터키 눌렀을 때 ipText 내용이 전송되도록 설정한다. setVisible(true);를 이용하여 창을 화면에 나타낸다. 
		// dispose();를 이용하여 현재 프레임을 종료시킵니다. 그리고 requestFocus();를 사용하여 해당 컴포넌트로부터 먼저 키를 입력받는다.
		ipText = new JTextField();
		ipText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					loginUI.ipBtn.setText(ipText.getText());
					loginUI.setVisible(true);
					dispose();
					loginUI.idText.requestFocus();
				}
			}
		});
		ipText.setText("172.30.1.2");
		// ipText를 "172.30.1.2"로 설정하고 BorderLayout에 가운데에 붙인다. 그리고 setColumns를 사용하여 텍스트 필드의 최대 입력 개수를 설정한다.
		panel.add(ipText, BorderLayout.CENTER);
		ipText.setColumns(10);

		// 확인 버튼을 만든다. 확인 버튼을 클릭하면 addMouseListener를 사용하여 확인 버튼이 클릭되면 ipText 내용이 전송된다. 
		// setVisible(true);를 이용하여 창을 화면에 나타낸다. dispose();를 이용하여 현재 프레임을 종료시킵니다. 
		// requestFocus();를 사용하여 해당 컴포넌트로부터 먼저 키를 입력받는다. 그리고 확인 버튼을 BorderLayout에 동쪽에 붙이고 setVisible(true);를 이용하여 창을 화면에 나타낸다.
		confirmBtn = new JButton("확인");
		confirmBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loginUI.ipBtn.setText(ipText.getText());
				loginUI.setVisible(true);
				dispose();
				loginUI.idText.requestFocus();
			}
		});
		panel.add(confirmBtn, BorderLayout.EAST);
		setVisible(true);
	}

}