package project_3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JTextArea;

public class ServerThread implements Runnable {

	MsgeBox msgbox = new MsgeBox();

	WaitRoomUI waitRoom;
	ArrayList<User> userArray; // 서버에 접속한 사용자들
	ArrayList<User> friendArray; // 친구 추가목록
	ArrayList<Room> roomArray; // 서버가 열어놓은 채팅방들
	User user; // 현재 스레드와 연결된(소켓이 생성된) 사용자
	JTextArea jta;
	String id;
	boolean onLine = true;

	private DataOutputStream thisUser;

	// ServerThread 생성자를 만든다. .add(person)을 통해 배열에 사용자를 추가한다.
	ServerThread(JTextArea jta, User person, ArrayList<User> userArray, ArrayList<Room> roomArray, ArrayList<User> friendArray) {
		
		this.roomArray = roomArray;
		this.userArray = userArray;
		this.friendArray = friendArray;
		this.userArray.add(person); // 배열에 사용자 추가
		this.user = person;
		this.jta = jta;
		this.thisUser = person.getDos();
	}
	
	public String getId() {
		return id;
	}

	@Override
	public void run() {
		DataInputStream dis = user.getDis(); // 스레드를 실행하면 getDis();를 이용하여 입력 스트림을 받는다.

		//onLine를 true로 설정하여 무한 반복을 한다. readUTF()를 사용하여 메시지를 받기 위해 대기를 한다. 
		//readUTF()는 UTF-8 형식으로 코딩된 문자열을 읽는다. readUTF는 상대가 입력하지 않으면 계속 대기한다. dataParsing 메소드는 데이터를 구분하는 메소드로 토큰을 이용해 분리한다. 
		//메시지를 받으면 dataParsing를 이용하여 메시지를 해석한다. jta.append를 이용하여 JTextArea를 출력하고 
		//jta.setCaretPosition(jta.getText().length()); 코드를 append 밑에 추가해주면 항상 아래로 스크롤 된다.
		while (onLine) {
			try {
				String receivedMsg = dis.readUTF(); // 메시지 받기(대기)
				dataParsing(receivedMsg); // 메시지 해석
				jta.append("성공 : 메시지 읽음 -" + receivedMsg + "\n");
				jta.setCaretPosition(jta.getText().length());
			} catch (IOException e) {
				try {
					user.getDis().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					jta.append("에러 : 서버스레드-읽기 실패\n");
					break;
				}
			}
		}
	}

	// 데이터를 구분하기 위해 토큰을 생성한다. 구분 문자(/)으로 구분된 문자열(토큰)을 분리한 스트링 protocol을 숫자로 바꾼다. 그리고 switch문을 이용하여 해당되는 데이터를 구분한다.
	// 로그인을 하면 사용자가 입력한(전송한) 아이디와 패스워드를 전달 받아 토큰을 출력한 뒤 로그인을 시도한다. 만약 로그아웃이라면 로그아웃을 실행한다.
	// 회원가입을 하면 아이디와 비밀번호를 전달받아 각 토큰을 출력한다.
	// 대기실 사용자 목록과 채팅방 사용자 목록은 thisUser를 전달받아 출력하고 rNum으로 방번호를 읽는다.
	// 대기실에서 선택한 채팅방의 사용자 목록, 방 목록, 방만들기, 방 들어가기, 방 나오기, 대기실 채팅 전송, 채팅방 채팅 전송, 귓속말을 수행한다.
	public synchronized void dataParsing(String data) {
		StringTokenizer token = new StringTokenizer(data, "/"); // 토큰 생성
		String protocol = token.nextToken(); // 토큰으로 분리된 스트링을 숫자로
		String id, pw, rNum, rName, msg;
		System.out.println("서버가 받은 데이터 : " + data);

		switch (protocol) {
		case User.LOGIN: // 로그인
			// 사용자가 입력한(전송한) 아이디와 패스워드
			id = token.nextToken();
			pw = token.nextToken();
			login(id, pw);
			break;
		case User.LOGOUT: // 로그아웃
			logout();
			break;
		case User.MEMBERSHIP: // 회원가입
			id = token.nextToken();
			pw = token.nextToken();
			break;
		case User.UPDATE_USERLIST: // 대기실 사용자 목록
			userList(thisUser);
			break;
		case User.UPDATE_ROOM_USERLIST: // 채팅방 사용자 목록
			// 방번호읽기
			rNum = token.nextToken();
			userList(rNum, thisUser);
			break;
		case User.UPDATE_FRIENDLIST:
			friendList(thisUser);
			break;
		case User.UPDATE_SELECTEDROOM_USERLIST: // 대기실에서 선택한 채팅방의 사용자 목록
			// 방번호읽기
			rNum = token.nextToken();
			selectedRoomUserList(rNum, thisUser);
			break;
		case User.UPDATE_ROOMLIST: // 방 목록
			roomList(thisUser);
			break;
		case User.CREATE_ROOM: // 방만들기
			rNum = token.nextToken();
			rName = token.nextToken();
			createRoom(rNum, rName);
			break;
		case User.GETIN_ROOM: // 방 들어가기
			rNum = token.nextToken();
			getInRoom(rNum);
			break;
		case User.GETOUT_ROOM: // 방 나오기
			rNum = token.nextToken();
			getOutRoom(rNum);
			break;
		case User.ECHO01: // 대기실 에코
			msg = token.nextToken();
			echoMsg(User.ECHO01 + "/" + user.toString() + msg);
			break;
		case User.ECHO02: // 채팅방 에코
			rNum = token.nextToken();
			msg = token.nextToken();
			echoMsg(rNum, msg);
			break;
		case User.WHISPER: // 귓속말
			id = token.nextToken();
			msg = token.nextToken();
			whisper(id, msg);
			break;
		}
	}

