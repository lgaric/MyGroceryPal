package hr.foi.air.mygrocerypal.myapplication.Core;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import hr.foi.air.mygrocerypal.myapplication.View.LoginActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void ShowActivity(Class<? extends BaseActivity> activity){
        startActivity(new Intent(this, activity));
    }

    protected void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void ShowLogin(){
        ShowActivity(LoginActivity.class);
    }
}
