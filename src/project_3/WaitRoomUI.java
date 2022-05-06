package project_3;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

public class WaitRoomUI extends JFrame implements ActionListener {

	MsgeBox msgbox = new MsgeBox();
	String temp,id;
	
	int lastRoomNum = 100;
	JButton makeRoomBtn, getInRoomBtn, whisperBtn, sendBtn, exitBtn, friendBtn;
	JTree userTree;
	JList roomList, friendList;
	JTextField chatField;
	JTextArea waitRoomArea;
	JLabel lbid, lbnick;
	JTextField lbip;

	EightClient client;
	ArrayList<User> userArray; // 사용자 목록 배열
	ArrayList<User> friendArray; //친구 목록 배열
	String currentSelectedTreeNode;
	DefaultListModel model, model2;
	DefaultMutableTreeNode level1;		
	DefaultMutableTreeNode level2;	
	DefaultMutableTreeNode level3;	
	DefaultMutableTreeNode level4;	
	
	JScrollPane scrollPane;
    ImageIcon icon;
    LoginUI login;
    Color c = new Color(206, 242, 121);	//배경색 추가

    // WaitRoomUI 생성자를 만든다. 사용자와 방을 매개변수로 전달 받는다. 타이틀 이름은 “아옹 채팅방”이다. 
    // userArray를 사용하여 사용자 목록 배열을 추가한다. initialize();를 입력하여 메소드를 실행시킨다.
	public WaitRoomUI(EightClient eigClient) {
		setTitle("야옹 채팅방");
		userArray = new ArrayList<User>();
		client = eigClient;
		initialize();
	}

