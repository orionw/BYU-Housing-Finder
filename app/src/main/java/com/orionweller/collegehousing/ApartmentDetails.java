package com.orionweller.collegehousing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.widget.TableRow.LayoutParams;

import me.relex.circleindicator.CircleIndicator;


public class ApartmentDetails extends AppCompatActivity{

    private Context mContext;
    private Activity mActivity;

    private CoordinatorLayout mCLayout;
    private Button mButtonDo;
    private ProgressDialog mProgressDialog;
    private LinearLayout mLLayout;

    private AsyncTask mMyTask;

    public static List<Bitmap> images;
    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;

    public static boolean hasLoaded = false;
    public static String lastApartment;
    String apartmentName;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private Cursor data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (lastApartment == null) {
            lastApartment = "";
        }

        Intent intent = getIntent();
        apartmentName = intent.getExtras().getString("apartmentName");
        if (!lastApartment.equals(apartmentName)) {
            hasLoaded = false;
            lastApartment = apartmentName;
        }

        if (!hasLoaded) {
            setContentView(R.layout.apartment_details);
            // set up drawer
            mDrawerList = (ListView)findViewById(R.id.navListDetails);
            mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_details);
            mActivityTitle = getTitle().toString();

            addDrawerItems();
            setupDrawer();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            // Set up database and info
            DataBaseHelper helper = new DataBaseHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();
            String query = "SELECT * FROM apartments1 WHERE name=\"" + apartmentName +"\"";
            data = db.rawQuery(query, null);
            Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(data));
            initInfo();

            // Set up layout stuff
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            myPagerAdapter = new MyPagerAdapter(ApartmentDetails.this, apartmentName);
            viewPager.setAdapter(myPagerAdapter);
            indicator.setViewPager(viewPager);
            hasLoaded = true;
            mLLayout = (LinearLayout) findViewById(R.id.real_linear_details);

            // Get the application context
            mContext = getApplicationContext();
            mActivity = ApartmentDetails.this;

//            // Initialize the progress dialog
//            mProgressDialog = new ProgressDialog(mActivity);
//            mProgressDialog.setIndeterminate(false);
//            // Progress dialog horizontal style
//            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            // Progress dialog title
//            mProgressDialog.setTitle("Getting images");
//            // Progress dialog message
//            mProgressDialog.setMessage("Please wait, we are downloading the apartment's photos...");
//            mProgressDialog.setCancelable(true);
//            mMyTask = new DownloadTask()
//                    .execute(
//                            url1,
//                            url2,
//                            url3,
//                            url4,
//                            url5
//                    );
//            // Set a progress dialog dismiss listener
//            mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialogInterface) {
//                    // Cancel the AsyncTask
//                    mMyTask.cancel(false);
//                }
//            });


        } else {
            setContentView(R.layout.apartment_details);
            // set up action bar
            mDrawerList = (ListView)findViewById(R.id.navListDetails);
            mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_details);
            mActivityTitle = getTitle().toString();

            addDrawerItems();
            setupDrawer();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            myPagerAdapter = new MyPagerAdapter(ApartmentDetails.this, apartmentName);
            viewPager.setAdapter(myPagerAdapter);
            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(viewPager);
            mLLayout = (LinearLayout) findViewById(R.id.real_linear_details);

        }
    }

    // Get the widget reference from XML layout
