package ch.get.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.get.common.Connections;
import ch.get.view.RootLayoutController;

public class ChatUser {

	private String uuid;
	private String name;
	private Socket socket;
	private ExecutorService executorService;
	
	// 필수 인자
	public ChatUser(String name, Socket socket, String uuid) {
		this.name = name;
		this.socket = socket;
		this.uuid = uuid;
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		// reciver
		reciver();
	}
	
	// chat reciver
	public void reciver() {
		// 쓰레드 풀로 이행
		Runnable reciver = () -> {
			try {
				while (true) {
					// Client 비정상 종료일 경우 IOException 발생
					byte[] byteArr = new byte[100];
					InputStream is = socket.getInputStream();
					
					// 비정상 종료 일 경우 IO예외
					int readByteCount = is.read(byteArr);
					if (readByteCount == -1) { // 정상 종료 일 경우 예외 발생
						throw new IOException();
					}
					
					String message = new String(byteArr, 0, readByteCount, "UTF-8");
					RootLayoutController.getInstance().printText(socket.getRemoteSocketAddress() + " [ " + message + " ]");
					
					// Lazy Connections Collection
					// 브로드 캐스팅 자신을 제외한 나머지
					for (ChatUser user : Connections.getConnections()) {
						if (user != this) {
							user.sender(message);
						}
					}
				}
			} catch (Exception e) {
				try {
					e.printStackTrace();
					socket.close(); // 해당 user socket 닫아줌
					Connections.getConnections().remove(ChatUser.this); // 비정상 종료 혹은 정상 종료 일떄
					RootLayoutController.getInstance().printText("클라이언트 연결 해제 [ " + name + " ]");	
				} catch (Exception ie) {
					ie.printStackTrace();
				}
			}
		};
		
		executorService.submit(reciver);
	}
	
	// chat sender
	public void sender(String message) {
		// 쓰레드 풀로 이행
		Runnable sender = () -> {
			try {
				// 메세지 보내기
				byte[] byteArr = message.getBytes("UTF-8");
				OutputStream os = socket.getOutputStream();
				os.write(byteArr);
				os.flush();
			} catch (Exception e) {
				try {
					RootLayoutController.getInstance().printText("클라이언트 연결 해제 [ " + name + " ]");
					Connections.getConnections().remove(ChatUser.this); // 비정상 종료 혹은 정상 종료 일떄
					socket.close(); // 해당 user socket 닫아줌
				} catch (Exception e2) {
					System.out.println(this.getClass().getName());
					// TODO: handle exception
				}
			}
		};
		
		executorService.submit(sender);
	}
	
	// 닉네임 변경 가능
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	// 소켓 변경 불가능
	public Socket getSocket() {
		return socket;
	}	
	
	public String getUuid() {
		return uuid;
	}
}