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

	static final int PORT = 5550; // ���� ���α׷��� ��Ʈ��ȣ�̴�.
	Socket socket;
	ServerSocket serverSocket; // ���� ������ �����ϱ� ���� ���� �����̴�.
	DataOutputStream dos;
	DataInputStream dis;
	ArrayList<User> userArray;
	ArrayList<Room> roomArray;
	ArrayList<User> friendArray; // ģ�� ���
	// ������ ������ ����ڵ�� ������ ������� ä�ù�� ����Ʈ�� ��Ÿ����. ArrayList�� List �������̽��� ��ӹ��� Ŭ������ ũ�Ⱑ ���������� ���ϴ� ���� ����Ʈ�̴�.


	int sizeX = 600, sizeY = 600;
	Dimension whole, part;
	int xPos, yPos;
	JTextArea jta;
    ImageIcon icon;
	JPanel jp;

	EightServer() {
		userArray = new ArrayList<User>();
		roomArray = new ArrayList<Room>();
		setTitle("�߿� ä�� ����");
		setSize(sizeX, sizeY);

		icon = new ImageIcon("icon2.png");
		this.setIconImage(icon.getImage());//Ÿ��Ʋ�ٿ� �̹����ֱ�
		
		jta = new JTextArea();
		jp = new JPanel();

		jp.setLayout(new GridLayout(1, 2)); // �׸��� ���̾ƿ�
		jta.setEditable(false);
		jta.setLineWrap(true);
		//setEditable�� ������ �� �� ���� �ϴ� ���̰� setLineWrap�� �ڵ� �ٹٲ��� ���ִ� ����̴�.

		JScrollPane jsp = new JScrollPane(jta); // �ؽ�Ʈ���� ��ũ�� �߰�
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jp.add(jsp);// �гο� ��ũ�� ����
		jta.setText("Server Start...1\n");

		add(jp); // �����ӿ� �г� ����

		// ������ ��ġ ���
		whole = Toolkit.getDefaultToolkit().getScreenSize();
		part = this.getSize();
		xPos = (int) (whole.getWidth() / 2 - part.getWidth() / 2);
		yPos = (int) (whole.getHeight() / 2 - part.getHeight() / 2);

		setLocation(xPos, yPos);
		// ������ ��ġ�� �����ϱ� ���� xPos�� yPos�� ����ϰ� �ش� ��ġ�� �����츦 ��Ÿ������ �Ѵ�,
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		// Create server UI
		System.out.println("Server start...2");
		EightServer server = new EightServer();
		Thread thread = new Thread(server);
		thread.start();
		// ���� UI�� �����ϰ� ���� ������ �����ϱ� ���� ������Ʋ �����Ų��.
	}

	@Override
	public void run() {
		// Ŭ���̾�Ʈ ��� ���

		// ���� ȣ��Ʈ �ּҸ� �̿��Ͽ� ���� ������ �����Ѵ�.
		try {
			InetAddress addr = InetAddress.getLocalHost(); // ����ȣ��Ʈ �ּ�
			serverSocket = new ServerSocket(PORT); // �������� ����
			jta.append(PORT + "�� ��Ʈ�� ���������� ������ �����Ǿ����ϴ�.\n" + "���� ���� ������ IP �ּҴ� " 
							+ addr.getHostAddress().toString() + "�Դϴ�. \n");
		} catch (IOException e1) {
			e1.printStackTrace();
			jta.append("���� ���� ��������\n");
		}

		while (true) {
			socket = null;
			dis = null;
			dos = null;
			try {
				// ���ѹݺ�, ����� ������ ���ų� ���α׷��� ����� ������ ����
				socket = serverSocket.accept(); // Ŭ���̾�Ʈ ���� ���
				jta.append("Ŭ���̾�Ʈ " + socket.getInetAddress().getHostAddress()	+ "�� ���ӵǾ����ϴ�.\n");

			} catch (IOException e) {
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				jta.append("Ŭ���̾�Ʈ ���ӿ���\n");
			}
			try {
				// ������ �Ǹ� ����ȴ�. ������ ������� ���� ��Ʈ�� ��ü�� �����Ѵ�. DataInputStream�� ������ �а� ���� Ŭ�����̴�,
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				try {
					dis.close();
					dos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					jta.append("��Ʈ�� ��������\n");
				}
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					jta.append("���� ��������\n");
				}
				jta.append("��Ʈ�� ��������\n");
			}
			User person = new User(dis, dos); // ������ ����� ��ü�� �����ϰ� ������ ���� �ּҸ� �ο��Ѵ�.
			person.setIP(socket.getInetAddress().getHostName()); // �������ּ� ���� �ο�

			Thread thread = new Thread(new ServerThread(jta, person, userArray,	roomArray, friendArray));
			thread.start(); // ������ ����
		}
	}
}
