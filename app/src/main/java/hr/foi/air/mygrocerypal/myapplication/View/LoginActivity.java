package hr.foi.air.mygrocerypal.myapplication.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hr.foi.air.mygrocerypal.myapplication.Controller.LoginController;
import hr.foi.air.mygrocerypal.myapplication.Controller.LoginListener;
import hr.foi.air.mygrocerypal.myapplication.Core.BaseActivity;
import hr.foi.air.mygrocerypal.myapplication.R;

public class LoginActivity extends BaseActivity implements LoginListener {
    private EditText username, password;
    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameLogin);
        password = (EditText) findViewById(R.id.passwordLogin);

        loginController = new LoginController(this);

        // TODO Ukloniti kompoziciju prema Controlleru!
    }

    public void Login(View view){
        loginController.Login(username.getText().toString(), password.getText().toString());
    }

    public void ShowRegister(View view) {
        ShowActivity(RegisterActivity.class);
    }

        public void ShowRecoveryPassword(View view) {
        ShowActivity(PasswordRecoveryActivity.class);
    }

    @Override
    public void onStatusFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusSuccess() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
