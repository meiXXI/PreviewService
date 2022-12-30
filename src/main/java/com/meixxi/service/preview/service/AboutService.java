package com.meixxi.service.preview.service;

/**
 * Service interface providing details about the current library.
 */
public interface AboutService {

	/**
	 * Returns the name of the addon
	 * @return the addons name
	 */
	String getName();

	/**
	 * Returns the version of the addon.
	 * @return The addons version.
	 */
	String getVersion();

	/**
	 * Retruns the build time of the application.
	 * @return The addons build time.
	 */
	String getBuildTime();

	/**
	 * Returns the commit identifier of the latest change.
	 * @return The latest commit identifier.
	 */
	String getCommitId();

	/**
	 * Returns the service's start time.
	 * @return The service's start time.
	 */
	long getStartTime();

	/**
	 * Returns the service's hostname.
	 * @return The service's hostname.
	 */
	String getHostname();

	/**
	 * Returns the service's instance id.
	 * @return The service's instance id.
	 */
	String getInstanceId();

	/**
	 * Returns the pipeline id of the build.
	 * @return The pipeline ide of the build.
	 */
	String getRunId();

	/**
	 * Returns the projects build number.
	 * @return The projects build number
	 */
	String getRunNumber();

	/**
	 * Returns the full commit id.
	 * @return the full commit id.
	 */
	String getCommitIdFull();

	/**
	 * Returns the user's name who has initiated this build.
	 * @return The user's name who has initiated this build.
	 */
	String getActor();
}