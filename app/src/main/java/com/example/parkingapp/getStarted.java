package com.example.parkingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class getStarted extends AppCompatActivity {
    TextView logInPage;
    Button signUpPage;
    BottomSheetDialog signUpbottomSheetDialog;
    BottomSheetDialog signInbottomSheetDialog;
TextView signUpText;
    EditText logInUserId;
    EditText logInPassword;
    logInManager loginManager;
    Button logInButton;
    Button signUpBtn,sendOtp;
    EditText signUpuserId;
    EditText signUpEmail,forgotPasswordEmail;
    EditText signUpPassword;
    RadioGroup userType;
    TextView errorMessage;

    TextView signInText,forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        userSessionManager userSessionManager = new userSessionManager(this);


        // Check if the user is already logged in
        if (userSessionManager.isLoggedIn()) {
            openNavigationDrawer();
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logInPage = findViewById(R.id.SignInPage);
        logInPage.setOnClickListener(v -> openSignInPage());
        signUpPage = findViewById(R.id.signUpPage);
        signUpPage.setOnClickListener(v -> openSignUpPage(v));

    }
    public void openSignInPage() {
         signInbottomSheetDialog = new BottomSheetDialog(getStarted.this);
        View newLogInView = LayoutInflater.from(this).inflate(R.layout.login_sheet, null);
        signInbottomSheetDialog.setContentView(newLogInView);
        signInbottomSheetDialog.show();
        logInUserId = newLogInView.findViewById(R.id.logInUserId);
        logInPassword = newLogInView.findViewById(R.id.logInPassword);
        logInButton = newLogInView.findViewById(R.id.logInBtn);
        signUpText = newLogInView.findViewById(R.id.signUpText);
        loginManager = new logInManager(this,logInUserId,logInPassword);
        forgotPassword = newLogInView.findViewById(R.id.forgotPassword);

        forgotPassword.setOnClickListener(v -> {
            signInbottomSheetDialog.dismiss();
            openForgotPassword();});

        logInButton.setOnClickListener(v -> {
            loginManager.logIn();
        });
        signUpText.setOnClickListener(v -> {
            signInbottomSheetDialog.dismiss();
            openSignUpPage(v);
        });
    }


    public  void openSignUpPage(View view){
        signUpbottomSheetDialog = new BottomSheetDialog(getStarted.this);
        View newSignUpView = LayoutInflater.from(this).inflate(R.layout.signup_sheet, null);
        signUpbottomSheetDialog.setContentView(newSignUpView);
        signUpbottomSheetDialog.show();
        signUpBtn = newSignUpView.findViewById(R.id.signUpBtn);
        signUpuserId = newSignUpView.findViewById(R.id.signUpUserId);
        signUpEmail = newSignUpView.findViewById(R.id.signUpEmail);
        signUpPassword = newSignUpView.findViewById(R.id.signUpPassword);
        userType = newSignUpView.findViewById(R.id.userTypeSelection);
        errorMessage = newSignUpView.findViewById(R.id.errorMessage);
        signInText = newSignUpView.findViewById(R.id.signInText);
        signUpManager signUpManager = new signUpManager(this,signUpuserId,signUpEmail,signUpPassword,userType,errorMessage,this);
        signUpBtn.setOnClickListener(v -> {signUpManager.signUp();});
        signInText.setOnClickListener(v -> {signUpbottomSheetDialog.dismiss();
            openSignInPage();});
    }

    public void openForgotPassword(){
        BottomSheetDialog forgotPasswordbottomSheetDialog = new BottomSheetDialog(getStarted.this);
        View newForgotPasswordView = LayoutInflater.from(this).inflate(R.layout.sendotp_sheeet, null);
        forgotPasswordbottomSheetDialog.setContentView(newForgotPasswordView);
        forgotPasswordbottomSheetDialog.show();
        sendOtp = newForgotPasswordView.findViewById(R.id.sendOtpButton);
        EditText otpInput = newForgotPasswordView.findViewById(R.id.otpInput);
        forgotPasswordEmail = newForgotPasswordView.findViewById(R.id.forgotPasswordEmail);
        forgotPasswordManager forgotPasswordManager = new forgotPasswordManager(this,forgotPasswordEmail,otpInput);//this class will validate the email and send otp

        sendOtp.setOnClickListener(new View.OnClickListener() {
            boolean isOtpSent = false;

            @Override
            public void onClick(View v) {
                    // First click: Validate email and send OTP
                forgotPasswordManager.validateEmail()
                        .thenAccept(isValid -> {
                            if (isValid) {
                                // Email is valid
                                runOnUiThread(() -> {
                                    sendOtp.setText("Confirm");
                                    otpInput.setVisibility(View.VISIBLE);
                                    // Second click: Validate OTP
                                    sendOtp.setOnClickListener(v1 -> {
                                        if (forgotPasswordManager.validateOtp()) {
                                            forgotPasswordbottomSheetDialog.dismiss();
                                            setNewPasswordSheet(forgotPasswordEmail);
                                        }
                                    });
                                });
                            } else {
                                // Email validation failed
                                // You can handle this case if needed
                            }
                        })
                        .exceptionally(ex -> {
                            // Handle exceptions here
                            ex.printStackTrace();
                            return null;
                        });

            }




        });


        signUpText = newForgotPasswordView.findViewById(R.id.signUpText);
        signUpText.setOnClickListener(v -> {
            forgotPasswordbottomSheetDialog.dismiss();
            openSignUpPage(v);}); //when user presses sign up the sign up bottom sheet will close
    }


    public void setNewPasswordSheet(EditText setPasswordEmail){
            BottomSheetDialog newPasswordbottomSheetDialog = new BottomSheetDialog(getStarted.this);
            View newPasswordView = LayoutInflater.from(this).inflate(R.layout.newpassword_sheet, null);
            newPasswordbottomSheetDialog.setContentView(newPasswordView);
            newPasswordbottomSheetDialog.show();
            EditText newPassword = newPasswordView.findViewById(R.id.newPasswordInput);
            EditText confirmPassword = newPasswordView.findViewById(R.id.confirmPasswordInput);
            Button resetPassword = newPasswordView.findViewById(R.id.resetPasswordBtn);
            forgotPasswordManager resetPasswordManager = new forgotPasswordManager(newPassword,confirmPassword,setPasswordEmail,this,this);
            resetPassword.setOnClickListener(v -> {
                resetPasswordManager.resetPassword();
            });

    }




    public void openNavigationDrawer() {
        Intent intent = new Intent(this, navigationDrawer.class);
        startActivity(intent);
        finish();
    }





}