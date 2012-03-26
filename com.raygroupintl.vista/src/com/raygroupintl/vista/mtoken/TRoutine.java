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

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.token.TBase;

public class TRoutine extends TBase {
	private List<IToken> lines = new ArrayList<IToken>();
	
	public void add(IToken line) {
		this.lines.add(line);
	}
	
	public List<IToken> asList() {
		return this.lines;
	}
	
	@Override
	public String getStringValue() {
		StringBuilder sb = new StringBuilder();
		for (IToken line : this.lines) {
			sb.append(line.getStringValue());
			sb.append('\n');
		}
		return sb.toString();
	}
	
	@Override
	public List<MError> getErrors() {
		List<MError> result = null;
		for (IToken line : this.lines) {
			List<MError> lerr = line.getErrors();
			if (lerr != null) {
				if (result == null) {
					result = new ArrayList<MError>();	
				}				
				result.addAll(lerr);
			}
		}
		return result;
	}

	@Override
	public void beautify() {		
		for (IToken line : this.lines) {
			line.beautify();
		}	
	}

	public void write(OutputStream os) throws IOException {
		String seperator = System.getProperty("line.seperator");
		if (seperator == null) {
			seperator = "\n";
		}
		for (IToken line : this.lines) {
			String lineAsString = line.getStringValue(); 
			os.write(lineAsString.getBytes());
			os.write(seperator.getBytes());
		}		
	}
		
	public void write(Path path) throws IOException {
		List<String> fileLines = new ArrayList<String>();
		for (IToken line : this.lines) {
			if (line == null) {
				fileLines.add("");
			} else {
				String fileLine = line.getStringValue();
				fileLines.add(fileLine);
			}
		}
		Files.write(path, fileLines, StandardCharsets.UTF_8);
	}
	
	
	private static TRoutine getInstance(Scanner scanner) {
		TRoutine result = new TRoutine();
		TFLine f = TFLine.getInstance();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			IToken tokens = f.tokenize(line, 0);
			result.lines.add(tokens);
		}
		return result;				
	}
	
	public static TRoutine getInstance(InputStream is) {
		Scanner scanner = new Scanner(is);
		TRoutine result = getInstance(scanner);
		scanner.close();
		return result;				
	}
	
	public static TRoutine getInstance(Path file) throws IOException {
		Scanner scanner = new Scanner(file);
		TRoutine result = getInstance(scanner);
		scanner.close();
		return result;		
	}
}
