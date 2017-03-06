package com.cartora.android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cartora.android.R;
import com.cartora.android.models.SignUpRequest;
import com.cartora.android.models.UserResponse;
import com.cartora.android.rest.RestServer;
import com.cartora.android.utils.Strings;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class LoginActivity extends AppCompatActivity {

	private EditText email;
	private EditText password;
	private Button signUpButton;
	private Button logInButton;

	private SharedPreferences sharedPreferences;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		sharedPreferences = getSharedPreferences(Strings.SHARED_PREF_NAME, MODE_PRIVATE);

		// TODO: just for testing
//		String authToken = sharedPreferences.getString(Strings.SHARED_PREF_AUTH_TOKEN_KEY, null);
//		if (!Strings.isNullOrEmpty(authToken)) {
//			Log.d("test123", authToken);
//			goToEventListActivity();
//		}

		setupViews();
	}

	private void setupViews() {
		email = (EditText) findViewById(R.id.login_email_edittext);
		password = (EditText) findViewById(R.id.login_pwd_edittext);
		signUpButton = (Button) findViewById(R.id.login_sign_up_button);
		logInButton = (Button) findViewById(R.id.login_log_in_button);

		password.setTypeface(Typeface.DEFAULT);
		password.setTransformationMethod(new PasswordTransformationMethod());

		signUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RestServer.createService().signUp(new SignUpRequest(
						"ha", "ha",
						email.getText().toString(),
						password.getText().toString(),
						sharedPreferences.getString(Strings.SHARED_PREF_FCM_ID_KEY, null)))
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Observer<UserResponse>() {
							@Override
							public void onCompleted() {
							}

							@Override
							public void onError(Throwable e) {
								Snackbar.make(findViewById(R.id.container), R.string.error_generic_retry, Snackbar.LENGTH_SHORT).show();
							}

							@Override
							public void onNext(UserResponse userResponse) {
								Log.d("test123", userResponse.authToken);
								SharedPreferences.Editor editor = sharedPreferences.edit();
								editor.putInt(Strings.SHARED_PREF_ID_KEY, userResponse.id);
								editor.putString(Strings.SHARED_PREF_FIRST_NAME_KEY, userResponse.firstName);
								editor.putString(Strings.SHARED_PREF_LAST_NAME_KEY, userResponse.lastName);
								editor.putString(Strings.SHARED_PREF_AVATAR_KEY, userResponse.photo);
								editor.putString(Strings.SHARED_PREF_EMAIL_KEY, userResponse.email);
								editor.putString(Strings.SHARED_PREF_AUTH_TOKEN_KEY, userResponse.authToken);
								editor.apply();

								goToEventListActivity();
							}
						});
			}
		});

		// TODO: just for testing
		logInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int userId = sharedPreferences.getInt(Strings.SHARED_PREF_ID_KEY, -1);
				String authToken = sharedPreferences.getString(Strings.SHARED_PREF_AUTH_TOKEN_KEY, null);

				if (!Strings.isNullOrEmpty(authToken)) {
					Log.d("test123", userId + " " + authToken);
					goToEventListActivity();
				}
			}
		});
	}

	private void goToEventListActivity() {
		Intent intent = new Intent(LoginActivity.this, EventListActivity.class);
		startActivity(intent);
	}
}
