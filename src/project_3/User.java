package project_3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class User {
//	String defaultNick = "������";
//	static int nickCnt = 1;
	String IP; // ��Ʈ��ũ�� IP�̴�.
	String nickName; // ����� �г���
	String id; // ����� ���̵� - IP �ּ�
	String pw; // password
	boolean online;
	ArrayList<Room> userooms; // ����ڰ� ������ ���� ���

	DataInputStream dis; // �Է½�Ʈ��
	DataOutputStream dos; // ��½�Ʈ��

	// PROTOCOLs
	public static final String LOGIN = "EI"; // �α���
	public static final String LOGOUT = "EO"; // �α׾ƿ�
	public static final String MEMBERSHIP = "EM"; // ȸ������

	public static final String UPDATE_SELECTEDROOM_USERLIST = "ED"; // ���ǿ��� ������ ä�ù��� ��������Ʈ ������Ʈ
	public static final String UPDATE_ROOM_USERLIST = "ES"; // ä�ù��� ��������Ʈ ������Ʈ
	public static final String UPDATE_USERLIST = "EU"; // ��������Ʈ ������Ʈ
	public static final String UPDATE_ROOMLIST = "ER"; // ä�ù渮��Ʈ ������Ʈ
	public static final String UPDATE_FRIENDLIST = "EF"; // ģ������Ʈ ������Ʈ

	public static final String CREATE_ROOM = "RC"; // ä�ù� ����
	public static final String GETIN_ROOM = "RI"; // ä�ù� ����
	public static final String GETOUT_ROOM = "RO"; // ä�ù� ����
	public static final String ECHO01 = "MM"; // ���� ä��
	public static final String ECHO02 = "ME"; // ä�ù� ä��
	public static final String WHISPER = "MW"; // �ӼӸ�


	User(String id, String nick) {
		this.id = id;
		this.nickName = nick;
	}

	User(DataInputStream dis, DataOutputStream dos) {
		this.dis = dis;
		this.dos = dos;
//		nickCnt++;
//		setNickName(defaultNick + nickCnt);
		userooms = new ArrayList<Room>();
	}

	public String toStringforLogin() {
		return id + "/" + pw;
	}

	public String toProtocol() {
		return id + "/" + nickName;
	}

	public String toString() {
		return nickName + "(" + id + ")";
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
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

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public ArrayList<Room> getRoomArray() {
		return userooms;
	}

	public void setRooms(ArrayList<Room> rooms) {
		this.userooms = rooms;
	}

}