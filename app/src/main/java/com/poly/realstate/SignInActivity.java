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

public class SignInActivity extends AppCompatActivity {

    Button btn_signIn;
    TextView creerCompte, mdp_oublie;
    EditText email_ed, password_ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_signIn = findViewById(R.id.btn_confirm);
        creerCompte = findViewById(R.id.creer_compte_btn);
        mdp_oublie = findViewById(R.id.forget_pwd);
        email_ed = findViewById(R.id.email);
        password_ed = findViewById(R.id.password);

        // 👉 Redirect to SignUp
        creerCompte.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

        // 👉 Sign In
        btn_signIn.setOnClickListener(v -> loginUser());

        // 👉 Forgot password
        mdp_oublie.setOnClickListener(v -> {
            Toast.makeText(this,
                    "Fonction de récupération du mot de passe désactivée",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    // 👉 LOCAL LOGIN FUNCTION (NO FIREBASE)
    private void loginUser() {
        String email = email_ed.getText().toString().trim();
        String password = password_ed.getText().toString().trim();

        // Validate email
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_ed.setError("Email invalide");
            return;
        }

        // Validate password
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            password_ed.setError("Mot de passe invalide");
            return;
        }

        // 👉 Here you can add API call later
        Toast.makeText(this, "Connexion réussie ✅", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
