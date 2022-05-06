package project_3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class DBDelete {
	MsgeBox msgbox = new MsgeBox();

	String id = null;

	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:oracle:thin:@localhost:1521:system";
	//���� �ڵ�� DBMS�� ���ῡ ���� url�̴�. localhost�� ����Ŭ�� ��ġ�� IP Address�̰�, 1521�� ����Ŭ �����ͺ��̽��� ���� ��Ʈ�̴�. 
	//system�� ����Ŭ�� ����� ���� �����ͺ��̽� �̸��̴�.
	String sql = null;
	String sql2 = null;
	Properties info = null;	//�� �ڵ�� �Ӽ�������(���ڿ�, text)�� ��� Ŭ�����̴�.
	Connection cnn = null;

	// id�� �޾ƿͼ� �װ��� ������ pw/name/birth ����
	int InfoDel(String id) {
		int result = 0;
		this.id = id;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // �˾Ƽ� ����..conn��
			//���� �ڵ�� ����Ŭ ����̹��� �ε��ϴ� �ڵ��̴�. JDBC�� �̿��ؼ� �����ͺ��̽��� ������ �� ����Ѵ�. 
			//�����ͺ��̽��� �����ϱ� ���� ������ �����ͺ��̽��� �´� ����̹��� �ε��ؾ� �Ѵ�. ����̹��� �ε��ϱ� ���ؼ� �Ʒ��� ���� Class Ŭ������ forName �޼ҵ带 ����ؾ� �Ѵ�.
			info = new Properties();
			info.setProperty("user", "system");	//�����͸� �����ϴ� �ڵ��̴�. ��user���� ���� �Ǵ� �˻��� ���� ���̰�, ��system���� �����ϰ��� �ϴ� ���ڿ�(text) �������̴�.
			info.setProperty("password", "j327532");
			cnn = DriverManager.getConnection(url, info); // ������ ������ �������ִ� ����̹��Ŵ����� ������
			//���� �ڵ带 ���� ����̹��� �ε��ϰ� DriverManager.getConnection() �޼ҵ�� URL�� �̿��ؼ� ������ �����ϰ� �ȴ�.
			stmt = cnn.createStatement();	//�� �ڵ�� �����ͺ��̽��� SQL���� ������ ���� SQLServerStatement ��ü�� ����� ���̴�.

			sql = "delete from joinDB where id='" + id + "'";
			//ID�� ���� ������ �޾ƿ� �ش� pw�� name, birth �����͸� �����Ѵ�.
			stmt.executeUpdate(sql);
			//executeUpdate �޼���� �����ͺ��̽����� �����͸� �߰�(Insert), ����(Delete), ����(Update)�ϴ� SQL���� �����Ѵ�. 
			//�޼ҵ��� ��ȯ ���� �ش� SQL�� ���࿡ ������ �޴� �� ���� ��ȯ�Ѵ�.

			sql = "select * from joinDB where id='" + id + "'";
			rs = stmt.executeQuery(sql);
			//executeQuery �޼���� �����ͺ��̽����� �����͸� �����ͼ� ��� ������ ��ȯ�Ѵ�. �� �޼���� Select �������� �����ϴ� Ư¡�� �ִ�.
			if (rs.next() == true) { // ��������
				result = 0; // ����
			} else {
				result = 1; // ����
			}
		} catch (Exception ee) {
			System.out.println("��������");
			ee.printStackTrace();
		}

		return result;
	}

}
