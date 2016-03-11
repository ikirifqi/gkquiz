package com.nextinnovation.gkquiz.application;

/**
 * Created by rifqi on Feb 14, 2016.
 */
public class Config {
    public enum AppMode { DEBUG, RELEASE }

    public static final String APP_LONG_NAME = "General Knowledge Quiz";
    public static final String APP_SHORT_NAME = "gkquiz";

    public static final String APP_LOG_TAG_VERBOSE = APP_SHORT_NAME + "_verbose";
    public static final String APP_LOG_TAG_INFO = APP_SHORT_NAME + "_info";
    public static final String APP_LOG_TAG_WARNING = APP_SHORT_NAME + "_warning";
    public static final String APP_LOG_TAG_ERROR = APP_SHORT_NAME + "_error";

    public static final String APP_FONT_BOLD = "berlin_sans_bold.ttf";
    public static final String APP_FONT_REGULAR = "berlin_sans_regular.ttf";

    public static final String STATIC_CATEGORY_LIST = "categories.json";
    public static final String STATIC_QUESTION_LIST = "questions.json";

    public static final AppMode APP_MODE = AppMode.DEBUG;

    public static final String APP_DATABASE_NAME = APP_SHORT_NAME + "_db";
    public static final int APP_DATABASE_VERSION = 1;
}
