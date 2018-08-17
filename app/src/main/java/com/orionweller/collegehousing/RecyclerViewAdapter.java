package com.orionweller.collegehousing;

import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    // using a cursor is kinda ugly but it saves space and reduces complexity in the long run
    private Cursor mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecyclerViewAdapter(Context context, Cursor data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.apartment_individual_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        //  Move the cursor to the current row
        mData.moveToPosition(position);

        int name_index = mData.getColumnIndex("Name");

        String apartment_name = mData.getString(name_index);

        String strippedApartmentName = apartment_name.replaceAll("\\s+", "");
        String urlString = "http://orionweller.com/photos/" + strippedApartmentName + "1.jpg";
        Log.d("urlString", urlString);
        Picasso.get().setLoggingEnabled(true);
        Picasso.get()
                .load(urlString)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.noimage)
                .into(holder.ComplexPicture);

        // find the index of the columns we need
        // in Tab_1_list
        try {
            int longitude_index = mData.getColumnIndex("Longitude");
            int latitude_index = mData.getColumnIndex("Latitude");
            double latitude = Double.parseDouble(mData.getString(latitude_index));
            double longitude = Double.parseDouble(mData.getString(longitude_index));
            // get location and distance info
            Location BYU = new Location("");
            BYU.setLatitude(40.2518);
            BYU.setLongitude(-111.6493);

            Location currentLocation = new Location("");
            currentLocation.setLatitude(latitude);
            currentLocation.setLongitude(longitude);

            double metersToMilesConvert = 0.000621371;
            double distanceInMeters = currentLocation.distanceTo(BYU) * metersToMilesConvert;
            String apartment_distance = String.format("%.2f", distanceInMeters) + " miles from campus";
            holder.ComplexDistance.setText(apartment_distance);

        }
        // if in Favorites
        catch(IllegalStateException error) {
            int distance_index = mData.getColumnIndex("Distance");
            if (distance_index == -1) {
                distance_index = mData.getColumnIndex("distance");
            }
            double distanceInMeters = Double.parseDouble(mData.getString(distance_index));
            String apartment_distance = String.format("%.2f", distanceInMeters) + " miles from campus";
            holder.ComplexDistance.setText(apartment_distance);
        }

        // get price info (TODO refactor this)
        int price_index = mData.getColumnIndex("Rent_shared_room_year");
        String apartment_price = "$" + mData.getString(price_index) + " a month";

        if (TextUtils.isEmpty(mData.getString(price_index))){
            price_index = mData.getColumnIndex("Rent_private_room_year");
            apartment_price = "$" + mData.getString(price_index) + " a month";
        }

        if (TextUtils.isEmpty(mData.getString(price_index))){
            price_index = mData.getColumnIndex("Rent_shared_room_fall_winter");
            apartment_price = "$" + mData.getString(price_index) + " a month";

        }
        if (TextUtils.isEmpty(mData.getString(price_index))){
            price_index = mData.getColumnIndex("Rent_private_room_fall_winter");
            apartment_price = "$" + mData.getString(price_index) + " a month";
        }
        if (TextUtils.isEmpty(mData.getString(price_index))) {
            price_index = mData.getColumnIndex("price");
            apartment_price = "$" + mData.getString(price_index) + " a month";
        }


        // get the info we need


        // set the layout
        holder.ComplexName.setText(apartment_name);
        holder.ComplexPrice.setText(apartment_price);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.getCount();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ComplexName;
        TextView ComplexPrice;
        TextView ComplexDistance;
        ImageView ComplexPicture;

        ViewHolder(View itemView) {
            super(itemView);
            ComplexName = itemView.findViewById(R.id.title);
            ComplexPrice = itemView.findViewById(R.id.price);
            ComplexDistance = itemView.findViewById(R.id.address);
            ComplexPicture = itemView.findViewById(R.id.apartment_picture);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        mData.moveToPosition((id));
        int name_index = mData.getColumnIndex("Name");
        return mData.getString(name_index);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
