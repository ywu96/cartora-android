package com.yifanfwu.locationevents.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.yifanfwu.locationevents.R;

public class LoginActivity extends AppCompatActivity {

	protected LoginButton fbLoginButton;
	protected CallbackManager callbackManager;
	protected AccessTokenTracker accessTokenTracker;
	protected Firebase firebaseRef;
	protected FloatingActionButton fab;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(this);
		Firebase.setAndroidContext(this);
		setContentView(R.layout.activity_login);

		this.firebaseRef = new Firebase("https://vivid-inferno-3846.firebaseio.com");

//        if (this.firebaseRef.getAuth() != null) {
//            Log.d("test123", "EventUserResponse logged in: " + this.firebaseRef.getAuth());
//            Intent intent = new Intent(this, EventBaseActivity.class);
//            startActivity(intent);
//        } else {
//
//        }

		this.fab = (FloatingActionButton) findViewById(R.id.login_fab);
		this.fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (LoginActivity.this.firebaseRef.getAuth() != null) {
					Intent intent = new Intent(getBaseContext(), EventBaseActivity.class);
					startActivity(intent);
				}
			}
		});

		this.callbackManager = CallbackManager.Factory.create();

		this.fbLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
		this.fbLoginButton.setReadPermissions("user_friends");
		this.fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				LoginActivity.this.authFirebaseWithFacebookToken(loginResult.getAccessToken());
			}

			@Override
			public void onCancel() {

			}

			@Override
			public void onError(FacebookException error) {

			}
		});

		this.accessTokenTracker = new AccessTokenTracker() {
			@Override
			protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
				LoginActivity.this.authFirebaseWithFacebookToken(currentAccessToken);
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (this.accessTokenTracker != null && !this.accessTokenTracker.isTracking()) {
			this.accessTokenTracker.startTracking();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (this.accessTokenTracker != null && this.accessTokenTracker.isTracking()) {
			this.accessTokenTracker.stopTracking();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	protected void authFirebaseWithFacebookToken(AccessToken token) {
		if (token != null) {
			this.firebaseRef.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
				@Override
				public void onAuthenticated(AuthData authData) {
					// The Facebook user is now authenticated with your Firebase app
				}
				@Override
				public void onAuthenticationError(FirebaseError firebaseError) {
					// there was an error
				}
			});
		} else {
			/* Logged out of Facebook so do a logout from the Firebase app */
			this.firebaseRef.unauth();
		}
	}
}
