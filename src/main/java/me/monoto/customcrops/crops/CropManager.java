package me.monoto.customcrops.crops;

import java.util.ArrayList;

public class CropManager {
    public static ArrayList<Crop> cropList = new ArrayList<>();

    public static ArrayList<Crop> getCropList() {
        return cropList;
    }

    public static void addCrop(Crop crop) {
        cropList.add(crop);
    }
}
