package ch.get.model;

import java.net.Socket;
import java.util.concurrent.ExecutorService;

import ch.get.config.ExecutorConfig;

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
		executorService = ExecutorConfig.getInstance().getExecutorService();
		
		// reciver
		reciver();
	}
	
	// chat reciver
	public void reciver() {
		// 쓰레드 풀로 이행
		ClientReciver clientReciver = new ClientReciver(this);
		executorService.submit(clientReciver);
	}
	
	// chat sender
	public void sender(String message) {
		// 쓰레드 풀로 이행
		ClientSender clientSender = new ClientSender(this, message);
		executorService.submit(clientSender);
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