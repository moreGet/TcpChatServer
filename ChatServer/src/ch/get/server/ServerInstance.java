package ch.get.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.UUID;

import ch.get.common.Connections;
import ch.get.common.ServerMessageTag;
import ch.get.model.ChatUser;
import ch.get.util.LogTime;
import ch.get.view.RootLayoutController;
import javafx.application.Platform;

public class ServerInstance implements Runnable {

	private ServerSocket serverSocket;
	
	// STREAM
	private InputStreamReader isr = null;
	private BufferedReader br = null;
//	private OutputStreamWriter osw = null;
//	private PrintWriter pw = null;
	
	public ServerInstance(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void run() {
		Platform.runLater(() -> {
			RootLayoutController.getInstance().printText(
					LogTime.getInstance().getTime() + ServerMessageTag.SERVER_START.getTag());
			
			RootLayoutController.getInstance()
								.getStartButton()
								.setText(ServerMessageTag.SERVER_STOP.getTag());
		});
		
		while (true) {
			try {
				Socket socket = serverSocket.accept(); // ���� ����
				String message = 
						ServerMessageTag.SERVER_ACCECPT.getTag() 
						+ socket.getRemoteSocketAddress() 
						+ " : " 
						+ Thread.currentThread().getName();

				// ���� ���� �޽���
				Platform.runLater(() -> {
					RootLayoutController.getInstance().printText(message);
				});
				
				// �г��� �ޱ�
				String nickName = "";
				isr = new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8"));
				br = new BufferedReader(isr);
				
				nickName = br.readLine();
				nickName = Optional.ofNullable(nickName)
			 					    .filter(name -> !name.isEmpty()).orElse("Guest");
				
				String uuid = UUID.randomUUID().toString();
				ChatUser user = new ChatUser(nickName, socket, uuid);
				Connections.getConnections().put(uuid, user);
				
				// ������ ������ ���� ��
				Platform.runLater(() -> {
					RootLayoutController.getInstance().printText("���� [ " + user.getName() + " �� ]");
					RootLayoutController.getInstance().printText("���� ���� [ " + Connections.getConnections().size() + " �� ]");
				});
			} catch (Exception e) {
				break;
			}
		}
	}
}
