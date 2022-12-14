package com.aimplatfarm.aimplatfarmdelivery.Helpers;

import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public boolean validInput(EditText editText){
        return !editText.getText().toString().trim().equals("");
    }

    public boolean validInputs(EditText input1, EditText input2){
        return validInput(input1) && validInput(input2);
    }

    public boolean validInputs(EditText input1, EditText input2, EditText input3){
        return validInput(input1) && validInput(input2) && validInput(input3);
    }

    public boolean validInputs(EditText input1, EditText input2, EditText input3, EditText input4, EditText input5){
        return validInput(input1) && validInput(input2) && validInput(input3) && validInput(input4) && validInput(input5);
    }

    public boolean validInputs(EditText input1, EditText input2, EditText input3, EditText input4){
        return validInput(input1) && validInput(input2) && validInput(input3) && validInput(input4);
    }

    public boolean validEmail(EditText email){
        return Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches();
    }

    public boolean passwordConfirmation(EditText password, EditText passwordConf){
        if (validInputs(password, passwordConf)){
            return password.getText().toString().equals(passwordConf.getText().toString());
        }else{
            return false;
        }
    }

    public String getString(EditText editText){
        return editText.getText().toString().trim();
    }

    public long getLong(EditText editText){
        return Long.parseLong(getString(editText));
    }

    public int getInt(EditText editText){
        return Integer.parseInt(getString(editText));
    }



}
