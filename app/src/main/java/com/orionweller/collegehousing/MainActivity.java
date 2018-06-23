package com.orionweller.collegehousing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.R.layout;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner spinner_marriage = (Spinner) findViewById(R.id.spinner0);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_marriage = ArrayAdapter.createFromResource(this,
                R.array.married_array, layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_marriage.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_marriage.setAdapter(adapter_marriage);

        Spinner spinner_price = (Spinner) findViewById(R.id.spinner1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_price = ArrayAdapter.createFromResource(this,
                R.array.Price_Array, layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_price.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_price.setAdapter(adapter_price);

        Spinner spinner_distance = (Spinner) findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_distance = ArrayAdapter.createFromResource(this,
                R.array.distance_array, layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_distance.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_distance.setAdapter(adapter_distance);

        Spinner spinner_reviews = (Spinner) findViewById(R.id.spinner3);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_reviews = ArrayAdapter.createFromResource(this,
                R.array.reviews_array, layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_reviews.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_reviews.setAdapter(adapter_reviews);
    }
}