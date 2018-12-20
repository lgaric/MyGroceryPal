package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.PasswordRecoveryController;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.PasswordRecoveryListener;
import hr.foi.air.mygrocerypal.myapplication.Core.BaseActivity;
import hr.foi.air.mygrocerypal.myapplication.R;

public class PasswordRecoveryActivity extends BaseActivity implements PasswordRecoveryListener {
    private EditText userEmail;
    private Button buttonRecoveryPassword, showLogin;
    FirebaseAuth firebaseAuth;
    private PasswordRecoveryController passwordRecoveryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        getSupportActionBar().setTitle(R.string.forgotPassword);

        passwordRecoveryController = new PasswordRecoveryController(this);

        buttonRecoveryPassword = findViewById(R.id.buttonRecoveryPassword);
        showLogin = findViewById(R.id.buttonShowLogin);

        userEmail =(EditText) findViewById(R.id.emailRecoveryPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        buttonRecoveryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
