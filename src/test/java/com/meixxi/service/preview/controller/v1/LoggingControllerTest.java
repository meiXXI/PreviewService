package com.meixxi.service.preview.controller.v1;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.meixxi.service.preview.controller.v1.LoggingController.LogEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@Execution(SAME_THREAD)
class LoggingControllerTest {

    @Test
    void append_1() {

        // arrange
        LoggingController loggingController = new LoggingController();
        List<LogEvent> eventsCache = (List<LogEvent>) ReflectionTestUtils.getField(LoggingController.class, "events");
        eventsCache.clear();

        // act
        for(long l = 0; l < 500; l ++) {
            ILoggingEvent loggingEventMock = mock(ILoggingEvent.class);
            doReturn(l).when(loggingEventMock).getTimeStamp();
            doReturn(Level.INFO).when(loggingEventMock).getLevel();
            doReturn("Message").when(loggingEventMock).getFormattedMessage();
            doReturn("ThreadName").when(loggingEventMock).getThreadName();
            doReturn("LoggerName").when(loggingEventMock).getLoggerName();

            loggingController.append(loggingEventMock);
        }

        // assert
        Assertions.assertEquals(250, eventsCache.size());

        List<LogEvent> events = loggingController.getLogEvents();
        Assertions.assertEquals(200, events.size());
        Assertions.assertEquals(499, events.get(0).getTimestamp());
        Assertions.assertEquals(300, events.get(199).getTimestamp());
    }

    @Test
    void append_2() {

        // arrange
        LoggingController loggingController = new LoggingController();
        List<LogEvent> eventsCache = (List<LogEvent>) ReflectionTestUtils.getField(LoggingController.class, "events");
        eventsCache.clear();

        // act
        for(long l = 0; l < 200; l ++) {
            ILoggingEvent loggingEventMock = mock(ILoggingEvent.class);
            doReturn(l).when(loggingEventMock).getTimeStamp();
            doReturn(Level.INFO).when(loggingEventMock).getLevel();
            doReturn("Message").when(loggingEventMock).getFormattedMessage();
            doReturn("ThreadName").when(loggingEventMock).getThreadName();
            doReturn("LoggerName").when(loggingEventMock).getLoggerName();

            loggingController.append(loggingEventMock);
        }

        // assert
        Assertions.assertEquals(200, eventsCache.size());

        List<LogEvent> events = loggingController.getLogEvents();
        Assertions.assertEquals(200, events.size());
        Assertions.assertEquals(199, events.get(0).getTimestamp());
        Assertions.assertEquals(0, events.get(199).getTimestamp());
    }

    @Test
    void append_3() {

        // arrange
        LoggingController loggingController = new LoggingController();
        List<LogEvent> eventsCache = (List<LogEvent>) ReflectionTestUtils.getField(LoggingController.class, "events");
        eventsCache.clear();

        // act
        for(long l = 0; l < 25; l ++) {
            ILoggingEvent loggingEventMock = mock(ILoggingEvent.class);
            doReturn(l).when(loggingEventMock).getTimeStamp();
            doReturn(Level.INFO).when(loggingEventMock).getLevel();
            doReturn("Message").when(loggingEventMock).getFormattedMessage();
            doReturn("ThreadName").when(loggingEventMock).getThreadName();
            doReturn("LoggerName").when(loggingEventMock).getLoggerName();

            loggingController.append(loggingEventMock);
        }

        // assert
        Assertions.assertEquals(25, eventsCache.size());

        List<LogEvent> events = loggingController.getLogEvents();
        Assertions.assertEquals(25, events.size());
        Assertions.assertEquals(24, events.get(0).getTimestamp());
        Assertions.assertEquals(0, events.get(24).getTimestamp());
    }
}