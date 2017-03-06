package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

public class FcmIdUpdateRequest {

	@SerializedName("fcm_id")
	String fcmId;

	public static FcmIdUpdateRequest from(String fcmId) {
		return new FcmIdUpdateRequest(fcmId);
	}

	private FcmIdUpdateRequest(String fcmId) {
		this.fcmId = fcmId;
	}
}
