package com.raygroupintl.m.parsetree;

public interface Node {
	void accept(Visitor visitor);
}
