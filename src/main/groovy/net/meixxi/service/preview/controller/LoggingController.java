package net.meixxi.service.preview.controller;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/logging")
public class LoggingController extends AppenderBase<ILoggingEvent> {

	private final static List<LogEvent> events = new ArrayList<>(10000);

	@Override
	protected void append(ILoggingEvent event) {
		events.add(new LogEvent(event));
	}

	@GetMapping
	public List<LogEvent> getLogEvents() {

		List<LogEvent> result = new ArrayList<>(1000);

		for(int i = events.size(); i > 0 && result.size() < 1000 ; i --) {
			result.add(events.get(i -1));
		}


		return result;
	}

	/**
	 * Model class for a log event.
	 */
	private class LogEvent {
		private final long timeStamp;
		private final String level;
		private final String formattedMessage;
		private final String threadName;

		/**
		 * Custom constructor.
		 */
		private LogEvent(ILoggingEvent event) {
			timeStamp = event.getTimeStamp();
			this.level = event.getLevel().toString();
			this.formattedMessage = event.getFormattedMessage();
			this.threadName = event.getThreadName();
		}

		public long getTimeStamp() {
			return timeStamp;
		}

		public String getLevel() {
			return level;
		}

		public String getFormattedMessage() {
			return formattedMessage;
		}

		public String getThreadName() {
			return threadName;
		}
	}
}
