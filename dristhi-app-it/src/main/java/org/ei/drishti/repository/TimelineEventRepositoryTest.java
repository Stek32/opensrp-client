package org.ei.drishti.repository;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import org.ei.drishti.domain.TimelineEvent;
import org.ei.drishti.util.Session;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.ei.drishti.domain.TimelineEvent.forChildBirthInChildProfile;

public class TimelineEventRepositoryTest extends AndroidTestCase {
    private TimelineEventRepository repository;
    private Map<String, String> details;

    @Override
    protected void setUp() throws Exception {
        repository = new TimelineEventRepository();
        Session session = new Session().setPassword("password").setRepositoryName("drishti.db" + new Date().getTime());
        new Repository(new RenamingDelegatingContext(getContext(), "test_"), session, repository);
        details = new HashMap<String, String>();
    }

    public void testShouldInsertTimelineEvents() throws Exception {
        TimelineEvent event1 = forChildBirthInChildProfile("CASE X", "2012-12-12", details);
        TimelineEvent event2 = forChildBirthInChildProfile("CASE X", "2012-01-01", details);
        TimelineEvent event3 = forChildBirthInChildProfile("CASE Y", "2012-12-1", details);
        repository.add(event1);
        repository.add(event2);
        repository.add(event3);

        assertEquals(asList(event1, event2), repository.allFor("CASE X"));
    }

    public void testShouldDeleteTimelineEventsByCaseId() throws Exception {
        TimelineEvent event1 = forChildBirthInChildProfile("CASE X", "2012-12-12", details);
        TimelineEvent event2 = forChildBirthInChildProfile("CASE X", "2012-01-01", details);
        TimelineEvent event3 = forChildBirthInChildProfile("CASE Y", "2012-12-1", details);
        repository.add(event1);
        repository.add(event2);
        repository.add(event3);

        repository.deleteAllTimelineEventsForEntity("CASE X");

        assertEquals(Collections.emptyList(), repository.allFor("CASE X"));
        assertEquals(asList(event3), repository.allFor("CASE Y"));
    }
}
