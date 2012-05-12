package com.raygroupintl.m.token;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		TFCommandTest.class, TFDelimitedListTest.class,
		TFIntrinsicTest.class, TFTest.class, TFLineTest.class, TRoutineTest.class })
public class LocalTests {

}
