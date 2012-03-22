package com.raygroupintl.vista.token;

import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenArray;
import com.raygroupintl.vista.struct.MError;

public class TPair implements IToken, ITokenArray {
	private IToken t0;
	private IToken t1;
	
	public TPair(IToken t0) {
		this.t0 = t0;
	}

	public TPair(IToken t0, IToken t1) {
		this.t0 = t0;
		this.t1 = t1;
	}
		
	public TPair(TPair p) {
		this.t0 = p.t0;
		this.t1 = p.t1;
	}

	@Override
	public String getStringValue() {
		if (this.t1 == null) {
			return this.t0.getStringValue();
		} else {
			return this.t0.getStringValue() + this.t1.getStringValue();
		}
	}

	@Override
	public int getStringSize() {
		if (this.t1 == null) {
			return this.t0.getStringSize();
		} else {
			return this.t0.getStringSize() + this.t1.getStringSize();
		}
	}

	@Override
	public List<MError> getErrors() {
		List<MError> es0 = this.t0.getErrors();
		List<MError> es1 = (this.t1 == null) ? null : this.t1.getErrors();
		if (es0 == null) {
			return es1;
		}
		if (es1 == null) {
			return es0;
		}
		es0.addAll(es1);
		return es0;
	}
	
	@Override
	public boolean hasError() {
		if (this.t1 == null) {
			return this.t0.hasError();
		} else {
			return this.t0.hasError() || this.t1.hasError();
		}
	}

	@Override
	public boolean hasFatalError() {
		if (this.t1 == null) {
			return this.t0.hasFatalError();
		} else {
			return this.t0.hasFatalError() || this.t1.hasFatalError();
		}
	}

	@Override
	public void beautify() {
		this.t0.beautify();
		if (this.t1 != null) this.t1.beautify();
	}
	
	@Override
	public boolean isError() {
		return false;
	}
	
	@Override
	public int getCount() {
		return 2;
	}
	
	@Override
	public IToken get(int i) {
		if (i == 0) return this.t0;
		if (i == 1) return this.t1;
		return null;
	}
}
