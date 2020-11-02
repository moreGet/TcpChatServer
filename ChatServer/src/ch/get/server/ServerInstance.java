package ch.get.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
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
	
	public ServerInstance(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void run() {
		RootLayoutController.getInstance().printText(
				LogTime.getInstance().getTime() + ServerMessageTag.SERVER_START.getTag());
		
		Platform.runLater(() -> {
			RootLayoutController.getInstance()
								.getStartButton()
								.setText(ServerMessageTag.SERVER_STOP.getTag());
		});
		
		while (true) {
			try {
				Socket socket = serverSocket.accept(); // 연결 수락
				String message = 
						ServerMessageTag.SERVER_ACCECPT.getTag() 
						+ socket.getRemoteSocketAddress() 
						+ " : " 
						+ Thread.currentThread().getName();
				
				RootLayoutController.getInstance().printText(message);
				
				// 닉네임 받기
				String nickName = "";
				
				// Client 비정상 종료일 경우 IOException 발생
				byte[] byteArr = new byte[100];
				InputStream is = socket.getInputStream();
				
				// 비정상 종료 일 경우 IO예외
				int readByteCount = is.read(byteArr);
				if (readByteCount == -1) { // 정상 종료 일 경우 예외 발생
					throw new IOException();
				}
				
				nickName = new String(byteArr, 0, readByteCount, "UTF-8");
				nickName = Optional.ofNullable(nickName)
			 					    .filter(name -> !name.isEmpty()).orElse("Guest");
				
				String uuid = UUID.randomUUID().toString();
				ChatUser user = new ChatUser(nickName, socket, uuid);
				Connections.getConnections().add(user);
				
				RootLayoutController.getInstance().printText("접속 [ " + user.getName() + " 님 ]");
				RootLayoutController.getInstance().printText("현재 유저 [ " + Connections.getConnections().size() + " 명 ]");
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
