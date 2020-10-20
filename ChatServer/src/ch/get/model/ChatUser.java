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
	
	// �ʼ� ����
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
		// ������ Ǯ�� ����
		Runnable reciver = () -> {
			try {
				while (true) {
					// Client ������ ������ ��� IOException �߻�
					byte[] byteArr = new byte[100];
					InputStream is = socket.getInputStream();
					
					// ������ ���� �� ��� IO����
					int readByteCount = is.read(byteArr);
					if (readByteCount == -1) { // ���� ���� �� ��� ���� �߻�
						throw new IOException();
					}
					
					String message = new String(byteArr, 0, readByteCount, "UTF-8");
					RootLayoutController.getInstance().printText(socket.getRemoteSocketAddress() + " [ " + message + " ]");
					
					// Lazy Connections Collection
					// ��ε� ĳ����
					for (ChatUser user : Connections.getConnections()) {
						user.sender(message);
					}
				}
			} catch (Exception e) {
				try {
					e.printStackTrace();
					RootLayoutController.getInstance().printText("Ŭ���̾�Ʈ ���� ���� [ " + name + " ]");
					Connections.getConnections().remove(ChatUser.this); // ������ ���� Ȥ�� ���� ���� �ϋ�
					socket.close(); // �ش� user socket �ݾ���
				} catch (IOException ie) {
					ie.printStackTrace();
				}
			}
		};
		
		executorService.submit(reciver);
	}
	
	// chat sender
	public void sender(String message) {
		// ������ Ǯ�� ����
		Runnable sender = () -> {
			try {
				// �޼��� ������
				byte[] byteArr = message.getBytes("UTF-8");
				OutputStream os = socket.getOutputStream();
				os.write(byteArr);
				os.flush();
			} catch (Exception e) {
				try {
					RootLayoutController.getInstance().printText("Ŭ���̾�Ʈ ���� ���� [ " + name + " ]");
					Connections.getConnections().remove(ChatUser.this); // ������ ���� Ȥ�� ���� ���� �ϋ�
					socket.close(); // �ش� user socket �ݾ���
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		};
		
		executorService.submit(sender);
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