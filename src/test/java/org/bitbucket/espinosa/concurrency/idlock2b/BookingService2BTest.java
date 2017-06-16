package org.bitbucket.espinosa.concurrency.idlock2b;

import org.bitbucket.espinosa.concurrency.util.BookingServiceAbstractTest;
import org.junit.Test;

/**
 * Unit test for {@link BookingService2B} focused on its thread safety and lock handling.
 *  
 * @author Espinosa
 */
public class BookingService2BTest extends BookingServiceAbstractTest {
	
	@Test
	public void testServiceThreadSafety() throws Exception {
		testServiceThreadSafety(new BookingService2B());
    }
}