package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import hr.foi.air.mygrocerypal.myapplication.Core.BaseActivity;
import hr.foi.air.mygrocerypal.myapplication.R;
import hr.foi.air.mygrocerypal.myapplication.View.LoginFragment;

public class LoginActivity extends BaseActivity {

    private EditText username, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameLogin);
        password = (EditText) findViewById(R.id.passwordLogin);

    }

    public void Login(View view){

        String username = this.username.getText().toString().trim();
        String password = this.password.getText().toString().trim();

        if(username.isEmpty() || password.isEmpty()) {
            showToast("Ispunite odgovarajuća polja!");
            return;
        }

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            if(mAuth.getCurrentUser().isEmailVerified()) {
                                ShowActivity(DelivererMainActivity.class);
                            }
                            else {
                                showToast("Email nije verificiran");
                            }
                        }
                        else{

                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch(errorCode) {
                                case "ERROR_INVALID_EMAIL":
                                    showToast("Neispravan Email");
                                    break;
                                case "ERROR_WRONG_PASSWORD":
                                    showToast("Neispravna lozinka");
                                    break;
                                case "ERROR_USER_NOT_FOUND":
                                    showToast("Nepostojeći korisnik");
                                    break;
                                default:
                                    showToast("Greška");
                                    break;
                            }
                        }
                    }
                });
    }

    public void ShowRegister(View view) {
        ShowActivity(RegisterActivity.class);
    }

    public void ShowRecoveryPassword(View view) {
        ShowActivity(PasswordRecoveryActivity.class);
    }
}
