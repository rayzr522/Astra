
package me.rayzr522.astra.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Loader {

	public static Score[] load(String location) {

		try {

			File saveFile = new File(location);
			saveFile.createNewFile();

			BufferedReader reader = new BufferedReader(new FileReader(saveFile));

			String next = reader.readLine();
			int nextPos = 0;
			Score[] scores = new Score[1000];

			while (next != null) {

				String[] args = next.split("//");

				scores[Integer.parseInt(args[0]) - 1] = new Score(args[1], Integer.parseInt(args[2]));

				next = reader.readLine();
				nextPos++;

			}

			Score[] newScores = new Score[nextPos];
			for (int i = 0; i < newScores.length; i++) {

				newScores[i] = scores[i];

			}

			return newScores;

		} catch (Exception e) {
			System.out.println("Save File Location: " + location);
			e.printStackTrace();
		}

		return null;

	}

}
