package com.example.parkingapp;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

public class forgotPasswordManager {

    private Context context;
    private EditText email;
    private EditText Otp;
    private EditText password,confirmPassword;
    public forgotPasswordManager(Context context, EditText email, EditText Otp) {
        this.context = context;
        this.email = email;
        this.Otp = Otp;
    }
    public forgotPasswordManager(EditText password, EditText confirmPassword,Context context){
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.context = context;
    }


    public boolean validateEmail(){
        String email = this.email.getText().toString();
        if(email.isEmpty()){
            this.email.setError("Email is required");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this.email.setError("Invalid Email");
            return false;
        }
        return true;
        // you can also perform requests to database to check if email exists or not
        //if not then set error that email does not exists
    }
    public boolean validateOtp(){
        String otp = this.Otp.getText().toString();
       if(otp.isEmpty()){
            this.Otp.setError("Otp is required");
            return false;
        }
        else if(otp.length()!=5){
            this.Otp.setError("Invalid Otp");
            return false;
        }
        // you can also perform requests to database to check if otp is correct or not
        //if not then set error that otp is incorrect
        return true;
    }
    public boolean validatePassword(){
        String newPass = this.password.getText().toString();
        String confirmPasswordInput = this.confirmPassword.getText().toString();

        if(newPass.isEmpty()){
            this.password.setError("Password is required");
            return false;
        }
        else if(newPass.length()<8){
            this.password.setError("Password must be at least 8 characters");
            return false;
        }
        if(confirmPasswordInput.isEmpty()){
            this.confirmPassword.setError("Confirm Password is required");
            return false;
        }
        else if(!newPass.equals(confirmPasswordInput)) {
            this.confirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    public boolean updateDatabase() {
        // perform database update here
        return true;//return true if update is successful
    }

    public boolean resetPassword(){
        if(validatePassword()){
            if(updateDatabase()){
                return true;//return true if update is successful and password is reset
            };
        }return false;
    }
}
