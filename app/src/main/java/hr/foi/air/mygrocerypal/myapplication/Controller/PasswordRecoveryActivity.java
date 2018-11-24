package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import hr.foi.air.mygrocerypal.myapplication.Core.BaseActivity;
import hr.foi.air.mygrocerypal.myapplication.R;
import hr.foi.air.mygrocerypal.myapplication.View.LoginActivity;

public class PasswordRecoveryActivity extends BaseActivity/* implements View.OnClickListener*/ {
    private ProgressBar progressBar;
    private EditText userEmail;
    private Button buttonRecoveryPassword, showLogin;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        buttonRecoveryPassword = findViewById(R.id.buttonRecoveryPassword);
        showLogin = findViewById(R.id.buttonShowLogin);
        progressBar = findViewById(R.id.progressBar);

        userEmail =(EditText) findViewById(R.id.emailRecoveryPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        buttonRecoveryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail(userEmail)){
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        Toast.makeText(PasswordRecoveryActivity.this, "Link za promjenu lozinke je poslan. Provjerite email!", Toast.LENGTH_LONG).show();
                                        ShowActivity(LoginActivity.class);
                                    }
                                    else{
                                        Toast.makeText(PasswordRecoveryActivity.this, "Došlo je do greške, provjerite ispravnost email-a!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(PasswordRecoveryActivity.this, "Unesite ispravnu email adresu!", Toast.LENGTH_LONG).show();
                }
            }
        });

        showLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity(LoginActivity.class);
            }
        });


    }

    private boolean validateEmail(EditText emailTxt){
        String email = emailTxt.getText().toString().trim();

        if(email == null){
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }else return true;
    }


/*
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonRecoveryPassword:
                firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(PasswordRecoveryActivity.this, "Link za promjenu lozinke je poslan. Provjerite email!", Toast.LENGTH_LONG).show();
                                    ShowActivity(LoginActivity.class);
                                }
                                else{
                                    Toast.makeText(PasswordRecoveryActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                break;
            case R.id.buttonShowLogin:
                ShowActivity(LoginActivity.class);
                break;
        }
    }*/
}
