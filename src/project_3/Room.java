package project_3;

import java.util.ArrayList;

public class Room {

	int roomNum;
	String roomName; // �� ��ȣ�� �� �̸��� �����Ѵ�.
	ArrayList<User> userArray; // ä�ù濡 ������ �����
	User maker; // ����, �游����
	RoomUI rUI; // �� UI

	public Room() {
		userArray = new ArrayList<User>();
	}

	public Room(String message) {
		userArray = new ArrayList<User>();
		setRoomName(message);
	}

	public String toProtocol() {
		return roomNum + "/" + roomName;
	}

	public int getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public ArrayList<User> getUserArray() {
		return userArray;
	}

	public User getMaker() {
		return maker;
	}

	public void setMaker(User user) {
		this.maker = user;
	}

	public RoomUI getrUI() {
		return rUI;
	}

	public void setrUI(RoomUI rUI) {
		this.rUI = rUI;
	}

}