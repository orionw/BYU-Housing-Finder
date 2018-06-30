package com.orionweller.collegehousing;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HousingList extends AppCompatActivity {

    private ListView listView;
    public static Cursor c;

//    Toolbar myToolbar;
//    private propertiesDataSource datasource;

    CharSequence Titles[]={"List","Map"};
    int Numboftabs =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.housing_list_view);

        Intent intent = getIntent();
        String selectQuery;
        selectQuery = intent.getExtras().getString("sqlQuery");
        Log.v("sqlQuery", selectQuery);

/*
        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
//        animalNames.add("Goat");*/
//
//          This was the old way of reading from a CSV
//        InputStream inputStream = getResources().openRawResource(R.raw.apartments);
//        CSVFile csvFile = new CSVFile(inputStream);
//        List scoreList = csvFile.read();


        //Get the database and send the query, returns a list/dict?
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        c = db.rawQuery(selectQuery, null);
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
        // TODO change from arrayList to list[] to populate recyclerView

//        Bundle args = new Bundle();
//        args.putSerializable("cursor", c);
//        fragA.setArguments(args);

        List apartmentList = get_list_from_query(c);

        // TODO make list look better (see other app)

        Toolbar toolbar ;
        TabLayout tabLayout ;
        final ViewPager viewPager ;
        FragmentAdapterClass fragmentAdapter ;



        //toolbar = (Toolbar) findViewById(R.id.toolbar1);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout1);
        viewPager = (ViewPager) findViewById(R.id.pager1);

        //setSupportActionBar(toolbar);

        tabLayout.addTab(tabLayout.newTab().setText("TAB 1"));
        tabLayout.addTab(tabLayout.newTab().setText("TAB 2"));

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
//
//      RecyclerViewAdapter adapter;
//        // set up the RecyclerView
//        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new RecyclerViewAdapter(this, c);
//        adapter.setClickListener(this);
//        recyclerView.setAdapter(adapter);
//        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),1);
//        recyclerView.addItemDecoration(mDividerItemDecoration);




    }

    private List get_list_from_query(Cursor mCursor) {
        // Don't think I'm going to use this code but keeping it in case for a sec
        List apartmentList = new ArrayList<String>();
        mCursor.moveToFirst();
        while(!mCursor.isAfterLast()) {
            apartmentList.add(mCursor.getString(1)); //add the item
            apartmentList.add(mCursor.getString(1)); //add the item
            apartmentList.add(mCursor.getString(1)); //add the item

            mCursor.moveToNext();
        }
        Log.d("array to list", apartmentList.toString());
        return apartmentList;
    }


}