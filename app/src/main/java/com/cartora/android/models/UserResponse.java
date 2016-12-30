package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

	@SerializedName("id")
	public int id;

	@SerializedName("first_name")
	public String firstName;

	@SerializedName("last_name")
	public String lastName;

	@SerializedName("photo")
	public String photo;

	@SerializedName("email")
	public String email;

	@SerializedName("auth_token")
	public String authToken;
}
