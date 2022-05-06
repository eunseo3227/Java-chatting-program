package project_3;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class RoomUI extends JFrame implements ActionListener {

	EightClient client;
	Room room;

	JTextArea chatArea;
	JTextField chatField;
	JList uList;
	DefaultListModel model;
	Color c = new Color(255, 216, 216);	//배경색 추가(분홍색)
	
    ImageIcon icon;

    //RoomUI 생성자를 만든다. 사용자와 방을 매개변수로 전달 받는다. 그리고 타이틀바엔 이미지를 추가했다. initialize();를 입력하여 메소드를 실행시킨다.
	public RoomUI(EightClient client, Room room) {
		this.client = client;
		this.room = room;
		setTitle("야옹 채팅방 : " + room.toProtocol());
		icon = new ImageIcon("icon2.png");
		this.setIconImage(icon.getImage());//타이틀바에 이미지넣기
		initialize();
	}

	private void initialize() {
		setBounds(100, 100, 502, 481);	//setBounds() 메소드는 창 크기를 조절하는 메소드로 setBounds(가로위치, 세로위치, 가로길이, 세로길이);로 구성되어 있다.
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //프레임의 오른쪽 상단 x 버튼을 누르면 해당 프레임을 종료한다.
		getContentPane().setLayout(null); //레이아웃을 설정하는 것이다.
		setBackground(c);	//배경색 추가(실패)
		setResizable(false);	//창의 크기를 조절할 수 없도록 하는 것이다.

		// 채팅 패널이다. 채팅 패널의 위치, 너비, 높이를 지정하고 해당 패널을 컨텐츠팬에 붙인다. 배경색을 지정하고 배치관리자를 이용하여 배치 방법을 지정한다. 
		// 채팅 패널은 BorderLayout으로 설정하였다.
		final JPanel panel = new JPanel();
		panel.setBounds(12, 10, 300, 358);
		getContentPane().add(panel);
		getContentPane().setBackground(c);	//배경색 추가
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		//스크롤 페인(scroll pane)은 컴포넌트에 스크롤 기능을 제공한다. scrollPane은 BorderLayout에 센터에 부착한다.

		chatArea = new JTextArea();
		chatArea.setBackground(Color.WHITE);
		chatArea.setEditable(false); // 수정불가
		scrollPane.setViewportView(chatArea); // 화면 보임
		chatArea.append("※ 매너 채팅 부탁드립니다:) ※\n");
		//채팅 내용을 보여주는 chatArea를 만든다. 배경색은 하얀색으로 설정하고 수정 할 수 없도록 설정한다. setViewportView를 이용하여 jScrollPane에 JTextArea를 추가한다.
		// 채팅창에 “※ 매너 채팅 부탁드립니다:) ※”라는 문구도 추가한다.

		JPanel panel1 = new JPanel();
		// 채팅창에 채팅을 보내도록 글을 작성하는 패널을 추가한다. 
		panel1.setBounds(12, 378, 300, 34);
		getContentPane().add(panel1);
		panel1.setLayout(new BorderLayout(0, 0));

		// 채팅을 작성할 수 있는 JTextField이다. setColumns를 사용하여 텍스트 필드의 최대 입력 개수를 설정한다. 
		// addKeyListener를 사용하여 엔터키 눌렀을 때 메시지가 전송되도록 설정한다.
		chatField = new JTextField();
		panel1.add(chatField);
		chatField.setColumns(10);
		chatField.addKeyListener(new KeyAdapter() {
			// 엔터 버튼 이벤트
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					msgSummit();
				}
			}

		});

		// 채팅방 참여자를 나타내기 위한 참여자 패널을 생성한다. 참여자 패널에도 스크롤을 추가하였다.
		JPanel panel2 = new JPanel();
		panel2.setBounds(324, 10, 150, 358);
		getContentPane().add(panel2);
		panel2.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane1 = new JScrollPane();
		panel2.add(scrollPane1, BorderLayout.CENTER);

		// 채팅방 참여자를 리스트에 저장하기 위한 JList를 추가한다. 또한 setViewportView를 사용하여 jScrollPane에 JList를 추가한다.
		uList = new JList(new DefaultListModel());
		model = (DefaultListModel) uList.getModel();
		scrollPane1.setViewportView(uList);

		// send button
		// 채팅방에 메시지를 보내기 위한 보내기 버튼을 생성한다. ActionListener라는 인터페이스 안에 addActionListener를 오버로드하여 버튼을 누르면 메시지가 보내지고
		// requestFocus();를 이용하여 chatField에 포커스를 주어 키 이벤트를 받을 컴포넌트를 강제로 설정한다. 그리고 보내기 버튼을 컨텐트팬에 위치를 지정하여 붙인다.
		JButton roomSendBtn = new JButton("보내기");
		roomSendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				msgSummit();
				chatField.requestFocus();
			}
		});
		roomSendBtn.setBounds(324, 378, 75, 34);
		getContentPane().add(roomSendBtn);

		// exit button
		// 채팅방에서 나가기 위한 나가기 버튼을 생성한다. 나가기 버튼을 클릭하면 showConfirmDialog를 사용하여 “방을 삭제 하시겠습니까?”라는 메시지 다이얼로그를 띄운다. 
		// OK_CANCEL_OPTION을 통해 삭제확인을 누르면 0을 리턴하여 ans는 0이 된다.
		JButton roomExitBtn = new JButton("나가기");
		roomExitBtn.addMouseListener(new MouseAdapter() {		// 나가기 버튼
			@Override
			public void mouseClicked(MouseEvent e) {
				int ans = JOptionPane.showConfirmDialog(panel, "방을 삭제 하시겠습니까?", "삭제확인", JOptionPane.OK_CANCEL_OPTION);

				if (ans == 0) { // 만약에 삭제확인 버튼을 누르면 사용자는 방에서 나가게 되고 해당 방은 방 배열에서 삭제된다. 그리고 setVisible(false);을 사용하여 창을 화면에서 없앤다.
					try {
						client.getUser().getDos().writeUTF(User.GETOUT_ROOM + "/" + room.getRoomNum());
						for (int i = 0; i < client.getUser().getRoomArray().size(); i++) {
							if (client.getUser().getRoomArray().get(i).getRoomNum() == room.getRoomNum()) {
								client.getUser().getRoomArray().remove(i);
								setVisible(false);
							}
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else { 
				}
			}
		});
		roomExitBtn.setBounds(400, 378, 75, 34);
		getContentPane().add(roomExitBtn);
		//나가기 버튼을 컨텐트팬에 부착한다. 그리고 JMenuBar를 사용하여 메뉴를 만든다. 그리고 setJMenuBar를 사용하여 프레임에 메뉴를 넣고 setVisible(true);를 이용하여 창을 화면에 나타낸다.

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		setVisible(true);
		
		//////////////////////////////////////
		// 채팅저장이라는 메뉴를 추가하여 채팅 내용을 저장할 수 있도록 한다. 채팅저장을 누르면 파일로저장이라는 JMenuItem을 추가하여 채팅 내용을 파일로 저장하도록 한다.
		JMenu mnuSaveChat = new JMenu("채팅저장");
		mnuSaveChat.addActionListener(this);
		menuBar.add(mnuSaveChat);
		JMenuItem mitSaveChatToFile = new JMenuItem("파일로저장");
		mitSaveChatToFile.addActionListener(this);
		mnuSaveChat.add(mitSaveChatToFile);
		
		// 저장채팅확인이라는 메뉴를 추가하여 저장된 채팅 내용을 확인할 수 있도록 한다. 저장채팅확인을 누르면 파일열기라는 JMenuItem을 추가하여 저장된 채팅 내용을 열 수 있도록 한다.
		JMenu mnuLoadChat = new JMenu("저장채팅확인");
		mnuLoadChat.addActionListener(this);
		menuBar.add(mnuLoadChat);
		JMenuItem mitLoadChatFromFile = new JMenuItem("파일열기");
		mitLoadChatFromFile.addActionListener(this);
		mnuLoadChat.add(mitLoadChatFromFile);
		
		
		// WindowAdapter를 사용하여 WindowListener를 구현한다. 이를 통해 윈도우창을 종료한다. 윈도우창을 종료하면 해당 채팅방을 배열에서 제거한다.
		chatField.requestFocus();
		this.addWindowListener(new WindowAdapter() {	// 윈도우 나가기
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					client.getUser().getDos().writeUTF(User.GETOUT_ROOM + "/" + room.getRoomNum());
					for (int i = 0; i < client.getUser().getRoomArray().size(); i++) {
						if (client.getUser().getRoomArray().get(i).getRoomNum() == room.getRoomNum()) {
							client.getUser().getRoomArray().remove(i);
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	// msgSummit 메소드를 이용하여 채팅 필드에 텍스트를 받아서 채팅방에 메시지를 보내도록 한다.
	private void msgSummit() {
		String string = chatField.getText();
		if (!string.equals("")) {
			try {
				// 채팅방에 메시지 보냄
				client.getDos()
						.writeUTF(User.ECHO02 + "/" + room.getRoomNum() + "/" + client.getUser().toString() + string);
				chatField.setText("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	// actionPerformed 메소드를 통해 getActionCommand()를 이용하여 이벤트를 발생시킨 객체의 문자열을 가져온다. 파일로저장이라면 채팅방의 내용을 파일에 저장한다. 
	// 파일열기라면 해당 파일을 TextViewUI를 이용하여 해당 파일을 연다.
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "파일로저장":
				String filename = UtilFileIO.saveFile(chatArea);
				JOptionPane.showMessageDialog(chatArea.getParent(), 
						"채팅내용을 파일명(" + filename + ")으로 저장하였습니다", 
						"채팅백업", JOptionPane.INFORMATION_MESSAGE);
				break;
			case "파일열기":
				filename = UtilFileIO.getFilenameFromFileOpenDialog("./");
				String text = UtilFileIO.loadFile(filename);
				TextViewUI textview = new TextViewUI(text);
				break;
		}
	}
}
