package com.raygroupintl.parser;

public interface OrderedName {
	String getName();
	OrderedName getLeading(OrderedNameContainer names);
	int getSequenceCount();
}
