package project_3;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

public class EightClient implements Runnable {

	static int PORT = 5550; // ������Ʈ��ȣ
	static String IP = "localhost"; // �����������ּ�
	Socket socket; // ����
	User user; // �����

	LoginUI login;
	WaitRoomUI waitRoom;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	boolean ready = false;

	EightClient() { //�α��� UI�� �����ϰ� �����带 �����Ų��.
        login = new LoginUI(this);
		
		Thread thread = new Thread(this);
		thread.start();
	}

	public static void main(String[] args) {
		System.out.println("Client start...1");
		new EightClient();
		
	} // main end
	
	

	@Override
	public void run() {
		//
		// ���� ��� ����
		//
		while (!ready) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// ����ڰ� ��ü�� �����ϰ� �����Ǹ� �����Ѵ�.
		user = new User(dis, dos);
		user.setIP(socket.getInetAddress().getHostAddress());

		// �޽��� ����
		//readUTF()�� UTF-8 �������� �ڵ��� ���ڿ��� �д´�. readUTF�� ��밡 �Է����� ������ ��� ����Ѵ�. 
		//dataParsing �޼ҵ�� �����͸� �����ϴ� �޼ҵ�� ��ū�� �̿��� �и��Ѵ�.
		while (true) {
			try { 
				String receivedMsg = dis.readUTF(); // �޽��� �ޱ�(���)
				dataParsing(receivedMsg); // �޽��� �ؼ�
			} catch (IOException e) {
				e.printStackTrace();
				try {
					user.getDis().close();
					user.getDos().close();
					socket.close();
					break;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

		alarmMsg("�������α׷��� ���� ����Ǿ����ϴ�.");
		// ä�����α׷� ����
		waitRoom.dispose();
	}

	public boolean serverAccess() {
		if (!ready) {
			//������ ������ �̷������ ���� ���, ó�� ���� �ÿ� ����ȴ�. ������ �����ϱ� ���� ������ �����ϰ� ������ �ּҷ� ������ 3�ʵ��� �õ��Ѵ�.
			socket = null;
			IP = login.ipBtn.getText();
			try {
				// ��������
				InetSocketAddress inetSockAddr = new InetSocketAddress(InetAddress.getByName(IP), PORT);
				socket = new Socket();

				// ������ �ּҷ� ���� �õ� (3�ʵ���)
				socket.connect(inetSockAddr, 3000);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// ������ �Ǹ� ����ȴ�. �Է�, ��� ��Ʈ���� �����Ѵ�
			if (socket.isBound()) {
				// �Է�, ��� ��Ʈ�� ����
				try {
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				ready = true;
			}
		}
		return ready;
	}

	// �����͸� ����
	public synchronized void dataParsing(String data) {
		StringTokenizer token = new StringTokenizer(data, "/"); // �����͸� �����ϱ� ���� ��ū ����
		String protocol = token.nextToken(); // ��ū���� �зڵ� ��Ʈ���̴�. �׸��� switch���� �̿��Ͽ� ���� ����(/)���� ���е� ���ڿ�(��ū)�� ����Ѵ�.
		String id, pw, rNum, nick, rName, msg, result;
		System.out.println("���� ������ : " + data);

		switch (protocol) {
		case User.LOGIN: // �α���
			// ����ڰ� �Է���(������) ���̵�� �н�����
			result = token.nextToken();
			if (result.equals("OK")) {
				alarmMsg("�α��ο� �����߽��ϴ�!");
				nick = token.nextToken();
				login(nick);
			} else {
				msg = token.nextToken();
				alarmMsg(msg);
			}
			break;
		case User.LOGOUT:
			logout();
			break;
		case User.MEMBERSHIP: // ȸ������ ����
			result = token.nextToken();
			break;
		case User.UPDATE_USERLIST: // ���� ����� ���
			userList(token);
			break;
		case User.UPDATE_ROOM_USERLIST: // ä�ù� ����� ���
			// ���ȣ�б�
			rNum = token.nextToken();
			userList(rNum, token);
			break;
		case User.UPDATE_FRIENDLIST: //ģ�� ���
			friendList(token);
			break;
		case User.UPDATE_SELECTEDROOM_USERLIST: // ���ǿ��� ������ ä�ù��� ����� ���
			selectedRoomUserList(token);
			break;
		case User.UPDATE_ROOMLIST: // �� ���
			roomList(token);
			break;
		case User.ECHO01: // ���� ����
			msg = token.nextToken();
			echoMsg(msg);
			break;
		case User.ECHO02: // ä�ù� ����
			rNum = token.nextToken();
			msg = token.nextToken();
			echoMsgToRoom(rNum, msg);
			break;
		case User.WHISPER: // �ӼӸ�
			id = token.nextToken();
			nick = token.nextToken();
			msg = token.nextToken();
			whisper(id, nick, msg);
			break;
		}
	}

	private void logout() {
		try {
			alarmMsg("ä�� ���α׷��� �����մϴ�!");
			waitRoom.dispose();
			user.getDis().close();
			user.getDos().close();
			socket.close();
			waitRoom = null;
			user = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ä�ù� ���� ����� ����Ʈ
	// ä���� ���� ����� ����Ʈ�� ǥ���ϱ� ���� ��ū�� ����Ѵ�. Integer.parseInt�� ���ڿ��� ���ڷ� ��ȯ��Ų��.
	private void userList(String rNum, StringTokenizer token) {
		for (int i = 0; i < user.getRoomArray().size(); i++) {
			if (Integer.parseInt(rNum) == user.getRoomArray().get(i).getRoomNum()) {

				// ������ ����Ʈ�� ���� ��� �����ش�. removeAllElements �޼ҵ�� ��� ���� �����ϴ� �޼ҵ��̴�.
				if (user.getRoomArray().get(i).getrUI().model != null)
					user.getRoomArray().get(i).getrUI().model.removeAllElements();

				while (token.hasMoreTokens()) {
					// ���̵�� �г����� �о ���� ��ü �ϳ��� �����Ѵ�.
					String id = token.nextToken();
					String nick = token.nextToken();
					User tempUser = new User(id, nick);

					user.getRoomArray().get(i).getrUI().model.addElement(tempUser.toString());
				}
			}
		}
	}

	// ������ ä�ù��� ����� ����Ʈ
	// �����κ��� ��������Ʈ�� ������Ʈ�϶�� ����� ���� �� ����Ѵ�. ������尡 �ƴϰ�, �ڽ� ��尡 �ִٸ� ��� �����. isLeaf()�� ���� ��尡 ���� ����̸� true�� �����Ѵ�. 
	// removeAllChildren()�� ����� ��� �ڽ��� �����ϰ� �θ� null�� �����Ѵ�.

	private void selectedRoomUserList(StringTokenizer token) {
		// �����κ��� �� ����Ʈ�� ������Ʈ�϶�� ����� ���� �� ����Ѵ�. ���Ŀ��� removeAllElements()�� ����Ͽ� ������ ����Ʈ�� ���� ��� �����ش�.

		if (!waitRoom.level2.isLeaf()) {
			// ������尡 �ƴϰ�, ���ϵ尡 �ִٸ� ��� ����
			waitRoom.level2.removeAllChildren();
		}
		while (token.hasMoreTokens()) {
			// ���̵�� �г����� �о ���� ��ü �ϳ��� ����
			String id = token.nextToken();
			String nick = token.nextToken();
			User tempUser = new User(id, nick);

			// ä�ù� ����ڳ�忡 �߰�
			waitRoom.level2.add(new DefaultMutableTreeNode(tempUser.toString()));
		}
		waitRoom.userTree.updateUI();
	}

	// ���� ����� ����Ʈ
	private void userList(StringTokenizer token) {
		// �����κ��� ��������Ʈ(����)�� ������Ʈ�϶�� ����� ����

		if (waitRoom == null) {
			return;
		}

		if (!waitRoom.level3.isLeaf()) {
			// ������尡 �ƴϰ�, ���ϵ尡 �ִٸ� ��� ����
			waitRoom.level3.removeAllChildren();
		}
		while (token.hasMoreTokens()) {
			// ���̵�� �г����� �о ���� ��ü �ϳ��� ����
			String id = token.nextToken();
			String nick = token.nextToken();
			User tempUser = new User(id, nick);

			for (int i = 0; i < waitRoom.userArray.size(); i++) {
				if (tempUser.getId().equals(waitRoom.userArray.get(i))) {
				}
				if (i == waitRoom.userArray.size()) {
					// �迭�� ������ ������ �߰�����
					waitRoom.userArray.add(tempUser);
				}
			}
			// ���� ����ڳ�忡 �߰�
			waitRoom.level3.add(new DefaultMutableTreeNode(tempUser.toString()));
		}
		waitRoom.userTree.updateUI();
	}

	// �����κ��� �渮��Ʈ�� ������Ʈ�϶�� ����� ����
	private void roomList(StringTokenizer token) {
		String rNum, rName;
		Room room = new Room();

		// ������ ����Ʈ�� ���� ��� ������
		if (waitRoom.model != null) {
			waitRoom.model.removeAllElements();
		}

		while (token.hasMoreTokens()) {
			rNum = token.nextToken();
			rName = token.nextToken();
			int num = Integer.parseInt(rNum);

			// ��Ʈ�� �ѹ��� ������Ʈ�ϱ� ���� �ִ밪+1�� �Ѵ�.
			if (num >= waitRoom.lastRoomNum) {
				waitRoom.lastRoomNum = num + 1;
			}
			room.setRoomNum(num);
			room.setRoomName(rName);

			waitRoom.model.addElement(room.toProtocol());
		}
	}

	private void alarmMsg(String string) {
		int i = JOptionPane.showConfirmDialog(null, string, "�޽���", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
		// Ȯ�� ������ ����ȴ�.
		if (i == 0) { }
	}

	private void login(String nick) {
		// ������� �α��� ������ �����´�.
		user.setId(login.idText.getText());
		user.setNickName(nick);

		// �α��� â�� �ݰ� ���� â�� ����.
		login.dispose();
		waitRoom = new WaitRoomUI(EightClient.this);
		waitRoom.lbid.setText(user.getId());
		waitRoom.lbip.setText(user.getIP());
		waitRoom.lbnick.setText(user.getNickName());
	}

	// �ӼӸ��� �޴� �޼ҵ��̴�.
	private void whisper(String id, String nick, String msg) {
		waitRoom.waitRoomArea.append("(" + id + ")���� �ӼӸ� : " + msg + "\n");
	}

	private void echoMsg(String msg) {
		// ���濡�� �޽����� ���� �� ���Ǵ� �޼ҵ��̴�. setCaretPosition�� ����� �� �Ʒ��� ��ũ���Ͽ� Ŀ�� ��ġ�� �����Ѵ�.
		if (waitRoom != null) {
			waitRoom.waitRoomArea.setCaretPosition(waitRoom.waitRoomArea.getText().length());
			waitRoom.waitRoomArea.append(msg + "\n");
		}
	}

	//ä�ù濡�� �޽����� ���� �� ���Ǵ� �޼ҵ��̴�. ���� setCaretPosition�� ����� �� �Ʒ��� ��ũ���Ͽ� Ŀ�� ��ġ ������ �Ѵ�.
	private void echoMsgToRoom(String rNum, String msg) {
		for (int i = 0; i < user.getRoomArray().size(); i++) {
			if (Integer.parseInt(rNum) == user.getRoomArray().get(i).getRoomNum()) {
				// ����� -> ��迭 -> ������ -> �ؽ�Ʈ�����
				// Ŀ�� ��ġ ����
				user.getRoomArray().get(i).getrUI().chatArea.setCaretPosition(user.getRoomArray().get(i).getrUI().chatArea.getText().length());
				// ����
				user.getRoomArray().get(i).getrUI().chatArea.append(msg + "\n");
			}
		}
	}
	
	private void friendList(StringTokenizer token) {
		// �����κ��� ģ�� ����Ʈ(����)�� ������Ʈ�϶�� ����� ����

		if (waitRoom == null) {
			return;
		}

		if (!waitRoom.level4.isLeaf()) {
			// ������尡 �ƴϰ�, ���ϵ尡 �ִٸ� ��� ����
			waitRoom.level4.removeAllChildren();
		}
		while (token.hasMoreTokens()) {
			// ���̵�� �г����� �о ���� ��ü �ϳ��� ����
			String id = token.nextToken();
			String nick = token.nextToken();
			User tempUser = new User(id, nick);

			for (int i = 0; i < waitRoom.userArray.size(); i++) {
				if (tempUser.getId().equals(waitRoom.userArray.get(i))) {
				}
				if (i == waitRoom.friendArray.size()) {
					// �迭�� ������ ������ �߰�����
					waitRoom.friendArray.add(tempUser);
				}
			}
			// ģ�� ��� ����ڳ�忡 �߰�
			waitRoom.level4.add(new DefaultMutableTreeNode(tempUser.toString()));
		}
		waitRoom.userTree.updateUI();
	}

	// getter, setter
	public static int getPORT() {
		return PORT;
	}

	public static void setPORT(int pORT) {
		PORT = pORT;
	}

	public static String getIP() {
		return IP;
	}

	public static void setIP(String iP) {
		IP = iP;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LoginUI getLogin() {
		return login;
	}

	public void setLogin(LoginUI login) {
		this.login = login;
	}

	public WaitRoomUI getRestRoom() {
		return waitRoom;
	}

	public void setRestRoom(WaitRoomUI restRoom) {
		this.waitRoom = restRoom;
	}

	public DataInputStream getDis() {
		return dis;
	}

	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}

	public DataOutputStream getDos() {
		return dos;
	}

	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

}