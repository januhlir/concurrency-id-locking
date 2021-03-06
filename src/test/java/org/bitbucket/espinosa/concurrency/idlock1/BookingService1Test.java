package org.bitbucket.espinosa.concurrency.idlock1;

import org.bitbucket.espinosa.concurrency.util.BookingServiceAbstractTest;
import org.bitbucket.espinosa.concurrency.util.StopwatchRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit test for {@link BookingService1} focused on its thread safety and lock handling.
 *  
 * @author Espinosa
 */
public class BookingService1Test extends BookingServiceAbstractTest {
	
	@Rule
    public StopwatchRule watcher = new StopwatchRule();
	
	@Test
	public void testServiceThreadSafety() throws Exception {
		testServiceThreadSafety(new BookingService1());
    }
}