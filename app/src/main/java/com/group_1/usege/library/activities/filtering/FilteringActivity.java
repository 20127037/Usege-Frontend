package com.group_1.usege.library.activities.filtering;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.group_1.usege.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilteringActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    AutoCompleteTextView imageTagAutoCompleteTextView;
    EditText descriptionEditText, creationDateEditText;
    TextView selectedLocationTextView;
    Spinner locationSpinner;
    String[] imageTags = {"coffee", "tree", "chair", "people", "shirt"};
    String[] locations = {"", "Ho Chi Minh", "Ha Noi", "Can Tho", "Vinh Long", "Thua Thien Hue"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_filtering);

        imageTagAutoCompleteTextView = findViewById(R.id.image_tag_auto_complete_text_view);
        descriptionEditText = findViewById(R.id.description_edit_text);
        creationDateEditText = findViewById(R.id.creation_date_edit_text);
        selectedLocationTextView = findViewById(R.id.selected_location_text_view);
        locationSpinner = findViewById(R.id.location_spinner);

        ArrayAdapter<String> imageTagAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, imageTags);
        imageTagAutoCompleteTextView.setAdapter(imageTagAdapter);
        imageTagAutoCompleteTextView.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && imageTagAutoCompleteTextView.getText().toString().isEmpty())
                view.setBackgroundResource(R.drawable.lost_focus_edit_text);
            else view.setBackgroundResource(R.drawable.active_edit_text);
        });

        descriptionEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && descriptionEditText.getText().toString().isEmpty())
                view.setBackgroundResource(R.drawable.lost_focus_edit_text);
            else view.setBackgroundResource(R.drawable.active_edit_text);
        });

        creationDateEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && creationDateEditText.getText().toString().isEmpty())
                view.setBackgroundResource(R.drawable.lost_focus_edit_text);
            else view.setBackgroundResource(R.drawable.active_edit_text);
        });

        ArrayAdapter locationAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);
        locationSpinner.setOnItemSelectedListener(this);
    }

    public void openDatePicker(View v) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> creationDateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, 2002, 1, 29);
        datePickerDialog.show();
    }

    public void applyFilters(View v) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);
        String creationDate = creationDateEditText.getText().toString();
        if (!creationDate.isEmpty()) {
            try {
                Date date = formatter.parse(creationDate);
            } catch (ParseException e) {
                creationDateEditText.setBackgroundResource(R.drawable.error_edit_text);
                Toast.makeText(this, "Invalid creation date format", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int chosenIndex, long l) {
        if (chosenIndex == 0) selectedLocationTextView.setBackgroundResource(R.drawable.lost_focus_edit_text);
        else selectedLocationTextView.setBackgroundResource((R.drawable.active_edit_text));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
