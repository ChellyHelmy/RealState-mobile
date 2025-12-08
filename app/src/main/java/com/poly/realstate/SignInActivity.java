package com.poly.realstate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    Button btn_signIn;
    TextView creerCompte, mdp_oublie;
    EditText email_ed, password_ed;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        btn_signIn = findViewById(R.id.btn_confirm);
        creerCompte = findViewById(R.id.creer_compte_btn);
        mdp_oublie = findViewById(R.id.forget_pwd);
        email_ed = findViewById(R.id.email);
        password_ed = findViewById(R.id.password);

        //Redirect to Sign Up
        creerCompte.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

        //Login Button (REAL AUTH)
        btn_signIn.setOnClickListener(v -> loginUser());

        //Forgot password
        //Forgot password
        //Forgot password
        mdp_oublie.setOnClickListener(v -> {
            String email = email_ed.getText().toString().trim();

            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                email_ed.setError("Entrez un email valide pour réinitialiser le mot de passe");
                email_ed.requestFocus();
                return;
            }

            // Send password reset email via Firebase
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Email de réinitialisation envoyé ✅", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Erreur: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });


    }

    //FIREBASE LOGIN FUNCTION
    private void loginUser() {
        String email = email_ed.getText().toString().trim();
        String password = password_ed.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_ed.setError("Email invalide");
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            password_ed.setError("Mot de passe invalide");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Connexion réussie ✅", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(this, HomeActivity.class));
                        finish();

                    } else {
                        Toast.makeText(this,
                                "Erreur: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
