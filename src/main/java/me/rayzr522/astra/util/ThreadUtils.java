package me.rayzr522.astra.util;

public class ThreadUtils {

	public static void sleep(int millis) {

		try {
			new Thread().sleep(millis);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// public final void begin() {
	//
	// run();
	//
	// }
	//
	// public void start() {
	//
	// try {
	// new Thread() {
	// public void run() {
	// begin();
	// }
	// }.start();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// public abstract void run();
}
