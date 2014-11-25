package com.fguy;

import javax.ws.rs.WebApplicationException;

import org.junit.Assert;
import org.junit.Test;

public class UrlShortenerTest {
	UrlShortener shortener = new UrlShortener();

	@Test
	public void test() throws Throwable {
		String url = "http://www.apple.com/";
		String shorten1 = shortener.shorten(url);
		String shorten2 = shortener.shorten(url);
		Assert.assertEquals("Same url should have same id", shorten1, shorten2);
		Assert.assertEquals(url, shortener.resolve(shorten1).getLocation()
				.toString());
	}

	@Test(expected = WebApplicationException.class)
	public void testNonShortenId() throws Throwable {
		shortener.resolve("nevershorten");
	}
}
