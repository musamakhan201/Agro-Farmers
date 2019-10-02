package example.com.agrofarmers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    static final int GOOGLE_SIGN = 123;
    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    Button btnLoginGoogle;
    CheckBox cbShowPass;
    TextView tvForgotPassword;
    TextView tvCreateUser;
    ProgressBar progressBar;
    FirebaseAuth fbAuth;
    GoogleSignInClient gsClient;
    NavigationView navigationView;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        cbShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsClient = GoogleSignIn.getClient(this, googleSignInOptions);
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInGoogle();
            }
        });
        if (fbAuth.getCurrentUser() != null) {
            FirebaseUser user = fbAuth.getCurrentUser();
            updateUI(user);
        }
        tvCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEditText();
            }
        });
    }

    private void checkEditText() {
        if (etEmail.getText().toString().isEmpty())
        {
            etEmail.setError("Enter Email");
            etEmail.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches())
        {
            etEmail.setError("Enter Valid Email");
            etEmail.requestFocus();
            return;
        }
        else if (etPassword.getText().toString().length()<6)
        {
            etPassword.setError("Enter Password of length 6");
            etPassword.requestFocus();
            return;
        }else
        {
            signIn();
        }
    }

    private void signIn() {
        String email=etEmail.getText().toString();
        String password=etPassword.getText().toString();
        fbAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = fbAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void findViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_signIn);
        btnLoginGoogle = findViewById(R.id.btn_signInGoogle);
        cbShowPass = findViewById(R.id.show_pass);
        tvForgotPassword = findViewById(R.id.forgot_password);
        tvCreateUser = findViewById(R.id.new_user);
        progressBar = findViewById(R.id.progress_bar);
        fbAuth = FirebaseAuth.getInstance();
    }

    void SignInGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = gsClient.getSignInIntent();
        startActivityForResult(intent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "FireBaseAuthWithGoogle " + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d("TAG", "Successfully SignedIn.");
                            FirebaseUser user = fbAuth.getCurrentUser();
                            LoginActivity.this.updateUI(user);
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                            Log.d("TAG", "Login Failed.", task.getException());
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                            LoginActivity.this.updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user!=null)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}