	//채팅방에서 나가면 채팅방의 유저 리스트에서 사용자를 삭제한다.
	private void getOutRoom(String rNum) {
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					if (user.getId().equals(roomArray.get(i).getUserArray().get(j).getId())) {
						roomArray.get(i).getUserArray().remove(j);
					}
				}

				// 사용자가 채팅방에서 나가면 사용자의 채팅방 리스트에서 해당 채팅방을 제거한다. 
				for (int j = 0; j < user.getRoomArray().size(); j++) {
					if (Integer.parseInt(rNum) == user.getRoomArray().get(j).getRoomNum()) {
						user.getRoomArray().remove(j);
					}
				}
				echoMsg(roomArray.get(i), user.toString() + "님이 퇴장하셨습니다.");
				userList(rNum);

				if (roomArray.get(i).getUserArray().size() <= 0) {
					roomArray.remove(i);
					roomList();
				}
			}
		}
	}

	// 사용자가 채팅방에 들어갈 때 사용한다.
	private void getInRoom(String rNum) {
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				// 방 객체가 있는 경우, 방에 사용자추가
				roomArray.get(i).getUserArray().add(user);
				// 사용자 객체에 방 추가
				user.getRoomArray().add(roomArray.get(i));
				echoMsg(roomArray.get(i), user.toString() + "님이 입장하셨습니다.");
				userList(rNum);
			}
		}
	}

	// 사용자가 방을 만들 때 사용한다.
	private void createRoom(String rNum, String rName) {
		Room rm = new Room(rName); // 지정한 제목으로 채팅방 생성
		rm.setMaker(user); // 방장 설정
		rm.setRoomNum(Integer.parseInt(rNum)); // 방번호 설정

		rm.getUserArray().add(user); // 채팅방에 유저(본인) 추가
		roomArray.add(rm); // 룸리스트에 현재 채팅방 추가
		user.getRoomArray().add(rm); // 사용자 객체에 접속한 채팅방을 저장

		echoMsg(User.ECHO01 + "/" + user.toString() + "님이 " + rm.getRoomNum() + "번 채팅방을 개설하셨습니다.");
		echoMsg(rm, user.toString() + "님이 입장하셨습니다.");
		roomList();
		userList(rNum, thisUser);
		jta.append("성공 : " + userArray.toString() + "가 채팅방 생성\n");
	}

	// 사용자가 귓속말을 시도할 때 귓속말 상대를 찾으면 구분 문자 “/”를 사용하여 토큰으로 나눈 뒤 해당 내용을 상대방에게 전달한다.
	private void whisper(String id, String msg) {
		for (int i = 0; i < userArray.size(); i++) {
			if (id.equals(userArray.get(i).getId())) {
				// 귓속말 상대를 찾으면
				try {
					userArray.get(i).getDos().writeUTF(User.WHISPER + "/" + user.toProtocol() + "/" + msg);
					jta.append("성공 : 귓속말 보냄 : " + user.toString() + "가 " + userArray.get(i).toString() + "에게" + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 대기실에서 메시지를 보낼 때 사용한다.
	private void echoMsg(String msg) {
		for (int i = 0; i < userArray.size(); i++) {
			try {
				userArray.get(i).getDos().writeUTF(msg);
				jta.append(user.toString() + " - " + msg + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				jta.append("에러 : 에코 실패\n");
			}
		}
	}

	// 방 번호만 아는 경우 채팅방에서 메시지를 보낼 때 사용한다.
	private void echoMsg(String rNum, String msg) {
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				echoMsg(roomArray.get(i), msg);
			}
		}
	}

	// 방 객체가 있는 경우 채팅방에서 메시지를 보낼 때 사용한다. 방에 참가한 유저들에게 에코 메시지를 전송한다.
	private void echoMsg(Room room, String msg) {
		for (int i = 0; i < room.getUserArray().size(); i++) {
			try {
				room.getUserArray().get(i).getDos().writeUTF(User.ECHO02 + "/" + room.getRoomNum() + "/" + msg);
				jta.append("성공 : 메시지전송 : " + msg + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				jta.append("에러 : 에코 실패\n");
			}
		}
	}

	// 사용자가 로그인 시도시에 실행된다. 데이터베이스에 있는 아이디와 비밀번호를 가져와 비교를 하고 result가 0이면 로그인 성공이다.
	private void login(String id, String pw) {
		StringBuffer str = new StringBuffer();
		try {

			DBLogin logdb = new DBLogin();
			int result = logdb.checkIDPW(id, pw);

			if (result == 0) { // result가 0이면 성공
				for (int i = 0; i < userArray.size(); i++) {
					if (id.equals(userArray.get(i).getId())) {
						try {
							System.out.println("접속중");
							thisUser.writeUTF(User.LOGIN + "/fail/이미 접속 중입니다.");
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}
				// 로그인 성공시 비밀번호가 유저의 비밀번호로 설정되고 유저의 아이디가 닉네임으로 설정된다.
				user.setId(id);
				user.setPw(pw);
				user.setNickName(id);
				thisUser.writeUTF(User.LOGIN + "/OK/" + user.getNickName());
				this.user.setOnline(true);

				// 대기실에 에코
				// 로그인이 되면 대기실에 메시지가 전송된다. 그리고 룸리스트에 해당 유저가 추가된다. 만약 result가 1이면 로그인 실패이다.
				echoMsg(User.ECHO01 + "/" + user.toString() + "님이 입장하셨습니다.");
				jta.append(id + " : 님이 입장하셨습니다.\n");

				roomList(thisUser);
				for (int i = 0; i < userArray.size(); i++) {
					userList(userArray.get(i).getDos());
				}

				jta.append("성공 : DB 읽기 : " + id);
			} else { // result가 1이면 실패
				thisUser.writeUTF(User.LOGIN + "/fail/아이디와 비밀번호를 확인해 주세요!");
			}

		} catch (Exception e) {
			try {
				thisUser.writeUTF(User.LOGIN + "/fail/아이디가 존재하지 않습니다!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			jta.append("실패 : DB 읽기\n");
			return;
		}

	}

	// 로그아웃 시에 사용된다.
	private void logout() {
		System.out.println("로그아웃 했음!");

		// 오프라인으로 바꿈
		user.setOnline(false);
		// room 클래스의 멤버 변수인 사용자 배열에서 삭제한다. 그리고 해당 유저가 퇴장하였다는 메시지를 전송한다.
		for (int i = 0; i < userArray.size(); i++) {
			if (user.getId().equals(userArray.get(i).getId())) {
				System.out.println(userArray.get(i).getId() + "지웠다.");
				userArray.remove(i);
			}
		}
		// room 클래스의 멤버변수인 사용자배열에서 삭제
		for (int i = 0; i < roomArray.size(); i++) {
			for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
				if (user.getId().equals(roomArray.get(i).getUserArray().get(j).getId())) {
					roomArray.get(i).getUserArray().remove(j);
				}
			}
		}
		echoMsg(User.ECHO01 + "/" + user.toString() + "님이 퇴장하셨습니다.");

		for (int i = 0; i < userArray.size(); i++) {
			userList(userArray.get(i).getDos());
		}

		jta.append(user.getId() + "님이 퇴장하셨습니다.\n");

		try {
			user.getDos().writeUTF(User.LOGOUT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			user.getDis().close();
			user.getDos().close();
			user = null;
			jta.append("성공 : 스트림 닫기\n");
		} catch (IOException e) {
			e.printStackTrace();
			jta.append("실패 : 스트림 닫기\n");
		}
	}

	// 사용자 리스트 (선택한 채팅방)
	public void selectedRoomUserList(String rNum, DataOutputStream target) {
		String ul = "";

		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					// 채팅방에 접속되어 있는 유저들의 아이디+닉네임을 출력한다.
					ul += "/" + roomArray.get(i).getUserArray().get(j).toProtocol();
				}
			}
		}
		try {
			// 데이터 전송
			target.writeUTF(User.UPDATE_SELECTEDROOM_USERLIST + ul);
			jta.append("성공 : 목록(사용자)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("에러 : 목록(사용자) 전송 실패\n");
		}
	}

	// 사용자 리스트 (대기실)
	public String userList(DataOutputStream target) {
		String ul = "";

		for (int i = 0; i < userArray.size(); i++) {
			// 접속되어 있는 유저들의 아이디+닉네임을 출력한다.
			ul += "/" + userArray.get(i).toProtocol();
		}

		try {
			// 데이터 전송
			target.writeUTF(User.UPDATE_USERLIST + ul);
			jta.append("성공 : 목록(사용자)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("에러 : 목록(사용자) 전송 실패\n");
		}
		return ul;
	}

	// 사용자 리스트 (채팅방 내부)
	public void userList(String rNum, DataOutputStream target) {
		String ul = "/" + rNum;

		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					// 채팅방에 접속되어 있는 유저들의 아이디+닉네임을 출력한다.
					ul += "/" + roomArray.get(i).getUserArray().get(j).toProtocol();
				}
			}
		}
		try {
			// 데이터 전송
			target.writeUTF(User.UPDATE_ROOM_USERLIST + ul);
			jta.append("성공 : 목록(사용자)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("에러 : 목록(사용자) 전송 실패\n");
		}
	}

	// 사용자 리스트 (채팅방 내부 모든 사용자들에게 전달)
	public void userList(String rNum) {
		String ul = "/" + rNum;
		Room temp = null;
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				temp = roomArray.get(i);
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					// 채팅방에 접속되어 있는 유저들의 아이디+닉네임을 출력한다.
					ul += "/" + roomArray.get(i).getUserArray().get(j).toProtocol();
				}
			}
		}
		for (int i = 0; i < temp.getUserArray().size(); i++) {
			try {
				// 데이터 전송
				temp.getUserArray().get(i).getDos().writeUTF(User.UPDATE_ROOM_USERLIST + ul);
				jta.append("성공 : 목록(사용자)-" + ul + "\n");
			} catch (IOException e) {
				jta.append("에러 : 목록(사용자) 전송 실패\n");
			}
		}
	}

	// 채팅 방리스트
	public void roomList(DataOutputStream target) {
		String rl = "";

		for (int i = 0; i < roomArray.size(); i++) {
			// 만들어진 채팅방들의 제목
			rl += "/" + roomArray.get(i).toProtocol();
		}

		jta.append("test\n");

		try {
			// 데이터 전송
			target.writeUTF(User.UPDATE_ROOMLIST + rl);
			jta.append("성공 : 목록(방)-" + rl + "\n");
		} catch (IOException e) {
			jta.append("에러 : 목록(방) 전송 실패\n");
		}
	}

	// 채팅 방리스트를 나타내는데 테스트를 한다. 만들어진 채팅방들의 제목이 배열에 잘 추가되었는지 확인한다.
	public void roomList() {
		String rl = "";

		for (int i = 0; i < roomArray.size(); i++) {
			// 만들어진 채팅방들의 제목
			rl += "/" + roomArray.get(i).toProtocol();
		}

		jta.append("test\n");

		for (int i = 0; i < userArray.size(); i++) {

			try {
				// 데이터 전송
				userArray.get(i).getDos().writeUTF(User.UPDATE_ROOMLIST + rl);
				jta.append("성공 : 목록(방)-" + rl + "\n");
			} catch (IOException e) {
				jta.append("에러 : 목록(방) 전송 실패\n");
			}
		}
	}
	
	public String friendList(DataOutputStream target) {
		String ul = "";

		for (int i = 0; i < userArray.size(); i++) {
			// 접속되어 있는 유저들의 아이디+닉네임
			ul += "/" + userArray.get(i).toProtocol();
		}
		
		ul = ul.replace(user.id, "");

		try {
			// 데이터 전송
			target.writeUTF(User.UPDATE_FRIENDLIST + ul);
			jta.append("성공 : 목록(사용자)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("에러 : 목록(사용자) 전송 실패\n");
		}
		return ul;
	}
}