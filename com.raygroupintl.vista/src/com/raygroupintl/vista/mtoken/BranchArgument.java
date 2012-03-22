package com.raygroupintl.vista.mtoken;

import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.token.TSyntaxError;

public class BranchArgument extends Base {
	private IToken labelRef;
	private IToken offset;
	private IToken routineRef;
	private IToken actualList;
	private IToken postcond;
	private boolean isExternal;
	private IToken packageName;
	
	@Override
	public String getStringValue() {
		StringBuilder sb = new StringBuilder();		
		if (this.isExternal) {
			sb.append('&');
			Library.append(sb, this.packageName, '.');
		}
		Library.append(sb, this.labelRef);
		Library.append(sb, '+', this.offset);
		Library.append(sb, this.actualList);
		Library.append(sb, '^', this.routineRef);
		Library.append(sb, ':', this.postcond);
		return sb.toString();
	}

	@Override
	public void beautify() {
		if (this.labelRef != null) this.labelRef.beautify();
		if (this.offset != null) this.offset.beautify();
		if (this.routineRef != null) this.routineRef.beautify();
		if (this.actualList != null) this.actualList.beautify();
		if (this.postcond != null) this.postcond.beautify();
		if (this.packageName != null) this.packageName.beautify();
	}
	
	private static IToken getLabelInstance(String line, int fromIndex) {
		return null;
		//ITokenFactory f = new LabelFactory();
		//IToken label = f.tokenize(line, fromIndex);
		//return label;
	}
		
	public static IToken getInstance(String line, int fromIndex) {
		int endIndex = line.length();
		int index = fromIndex;		
		if (fromIndex < endIndex) {
			BranchArgument result = new BranchArgument();
			char ch = line.charAt(index);
			if (ch == '&') {
				result.isExternal = true;
				++index;
				IToken name = null; //Name.getInstance(line, index);
				if (name == null) {
					return new TSyntaxError(line, fromIndex);
				}
				index += name.getStringSize();
				if (index == endIndex) {
					result.labelRef = name;
					return result;
				}
				ch = line.charAt(index);
				if (ch == '.') {
					result.packageName = name;
					++index;
					IToken label = getLabelInstance(line, index);
					if (label == null) {
						return new TSyntaxError(line, fromIndex);
					}
				} else {
					result.labelRef = name;
				}
			} else {
				IToken label = getLabelInstance(line, index);
				if (label != null) {
					if (label instanceof TSyntaxError) {
						return new TSyntaxError(line, fromIndex);
					}
					result.labelRef = label;
					index += label.getStringSize();
					if (index >= endIndex) {
						return result;
					}
					ch = line.charAt(index);
					if (ch == '+') {
						++index;
						IToken offset = Algorithm.tokenize(line, index, ' ', '^', ':');
						if ((offset == null) || (offset instanceof TSyntaxError)) {
							return new TSyntaxError(line, fromIndex);
						}
						result.offset = offset;
						index += offset.getStringSize();
					}
				}
			}
			if (index < endIndex) {
				ch = line.charAt(index);
				if (ch == '^') {
					++index;
					ITokenFactory f = null; //new RoutineRefFactory();
					IToken routineRef = f.tokenize(line, index);
					if (routineRef == null) {
						return new TSyntaxError(line, fromIndex);						
					}
					if (routineRef instanceof TSyntaxError) {
						return new TSyntaxError(line, fromIndex);												
					}
					result.routineRef = routineRef;
					index += routineRef.getStringSize();
					if (index >= endIndex) {
						return result;
					}
					ch = line.charAt(index);
				}
				if (ch == ':') {
					IToken offset = Algorithm.tokenize(line, index, ' ', ':');
					
				}				
			}
			if ((result.labelRef == null) && (result.routineRef == null)) {
				return new TSyntaxError(line, fromIndex);				
			}
			return result;			
		}		
		return null;		
	}
	
	@Override
	public boolean isError() {
		return false;
	}

	@Override
	public List<MError> getErrors() {
		return null;
	}
}
