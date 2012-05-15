package com.raygroupintl.m.token;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.raygroupintl.bnf.TFDelimitedListTest;

@RunWith(Suite.class)
@SuiteClasses({
		TFCommandTest.class, TFDelimitedListTest.class,
		TFIntrinsicTest.class, TFTest.class, TFLineTest.class, TRoutineTest.class })
public class LocalTests {

}
