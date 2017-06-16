package org.bitbucket.espinosa.concurrency.idlock3;

import org.bitbucket.espinosa.concurrency.util.BookingServiceAbstractTest;
import org.junit.Test;

/**
 * Unit test for {@link BookingService3} focused on its thread safety and lock handling.
 *  
 * @author Espinosa
 */
public class BookingService3Test extends BookingServiceAbstractTest {
	
	@Test
	public void testServiceThreadSafety() throws Exception {
		testServiceThreadSafety(new BookingService3());
    }
}