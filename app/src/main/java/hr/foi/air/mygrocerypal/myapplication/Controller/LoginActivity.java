package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.LoginHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.LoginListener;
import hr.foi.air.mygrocerypal.myapplication.R;

public class LoginActivity extends AppCompatActivity implements LoginListener {
    private EditText username, password;
    private LoginHelper loginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);

        loginHelper = new LoginHelper(this);
    }

    public void login(View view){
        loginHelper.login(username.getText().toString(), password.getText().toString());
    }

    public void showRegister(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    public void showRecoveryPassword(View view) {
        startActivity(new Intent(this,PasswordRecoveryActivity.class));
    }

    @Override
    public void onStatusFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
