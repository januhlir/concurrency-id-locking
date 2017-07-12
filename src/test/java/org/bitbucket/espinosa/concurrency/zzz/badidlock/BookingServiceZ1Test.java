package org.bitbucket.espinosa.concurrency.zzz.badidlock;

import org.bitbucket.espinosa.concurrency.util.BookingServiceAbstractTest;
import org.junit.Test;

/**
 * Unit test for {@link BookingServiceZ1} focused on its thread safety and lock
 * handling. This test is expected to fail. It demonstrates that solution from
 * {@link BookingServiceZ1} is not thread safe.
 * 
 * @author Espinosa
 */
public class BookingServiceZ1Test extends BookingServiceAbstractTest {
	
	@Test
	public void testServiceThreadSafety() throws Exception {
		testServiceThreadSafety(new BookingServiceZ1());
    }
}