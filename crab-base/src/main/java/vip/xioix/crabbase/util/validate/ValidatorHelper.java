package vip.xioix.crabbase.util.validate;

import android.widget.EditText;

/**
 * Created by terge on 16-12-7.
 */

public class ValidatorHelper {
    public static ValidatorHelper from(EditText editText){
        return new ValidatorHelper(editText);
    }


    private EditText mEt;
    private ValidatorHelper(EditText editText){
        mEt = editText;
    }

    private Validator mValidator;
    public ValidatorHelper and(Validator validator){
        if(mValidator == null){
            mValidator = validator;
        }else {
            mValidator = new AndValidator(mValidator,validator);
        }

        return this;
    }


    public ValidatorHelper set(Validator validator){
        mValidator = validator;
        return this;
    }

    public ValidatorHelper or(Validator validator){
        if(mValidator == null){
            mValidator = validator;
        }else {
            mValidator = new OrValidator(mValidator,validator);
        }

        return this;
    }

    public boolean check(boolean showError){
       boolean result =  mValidator.isValid(mEt);
        if(showError){
            mEt.requestFocus();
            mEt.setError(mValidator.getErrorMessage());
        }
        return result;
    }
}
