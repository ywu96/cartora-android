package com.traveltime.android.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.traveltime.android.R;
import com.traveltime.android.Utils.Strings;

public class LoginActivity extends AppCompatActivity {

	private LoginButton fbLoginButton;
	private CallbackManager callbackManager;
	private AccessTokenTracker accessTokenTracker;
	private FirebaseAuth firebaseAuth;
	private FirebaseAuth.AuthStateListener authStateListener;
	private FloatingActionButton fab;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(this);
		setContentView(R.layout.activity_login);

		this.firebaseAuth = FirebaseAuth.getInstance();

//        if (this.firebaseAuth.getAuth() != null) {
//            Log.d("test123", "EventUserResponse logged in: " + this.firebaseAuth.getAuth());
//            Intent intent = new Intent(this, EventBaseActivity.class);
//            startActivity(intent);
//        } else {
//
//        }

		this.fab = (FloatingActionButton) findViewById(R.id.login_fab);
		this.fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (LoginActivity.this.firebaseAuth.getCurrentUser() != null) {
					SharedPreferences preferences = getApplicationContext().getSharedPreferences(Strings.SHARED_PREF_NAME, MODE_PRIVATE);
					preferences.edit().putString(Strings.UID_KEY, LoginActivity.this.firebaseAuth.getCurrentUser().getUid()).apply();
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
				if (currentAccessToken != null) {
					LoginActivity.this.authFirebaseWithFacebookToken(currentAccessToken);
				} else {
					FirebaseAuth.getInstance().signOut();
				}
			}
		};

		this.authStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					// user is signed in
					user.getPhotoUrl();
				} else {
					// user is signed out
				}
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.accessTokenTracker.startTracking();
		this.firebaseAuth.addAuthStateListener(this.authStateListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (this.accessTokenTracker != null && this.accessTokenTracker.isTracking()) {
			this.accessTokenTracker.stopTracking();
		}
		if (this.authStateListener != null) {
			this.firebaseAuth.removeAuthStateListener(this.authStateListener);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	protected void authFirebaseWithFacebookToken(AccessToken token) {
		AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
		this.firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (!task.isSuccessful()) {
					Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
