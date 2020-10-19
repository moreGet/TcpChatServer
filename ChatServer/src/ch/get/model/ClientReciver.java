package ch.get.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;

import ch.get.common.Connections;
import ch.get.common.ServerMessageTag;
import ch.get.view.RootLayoutController;
import javafx.application.Platform;

public class ClientReciver implements Runnable{

//	private Socket socket;
//	private String name;
	private ChatUser user;
	
//	public ServerReciver(Socket socket, String name) {
//		this.socket = socket;
//		this.name = name;
//	}
	
	public ClientReciver(ChatUser user) {
		this.user = user;
	}
	
	@Override
	public void run() {
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			isr = new InputStreamReader(
					user.getSocket().getInputStream(), Charset.forName("UTF-8"));
			br = new BufferedReader(isr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			while (true) {
				// Client 비정상 종료일 경우 IOException 발생
				String message = br.readLine();
				if (ServerMessageTag.QUIT.getTag().equalsIgnoreCase(message)) {
					
				}
				
				// Lazy Connections Collection
				// 브로드 캐스팅
				if (Connections.getConnections().size() > 1) {
//					Platform.runLater(() -> {
//						RootLayoutController.getInstance().printText(
//								"[ " + user.getName() + " ] : " + message);
//					});
					
					synchronized (this) {
						Connections.getConnections().forEach((uid, user) -> {
							// 자신에게는 브로드 캐스팅 안함
							if (!user.getUuid().equals(this.user.getUuid())) {
								user.sender(message);
							}
						});
					}
				}
			}
		} catch (Exception e) {
			try {
				e.printStackTrace();
				RootLayoutController.getInstance().printText("클라이언트 연결 해제 [ " + user.getName() + " ]");
				Connections.getConnections().remove(user.getUuid()); // 비정상 종료 혹은 정상 종료 일떄
				user.getSocket().close(); // 해당 user socket 닫아줌
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}
}
