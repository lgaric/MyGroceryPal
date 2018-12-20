package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.PasswordRecoveryController;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.PasswordRecoveryListener;
import hr.foi.air.mygrocerypal.myapplication.R;

public class PasswordRecoveryActivity extends AppCompatActivity implements PasswordRecoveryListener {
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
                showLogin();
            }
        });

    }

    public void showLogin(){
        startActivity(new Intent(this,LoginActivity.class));
    }

    @Override
    public void onRecoverySuccess(String message) {
        Toast.makeText(PasswordRecoveryActivity.this, message, Toast.LENGTH_LONG).show();
        showLogin();
    }

    @Override
    public void onRecoveryFail(String message) {
        Toast.makeText(PasswordRecoveryActivity.this, message, Toast.LENGTH_LONG).show();

    }
}
