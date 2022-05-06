package project_3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.*;

public class DBJoin implements MouseListener {
	JFrame frame;
	JPanel logPanel;
	JPanel logPanel1;
	JPanel logPanel2;
	JPanel logPanel3;
	JTextField idTf, pwTf, nameTf, birthTf = null;
	JButton joinBtn, checkBt;
    ImageIcon icon;
    Color c = new Color(212, 244, 250);	//���� �߰�(�ϴ�)
    Color c1 = new Color(255, 216, 216);	//���� �߰�(��ũ)
    
	MsgeBox msgbox = new MsgeBox();

	void JoinDBPanel() {
		
		frame = new JFrame("�߿� ä�� - ȸ������");
		logPanel = new JPanel();
		logPanel1 = new JPanel(new GridLayout(4, 1));
		logPanel2 = new JPanel(new GridLayout(4, 1));
		logPanel3 = new JPanel();
		logPanel.setBackground(c1);	//���� �߰�
		logPanel1.setBackground(c);	//���� �߰�
		logPanel2.setBackground(c);	//���� �߰�
		logPanel3.setBackground(c);	//���� �߰�
		//ȸ������ â�� GridLayout Ŭ������ ����ߴ�. ������ ���� �������� setBackround�� ����Ͽ� ���� �����ߴ�.
		
		icon = new ImageIcon("icon2.png");
		frame.setIconImage(icon.getImage());//ImageIcon�� frame.setIconImage�� ����Ͽ� Ÿ��Ʋ�ٿ� ���� ���ϴ� ������ �̹����� �־���.
		
		//ȸ������ â���� ID, Password, �̸�, ��������� �Է��ϴ� ĭ�� �߰��ߴ�.
		JLabel idLabel = new JLabel(" I D ", JLabel.CENTER);
		JLabel pwLabel = new JLabel(" P W ", JLabel.CENTER);
		JLabel nameLabel = new JLabel("�� ��", JLabel.CENTER);
		JLabel baLabel = new JLabel("�� �� �� �� ", JLabel.CENTER);
		logPanel1.add(idLabel);
		logPanel1.add(pwLabel);
		logPanel1.add(nameLabel);
		logPanel1.add(baLabel);

		idTf = new JTextField(20);
		idTf.addMouseListener(this);
		pwTf = new JTextField(20);
		pwTf.addMouseListener(this);
		nameTf = new JTextField(20);
		nameTf.addMouseListener(this);
		birthTf = new JTextField("ex)001124", 20);
		birthTf.addMouseListener(this);
		logPanel2.add(idTf);
		logPanel2.add(pwTf);
		logPanel2.add(nameTf);
		logPanel2.add(birthTf);

		//checkBt��� ID Check ��ư�� �����Ͽ� �ش� ���̵� ������ �����ϴ��� Ȯ���Ѵ�. 
		//checkBt�� addMouseListener �̺�Ʈ�� ����Ͽ� ��ư�� Ŭ���ϸ� �ش� �̺�Ʈ�� ����ȴ�.
		checkBt = new JButton("ID Check");
		logPanel3.add(checkBt, BorderLayout.NORTH);
		checkBt.addMouseListener(this); // addMouseListener�̺�Ʈ

		frame.add(logPanel, BorderLayout.NORTH);
		frame.add(logPanel1, BorderLayout.WEST);
		frame.add(logPanel2, BorderLayout.CENTER);
		frame.add(logPanel3, BorderLayout.EAST);

		//���������� ���Թ�ư�� ������ �������Ͻðڽ��ϱ�?�� ��� â�� �Բ� ���� �Ǵ� ��� ��ư�� ���� �� �ִ�. 
		//������ ������ ���������� ������ �ȴ�.
		//��ҹ�ư�� ������ �ش� frame�� �����Ű�� ���� dispose() �޼ҵ尡 ���ȴ�.
		JPanel logPanel4 = new JPanel();
		logPanel4.setBackground(c1);	//���� �߰�
		JLabel askLabel = new JLabel("�����Ͻðڽ��ϱ�?");
		joinBtn = new JButton("����");
		// joinBtn.setEnabled(false);
		JButton cancleBtn = new JButton("���");
		joinBtn.addMouseListener(this); // addMouseListener�̺�Ʈ
		logPanel4.add(askLabel);
		logPanel4.add(joinBtn);
		logPanel4.add(cancleBtn);
		frame.add(logPanel4, BorderLayout.SOUTH);

		// if((idTf.getText().isEmpty())==true ||
		// (pwTf.getText().isEmpty())==true){ //�־ȵ���???
		// joinBtn.setEnabled(true);
		// }

		// ��� ��ư
		cancleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				dbClose();
			}
		});

		frame.setBounds(450, 250, 350, 200);  
		//setBounds() �޼ҵ�� setBounds(������ġ, ������ġ, ���α���, ���α���);�� �����Ǿ� �ְ� �ش� frame�� ��ġ�� �����Ѵ�.
		frame.setResizable(false);
		//setResizable(false);�� â�� ũ�⸦ ������ �� ������ �ϴ� ���̴�.
		frame.setVisible(true);
		//setVisible() �޼ҵ�� â�� ȭ�鿡 ��Ÿ�� ������ �����ϴ� ���̴�.
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �츮�� �α���â�� �Բ�
		// �������

	}// JoinDBPanel() end
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:oracle:thin:@localhost:1521:system";
	//DBMS�� ���ῡ ���� url�̴�. localhost�� ����Ŭ�� ��ġ�� IP Address�̰�, 
	//1521�� ����Ŭ �����ͺ��̽��� ���� ��Ʈ�̴�. system�� ����Ŭ�� ����� ���� �����ͺ��̽� �̸��̴�.
	String sql = null;
	Properties info = null;
	Connection cnn = null;

	@Override
	//mouseClicked�� MouseEvent e�� �޾ƿ� ���콺�� Ŭ���� �� �ش� ��� ����ǵ��� �ϴ� �Լ��̴�.
	public void mouseClicked(MouseEvent e) {

		//if���� Ȱ���Ͽ� TextField�� Ŭ���ϸ� ������ �ԷµǾ� �ִ� ���ø� �����ش�.
		if (e.getSource().equals(idTf)) {
			idTf.setText("");
		} else if (e.getSource().equals(pwTf)) {
			pwTf.setText("");
		} else if (e.getSource().equals(nameTf)) {
			nameTf.setText("");
		} else if (e.getSource().equals(birthTf)) {
			birthTf.setText("");
		}

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//����Ŭ ����̹��� �ε��ϴ� �ڵ��̴�. JDBC�� �̿��ؼ� �����ͺ��̽��� ������ �� ����Ѵ�. 
			//�����ͺ��̽��� �����ϱ� ���� ������ �����ͺ��̽��� �´� ����̹��� �ε��ؾ� �Ѵ�. 
			//����̹��� �ε��ϱ� ���ؼ� �Ʒ��� ���� Class Ŭ������ forName �޼ҵ带 ����ؾ� �Ѵ�.
			info = new Properties();
			info.setProperty("user", "system");
			//�����͸� �����ϴ� �ڵ��̴�. ��user���� ���� �Ǵ� �˻��� ���� ���̰�, ��system���� �����ϰ��� �ϴ� ���ڿ�(text) �������̴�.
			info.setProperty("password", "j327532");
			cnn = DriverManager.getConnection(url, info); // ������ ������ �������ִ� ����̹��Ŵ����� ������
			//����̹��� �ε��ϰ� DriverManager.getConnection() �޼ҵ�� URL�� �̿��ؼ� ������ �����ϰ� �ȴ�.
			stmt = cnn.createStatement();
			//�����ͺ��̽��� SQL���� ������ ���� SQLServerStatement ��ü�� ����� ���̴�.

			// ���̺��� ����
			/*
			sql =
			"create table joinDB(id varchar2(20) primary key,pw varchar2(20) not null,name varchar2(30),barth number(6))"
			; stmt.execute(sql); System.out.println("���̺�����Ϸ�");
			*/

			// id üũ ��ư�� ������ ����ȴ�.
			if (e.getSource().equals(checkBt)) {
				sql = "select * from joinDB where id='" + idTf.getText() + "'";
				rs = stmt.executeQuery(sql);
				//java���� ��ȸ�� ������� ����Ѵ�. �� ������ �����ϸ� ResultSet Ÿ������ ��ȯ�� ���־� ������� ������ �� �ִ�.

				if (rs.next() == true || (idTf.getText().isEmpty()) == true) { //���� �ڵ带 �̿��Ͽ� �̹� id�� �����Ѵٸ� �ش� id�� ����� �� ���ٴ� ��Ʈ�� ���´�.
					msgbox.messageBox(logPanel3, "�ش� ID�� ����� �Ұ����մϴ�. �ٽ� �ۼ����ּ���.");
				} else {
					msgbox.messageBox(logPanel3, "��� ������ ID �Դϴ�.");
				}
			}

			// ���� ��ư
			if (e.getSource().equals(joinBtn)) {
				sql = "select * from joinDB where id='" + idTf.getText() + "'";

				rs = stmt.executeQuery(sql);
				//id üũ ��ư�� ���������� rs = stmt.executeQuery(sql);�� Ȱ���Ͽ� ResultSet Ÿ������ ��ȯ�� ���־� ������� �����Ѵ�.

				if (rs.next() == true) { // �̹� id�� �����Ѵٸ�
					msgbox.messageBox(logPanel3, "ID Check�� �ʿ��մϴ�.");

				} else if ((idTf.getText().isEmpty()) == true || (pwTf.getText().isEmpty()) == true
						|| (nameTf.getText().isEmpty()) || (birthTf.getText().isEmpty())) {		// id Ȥ�� pw ����������
					msgbox.messageBox(logPanel3, "����ִ� ĭ�� �����մϴ�.");
				} else if ((birthTf.getText().length()) != 6) {
					msgbox.messageBox(logPanel3, "������� ������ �߸��Ǿ����ϴ�."); 	// �ƴѰ��
				} else {

					sql = "insert into joinDB values ('" + idTf.getText() + "','" + pwTf.getText() + "','"
							+ nameTf.getText() + "','" + birthTf.getText() + "')";
					stmt.executeUpdate(sql);
					msgbox.messageBox(logPanel3, "�����մϴ�.���� �Ǽ̽��ϴ�.");
					frame.dispose(); // â �ݱ�
					dbClose();
				}
				//���� �ڵ带 �̿��Ͽ� �̹� id�� �����Ѵٸ� ID Check�� �ʿ��ϴٴ� �޽����� �ߵ��� �Ѵ�. �� �ܿ��� ����ִ� ĭ�� �ְų�, 
				//������� ������ �߸��� ��� ����ڰ� ������ �� �ֵ��� ��� �޽����� ���.
				//���Ŀ� �� �°� ȸ�������� �õ��Ͽ��ٸ� sql ���� ���� ���̵�, ��й�ȣ, �̸�, ��������� joinDB�� �߰��Ѵ�. 
				//�׸��� ���� �Ϸ� �޽����� �ߵ��� �Ѵ�.
			}
		} catch (Exception ee) {
			System.out.println("��������");
			ee.printStackTrace();
		}
	}// mouseClicked �̺�Ʈ end

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public void dbClose() {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (cnn != null)
				cnn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}// class end
