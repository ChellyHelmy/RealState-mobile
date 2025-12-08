package com.poly.realstate;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreationHomeActivity extends AppCompatActivity {

    EditText edtTitle, edtPrice, edtAddress, edtArea, edtDescription;
    Button btnSave, btnAddImages;
    ImageView imagePreview;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;

    List<Uri> imageUriList = new ArrayList<>();

    ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creationhome);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarCreationhome);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Views
        edtTitle = findViewById(R.id.edtHomeTitle);
        edtPrice = findViewById(R.id.edtHomePrice);
        edtAddress = findViewById(R.id.edtHomeAddress);
        edtArea = findViewById(R.id.edtHomeArea);
        edtDescription = findViewById(R.id.edtHomeDescription);
        btnSave = findViewById(R.id.btnHomeSave);
        btnAddImages = findViewById(R.id.btnAddImages);
        imagePreview = findViewById(R.id.imagePreview);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Image Picker (Multiple)
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                        imageUriList.clear();

                        if (result.getData().getClipData() != null) {
                            int count = result.getData().getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                imageUriList.add(result.getData().getClipData().getItemAt(i).getUri());
                            }
                        } else {
                            imageUriList.add(result.getData().getData());
                        }

                        imagePreview.setImageURI(imageUriList.get(0));
                    }
                }
        );

        btnAddImages.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            pickImageLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> saveHouse());
    }

    private void saveHouse() {

        String title = edtTitle.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String area = edtArea.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (title.isEmpty() || price.isEmpty() || address.isEmpty() || area.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(userDoc -> {

                    String userName = userDoc.getString("fullName");
                    String userEmail = userDoc.getString("email");
                    String userPhone = userDoc.getString("phone");

                    Map<String, Object> houseMap = new HashMap<>();
                    houseMap.put("title", title);
                    houseMap.put("price", price);
                    houseMap.put("address", address);
                    houseMap.put("area", area);
                    houseMap.put("description", description);
                    houseMap.put("userName", userName);
                    houseMap.put("userEmail", userEmail);
                    houseMap.put("userPhone", userPhone);

                    if (imageUriList.isEmpty()) {
                        houseMap.put("images", new ArrayList<>());
                        saveHouseToFirestore(houseMap);
                    } else {
                        uploadImagesAndSave(houseMap);
                    }

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erreur utilisateur", Toast.LENGTH_SHORT).show()
                );
    }

    private void uploadImagesAndSave(Map<String, Object> houseMap) {

        List<String> imageUrls = new ArrayList<>();
        final int total = imageUriList.size();
        final int[] uploaded = {0};

        for (Uri uri : imageUriList) {

            String fileName = "house_" + System.currentTimeMillis() + ".jpg";
            StorageReference ref = storage.getReference()
                    .child("houses_images")
                    .child(fileName);

            ref.putFile(uri)
                    .addOnSuccessListener(taskSnapshot ->
                            ref.getDownloadUrl().addOnSuccessListener(downloadUri -> {

                                imageUrls.add(downloadUri.toString());
                                uploaded[0]++;

                                if (uploaded[0] == total) {
                                    houseMap.put("images", imageUrls);
                                    saveHouseToFirestore(houseMap);
                                }

                            })
                    )
                    .addOnFailureListener(e ->
                            Log.e("UPLOAD_ERROR", e.getMessage())
                    );
        }
    }

    private void saveHouseToFirestore(Map<String, Object> houseMap) {

        db.collection("houses")
                .add(houseMap)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Maison enregistrée ✅", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erreur Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
