package com.profeosoft.common;

/**
 * Created by profeosoft@gmail.com on 2016-04-23.
 */
public class Helper {
    // Spinners dict names
    public static String PICK_UP_PROPERTY_TYPE = "PickUpPropertyType";
    public static String DEST_STAIRS_LIFT = "DestStairsLift";
    public static String PICK_UP_FURNISHED = "PickUpFurnished";
    public static String DEST_VAN_TO_DOOR_DISTANCE = "DestVanToDoorDistance";
    public static String PICK_UP_STAIRS_LIFT = "PickUpStairsLift";
    public static String DEST_ASSEMBLING = "DestAssembling";
    public static String PICK_UP_VAN_TO_DOOR_DISTANCE = "PickUpVanToDoorDistance";
    public static String PICK_UP_PACKING = "PickUpPacking";
    public static String PICK_UP_DISMANTLE = "PickUpDismantle";
    public static String PICK_UP_STORAGE = "PickUpStorage";

    // Version 0.1
    public static float calculateDistance(float lat1, float lon1, float lat2, float lon2) {
        final int earthRadius = 6371;

        float dLat = (float) Math.toRadians(lat2 - lat1);
        float dLon = (float) Math.toRadians(lon2 - lon1);
        float a =
                (float) (Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2));
        float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
        float d = earthRadius * c;
        return d;
    }
}