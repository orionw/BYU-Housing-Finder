package com.orionweller.collegehousing;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.R.layout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.orionweller.collegehousing.HousingList;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBaseHelper db;
         Cursor apartments;

        final Spinner spinner_marriage = (Spinner) findViewById(R.id.marital);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_marriage = ArrayAdapter.createFromResource(this,
                R.array.married_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_marriage.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinner_marriage.setAdapter(adapter_marriage);

        final Spinner spinner_reviews = (Spinner) findViewById(R.id.reviews);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_reviews = ArrayAdapter.createFromResource(this,
                R.array.reviews_array, layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_reviews.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_reviews.setAdapter(adapter_reviews);

        db = new DataBaseHelper(this);
        apartments = db.getApartments();


        Button btn = (Button)findViewById(R.id.search_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = spinner_marriage.getSelectedItem().toString();

                String people = spinner_reviews.getSelectedItem().toString();

                EditText priceEditText = (EditText)findViewById(R.id.price);
                String price      =  priceEditText.getText().toString();

                EditText distanceEditText = (EditText)findViewById(R.id.distance);
                String distance      =  distanceEditText.getText().toString();

                EditText apartmentEditText = (EditText)findViewById(R.id.apartment_name);
                String apartment      =  apartmentEditText.getText().toString();


                String sqlQuery = get_sql_query(apartment, price, type, people, distance);

                Intent intent = new Intent(MainActivity.this, HousingList.class);
                intent.putExtra("sqlQuery", sqlQuery);
                startActivity(intent);
            }
        });


    }

        public String get_sql_query(String apartment, String price, String type, String people, String distance) {

            // flag is used to determine where to put AND statements
            boolean andFlag = false;

            // base query
            String sqlQuery = "SELECT * FROM apartments1 WHERE ";

            if (!TextUtils.isEmpty(apartment)) {
                // use LIKE to get in any order and caps/not caps
                sqlQuery += "name LIKE \'%" + apartment + "%\'";
                andFlag = true;
            }

            if (!TextUtils.isEmpty(price)) {
                if (andFlag)
                    sqlQuery += " AND price<" + price;
                else {
                    sqlQuery += " price<" + price;
                    andFlag = true;
                }
            }

            if (!TextUtils.isEmpty(distance)) {
                if (andFlag)
                    sqlQuery += " AND distance<" + distance;
                else {
                    sqlQuery += " distance<" + distance;
                    andFlag = true;
                }
            }

            if (andFlag)
                sqlQuery += " AND type=\"" + type + "\"";
            else {
                sqlQuery += " type=\"" + type + "\"";
            }

            if (!TextUtils.isEmpty(people)) {
                sqlQuery += " AND people=" + people;
            }

            return sqlQuery;
        }
}