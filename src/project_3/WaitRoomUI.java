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
	ArrayList<User> userArray; // ����� ��� �迭
	ArrayList<User> friendArray; //ģ�� ��� �迭
	String currentSelectedTreeNode;
	DefaultListModel model, model2;
	DefaultMutableTreeNode level1;		
	DefaultMutableTreeNode level2;	
	DefaultMutableTreeNode level3;	
	DefaultMutableTreeNode level4;	
	
	JScrollPane scrollPane;
    ImageIcon icon;
    LoginUI login;
    Color c = new Color(206, 242, 121);	//���� �߰�

    // WaitRoomUI �����ڸ� �����. ����ڿ� ���� �Ű������� ���� �޴´�. Ÿ��Ʋ �̸��� ���ƿ� ä�ù桱�̴�. 
    // userArray�� ����Ͽ� ����� ��� �迭�� �߰��Ѵ�. initialize();�� �Է��Ͽ� �޼ҵ带 �����Ų��.
	public WaitRoomUI(EightClient eigClient) {
		setTitle("�߿� ä�ù�");
		userArray = new ArrayList<User>();
		client = eigClient;
		initialize();
	}

	private void initialize() {
		
		icon = new ImageIcon("icon2.png");
		this.setIconImage(icon.getImage());
		// ImageIcon�� setIconImage�� ����Ͽ� Ÿ��Ʋ�ٿ� �̹����� �����Ѵ�.
		
		setBounds(100, 100, 800, 500); // setBounds() �޼ҵ�� â ũ�⸦ �����ϴ� �޼ҵ�� setBounds(������ġ, ������ġ, ���α���, ���α���);�� �����Ǿ� �ִ�.
		setBackground(c);	//���� �߰�(����)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �������� ������ ��� x ��ư�� ������ �ش� �������� �����Ѵ�.
		setBackground(c);
		setResizable(false); // â�� ũ�⸦ ������ �� ������ �ϴ� ���̴�.

		// JMenuBar�� ����Ͽ� ���α׷�â ��ܿ� ǥ�õǴ� menuBar��� �޴��� �����ϰ� setJMenuBar�� ����Ͽ� �ִ´�.
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// �����ϡ��̶�� basicMenus �޴��� �����Ѵ�. �׸��� addActionListener(this)�� ����Ͽ� �͸� Ŭ������ ��ư �̺�Ʈ�� �߰����ش�. �׸��� ��� �޴��ٿ� �����ϡ��̶�� �޴��� �߰��Ѵ�.
		JMenu basicMenus = new JMenu("����");
		basicMenus.addActionListener(this);
		menuBar.add(basicMenus);

		// �����Ϸ� ���塱�̶�� mitSaveChatToFile �޴��� �����Ѵ�. �׸��� addActionListener(this)�� ����Ͽ� �͸� Ŭ������ ��ư �̺�Ʈ�� �߰����ش�. �׸��� ��� �޴��ٿ� �����Ϸ� ���塱�̶�� �޴��� �߰��Ѵ�.
		JMenuItem mitSaveChatToFile = new JMenuItem("���Ϸ�����");
		mitSaveChatToFile.addActionListener(this);
		basicMenus.add(mitSaveChatToFile);
		
		// ���� ���⡱�̶�� mitLoadChatFromFile �޴��� �����Ѵ�. �׸��� addActionListener(this)�� ����Ͽ� �͸� Ŭ������ ��ư �̺�Ʈ�� �߰����ش�. �׸��� ��� �޴��ٿ� ������ ���⡱��� �޴��� �߰��Ѵ�.
		JMenuItem mitLoadChatFromFile = new JMenuItem("���Ͽ���");
		mitLoadChatFromFile.addActionListener(this);
		basicMenus.add(mitLoadChatFromFile);
		

		// ������/Ż�𡱶�� updndel �޴��� �����Ѵ�. �׸��� addActionListener(this)�� ����Ͽ� �͸� Ŭ������ ��ư �̺�Ʈ�� �߰����ش�. �׸��� ��� �޴��ٿ� ������/Ż�𡱶�� �޴��� �߰��Ѵ�.
		JMenu updndel = new JMenu("����/Ż��");
		updndel.addActionListener(this);
		menuBar.add(updndel);

		// ȸ������ ���桱�̶�� changeInfo �޴��� �����Ѵ�. �׸��� addActionListener(this)�� ����Ͽ� �͸� Ŭ������ ��ư �̺�Ʈ�� �߰����ش�. �׸��� ��� �޴��ٿ� ��ȸ������ ���桱��� �޴��� �߰��Ѵ�.
		JMenuItem changeInfo = new JMenuItem("ȸ������ ����");
		changeInfo.addActionListener(this);
		updndel.add(changeInfo);
		
		// ��ȸ�� Ż�𡱶�� withdrawMem �޴��� �����Ѵ�. �׸��� addActionListener(this)�� ����Ͽ� �͸� Ŭ������ ��ư �̺�Ʈ�� �߰����ش�. �׸��� ��� �޴��ٿ� ��ȸ�� Ż�𡱶�� �޴��� �߰��Ѵ�.
		JMenuItem withdrawMem = new JMenuItem("ȸ�� Ż��");
		withdrawMem.addActionListener(this);
		updndel.add(withdrawMem);
		
		// �����򸻡��̶�� helpMenus �޴��� �����Ѵ�. �׸��� addActionListener(this)�� ����Ͽ� �͸� Ŭ������ ��ư �̺�Ʈ�� �߰����ش�. �׸��� ��� �޴��ٿ� �����򸻡���� �޴��� �߰��Ѵ�.
		JMenu helpMenus = new JMenu("����");
		helpMenus.addActionListener(this);
		menuBar.add(helpMenus);

		// �����α׷� ��������� proInfoItem �޴��� �����Ѵ�. �׸��� addActionListener(this)�� ����Ͽ� �͸� Ŭ������ ��ư �̺�Ʈ�� �߰����ش�. �׸��� ��� �޴��ٿ� �����α׷� ��������� �޴��� �߰��Ѵ�.
		JMenuItem proInfoItem = new JMenuItem("���α׷� ����");
		proInfoItem.addActionListener(this);
		helpMenus.add(proInfoItem);
		getContentPane().setLayout(null);

		// �� �г��̴�. �ش� �гο� ������ �ֱ� ���� setBorder�� ����Ѵ�. TitledBorder�� ������Ʈ ������ ������ �� �� �ִ� �����̴�, ���ΰ� ���� �ٸ� ������ ȿ���� �Բ� �� �� �ִ�. 
		// setBounds�� ä�� �г��� ��ġ, �ʺ�, ���̸� �����ϰ� �ش� �г��� �������ҿ� ���δ�. ������ �����ϰ� ��ġ�����ڸ� �̿��Ͽ� ��ġ ����� �����Ѵ�. ä�� �г��� BorderLayout���� �����Ͽ���.
		JPanel room = new JPanel();
		room.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "ä �� ��", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		room.setBounds(12, 10, 477, 215);	
		room.setBackground(c);		//�����߰�
		getContentPane().add(room);
		room.setLayout(new BorderLayout(0, 0));

		// ��ũ�� ����(scroll pane)�� ������Ʈ�� ��ũ�� ����� �����Ѵ�. scrollPane�� BorderLayout�� ���Ϳ� �����Ѵ�.
		JScrollPane scrollPane = new JScrollPane();
		room.add(scrollPane, BorderLayout.CENTER);

		// ���ǿ� �ִ� ������� ����Ʈ�� ����ϱ� ���� ����Ʈ ��ü�� ���� �����Ѵ�. addMouseListener�� ����Ͽ� ���콺�� Ŭ�� �Ǿ��� �� ��Ÿ���� �̺�Ʈ�̴�. 
		// ä�ù� ��� �� �ϳ��� ������ ��� ������ ���� �� ��ȣ�� �����Ѵ�.
		roomList = new JList(new DefaultListModel());
		roomList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = roomList.getFirstVisibleIndex();
				// System.out.println(">>>>>>>>>>>" + i);
				if (i != -1) {
					// ä�ù� ��� �� �ϳ��� ������ ���,
					// ������ ���� ���ȣ�� ����
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
		
		// ������Ʈ�� ���� ������ �����ϱ� ���� DefaultListModel�� ����Ѵ�. setViewportView�� ����Ͽ� ��ũ�� ������ Ŭ���̾�Ʈ�� roomList�� �����ϰ� ��ũ�� ����� �߰��Ѵ�.
		model = (DefaultListModel) roomList.getModel();
		scrollPane.setViewportView(roomList);

		// panel2�̶�� �г��� ����� room�� ���ʿ� �߰��Ѵ�.
		JPanel panel2 = new JPanel();
		room.add(panel2, BorderLayout.SOUTH);

		// ä�ù��� ����� ���� �� ����� ��ư�� �����. �ش� ��ư�� Ŭ���Ǹ� createRoom�� ����ȴ�.
		makeRoomBtn = new JButton("�� �����");
		makeRoomBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		makeRoomBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// �游��� ��ư Ŭ��
				createRoom();
			}
		});
		
		//panel2�̶�� �г����� GridLayout�� �߰��ϰ� ���� ����⡱ ��Ʋ�� �ش� �гο� �߰��Ѵ�.
		panel2.setLayout(new GridLayout(0, 2, 0, 0));
		panel2.add(makeRoomBtn);

		// ä�ù濡 ���� ���� ���� �����ϱ⡱ ��ư�� �����Ѵ�. �ش� ��ư�� Ŭ���Ǹ� getIn()�� ����ǰ� �ش� ��ư�� panel2�� �߰��Ѵ�.
		getInRoomBtn = new JButton("�� �����ϱ�");
		getInRoomBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// �� ����
				getIn();
			}
		});
		panel2.add(getInRoomBtn);

		// user��� �г��� �����Ѵ�. �ش� �гο� ������ �ֱ� ���� setBorder�� ����Ѵ�. 
		// TitledBorder�� ������Ʈ ������ ������ �� �� �ִ� �����̴�, ���ΰ� ���� �ٸ� ������ ȿ���� �Բ� �� �� �ִ�. 
		// setBounds�� ä�� �г��� ��ġ, �ʺ�, ���̸� �����ϰ� �ش� �г��� �������ҿ� ���δ�. ������ �����ϰ� ��ġ�����ڸ� �̿��Ͽ� ��ġ ����� �����Ѵ�. ä�� �г��� BorderLayout���� �����Ͽ���.
		JPanel user = new JPanel();
		user.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),	"����� ���", TitledBorder.CENTER,	TitledBorder.TOP, null, null));
		user.setBounds(501, 10, 271, 409);
		user.setBackground(c);		//�����߰�
		getContentPane().add(user);
		user.setLayout(new BorderLayout(0, 0));

		// ��ũ�� ����(scroll pane)�� ������Ʈ�� ��ũ�� ����� �����Ѵ�. scrollPane�� user �г��� BorderLayout�� ���Ϳ� �����Ѵ�.
		JScrollPane scrollPane1 = new JScrollPane();
		user.add(scrollPane1, BorderLayout.CENTER);

		// ����� ����� Ʈ�������� ��Ÿ����. addTreeSelectionListener�� Ʈ�� ��尡 ���� �Ǿ��� �� �Ͼ�� �̺�Ʈ�̴�. ���� ���� Ʈ�� �н��� toString�Ͽ� �ش� Ʈ���� ��ҵ��� [ROOT��, �ڽ�, ����, ��]�� ����Ѵ�.
		userTree = new JTree();
		userTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				currentSelectedTreeNode = e.getPath().getLastPathComponent().toString();
			}
		});
		
		// �� ������ ��带 �߰��Ѵ�. �� ����� �������� �����ϱ� ���� DefaultTreeCellRenderer�� ����Ͽ� �������� �����Ѵ�.
		// setLeafIcon�� leafnode�� �������� �ش� �������� �����ϴ� ���̰� setCloseIcon�� ������ node�� ������, setOpenIcon�� ���� node�� �������� �����Ѵ�. 
		level1 = new DefaultMutableTreeNode("������");
		level2 = new DefaultMutableTreeNode("ä�ù�");
		level3 = new DefaultMutableTreeNode("����");
		level4 = new DefaultMutableTreeNode("ģ�����");
		level1.add(level2);
		level1.add(level3);
		level1.add(level4);
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(new ImageIcon("user.png"));
		renderer.setClosedIcon(new ImageIcon("wait.png"));
		renderer.setOpenIcon(new ImageIcon("open.png"));

		// userTree�� �ش� �����ܵ��� �����ϰ� setEditable(false);�� �Ͽ� ���� �Ұ� ������ �Ѵ�.
		userTree.setCellRenderer(renderer);
		userTree.setEditable(false);

		// model�̶�� DefaultTreeModel�� �����Ѵ�. setModel�� ����Ͽ� �ش� Ʈ�� ������ �����Ѵ�. 
		// setViewportView�� ����Ͽ� ��ũ�� ������ Ŭ���̾�Ʈ�� roomList�� �����ϰ� ��ũ�� ����� �߰��Ѵ�.
		DefaultTreeModel model = new DefaultTreeModel(level1);
		userTree.setModel(model);

		scrollPane1.setViewportView(userTree);

		// panel1�̶�� �г��� �����ϰ� user�� BorderLayout�� ���ʿ� �߰��Ѵ�. panel1�� GridLayout���� �����ϰ� ������ �߰��Ѵ�.
		JPanel panel1 = new JPanel();
		user.add(panel1, BorderLayout.SOUTH);
		panel1.setLayout(new GridLayout(1, 0, 0, 0));
		panel1.setBackground(c);		//�����߰�

		// whisperBtn�̶�� ���ӼӸ��� ��ư�� �߰��ϰ� �ش� ������Ʈ�� �׼� �̺�Ʈ�� ����Ѵ�. �׸��� panel1�� �߰��Ѵ�.
		whisperBtn = new JButton("�ӼӸ�");
		
		whisperBtn.addActionListener(this);
		panel1.add(whisperBtn);

		// waitroom�̶�� �г��� �����Ѵ�. �ش� �гο� ������ �ֱ� ���� setBorder�� ����Ѵ�. TitledBorder�� ������Ʈ ������ ������ �� �� �ִ� �����̴�, 
		// ���ΰ� ���� �ٸ� ������ ȿ���� �Բ� �� �� �ִ�. setBounds�� ä�� �г��� ��ġ, �ʺ�, ���̸� �����ϰ� �ش� �г��� �������ҿ� ���δ�. 
		// ������ �����ϰ� ��ġ�����ڸ� �̿��Ͽ� ��ġ ����� �����Ѵ�. �ش� �г��� ������ �����ϰ� waitroom �г��� BorderLayout���� �����Ͽ���.
		
		exitBtn = new JButton("����");	
		exitBtn.addActionListener(this);
		panel1.add(exitBtn);
		
		// exitBtn�� �����Ѵ�. �׸��� addActionListener(this)�� ����Ͽ� �͸� Ŭ������ ��ư �̺�Ʈ�� �߰����ش�.
		// �ش� ��ư�� panel1�� ���δ�.
		
		friendBtn = new JButton("ģ���߰�");	
		friendBtn.addActionListener(this);
		panel1.add(friendBtn);
		
		JPanel waitroom = new JPanel();
		
		waitroom.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "�� �� ��",	TitledBorder.CENTER, TitledBorder.TOP, null, Color.DARK_GRAY));
		waitroom.setBounds(12, 235, 477, 185);
		waitroom.setBackground(c);		//�����߰�
		getContentPane().add(waitroom);
		waitroom.setLayout(new BorderLayout(0, 0));

		// panel�̶�� �г��� �����Ѵ�. waitroom�� panel�� ���ʿ� �߰��Ѵ�. BoxLayout.X_AXIS�� �ش� ������Ʈ�� ��->��� ��ġ�ϴ� ���̴�. �г��� ������ �����Ѵ�.
		JPanel panel = new JPanel();
		waitroom.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setBackground(c);		//�����߰�

		// ��ũ�� ����(scroll pane)�� ������Ʈ�� ��ũ�� ����� �����Ѵ�. scrollPane4�� panel�� �����Ѵ�.
		JScrollPane scrollPane4 = new JScrollPane();
		panel.add(scrollPane4);

		// chatField��� JTextField�� �����Ѵ�. addKeyListener�� ����Ͽ� ����Ű ������ �� �޽����� ���۵ǵ��� �����Ѵ�. 
		// setViewportView�� ����Ͽ� ��ũ�� ������ Ŭ���̾�Ʈ�� chatField�� �����ϰ� ��ũ�� ����� �߰��Ѵ�. 
		// setColumns�� ����Ͽ� �ؽ�Ʈ �ʵ��� �ִ� �Է� ������ �����Ѵ�. 
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

		// �޽����� ���� �� �ִ� sendBtn ��ư�� �����. �ش� ��ư�� Ŭ���ϸ� msgSummit�� ����ǰ�
		// requestFocus()�� ����Ͽ� Ű �̺�Ʈ ���� ������Ʈ�� chatField�� �����Ѵ�.
		sendBtn = new JButton("������");
		sendBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				msgSummit();
				chatField.requestFocus();
			}
		});
		panel.add(sendBtn);

		// ��ũ�� ����(scroll pane)�� ������Ʈ�� ��ũ�� ����� �����Ѵ�. scrollPane2�� waitrooml�� ���Ϳ� �����Ѵ�.
		JScrollPane scrollPane2 = new JScrollPane();
		waitroom.add(scrollPane2, BorderLayout.CENTER);

		// ���� ä��â�� ����� ���� JTextArea�� �̿��Ͽ� waitRoomArea�� �����Ѵ�. setEditable(false);�� ����Ͽ� ���� �Ұ� ������ �Ѵ�.
		// setViewportView�� ����Ͽ� ��ũ�� ������ Ŭ���̾�Ʈ�� waitRoomArea�� �����ϰ� ��ũ�� ����� �߰��Ѵ�. 
		waitRoomArea = new JTextArea();
		waitRoomArea.setEditable(false);
		scrollPane2.setViewportView(waitRoomArea);

		// info��� �г��� �����Ѵ�. �ش� �гο� lbid�� lbnick��� ���� �߰��Ѵ�. lbip��� JTextField�� �����ϰ� ���� �Ұ� ������ �Ѵ�. 
		// �ش� �ؽ�Ʈ �ʵ带 Info�� �߰��ϰ� setColumns�� ����Ͽ� �ؽ�Ʈ �ʵ��� �ִ� �Է� ������ �����Ѵ�.
		JPanel info = new JPanel();
		lbid = new JLabel("-");
		info.add(lbid);
		lbnick = new JLabel("-");
		info.add(lbnick);
		lbip = new JTextField();
		lbip.setEditable(false);
		info.add(lbip);
		lbip.setColumns(10);

		// chatField�� requestFocus()�� ����Ͽ� Ű �̺�Ʈ ���� ������Ʈ�� chatField�� �����Ѵ�.
		// setVisible(true);�� �Ͽ� â�� ȭ�鿡 ��Ÿ����. �׸��� �ٽ� requestFocus()�� ����Ͽ� Ű �̺�Ʈ ���� ������Ʈ�� chatField�� �����Ѵ�.
		chatField.requestFocus();
		setVisible(true);
		chatField.requestFocus();

		// addWindowListener�� ����Ͽ� ������ �̺�Ʈ�� ����Ѵ�. ������ â�� ������ exit01()�� ����ǵ��� �Ѵ�.
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
		case "�ӼӸ�":
			// �г��� �����ϰ� ���̵� ����
			StringTokenizer token = new StringTokenizer(currentSelectedTreeNode, "("); // ��ū ����
			temp = token.nextToken(); // ��ū���� �и��� ��Ʈ��
			temp = token.nextToken();
			id = "/" + temp.substring(0, temp.length() - 1) + " ";
			chatField.setText(id);
			chatField.requestFocus();
			break;
		case "����":
			int ans1 = JOptionPane.showConfirmDialog(this, "���� ���� �Ͻðڽ��ϱ�?", "����Ȯ��", JOptionPane.OK_CANCEL_OPTION);
			if (ans1 == 0) {
				// System.exit(0); // ���� ����
				try {
					client.getUser().getDos().writeUTF(User.LOGOUT);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		case "ģ���߰�":  //ģ�� �߰� ��ư�� ������ ��
			StringTokenizer token1 = new StringTokenizer(currentSelectedTreeNode, "("); // ��ū ����
			temp = token1.nextToken(); // ��ū���� �и��� ��Ʈ��
			temp = token1.nextToken();
			id = "/" + temp.substring(0, temp.length() - 1) + " ";
			int ans2 = JOptionPane.showConfirmDialog(this, "ģ�� �߰��Ͻðڽ��ϱ�??", "ģ���߰�", JOptionPane.OK_CANCEL_OPTION);
			if (ans2 == 0) {
				try {
					client.getUser().getDos().writeUTF(User.UPDATE_FRIENDLIST+id);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		// �޴�1 ���� �޴�
		case "ȸ������ ����":
			DBRevise reDB = new DBRevise();
			reDB.myInfo(id);
			break;
		case "ȸ�� Ż��":
			DBDelete delDB = new DBDelete();

			int ans = JOptionPane.showConfirmDialog(this, "���� Ż�� �Ͻðڽ��ϱ�?", "Ż��Ȯ��", JOptionPane.OK_CANCEL_OPTION);

			if (ans == 0) {
				int i = 0;
				i = delDB.InfoDel(id);
				if (i == 0) {
					// msgbox.messageBox(this, "Ż��� ������..:)");
				} else {
					msgbox.messageBox(this, "Ż�� �����Ͽ����ϴ�..:(");
					exit01();
				}
			}
			break;
		case "���Ϸ�����":
			String filename = UtilFileIO.saveFile(waitRoomArea);
			JOptionPane.showMessageDialog(waitRoomArea.getParent(), "ä�ó����� ���ϸ�(" + filename + ")���� �����Ͽ����ϴ�", "ä�ù��", JOptionPane.INFORMATION_MESSAGE);
			break;
		case "���Ͽ���":
			filename = UtilFileIO.getFilenameFromFileOpenDialog("./");
			if (filename == "") break;
			String text = UtilFileIO.loadFile(filename);
			TextViewUI textview = new TextViewUI(text);
			break;
		case "���α׷� ����":
			maker();
			break;
		}
		// actionPerformed(ActionEvent e)�� �����Ͽ� ActionEvent�� �߻��ϸ� �ش� �޼ҵ尡 ȣ��ȴ�. getId()�� ����Ͽ� ������� id�� �ް� geActionCommand():�� ����Ͽ� �̺�Ʈ�� �߻��� ��ü�� �ؽ�Ʈ�� ���ϵȴ�. 
		// ���� ���ӼӸ����̶�� ��ū�� ������ �� �ش� ���ڿ��� ��ū���� �и��Ѵ�. id�� �г����� �����ϰ� ���̵� ������ chatField�� setText�� �̿��Ͽ� id�� �ش� �ؽ�Ʈ �ڽ� ���� ���ڿ��� �����Ѵ�. requestFocus();�� ����Ͽ� Ű �̺�Ʈ ���� ������Ʈ�� chatField�� �����Ѵ�.
		// ���� ��ȸ������ ���桱�̶�� DBRevise���� reDB�� �����Ͽ� �ش� id�� ������ ȸ�������� �����Ѵ�.
		// ���� ��ȸ�� Ż�𡱶�� DBDelete���� delDB�� �����ϰ� ������ Ż�� �Ͻðڽ��ϱ�?����� ���̾�α׸� ����Ͽ� Ż��Ȯ���� ������ �ش� id�� ���޵ǰ� Ż�� �����ǵ��� �Ѵ�.
		// ���� �����ᡱ��� ������ ���� �Ͻðڽ��ϱ�?����� ���̾�αװ� ��Ÿ���� ����Ȯ�� ��ư�� ������ ����ڰ� �α׾ƿ� �ǵ��� �Ѵ�.
		// ���� �����Ϸ����塱�̶�� FileDialog���� ������ ���� �̸��� �����ͼ� �ش� �̸��� ��������� ����ǰ� ���� �̸��� ������ �ִٸ� TextViewUI�� ���� ���������� �Ѵ�.
		// ���� �����α׷� ��������� maker()�� ����ǵ��� �Ѵ�.

	}
	
	// �޽��� ������ �ϱ� ���� ����Ǵ� �޼ҵ�� �ش� ���ڿ����� ��ū�� �����ϰ� ��Ʈ���� ��ū���� �и��Ѵ�. ���࿡ ����ڰ� �ӼӸ��� ����ϸ� �ش� ������ waitRoomArea�� ���޵ȴ�. 
	// ���࿡ ����ڰ� ���ǿ� �޽����� ������ ���ǿ� �ش� �޽����� ���޵ȴ�.
	private void msgSummit() {
		String string = chatField.getText();// �޽�������
		if (!string.equals("")) {
			if (string.substring(0, 1).equals("/")) {
				
				StringTokenizer token = new StringTokenizer(string, " "); // ��ū ����
				String id = token.nextToken(); // ��ū���� �и��� ��Ʈ��
				String msg = token.nextToken();
				
				try {
					client.getDos().writeUTF(User.WHISPER + id + "/" + msg);
					waitRoomArea.append(id + "�Կ��� �ӼӸ� : " + msg + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				chatField.setText("");
			} else {

				try {
					// ���ǿ� �޽��� ����
					client.getDos().writeUTF(User.ECHO01 + "/" + string);
				} catch (IOException e) {
					e.printStackTrace();
				}
				chatField.setText("");
			}
		}
	}

	// exit01�޼ҵ�� �ش� ���α׷��� ����ǵ����ϴ� �޼ҵ�� ����ڰ� �ڵ����� �α׾ƿ��ǵ��� �����Ѵ�.
	private void exit01() {
		try {
			client.getUser().getDos().writeUTF(User.LOGOUT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// createRoom�� ä�ù��� ����� ���� �޼ҵ�� ����ȭ�� �̸��� �Է��ϼ���~����� ���̾�αװ� ���. 
	// roomname�� null�̶�� ��ҵȴ�. ���̸��� �Է��ϸ� �ش� �� ��ü�� �����ȴ�. �׸��� Ŭ���̾�Ʈ�� ������ �� ��Ͽ� �߰��ȴ�. �׸��� �� ������ ���۵ȴ�.
	private void createRoom() {
		String roomname = JOptionPane.showInputDialog(null,"��ȭ�� �̸��� �Է��ϼ���~", "ex) ģ���ؿ�~");
		if(roomname==null) {	// ��� ��ư
			
		} else {
			Room newRoom = new Room(roomname);	// �� ��ü ����
			newRoom.setRoomNum(lastRoomNum);
			newRoom.setrUI(new RoomUI(client, newRoom));
			
			// Ŭ���̾�Ʈ�� ������ �� ��Ͽ� �߰�
			client.getUser().getRoomArray().add(newRoom);
			
			try {
				client.getDos().writeUTF(User.CREATE_ROOM + "/" + newRoom.toProtocol());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}

	// getIn�� ä�ù濡 ���� ���� �޼ҵ�� selectedRoom�� ���� ������ �濡 ���� ������ ��Ÿ����. 
	// �ش� ������ ��ū���� �����Ѵ�. �׸��� �� ��ü�� �����ϰ� �� ��ȣ�� ������ �� RoomUI�� ����ȴ�. �׸��� Ŭ���̾�Ʈ�� ������ �� ��Ͽ� �߰��Ѵ�. �׸��� �� ������ ���۵ȴ�.
	private void getIn() {
		// ������ �� ����
		String selectedRoom = (String) roomList.getSelectedValue();
		StringTokenizer token = new StringTokenizer(selectedRoom, "/"); // ��ū ����
		String rNum = token.nextToken();
		String rName = token.nextToken();

		Room theRoom = new Room(rName); // �� ��ü ����
		theRoom.setRoomNum(Integer.parseInt(rNum)); // ���ȣ ����
		theRoom.setrUI(new RoomUI(client, theRoom)); // UI

		// Ŭ���̾�Ʈ�� ������ �� ��Ͽ� �߰�
		client.getUser().getRoomArray().add(theRoom);

		try {
			client.getDos().writeUTF(User.GETIN_ROOM + "/" + theRoom.getRoomNum());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// maker��� ���̾�α׸� �����Ѵ�. ���̾�α��� Ÿ��Ʋ�� �����α׷� �������̰� 400, 170���� ũ�⸦ �����ϰ� â�� ��Ÿ����.
	// setLocation�� ���� â�� ��Ÿ�� ��ǥ�� �����Ѵ�. 
	// setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);�� ����Ͽ� �ش� ���̾�α��� x��ư�� ������ �ش� ���̾�αװ� ����ǵ��� �����Ѵ�.
	public void maker() {
		JDialog maker = new JDialog();
		Maker m = new Maker();
		maker.setTitle("���α׷� ����");
		maker.getContentPane().add(m);
		maker.setSize(400, 170);
		maker.setVisible(true);
		maker.setLocation(400, 350);
		maker.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
}

// Maker�� JPanel�� ����Ѵ�. super�� �ڽ� Ŭ�������� ��ӹ޴� �θ� Ŭ������ ��� ������ ������ �� ����Ѵ�. �׸��� intialize()�� �����Ѵ�. 
// intialize()�� �����ϸ� ���α׷� ������ ���� ���� GridLayout ���̾ƿ��� �����Ǿ� ��µȴ�.
class Maker extends JPanel {
	public Maker() {
		super();
		initialize();
	}

	private void initialize() {
		this.setLayout(new GridLayout(3, 1));

		JLabel j1 = new JLabel("       ���α׷� ������ : �Ѻ� Class6 5��		");
		JLabel j2 = new JLabel("       ������ ��� : ������		");
		JLabel j3 = new JLabel("       ���α׷� ���� : ��Ŭ����  ( 2022 . 1. 26 )		");

		this.add(j1);
		this.add(j2);
		this.add(j3);
	}
}