package com.orionweller.collegehousing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.R.layout;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner spinner_marriage = (Spinner) findViewById(R.id.marital);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_marriage = ArrayAdapter.createFromResource(this,
                R.array.married_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_marriage.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinner_marriage.setAdapter(adapter_marriage);

        Spinner spinner_reviews = (Spinner) findViewById(R.id.reviews);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_reviews = ArrayAdapter.createFromResource(this,
                R.array.reviews_array, layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_reviews.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_reviews.setAdapter(adapter_reviews);

        Button btn = (Button)findViewById(R.id.search_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HousingList.class));
            }
        });


    }


}