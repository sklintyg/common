/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.util.logging;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

@RunWith(MockitoJUnitRunner.class)
public class MarkerFilterTest {

    private MarkerFilter markerFilter;
    private FilterReply accept = FilterReply.ACCEPT;
    private FilterReply deny = FilterReply.DENY;

    @Before
    public void setUp() {
        markerFilter = new MarkerFilter();
        markerFilter.setOnMatch(accept);
        markerFilter.setOnMismatch(deny);
    }

    @Test
    public void testSingleMarkerAcceptsMatchingEvent() {
        markerFilter.setMarker("markerName");
        markerFilter.start();
        Marker marker = MarkerFactory.getMarker("markerName");
        ILoggingEvent matchingEvent = mock(ILoggingEvent.class);
        when(matchingEvent.getMarker()).thenReturn(marker);
        Assert.assertEquals(accept, markerFilter.decide(matchingEvent));
    }

    @Test
    public void testSingleMarkerRejectsNonMatchingEvent() {
        markerFilter.setMarker("markerName");
        markerFilter.start();
        Marker anotherMarker = MarkerFactory.getMarker("anotherMarkerName");
        ILoggingEvent nonMatchingEvent = mock(ILoggingEvent.class);
        when(nonMatchingEvent.getMarker()).thenReturn(anotherMarker);
        Assert.assertEquals(deny, markerFilter.decide(nonMatchingEvent));
    }

    @Test
    public void testMultipleMarkerWithOnlyOneMarkerAcceptsMatchingEvent() {
        markerFilter.setMarkers(" markerName ");
        markerFilter.start();
        Marker marker = MarkerFactory.getMarker("markerName");
        ILoggingEvent matchingEvent = mock(ILoggingEvent.class);
        when(matchingEvent.getMarker()).thenReturn(marker);
        Assert.assertEquals(accept, markerFilter.decide(matchingEvent));
    }

    @Test
    public void testMultipleMarkerAcceptsMatchingEvents() {
        markerFilter.setMarkers(" markerOne, markerTwo ");
        markerFilter.start();
        Marker marker1 = MarkerFactory.getMarker("markerOne");
        ILoggingEvent matchingEvent1 = mock(ILoggingEvent.class);
        when(matchingEvent1.getMarker()).thenReturn(marker1);
        Assert.assertEquals(accept, markerFilter.decide(matchingEvent1));
        Marker marker2 = MarkerFactory.getMarker("markerTwo");
        ILoggingEvent matchingEvent2 = mock(ILoggingEvent.class);
        when(matchingEvent2.getMarker()).thenReturn(marker2);
        Assert.assertEquals(accept, markerFilter.decide(matchingEvent2));
    }

    @Test
    public void testMultipleMarkerRejectsNonMatchingEvent() {
        markerFilter.setMarkers(" markerOne, markerTwo ");
        markerFilter.start();
        Marker anotherMarker = MarkerFactory.getMarker("anotherMarkerName");
        ILoggingEvent nonMatchingEvent = mock(ILoggingEvent.class);
        when(nonMatchingEvent.getMarker()).thenReturn(anotherMarker);
        Assert.assertEquals(deny, markerFilter.decide(nonMatchingEvent));
    }

}
