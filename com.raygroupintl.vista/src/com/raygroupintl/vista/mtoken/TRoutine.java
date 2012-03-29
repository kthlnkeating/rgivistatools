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
	private String name;
	private List<TLine> lines = new ArrayList<TLine>();
	
	public TRoutine(String name) {
		this.name = name;
	}
	
	public void add(TLine line) {
		this.lines.add(line);
	}
	
	public List<TLine> asList() {
		return this.lines;
	}
	
	@Override
	public String getStringValue() {
		StringBuilder sb = new StringBuilder();
		for (TLine line : this.lines) {
			sb.append(line.getStringValue());
			sb.append('\n');
		}
		return sb.toString();
	}
	
	@Override
	public List<MError> getErrors() {
		List<MError> result = null;
		for (TLine line : this.lines) {
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
		for (TLine line : this.lines) {
			line.beautify();
		}	
	}
	
	public static String getEOL() {
		String eol = System.getProperty("line.separator");
		if (eol == null) {
			eol = "\n";
		}
		return eol;		
	}

	public void write(OutputStream os) throws IOException {
		String seperator = getEOL();
		for (TLine line : this.lines) {
			String lineAsString = line.getStringValue(); 
			os.write(lineAsString.getBytes());
			os.write(seperator.getBytes());
		}		
	}
		
	public int writeErrors(OutputStream os) throws IOException {
		String eol = getEOL();
		String lastTag = this.name;
		int count = 0;
		int index = 0;
		boolean first = true;
		for (TLine line : this.lines) {
			String tag = line.getTag();
			if (tag != null) {
				lastTag = tag;
				index = 0;
			}
			List<MError> errors = line.getErrors();
			boolean lineFirst = true;
			if ((errors != null) && (errors.size() > 0)) {
				if (first) {
					os.write((eol + eol + this.name + eol).getBytes());
					first = false;
				}
				if (lineFirst) {
					String offset = (index == 0 ? "" : '+' + String.valueOf(index));
					os.write(("  " + lastTag + offset + eol).getBytes());
					os.write(("    " + line.getStringValue() + eol).getBytes());
					lineFirst = false;
				}
				for (MError error : errors) {
					os.write(("    " + error.getText() + eol).getBytes());
					++count;
				}
			}
			++index;
		}		
		return count;
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
	
	
	private static TRoutine getInstance(String fileName, Scanner scanner) {
		TRoutine result = new TRoutine(fileName);
		TFLine f = TFLine.getInstance();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			TLine tokens = (TLine) f.tokenize(line, 0);
			result.lines.add(tokens);
		}
		return result;				
	}
	
	public static TRoutine getInstance(String fileName, InputStream is) {
		Scanner scanner = new Scanner(is);
		TRoutine result = getInstance(fileName, scanner);
		scanner.close();
		return result;				
	}
	
	public static TRoutine getInstance(Path file) throws IOException {
		String fileName = file.getFileName().toString();
		Scanner scanner = new Scanner(file);
		TRoutine result = getInstance(fileName, scanner);
		scanner.close();
		return result;		
	}
}
