package com.inventivestack.contactslist;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by akumar1 on 11/21/2017.
 */

public class Contact {
    String id;
    String name;
    List<Phone> phone;
    List<Email> email;
    Bitmap image;
    String address;
    String website;

    public Contact() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Phone> getPhone() {
        return phone;
    }

    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    public List<Email> getEmail() {
        return email;
    }

    public void setEmail(List<Email> email) {
        this.email = email;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public static class Phone {
        String phoneNo;
        String phoneType;
        String phoneTypeLabel;

        public Phone() {
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getPhoneType() {
            return phoneType;
        }

        public void setPhoneType(String phoneType) {
            this.phoneType = phoneType;
        }

        public String getPhoneTypeLabel() {
            return phoneTypeLabel;
        }

        public void setPhoneTypeLabel(String phoneTypeLabel) {
            this.phoneTypeLabel = phoneTypeLabel;
        }
    }

    public static class Email {
        String email;
        String emailType;
        String emailTypeLabel;

        public Email() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmailType() {
            return emailType;
        }

        public void setEmailType(String emailType) {
            this.emailType = emailType;
        }

        public String getEmailTypeLabel() {
            return emailTypeLabel;
        }

        public void setEmailTypeLabel(String emailTypeLabel) {
            this.emailTypeLabel = emailTypeLabel;
        }
    }
}
