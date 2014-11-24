package org.jbake.forge.addon.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.jbake.forge.addon.utils.JBakeUtil.toSlug;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JBakeUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void toSlugShouldMakeLowerCase() {
		String slugString = toSlug("ThisIsATest");
		assertThat(slugString, equalTo("thisisatest"));
	}

	@Test
	public void toSlugShouldMakeIphensForSpace() {
		String slugString = toSlug("This is a test");
		assertThat(slugString, equalTo("this-is-a-test"));
	}

	@Test
	public void toSlugShouldMakeDoubleIphensForDoubleSpace() {
		String slugString = toSlug("This is a  test");
		assertThat(slugString, equalTo("this-is-a--test"));
	}

	@Test
	public void toSlugShouldRemoveSingleQuote() {
		String slugString = toSlug("This is test's");
		assertThat(slugString, equalTo("this-is-tests"));
	}

	@Test
	public void toSlugShouldRemoveDoubleQuote() {
		String slugString = toSlug("This is test\"s");
		assertThat(slugString, equalTo("this-is-tests"));
	}

	@Test
	public void toSlugShouldRemoveDoubleQuot() {
		String slugString = toSlug("This is test & run");
		System.out.println(slugString);
	}

}
