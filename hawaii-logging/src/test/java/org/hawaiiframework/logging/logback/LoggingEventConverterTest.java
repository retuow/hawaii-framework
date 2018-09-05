package org.hawaiiframework.logging.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoggingEventConverterTest {

    @Mock
    private IThrowableProxyConverter iThrowableProxyConverter;

    @Mock
    private IThrowableProxy iThrowableProxy;

    @Mock
    private LoggingEvent loggingEvent;

    private StackTraceElement stackTraceElement;

    private static final String CONVERTED_MESSAGE = "converted message";
    private static final String FORMATTED_MESSAGE = "formatted message";
    private static final String BASE_LOG_MESSAGE =
            "1970-01-01 01:00:00,000 DEBUG -  log.loc=\"org.hawaiiframework.logging.logback.LoggingEventConverterTest:9\" thread=\"threadio\" message=#";

    private LoggingEventConverter loggingEventConverter;

    @Before
    public void setUp() {
        stackTraceElement = new StackTraceElement(this.getClass().getCanonicalName(), "<methodName>", "<fileName>", 9);
        when(loggingEvent.getCallerData()).thenReturn(new StackTraceElement[] {stackTraceElement});
        when(loggingEvent.getThreadName()).thenReturn("threadio");
        when(loggingEvent.getThrowableProxy()).thenReturn(iThrowableProxy);

        loggingEventConverter = new LoggingEventConverter(iThrowableProxyConverter);
        when(loggingEvent.getLevel()).thenReturn(Level.DEBUG);
    }

    @Test
    public void thatDoEncodeHasBothLoggingEvenMessageAsWellAsIThrowableProxyMessage() {

        when(loggingEvent.getFormattedMessage()).thenReturn(FORMATTED_MESSAGE);
        when(iThrowableProxyConverter.convert(iThrowableProxy)).thenReturn(CONVERTED_MESSAGE);

        assertThat(loggingEventConverter.convert(loggingEvent), is(equalTo(
                BASE_LOG_MESSAGE + CharacterConstants.INDENT + FORMATTED_MESSAGE + CharacterConstants.NEW_LINE + CharacterConstants.INDENT
                        + CONVERTED_MESSAGE + CharacterConstants.NEW_LINE)));

    }

    @Test
    public void thatMultiLineLogEventsStartWithAWhitespace() {
        Throwable throwable = null;
        try {
            new ExceptionThrower().xyz();
        } catch (Throwable t) {
            throwable = t;
        }

        assertThat(throwable, is(not(nullValue())));

        when(loggingEvent.getThrowableProxy()).thenReturn(new ThrowableProxy(throwable));
        final String messageWithLineEnding = "Some multi line " + System.lineSeparator() + "error message.";
        when(loggingEvent.getFormattedMessage()).thenReturn(messageWithLineEnding);
        loggingEventConverter = new LoggingEventConverter(new IThrowableProxyConverter());

        final String converted = loggingEventConverter.convert(loggingEvent);
        final String[] lines = converted.split(System.lineSeparator());
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            assertThat("Line '" + line + "' does not start with a whitespace.", Character.isWhitespace(line.charAt(0)), is(true));
        }
    }


}