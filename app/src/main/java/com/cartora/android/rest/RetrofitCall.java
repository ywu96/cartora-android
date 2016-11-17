package com.cartora.android.rest;

import android.util.Log;

import retrofit2.Response;

public class RetrofitCall {

	public interface Callback<E> {
		void result(E result);
	}

	public static abstract class RetrofitCallback<T> implements retrofit2.Callback<T> {
		public void failure(Response response, String LOG_TAG) {
			Log.e(LOG_TAG, "Response Message: " + response.message());
			Log.e(LOG_TAG, "Response Status: " + response.code());
		}
	}

}
