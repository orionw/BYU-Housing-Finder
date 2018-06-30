package com.orionweller.collegehousing;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ApartmentTabView extends AppCompatActivity {

    // will contain the results of the SQL query
    public static Cursor c;
    public static ArrayList<String> apartmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

        Intent intent = getIntent();
        String selectQuery;
        selectQuery = intent.getExtras().getString("sqlQuery");
        Log.v("sqlQuery", selectQuery);

//         This was the old way of reading from a CSV
//        InputStream inputStream = getResources().openRawResource(R.raw.apartments);
//        CSVFile csvFile = new CSVFile(inputStream);
//        List scoreList = csvFile.read();

        //  Get the database and send the query, returns a cursor
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        c = db.rawQuery(selectQuery, null);
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));

        // Get list of names and addresses

        apartmentList = get_list_from_query(c);

        // Set up tabs
        Toolbar toolbar ;
        TabLayout tabLayout ;
        final ViewPager viewPager ;
        FragmentAdapterClass fragmentAdapter ;

        //toolbar = (Toolbar) findViewById(R.id.toolbar1);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout1);
        viewPager = (ViewPager) findViewById(R.id.pager1);

        //setSupportActionBar(toolbar);

        // name the tabs
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.addTab(tabLayout.newTab().setText("Map"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        fragmentAdapter = new FragmentAdapterClass(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

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

    private ArrayList<String> get_list_from_query(Cursor mCursor) {
        // Don't think I'm going to use this code but keeping it in case for a sec
        ArrayList builder = new ArrayList<String>();

        mCursor.moveToFirst();
        int name_index = mCursor.getColumnIndex("name");
        int address_index = mCursor.getColumnIndex("address");

        while(!mCursor.isAfterLast()) {
            builder.add(mCursor.getString(name_index)); //add the item
            builder.add(mCursor.getString(address_index)); //add the item
            mCursor.moveToNext();
        }
        Log.d("array to list", builder.toString());
        return builder;
    }


}