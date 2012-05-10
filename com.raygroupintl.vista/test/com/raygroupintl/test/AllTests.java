package com.raygroupintl.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.raygroupintl.bnf.GrammarTest;
import com.raygroupintl.bnf.annotation.DescriptionSpecTest;
import com.raygroupintl.m.token.test.LocalTests;

@RunWith(Suite.class)
@SuiteClasses({GrammarTest.class, DescriptionSpecTest.class, LocalTests.class})
public class AllTests {
}
