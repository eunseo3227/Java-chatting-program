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
	Color c = new Color(255, 216, 216);	//���� �߰�(��ȫ��)
	
    ImageIcon icon;

    //RoomUI �����ڸ� �����. ����ڿ� ���� �Ű������� ���� �޴´�. �׸��� Ÿ��Ʋ�ٿ� �̹����� �߰��ߴ�. initialize();�� �Է��Ͽ� �޼ҵ带 �����Ų��.
	public RoomUI(EightClient client, Room room) {
		this.client = client;
		this.room = room;
		setTitle("�߿� ä�ù� : " + room.toProtocol());
		icon = new ImageIcon("icon2.png");
		this.setIconImage(icon.getImage());//Ÿ��Ʋ�ٿ� �̹����ֱ�
		initialize();
	}

	private void initialize() {
		setBounds(100, 100, 502, 481);	//setBounds() �޼ҵ�� â ũ�⸦ �����ϴ� �޼ҵ�� setBounds(������ġ, ������ġ, ���α���, ���α���);�� �����Ǿ� �ִ�.
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //�������� ������ ��� x ��ư�� ������ �ش� �������� �����Ѵ�.
		getContentPane().setLayout(null); //���̾ƿ��� �����ϴ� ���̴�.
		setBackground(c);	//���� �߰�(����)
		setResizable(false);	//â�� ũ�⸦ ������ �� ������ �ϴ� ���̴�.

		// ä�� �г��̴�. ä�� �г��� ��ġ, �ʺ�, ���̸� �����ϰ� �ش� �г��� �������ҿ� ���δ�. ������ �����ϰ� ��ġ�����ڸ� �̿��Ͽ� ��ġ ����� �����Ѵ�. 
		// ä�� �г��� BorderLayout���� �����Ͽ���.
		final JPanel panel = new JPanel();
		panel.setBounds(12, 10, 300, 358);
		getContentPane().add(panel);
		getContentPane().setBackground(c);	//���� �߰�
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		//��ũ�� ����(scroll pane)�� ������Ʈ�� ��ũ�� ����� �����Ѵ�. scrollPane�� BorderLayout�� ���Ϳ� �����Ѵ�.

		chatArea = new JTextArea();
		chatArea.setBackground(Color.WHITE);
		chatArea.setEditable(false); // �����Ұ�
		scrollPane.setViewportView(chatArea); // ȭ�� ����
		chatArea.append("�� �ų� ä�� ��Ź�帳�ϴ�:) ��\n");
		//ä�� ������ �����ִ� chatArea�� �����. ������ �Ͼ������ �����ϰ� ���� �� �� ������ �����Ѵ�. setViewportView�� �̿��Ͽ� jScrollPane�� JTextArea�� �߰��Ѵ�.
		// ä��â�� ���� �ų� ä�� ��Ź�帳�ϴ�:) �ء���� ������ �߰��Ѵ�.

		JPanel panel1 = new JPanel();
		// ä��â�� ä���� �������� ���� �ۼ��ϴ� �г��� �߰��Ѵ�. 
		panel1.setBounds(12, 378, 300, 34);
		getContentPane().add(panel1);
		panel1.setLayout(new BorderLayout(0, 0));

		// ä���� �ۼ��� �� �ִ� JTextField�̴�. setColumns�� ����Ͽ� �ؽ�Ʈ �ʵ��� �ִ� �Է� ������ �����Ѵ�. 
		// addKeyListener�� ����Ͽ� ����Ű ������ �� �޽����� ���۵ǵ��� �����Ѵ�.
		chatField = new JTextField();
		panel1.add(chatField);
		chatField.setColumns(10);
		chatField.addKeyListener(new KeyAdapter() {
			// ���� ��ư �̺�Ʈ
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					msgSummit();
				}
			}

		});

		// ä�ù� �����ڸ� ��Ÿ���� ���� ������ �г��� �����Ѵ�. ������ �гο��� ��ũ���� �߰��Ͽ���.
		JPanel panel2 = new JPanel();
		panel2.setBounds(324, 10, 150, 358);
		getContentPane().add(panel2);
		panel2.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane1 = new JScrollPane();
		panel2.add(scrollPane1, BorderLayout.CENTER);

		// ä�ù� �����ڸ� ����Ʈ�� �����ϱ� ���� JList�� �߰��Ѵ�. ���� setViewportView�� ����Ͽ� jScrollPane�� JList�� �߰��Ѵ�.
		uList = new JList(new DefaultListModel());
		model = (DefaultListModel) uList.getModel();
		scrollPane1.setViewportView(uList);

		// send button
		// ä�ù濡 �޽����� ������ ���� ������ ��ư�� �����Ѵ�. ActionListener��� �������̽� �ȿ� addActionListener�� �����ε��Ͽ� ��ư�� ������ �޽����� ��������
		// requestFocus();�� �̿��Ͽ� chatField�� ��Ŀ���� �־� Ű �̺�Ʈ�� ���� ������Ʈ�� ������ �����Ѵ�. �׸��� ������ ��ư�� ����Ʈ�ҿ� ��ġ�� �����Ͽ� ���δ�.
		JButton roomSendBtn = new JButton("������");
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
		// ä�ù濡�� ������ ���� ������ ��ư�� �����Ѵ�. ������ ��ư�� Ŭ���ϸ� showConfirmDialog�� ����Ͽ� ������ ���� �Ͻðڽ��ϱ�?����� �޽��� ���̾�α׸� ����. 
		// OK_CANCEL_OPTION�� ���� ����Ȯ���� ������ 0�� �����Ͽ� ans�� 0�� �ȴ�.
		JButton roomExitBtn = new JButton("������");
		roomExitBtn.addMouseListener(new MouseAdapter() {		// ������ ��ư
			@Override
			public void mouseClicked(MouseEvent e) {
				int ans = JOptionPane.showConfirmDialog(panel, "���� ���� �Ͻðڽ��ϱ�?", "����Ȯ��", JOptionPane.OK_CANCEL_OPTION);

				if (ans == 0) { // ���࿡ ����Ȯ�� ��ư�� ������ ����ڴ� �濡�� ������ �ǰ� �ش� ���� �� �迭���� �����ȴ�. �׸��� setVisible(false);�� ����Ͽ� â�� ȭ�鿡�� ���ش�.
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
		//������ ��ư�� ����Ʈ�ҿ� �����Ѵ�. �׸��� JMenuBar�� ����Ͽ� �޴��� �����. �׸��� setJMenuBar�� ����Ͽ� �����ӿ� �޴��� �ְ� setVisible(true);�� �̿��Ͽ� â�� ȭ�鿡 ��Ÿ����.

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		setVisible(true);
		
		//////////////////////////////////////
		// ä�������̶�� �޴��� �߰��Ͽ� ä�� ������ ������ �� �ֵ��� �Ѵ�. ä�������� ������ ���Ϸ������̶�� JMenuItem�� �߰��Ͽ� ä�� ������ ���Ϸ� �����ϵ��� �Ѵ�.
		JMenu mnuSaveChat = new JMenu("ä������");
		mnuSaveChat.addActionListener(this);
		menuBar.add(mnuSaveChat);
		JMenuItem mitSaveChatToFile = new JMenuItem("���Ϸ�����");
		mitSaveChatToFile.addActionListener(this);
		mnuSaveChat.add(mitSaveChatToFile);
		
		// ����ä��Ȯ���̶�� �޴��� �߰��Ͽ� ����� ä�� ������ Ȯ���� �� �ֵ��� �Ѵ�. ����ä��Ȯ���� ������ ���Ͽ����� JMenuItem�� �߰��Ͽ� ����� ä�� ������ �� �� �ֵ��� �Ѵ�.
		JMenu mnuLoadChat = new JMenu("����ä��Ȯ��");
		mnuLoadChat.addActionListener(this);
		menuBar.add(mnuLoadChat);
		JMenuItem mitLoadChatFromFile = new JMenuItem("���Ͽ���");
		mitLoadChatFromFile.addActionListener(this);
		mnuLoadChat.add(mitLoadChatFromFile);
		
		
		// WindowAdapter�� ����Ͽ� WindowListener�� �����Ѵ�. �̸� ���� ������â�� �����Ѵ�. ������â�� �����ϸ� �ش� ä�ù��� �迭���� �����Ѵ�.
		chatField.requestFocus();
		this.addWindowListener(new WindowAdapter() {	// ������ ������
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

	// msgSummit �޼ҵ带 �̿��Ͽ� ä�� �ʵ忡 �ؽ�Ʈ�� �޾Ƽ� ä�ù濡 �޽����� �������� �Ѵ�.
	private void msgSummit() {
		String string = chatField.getText();
		if (!string.equals("")) {
			try {
				// ä�ù濡 �޽��� ����
				client.getDos()
						.writeUTF(User.ECHO02 + "/" + room.getRoomNum() + "/" + client.getUser().toString() + string);
				chatField.setText("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	// actionPerformed �޼ҵ带 ���� getActionCommand()�� �̿��Ͽ� �̺�Ʈ�� �߻���Ų ��ü�� ���ڿ��� �����´�. ���Ϸ������̶�� ä�ù��� ������ ���Ͽ� �����Ѵ�. 
	// ���Ͽ����� �ش� ������ TextViewUI�� �̿��Ͽ� �ش� ������ ����.
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "���Ϸ�����":
				String filename = UtilFileIO.saveFile(chatArea);
				JOptionPane.showMessageDialog(chatArea.getParent(), 
						"ä�ó����� ���ϸ�(" + filename + ")���� �����Ͽ����ϴ�", 
						"ä�ù��", JOptionPane.INFORMATION_MESSAGE);
				break;
			case "���Ͽ���":
				filename = UtilFileIO.getFilenameFromFileOpenDialog("./");
				String text = UtilFileIO.loadFile(filename);
				TextViewUI textview = new TextViewUI(text);
				break;
		}
	}
}
