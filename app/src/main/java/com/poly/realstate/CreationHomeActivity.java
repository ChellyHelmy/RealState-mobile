package com.poly.realstate;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreationHomeActivity extends AppCompatActivity {

    EditText edtTitle, edtPrice, edtAddress, edtArea, edtDescription;
    Button btnSave, btnAddImages;
    ImageView imagePreview;

    // Hold selected images locally (no Firebase)
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

        // Image Picker Launcher
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

                        // Preview first selected image
                        if (!imageUriList.isEmpty()) {
                            imagePreview.setImageURI(imageUriList.get(0));
                        }
                    }
                }
        );

        // Button Pick Images
        btnAddImages.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            pickImageLauncher.launch(intent);
        });

        // Save Button
        btnSave.setOnClickListener(v -> saveLocally());
    }


    // Fake save (local only)
    private void saveLocally() {

        String title = edtTitle.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String area = edtArea.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (title.isEmpty() || price.isEmpty() || address.isEmpty() ||
                area.isEmpty() || description.isEmpty()) {

            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this,
                "Maison sauvegardée localement (fake) ✅\n"
                        + "Titre: " + title + "\n"
                        + "Images: " + imageUriList.size(),
                Toast.LENGTH_LONG).show();

        // Go back
        finish();
    }
}
