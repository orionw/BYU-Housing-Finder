package com.orionweller.collegehousing;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.orionweller.collegehousing.ApartmentTabView.apartmentList;
import static com.orionweller.collegehousing.ApartmentTabView.c;

public class Tab_1_Apartment_List extends Fragment implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    DividerItemDecoration mDividerItemDecoration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // get housing activity view
        View rootView = inflater.inflate(R.layout.housing_recyclerview_tab_1, container, false);

        // set up the RecyclerView
        recyclerView = rootView.findViewById(R.id.apartment_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(getContext(), c);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // Add the divider
        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),1);
        recyclerView.addItemDecoration(mDividerItemDecoration);

        return rootView;

    }

    @Override
    public void onItemClick(View view, int position) {
        // says what you clicked on
        Toast.makeText(getActivity(), "You clicked " + adapter.getItem(position) +
                " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ApartmentDetails.class);
        for (Apartment apt : apartmentList) {
            if (adapter.getItem(position).equals(apt.name)) {
                // get the price from the edit text
                // TODO: remove this
                TextView editTextPrice = (TextView)getActivity().findViewById(R.id.price);
                apt.price = Integer.valueOf(editTextPrice.getText().toString().substring(1,4));

                intent.putExtra("Apartment", apt);
                startActivity(intent);
            }
        }
    }
}
