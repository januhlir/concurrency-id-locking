package org.bitbucket.espinosa.concurrency.idlock2;

import org.bitbucket.espinosa.concurrency.util.BookingServiceAbstractTest;
import org.bitbucket.espinosa.concurrency.util.StopwatchRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit test for {@link BookingService2} focused on its thread safety and lock handling.
 *  
 * @author Espinosa
 */
public class BookingService2Test extends BookingServiceAbstractTest {
	
	@Rule
    public StopwatchRule watcher = new StopwatchRule();
	
	@Test
	public void testServiceThreadSafety() throws Exception {
		testServiceThreadSafety(new BookingService2());
    }
}