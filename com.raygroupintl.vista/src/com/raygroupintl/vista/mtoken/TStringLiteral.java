package com.raygroupintl.vista.mtoken;

public class TStringLiteral extends TBasic {
	public TStringLiteral(String value) {
		super(value);
	}
	
	@Override
	public String getStringValue() {
		return '"' + super.getStringValue().replaceAll("\"", "\"\"") + '"';
	}

	@Override
	public int getStringSize() {
		String value = super.getStringValue();
		int quoteCount = 0;
		for (int i=0; i<value.length(); ++i) if (value.charAt(i) == '"') ++quoteCount;
		return 2 + quoteCount + super.getStringSize();
	}
}
