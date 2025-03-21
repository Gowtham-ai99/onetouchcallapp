package com.gowtham.onetouchcallapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private Context context;
    private List<Contact> contactList;
    private OnItemClickListener listener;

    public ContactAdapter(Context context, List<Contact> contactList, OnItemClickListener listener) {
        this.context = context;
        this.contactList = contactList;
        this.listener = listener;
    }

    // Define the correct OnItemClickListener interface
    public interface OnItemClickListener {
        void onItemClick(String phoneNumber);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.textViewName.setText(contact.getName());

        // Load profile image
        if (contact.getImageUri() != null && !contact.getImageUri().isEmpty()) {
            holder.imageViewProfile.setImageURI(Uri.parse(contact.getImageUri()));
        } else {
            holder.imageViewProfile.setImageResource(R.drawable.profile_placeholder);
        }

        // Click listener for calling the contact
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(contact.getPhone());  // ✅ Pass `contact.getPhone()` instead of `contact`
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageViewProfile;

        public ContactViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.contactName);
            imageViewProfile = itemView.findViewById(R.id.contactImage);
        }
    }
}
