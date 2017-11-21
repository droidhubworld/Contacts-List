package com.inventivestack.contactslist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akumar1 on 11/21/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private List<Contact> contactList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, details;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            details = (TextView) view.findViewById(R.id.tv_details);
            image = (ImageView) view.findViewById(R.id.img_image);
        }
    }

    public CustomAdapter(Context context) {
        this.context = context;
        contactList = new ArrayList<>();
    }

    public void addItem(Contact contactItem) {
        this.contactList.add(contactItem);
        notifyItemInserted(contactList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_items_listview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.name.setText(contact.getName());
        String details = "";

        for (int i = 0; i < contact.getPhone().size(); i++) {
            if (details.equals("")) {
                details = contact.getPhone().get(i).getPhoneNo() + " , Type: " + contact.getPhone().get(i).getPhoneTypeLabel();
            } else {
                details += "\n" + contact.getPhone().get(i).getPhoneNo() + " , Type: " + contact.getPhone().get(i).getPhoneTypeLabel();
            }
        }

        for (int i = 0; i < contact.getEmail().size(); i++) {
            if (details.equals("")) {
                details = contact.getEmail().get(i).getEmail() + " , Type: " + contact.getEmail().get(i).getEmailTypeLabel();
            } else {
                details += "\n" + contact.getEmail().get(i).getEmail() + " , Type: " + contact.getEmail().get(i).getEmailTypeLabel();
            }
        }
        if (contact.getAddress() != null)
            details += "\nAddress: " + contact.getAddress();
        if (contact.getWebsite() != null)
            details += "\nWebsite: " + contact.getWebsite();

        holder.details.setText(details);
        if (contact.getImage() != null)
            holder.image.setImageBitmap(contact.getImage());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}