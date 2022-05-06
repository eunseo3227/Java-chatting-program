package project_3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ServerAddress extends JFrame {

	JButton confirmBtn;
	JTextField ipText;
	LoginUI loginUI;
    ImageIcon icon;
    Color c = new Color(178, 235, 244);	//���� �߰�

    // ServerAddress �����ڸ� �����. LoginUI�� �Ű������� ���� �޴´�. �׸��� initialize();�� �Է��Ͽ� �޼ҵ带 �����Ų��.
	public ServerAddress(LoginUI loginUI) {
		this.loginUI = loginUI;
		initialize();
	}

	private void initialize() {
		
		icon = new ImageIcon("icon2.png");
		this.setIconImage(icon.getImage());// Ÿ��Ʋ �ٿ� �̹����� �ֱ� ���� ImageIcon�� ����Ѵ�. �׸��� setIconImage�� ����Ͽ� �̹����� �ִ´�.
		setTitle("���� ������ �ּ� �Է�"); // Ÿ��Ʋ ������ ������ ������ �ּ� �Է¡����� �����Ѵ�.
		setBounds(100, 100, 306, 95); // setBounds() �޼ҵ�� â ũ�⸦ �����ϴ� �޼ҵ�� setBounds(������ġ, ������ġ, ���α���, ���α���);�� �����Ǿ� �ִ�.
		setDefaultCloseOperation(EXIT_ON_CLOSE); // �������� ������ ��� x ��ư�� ������ �ش� �������� �����Ѵ�.
		getContentPane().setLayout(null); // ���̾ƿ��� �����ϴ� ���̴�.

		JPanel panel = new JPanel();
		panel.setBounds(12, 10, 266, 37);
		getContentPane().add(panel);
		getContentPane().setBackground(c);	//���� �߰�
		panel.setLayout(new BorderLayout(0, 0)); // �ؽ�Ʈ ũ��
		// �ؽ�Ʈ�� �Է��ϴ� �г��̴�. �ش� �г��� ��ġ, �ʺ�, ���̸� �����ϰ� �ش� �г��� �������ҿ� ���δ�. 
		// ������ �����ϰ� ��ġ�����ڸ� �̿��Ͽ� ��ġ ����� �����Ѵ�. ä�� �г��� BorderLayout���� �����Ͽ���.

		// ip�� �Է��� �� �ִ� JTextField�̴�. addKeyListener�� ����Ͽ� ����Ű ������ �� ipText ������ ���۵ǵ��� �����Ѵ�. setVisible(true);�� �̿��Ͽ� â�� ȭ�鿡 ��Ÿ����. 
		// dispose();�� �̿��Ͽ� ���� �������� �����ŵ�ϴ�. �׸��� requestFocus();�� ����Ͽ� �ش� ������Ʈ�κ��� ���� Ű�� �Է¹޴´�.
		ipText = new JTextField();
		ipText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					loginUI.ipBtn.setText(ipText.getText());
					loginUI.setVisible(true);
					dispose();
					loginUI.idText.requestFocus();
				}
			}
		});
		ipText.setText("172.30.1.2");
		// ipText�� "172.30.1.2"�� �����ϰ� BorderLayout�� ����� ���δ�. �׸��� setColumns�� ����Ͽ� �ؽ�Ʈ �ʵ��� �ִ� �Է� ������ �����Ѵ�.
		panel.add(ipText, BorderLayout.CENTER);
		ipText.setColumns(10);

		// Ȯ�� ��ư�� �����. Ȯ�� ��ư�� Ŭ���ϸ� addMouseListener�� ����Ͽ� Ȯ�� ��ư�� Ŭ���Ǹ� ipText ������ ���۵ȴ�. 
		// setVisible(true);�� �̿��Ͽ� â�� ȭ�鿡 ��Ÿ����. dispose();�� �̿��Ͽ� ���� �������� �����ŵ�ϴ�. 
		// requestFocus();�� ����Ͽ� �ش� ������Ʈ�κ��� ���� Ű�� �Է¹޴´�. �׸��� Ȯ�� ��ư�� BorderLayout�� ���ʿ� ���̰� setVisible(true);�� �̿��Ͽ� â�� ȭ�鿡 ��Ÿ����.
		confirmBtn = new JButton("Ȯ��");
		confirmBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loginUI.ipBtn.setText(ipText.getText());
				loginUI.setVisible(true);
				dispose();
				loginUI.idText.requestFocus();
			}
		});
		panel.add(confirmBtn, BorderLayout.EAST);
		setVisible(true);
	}

}