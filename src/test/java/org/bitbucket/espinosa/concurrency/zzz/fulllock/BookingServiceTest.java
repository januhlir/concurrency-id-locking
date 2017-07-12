package org.bitbucket.espinosa.concurrency.zzz.fulllock;

import org.bitbucket.espinosa.concurrency.util.BookingServiceAbstractTest;
import org.bitbucket.espinosa.concurrency.util.ExclusivityCheck;
import org.junit.Test;

/**
 * Unit test for {@link BookingServiceFullySynchronized} focused on its thread
 * safety and lock handling.
 * 
 * It was meant as sanity check test for thread safety testing framework of
 * {@link BookingServiceAbstractTest} and {@link ExclusivityCheck}
 * 
 * @author Espinosa
 */
public class BookingServiceTest extends BookingServiceAbstractTest {
	
	@Test
	public void testServiceThreadSafety() throws Exception {
		testServiceThreadSafety(new BookingServiceFullySynchronized());
    }
}