package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

public class SignUpRequest {

	@SerializedName("first_name")
	public String firstName;

	@SerializedName("last_name")
	public String lastName;

	@SerializedName("email")
	public String email;

	@SerializedName("password")
	public String password;

	@SerializedName("password_confirmation")
	public String passwordConfirmation;

	@SerializedName("fcm_id")
	public String fcmId;

	public SignUpRequest(String firstName, String lastName, String email, String password, String fcmId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.passwordConfirmation = password;
		this.fcmId = fcmId;
	}
}
