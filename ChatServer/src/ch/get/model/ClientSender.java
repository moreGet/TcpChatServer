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
			// 메세지 보내기
			OutputStreamWriter osw = new OutputStreamWriter(user.getSocket().getOutputStream());
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(msg);
			bw.flush();
		} catch (Exception e) {
			try {
				RootLayoutController.getInstance().printText("클라이언트 연결 해제 [ " + user.getName() + " ]");
				Connections.getConnections().remove(user.getUuid()); // 비정상 종료 혹은 정상 종료 일떄
				user.getSocket().close(); // 해당 user socket 닫아줌
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
}
