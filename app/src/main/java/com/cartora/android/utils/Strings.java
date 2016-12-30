package com.cartora.android.utils;

public class Strings {

	public static final String SHARED_PREF_NAME = "com.cartora.android_preferences";

	public static final String SHARED_PREF_ID_KEY = "id";
	public static final String SHARED_PREF_FIRST_NAME_KEY = "first_name";
	public static final String SHARED_PREF_LAST_NAME_KEY = "last_name";
	public static final String SHARED_PREF_AVATAR_KEY = "avatar";
	public static final String SHARED_PREF_EMAIL_KEY = "email";
	public static final String SHARED_PREF_AUTH_TOKEN_KEY = "auth_token";

	public static boolean isNullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}
}
