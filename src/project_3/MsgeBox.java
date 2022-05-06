package project_3;


import java.awt.Component;

import javax.swing.JOptionPane;

public class MsgeBox {

	public void messageBox(Object obj , String message){
        JOptionPane.showMessageDialog( (Component)obj , message);
    }
	// JOptionPane.showMessageDialog()는 단순한 알림창을 띄울 수 있는 함수이다. 이 함수를 이용하여 메시지 박스를 출력한다.
}
