package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

public class UserPublicResponse {

	@SerializedName("id")
	public int id;

	@SerializedName("first_name")
	public String firstName;

	@SerializedName("last_name")
	public String lastName;

	@SerializedName("photo")
	public String avatar;

	@SerializedName("email")
	public String email;
}
