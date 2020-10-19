package ch.get.view;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.BiConsumer;

import ch.get.server.StartServerImpl;
import ch.get.util.LogTime;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class RootLayoutController implements Initializable {

	private Stage primaryStage; 
	private static RootLayoutController instance;
	private static HashMap<String, StartServerImpl> serverList;
	
	@FXML
	private Button startButton;
	@FXML
	private TextArea textArea;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		serverList = new HashMap<>();
	}
	
	@FXML
	private void clickExitMenuBtn() {
		serverShutdown();
		
		// â ����
//		WindowController.getInstance().exit();
	}
	
	@FXML
	private void clickStartButton() {
		String serverName = "100";
		String serverCode = UUID.randomUUID().toString();
		
		if (serverList.size() < 1) { // ���� ����
			StartServerImpl serverImpl = new StartServerImpl(serverName);
			serverImpl.activeServer(); // ���� ����
			
			serverList.put(serverCode, serverImpl); // ���� ��� ����
			printText(serverCode + " / " + serverList.size());
		} else {
			printText("============================================");
			printText("���� ��� ���� �ʰ� Max Server Stack [ " + serverList.size() + " ]");
			BiConsumer<String, StartServerImpl> serverBiConsumer = (str, serInst) -> {
				printText("���� UUID : " + str + " / " + "���� �̸� : " + serInst);
			};
			
			serverList.forEach(serverBiConsumer);
		}
	}
	
	// ���� �ݱ�
	public void serverShutdown() {
		if (serverList.size() > 0) {
			// ���� ����
			serverList.entrySet().stream().forEach(list -> {
				list.getValue().deActiveServer();
			});
			
			// ���� ���� ����
			serverList.clear();
			printText("���� ����");
		}
	}
	
	// root TextArea
	public void printText(String str) {
		textArea.appendText(LogTime.getInstance().getTime() + str + "\r\n");
	}

	public void setApp(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public static RootLayoutController getInstance() {
		return instance;
	}
	
	public Button getStartButton() {
		return startButton;
	}
}
