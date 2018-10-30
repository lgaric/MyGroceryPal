package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import hr.foi.air.mygrocerypal.myapplication.Core.BaseActivity;
import hr.foi.air.mygrocerypal.myapplication.R;
import hr.foi.air.mygrocerypal.myapplication.View.LoginFragment;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void Login(View view) {
        ShowActivity(DelivererMainActivity.class);
    }

    public void ShowRegister(View view) {
        ShowActivity(RegisterActivity.class);
    }

    public void ShowRecoveryPassword(View view) {
        ShowActivity(PasswordRecoveryActivity.class);
    }
}
