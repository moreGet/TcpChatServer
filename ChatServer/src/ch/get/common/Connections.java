package ch.get.common;

import java.util.HashMap;

import ch.get.model.ChatUser;

public class Connections {

	private static HashMap<String, ChatUser> CONNECTIONS;
	
	private Connections() {
		Connections.CONNECTIONS = new HashMap<>();
	};
	
	public static Connections getInstance() {
		return LazyHolder.inst;
	}
	
	private static class LazyHolder {
		private static final Connections inst = new Connections();
	}
	
	public static HashMap<String, ChatUser> getConnections() {
		return CONNECTIONS;
	}
}
