package com.profeosoft;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.friendly.ps.friendly.R;
import com.profeosoft.common.Helper;
import com.profeosoft.service.DictsServcies;

import java.util.ArrayList;
import java.util.List;

public class CalcActivity extends AppCompatActivity {

    private DictsServcies dictsServcies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize services
        dictsServcies = new DictsServcies();

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.

        setContentView(R.layout.activity_calc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Loading spinners
        String[] spinnerArr = {"PickUpPropertyType", "DestStairsLift", "PickUpFurnished",
                "DestVanToDoorDistance", "PickUpStairsLift", "DestAssembling",
                "PickUpVanToDoorDistance", "PickUpPacking", "PickUpDismantle",
                "PickUpStorage", "PickUpSurvey"};
        for(String s : spinnerArr) {
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
        } else if (dictName.equals(Helper.PICK_UP_SURVEY)) {
            spinner = (Spinner) findViewById(R.id.spinnerPickUpSurvey);
            list = dictsServcies.getItemsByDictName(Helper.PICK_UP_SURVEY);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}
