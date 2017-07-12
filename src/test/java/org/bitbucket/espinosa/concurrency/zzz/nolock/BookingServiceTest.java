package org.bitbucket.espinosa.concurrency.zzz.nolock;

import org.bitbucket.espinosa.concurrency.util.BookingServiceAbstractTest;
import org.junit.Test;

/**
 * Unit test for {@link BookingServiceUnsynchronized}. It was meant as sanity
 * check for testing framework of {@link BookingServiceAbstractTest}. Nothing is
 * really tested here.
 * 
 * @author Espinosa
 */
public class BookingServiceTest extends BookingServiceAbstractTest {
	
	@Test
	public void testServiceThreadSafety() throws Exception {
		testServiceThreadSafety(new BookingServiceUnsynchronized());
    }
}