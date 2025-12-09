package com.poly.realstate;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    Button creerCompteBtn;
    ImageView profileImage;
    TextInputEditText fullNameEd, emailEd, passwordEd, phoneEd;

    Uri profileImageUri = null;

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

        // Image Picker
        ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        profileImageUri = result.getData().getData();
                        profileImage.setImageURI(profileImageUri);
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

        // Validation
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

        // Optional: handle profile image as string if needed
        String profileImageString = null;
        if(profileImageUri != null) {
            profileImageString = profileImageUri.toString(); // save Uri as String
        }

        // Local user object (you can later save to DB or API)
        User newUser = new User(fullName, email, password, phone, profileImageString);

        Toast.makeText(this, "Compte créé avec succès ✅", Toast.LENGTH_SHORT).show();

        // Redirect to HomeActivity
        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
        finish();
    }

    // Simple user class for demonstration
    public static class User {
        public String fullName, email, password, phone, profileImage;

        public User(String fullName, String email, String password, String phone, String profileImage) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.profileImage = profileImage;
        }
    }
}
