package org.bitbucket.espinosa.concurrency.util;

/**
 * Medium quality XOR Shift based random number generator. Here, it is used only
 * in tests. Major benefit of this implementation are: simplicity, speed and
 * non blocking code.
 * 
 * Source: https://stackoverflow.com/a/13533895/1185845
 */
public class XorShiftRandom {
	private long last;
	private static final XorShiftRandom INSTANCE = new XorShiftRandom();

	public XorShiftRandom() {
		this(System.currentTimeMillis());
	}

	public XorShiftRandom(long seed) {
		this.last = seed;
	}

	public int nextInt(int max) {
		last ^= (last << 21);
		last ^= (last >>> 35);
		last ^= (last << 4);
		int out = (int) last % max;     
		return (out < 0) ? -out : out;
	}
	
	public static int nextInt() {
		return INSTANCE.nextInt(Integer.MAX_VALUE);
	}
}