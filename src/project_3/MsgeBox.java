package project_3;


import java.awt.Component;

import javax.swing.JOptionPane;

public class MsgeBox {

	public void messageBox(Object obj , String message){
        JOptionPane.showMessageDialog( (Component)obj , message);
    }
	// JOptionPane.showMessageDialog()�� �ܼ��� �˸�â�� ��� �� �ִ� �Լ��̴�. �� �Լ��� �̿��Ͽ� �޽��� �ڽ��� ����Ѵ�.
}
