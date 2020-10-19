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
		
		// 창 종료
//		WindowController.getInstance().exit();
	}
	
	@FXML
	private void clickStartButton() {
		String serverName = "100";
		String serverCode = UUID.randomUUID().toString();
		
		if (serverList.size() < 1) { // 서버 제한
			StartServerImpl serverImpl = new StartServerImpl(serverName);
			serverImpl.activeServer(); // 서버 실행
			
			serverList.put(serverCode, serverImpl); // 서버 목록 적재
			printText(serverCode + " / " + serverList.size());
		} else {
			printText("============================================");
			printText("서버 허용 갯수 초과 Max Server Stack [ " + serverList.size() + " ]");
			BiConsumer<String, StartServerImpl> serverBiConsumer = (str, serInst) -> {
				printText("서버 UUID : " + str + " / " + "서버 이름 : " + serInst);
			};
			
			serverList.forEach(serverBiConsumer);
		}
	}
	
	// 서버 닫기
	public void serverShutdown() {
		if (serverList.size() > 0) {
			// 서버 종료
			serverList.entrySet().stream().forEach(list -> {
				list.getValue().deActiveServer();
			});
			
			// 서버 스택 삭제
			serverList.clear();
			printText("서버 종료");
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
