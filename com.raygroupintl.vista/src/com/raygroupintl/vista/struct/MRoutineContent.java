package com.raygroupintl.vista.struct;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MRoutineContent {
	private String name;
	private List<String> lines = new ArrayList<String>();

	public MRoutineContent(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<String> getLines() {
		return this.lines;
	}
	
	private static MRoutineContent getInstance(String name, Scanner scanner) {
		MRoutineContent result = new MRoutineContent(name);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			result.lines.add(line);
		}
		return result;				
	}

	public static MRoutineContent getInstance(String name, InputStream is) {
		Scanner scanner = new Scanner(is);
		MRoutineContent result = MRoutineContent.getInstance(name, scanner);
		scanner.close();
		return result;				
	}

	public static MRoutineContent getInstance(Path path) throws IOException {
		String fileName = path.getFileName().toString();
		String name = fileName.split(".m")[0];
		Scanner scanner = new Scanner(path);
		MRoutineContent result = MRoutineContent.getInstance(name, scanner);
		scanner.close();
		return result;		
	}	
}
