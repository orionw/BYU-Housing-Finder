package com.orionweller.collegehousing;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        // find the index of the columns we need
        int name_index = mData.getColumnIndex("name");
        int price_index = mData.getColumnIndex("price");
        int distance_index = mData.getColumnIndex("distance");

        // get the info we need
        String apartment_name = mData.getString(name_index);
        String apartment_price = "$" + mData.getString(price_index) + " a month";
        String apartment_distance = mData.getString(distance_index) + " miles from campus";

        // set the layout
        holder.ComplexName.setText(apartment_name);
        holder.ComplexPrice.setText(apartment_price);
        holder.ComplexDistance.setText(apartment_distance);
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

        ViewHolder(View itemView) {
            super(itemView);
            ComplexName = itemView.findViewById(R.id.title);
            ComplexPrice = itemView.findViewById(R.id.price);
            ComplexDistance = itemView.findViewById(R.id.address);
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
        int name_index = mData.getColumnIndex("name");
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

