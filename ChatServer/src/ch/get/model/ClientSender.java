package ch.get.model;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import ch.get.common.Connections;
import ch.get.view.RootLayoutController;

public class ClientSender implements Runnable {

	private ChatUser user;
	private String msg;
	
	public ClientSender(ChatUser user, String msg) {
		this.user = user;
		this.msg = msg;
	}
	
	@Override
	public void run() {
		try {
			// �޼��� ������
			OutputStreamWriter osw = new OutputStreamWriter(user.getSocket().getOutputStream());
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(msg);
			bw.flush();
		} catch (Exception e) {
			try {
				RootLayoutController.getInstance().printText("Ŭ���̾�Ʈ ���� ���� [ " + user.getName() + " ]");
				Connections.getConnections().remove(user.getUuid()); // ������ ���� Ȥ�� ���� ���� �ϋ�
				user.getSocket().close(); // �ش� user socket �ݾ���
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
}
