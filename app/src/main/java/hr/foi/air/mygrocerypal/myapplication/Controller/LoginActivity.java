package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.LoginHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.LoginListener;
import hr.foi.air.mygrocerypal.myapplication.R;

public class LoginActivity extends AppCompatActivity implements LoginListener {
    private EditText mUsername, mPassword;
    private LoginHelper mLoginHelper;
    private LinearLayout mProgressLayout;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = findViewById(R.id.usernameLogin);
        mPassword = findViewById(R.id.passwordLogin);
        mProgressLayout = findViewById(R.id.linlaHeaderProgress);
        mProgressBar = findViewById(R.id.pbHeaderProgress);

        mProgressBar.getIndeterminateDrawable().
                setColorFilter(getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);

        mLoginHelper = new LoginHelper(this);
    }

    public void login(View view){
        mLoginHelper.login(mUsername.getText().toString(), mPassword.getText().toString(), mProgressLayout);
    }

    public void showRegister(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    public void showRecoveryPassword(View view) {
        startActivity(new Intent(this,PasswordRecoveryActivity.class));
    }

    @Override
    public void onStatusFailed(String mMessage) {
        Toast.makeText(this, mMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
