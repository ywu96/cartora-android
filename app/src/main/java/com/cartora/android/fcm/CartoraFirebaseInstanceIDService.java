package com.cartora.android.fcm;

import android.content.SharedPreferences;
import android.util.Log;

import com.cartora.android.models.FcmIdUpdateRequest;
import com.cartora.android.models.UserResponse;
import com.cartora.android.rest.RestServer;
import com.cartora.android.utils.Strings;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import rx.Observer;

public class CartoraFirebaseInstanceIDService extends FirebaseInstanceIdService {

	private SharedPreferences sharedPreferences;

	@Override
	public void onTokenRefresh() {
		// Store updated InstanceID token.
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		sharedPreferences = getSharedPreferences(Strings.SHARED_PREF_NAME, MODE_PRIVATE);
		sharedPreferences.edit()
				.putString(Strings.SHARED_PREF_FCM_ID_KEY, refreshedToken)
				.apply();

		Log.d("test123", "Token refresh: " + refreshedToken);

		if (sharedPreferences.getString(Strings.SHARED_PREF_AUTH_TOKEN_KEY, null) != null) {
			sendRegistrationToServer(refreshedToken);
		}
	}

	public void sendRegistrationToServer(final String refreshedToken) {
		RestServer.createService(sharedPreferences.getString(Strings.SHARED_PREF_AUTH_TOKEN_KEY, null))
				.updateFcmId(FcmIdUpdateRequest.from(refreshedToken))
				.subscribe(new Observer<UserResponse>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						// TODO: fix sketchy retry logic
						try {
							Thread.sleep(3000L);
							sendRegistrationToServer(refreshedToken);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}

					@Override
					public void onNext(UserResponse userResponse) {
					}
				});
	}
}
