package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.os.Bundle;
import android.view.View;

import hr.foi.air.mygrocerypal.myapplication.Core.BaseActivity;
import hr.foi.air.mygrocerypal.myapplication.R;
import hr.foi.air.mygrocerypal.myapplication.View.LoginActivity;

public class PasswordRecoveryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
    }


    public void ShowLoginPROMIJENITI(View view) {
        ShowActivity(LoginActivity.class);
    }
}
