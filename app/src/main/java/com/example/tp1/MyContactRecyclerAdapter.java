package com.example.tp1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class MyContactRecyclerAdapter extends RecyclerView.Adapter<MyContactRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Contact> data;
    private ContactManager manager;

    public MyContactRecyclerAdapter(Context context, ArrayList<Contact> data, ContactManager manager) {
        this.context = context;
        this.data = data;
        this.manager = manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = data.get(position);
        
        // Set contact information
        holder.nameTextView.setText(contact.getNom());
        holder.usernameTextView.setText(contact.getPseudo());
        holder.phoneTextView.setText(contact.getNumero());

        // Call button click listener
        holder.callButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact.getNumero()));
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Unable to make call", Toast.LENGTH_SHORT).show();
            }
        });

        // Edit button click listener
        holder.editButton.setOnClickListener(v -> {
            showEditDialog(contact, position);
        });

        // Delete button click listener
        holder.deleteButton.setOnClickListener(v -> {
            showDeleteConfirmationDialog(contact, position);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView avatarImage;
        TextView nameTextView, usernameTextView, phoneTextView;
        MaterialButton callButton, editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            // Initialize views
            avatarImage = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.tv_nom_contact);
            usernameTextView = itemView.findViewById(R.id.tv_pseudo_contact);
            phoneTextView = itemView.findViewById(R.id.tv_num_contact);
            callButton = itemView.findViewById(R.id.imgview_call_contact);
            editButton = itemView.findViewById(R.id.imgview_edit_contact);
            deleteButton = itemView.findViewById(R.id.imgview_delete_contact);
        }
    }

    private void showEditDialog(Contact contact, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.add_contact_dialog, null);
        builder.setView(dialogView);

        // Initialize dialog views
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_add_nom);
        TextView dialogUsername = dialogView.findViewById(R.id.dialog_add_pseudo);
        TextView dialogPhone = dialogView.findViewById(R.id.dialog_add_phone);
        MaterialButton saveButton = dialogView.findViewById(R.id.dialog_add_save);
        MaterialButton cancelButton = dialogView.findViewById(R.id.dialog_add_cancel);

        // Set current values
        dialogTitle.setText(contact.getNom());
        dialogUsername.setText(contact.getPseudo());
        dialogPhone.setText(contact.getNumero());

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(v -> {
            String newName = dialogTitle.getText().toString();
            String newUsername = dialogUsername.getText().toString();
            String newPhone = dialogPhone.getText().toString();

            if (!newName.isEmpty() && !newUsername.isEmpty() && !newPhone.isEmpty()) {
                // Update contact in database
                manager.modifier(contact.getNom(), newName, newUsername, newPhone);
                
                // Update contact in list
                contact.setNom(newName);
                contact.setPseudo(newUsername);
                contact.setNumero(newPhone);
                notifyItemChanged(position);
                
                dialog.dismiss();
            } else {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showDeleteConfirmationDialog(Contact contact, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete this contact?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    manager.supprimer(contact.getNom());
                    data.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