	private void initialize() {
		
		icon = new ImageIcon("icon2.png");
		this.setIconImage(icon.getImage());
		// ImageIcon과 setIconImage를 사용하여 타이틀바에 이미지를 삽입한다.
		
		setBounds(100, 100, 800, 500); // setBounds() 메소드는 창 크기를 조절하는 메소드로 setBounds(가로위치, 세로위치, 가로길이, 세로길이);로 구성되어 있다.
		setBackground(c);	//배경색 추가(실패)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임의 오른쪽 상단 x 버튼을 누르면 해당 프레임을 종료한다.
		setBackground(c);
		setResizable(false); // 창의 크기를 조절할 수 없도록 하는 것이다.

		// JMenuBar를 사용하여 프로그램창 상단에 표시되는 menuBar라는 메뉴를 생성하고 setJMenuBar를 사용하여 넣는다.
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// “파일”이라는 basicMenus 메뉴를 생성한다. 그리고 addActionListener(this)를 사용하여 익명 클래스로 버튼 이벤트를 추가해준다. 그리고 상단 메뉴바에 “파일”이라는 메뉴를 추가한다.
		JMenu basicMenus = new JMenu("파일");
		basicMenus.addActionListener(this);
		menuBar.add(basicMenus);

		// “파일로 저장”이라는 mitSaveChatToFile 메뉴를 생성한다. 그리고 addActionListener(this)를 사용하여 익명 클래스로 버튼 이벤트를 추가해준다. 그리고 상단 메뉴바에 “파일로 저장”이라는 메뉴를 추가한다.
		JMenuItem mitSaveChatToFile = new JMenuItem("파일로저장");
		mitSaveChatToFile.addActionListener(this);
		basicMenus.add(mitSaveChatToFile);
		
		// 파일 열기”이라는 mitLoadChatFromFile 메뉴를 생성한다. 그리고 addActionListener(this)를 사용하여 익명 클래스로 버튼 이벤트를 추가해준다. 그리고 상단 메뉴바에 “파일 열기”라는 메뉴를 추가한다.
		JMenuItem mitLoadChatFromFile = new JMenuItem("파일열기");
		mitLoadChatFromFile.addActionListener(this);
		basicMenus.add(mitLoadChatFromFile);
		

		// “변경/탈퇴”라는 updndel 메뉴를 생성한다. 그리고 addActionListener(this)를 사용하여 익명 클래스로 버튼 이벤트를 추가해준다. 그리고 상단 메뉴바에 “변경/탈퇴”라는 메뉴를 추가한다.
		JMenu updndel = new JMenu("변경/탈퇴");
		updndel.addActionListener(this);
		menuBar.add(updndel);

		// 회원정보 변경”이라는 changeInfo 메뉴를 생성한다. 그리고 addActionListener(this)를 사용하여 익명 클래스로 버튼 이벤트를 추가해준다. 그리고 상단 메뉴바에 “회원정보 변경”라는 메뉴를 추가한다.
		JMenuItem changeInfo = new JMenuItem("회원정보 변경");
		changeInfo.addActionListener(this);
		updndel.add(changeInfo);
		
		// “회원 탈퇴”라는 withdrawMem 메뉴를 생성한다. 그리고 addActionListener(this)를 사용하여 익명 클래스로 버튼 이벤트를 추가해준다. 그리고 상단 메뉴바에 “회원 탈퇴”라는 메뉴를 추가한다.
		JMenuItem withdrawMem = new JMenuItem("회원 탈퇴");
		withdrawMem.addActionListener(this);
		updndel.add(withdrawMem);
		
		// “도움말”이라는 helpMenus 메뉴를 생성한다. 그리고 addActionListener(this)를 사용하여 익명 클래스로 버튼 이벤트를 추가해준다. 그리고 상단 메뉴바에 “도움말”라는 메뉴를 추가한다.
		JMenu helpMenus = new JMenu("도움말");
		helpMenus.addActionListener(this);
		menuBar.add(helpMenus);

		// “프로그램 정보”라는 proInfoItem 메뉴를 생성한다. 그리고 addActionListener(this)를 사용하여 익명 클래스로 버튼 이벤트를 추가해준다. 그리고 상단 메뉴바에 “프로그램 정보”라는 메뉴를 추가한다.
		JMenuItem proInfoItem = new JMenuItem("프로그램 정보");
		proInfoItem.addActionListener(this);
		helpMenus.add(proInfoItem);
		getContentPane().setLayout(null);

		// 방 패널이다. 해당 패널에 여백을 주기 위해 setBorder를 사용한다. TitledBorder은 컴포넌트 주위로 제목을 줄 수 있는 보더이다, 라인과 같은 다른 보더의 효과도 함께 줄 수 있다. 
		// setBounds로 채팅 패널의 위치, 너비, 높이를 지정하고 해당 패널을 컨텐츠팬에 붙인다. 배경색을 지정하고 배치관리자를 이용하여 배치 방법을 지정한다. 채팅 패널은 BorderLayout으로 설정하였다.
		JPanel room = new JPanel();
		room.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "채 팅 방", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		room.setBounds(12, 10, 477, 215);	
		room.setBackground(c);		//배경색추가
		getContentPane().add(room);
		room.setLayout(new BorderLayout(0, 0));

		// 스크롤 페인(scroll pane)은 컴포넌트에 스크롤 기능을 제공한다. scrollPane은 BorderLayout에 센터에 부착한다.
		JScrollPane scrollPane = new JScrollPane();
		room.add(scrollPane, BorderLayout.CENTER);

		// 대기실에 있는 사용자의 리스트를 출력하기 위해 리스트 객체와 모델을 생성한다. addMouseListener를 사용하여 마우스로 클릭 되었을 때 나타나는 이벤트이다. 
		// 채팅방 목록 중 하나를 선택한 경우 선택한 방의 방 번호를 전송한다.
		roomList = new JList(new DefaultListModel());
		roomList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = roomList.getFirstVisibleIndex();
				// System.out.println(">>>>>>>>>>>" + i);
				if (i != -1) {
					// 채팅방 목록 중 하나를 선택한 경우,
					// 선택한 방의 방번호를 전송
					String temp = (String) roomList.getSelectedValue();
					if(temp.equals(null)){
						return;
					}

					try {
						client.getUser().getDos().writeUTF(User.UPDATE_SELECTEDROOM_USERLIST + "/" + temp.substring(0, 3));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		// 컴포넌트에 따라 내용을 변경하기 위해 DefaultListModel를 사용한다. setViewportView를 사용하여 스크롤 페인의 클라이언트를 roomList로 설정하고 스크롤 기능을 추가한다.
		model = (DefaultListModel) roomList.getModel();
		scrollPane.setViewportView(roomList);

		// panel2이라는 패널을 만들고 room에 남쪽에 추가한다.
		JPanel panel2 = new JPanel();
		room.add(panel2, BorderLayout.SOUTH);

		// 채팅방을 만들기 위한 방 만들기 버튼을 만든다. 해당 버튼이 클릭되면 createRoom이 실행된다.
		makeRoomBtn = new JButton("방 만들기");
		makeRoomBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		makeRoomBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// 방만들기 버튼 클릭
				createRoom();
			}
		});
		
