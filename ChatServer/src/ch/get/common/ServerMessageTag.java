package ch.get.common;

public enum ServerMessageTag {

	// SERVER STATUS
	SERVER_START(0, "���� ����"),
	SERVER_STOP(1, "���� ����"),
	
	// SERVER RESPONSE
	SERVER_ACCECPT(100, "���� ����"),
	
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
