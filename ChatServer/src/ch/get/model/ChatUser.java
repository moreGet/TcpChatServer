package ch.get.model;

import java.net.Socket;
import java.util.concurrent.ExecutorService;

import ch.get.config.ExecutorConfig;

public class ChatUser {

	private String uuid;
	private String name;
	private Socket socket;
	private ExecutorService executorService;
	
	// �ʼ� ����
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
		// ������ Ǯ�� ����
		ClientReciver clientReciver = new ClientReciver(this);
		executorService.submit(clientReciver);
	}
	
	// chat sender
	public void sender(String message) {
		// ������ Ǯ�� ����
		ClientSender clientSender = new ClientSender(this, message);
		executorService.submit(clientSender);
	}
	
	// �г��� ���� ����
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	// ���� ���� �Ұ���
	public Socket getSocket() {
		return socket;
	}	
	
	public String getUuid() {
		return uuid;
	}
}