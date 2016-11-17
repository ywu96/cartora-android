package com.cartora.android.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cartora.android.R;
import com.cartora.android.models.SignUpRequest;
import com.cartora.android.rest.RestServer;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class LoginActivity extends AppCompatActivity {

	private EditText email;
	private EditText password;
	private EditText passwordConfirm;
	private Button signUpButton;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		setupViews();
	}

	private void setupViews() {
		email = (EditText) findViewById(R.id.login_email_edittext);
		password = (EditText) findViewById(R.id.login_pwd_edittext);
		passwordConfirm = (EditText) findViewById(R.id.login_pwd_confirm_edittext);
		signUpButton = (Button) findViewById(R.id.login_sign_up_button);

		password.setTypeface(Typeface.DEFAULT);
		password.setTransformationMethod(new PasswordTransformationMethod());
		passwordConfirm.setTypeface(Typeface.DEFAULT);
		passwordConfirm.setTransformationMethod(new PasswordTransformationMethod());

		signUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RestServer.getInstance().signUp(new SignUpRequest(
						"ha", "ha",
						email.getText().toString(),
						password.getText().toString()))
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Observer<String>() {
							@Override
							public void onCompleted() {
							}

							@Override
							public void onError(Throwable e) {
								Log.d("test123", "error");
								Intent intent = new Intent(LoginActivity.this, EventListActivity.class);
								startActivity(intent);
							}

							@Override
							public void onNext(String s) {
								Log.d("test123", "auth_token: " + s);
							}
						});
			}
		});
	}
}
