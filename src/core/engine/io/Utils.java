package core.engine.io;

import java.io.*;

public class Utils {
	
	private Utils() {
		
	}
	
	public static StringBuilder readFile(String filePath) {
		StringBuilder str = new StringBuilder();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line;
			while((line = reader.readLine()) != null) {
				str.append(line).append("\n");
			}
			
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		
		return str;
		
	}
}
