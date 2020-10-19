package ch.get.controller;

import javafx.application.Platform;

public class WindowController {
	
	private WindowController() {}
	
	public static WindowController getInstance() {
		return LazyHolder.inst;
	}
	
	private static class LazyHolder {
		private static final WindowController inst = new WindowController();
	}
	
	public void exit() {
		Platform.exit();
	}
}
