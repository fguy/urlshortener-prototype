package com.fguy;

/**
 * Base62 is in only alphabets and numbers. More recognizable than long digits
 * number or mixing with sign characters.
 * 
 * @author fguy
 *
 */
public abstract class Base62 {
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final int BASE = ALPHABET.length(); // 62

	public static String encode(long num) {
		if (num == 0) {
			return String.valueOf(ALPHABET.charAt(0));
		}
		StringBuilder sb = new StringBuilder();
		while (num > 0) {
			int rem = (int) num % BASE;
			num = num / BASE;
			sb.append(ALPHABET.charAt(rem));
		}
		return sb.reverse().toString();
	}

	public static long decode(String str) {
		int len = str.length();
		long result = 0;
		char[] arr = str.toCharArray();
		for (int i = 0; i < len; i++) {
			result += ALPHABET.indexOf(arr[i])
					* (int) Math.pow(62, len - i - 1);
		}
		return result;
	}
}
