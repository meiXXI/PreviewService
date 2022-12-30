package com.meixxi.service.preview.controller;

import com.meixxi.service.preview.service.AboutService;
import com.meixxi.service.preview.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for system related REST requests.
 */
@RestController
public class SystemController {

	private static final Logger log = LoggerFactory.getLogger(SystemController.class);

	@Autowired
	private AboutService aboutService;

	@RequestMapping(value = "/version", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	public Version version() {
		return new Version();
	}

	@RequestMapping(value = "/status", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	public Status status() {
		return new Status();
	}

	/**
	 * Private model class for status.
	 */
	private class Status {
		private final String status;
		private final long startTime;
		private final String startTimeReadable;
		private final long duration;
		private final String durationReadable;
		private final String hostname;
		private final String instanceId;
		private final Version version;

		private Status() {
			this.status = "UP";
			this.version = new Version();
			this.startTime = aboutService.getStartTime();
			this.startTimeReadable = TimeUtil.millis2readable(aboutService.getStartTime());
			this.duration = System.currentTimeMillis() - aboutService.getStartTime();
			this.durationReadable = TimeUtil.duration2readable(this.duration);
			this.hostname = aboutService.getHostname();
			this.instanceId = aboutService.getInstanceId();
		}

		public String getStatus() {
			return status;
		}

		public long getStartTime() {
			return startTime;
		}

		public String getStartTimeReadable() {
			return startTimeReadable;
		}

		public long getDuration() {
			return duration;
		}

		public String getDurationReadable() {
			return durationReadable;
		}

		public String getHostname() {
			return hostname;
		}

		public String getInstanceId() { return instanceId; }

		public Version getVersion() {
			return version;
		}
	}

	/**
	 * Private model class for version
	 */
	private class Version {

		private final String name;
		private final String version;
		private final String buildNumber;
		private final String githubRunId;
		private final String buildTime;
		private final String commitId;
		private final String commitIdFull;
		private final String actor;

		private Version() {
			this.name = aboutService.getName();
			this.version = aboutService.getVersion();
			this.buildNumber = aboutService.getRunNumber();
			this.githubRunId = aboutService.getRunId();
			this.buildTime = aboutService.getBuildTime();
			this.commitId = aboutService.getCommitId();
			this.commitIdFull = aboutService.getCommitIdFull();
			this.actor = aboutService.getActor();
		}

		public String getName() {
			return name;
		}

		public String getVersion() {
			return version;
		}

		public String getBuildNumber() {
			return buildNumber;
		}

		public String getGithubRunId() {
			return githubRunId;
		}

		public String getBuildTime() {
			return buildTime;
		}

		public String getCommitId() {
			return commitId;
		}

		public String getCommitIdFull() {
			return commitIdFull;
		}

		public String getActor() {
			return actor;
		}
	}
}
