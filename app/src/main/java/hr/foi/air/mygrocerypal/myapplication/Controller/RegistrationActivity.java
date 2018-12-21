package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.RegistrationHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.RegistrationListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.ValidateInputs;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;
import hr.foi.air.mygrocerypal.myapplication.R;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class RegistrationActivity extends AppCompatActivity implements RegistrationListener {

    ArrayList<String> cities = new ArrayList<>();
    SpinnerDialog spinnerDialog;
    private static final String OBLIGATORY  = "Obavezno polje!";

    public void initCities(){
        String json = null;
        try (InputStream is = getAssets().open("CroatianCities.json")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            while(is.read(buffer) > 0){
                json = new String(buffer, "UTF-8");
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i<jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    cities.add(obj.getString("mjesto"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(getClass().toString(), e.getMessage());
        }
        catch (JSONException e){
            e.printStackTrace();
            Log.e(getClass().toString(), e.getMessage());
        }
    }



    private TextView dateOfBirthTxt, txtCities;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText emailTxt, passwordTxt, userNameTxt, firstNameTxt, lastNameTxt, adressTxt, contactTxt, retypedPasswordTxt;
    private Button registerBtn, backToLoginBtn;

    private RegistrationHelper controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle(R.string.register);

        controller = new RegistrationHelper(this);

        firstNameTxt = findViewById(R.id.firstnameRegistration);
        lastNameTxt = findViewById(R.id.lastnameRegistration);
        userNameTxt = findViewById(R.id.usernameRegistration);
        passwordTxt = findViewById(R.id.passwordRegistration);
        retypedPasswordTxt = findViewById(R.id.repeatPasswordRegistration);
        emailTxt =  findViewById(R.id.emailRegistration);
        adressTxt = findViewById(R.id.addressRegistration);
        dateOfBirthTxt = findViewById(R.id.dateOfBirthRegistration);
        contactTxt = findViewById(R.id.contactRegistration);

        txtCities =  findViewById(R.id.txtCity);

        registerBtn =  findViewById(R.id.buttonRegister);
        backToLoginBtn =  findViewById(R.id.buttonBackToLogin);

        initCities();
        spinnerDialog = new SpinnerDialog(RegistrationActivity.this, cities, "Odaberite grad", R.style.DialogAnimations_SmileWindow , "Zatvori");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                Toast.makeText(RegistrationActivity.this, "Selected: " + item, Toast.LENGTH_LONG).show();
                txtCities.setText(item);
            }
        });

        txtCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });

        dateOfBirthTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegistrationActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = (month+1) + "/" + dayOfMonth + "/" + year;
                dateOfBirthTxt.setText(date);
                contactTxt.requestFocus();
            }
        };

        emailTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(emailTxt.getText().toString().trim().length() > 0){
                        boolean validationSuccess = ValidateInputs.validateEmail(emailTxt.getText().toString().trim());
                        if(!validationSuccess) emailTxt.setError("Molimo unesite ispravnu email adresu!");
                    }else {
                        emailTxt.setError(OBLIGATORY);
                    }
                }
            }
        });

        passwordTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if (passwordTxt.getText().toString().trim().length() > 0) {
                        boolean validationSuccess = ValidateInputs.validatePassword(passwordTxt.getText().toString().trim());
                        if (!validationSuccess)
                            passwordTxt.setError("Minimalno 6 slova i jedan specijalni znak!");
                    } else {
                        passwordTxt.setError(OBLIGATORY);
                    }
                }
            }
        });

        retypedPasswordTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(retypedPasswordTxt.getText().toString().trim().length() > 0){
                        boolean validationSuccess = ValidateInputs.validateRetypedPassword(passwordTxt.getText().toString().trim(), retypedPasswordTxt.getText().toString().trim());
                        if(!validationSuccess) retypedPasswordTxt.setError("Lozinke ne odgovaraju!");
                    }else{
                        retypedPasswordTxt.setError(OBLIGATORY);
                    }
                }
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                controller.registerUser(createUser(), retypedPasswordTxt.getText().toString().trim());
                }
        });

        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
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
    public void onRegistrationSuccess(String message) {
        showLogin();
        showToastRegistration(message);
    }

    @Override
    public void onRegistrationFail(String message) {
        showToastRegistration(message);
    }

    public void showToastRegistration(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private UserModel createUser(){
        UserModel newUser = new UserModel();
        newUser.setFirst_name(firstNameTxt.getText().toString().trim());
        newUser.setLast_name(lastNameTxt.getText().toString().trim());
        newUser.setUsername(userNameTxt.getText().toString().trim());
        newUser.setPassword(passwordTxt.getText().toString().trim());
        newUser.setEmail(emailTxt.getText().toString().trim());
        newUser.setAddress(adressTxt.getText().toString().trim());
        newUser.setTown(txtCities.getText().toString().trim());
        newUser.setPhone_number(contactTxt.getText().toString().trim());
        newUser.setBirth_date(dateOfBirthTxt.getText().toString().trim());
        return newUser;
    }
}
