package org.bitbucket.espinosa.concurrency.zzz.setlock;

import org.bitbucket.espinosa.concurrency.util.BookingServiceAbstractTest;
import org.bitbucket.espinosa.concurrency.util.StopwatchRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit test for {@link BookingServiceZ3} focused on its thread safety and lock handling.
 *  
 * @author Espinosa
 */
public class BookingServiceZ3Test extends BookingServiceAbstractTest {
	
	@Rule
    public StopwatchRule watcher = new StopwatchRule();
	
	@Test
	public void testServiceThreadSafety() throws Exception {
		testServiceThreadSafety(new BookingServiceZ3());
    }
	// with waiting:    duration=97817 ms
	// without waiting: duration=243 ms
}