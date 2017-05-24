package com.example.mahmoudshahen.egypttovisit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class SignUp extends AppCompatActivity {

    TextView mEmail , mPassword , mRepassword  ;
    Button mSignup ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Context mContext ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_up);
        mEmail = (TextView) findViewById(R.id.et_emailSignup);
        mPassword = (TextView) findViewById(R.id.et_passwordsignup);
        mRepassword = (TextView) findViewById(R.id.et_repasswordsignup);
        mSignup = (Button) findViewById(R.id.bt_signup);
        mContext = this ;
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = SignUp.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                String confirm_password =mRepassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(! password.equals(confirm_password))
                {
                    Toast.makeText(getApplicationContext(), "Confirm Password isn't match !", Toast.LENGTH_SHORT).show();
                    return ;

                }

                else {
                   // mProgressBar.setVisibility(View.VISIBLE);
                    //check for availability of the email
                    mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                            Toast.makeText(SignUp.this, "fetchProvidersForEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                           // mProgressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                // getProviders() will return size 1. if email ID is available.
                                if( task.getResult().getProviders().isEmpty())
                                {
                                    Intent intent = new Intent(new Intent(SignUp.this, Home.class));
                                    intent.putExtra("Email",email);
                                    intent.putExtra("Password",password);
                                    Thread thread=new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.v("test","first in run");
                                            mAuth.createUserWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            Toast.makeText(SignUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                            Log.v("test","in notify");
                                                            if (!task.isSuccessful()) {
                                                                Toast.makeText(SignUp.this, "Authentication failed." + task.getException(),
                                                                        Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(SignUp.this, "Done",
                                                                        Toast.LENGTH_SHORT).show();

                                                            }

                                                        }

                                                    });
                                        }
                                    });
                                    thread.start();

                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(SignUp.this, "Email is already exist",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(SignUp.this, "connection error or bad email format",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

}
