package com.endpoint.chooseme.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String twitter;
    private String linkedin;
    private String whatsapp;
    private String facebook;
    private String price_in_hour;
    private double rate;
    private List<ServiceModel> serviceModelList;
    private List<Works> worksList;


    public UserModel() {
    }


    public UserModel(String id, String name, String email, String phone, String password, String twitter, String linkedin, String whatsapp, String facebook, String price_in_hour,double rate, List<ServiceModel> serviceModelList, List<Works> worksList) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.rate = rate;
        this.twitter = twitter;
        this.linkedin = linkedin;
        this.whatsapp = whatsapp;
        this.facebook = facebook;
        this.price_in_hour = price_in_hour;
        this.serviceModelList = serviceModelList;
        this.worksList = worksList;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getPrice_in_hour() {
        return price_in_hour;
    }

    public void setPrice_in_hour(String price_in_hour) {
        this.price_in_hour = price_in_hour;
    }



    public List<ServiceModel> getServiceModelList() {
        return serviceModelList;
    }

    public void setServiceModelList(List<ServiceModel> serviceModelList) {
        this.serviceModelList = serviceModelList;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<Works> getWorksList() {
        return worksList;
    }

    public void setWorksList(List<Works> worksList) {
        this.worksList = worksList;
    }

    public static class Works implements Serializable
    {
        private String image;
        private String price;
        private String title;
        private double rate;

        public Works() {
        }

        public Works(String image, String price, String title,double rate) {
            this.image = image;
            this.price = price;
            this.title = title;
            this.rate = rate;
        }


        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }
    }
}
