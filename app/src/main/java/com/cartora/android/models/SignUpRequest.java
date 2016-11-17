package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

public class SignUpRequest {

	@SerializedName("first_name")
	protected String firstName;

	@SerializedName("last_name")
	protected String lastName;

	@SerializedName("email")
	protected String email;

	@SerializedName("password")
	protected String password;

	@SerializedName("password_confirmation")
	protected String passwordConfirmation;

	public SignUpRequest(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.passwordConfirmation = password;
	}
}
