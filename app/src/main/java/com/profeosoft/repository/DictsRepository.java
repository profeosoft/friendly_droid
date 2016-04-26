package com.profeosoft.repository;

import com.profeosoft.common.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by biuro_000 on 2016-04-23.
 */
public class DictsRepository {

    public DictsRepository() {
    }

    public List<String> getItemsByDictName(String dictName) {
        try {
            List<String> list = new ArrayList<String>();
            if (dictName.equals(Helper.PICK_UP_PROPERTY_TYPE)) {
                list.add("Flat");
                list.add("Studio flat");
                list.add("One bed flat");
                list.add("Two bed flat");
                list.add("Three bed flat");
                list.add("Four bed flat");
                list.add("Five");
                list.add("House like Flat 1-5");
                list.add("Flat share");
                list.add("Commercial property");
            } else if (dictName.equals(Helper.DEST_STAIRS_LIFT)) {
                list.add("Stairs");
                list.add("Lift");
            } else if (dictName.equals(Helper.PICK_UP_FURNISHED)) {
                list.add("Furnished");
                list.add("Part");
                list.add("Unfurnished");
            } else if (dictName.equals(Helper.DEST_VAN_TO_DOOR_DISTANCE)) {
                list.add("less than 15yard");
                list.add("more than 15yard");
            } else if (dictName.equals(Helper.PICK_UP_STAIRS_LIFT)) {
                list.add("Stairs");
                list.add("Lift");
            } else if (dictName.equals(Helper.DEST_ASSEMBLING)) {
                list.add("Yes");
                list.add("No");
            } else if (dictName.equals(Helper.PICK_UP_VAN_TO_DOOR_DISTANCE)) {
                list.add("less than 15yard");
                list.add("more than 15yard");
            } else if (dictName.equals(Helper.PICK_UP_PACKING)) {
                list.add("Yes");
                list.add("No");
            } else if (dictName.equals(Helper.PICK_UP_DISMANTLE)) {
                list.add("Yes");
                list.add("No");
            } else if (dictName.equals(Helper.PICK_UP_STORAGE)) {
                list.add("Yes");
                list.add("No");
            }
            return list;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
