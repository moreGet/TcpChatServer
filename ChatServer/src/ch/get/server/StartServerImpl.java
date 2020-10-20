package ch.get.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

import ch.get.common.Connections;
import ch.get.config.ExecutorConfig;
import ch.get.model.ChatUser;
import ch.get.view.RootLayoutController;

public class StartServerImpl implements StartServer {

	private String serverName;
	private ServerSocket serverSocket;
	private ServerInstance serverInstance;
	private ExecutorService executorService;
	
	public StartServerImpl(String serverName) {
		this.serverName = serverName;
		// ch.get.config
		executorService = ExecutorConfig.getInstance().getExecutorService();
	}
	
	@Override
	public void activeServer() {
		
		try {	
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress("localhost", 10000));
			
			// ���� �ν��Ͻ� ����
			serverInstance = new ServerInstance(serverSocket);
			// ���� �߻��� ������ Ǯ�� ���� �ϴ� ������ ����
			executorService.submit(serverInstance);
		} catch (Exception e) {
			if (!serverSocket.isClosed()) {
				deActiveServer();
			}
			
			e.printStackTrace();
		}
	}
	
	@Override
	public void deActiveServer() {
		try {
			// ���� ������ �����Ѵٸ� ����
			if (Connections.getConnections().size() > 0) {
				for (ChatUser user : Connections.getConnections()) {
					user.getSocket().close();
				}
			} else {
				RootLayoutController.getInstance().printText("[ " + "���� �̿��� �������� ����" + " ]");
			}
			
			// �������� �ݱ�
			if (!serverSocket.isClosed() && serverSocket!=null) {
				serverSocket.close();
			}
			
			// ���� ������ Ǯ ���ͷ�Ʈ
			if (executorService!=null && !executorService.isShutdown()) {
				executorService.shutdown();
			}
			
			RootLayoutController.getInstance().printText("[ ���� ���� ���� ]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ETC
	public String getServerName() {
		return serverName;
	}
	
	public ExecutorService getExecutorService() {
		return executorService;
	}
}