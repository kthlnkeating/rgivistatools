package com.raygroupintl.vista.mtoken;

import java.util.Arrays;
import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.token.TList;
import com.raygroupintl.vista.token.TSyntaxError;

public class Line extends TList {	
	private boolean hasTagToken;
	private boolean hasLevelToken;
	
	@Override
	public List<MError> getErrors() {
		if (this.size() == 0) {
			MError[] errors = new MError[]{new MError(MError.ERR_NULL_LINE)};
			return Arrays.asList(errors);
		}
		return super.getErrors();
	}
	
	public int getLevel() {
		if (this.hasLevelToken) {
			int index = this.hasTagToken ? 1 : 0;
			IToken levelToken = this.get(index);
			String s = levelToken.getStringValue();
			int count = 0;
			for (int i=0; i<s.length(); ++i) {
				if (s.charAt(i) == '.') {
					++count;
				}
			}
			return count;
		}
		return 0;	
	}

	public String getTagView() {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		if (this.hasTagToken) {
			IToken tagToken = this.get(0);
			++index;
			sb.append(tagToken.getStringValue());
		}
		int count = this.getLevel();		
		sb.append(" LEVEL:" + String.valueOf(count) + " ");
		if (this.hasLevelToken) ++index;
		if (this.size() > index) {
			sb.append(" ...");
		}
		return sb.toString();
	}

	public String getCommandView() {
		StringBuilder sb = new StringBuilder();
		if (this.hasTagToken) {
			IToken token = this.get(0);
			sb.append(token.getStringValue());
		}
		if (this.hasLevelToken) {
			IToken token = this.get(this.hasTagToken ? 1 : 0);
			sb.append(token.getStringValue());
		}
		sb.append(this.getCommandView());
		return sb.toString();
	}

	public static Line getInstance(String line) {
		Line result = new Line();
		int length = line.length();
		if (line.length() > 0) {
			int index = 0;
			
			char ch = line.charAt(0);
			if (Character.isLetterOrDigit(ch) || ch == '%') {
				Tag tag = Tag.getInstance(line, 0);
				if (tag != null) {
					result.add(tag);
					result.hasTagToken = true;				
					index += tag.getStringSize();
				}
			}
			
			if (index < length) {
				ch = line.charAt(index);
				if (ch == ';') {
					Comment cmt = new Comment(line.substring(index));
					index = length;
					result.add(cmt);
				} else if (ch != ' ') {
					FatalError fet = new FatalError(MError.ERR_GENERAL_SYNTAX, line, index);
					index = length;
					result.add(fet);
				} else {
					Level level = Level.getInstance(line, index);
					result.add(level);
					result.hasLevelToken = true;
					index += level.getStringSize();
				}
			}
	
			while (index < length) {
				ch = line.charAt(index);
				if (ch == ';') {
					Comment cmt = new Comment(line.substring(index+1));
					index = length;
					result.add(cmt);				
				} else if (! Character.isLetter(ch)) {
					TSyntaxError err = new TSyntaxError(line, index);
					index = length;
					result.add(err);
				} else {
					TFCommand f = new TFCommand();
					IToken t = f.tokenize(line, index);
					result.add(t);
					index += t.getStringSize();
					//int toIndex = Algorithm.findOther(line, index, Algorithm.CharType.LETTER_DIGIT);
					//String identifier = line.substring(index, toIndex);
					//TCommand newToken = Line.getCommand(identifier);
					//index = newToken.extractDetails(line, toIndex);
					//result.add(newToken);
				}			
			}
		}
		return result;
	}
}
