package com.orionweller.collegehousing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class ApartmentDetails extends AppCompatActivity{

    private Context mContext;
    private Activity mActivity;

    private CoordinatorLayout mCLayout;
    private Button mButtonDo;
    private ProgressDialog mProgressDialog;
    private LinearLayout mLLayout;

    private AsyncTask mMyTask;

    public static List<Bitmap> images;
    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;

    public static boolean hasLoaded = false;
    public static String lastApartment;
    String apartmentName;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private Cursor data;
    public Apartment currentApartment;
    boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (lastApartment == null) {
            lastApartment = "";
        }

        // if we came from apartmentList grab the name else from Favs take the last apartment name
        Intent intent = getIntent();
        try {
            currentApartment = (Apartment) getIntent().getSerializableExtra("Apartment");
            apartmentName = currentApartment.name;
        } catch (NullPointerException error) {
            Log.d("ApartmentDetails", "onCreate: no string");
            apartmentName = lastApartment;
            currentApartment = new Apartment();
        }

        if (!lastApartment.equals(apartmentName)) {
            hasLoaded = false;
            lastApartment = apartmentName;
        }

        if (!hasLoaded) {
            setContentView(R.layout.apartment_details);
            // set up drawer
            mDrawerList = (ListView)findViewById(R.id.navListDetails);
            mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_details);
            mActivityTitle = getTitle().toString();

            addDrawerItems();
            setupDrawer();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            // Set up database and info
            DataBaseHelper helper = new DataBaseHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();
            String query = "SELECT * FROM apts WHERE Name=\"" + apartmentName +"\"";
            data = db.rawQuery(query, null);
            Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(data));
            initInfo();

            // Set up layout stuff
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            myPagerAdapter = new MyPagerAdapter(ApartmentDetails.this, apartmentName);
            viewPager.setAdapter(myPagerAdapter);
            indicator.setViewPager(viewPager);
            hasLoaded = true;
            mLLayout = (LinearLayout) findViewById(R.id.real_linear_details);

            // Get the application context
            mContext = getApplicationContext();
            mActivity = ApartmentDetails.this;

            // Todo: link comments database with scrollView


        } else {
            setContentView(R.layout.apartment_details);
            // set up action bar
            mDrawerList = (ListView)findViewById(R.id.navListDetails);
            mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_details);
            mActivityTitle = getTitle().toString();

            addDrawerItems();
            setupDrawer();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);


            // Set up database and info
            DataBaseHelper helper = new DataBaseHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();
            String query = "SELECT * FROM apts WHERE Name=\"" + apartmentName +"\"";
            data = db.rawQuery(query, null);
            Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(data));
            initInfo();

            viewPager = (ViewPager) findViewById(R.id.viewPager);
            myPagerAdapter = new MyPagerAdapter(ApartmentDetails.this, apartmentName);
            viewPager.setAdapter(myPagerAdapter);
            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(viewPager);
            mLLayout = (LinearLayout) findViewById(R.id.real_linear_details);

        }
    }

    //TODO add a reviews button

    private void addDrawerItems() {
        String[] optionsMenu = {"Home", "Log In", "Favorites", "Settings" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionsMenu);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==  0) {
                    Intent intent = new Intent(ApartmentDetails.this, MainActivity.class);
                    startActivity(intent);
                }
                else if (position ==  2) {
                    Intent intent = new Intent(ApartmentDetails.this, Favorites.class);
                    //TODO Clean up new.memory leaks?
                    startActivity(intent);
                }
                else if (position == 3) {
                    Intent intent = new Intent(ApartmentDetails.this, SettingsActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ApartmentDetails.this, "I haven't implemented this yet!", Toast.LENGTH_SHORT).show();
                }
            }
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
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        initFavoritesButton(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.favorites) {
            isFavorite = dataInDatabase("favorites", "Name", currentApartment.name);
            // if data not already in database
            if (!isFavorite) {
                DataBaseHelper helper = new DataBaseHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("Name", currentApartment.name);
                values.put("Rent_shared_room_year", currentApartment.price);
                values.put("Distance", currentApartment.distance);
                // todo add a favorites column to Database and check it or something
                // Inserting Row
                db.insert("favorites", null, values);
            }
            checkAndToggleFavorite(item);
            return true;
        }

        if (id == R.id.back_to_list) {
            Intent intent = new Intent(this,
                    ApartmentTabView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }

        // Activate the navigation drawer toggle
        else if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initInfo(){
        TableLayout table = (TableLayout) findViewById(R.id.table_layout);
        List<Integer> positionList = new ArrayList<>();
        int name_index = data.getColumnIndex("Name");
//        int tenants_index = data.getColumnIndex("Tenants");
//        int bathrooms_index = data.getColumnIndex("Bathrooms");
        int address_index = data.getColumnIndex("Address");
//        int website_index = data.getColumnIndex("Website");
//        int gender_index = data.getColumnIndex("Gender");
        int year_price_index = data.getColumnIndex("Rent_shared_room_year");
//        int fall_winter_price_index = data.getColumnIndex("Fall-winter-price");
//        int deposit_index = data.getColumnIndex("Deposit");
        ArrayList<String> nameList = new ArrayList<>();
        nameList.add("Name:");
        nameList.add("Address:  ");
        nameList.add("Price:");

        positionList.add(name_index);
        positionList.add(address_index);
        positionList.add(year_price_index);
        data.moveToFirst();

        currentApartment.name = data.getString(positionList.get(0));
        currentApartment.address = data.getString(positionList.get(1));
        currentApartment.price = currentApartment.price;




        for (int i = 0; i <positionList.size(); i++) {

            TableRow row= new TableRow(this);
            //TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            //row.setLayoutParams(lp);
            TextView option = new TextView(this);
            TextView param = new TextView(this);
            // Get name of param for table
            option.setText(nameList.get(i));
            // Get cursors result for table
            param.setText(data.getString(positionList.get(i)));
            // set table
            row.addView(option);
            row.addView(param);
            table.addView(row,i+1);
            Log.d("Table Row is", table.toString());
            Log.d("Table  is", row.toString());
        }
    }

    public void initFavoritesButton(Menu menu) {
        if (dataInDatabase("favorites", "NAME", currentApartment.name)) {
            MenuItem star =  menu.findItem(R.id.favorites);
            star.setIcon(android.R.drawable.btn_star_big_on);
        }
        // else it is by default off
    }

    public void checkAndToggleFavorite(MenuItem item) {
        if (!isFavorite) {
            isFavorite = true;
            item.setIcon(android.R.drawable.btn_star_big_on);

        } else {
            isFavorite = false;
            item.setIcon(android.R.drawable.btn_star_big_off);
            deleteRow(currentApartment.name);

        }
    }

    public  boolean dataInDatabase(String TableName,
                                   String dbfield, String fieldValue) {
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase sqldb = helper.getReadableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = '" + fieldValue + "'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public void deleteRow(String value)
    {
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase sqldb = helper.getWritableDatabase();
        sqldb.execSQL("DELETE FROM favorites WHERE NAME='" + value + "'");
        sqldb.close();
    }
}

