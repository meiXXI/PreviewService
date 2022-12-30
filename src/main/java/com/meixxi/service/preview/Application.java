package com.meixxi.service.preview;

import com.meixxi.service.preview.service.AboutService;
import org.cip4.lib.xjdf.xml.XJdfConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private AboutService aboutService;

	/**
	 * Applications main entrance point.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Event is called after applications start up.
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void onStartUp() {
		log.warn(String.format("%s %s has started. (rev: %s)", aboutService.getName(), aboutService.getVersion(), aboutService.getCommitId()));

		// set constants
		XJdfConstants.AGENT_NAME = aboutService.getName();
		XJdfConstants.AGENT_VERSION = aboutService.getVersion();
		XJdfConstants.DEVICE_ID = "PREVIEW_SERVICE";
	}
}
