package com.benlinus92.synchronize.config;

public final class AppConstants {
	public static final String TEMP_LOCATION = System.getenv("OPENSHIFT_TMP_DIR");
	public static final long MAX_FILE_SIZE = 1024 * 1024 * 30;
	public static final long MAX_REQUEST_SIZE = 1024 * 1024 * 40;
	public static final int FILE_SIZE_THRESHOLD = 0;
	public static final String VIDEOSTORE_LOCATION = System.getenv("OPENSHIFT_DATA_DIR") + "videos/";
	public static final String TYPE_UPLOAD_VIDEO = "upvideo";
	public static final String TYPE_YOUTUBE_VIDEO = "youtube";
}
