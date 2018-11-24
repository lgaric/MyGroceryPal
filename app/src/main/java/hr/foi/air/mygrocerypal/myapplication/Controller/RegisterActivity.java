package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Pattern;

import hr.foi.air.mygrocerypal.myapplication.Core.BaseActivity;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;
import hr.foi.air.mygrocerypal.myapplication.R;
import hr.foi.air.mygrocerypal.myapplication.View.LoginActivity;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$");

    private TextView dateOfBirthTxt;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText emailTxt, passwordTxt, userNameTxt, firstNameTxt, lastNameTxt, adressTxt, townTxt, contactTxt, retypedPasswordTxt;
    private Button registerBtn, backToLoginBtn;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameTxt = (EditText) findViewById(R.id.firstnameRegistration);
        lastNameTxt = (EditText) findViewById(R.id.lastnameRegistration);
        userNameTxt = (EditText) findViewById(R.id.usernameRegistration);
        passwordTxt = (EditText) findViewById(R.id.passwordRegistration);
        retypedPasswordTxt = (EditText) findViewById(R.id.repeatPasswordRegistration);
        emailTxt = (EditText) findViewById(R.id.emailRegistration);
        adressTxt = (EditText) findViewById(R.id.addressRegistration);
        townTxt = (EditText) findViewById(R.id.townRegistration);
        dateOfBirthTxt = (TextView) findViewById(R.id.dateOfBirthRegistration);
        contactTxt = (EditText) findViewById(R.id.contactRegistration);

        registerBtn = (Button) findViewById(R.id.buttonRegister);
        registerBtn.setOnClickListener(this);
        backToLoginBtn = (Button) findViewById(R.id.buttonBackToLogin);
        backToLoginBtn.setOnClickListener(this);

        dateOfBirthTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
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

        emailTxt.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEmail();
            }
        });

        passwordTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePassword();
            }
        });

        retypedPasswordTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateRetypedPassword();
            }
        });
    }

    private void registerUser(){
        final String firstName = firstNameTxt.getText().toString().trim();
        final String lastName = lastNameTxt.getText().toString().trim();
        final String username = userNameTxt.getText().toString().trim();
        final String pass = passwordTxt.getText().toString().trim();
        final String email = emailTxt.getText().toString().trim();
        final String adress = adressTxt.getText().toString().trim();
        final String town = townTxt.getText().toString().trim();
        final String contact = contactTxt.getText().toString().trim();
        final String dateOfBirth = dateOfBirthTxt.getText().toString().trim();

        if(mAuth == null)
            mAuth = FirebaseAuth.getInstance();

        if(mDatabase == null)
            mDatabase = FirebaseDatabase.getInstance();


        Query query = mDatabase.getReference().child("users").orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //username exists
                if(dataSnapshot.getChildrenCount() > 0) {
                    showToastRegistration("Korisničko ime je već u upotrebi!");
                }
                else{
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final String uId = mAuth.getCurrentUser().getUid();
                                final UserModel newUser = new UserModel(firstName, lastName, username, email, pass, town, adress, contact, dateOfBirth);
                                mDatabase.getReference().child("users").child(uId).setValue(newUser);
                                mAuth.getCurrentUser().sendEmailVerification();
                                mAuth.signOut();
                                showToastRegistration("Registracija uspješna. Molimo potvrdite email!");
                            }
                            else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                showToastRegistration("Već postoji račun s navedenom email adresom!");
                            }
                            else{
                                showToastRegistration("Greška prilikom registracije!");
                            }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean validateEmail(){
        String email = emailTxt.getText().toString().trim();

        if(email == null){
            emailTxt.setError("Obavezno polje!");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTxt.setError("Unesite ispravnu email adresu!");
            return false;
        }else return true;
    }

    private boolean validatePassword(){
        String password = passwordTxt.getText().toString().trim();

        if(password == null){
            passwordTxt.setError("Obavezno polje!");
            return false;
        }else if (!PASSWORD_PATTERN.matcher(password).matches()){
            passwordTxt.setError("Minimalno 6 znakova i jedno specijalno slovo!");
            return false;
        }else return true;
    }

    private boolean validateRetypedPassword(){
        String firstPassword = passwordTxt.getText().toString().trim();
        String secondPassword = retypedPasswordTxt.getText().toString().trim();
        if(!firstPassword.equals(secondPassword)){
            retypedPasswordTxt.setError("Lozinke ne odgovaraju!");
            return false;
        }else return true;
    }

    private boolean validateInput(){
        if(firstNameTxt.getText().toString().trim().length() > 0 &&
                lastNameTxt.getText().toString().trim().length() > 0 &&
                userNameTxt.getText().toString().trim().length() > 0 &&
                townTxt.getText().toString().trim().length() > 0 &&
                adressTxt.getText().toString().trim().length() > 0 &&
                contactTxt.getText().toString().trim().length() > 0 &&
                dateOfBirthTxt.getText().toString().trim().length() > 0){
            return true;
        } else return false;
    }

    private void showToastRegistration(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonRegister:
                if(validateEmail() && validatePassword() && validateRetypedPassword() && validateInput()){
                    registerUser();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
                else if (!validateInput()){
                    showToastRegistration("Sva polja su obavezna!");
                }else{
                    showToastRegistration("Molimo ispravno popunite sva polja!");
                }
                break;
            case R.id.buttonBackToLogin:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
        }
    }
}
