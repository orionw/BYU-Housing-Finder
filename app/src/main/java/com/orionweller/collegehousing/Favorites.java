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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static com.orionweller.collegehousing.ApartmentTabView.c;


public class Favorites extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    DividerItemDecoration mDividerItemDecoration;
    public static ArrayList<String> apartmentList;
    Cursor favoritesData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get favorites activity view
        setContentView(R.layout.favorites_recyclerview);

        //  Get the database and send the query, returns a cursor
        String selectQuery = "SELECT * FROM favorites";
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        favoritesData = db.rawQuery(selectQuery, null);
        Log.v("Cursor Object Favs", DatabaseUtils.dumpCursorToString(favoritesData));

        // Get list of names and addresses

        //apartmentList = get_address_list_from_query(favoritesData);

        // set up the RecyclerView
        recyclerView = this.findViewById(R.id.favorites_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, favoritesData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // Add the divider
        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),1);
        recyclerView.addItemDecoration(mDividerItemDecoration);

    }

    @Override
    public void onItemClick(View view, int position) {
        // says what you clicked on
        Toast.makeText(this, "You clicked " + adapter.getItem(position) +
                " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ApartmentDetails.class);
        intent.putExtra("apartmentName", adapter.getItem(position));
        startActivity(intent);
    }
}

