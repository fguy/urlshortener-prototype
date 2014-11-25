package com.fguy;

import org.junit.Assert;
import org.junit.Test;

public class Base62Test {
	@Test
	public void test() throws Throwable {
		long num = 1212311313;
		String encoded = Base62.encode(num);

		Assert.assertEquals(num, Base62.decode(encoded));
	}

}
