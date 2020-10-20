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
			
			// 서버 인스턴스 설정
			serverInstance = new ServerInstance(serverSocket);
			// 예외 발생시 쓰레드 풀에 존재 하는 쓰레드 재사용
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
			// 현재 유저가 존재한다면 삭제
			if (Connections.getConnections().size() > 0) {
				for (ChatUser user : Connections.getConnections()) {
					user.getSocket().close();
				}
			} else {
				RootLayoutController.getInstance().printText("[ " + "서버 이용자 존재하지 않음" + " ]");
			}
			
			// 서버소켓 닫기
			if (!serverSocket.isClosed() && serverSocket!=null) {
				serverSocket.close();
			}
			
			// 서버 스레드 풀 인터럽트
			if (executorService!=null && !executorService.isShutdown()) {
				executorService.shutdown();
			}
			
			RootLayoutController.getInstance().printText("[ 서버 강제 종료 ]");
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