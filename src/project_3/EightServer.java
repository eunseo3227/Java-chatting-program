package project_3;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

// To create UI and listen to client access
public class EightServer extends JFrame implements Runnable {

	static final int PORT = 5550; // 서버 프로그램의 포트번호이다.
	Socket socket;
	ServerSocket serverSocket; // 서버 소켓을 생성하기 위한 서버 소켓이다.
	DataOutputStream dos;
	DataInputStream dis;
	ArrayList<User> userArray;
	ArrayList<Room> roomArray;
	ArrayList<User> friendArray; // 친구 목록
	// 서버에 접속한 사용자들과 서버가 열어놓은 채팅방들 리스트를 나타낸다. ArrayList는 List 인터페이스를 상속받은 클래스로 크기가 가변적으로 변하는 선형 리스트이다.


	int sizeX = 600, sizeY = 600;
	Dimension whole, part;
	int xPos, yPos;
	JTextArea jta;
    ImageIcon icon;
	JPanel jp;

	EightServer() {
		userArray = new ArrayList<User>();
		roomArray = new ArrayList<Room>();
		setTitle("야옹 채팅 서버");
		setSize(sizeX, sizeY);

		icon = new ImageIcon("icon2.png");
		this.setIconImage(icon.getImage());//타이틀바에 이미지넣기
		
		jta = new JTextArea();
		jp = new JPanel();

		jp.setLayout(new GridLayout(1, 2)); // 그리드 레이아웃
		jta.setEditable(false);
		jta.setLineWrap(true);
		//setEditable은 편집을 할 수 없게 하는 것이고 setLineWrap은 자동 줄바꿈을 해주는 기능이다.

		JScrollPane jsp = new JScrollPane(jta); // 텍스트에어리어에 스크롤 추가
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jp.add(jsp);// 패널에 스크롤 붙임
		jta.setText("Server Start...1\n");

		add(jp); // 프레임에 패널 붙임

		// 윈도우 위치 계산
		whole = Toolkit.getDefaultToolkit().getScreenSize();
		part = this.getSize();
		xPos = (int) (whole.getWidth() / 2 - part.getWidth() / 2);
		yPos = (int) (whole.getHeight() / 2 - part.getHeight() / 2);

		setLocation(xPos, yPos);
		// 윈도우 위치를 설정하기 위해 xPos와 yPos를 계산하고 해당 위치에 윈도우를 나타나도록 한다,
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		// Create server UI
		System.out.println("Server start...2");
		EightServer server = new EightServer();
		Thread thread = new Thread(server);
		thread.start();
		// 서버 UI를 생성하고 서버 소켓을 생성하기 위해 스레드틀 실행시킨다.
	}

	@Override
	public void run() {
		// 클라이언트 대기 모드

		// 로컬 호스트 주소를 이용하여 서버 소켓을 생성한다.
		try {
			InetAddress addr = InetAddress.getLocalHost(); // 로컬호스트 주소
			serverSocket = new ServerSocket(PORT); // 서버소켓 생성
			jta.append(PORT + "번 포트로 정상적으로 소켓이 생성되었습니다.\n" + "현재 열린 서버의 IP 주소는 " 
							+ addr.getHostAddress().toString() + "입니다. \n");
		} catch (IOException e1) {
			e1.printStackTrace();
			jta.append("서버 소켓 생성에러\n");
		}

		while (true) {
			socket = null;
			dis = null;
			dos = null;
			try {
				// 무한반복, 입출력 에러가 나거나 프로그램이 종료될 때까지 실행
				socket = serverSocket.accept(); // 클라이언트 접속 대기
				jta.append("클라이언트 " + socket.getInetAddress().getHostAddress()	+ "가 접속되었습니다.\n");

			} catch (IOException e) {
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				jta.append("클라이언트 접속에러\n");
			}
			try {
				// 접속이 되면 실행된다. 데이터 입출력을 위한 스트림 객체를 생성한다. DataInputStream은 파일을 읽고 쓰는 클래스이다,
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				try {
					dis.close();
					dos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					jta.append("스트림 해제에러\n");
				}
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					jta.append("소켓 해제에러\n");
				}
				jta.append("스트림 생성에러\n");
			}
			User person = new User(dis, dos); // 가명의 사용자 객체를 생성하고 아이피 설정 주소를 부여한다.
			person.setIP(socket.getInetAddress().getHostName()); // 아이피주소 설정 부여

			Thread thread = new Thread(new ServerThread(jta, person, userArray,	roomArray, friendArray));
			thread.start(); // 스레드 시작
		}
	}
}
