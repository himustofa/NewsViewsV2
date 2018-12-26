package com.apps.newsviews.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.newsviews.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    private EditText email, pass;
    private Button reg;

    private ProgressDialog progress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        progress = new ProgressDialog(this);


        email = (EditText) findViewById(R.id.student_email);
        pass = (EditText) findViewById(R.id.student_password);
        reg = (Button) findViewById(R.id.registration);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage(getResources().getString( R.string.progress));
                progress.show();

                registration();
            }
        });
    }

    private void registration() {
        final String e = email.getText().toString().trim();
        final String p = pass.getText().toString().trim();

        if (!TextUtils.isEmpty(e) && !TextUtils.isEmpty(p)) {
            mAuth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progress.cancel();
                        Toast.makeText(RegistrationActivity.this, "Registration successfully", Toast.LENGTH_SHORT).show();
                        //String id = FirebaseAuth.getInstance().getCurrentUser().getUid(); //Get UUID from created FirebaseAuth
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            progress.cancel();
                            alertDialog("Already have registered");
                        } else {
                            progress.cancel();
                            alertDialog(""+task.getException().getMessage());
                            Log.d(TAG, ""+task.getException().getMessage());
                        }
                    }
                }
            });
        } else {
            progress.cancel();
            alertDialog("Please insert the proper values in these fields");
        }
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
