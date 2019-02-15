package io.todo.todo.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

	private static final String TASK_EXECUTOR_DEFAULT = "taskExecutor";
	private static final String TASK_EXECUTOR_NAME_PREFIX_DEFAULT = "taskExecutor-";
	private static final String TASK_EXECUTOR_NAME_PREFIX_REPOSITORY = "serviceTaskExecutor-";
	private static final String TASK_EXECUTOR_NAME_PREFIX_CONTROLLER = "controllerTaskExecutor-";
	private static final String TASK_EXECUTOR_NAME_PREFIX_SERVICE = "serviceTaskExecutor-";

	public static final String TASK_EXECUTOR_REPOSITORY = "repositoryTaskExecutor";
	public static final String TASK_EXECUTOR_SERVICE = "serviceTaskExecutor";
	public static final String TASK_EXECUTOR_CONTROLLER = "controllerTaskExecutor";

	@Value("${executor.core-pool-size}")
	private int corePoolSize;

	@Value("${executor.max-pool-size}")
	private int maxPoolSize;

	@Value("${executor.queue-capacity}")
	private int queueCapacity;

	@Override
	@Bean(name = TASK_EXECUTOR_DEFAULT)
	public Executor getAsyncExecutor() {
		return newTaskExecutor(TASK_EXECUTOR_NAME_PREFIX_DEFAULT);
	}

	@Bean(name = TASK_EXECUTOR_REPOSITORY)
	public Executor getRepositoryAsyncExecutor() {
		return newTaskExecutor(TASK_EXECUTOR_NAME_PREFIX_REPOSITORY);
	}

	@Bean(name = TASK_EXECUTOR_SERVICE)
	public Executor getServiceAsyncExecutor() {
		return newTaskExecutor(TASK_EXECUTOR_NAME_PREFIX_SERVICE);
	}

	@Bean(name = TASK_EXECUTOR_CONTROLLER)
	public Executor getControllerAsyncExecutor() {
		return newTaskExecutor(TASK_EXECUTOR_NAME_PREFIX_CONTROLLER);
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SimpleAsyncUncaughtExceptionHandler();
	}

	private Executor newTaskExecutor(final String taskExecutorNamePrefix) {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix(taskExecutorNamePrefix);
		return executor;
	}

}
