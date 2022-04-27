package com.globalshops.customer.shoeShop.utils;

import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

public class InputValidation {

    public static boolean isValidEmail(String input, TextInputLayout textInputLayout){
        String emptyFieldErrorMessage = "Field can't be empty";
        String invalidEmailAddressErrorMessage = "Invalid email address";
        if (input.isEmpty()){
            textInputLayout.setError(emptyFieldErrorMessage);
            return true;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()){
            textInputLayout.setError(invalidEmailAddressErrorMessage);
            return true;
        }else {
            textInputLayout.setError(null);
            return false;
        }
    }
    public static boolean isValidPassword(String input, TextInputLayout textInputLayout){
        String emptyFieldErrorMessage = "Field can't be empty";
        String validCharLengthErrorMessage = "Password must be at least 4 character";
      if (input.isEmpty()){
          textInputLayout.setError(emptyFieldErrorMessage);
          return true;
      }else if(input.length() < 4){
          textInputLayout.setError(validCharLengthErrorMessage);
          return true;
      }else {
          textInputLayout.setError(null);
          return false;
      }
    }
    public static boolean isValidName(String input, TextInputLayout textInputLayout){
        String emptyFieldErrorMessage = "Field can't be empty";
        if (input.isEmpty()){
            textInputLayout.setError(emptyFieldErrorMessage);
            return true;
        }else {
            textInputLayout.setError(null);
            return false;
        }
    }
    public static boolean isNewPasswordEqualsRepeatPassword(String newPassword, String repeatPassword, TextInputLayout textInputLayout){
        String passwordMismatchError = "This password don't match";

        if (!newPassword.equals(repeatPassword)){
            textInputLayout.setError(passwordMismatchError);
            return true;
        }else {
            textInputLayout.setError(null);
            return false;
        }

    }
    public static boolean isValidPhoneNumber(String phoneNumber, TextInputLayout textInputLayout){
        String emptyFieldErrorMessage = "Field can't be empty";
        String validLengthPhoneNumber = "Phone number must be 9 character long";

        if (phoneNumber.isEmpty()){
            textInputLayout.setError(emptyFieldErrorMessage);
            return true;
        }else if (phoneNumber.length() != 9){
            textInputLayout.setError(validLengthPhoneNumber);
            return true;
        }else {
            textInputLayout.setError(null);
            return false;
        }
    }

    public static boolean isValidShoeQuantity(String shoeQuantity, TextInputLayout textInputLayout){
        String errorMessage = "Field can't be empty, add 0 instead";

        if (shoeQuantity.isEmpty()){
            textInputLayout.setError(errorMessage);
            return true;
        }else {
            textInputLayout.setError(null);
            return false;
        }
    }

}
