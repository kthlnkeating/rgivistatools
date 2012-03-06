package com.raygroupintl.vista.mtoken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.raygroupintl.vista.struct.MError;

public class Routine extends Base {
	private List<Line> lines = new ArrayList<Line>();
	
	public void add(Line line) {
		this.lines.add(line);
	}
	
	public List<Line> asList() {
		return this.lines;
	}
	
	@Override
	public String getStringValue() {
		StringBuilder sb = new StringBuilder();
		for (Line line : this.lines) {
			sb.append(line.getStringValue());
			sb.append('\n');
		}
		return sb.toString();
	}
	
	@Override
	public List<MError> getErrors() {
		List<MError> result = new ArrayList<MError>();
		for (Line line : this.lines) {
			List<MError> lerr = line.getErrors();
			if (lerr != null) {
				result.addAll(lerr);
			}
		}
		return result;
	}

	@Override
	public void beautify() {		
		for (Line line : this.lines) {
			line.beautify();
		}	
	}

	public void write(OutputStream os) throws IOException {
		String seperator = System.getProperty("line.seperator");
		if (seperator == null) {
			seperator = "\n";
		}
		for (Line line : this.lines) {
			String lineAsString = line.getStringValue(); 
			os.write(lineAsString.getBytes());
			os.write(seperator.getBytes());
		}		
	}
		
	public void write(Path path) throws IOException {
		List<String> fileLines = new ArrayList<String>();
		for (Line line : this.lines) {
			if (line == null) {
				fileLines.add("");
			} else {
				String fileLine = line.getStringValue();
				fileLines.add(fileLine);
			}
		}
		Files.write(path, fileLines, StandardCharsets.UTF_8);
	}
	
	
	private static Routine getInstance(Scanner scanner) {
		Routine result = new Routine();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Line tokens = Line.getInstance(line);
			result.lines.add(tokens);
		}
		return result;				
	}
	
	public static Routine getInstance(InputStream is) {
		Scanner scanner = new Scanner(is);
		Routine result = getInstance(scanner);
		scanner.close();
		return result;				
	}
	
	public static Routine getInstance(Path file) throws IOException {
		Scanner scanner = new Scanner(file);
		Routine result = getInstance(scanner);
		scanner.close();
		return result;		
	}
}
