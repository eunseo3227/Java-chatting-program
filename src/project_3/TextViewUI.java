package project_3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextViewUI extends JFrame {
	
	final int FRAME_WIDTH  = 500;
	final int FRAME_HEIGHT = 600; 
	// final 상수를 사용하여 프레임의 높이와 너비를 설정한다.
	JTextArea taChatText;
	
	public TextViewUI() {
		setTitle("채팅내용확인\n"); // setTitle을 사용하여 프레임의 제목을 “채팅내용확인”으로 설정한다.
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 프레임의 오른쪽 상단 x 버튼을 누르면 해당 프레임을 종료한다.
		setLayout(new BorderLayout()); // 프레임의 레이아웃은 BorderLayout으로 설정한다.
		setSize(500, 600); // 프레임의 크기를 500, 600으로 설정한다.
		
		// 채팅 내용이 출력될 패널을 추가한다. 그리고 텍스트 영역을 만들기 위해 JTextArea를 이용하여 taChatText를 만든다. taChatText의 배경색은 하얀색으로 지정한다.
		JPanel pnlCenter = new JPanel(new BorderLayout());
		taChatText = new JTextArea();
		taChatText.setBackground(Color.WHITE);
//		taChatText.setText("글자가 표시되는가?");
//		pnlCenter.add(taChatText, BorderLayout.CENTER);
	
		
		
		// 스크롤 페인(scroll pane)은 컴포넌트에 스크롤 기능을 제공한다. scrollPane은 BorderLayout에 센터에 부착한다. 
		// Scrollbar.VERTICAL과 Scrollbar.HORIZONTAL은  스크롤바의 종류이고 AS_NEEDED는 필요할 때만 스크롤 바가 보이도록 설정하는 것이고, 
		// NEVER는 스크롤바가 보이지 않게 하는 것이다. pnlCenter도 BorderLayout에 센터에 부착한다.
		JScrollPane spChatText = new JScrollPane(taChatText, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pnlCenter.add(spChatText, BorderLayout.CENTER);
		
		add(pnlCenter, BorderLayout.CENTER);
		
		
		// Toolkit 클래스의 static 메소드들은 native operation system을 직접 조회함으로써 원하는 내용을 바인딩(Binding)시켜 줄 수 있다. 
		// getDefaultToolkit을 사용하여 디폴트의 툴킷을 리턴한다. Dimension 클래스는 클래스는 특정 영역의 사각형과 폭과 높이의 값을 관리할 수 있도록 도와주는 클래스이다. 
		// getScreenSize는 실제 화면 크기를 구한다. d.heigt와 d.width를 사용하여 높이와 폭을 screenHeigt와 screenWidth에 넣는다.
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth  = d.width;
		
		setLocation((screenWidth - FRAME_WIDTH) / 2, (screenHeight - FRAME_HEIGHT) / 2);
		setVisible(true);
		// setLocation을 사용하여 프레임창이 나타날 위치를 지정한다. 그리고 setVisible(true)를 사용하여 창을 출력한다.
	}
	
	public TextViewUI(String text) {
		this();
		taChatText.setText(text+"\n");
	}
	public TextViewUI(JTextArea jta) {
		this();
		taChatText.setText(jta.getText());
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TextViewUI();
	}

}
