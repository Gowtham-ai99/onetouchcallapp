package com.gowtham.onetouchcallapp;// Ensure this matches your package name

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class AddContactActivity extends AppCompatActivity {  // Extend AppCompatActivity
    private ImageView profileImage;
    private EditText nameField, phoneField;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);  // Ensure this XML file exists

        // Initialize UI elements
        ImageView backArrow = findViewById(R.id.back_arrow);
        ImageView cameraIcon = findViewById(R.id.camera_icon);
        profileImage = findViewById(R.id.profile_picture);
        nameField = findViewById(R.id.nameField);
        phoneField = findViewById(R.id.phoneField);
        Button submitButton = findViewById(R.id.submitButton);
        // Back button functionality
        backArrow.setOnClickListener(v -> finish());

        // Open Gallery for Profile Picture Selection
        cameraIcon.setOnClickListener(v -> openGallery());

        // Add Contact Button Click
        submitButton.setOnClickListener(v -> saveContact());
    }

    private static final int PICK_IMAGE_REQUEST = 1;

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }

    private void saveContact() {
        String name = nameField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please enter name and phone number!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Contact Saved: " + name, Toast.LENGTH_SHORT).show();
            finish();  // Go back to previous screen
        }
        Contact newContact = new Contact(name, phone, "");

        // Insert into database
        ContactDatabase.getInstance(this).contactDao().insert(newContact);

        Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show();

        // Return to MainActivity with result
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish(); // Close the activity
    }
    }




