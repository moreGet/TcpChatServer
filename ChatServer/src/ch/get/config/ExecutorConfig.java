package ch.get.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorConfig {

	private ExecutorService executorService;
	
	private ExecutorConfig() {
		executorService = Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors());
	}
	
	public static ExecutorConfig getInstance() {
		return LazyHolder.inst;
	}
	
	private static class LazyHolder {
		private static final ExecutorConfig inst = new ExecutorConfig();
	}
	
	// 쓰레드 풀
	public ExecutorService getExecutorService() {
		return executorService;
	}
}
