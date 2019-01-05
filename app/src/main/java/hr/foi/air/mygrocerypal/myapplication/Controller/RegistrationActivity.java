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

import hr.foi.air.mygrocerypal.myapplication.Core.Cities;
import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.CitiesListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.RegistrationHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.RegistrationListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.ValidateInputs;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;
import hr.foi.air.mygrocerypal.myapplication.R;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class RegistrationActivity extends AppCompatActivity implements RegistrationListener, CitiesListener {
    ArrayList<String> mListOfCities;
    SpinnerDialog mSpinnerDialog;
    private static final String OBLIGATORY  = "Obavezno polje!";

    private TextView mDateOfBirth, mCities;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText mEmail, mPassword, mUsername, mFirstName, mLastName, mAddress, mContact, mRetypedPassword;
    private Button btnRegister, btnBackToLogin;

    private RegistrationHelper mRegistrationHelper;

    /**
     * Inicijalizacija
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle(R.string.register);

        mRegistrationHelper = new RegistrationHelper(this);

        mFirstName = findViewById(R.id.firstnameRegistration);
        mLastName = findViewById(R.id.lastnameRegistration);
        mUsername = findViewById(R.id.usernameRegistration);
        mPassword = findViewById(R.id.passwordRegistration);
        mRetypedPassword = findViewById(R.id.repeatPasswordRegistration);
        mEmail =  findViewById(R.id.emailRegistration);
        mAddress = findViewById(R.id.addressRegistration);
        mDateOfBirth = findViewById(R.id.dateOfBirthRegistration);
        mContact = findViewById(R.id.contactRegistration);

        mCities =  findViewById(R.id.txtCity);

        btnRegister =  findViewById(R.id.buttonRegister);
        btnBackToLogin =  findViewById(R.id.buttonBackToLogin);

        new Cities(this, this).execute();

        mDateOfBirth.setOnClickListener(new View.OnClickListener() {
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
                mDateOfBirth.setText(date);
                mContact.requestFocus();
            }
        };

        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(mEmail.getText().toString().trim().length() > 0){
                        boolean validationSuccess = ValidateInputs.validateEmail(mEmail.getText().toString().trim());
                        if(!validationSuccess) mEmail.setError("Molimo unesite ispravnu email adresu!");
                    }else {
                        mEmail.setError(OBLIGATORY);
                    }
                }
            }
        });

        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if (mPassword.getText().toString().trim().length() > 0) {
                        boolean validationSuccess = ValidateInputs.validatePassword(mPassword.getText().toString().trim());
                        if (!validationSuccess)
                            mPassword.setError("Minimalno 6 slova i jedan specijalni znak!");
                    } else {
                        mPassword.setError(OBLIGATORY);
                    }
                }
            }
        });

        mRetypedPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(mRetypedPassword.getText().toString().trim().length() > 0){
                        boolean validationSuccess = ValidateInputs.validateRetypedPassword(mPassword.getText().toString().trim(), mRetypedPassword.getText().toString().trim());
                        if(!validationSuccess) mRetypedPassword.setError("Lozinke ne odgovaraju!");
                    }else{
                        mRetypedPassword.setError(OBLIGATORY);
                    }
                }
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRegistrationHelper.registerUser(createUser(), mRetypedPassword.getText().toString().trim());
                }
        });

        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogin();
            }
        });
    }

    /**
     * Otvori Activity login
     */
    public void showLogin(){
        startActivity(new Intent(this,LoginActivity.class));
    }

    /**
     * Prikazi poruku korisniku
     * @param mMessage
     */
    public void showToastRegistration(String mMessage){
        Toast.makeText(this, mMessage, Toast.LENGTH_LONG).show();
    }

    /**
     * Kreiraj novog korisnika
     * @return
     */
    private UserModel createUser(){
        UserModel newUser = new UserModel();
        newUser.setFirst_name(mFirstName.getText().toString().trim());
        newUser.setLast_name(mLastName.getText().toString().trim());
        newUser.setUsername(mUsername.getText().toString().trim());
        newUser.setPassword(mPassword.getText().toString().trim());
        newUser.setEmail(mEmail.getText().toString().trim());
        newUser.setAddress(mAddress.getText().toString().trim());
        newUser.setTown(mCities.getText().toString().trim());
        newUser.setPhone_number(mContact.getText().toString().trim());
        newUser.setBirth_date(mDateOfBirth.getText().toString().trim());
        return newUser;
    }

    /**
     * U slucaju uspjesne registracije prikazi poruku
     * @param mMessage
     */
    @Override
    public void onRegistrationSuccess(String mMessage) {
        showLogin();
        showToastRegistration(mMessage);
    }

    /**
     * U slucaju neuspjesne registracije prikazi poruku
     * @param mMessage
     */
    @Override
    public void onRegistrationFail(String mMessage) {
        showToastRegistration(mMessage);
    }

    /**
     * Dodaj gradove u SpinnerDialog
     * @param listOfCities
     */
    @Override
    public void citiesLoaded(ArrayList<String> listOfCities) {
        if(listOfCities != null) {
            mListOfCities = listOfCities;
            mSpinnerDialog = new SpinnerDialog(RegistrationActivity.this, mListOfCities, "Odaberite grad", R.style.DialogAnimations_SmileWindow, "Zatvori");
            mSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String item, int position) {
                    Toast.makeText(RegistrationActivity.this, "Selected: " + item, Toast.LENGTH_LONG).show();
                    mCities.setText(item);
                }
            });

            mCities.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSpinnerDialog.showSpinerDialog();
                }
            });
        }
    }
}
