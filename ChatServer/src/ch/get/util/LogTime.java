package ch.get.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class LogTime {

	private LogTime() {}
	private String pattern;
	
	public static LogTime getInstance() {
		return LazyHolder.inst;
	}
	
	private static class LazyHolder {
		private static final LogTime inst = new LogTime();
	}
	
	public String getTime() {
		pattern = Optional.ofNullable(pattern)
						  .filter(elem -> !elem.isEmpty())
						  .orElse("yyyy-MM-dd hh:mm:ss");
		
		return "[ " + DateTimeFormatter.ofPattern(pattern)
									   .format(LocalDateTime.now()) + " ] ";
	}

	// etc
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
