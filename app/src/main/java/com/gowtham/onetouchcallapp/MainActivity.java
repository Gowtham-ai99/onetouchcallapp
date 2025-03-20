package com.gowtham.onetouchcallapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    private ContactViewModel contactViewModel;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<Contact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Grid with 2 columns
        recyclerView.setHasFixedSize(true);

        // Initialize Adapter BEFORE using it
        contactAdapter = new ContactAdapter(this, contactList, this::makeCall);
        recyclerView.setAdapter(contactAdapter);

        // Initialize ViewModel
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        // Observe LiveData and update UI
        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                if (contacts != null) {
                    contactList.clear(); // Clear old list
                    contactList.addAll(contacts); // Add new contacts
                    contactAdapter.notifyDataSetChanged(); // Notify adapter to refresh RecyclerView
                }
            }
        });

        // Floating Action Button for Adding Contacts
        FloatingActionButton addContactButton = findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            startActivity(intent);
        });

        // Set click listener for contacts
        contactAdapter.setOnItemClickListener(phoneNumber -> makeCall(phoneNumber));
    }

    // Function to Make a Call
    private void makeCall(String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            return;
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    // Handle Permission Request Result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    // Reload Contacts when Returning to Home Screen
    @Override
    protected void onResume() {
        super.onResume();
        contactViewModel.getAllContacts().observe(this, contacts -> {
            if (contacts != null) {
                contactList.clear();
                contactList.addAll(contacts);
                contactAdapter.notifyDataSetChanged();
            }
        });
    }
}
