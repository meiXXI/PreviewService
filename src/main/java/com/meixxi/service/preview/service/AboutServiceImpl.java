package com.meixxi.service.preview.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * Implementation of the About Service Interface.
 */
@Service
public class AboutServiceImpl implements AboutService {

	private static final Logger log = LoggerFactory.getLogger(AboutServiceImpl.class);

	private static final long startTime = System.currentTimeMillis();

	private static final String hostname = readHostname();

	private static final String instanceId = UUID.randomUUID().toString().substring(0,8);

	@Value("${app.name}")
	private String name;

	@Value("${app.version}")
	private String version;

	@Value("${GITHUB_RUN_ID:n. a.}")
	private String runId;

	@Value("${GITHUB_RUN_NUMBER:n. a.}")
	private String runNumber;

	@Value("${app.buildtime}")
	private String buildTime;

	@Value("${GITHUB_SHORT_SHA:n. a.}")
	private String commitId;

	@Value("${GITHUB_SHA:n. a.}")
	private String commitIdFull;

	@Value("${GITHUB_ACTOR:n. a.}")
	private String actor;

	/**
	 * Default constructor.
	 */
	public AboutServiceImpl() {
	}

	/**
	 * Read and returns the system's hostname.
	 * @return The system's hostname as string.
	 */
	private static String readHostname() {
		String hostname;

		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.error("Error reading hostname.", e);
			hostname = "UNKNOWN";
		}

		return hostname;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getRunId() {
		return runId;
	}

	@Override
	public String getRunNumber() {
		return runNumber;
	}

	@Override
	public String getBuildTime() {
		return buildTime;
	}

	@Override
	public String getCommitId() {
		return commitId;
	}

	@Override
	public long getStartTime() {
		return startTime;
	}

	@Override
	public String getHostname() {
		return hostname;
	}

	@Override
	public String getInstanceId() {
		return instanceId;
	}

	@Override
	public String getCommitIdFull() {
		return commitIdFull;
	}

	@Override
	public String getActor() {
		return actor;
	}
}