package project_3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DBLogin {

	String id = null;
	String pw = null;

	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:oracle:thin:@localhost:1521:system";
	//���� �ڵ�� DBMS�� ���ῡ ���� url�̴�. localhost�� ����Ŭ�� ��ġ�� IP Address�̰�, 1521�� ����Ŭ �����ͺ��̽��� ���� ��Ʈ�̴�. 
	//system�� ����Ŭ�� ����� ���� �����ͺ��̽� �̸��̴�.
	String sql = null;
	Properties info = null;
	Connection cnn = null;

	int checkIDPW(String id, String pw) { //ID Password�� DB���� �ҷ��ͼ� �񱳸� �Ͽ� ID�� PW�� ��ġ�Ѵٸ� �α��ο� �����ϵ��� �Ѵ�.
		this.id = id;
		this.pw = pw;
		int result = 1;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//���� �ڵ�� ����Ŭ ����̹��� �ε��ϴ� �ڵ��̴�. JDBC�� �̿��ؼ� �����ͺ��̽��� ������ �� ����Ѵ�. �����ͺ��̽��� �����ϱ� ���� ������ �����ͺ��̽��� �´� ����̹��� �ε��ؾ� �Ѵ�. 
			//����̹��� �ε��ϱ� ���ؼ� �Ʒ��� ���� Class Ŭ������ forName �޼ҵ带 ����ؾ� �Ѵ�.
			info = new Properties();
			info.setProperty("user", "system");
			//�����͸� �����ϴ� �ڵ��̴�. ��user���� ���� �Ǵ� �˻��� ���� ���̰�, ��system���� �����ϰ��� �ϴ� ���ڿ�(text) �������̴�.
			info.setProperty("password", "j327532");
			cnn = DriverManager.getConnection(url, info); // ������ ������ �������ִ� ����̹��Ŵ����� ������
			stmt = cnn.createStatement();
			//���� �ڵ�� �����ͺ��̽��� SQL���� ������ ���� SQLServerStatement ��ü�� ����� ���̴�.

			sql = "select * from joinDB where id='" + id + "'";
			rs = stmt.executeQuery(sql);
			//java���� ��ȸ�� ������� ����Ѵ�. �� ������ �����ϸ� ResultSet Ÿ������ ��ȯ�� ���־� ������� ������ �� �ִ�. ���̵� �����ϴ��� Ȯ���ϰ� ���ϱ� ���� ���̵� �ҷ��´�.

			if (rs.next() == false || (id.isEmpty()) == true) { //�� �ڵ带 Ȱ���Ͽ� id�� �����ϰ� �ִ��� Ȯ���Ѵ�.
				result = 1;
			} else {
				sql = "select * from (select * from joinDB where id='" + id + "')";
				rs = stmt.executeQuery(sql);
				while (rs.next() == true) { //���̺��� �ش� id�� ã�� id ���� ���� �Էµ� pw�� ������ �񱳸� �Ѵ�. ������ �α��� �����̰�, ���̵�� ���� pw�� �ٸ� ��� 1�� �����Ѵ�.
					if (rs.getString(2).equals(pw)) { // pw�� ������ ��
						result = 0; 		// ������ �α��� ����
					} else {				// ���̵�°��� pw�� �ٸ����
						result = 1;
					}
				}
			}
		} catch (Exception ee) { //����ó���� �Ͽ� ������ ����� �������������� ��µǵ��� �Ѵ�.
			System.out.println("��������");
			ee.printStackTrace();
		}
		return result;
	}
}
