package com.poly.realstate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    Button btn_valider;
    TextInputEditText password_ed;
    FirebaseAuth mAuth;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // Firebase
        mAuth = FirebaseAuth.getInstance();

        password_ed = findViewById(R.id.password);
        btn_valider = findViewById(R.id.btn_valider);

        // Get email from SignInActivity
        userEmail = getIntent().getStringExtra("user_email");

        btn_valider.setOnClickListener(v -> {
            String newPassword = password_ed.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword) || newPassword.length() < 6) {
                password_ed.setError("Le mot de passe doit contenir au moins 6 caractères");
                password_ed.requestFocus();
                return;
            }

            // Update password in Firebase
            mAuth.fetchSignInMethodsForEmail(userEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().getSignInMethods() != null && !task.getResult().getSignInMethods().isEmpty()) {
                        mAuth.signInWithEmailAndPassword(userEmail, newPassword)
                                .addOnCompleteListener(signInTask -> {
                                    if (signInTask.isSuccessful()) {
                                        mAuth.getCurrentUser().updatePassword(newPassword)
                                                .addOnCompleteListener(updateTask -> {
                                                    if (updateTask.isSuccessful()) {
                                                        Toast.makeText(this, "Mot de passe mis à jour avec succès ✅", Toast.LENGTH_SHORT).show();
                                                        finish(); // return to SignInActivity
                                                    } else {
                                                        Toast.makeText(this, "Erreur: " + updateTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    } else {
                                        // If user is not signed in, send password reset email
                                        mAuth.sendPasswordResetEmail(userEmail)
                                                .addOnCompleteListener(resetTask -> {
                                                    if (resetTask.isSuccessful()) {
                                                        Toast.makeText(this, "Email de réinitialisation envoyé ✅", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(this, "Erreur: " + resetTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Email non trouvé dans la base", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Erreur: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
