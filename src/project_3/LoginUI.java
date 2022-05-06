package project_3;

import java.awt.Graphics;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginUI extends JFrame implements TextListener {

	boolean confirm = false;
	TextField idText;
	TextField pwText;
	JButton loginBtn, signUpBtn;
	JButton ipBtn;
	EightClient client;

	DBJoin jdb;
	JScrollPane scrollPane;
    ImageIcon icon;
    LoginUI login;
    Color c = new Color(212, 244, 250);	//���� �߰�(�ϴ�)
    Color c1 = new Color(255, 216, 216);	//���� �߰�(��ũ)
	
    // LoginUI �����ڸ� �����.
	public LoginUI(EightClient eigClient) {
		
		setTitle("�߿� ä�� - �α���");
		ServerAddress sd = new ServerAddress(this);
		this.client = eigClient;
		loginUIInitialize();
	}

	// �޼���
	private void loginUIInitialize() {
		setBounds(100, 100, 335, 218); // setBounds() �޼ҵ�� â ũ�⸦ �����ϴ� �޼ҵ�� setBounds(������ġ, ������ġ, ���α���, ���α���);�� �����Ǿ� �ִ�.
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //�������� ������ ��� x ��ư�� ������ �ش� �������� �����Ѵ�.
		getContentPane().setLayout(null);
		setResizable(false); //â�� ũ�⸦ ������ �� ������ �ϴ� ���̴�.

		icon = new ImageIcon("icon2.png");
		this.setIconImage(icon.getImage());//Ÿ��Ʋ �ٿ� �̹����� �ִ� ���̴�.

		JPanel panel = new JPanel();
		panel.setBackground(c);		//���� �߰�
		panel.setBounds(12, 10, 295, 160); // �α��� ��й�ȣ ��Ÿ����
		getContentPane().add(panel); // �α��� ȭ�� ��Ÿ����
		getContentPane().setBackground(c1);	//���� �߰�
		
		panel.setLayout(null);

		//�α��� UI�� ���̵�� ��й�ȣ ���� �߰��Ѵ�.
		JLabel jbNewLabel1 = new JLabel("���̵�");
		jbNewLabel1.setBounds(60, 55, 57, 15); // "���̵�" ��ġ
		panel.add(jbNewLabel1);

		JLabel jbNewLabel2 = new JLabel("��й�ȣ");
		jbNewLabel2.setBounds(60, 86, 57, 15);
		panel.add(jbNewLabel2);

		idText = new TextField();
		idText.setBounds(129, 52, 116, 21);
		panel.add(idText);
		idText.setColumns(10);

		pwText = new TextField();
		pwText.addKeyListener(new KeyAdapter() {

			// �α��� ���� ��ư
			// �α��� ���� ��ư�� ������ �޽��� ���� �޼ҵ�� �Ѿ���� �Ѵ�.
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					msgSummit();
				}
			}

		});
		pwText.setBounds(129, 83, 116, 21);
		panel.add(pwText);
		pwText.setColumns(10);

		idText.addTextListener(this);
		pwText.addTextListener(this);

		loginBtn = new JButton("�α���");
		loginBtn.setEnabled(false);
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});

		loginBtn.addMouseListener(new MouseAdapter() {
			// �α��� ��ư�� ������ ���� �޽��� ���� �޼ҵ�� �Ѿ��
			@Override
			public void mouseClicked(MouseEvent e) {
				if (loginBtn.isEnabled() == true) {
					msgSummit();
				}
			}
		});

		loginBtn.setBounds(50, 111, 97, 23);
		panel.add(loginBtn);

		//ȸ������ ��ư�� ����� ȸ������ ��ư�� Ŭ������ �� DBJoin Ŭ������ �Ѿ ȸ�������� �� �� �ֵ��� �Ѵ�.
		signUpBtn = new JButton("ȸ������");
		signUpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		signUpBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// ȸ������
				jdb = new DBJoin();
				jdb.JoinDBPanel();
			}
		});
		signUpBtn.setBounds(149, 111, 97, 23);
		panel.add(signUpBtn);

		//���� �����Ǹ� �Է��ϴ� ���� �����Ѵ�. true�� â�� �׳� ������.
		JLabel jbNewLabe3 = new JLabel("���� ������");
		jbNewLabe3.setBounds(12, 10, 78, 15);
		panel.add(jbNewLabe3);

		ipBtn = new JButton("");
		ipBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServerAddress sd = new ServerAddress(LoginUI.this);
				setVisible(false); // true�̸� â�� �׳� ������.
			}
		});
		ipBtn.setBounds(93, 6, 97, 23);
		panel.add(ipBtn);
	}

	// �޽��� ���� �޼ҵ��̴�. ������ �����ؼ� �α��� ������ ���̵�� �н����带 �����Ѵ�.
	private void msgSummit() {
		new Thread(new Runnable() {
			public void run() {

				// ���ϻ���(�α��� ������ �ȵȴ�)
				if (client.serverAccess()) {
					try {
						// �α�������(���̵�+�н�����) ����
						client.getDos().writeUTF(User.LOGIN + "/" + idText.getText() + "/" + pwText.getText());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			} // run() start
		}).start();
	} // msgSummit() end

	@Override
	// ���̵�� �н����带 �Է��ϴ� â�� ��������� setEnabled(false)�� ����Ͽ� �α��� ��ư�� ��Ȱ��ȭ ��Ų��.
	public void textValueChanged(TextEvent e) {
		if (idText.getText().equals("") || pwText.getText().equals("")) {
			loginBtn.setEnabled(false);
		} else {
			loginBtn.setEnabled(true);
		}
	}

}
