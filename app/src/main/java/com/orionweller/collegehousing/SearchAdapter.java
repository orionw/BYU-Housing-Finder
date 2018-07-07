//package com.orionweller.collegehousing;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
//    private ArrayList<String> mArrayList;
//    private ArrayList<String> mFilteredList;
//
//    public SearchAdapter(ArrayList<String> arrayList) {
//        mArrayList = arrayList;
//        mFilteredList = arrayList;
//    }
//
//    @Override
//    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.housing_recyclerview_tab_1, viewGroup, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(SearchAdapter.ViewHolder viewHolder, int i) {
//
//        viewHolder.ComplexDistance.setText(mFilteredList.get(i));
//        viewHolder.ComplexName.setText(mFilteredList.get(i));
//        viewHolder.ComplexPrice.setText(mFilteredList.get(i));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mFilteredList.size();
//    }
//
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        private TextView ComplexName,ComplexPrice,ComplexDistance;
//        public ViewHolder(View view) {
//            super(view);
//
//            ComplexName = itemView.findViewById(R.id.title);
//            ComplexPrice = itemView.findViewById(R.id.price);
//            ComplexDistance = itemView.findViewById(R.id.address);
//
//        }
//    }
//
//}