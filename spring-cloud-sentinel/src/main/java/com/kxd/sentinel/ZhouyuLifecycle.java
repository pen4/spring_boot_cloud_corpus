package com.kxd.sentinel;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class ZhouyuLifecycle implements SmartLifecycle {

	private boolean isRunning = false;

	@Override
	public void start() {
		System.out.println("启动");
		isRunning = true;
	}

	@Override
	public void stop() {
        // 要触发stop()，要调用context.close()，或者注册关闭钩子（context.registerShutdownHook();）
		System.out.println("停止");
		isRunning = false;
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}
}