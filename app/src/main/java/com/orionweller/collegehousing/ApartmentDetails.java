package com.orionweller.collegehousing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static android.content.ContentValues.TAG;


public class ApartmentDetails extends Activity {

    private Context mContext;
    private Activity mActivity;

    private CoordinatorLayout mCLayout;
    private Button mButtonDo;
    private ProgressDialog mProgressDialog;
    private LinearLayout mLLayout;

    private AsyncTask mMyTask;

    // Specify some url to download images
    final URL url1 = stringToURL("http://www.freeimageslive.com/galleries/transtech/informationtechnology/pics/beige_keyboard.jpg");
    final URL url2 = stringToURL("http://www.freeimageslive.com/galleries/transtech/informationtechnology/pics/computer_blank_screen.jpg");
    final URL url3 = stringToURL("http://www.freeimageslive.com/galleries/transtech/informationtechnology/pics/computer_memory_dimm.jpg");
    final URL url4 = stringToURL("http://www.freeimageslive.com/galleries/transtech/informationtechnology/pics/computer_memory.jpg");
    final URL url5 = stringToURL("http://www.freeimageslive.com/galleries/transtech/informationtechnology/pics/ethernet_router.jpg");
    public static List<Bitmap> images;
    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;

    public static boolean hasLoaded = false;
    public static String lastApartment;
    String apartmentName;


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
            setContentView(R.layout.details_start);

            // Get the application context
            mContext = getApplicationContext();
            mActivity = ApartmentDetails.this;
            mLLayout = (LinearLayout) findViewById(R.id.detail_linear);

            // Initialize the progress dialog
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setIndeterminate(false);
            // Progress dialog horizontal style
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // Progress dialog title
            mProgressDialog.setTitle("Getting images");
            // Progress dialog message
            mProgressDialog.setMessage("Please wait, we are downloading the apartment's photos...");
            mProgressDialog.setCancelable(true);
            mMyTask = new DownloadTask()
                    .execute(
                            url1,
                            url2,
                            url3,
                            url4,
                            url5
                    );
            // Set a progress dialog dismiss listener
            mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    // Cancel the AsyncTask
                    mMyTask.cancel(false);
                }
            });
        } else {
            setContentView(R.layout.apartment_details);
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            myPagerAdapter = new MyPagerAdapter(ApartmentDetails.this, images);
            viewPager.setAdapter(myPagerAdapter);
            indicator.setViewPager(viewPager);
            mLLayout = (LinearLayout) findViewById(R.id.real_linear_details);

        }
    }

        // Get the widget reference from XML layout
//        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
//        mButtonDo = (Button) findViewById(R.id.btn_do);
//        mLLayout = (LinearLayout) findViewById(R.id.ll);



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }


//        // Initialize a new click listener for positive button widget
//        mButtonDo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Execute the async task
//                // Don't know what they want
//            }
//        });
//    }
    /*
        First parameter URL for doInBackground
        Second parameter Integer for onProgressUpdate
        Third parameter List<Bitmap> for onPostExecute
     */
    private class DownloadTask extends AsyncTask<URL, Integer, List<Bitmap>> {
        // Before the tasks execution
        protected void onPreExecute() {
            // Display the progress dialog on async task start
            mProgressDialog.show();
            mProgressDialog.setProgress(0);
        }

        // Do the task in background/non UI thread
        protected List<Bitmap> doInBackground(URL... urls) {
            int count = urls.length;
            //URL url = urls[0];
            HttpURLConnection connection = null;
            List<Bitmap> bitmaps = new ArrayList<>();

            // Loop through the urls
            int numberOfPictures = 1;
            for (int i = 0; i < numberOfPictures; i++) {
                String strippedApartmentName = apartmentName.replaceAll("\\s+","");
                String urlString = "http://orionweller.com/photos/" + strippedApartmentName + String.valueOf(i+1) + ".png";
                Log.d("urlString", urlString);
                URL currentURL = stringToURL(urlString);
                // So download the image from this url
                try {
                    // Initialize a new http url connection
                    connection = (HttpURLConnection) currentURL.openConnection();
                    Log.d("Connection", connection.toString());

                    // Connect the http url connection
                    connection.connect();

                    // Get the input stream from http url connection
                    InputStream inputStream = connection.getInputStream();

                    // Initialize a new BufferedInputStream from InputStream
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                    // Convert BufferedInputStream to Bitmap object
                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    Log.d("Bitmap is", bmp.toString());

                    // Add the bitmap to list
                    bitmaps.add(bmp);

                    // Publish the async task progress
                    // Added 1, because index start from 0
                    publishProgress((int) (((i + 1) / (float) numberOfPictures) * 100));
                    if (isCancelled()) {
                        break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Disconnect the http url connection
                    connection.disconnect();
                }
            }
            // Return bitmap list
            Log.d("Bitmap to String", bitmaps.toString());
            return bitmaps;
        }

        // On progress update
        protected void onProgressUpdate(Integer... progress) {
            // Update the progress bar
            mProgressDialog.setProgress(progress[0]);
        }

        // On AsyncTask cancelled
        protected void onCancelled() {
            mProgressDialog.dismiss();
            mProgressDialog = null;
            Snackbar.make(mCLayout, "Task Cancelled.", Snackbar.LENGTH_LONG).show();
        }

        // When all async task done
        protected void onPostExecute(List<Bitmap> result) {
            // Hide the progress dialog
            mProgressDialog.dismiss();
            mProgressDialog = null;

            images = result;

            setContentView(R.layout.apartment_details);
            viewPager = (ViewPager)findViewById(R.id.viewPager);
            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            myPagerAdapter = new MyPagerAdapter(ApartmentDetails.this, images);
            viewPager.setAdapter(myPagerAdapter);
            indicator.setViewPager(viewPager);

            hasLoaded = true;
            mLLayout = (LinearLayout) findViewById(R.id.real_linear_details);


            // Remove all views from linear layout
            //mLLayout.removeAllViews();

        }
    }




    // Custom method to convert string to url
    protected URL stringToURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}