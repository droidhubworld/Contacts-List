package com.inventivestack.contactslist;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView contactList;
    CustomAdapter mAdapter;
    ArrayList<Contact> StoreContacts;

    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = (RecyclerView) findViewById(R.id.list_contactList);
        contactList.setHasFixedSize(true);
        contactList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CustomAdapter(this);
        contactList.setAdapter(mAdapter);
        findViewById(R.id.tv_permissionError).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.tv_permissionError).setVisibility(View.GONE);
                askForPermission(Manifest.permission.READ_CONTACTS, RequestPermissionCode);
            }
        });
        if (askForPermission(Manifest.permission.READ_CONTACTS, RequestPermissionCode)) {
            readContacts();
        }
    }

    public void readContacts() {
        long startMillis = System.currentTimeMillis();//Calendar.getInstance().getTimeInMillis();
        Calendar startTime = Calendar.getInstance();
        startTime.setTimeInMillis(startMillis);

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        String image_uri = "";
        Bitmap bitmap = null;


        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                Contact contact = new Contact();
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);

                    contact.setName(name);
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    List<Contact.Phone> phone = new ArrayList<>();
                    while (pCur.moveToNext()) {
                        Contact.Phone phoneItem = new Contact.Phone();
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String type = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        String typeLabel = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(this.getResources(), Integer.parseInt(type), "");
                        phoneItem.setPhoneNo(phoneNo);
                        phoneItem.setPhoneType(type);
                        phoneItem.setPhoneTypeLabel(typeLabel);
                        phone.add(phoneItem);
                        System.out.println(typeLabel + ":" + phoneNo);
                    }
                    pCur.close();
                    contact.setPhone(phone);

                    Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                    List<Contact.Email> email = new ArrayList<>();
                    while (emailCur.moveToNext()) {
                        Contact.Email emailItem = new Contact.Email();
                        String emailAddress = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        String emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        String emailTypeLabel = (String) ContactsContract.CommonDataKinds.Email.getTypeLabel(this.getResources(), Integer.parseInt(emailType), "");

                        emailItem.setEmail(emailAddress);
                        emailItem.setEmailType(emailType);
                        emailItem.setEmailTypeLabel(emailTypeLabel);
                        email.add(emailItem);

                        System.out.println("Email " + emailAddress + " Email Type : " + emailTypeLabel + " >>>> " + emailType);
                    }
                    emailCur.close();

                        contact.setEmail(email);

                    Cursor postal_cursor = cr.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + "=" + id.toString(), null, null);
                    String address = null;
                    while (postal_cursor.moveToNext()) {
                        String street = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                        if (address != null) {
                            address += "\nAddress : " + street;
                        } else {
                            address = "Address : " + street;
                        }
                        System.out.println("\n Address : " + street);

                    }
                    postal_cursor.close();
                    if (address != null)
                        contact.setAddress(address);

                    String[] PROJECTION = {
                            ContactsContract.CommonDataKinds.Website.URL
                    };
                    Cursor website_cursor = cr.query(Data.CONTENT_URI, PROJECTION, Data.RAW_CONTACT_ID + "=? AND " + Data.MIMETYPE + "=?", new String[]{id, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE}, ContactsContract.CommonDataKinds.Website._ID);
                    String website = null;
                    while (website_cursor.moveToNext()) {
                        if (website != null) {
                            website = website + "\n" + website_cursor.getString(website_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
                        } else {
                            website = website_cursor.getString(website_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));

                        }
                        System.out.println("\n Website : " + website);

                    }
                    website_cursor.close();
                    if (website != null)
                        contact.setWebsite(website);

                }
                if (image_uri != null) {
                    System.out.println(Uri.parse(image_uri));
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(image_uri));
                        contact.setImage(bitmap);
                        System.out.println(bitmap);
                    } catch (FileNotFoundException e) { // TODO Auto-generated catch block e.printStackTrace();
                    } catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace();
                    }
                }

                mAdapter.addItem(contact);

            }

        }
        long now = System.currentTimeMillis();
        long difference = now - startMillis;
        long differenceInSeconds = difference / DateUtils.SECOND_IN_MILLIS;
        // formatted will be HH:MM:SS or MM:SS
        String formatted = DateUtils.formatElapsedTime(differenceInSeconds);
        System.out.println("\n TOTAL TIME : " + formatted);
    }

    private boolean askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this, "Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();
                    readContacts();
                } else {
                    findViewById(R.id.tv_permissionError).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_permissionError)).setText("Permission Canceled, Now your application cannot access CONTACTS.");
                    Toast.makeText(MainActivity.this, "Permission Canceled, Now application cannot access your CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}
