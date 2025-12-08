package com.poly.realstate;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    Button creerCompteBtn;
    ImageView profileImage;
    TextInputEditText fullNameEd, emailEd, passwordEd, phoneEd;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        profileImage = findViewById(R.id.profileImage);
        creerCompteBtn = findViewById(R.id.btn_creerCompte);

        fullNameEd = findViewById(R.id.nom_prénom);
        emailEd = findViewById(R.id.email);
        passwordEd = findViewById(R.id.password);
        phoneEd = findViewById(R.id.tel);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Optional: select profile image
        ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        profileImage.setImageURI(imageUri);
                    }
                }
        );

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            pickImageLauncher.launch(intent);
        });

        creerCompteBtn.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        String fullName = fullNameEd.getText().toString().trim();
        String email = emailEd.getText().toString().trim();
        String password = passwordEd.getText().toString().trim();
        String phone = phoneEd.getText().toString().trim();

        if(TextUtils.isEmpty(fullName)) {
            fullNameEd.setError("Veuillez entrer votre nom complet");
            return;
        }
        if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEd.setError("Email invalide");
            return;
        }
        if(TextUtils.isEmpty(password) || password.length() < 6) {
            passwordEd.setError("Mot de passe invalide (6 caractères min)");
            return;
        }
        if(TextUtils.isEmpty(phone) || phone.length() != 8) {
            phoneEd.setError("Numéro de téléphone invalide");
            return;
        }

        // Create user in Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        // Save extra info in Firestore
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("fullName", fullName);
                        userMap.put("email", email);
                        userMap.put("phone", phone);

                        db.collection("users")
                                .document(mAuth.getCurrentUser().getUid())
                                .set(userMap)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Compte créé avec succès ✅", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> Log.e("Erreur sign up", e.getMessage(), task.getException())); //Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this, "Erreur: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Erreur sign up", "Firebase Auth: Failed to create user", task.getException());
                    }
                });
    }
}
