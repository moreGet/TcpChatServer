package ch.get.common;

public enum ServerMessageTag {

	// SERVER STATUS
	SERVER_START(0, "서버 시작"),
	SERVER_STOP(1, "서버 중지"),
	
	// SERVER RESPONSE
	SERVER_ACCECPT(100, "연결 수락"),
	
	// CLIENT REQUEST
	QUIT(1000, "QUIT");
	
	private int row;
	private String tag;
	
	private ServerMessageTag(int row, String tag) {
		this.row = row;
		this.tag = tag;
	}
	
	public int getRow() {
		return row;
	}
	
	public String getTag() {
		return tag;
	}
}
