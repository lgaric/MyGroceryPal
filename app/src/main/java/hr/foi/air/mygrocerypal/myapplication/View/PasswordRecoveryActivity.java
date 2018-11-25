package hr.foi.air.mygrocerypal.myapplication.View;

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

import hr.foi.air.mygrocerypal.myapplication.Controller.PasswordRecoveryController;
import hr.foi.air.mygrocerypal.myapplication.Controller.PasswordRecoveryListener;
import hr.foi.air.mygrocerypal.myapplication.Core.BaseActivity;
import hr.foi.air.mygrocerypal.myapplication.R;
import hr.foi.air.mygrocerypal.myapplication.View.LoginActivity;

public class PasswordRecoveryActivity extends BaseActivity implements PasswordRecoveryListener {
    private ProgressBar progressBar;
    private EditText userEmail;
    private Button buttonRecoveryPassword, showLogin;
    FirebaseAuth firebaseAuth;
    private PasswordRecoveryController passwordRecoveryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        passwordRecoveryController = new PasswordRecoveryController(this);

        buttonRecoveryPassword = findViewById(R.id.buttonRecoveryPassword);
        showLogin = findViewById(R.id.buttonShowLogin);
        progressBar = findViewById(R.id.progressBar);

        userEmail =(EditText) findViewById(R.id.emailRecoveryPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        buttonRecoveryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                passwordRecoveryController.sendRecoveryMail(userEmail.getText().toString());
            }
        });

        showLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity(LoginActivity.class);
            }
        });

    }


    @Override
    public void onRecoverySuccess(String message) {
        Toast.makeText(PasswordRecoveryActivity.this, message, Toast.LENGTH_LONG).show();
        ShowActivity(LoginActivity.class);
    }

    @Override
    public void onRecoveryFail(String message) {
        Toast.makeText(PasswordRecoveryActivity.this, message, Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);

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
                                    Toast.makeText(PasswordRecoveryController.this, "Link za promjenu lozinke je poslan. Provjerite email!", Toast.LENGTH_LONG).show();
                                    ShowActivity(LoginActivity.class);
                                }
                                else{
                                    Toast.makeText(PasswordRecoveryController.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
