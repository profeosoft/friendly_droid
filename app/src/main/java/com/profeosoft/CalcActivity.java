package com.profeosoft;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.profeosoft.adapter.GooglePlacesAutocompleteAdapter;
import com.profeosoft.adapter.PlaceArrayAdapter;
import com.profeosoft.common.Helper;
import com.profeosoft.model.CalcModel;
import com.profeosoft.service.DictsServcies;
import com.profeosoft.service.RestServices;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class CalcActivity extends AppCompatActivity //implements
        //GoogleApiClient.OnConnectionFailedListener,
        //GoogleApiClient.ConnectionCallbacks
        {

    private static final String LOG_TAG = "CalcActivity";
    //private static final int GOOGLE_API_CLIENT_ID = 0;
    //private GoogleApiClient mGoogleApiClient;
    private GooglePlacesAutocompleteAdapter mPlaceFromArrayAdapter;
    private GooglePlacesAutocompleteAdapter mPlaceToArrayAdapter;

    //private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
    //        new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private String actFromTextPlaceId;
    private String actToTextPlaceId;

    private DictsServcies dictsServcies;
    private RestServices restServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize services
        dictsServcies = new DictsServcies();
        restServices = new RestServices();

        super.onCreate(savedInstanceState);

//        mGoogleApiClient = new GoogleApiClient.Builder(CalcActivity.this)
//                .addApi(Places.GEO_DATA_API)
//                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
//                .addConnectionCallbacks(this)
//                .build();

        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.

        setContentView(R.layout.activity_calc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setImageIcon();
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcModel m = new CalcModel();
                double summary = restServices.getSummary(m);
                NumberFormat summaryFormat = new DecimalFormat("#.##");
                CharSequence summaryText = String.format("Costs = %s GBR", summaryFormat.format(summary));
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.summary_toast,
                        (ViewGroup) findViewById(R.id.summary_toast_layout_root));
                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(summaryText + " PlaceId(from) =  " + actFromTextPlaceId);
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        });
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // CalcModel
                CalcModel m = new CalcModel();
                m.setId("");
                m.setUid("");
                m.setData("");
                m.setFrom("");
                m.setTo("");
                m.setPickUpPropertyType("");
                m.setDestStairsLift("");
                m.setPickUpFurnished("");
                m.setDestVanToDoorDistance("");
                m.setPickUpStairsLift("");
                m.setDestAssembling("");
                m.setPickUpVanToDoorDistance("");
                m.setPickUpPacking("");
                m.setPickUpDismantle("");
                m.setPickUpStorage("");
                m.setCreateDate(new Date());
                // Getting summary from server
                TextView tv = (TextView) findViewById(R.id.tvSummary);
                double summary = restServices.getSummary(m);
                NumberFormat summaryFormat = new DecimalFormat("#.##");
                String summaryText = String.format("Costs = %s GBR", summaryFormat.format(summary));
                tv.setText(summaryText);
                Snackbar.make(view, summaryText, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            });
