package com.gowtham.onetouchcallapp;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactRepository {
    private final ContactDao contactDao;
    private final LiveData<List<Contact>> allContacts;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Constructor to initialize DAO and LiveData
    public ContactRepository(Application application) {
        ContactDatabase database = ContactDatabase.getInstance(application);
        contactDao = database.contactDao();
        allContacts = contactDao.getAllContacts();
    }

    // Insert a new contact
    public void insert(Contact contact) {
        executorService.execute(() -> contactDao.insert(contact));
    }

    // Update an existing contact
    public void update(Contact contact) {
        executorService.execute(() -> contactDao.update(contact));
    }

    // Delete a contact
    public void delete(Contact contact) {
        executorService.execute(() -> contactDao.delete(contact));
    }

    // Get all contacts as LiveData
    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }
}
