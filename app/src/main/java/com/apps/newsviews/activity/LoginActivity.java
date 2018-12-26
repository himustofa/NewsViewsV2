package com.apps.newsviews.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.newsviews.R;
import com.apps.newsviews.utility.ConstantKey;
import com.apps.newsviews.utility.SharedPrefManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    //https://firebase.google.com/docs/auth/android/google-signin
    private static final String TAG = "LoginActivity";
    public static final int RC_SIGN_IN = 101;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private EditText email, pass;
    private Button log;
    private SignInButton mGoogleSignIn;
    private ProgressDialog progress;

    private SharedPreferences preferences;
    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(this);

        email = (EditText) findViewById(R.id.student_email);
        pass = (EditText) findViewById(R.id.student_password);
        log = (Button) findViewById(R.id.login);
        mGoogleSignIn = (SignInButton) findViewById(R.id.gmail_login_button);

        //===============================================| Getting SharedPreferences
        isLoggedIn = SharedPrefManager.getInstance(LoginActivity.this).getSharedPrefIsLoggedIn();
        if (isLoggedIn) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }

        //====================================================| Logged In
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage(getResources().getString( R.string.progress));
                progress.show();
                userLoggedIn();
            }
        });

        //====================================================| Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage(getResources().getString( R.string.progress));
                progress.show();
                signIn();
            }
        });

        //====================================================| Registration Activity
        TextView textView = (TextView) findViewById(R.id.registration);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    //====================================================| Logged In
    private void userLoggedIn() {
        final String e = email.getText().toString().trim();
        final String p = pass.getText().toString().trim();
        if (!TextUtils.isEmpty(e) && !TextUtils.isEmpty(p)) {
            mAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        SharedPrefManager.getInstance(LoginActivity.this).saveSharedPref(email.getText().toString(), true);
                        progress.cancel();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        //startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                        finish();
                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        progress.cancel();
                        alertDialog(""+task.getException().getMessage());
                        Log.d(TAG, ""+task.getException().getMessage());
                    }
                }
            });
        } else {
            progress.cancel();
            alertDialog("Empty fields are not allowed.");
        }
    }

    //====================================================| Google Sign In
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                progress.cancel();
                alertDialog("Google sign in failed");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPrefManager.getInstance(LoginActivity.this).saveSharedPref(email.getText().toString(), true);
                            progress.cancel();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                            Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            progress.cancel();
                            alertDialog("signInWithCredential:failure "+task.getException());
                        }
                    }
                });
    }

    //====================================================| Alert Message
    public void alertDialog(String msg) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();
    }
    
    
}
