package example.com.agrofarmers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    EditText etEmail,etPassword,etRepass;
    Button btnRegister;
    TextView tvUser;
    private FirebaseAuth mAuth;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        bind();
        registerUser();
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
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
        }
        else if (etRepass.getText().toString().length()<6)
        {
            etRepass.setError("Re-Type Password");
            etRepass.requestFocus();
            return;
        }
        else if (!etRepass.getText().toString().equals(etPassword.getText().toString()))
        {
            etRepass.setError("Password Must Be Same");
            return;
        }
        else {
            register();
        }
    }

    private void register() {
        bind();
        String email=etEmail.getText().toString();
        String password=etPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User Registered Successfully");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w("TAG", "Failed", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void bind() {
        etEmail=findViewById(R.id.et_email);
        etPassword=findViewById(R.id.et_pass);
        etRepass=findViewById(R.id.et_repass);
        btnRegister=findViewById(R.id.btn_register);
        tvUser=findViewById(R.id.already_user);
        mAuth = FirebaseAuth.getInstance();

    }
}
