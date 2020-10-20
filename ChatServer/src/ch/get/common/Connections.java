package ch.get.common;

import java.util.concurrent.CopyOnWriteArrayList;

import ch.get.model.ChatUser;

public class Connections {

	private static CopyOnWriteArrayList<ChatUser> CONNECTIONS;
	
	private Connections() {
		Connections.CONNECTIONS = new CopyOnWriteArrayList<ChatUser>();
	};
	
	public static Connections getInstance() {
		return LazyHolder.inst;
	}
	
	private static class LazyHolder {
		private static final Connections inst = new Connections();
	}	
	
	public static CopyOnWriteArrayList<ChatUser> getConnections() {
		return CONNECTIONS;
	}
}
