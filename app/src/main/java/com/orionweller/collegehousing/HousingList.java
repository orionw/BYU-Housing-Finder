package com.orionweller.collegehousing;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class HousingList extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter adapter;
    private ListView listView;
    DividerItemDecoration mDividerItemDecoration;

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
        Cursor c = db.rawQuery(selectQuery, null);
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
        // TODO change from arrayList to list[] to populate recyclerView

        List apartmentList = get_list_from_query(c);

        // TODO make list look better (see other app)

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, c);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),1);
        recyclerView.addItemDecoration(mDividerItemDecoration);


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

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}