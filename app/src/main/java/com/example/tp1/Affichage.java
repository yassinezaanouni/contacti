package com.example.tp1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Affichage extends AppCompatActivity {
    ArrayList<Contact> contactList, filteredList = new ArrayList<>();
    RecyclerView rv;
    EditText searchContact;
    FloatingActionButton addContact;
    MyContactRecyclerAdapter adapter; // Define the adapter here

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        rv = findViewById(R.id.con_rv);
        searchContact = findViewById(R.id.search_contact);
        addContact = findViewById(R.id.add_contact_button);
        ContactManager manager = new ContactManager(Affichage.this);
        manager.ouvrir();
//        manager.ajouter("oussama","ouss","22863831");
//        manager.ajouter("iskander","skon","24093831");
//        manager.ajouter("laterech","tor","99063831");
//        manager.ajouter("alibou","bou","55863831");
        contactList = manager.getAllContacts();
        filteredList.addAll(contactList); // Initially, filteredList contains all contacts

        EdgeToEdge.enable(this);

        // Initialize the adapter with the filtered list
        adapter = new MyContactRecyclerAdapter(Affichage.this, filteredList, manager);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Affichage.this, 1, RecyclerView.VERTICAL, false);
        rv.setAdapter(adapter);
        rv.setLayoutManager(gridLayoutManager);
        // Add a contact
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddContactDialog(manager);
            }
        });

        // Add a TextWatcher to the searchContact EditText
        searchContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterContacts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterContacts(s.toString());
            }
        });
    }

    private void showAddContactDialog(ContactManager manager) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Affichage.this);
        LayoutInflater inflater = LayoutInflater.from(Affichage.this);
        View dialogView = inflater.inflate(R.layout.add_contact_dialog, null);
        dialogBuilder.setView(dialogView);

        // Retrieve the EditTexts and Buttons from the dialog layout
        EditText editName = dialogView.findViewById(R.id.dialog_add_nom);
        EditText editPseudo = dialogView.findViewById(R.id.dialog_add_pseudo);
        EditText editPhone = dialogView.findViewById(R.id.dialog_add_phone);
        Button saveButton = dialogView.findViewById(R.id.dialog_add_save);
        Button cancelButton = dialogView.findViewById(R.id.dialog_add_cancel);

        AlertDialog alertDialog = dialogBuilder.create();

        // Set onClickListener for the Save button to save the new contact
        saveButton.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String pseudo = editPseudo.getText().toString();
            String phone = editPhone.getText().toString();

            if (!name.isEmpty() && !pseudo.isEmpty() && !phone.isEmpty()) {
                // Add the new contact to the manager and refresh the RecyclerView
                manager.ajouter(name, pseudo, phone);
                contactList.clear();
                contactList.addAll(manager.getAllContacts());
                filteredList.clear();
                filteredList.addAll(contactList);
                adapter.notifyDataSetChanged();
                alertDialog.dismiss(); // Close the dialog after adding the contact
            } else {
                Toast.makeText(Affichage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Set onClickListener for the Cancel button
        cancelButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show(); // Display the dialog
    }


    // Method to filter the contacts based on the search query
    private void filterContacts(String query) {
        filteredList.clear(); // Clear the previous filtered list

        if (query.isEmpty()) {
            // If query is empty, add all contacts back to the filtered list
            filteredList.addAll(contactList);
        } else {
            // Convert query to lowercase for case-insensitive search
            String lowerCaseQuery = query.toLowerCase();

            for (Contact contact : contactList) {
                // Check if the query matches the name, pseudo, or number (case-insensitive)
                if (contact.nom.toLowerCase().contains(lowerCaseQuery) ||
                        contact.pseudo.toLowerCase().contains(lowerCaseQuery) ||
                        contact.numero.toLowerCase().contains(lowerCaseQuery)) {

                    filteredList.add(contact); // Add matching contacts to the filtered list
                }
            }
        }

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }
}