
package me.rayzr522.astra.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Saver {

	public static void write(Score[] scores, String location, int maxWrites) {

		try {

			File saveFile = new File(location);
			saveFile.createNewFile();

			System.out.println("Creating buffered writer...");

			BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));

			for (int i = 0; i < scores.length; i++) {
				if (i < maxWrites) {
					System.out.println("About to write '" + (i + 1) + "//" + scores[i].name + "//" + scores[i].score + "' to the file...");
					bw.write((i + 1) + "//" + scores[i].name + "//" + scores[i].score);
					bw.newLine();
				}
			}
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void reset(String location) {

		try {

			File saveFile = new File(location);
			saveFile.createNewFile();

			System.out.println("Dumping...");
			BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
