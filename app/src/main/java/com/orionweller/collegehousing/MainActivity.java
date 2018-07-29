package com.orionweller.collegehousing;

import android.R.layout;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add a drawer to the activity

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final Spinner spinner_marriage = (Spinner) findViewById(R.id.marital);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_marriage = ArrayAdapter.createFromResource(this,
                R.array.married_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_marriage.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinner_marriage.setAdapter(adapter_marriage);

        // same as above but another spinner
        final Spinner spinner_reviews = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter_reviews = ArrayAdapter.createFromResource(this,
                R.array.gender_array, layout.simple_spinner_item);
        adapter_reviews.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        spinner_reviews.setAdapter(adapter_reviews);

        Button btn = (Button)findViewById(R.id.search_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get all the search params

                String type = spinner_marriage.getSelectedItem().toString();

                String gender = spinner_reviews.getSelectedItem().toString();

                EditText priceEditText = (EditText)findViewById(R.id.price);
                String price      =  priceEditText.getText().toString();

                EditText tenantsEditText = (EditText)findViewById(R.id.tenants);
                String tenants      =  tenantsEditText.getText().toString();

                EditText apartmentEditText = (EditText)findViewById(R.id.apartment_name);
                String apartment      =  apartmentEditText.getText().toString();

                // use the search params to make a query
                String sqlQuery = get_sql_query(apartment, price, type, gender, tenants);

                Intent intent = new Intent(MainActivity.this, ApartmentTabView.class);
                intent.putExtra("sqlQuery", sqlQuery);
                startActivity(intent);
            }
        });


    }

    private void addDrawerItems() {
        String[] optionsMenu = {"Home", "Log In", "Favorites", "Settings" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionsMenu);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==  0) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    // TODO or should I do nothing?
                }
                else if (position ==  2) {
                    Intent intent = new Intent(MainActivity.this, Favorites.class);
                    startActivity(intent);
                }
                else if (position == 3) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "I haven't implemented this yet!", Toast.LENGTH_SHORT).show();
                }            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public String get_sql_query(String apartment, String price, String type, String gender, String tenants) {

            // flag is used to determine where to put AND statements
            boolean andFlag = false;

            // base query
            String sqlQuery = "SELECT * FROM apts WHERE ";

            if (!TextUtils.isEmpty(apartment)) {
                // use LIKE to get in any order and caps/not caps
                sqlQuery += "Name LIKE \'%" + apartment + "%\'";
                andFlag = true;
            }

            if (!TextUtils.isEmpty(price)) {
                if (andFlag)
                    sqlQuery += " AND Rent_shared_room_year<" + price;
                else {
                    sqlQuery += " Rent_shared_room_year<" + price;
                    andFlag = true;
                }
            }

            if (!TextUtils.isEmpty(tenants)) {
                if (andFlag)
                    sqlQuery += " AND Tenants=" + tenants;
                else {
                    sqlQuery += " Tenants=" + tenants;
                    andFlag = true;
                }
            }

            if (andFlag)
                sqlQuery += " AND Single=\"" + type + "\"";
            else {
                sqlQuery += " Single=\"" + type + "\"";
            }

            if (!TextUtils.isEmpty(gender)) {
                sqlQuery += " AND " + gender + " =\"TRUE\"";
            }

            return sqlQuery;
        }
}