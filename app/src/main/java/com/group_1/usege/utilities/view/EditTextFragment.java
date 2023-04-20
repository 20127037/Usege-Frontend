package com.group_1.usege.utilities.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.group_1.usege.R;
import com.group_1.usege.utilities.validator.PasswordValidator;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Utility fragment for showing an edit text with associated label
 */
public class EditTextFragment extends Fragment {
    protected TextInputEditText editText;
    protected TextInputLayout textInputLayout;
    private TypedArray typedArray;
    private boolean isCheckRule;

    public EditTextFragment() {
        super(R.layout.fragment_edit_text);
    }

    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextFragment);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            final String label = typedArray.getString(R.styleable.EditTextFragment_android_text);
            final Drawable leftDrawable = typedArray.getDrawable(R.styleable.EditTextFragment_android_drawableLeft);
            final int inputType = typedArray.getInt(R.styleable.EditTextFragment_android_inputType, InputType.TYPE_CLASS_TEXT);
            isCheckRule = typedArray.getBoolean(R.styleable.EditTextFragment_checkRule, false);
            typedArray.recycle();

            final TextView labelText = view.findViewById(R.id.label_text);
            labelText.setText(label);

            editText = view.findViewById(R.id.edit_text);
            editText.setInputType(inputType);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    textInputLayout.setError(null);
                }
            });
            textInputLayout = view.findViewById(R.id.text_input_layout);
            textInputLayout.setErrorIconDrawable(0);
            if (inputType == (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT))
                textInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            else if (inputType == (InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE))
            {
                editText.setOnClickListener(v -> {
                    YearMonth now = YearMonth.now();
                    RackMonthPicker picker = new RackMonthPicker(v.getContext())
                            .setLocale(Locale.ENGLISH)
                            .setSelectedMonth(now.getMonthValue())
                            .setPositiveButton((month, startDate, endDate, year, monthLabel) -> {
                                YearMonth value = YearMonth.of(year, month);
                                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/yy", Locale.getDefault());
                                editText.setText(value.format(dateFormat));
                            })
                            .setNegativeButton(AppCompatDialog::dismiss);
                    picker.show();
                });
                editText.setCursorVisible(false);
                editText.setOnFocusChangeListener((v, hasFocus) -> {
                    if (hasFocus)
                    {
                        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                });
                editText.setShowSoftInputOnFocus(false);
//                editText.setInputType(InputType.TYPE_NULL);
            }
            textInputLayout.setStartIconDrawable(leftDrawable);
        } catch (IllegalStateException exception) {
            Log.e(EditTextFragment.class.getName(), exception.getMessage());
        }
    }

    public void setError(@StringRes int errRes)
    {
        textInputLayout.setError(getContext().getResources().getString(errRes));
    }

    public static List<String> getValues(EditTextFragment... fragments)
    {
        List<String> values = new ArrayList<>();
        for (EditTextFragment fragment : fragments)
        {
            String value = fragment.getValue();
            if (value == null)
                return null;
            values.add(value);
        }
        return values;
    }

    public String getValue() {
        String input = editText.getText().toString();
        if (input.isEmpty()) {
            textInputLayout.setError(getContext().getResources().getString(R.string.filed_not_empty));
            return null;
        }
        if (isCheckRule && !PasswordValidator.checkPasswordFollowRules(input))
        {
            setError(R.string.password_not_follow_rules);
            return null;
        }
        return input;
    }
}