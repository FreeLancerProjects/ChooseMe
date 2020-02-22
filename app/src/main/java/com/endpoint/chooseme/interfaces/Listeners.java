package com.endpoint.chooseme.interfaces;



public interface Listeners {

    interface LoginListener {
        void checkDataLogin(String email, String password);
    }

    interface CreateAccountListener
    {
        void createNewAccount();
    }
    interface ShowCountryDialogListener
    {
        void showDialog();
    }

    interface SignUpListener
    {
        void checkDataSignUp(String name, String phone_code, String phone,String email, String password);

    }

}
