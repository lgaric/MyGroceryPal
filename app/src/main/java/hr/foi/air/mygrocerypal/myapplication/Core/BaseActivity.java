package hr.foi.air.mygrocerypal.myapplication.Core;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hr.foi.air.mygrocerypal.myapplication.Controller.LoginActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void ShowActivity(Class<? extends BaseActivity> activity){
        startActivity(new Intent(this, activity));
    }

    public void ShowLogin(){
        ShowActivity(LoginActivity.class);
    }
}
