package com.raygroupintl.m.cmdtree;

public interface Node {
	void accept(Visitor visitor);
}
