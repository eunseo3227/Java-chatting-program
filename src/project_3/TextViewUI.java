package project_3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextViewUI extends JFrame {
	
	final int FRAME_WIDTH  = 500;
	final int FRAME_HEIGHT = 600; 
	// final ����� ����Ͽ� �������� ���̿� �ʺ� �����Ѵ�.
	JTextArea taChatText;
	
	public TextViewUI() {
		setTitle("ä�ó���Ȯ��\n"); // setTitle�� ����Ͽ� �������� ������ ��ä�ó���Ȯ�Ρ����� �����Ѵ�.
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); // �������� ������ ��� x ��ư�� ������ �ش� �������� �����Ѵ�.
		setLayout(new BorderLayout()); // �������� ���̾ƿ��� BorderLayout���� �����Ѵ�.
		setSize(500, 600); // �������� ũ�⸦ 500, 600���� �����Ѵ�.
		
		// ä�� ������ ��µ� �г��� �߰��Ѵ�. �׸��� �ؽ�Ʈ ������ ����� ���� JTextArea�� �̿��Ͽ� taChatText�� �����. taChatText�� ������ �Ͼ������ �����Ѵ�.
		JPanel pnlCenter = new JPanel(new BorderLayout());
		taChatText = new JTextArea();
		taChatText.setBackground(Color.WHITE);
//		taChatText.setText("���ڰ� ǥ�õǴ°�?");
//		pnlCenter.add(taChatText, BorderLayout.CENTER);
	
		
		
		// ��ũ�� ����(scroll pane)�� ������Ʈ�� ��ũ�� ����� �����Ѵ�. scrollPane�� BorderLayout�� ���Ϳ� �����Ѵ�. 
		// Scrollbar.VERTICAL�� Scrollbar.HORIZONTAL��  ��ũ�ѹ��� �����̰� AS_NEEDED�� �ʿ��� ���� ��ũ�� �ٰ� ���̵��� �����ϴ� ���̰�, 
		// NEVER�� ��ũ�ѹٰ� ������ �ʰ� �ϴ� ���̴�. pnlCenter�� BorderLayout�� ���Ϳ� �����Ѵ�.
		JScrollPane spChatText = new JScrollPane(taChatText, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pnlCenter.add(spChatText, BorderLayout.CENTER);
		
		add(pnlCenter, BorderLayout.CENTER);
		
		
		// Toolkit Ŭ������ static �޼ҵ���� native operation system�� ���� ��ȸ�����ν� ���ϴ� ������ ���ε�(Binding)���� �� �� �ִ�. 
		// getDefaultToolkit�� ����Ͽ� ����Ʈ�� ��Ŷ�� �����Ѵ�. Dimension Ŭ������ Ŭ������ Ư�� ������ �簢���� ���� ������ ���� ������ �� �ֵ��� �����ִ� Ŭ�����̴�. 
		// getScreenSize�� ���� ȭ�� ũ�⸦ ���Ѵ�. d.heigt�� d.width�� ����Ͽ� ���̿� ���� screenHeigt�� screenWidth�� �ִ´�.
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth  = d.width;
		
		setLocation((screenWidth - FRAME_WIDTH) / 2, (screenHeight - FRAME_HEIGHT) / 2);
		setVisible(true);
		// setLocation�� ����Ͽ� ������â�� ��Ÿ�� ��ġ�� �����Ѵ�. �׸��� setVisible(true)�� ����Ͽ� â�� ����Ѵ�.
	}
	
	public TextViewUI(String text) {
		this();
		taChatText.setText(text+"\n");
	}
	public TextViewUI(JTextArea jta) {
		this();
		taChatText.setText(jta.getText());
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TextViewUI();
	}

}