		//panel2이라는 패널을을 GridLayout에 추가하고 “방 만들기” 버틀을 해당 패널에 추가한다.
		panel2.setLayout(new GridLayout(0, 2, 0, 0));
		panel2.add(makeRoomBtn);

		// 채팅방에 들어가기 위한 “방 입장하기” 버튼을 생성한다. 해당 버튼이 클릭되면 getIn()이 실행되고 해당 버튼을 panel2에 추가한다.
		getInRoomBtn = new JButton("방 입장하기");
		getInRoomBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// 방 들어가기
				getIn();
			}
		});
		panel2.add(getInRoomBtn);

		// user라는 패널을 생성한다. 해당 패널에 여백을 주기 위해 setBorder를 사용한다. 
		// TitledBorder은 컴포넌트 주위로 제목을 줄 수 있는 보더이다, 라인과 같은 다른 보더의 효과도 함께 줄 수 있다. 
		// setBounds로 채팅 패널의 위치, 너비, 높이를 지정하고 해당 패널을 컨텐츠팬에 붙인다. 배경색을 지정하고 배치관리자를 이용하여 배치 방법을 지정한다. 채팅 패널은 BorderLayout으로 설정하였다.
		JPanel user = new JPanel();
		user.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),	"사용자 목록", TitledBorder.CENTER,	TitledBorder.TOP, null, null));
		user.setBounds(501, 10, 271, 409);
		user.setBackground(c);		//배경색추가
		getContentPane().add(user);
		user.setLayout(new BorderLayout(0, 0));

		// 스크롤 페인(scroll pane)은 컴포넌트에 스크롤 기능을 제공한다. scrollPane은 user 패널의 BorderLayout에 센터에 부착한다.
		JScrollPane scrollPane1 = new JScrollPane();
		user.add(scrollPane1, BorderLayout.CENTER);

		// 사용자 목록을 트리구조로 나타낸다. addTreeSelectionListener는 트리 노드가 선택 되었을 때 일어나는 이벤트이다. 리턴 받은 트리 패스는 toString하여 해당 트리의 요소들을 [ROOT명, 자식, 손자, 나]를 출력한다.
		userTree = new JTree();
		userTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				currentSelectedTreeNode = e.getPath().getLastPathComponent().toString();
			}
		});
		
		// 각 레벨에 노드를 추가한다. 각 노드의 아이콘을 변경하기 위해 DefaultTreeCellRenderer를 사용하여 아이콘을 설정한다.
		// setLeafIcon은 leafnode의 아이콘을 해당 사진으로 설정하는 것이고 setCloseIcon은 닫혀진 node의 아이콘, setOpenIcon은 열린 node의 아이콘을 설정한다. 
		level1 = new DefaultMutableTreeNode("참여자");
		level2 = new DefaultMutableTreeNode("채팅방");
		level3 = new DefaultMutableTreeNode("대기실");
		level4 = new DefaultMutableTreeNode("친구목록");
		level1.add(level2);
		level1.add(level3);
		level1.add(level4);
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(new ImageIcon("user.png"));
		renderer.setClosedIcon(new ImageIcon("wait.png"));
		renderer.setOpenIcon(new ImageIcon("open.png"));

		// userTree에 해당 아이콘들을 설정하고 setEditable(false);를 하여 수정 불가 설정을 한다.
		userTree.setCellRenderer(renderer);
		userTree.setEditable(false);

		// model이라는 DefaultTreeModel을 생성한다. setModel을 사용하여 해당 트리 내용을 갱신한다. 
		// setViewportView를 사용하여 스크롤 페인의 클라이언트를 roomList로 설정하고 스크롤 기능을 추가한다.
		DefaultTreeModel model = new DefaultTreeModel(level1);
		userTree.setModel(model);

		scrollPane1.setViewportView(userTree);

		// panel1이라는 패널을 생성하고 user의 BorderLayout의 남쪽에 추가한다. panel1을 GridLayout으로 설정하고 배경색을 추가한다.
		JPanel panel1 = new JPanel();
		user.add(panel1, BorderLayout.SOUTH);
		panel1.setLayout(new GridLayout(1, 0, 0, 0));
		panel1.setBackground(c);		//배경색추가

		// whisperBtn이라는 “귓속말” 버튼을 추가하고 해당 컴포넌트에 액션 이벤트를 등록한다. 그리고 panel1에 추가한다.
		whisperBtn = new JButton("귓속말");
		
		whisperBtn.addActionListener(this);
		panel1.add(whisperBtn);

		// waitroom이라는 패널을 생성한다. 해당 패널에 여백을 주기 위해 setBorder를 사용한다. TitledBorder은 컴포넌트 주위로 제목을 줄 수 있는 보더이다, 
		// 라인과 같은 다른 보더의 효과도 함께 줄 수 있다. setBounds로 채팅 패널의 위치, 너비, 높이를 지정하고 해당 패널을 컨텐츠팬에 붙인다. 
		// 배경색을 지정하고 배치관리자를 이용하여 배치 방법을 지정한다. 해당 패널의 배경색을 지정하고 waitroom 패널은 BorderLayout으로 설정하였다.
		
		exitBtn = new JButton("종료");	
		exitBtn.addActionListener(this);
		panel1.add(exitBtn);
		
		// exitBtn을 생성한다. 그리고 addActionListener(this)를 사용하여 익명 클래스로 버튼 이벤트를 추가해준다.
		// 해당 버튼을 panel1에 붙인다.
		
		friendBtn = new JButton("친구추가");	
		friendBtn.addActionListener(this);
		panel1.add(friendBtn);
		
		JPanel waitroom = new JPanel();
		
		waitroom.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "대 기 실",	TitledBorder.CENTER, TitledBorder.TOP, null, Color.DARK_GRAY));
		waitroom.setBounds(12, 235, 477, 185);
		waitroom.setBackground(c);		//배경색추가
		getContentPane().add(waitroom);
		waitroom.setLayout(new BorderLayout(0, 0));

		// panel이라는 패널을 생성한다. waitroom을 panel에 남쪽에 추가한다. BoxLayout.X_AXIS는 해당 컴포넌트를 좌->우로 배치하는 것이다. 패널의 배경색을 지정한다.
		JPanel panel = new JPanel();
		waitroom.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setBackground(c);		//배경색추가

		// 스크롤 페인(scroll pane)은 컴포넌트에 스크롤 기능을 제공한다. scrollPane4은 panel에 부착한다.
		JScrollPane scrollPane4 = new JScrollPane();
		panel.add(scrollPane4);

		// chatField라는 JTextField를 생성한다. addKeyListener를 사용하여 엔터키 눌렀을 때 메시지가 전송되도록 설정한다. 
		// setViewportView를 사용하여 스크롤 페인의 클라이언트를 chatField로 설정하고 스크롤 기능을 추가한다. 
		// setColumns를 사용하여 텍스트 필드의 최대 입력 개수를 설정한다. 
		chatField = new JTextField();

		chatField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					msgSummit();
				}
			}

		});
		scrollPane4.setViewportView(chatField);
		chatField.setColumns(10);

		// 메시지를 보낼 수 있는 sendBtn 버튼을 만든다. 해당 버튼을 클릭하면 msgSummit가 실행되고
		// requestFocus()를 사용하여 키 이벤트 받을 컴포넌트를 chatField로 설정한다.
		sendBtn = new JButton("보내기");
		sendBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				msgSummit();
				chatField.requestFocus();
			}
		});
		panel.add(sendBtn);

		// 스크롤 페인(scroll pane)은 컴포넌트에 스크롤 기능을 제공한다. scrollPane2은 waitrooml의 센터에 부착한다.
		JScrollPane scrollPane2 = new JScrollPane();
		waitroom.add(scrollPane2, BorderLayout.CENTER);

		// 대기방 채팅창을 만들기 위해 JTextArea를 이용하여 waitRoomArea를 생성한다. setEditable(false);를 사용하여 수정 불가 설정을 한다.
		// setViewportView를 사용하여 스크롤 페인의 클라이언트를 waitRoomArea로 설정하고 스크롤 기능을 추가한다. 
		waitRoomArea = new JTextArea();
		waitRoomArea.setEditable(false);
		scrollPane2.setViewportView(waitRoomArea);

		// info라는 패널을 생성한다. 해당 패널에 lbid와 lbnick라는 라벨을 추가한다. lbip라는 JTextField를 생성하고 수정 불가 설정을 한다. 
		// 해당 텍스트 필드를 Info에 추가하고 setColumns를 사용하여 텍스트 필드의 최대 입력 개수를 설정한다.
		JPanel info = new JPanel();
		lbid = new JLabel("-");
		info.add(lbid);
		lbnick = new JLabel("-");
		info.add(lbnick);
		lbip = new JTextField();
		lbip.setEditable(false);
		info.add(lbip);
		lbip.setColumns(10);

		// chatField를 requestFocus()를 사용하여 키 이벤트 받을 컴포넌트를 chatField로 설정한다.
		// setVisible(true);를 하여 창을 화면에 나타낸다. 그리고 다시 requestFocus()를 사용하여 키 이벤트 받을 컴포넌트를 chatField로 설정한다.
		chatField.requestFocus();
		setVisible(true);
		chatField.requestFocus();

		// addWindowListener를 사용하여 윈도우 이벤트를 등록한다. 윈도우 창을 닫으면 exit01()이 실행되도록 한다.
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit01();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		id = client.user.getId();
		switch (e.getActionCommand()) {
		case "귓속말":
			// 닉네임 제외하고 아이디만 따옴
			StringTokenizer token = new StringTokenizer(currentSelectedTreeNode, "("); // 토큰 생성
			temp = token.nextToken(); // 토큰으로 분리된 스트링
			temp = token.nextToken();
			id = "/" + temp.substring(0, temp.length() - 1) + " ";
			chatField.setText(id);
			chatField.requestFocus();
			break;
		case "종료":
			int ans1 = JOptionPane.showConfirmDialog(this, "정말 종료 하시겠습니까?", "종료확인", JOptionPane.OK_CANCEL_OPTION);
			if (ans1 == 0) {
				// System.exit(0); // 강제 종료
				try {
					client.getUser().getDos().writeUTF(User.LOGOUT);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		case "친구추가":  //친구 추가 버튼을 눌렀을 때
			StringTokenizer token1 = new StringTokenizer(currentSelectedTreeNode, "("); // 토큰 생성
			temp = token1.nextToken(); // 토큰으로 분리된 스트링
			temp = token1.nextToken();
			id = "/" + temp.substring(0, temp.length() - 1) + " ";
			int ans2 = JOptionPane.showConfirmDialog(this, "친구 추가하시겠습니까??", "친구추가", JOptionPane.OK_CANCEL_OPTION);
			if (ans2 == 0) {
				try {
					client.getUser().getDos().writeUTF(User.UPDATE_FRIENDLIST+id);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		// 메뉴1 파일 메뉴
		case "회원정보 변경":
			DBRevise reDB = new DBRevise();
			reDB.myInfo(id);
			break;
		case "회원 탈퇴":
			DBDelete delDB = new DBDelete();

			int ans = JOptionPane.showConfirmDialog(this, "정말 탈퇴 하시겠습니까?", "탈퇴확인", JOptionPane.OK_CANCEL_OPTION);

			if (ans == 0) {
				int i = 0;
				i = delDB.InfoDel(id);
				if (i == 0) {
					// msgbox.messageBox(this, "탈퇴는 신중히..:)");
				} else {
					msgbox.messageBox(this, "탈퇴 성공하였습니다..:(");
					exit01();
				}
			}
			break;
		case "파일로저장":
			String filename = UtilFileIO.saveFile(waitRoomArea);
			JOptionPane.showMessageDialog(waitRoomArea.getParent(), "채팅내용을 파일명(" + filename + ")으로 저장하였습니다", "채팅백업", JOptionPane.INFORMATION_MESSAGE);
			break;
		case "파일열기":
			filename = UtilFileIO.getFilenameFromFileOpenDialog("./");
			if (filename == "") break;
			String text = UtilFileIO.loadFile(filename);
			TextViewUI textview = new TextViewUI(text);
			break;
		case "프로그램 정보":
			maker();
			break;
		}
		// actionPerformed(ActionEvent e)가 존재하여 ActionEvent가 발생하면 해당 메소드가 호출된다. getId()를 사용하여 사용자의 id를 받고 geActionCommand():를 사용하여 이벤트가 발생한 객체의 텍스트가 리턴된다. 
		// 만약 “귓속말”이라면 토큰을 생성한 뒤 해당 문자열을 토큰으로 분리한다. id는 닉네임을 제외하고 아이디만 따오고 chatField에 setText를 이용하여 id를 해당 텍스트 박스 내에 문자열로 설정한다. requestFocus();를 사용하여 키 이벤트 받을 컴포넌트를 chatField로 설정한다.
		// 만약 “회원정보 변경”이라면 DBRevise에서 reDB를 생성하여 해당 id를 보내서 회원정보를 변경한다.
		// 만약 “회원 탈퇴”라면 DBDelete에서 delDB를 생성하고 “정말 탈퇴 하시겠습니까?”라는 다이얼로그를 출력하여 탈퇴확인을 누르면 해당 id가 전달되고 탈퇴가 성공되도록 한다.
		// 만약 “종료”라면 “정말 종료 하시겠습니까?”라는 다이얼로그가 나타나고 종료확인 버튼을 누르면 사용자가 로그아웃 되도록 한다.
		// 만약 “파일로저장”이라면 FileDialog에서 선택한 파일 이름을 가져와서 해당 이름이 비어있으면 종료되고 같은 이름의 파일이 있다면 TextViewUI를 통해 보여지도록 한다.
		// 만약 “프로그램 정보”라면 maker()이 실행되도록 한다.

	}
	
	// 메시지 전송을 하기 위해 실행되는 메소드로 해당 문자열으로 토큰을 생성하고 스트링을 토큰으로 분리한다. 만약에 사용자가 귓속말을 사용하면 해당 내용이 waitRoomArea에 전달된다. 
	// 만약에 사용자가 대기실에 메시지를 보내면 대기실에 해당 메시지가 전달된다.
	private void msgSummit() {
		String string = chatField.getText();// 메시지전송
		if (!string.equals("")) {
			if (string.substring(0, 1).equals("/")) {
				
				StringTokenizer token = new StringTokenizer(string, " "); // 토큰 생성
				String id = token.nextToken(); // 토큰으로 분리된 스트링
				String msg = token.nextToken();
				
				try {
					client.getDos().writeUTF(User.WHISPER + id + "/" + msg);
					waitRoomArea.append(id + "님에게 귓속말 : " + msg + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				chatField.setText("");
			} else {

				try {
					// 대기실에 메시지 보냄
					client.getDos().writeUTF(User.ECHO01 + "/" + string);
				} catch (IOException e) {
					e.printStackTrace();
				}
				chatField.setText("");
			}
		}
	}

	// exit01메소드는 해당 프로그램이 종료되도록하는 메소드로 사용자가 자동으로 로그아웃되도록 설정한다.
	private void exit01() {
		try {
			client.getUser().getDos().writeUTF(User.LOGOUT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// createRoom은 채팅방을 만들기 위한 메소드로 “대화방 이름을 입력하세요~”라는 다이얼로그가 뜬다. 
	// roomname이 null이라면 취소된다. 방이름을 입력하면 해당 방 객체가 생성된다. 그리고 클라이언트가 접속한 방 목록에 추가된다. 그리고 방 생성이 시작된다.
	private void createRoom() {
		String roomname = JOptionPane.showInputDialog(null,"대화방 이름을 입력하세요~", "ex) 친목해요~");
		if(roomname==null) {	// 취소 버튼
			
		} else {
			Room newRoom = new Room(roomname);	// 방 객체 생성
			newRoom.setRoomNum(lastRoomNum);
			newRoom.setrUI(new RoomUI(client, newRoom));
			
			// 클라이언트가 접속한 방 목록에 추가
			client.getUser().getRoomArray().add(newRoom);
			
			try {
				client.getDos().writeUTF(User.CREATE_ROOM + "/" + newRoom.toProtocol());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}

	// getIn은 채팅방에 들어가기 위한 메소드로 selectedRoom을 통해 선택한 방에 대한 정보가 나타난다. 
	// 해당 정보를 토큰으로 생성한다. 그리고 방 객체를 생성하고 방 번호를 생성한 뒤 RoomUI가 실행된다. 그리고 클라이언트가 접속한 방 목록에 추가한다. 그리고 방 입장이 시작된다.
	private void getIn() {
		// 선택한 방 정보
		String selectedRoom = (String) roomList.getSelectedValue();
		StringTokenizer token = new StringTokenizer(selectedRoom, "/"); // 토큰 생성
		String rNum = token.nextToken();
		String rName = token.nextToken();

		Room theRoom = new Room(rName); // 방 객체 생성
		theRoom.setRoomNum(Integer.parseInt(rNum)); // 방번호 설정
		theRoom.setrUI(new RoomUI(client, theRoom)); // UI

		// 클라이언트가 접속한 방 목록에 추가
		client.getUser().getRoomArray().add(theRoom);

		try {
			client.getDos().writeUTF(User.GETIN_ROOM + "/" + theRoom.getRoomNum());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// maker라는 다이얼로그를 생성한다. 다이얼로그의 타이틀은 “프로그램 정보”이고 400, 170으로 크기를 설정하고 창을 나타낸다.
	// setLocation을 통해 창이 나타날 좌표를 설정한다. 
	// setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);를 사용하여 해당 다이얼로그의 x버튼을 누르면 해당 다이얼로그가 종료되도록 설정한다.
	public void maker() {
		JDialog maker = new JDialog();
		Maker m = new Maker();
		maker.setTitle("프로그램 정보");
		maker.getContentPane().add(m);
		maker.setSize(400, 170);
		maker.setVisible(true);
		maker.setLocation(400, 350);
		maker.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
}

// Maker는 JPanel을 상속한다. super는 자식 클래스에서 상속받는 부모 클래스의 멤버 변수를 참조할 때 사용한다. 그리고 intialize()를 실행한다. 
// intialize()를 실행하면 프로그램 정보에 대한 라벨이 GridLayout 레이아웃에 부착되어 출력된다.
class Maker extends JPanel {
	public Maker() {
		super();
		initialize();
	}

	private void initialize() {
		this.setLayout(new GridLayout(3, 1));

		JLabel j1 = new JLabel("       프로그램 제작자 : 한빛 Class6 5조		");
		JLabel j2 = new JLabel("       수정한 사람 : 유은서		");
		JLabel j3 = new JLabel("       프로그램 버전 : 이클립스  ( 2022 . 1. 26 )		");

		this.add(j1);
		this.add(j2);
		this.add(j3);
	}
}