*/
        // Initialize AutoCompleteTextView
        autoCompleteFromText();
        autoCompleteToText();

        // Initialize Tab's
        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("PICK UP");
        spec.setContent(R.id.linearLayout);
        spec.setIndicator("PICK UP");
        host.addTab(spec);
        //Tab 2
        spec = host.newTabSpec("DESTINATION");
        spec.setContent(R.id.linearLayout2);
        spec.setIndicator("DESTINATION");
        host.addTab(spec);
        //Tab 3
        spec = host.newTabSpec("SUMMARY");
        spec.setContent(R.id.tabSummary);
        spec.setIndicator("SUMMARY");
        host.addTab(spec);

        // Loading spinners
        String[] spinnerArr = {"PickUpPropertyType", "DestStairsLift", "PickUpFurnished",
                "DestVanToDoorDistance", "PickUpStairsLift", "DestAssembling",
                "PickUpVanToDoorDistance", "PickUpPacking", "PickUpDismantle",
                "PickUpStorage"};
        for (String s : spinnerArr) {
            addItemsOnSpinner(s);
        }
    }

    private void addItemsOnSpinner(String dictName) {
        Spinner spinner = null;
        List<String> list = null;
        if (dictName.equals(Helper.PICK_UP_PROPERTY_TYPE)) {
            spinner = (Spinner) findViewById(R.id.spinnerPickUpPropertyType);
            list = dictsServcies.getItemsByDictName(Helper.PICK_UP_PROPERTY_TYPE);
        } else if (dictName.equals(Helper.DEST_STAIRS_LIFT)) {
            spinner = (Spinner) findViewById(R.id.spinnerDestStairsLift);
            list = dictsServcies.getItemsByDictName(Helper.DEST_STAIRS_LIFT);
        } else if (dictName.equals(Helper.PICK_UP_FURNISHED)) {
            spinner = (Spinner) findViewById(R.id.spinnerPickUpFurnished);
            list = dictsServcies.getItemsByDictName(Helper.PICK_UP_FURNISHED);
        } else if (dictName.equals(Helper.DEST_VAN_TO_DOOR_DISTANCE)) {
            spinner = (Spinner) findViewById(R.id.spinnerDestVanToDoorDistance);
            list = dictsServcies.getItemsByDictName(Helper.DEST_VAN_TO_DOOR_DISTANCE);
        } else if (dictName.equals(Helper.PICK_UP_STAIRS_LIFT)) {
            spinner = (Spinner) findViewById(R.id.spinnerPickUpStairsLift);
            list = dictsServcies.getItemsByDictName(Helper.PICK_UP_STAIRS_LIFT);
        } else if (dictName.equals(Helper.DEST_ASSEMBLING)) {
            spinner = (Spinner) findViewById(R.id.spinnerDestAssembling);
            list = dictsServcies.getItemsByDictName(Helper.DEST_ASSEMBLING);
        } else if (dictName.equals(Helper.PICK_UP_VAN_TO_DOOR_DISTANCE)) {
            spinner = (Spinner) findViewById(R.id.spinnerPickUpVanToDoorDistance);
            list = dictsServcies.getItemsByDictName(Helper.PICK_UP_VAN_TO_DOOR_DISTANCE);
        } else if (dictName.equals(Helper.PICK_UP_PACKING)) {
            spinner = (Spinner) findViewById(R.id.spinnerPickUpPacking);
            list = dictsServcies.getItemsByDictName(Helper.PICK_UP_PACKING);
        } else if (dictName.equals(Helper.PICK_UP_DISMANTLE)) {
            spinner = (Spinner) findViewById(R.id.spinnerPickUpDismantle);
            list = dictsServcies.getItemsByDictName(Helper.PICK_UP_DISMANTLE);
        } else if (dictName.equals(Helper.PICK_UP_STORAGE)) {
            spinner = (Spinner) findViewById(R.id.spinnerPickUpStorage);
            list = dictsServcies.getItemsByDictName(Helper.PICK_UP_STORAGE);
        }
        if (spinner != null) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }
    }

    private void autoCompleteFromText() {
        try {
            AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.fromText);
            tv.setThreshold(3);
            tv.setOnItemClickListener(mAutocompleteFromTextClickListener);
            mPlaceFromArrayAdapter = new GooglePlacesAutocompleteAdapter(this, android.R.layout.simple_list_item_1);
            tv.setAdapter(mPlaceFromArrayAdapter);
        } catch (Exception ex) {
            Log.e(LOG_TAG,"",ex);
            throw ex;
        }
    }

    private void autoCompleteToText() {
        try {
            AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.toText);
            tv.setThreshold(3);
            tv.setOnItemClickListener(mAutocompleteToTextClickListener);
            mPlaceToArrayAdapter = new GooglePlacesAutocompleteAdapter(this, android.R.layout.simple_list_item_1);
            tv.setAdapter(mPlaceToArrayAdapter);
        } catch (Exception ex) {
            Log.e(LOG_TAG,"",ex);
            throw ex;
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteFromTextClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(LOG_TAG,"position="+ position);
            final GooglePlacesAutocompleteAdapter.PlaceAutocomplete item = mPlaceFromArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
//            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                    .getPlaceById(mGoogleApiClient, placeId);
//            placeResult.setResultCallback(mUpdatePlaceFromDetailsCallback);
//            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

            private AdapterView.OnItemClickListener mAutocompleteToTextClickListener
                    = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(LOG_TAG, "position=" + position);
                    final GooglePlacesAutocompleteAdapter.PlaceAutocomplete item = mPlaceToArrayAdapter.getItem(position);
                    final String placeId = String.valueOf(item.placeId);
                    Log.i(LOG_TAG, "Selected: " + item.description);
                    // https://maps.googleapis.com/maps/api/geocode/json?place_id=ChIJ2eUgeAK6j4ARbn5u_wAGqWA&key=MYKEY
//            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                    .getPlaceById(mGoogleApiClient, placeId);
//            placeResult.setResultCallback(mUpdatePlaceToDetailsCallback);
//            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
                }
            };

    private ResultCallback<PlaceBuffer> mUpdatePlaceFromDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            //mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            //mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            actFromTextPlaceId = String.format("%s", Html.fromHtml(place.getId()));
            //mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            //mWebTextView.setText(place.getWebsiteUri() + "");
            //if (attributions != null) {
            //    mAttTextView.setText(Html.fromHtml(attributions.toString()));
            //}
        }
    };

            private ResultCallback<PlaceBuffer> mUpdatePlaceToDetailsCallback
                    = new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(PlaceBuffer places) {
                    if (!places.getStatus().isSuccess()) {
                        Log.e(LOG_TAG, "Place query did not complete. Error: " +
                                places.getStatus().toString());
                        return;
                    }
                    // Selecting the first object buffer.
                    final Place place = places.get(0);
                    CharSequence attributions = places.getAttributions();

                    //mNameTextView.setText(Html.fromHtml(place.getName() + ""));
                    //mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
                    actToTextPlaceId = String.format("%s", Html.fromHtml(place.getId()));
                    //mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
                    //mWebTextView.setText(place.getWebsiteUri() + "");
                    //if (attributions != null) {
                    //    mAttTextView.setText(Html.fromHtml(attributions.toString()));
                    //}
                }
            };

//    @Override
//    public void onConnected(Bundle bundle) {
//        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
//        Log.i(LOG_TAG, "Google Places API connected.");
//    }

//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
//                + connectionResult.getErrorCode());
//        Toast.makeText(this,
//                "Google Places API connection failed with error code:" +
//                        connectionResult.getErrorCode(),
//                Toast.LENGTH_LONG).show();
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//        mPlaceArrayAdapter.setGoogleApiClient(null);
//        Log.e(LOG_TAG, "Google Places API connection suspended.");
//    }
}
