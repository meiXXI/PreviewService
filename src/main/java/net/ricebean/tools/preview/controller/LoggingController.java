package net.ricebean.tools.preview.controller;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/logging")
public class LoggingController extends AppenderBase<ILoggingEvent> {

	private final static List<ILoggingEvent> events = new ArrayList<>(10000);

	@Override
	protected void append(ILoggingEvent event) {
		events.add(event);
	}

	@GetMapping
	public List<ILoggingEvent> getLogEvents() {
		return events;
	}
}
