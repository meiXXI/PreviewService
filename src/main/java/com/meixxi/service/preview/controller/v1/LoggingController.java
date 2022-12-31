package com.meixxi.service.preview.controller.v1;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/logging")
public class LoggingController extends AppenderBase<ILoggingEvent> {
	private final static int MAX_ITEMS = 250;
	private final static List<LogEvent> events = new ArrayList<>(MAX_ITEMS);

	@Override
	protected void append(ILoggingEvent event) {
		events.add(new LogEvent(event));

		while(events.size() > MAX_ITEMS) {
			events.remove(0);
		}
	}

	@GetMapping
	public List<LogEvent> getLogEvents() {

		List<LogEvent> result = new ArrayList<>(200);

		for(int i = events.size(); i > 0 && result.size() < 200 ; i --) {
			result.add(events.get(i -1));
		}


		return result;
	}

	/**
	 * Model class for a log event.
	 */
	static class LogEvent {
		private final long timestamp;
		private final String level;
		private final String formattedMessage;
		private final String threadName;
		private final String loggerName;

		/**
		 * Custom constructor.
		 */
		private LogEvent(ILoggingEvent event) {
			this.timestamp = event.getTimeStamp();
			this.level = event.getLevel().toString();
			this.formattedMessage = event.getFormattedMessage();
			this.threadName = event.getThreadName();
			this.loggerName = event.getLoggerName();
		}

		public long getTimestamp() {
			return timestamp;
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

		public String getLoggerName() {
			return loggerName;
		}
	}
}
