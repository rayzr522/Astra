package me.rayzr522.astra.util;

public abstract class ThreadedCode {

	public ThreadedCode() {

		begin();

	}

	private void begin() {
		try {
			new Thread() {
				@Override
				public void run() {
					x();
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final void x() {

		this.run();

	}

	public abstract void run();

}