//        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
//        mButtonDo = (Button) findViewById(R.id.btn_do);
//        mLLayout = (LinearLayout) findViewById(R.id.ll);



    private void addDrawerItems() {
        String[] optionsMenu = { "Log In", "Favorites", "Settings" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionsMenu);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ApartmentDetails.this, "I haven't implemented this yet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
//    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.back_to_list) {
            //finish();
            //Intent intent = new Intent(this, ApartmentTabView.class);
//            NavUtils.navigateUpFromSameTask(this);

            Intent intent = new Intent(this,
                    ApartmentTabView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initInfo(){
        TableLayout table = (TableLayout) findViewById(R.id.table_layout);
        List<Integer> positionList = new ArrayList<>();
        int name_index = data.getColumnIndex("name");
//        int tenants_index = data.getColumnIndex("tenants");
//        int bathrooms_index = data.getColumnIndex("bathrooms");
        int address_index = data.getColumnIndex("address");
//        int website_index = data.getColumnIndex("website");
//        int gender_index = data.getColumnIndex("gender");
        int year_price_index = data.getColumnIndex("price");
//        int fall_winter_price_index = data.getColumnIndex("fall-winter-price");
//        int deposit_index = data.getColumnIndex("deposit");
        ArrayList<String> nameList = new ArrayList<>();
        nameList.add("Name:");
        nameList.add("Address:  ");
        nameList.add("Price:");

        positionList.add(name_index);
        positionList.add(address_index);
        positionList.add(year_price_index);
        data.moveToFirst();

        for (int i = 0; i <positionList.size(); i++) {

            TableRow row= new TableRow(this);
            //TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            //row.setLayoutParams(lp);
            TextView option = new TextView(this);
            TextView param = new TextView(this);
            // Get name of param for table
            option.setText(nameList.get(i));
            // Get cursors result for table
            param.setText(data.getString(positionList.get(i)));
            // set table
            row.addView(option);
            row.addView(param);
            table.addView(row,i+1);
            Log.d("Table Row is", table.toString());
            Log.d("Table  is", row.toString());
        }
    }
}



//
//
//
//
//
//
//
//
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//            mProgressDialog = null;
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//            mProgressDialog = null;
//        }
//    }
//
//
////        // Initialize a new click listener for positive button widget
////        mButtonDo.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                // Execute the async task
////                // Don't know what they want
////            }
////        });
////    }
//    /*
//        First parameter URL for doInBackground
//        Second parameter Integer for onProgressUpdate
//        Third parameter List<Bitmap> for onPostExecute
//     */
//private class DownloadTask extends AsyncTask<URL, Integer, List<Bitmap>> {
//    // Before the tasks execution
//    protected void onPreExecute() {
//        // Display the progress dialog on async task start
//        mProgressDialog.show();
//        mProgressDialog.setProgress(0);
//    }
//
//    // Do the task in background/non UI thread
//    protected List<Bitmap> doInBackground(URL... urls) {
//        int count = urls.length;
//        //URL url = urls[0];
//        HttpURLConnection connection = null;
//        List<Bitmap> bitmaps = new ArrayList<>();
//
//        // Loop through the urls
//        int numberOfPictures = 1;
//        for (int i = 0; i < numberOfPictures; i++) {
//            String strippedApartmentName = apartmentName.replaceAll("\\s+", "");
//            String currentURL = "http://orionweller.com/photos/" + strippedApartmentName + String.valueOf(i + 1) + ".png";
////                Log.d("urlString", urlString);
////                URL currentURL = (urlString);
//            // So download the image from this url
//            try {
//                // Initialize a new http url connection
////                    connection = (HttpURLConnection) currentURL.openConnection();
////                    Log.d("Connection", connection.toString());
////
////                    // Connect the http url connection
////                    connection.connect();
////
////                    // Get the input stream from http url connection
////                    InputStream inputStream = connection.getInputStream();
////
////                    // Initialize a new BufferedInputStream from InputStream
////                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
////
////                    // Convert BufferedInputStream to Bitmap object
////                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
////                    Log.d("Bitmap is", bmp.toString());
////
////                    // Add the bitmap to list
////                    bitmaps.add(bmp);
//
//                // Publish the async task progress
//                // Added 1, because index start from 0
//                publishProgress((int) (((i + 1) / (float) numberOfPictures) * 100));
//                if (isCancelled()) {
//                    break;
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                // Disconnect the http url connection
//                connection.disconnect();
//            }
//        }
//        // Return bitmap list
//        Log.d("Bitmap to String", bitmaps.toString());
//        return bitmaps;
//    }
//
//    // On progress update
//    protected void onProgressUpdate(Integer... progress) {
//        // Update the progress bar
//        mProgressDialog.setProgress(progress[0]);
//    }
//
//    // On AsyncTask cancelled
//    protected void onCancelled() {
//        mProgressDialog.dismiss();
//        mProgressDialog = null;
//        Snackbar.make(mCLayout, "Task Cancelled.", Snackbar.LENGTH_LONG).show();
//    }
//
//    // When all async task done
//    protected void onPostExecute(List<Bitmap> result) {
//        // Hide the progress dialog
//        mProgressDialog.dismiss();
//        mProgressDialog = null;
//
//        images = result;
//
//        setContentView(R.layout.apartment_details);
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
//        myPagerAdapter = new MyPagerAdapter(ApartmentDetails.this, apartmentName);
//        viewPager.setAdapter(myPagerAdapter);
//        indicator.setViewPager(viewPager);
//
//        hasLoaded = true;
//        mLLayout = (LinearLayout) findViewById(R.id.real_linear_details);
//
//
//        // Remove all views from linear layout
//        //mLLayout.removeAllViews();
//
//    }
//}

