package com.orionweller.collegehousing;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static com.orionweller.collegehousing.ApartmentTabView.c;


public class Favorites extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    DividerItemDecoration mDividerItemDecoration;
    public static ArrayList<String> apartmentList;
    Cursor favoritesData;
    enum ButtonsState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }
    private ButtonsState buttonShowedState = ButtonsState.GONE;

    private RectF buttonInstance = null;

    private RecyclerView.ViewHolder currentItemViewHolder = null;

//    private SwipeControllerActions buttonsActions = null;

    private static final float buttonWidth = 300;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get favorites activity view
        setContentView(R.layout.favorites_recyclerview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //  Get the database and send the query, returns a cursor
        favoritesData = getFavoritesSQLQuery();

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

        final SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                favoritesData.moveToPosition((position));
                int name_index = favoritesData.getColumnIndex("name");
                String name =  favoritesData.getString(name_index);

                DataBaseHelper helper = new DataBaseHelper(Favorites.this);
                SQLiteDatabase sqldatabase = helper.getWritableDatabase();
                sqldatabase.execSQL("DELETE FROM favorites WHERE NAME='" + name + "'");
                sqldatabase.close();

                adapter = new RecyclerViewAdapter(Favorites.this, getFavoritesSQLQuery());
                adapter.setClickListener(Favorites.this);
                recyclerView.setAdapter(adapter);

                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });


//            @Override
//            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
//                final int position = viewHolder.getAdapterPosition(); //get position which is swipe
//
//                if (direction == ItemTouchHelper.LEFT) {    //if swipe left
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(Favorites.this); //alert for confirm to delete
//                    builder.setMessage("Are you sure you want to remove this apartment?");    //set message
//
//                    builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() { //when click on DELETE
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            adapter.notifyItemRemoved(position);    //item removed from recylcerview
//
//                            favoritesData.moveToPosition((position));
//                            int name_index = favoritesData.getColumnIndex("name");
//                            String name =  favoritesData.getString(name_index);
//
//                            DataBaseHelper helper = new DataBaseHelper(Favorites.this);
//                            SQLiteDatabase sqldatabase = helper.getWritableDatabase();
//                            sqldatabase.execSQL("DELETE FROM favorites WHERE NAME='" + name + "'");
//                            sqldatabase.close();
////                            list.remove(position);  //then remove item
//
//                            return;
//                        }
//                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            adapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
//                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
//                            return;
//                        }
//                    }).show();  //show alert dialog
//                }
//            }
//        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView); //set swipe to recylcerview




    }

//    /**
//     * callback when recycler view is swiped
//     * item will be removed on swiped
//     * undo option will be provided in snackbar to restore the item
//     */
//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
//        if (viewHolder instanceof CartListAdapter.MyViewHolder) {
//            // get the removed item name to display it in snack bar
//            String name = cartList.get(viewHolder.getAdapterPosition()).getName();
//
//            // backup of removed item for undo purpose
//            final Item deletedItem = adapter.get(viewHolder.getAdapterPosition());
//            final int deletedIndex = viewHolder.getAdapterPosition();
//
//            // remove the item from recycler view
//            adapter.removeItem(viewHolder.getAdapterPosition());
//
//            // showing snack bar with Undo option
//            Snackbar snackbar = Snackbar
//                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // undo is selected, restore the deleted item
//                    adapter.restoreItem(deletedItem, deletedIndex);
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();
//        }
//    }
//

// attaching the touch helper to recycler view
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        if (id == R.id.back_to_list) {
//            //finish();
//            //Intent intent = new Intent(this, ApartmentTabView.class);
////            NavUtils.navigateUpFromSameTask(this);
//
//            Intent intent = new Intent(this,
//                    ApartmentTabView.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//            return true;
//        }
//        return true;
//    }

    @Override
    public void onItemClick(View view, int position) {
        // says what you clicked on
        Toast.makeText(this, "You clicked " + adapter.getItem(position) +
                " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ApartmentDetails.class);
        intent.putExtra("apartmentName", adapter.getItem(position));
        startActivity(intent);
    }

    public Cursor getFavoritesSQLQuery() {
        String selectQuery = "SELECT * FROM favorites";
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor favorites = db.rawQuery(selectQuery, null);
        Log.v("Cursor Object Favs", DatabaseUtils.dumpCursorToString(favoritesData));
        return favorites;
    }
}

