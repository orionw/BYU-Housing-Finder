package com.orionweller.collegehousing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class ApartmentTabView extends AppCompatActivity {

    // will contain the results of the SQL query
    public static Cursor c;
    public static ArrayList<Apartment> apartmentList;
    public String selectQuery;
    public static ArrayList<MarkerOptions> markers;
    public static boolean pinsDoneLoading;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d("Saving apartmentlist", "onSaveInstanceState: Saving it");
        savedInstanceState.putString("selectQuery", selectQuery);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            selectQuery = savedInstanceState.getString("selectQuery");
        } else {
            Intent intent = getIntent();
            selectQuery = intent.getExtras().getString("sqlQuery");
            Log.v("sqlQuery", selectQuery);
        }

//        ProgressDialog pdLoading = new ProgressDialog(this);
//        pdLoading.setMessage("\tLoading...");
//        pdLoading.show();


//         This was the old way of reading from a CSV
//        InputStream inputStream = getResources().openRawResource(R.raw.apartments);
//        CSVFile csvFile = new CSVFile(inputStream);
//        List scoreList = csvFile.read();

        //  Get the database and send the query, returns a cursor
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+"favorites"+" (id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Rent_shared_room_year INTEGER, " +
                "Latitude TEXT,  "+" Longitude TEXT, "+" Distance FLOAT)");
        c = db.rawQuery(selectQuery, null);
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));

        // Get list of names and addresses

        apartmentList = get_address_list_from_query(c);
        new GetLatLngInfo().execute();


        // Set up tabs
        Toolbar toolbar ;
        TabLayout tabLayout ;
        final ViewPager viewPager ;
        FragmentAdapterClass fragmentAdapter ;

        tabLayout = (TabLayout) findViewById(R.id.tab_layout1);
        viewPager = (ViewPager) findViewById(R.id.pager1);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);

        //setSupportActionBar(toolbar);

        // name the tabs
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.addTab(tabLayout.newTab().setText("Map"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        fragmentAdapter = new FragmentAdapterClass(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Results");
//        pdLoading.dismiss();

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // back button pressed
//                Intent intent = new Intent(ApartmentTabView.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab LayoutTab) {

                viewPager.setCurrentItem(LayoutTab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab LayoutTab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab LayoutTab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private ArrayList<Apartment> get_address_list_from_query(Cursor mCursor) {

        ArrayList<Apartment> builder = new ArrayList<>();
        mCursor.moveToFirst();

        int name_index = mCursor.getColumnIndex("Name");
        int address_index = mCursor.getColumnIndex("Address");
        int price_index = mCursor.getColumnIndex("Rent_shared_room_year");
        int longitude_index = mCursor.getColumnIndex("Longitude");
        int latitude_index = mCursor.getColumnIndex("Latitude");
        int distance_index = mCursor.getColumnIndex("Distance");


        try {
            while (!mCursor.isAfterLast()) {
                builder.add(new Apartment(mCursor.getString(name_index), mCursor.getString(address_index),
                        mCursor.getString(latitude_index), mCursor.getString(longitude_index))); //add the apartment object
                mCursor.moveToNext();
            }
        } catch (IllegalStateException NoLongandLat) {
            builder.add(new Apartment(mCursor.getString(name_index), mCursor.getInt(price_index),
                            mCursor.getString(latitude_index), mCursor.getString(longitude_index),
                    Double.parseDouble(mCursor.getString(distance_index))));
            mCursor.moveToNext();

        }
        Log.d("array to list", builder.toString());
        return builder;
    }



    public ArrayList<MarkerOptions> setAllLocations() {
        LatLng place;
        ArrayList<MarkerOptions> markers = new ArrayList<>();
        Log.d(TAG, "I'm in SetAllLocations: ");
        for(int i = 0; i < apartmentList.size(); i += 1){
            place = new LatLng(Double.parseDouble(apartmentList.get(i).latitude), Double.parseDouble(apartmentList.get(i).longitude));


            // if there is an address get long/lat as long as nothing is null
            if (apartmentList.get(i) != null && apartmentList.size() > 0 && place != null) {
                MarkerOptions markerOptions = new MarkerOptions().position(place); //1
                String titleStr = apartmentList.get(i).name;
                markerOptions.title(titleStr);
                Log.d("Marker", markerOptions.getPosition().toString());
                Log.d("Marker", markerOptions.getTitle());
                markers.add(markerOptions);
            }
        }
        return markers;
    }

    public class GetLatLngInfo extends AsyncTask<Void, Void, Void>
    {
//        ProgressDialog pdLoading = new ProgressDialog(getContext());


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading...");
//            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            markers = setAllLocations();
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pinsDoneLoading = true;
            //this method will be running on UI thread

//            pdLoading.dismiss();
        }

    }

//    public LatLng getLocationFromAddress(String strAddress){
//
//        Geocoder coder = new Geocoder(this);
//        List<Address> address;
//        LatLng p1;
//
//        try {
//            address = coder.getFromLocationName(strAddress, 5);
//            if (address == null) {
//                return null;
//            }
//            Address location = address.get(0);
//            location.getLatitude();
//            location.getLongitude();
//
//
//            return p1;
//        }
//        catch (IOException e ) {
//            Log.e(TAG, "Error getting address list");
//        }
//        return null;
//    }



}