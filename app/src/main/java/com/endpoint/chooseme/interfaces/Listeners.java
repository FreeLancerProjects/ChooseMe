package com.endpoint.chooseme.interfaces;



public interface Listeners {

    interface SkipListener
    {
        void skip();
    }

    interface CreateAccountListener
    {
        void createNewAccount();
    }
    interface ShowCountryDialogListener
    {
        void showDialog();
    }



}
