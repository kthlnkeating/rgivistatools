package com.raygroupintl.vista.fnds;


public interface ITokenFactory {

	public abstract IToken tokenize(String line, int index);

}