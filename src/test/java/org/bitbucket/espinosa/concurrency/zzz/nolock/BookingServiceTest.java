package org.bitbucket.espinosa.concurrency.zzz.nolock;

import org.bitbucket.espinosa.concurrency.util.BookingServiceAbstractTest;
import org.junit.Test;

/**
 * Unit test for {@link BookingServiceUnsynchronized} focused on its thread safety and lock handling.
 *  
 * @author Espinosa
 */
public class BookingServiceTest extends BookingServiceAbstractTest {
	
	@Test
	public void testServiceThreadSafety() throws Exception {
		testServiceThreadSafety(new BookingServiceUnsynchronized());
    }
}