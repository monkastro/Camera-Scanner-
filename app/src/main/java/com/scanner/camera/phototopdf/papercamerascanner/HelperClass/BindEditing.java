package com.scanner.camera.phototopdf.papercamerascanner.HelperClass;


import com.scanner.camera.phototopdf.papercamerascanner.Model.ColorModel;
import com.scanner.camera.phototopdf.papercamerascanner.Model.FontStyleModel;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.util.ArrayList;

public class BindEditing {
    public static ArrayList<ColorModel> bindTextColor() {
        ArrayList<ColorModel> arrayList = new ArrayList<>();
        arrayList.add(new ColorModel("#ffffff", false));
        arrayList.add(new ColorModel("#000000", true));
        arrayList.add(new ColorModel("#FF8A80", false));
        arrayList.add(new ColorModel("#B388FF", false));
        arrayList.add(new ColorModel("#1E88E5", false));
        arrayList.add(new ColorModel("#03A9F4", false));
        arrayList.add(new ColorModel("#00BCD4", false));
        arrayList.add(new ColorModel("#009688", false));
        arrayList.add(new ColorModel("#69F0AE", false));
        arrayList.add(new ColorModel("#B2FF59", false));
        arrayList.add(new ColorModel("#FFD180", false));
        arrayList.add(new ColorModel("#9E9E9E", false));
        arrayList.add(new ColorModel("#B0BEC5", false));
        arrayList.add(new ColorModel("#808000", false));
        arrayList.add(new ColorModel("#00FF00", false));
        arrayList.add(new ColorModel("#00FFFF", false));
        arrayList.add(new ColorModel("#FF00FF", false));
        arrayList.add(new ColorModel("#000080", false));
        return arrayList;
    }

    public static ArrayList<FontStyleModel> bindFontStyle() {
        ArrayList<FontStyleModel> arrayList = new ArrayList<>();
        arrayList.add(new FontStyleModel(R.font.alexbrush, "AlexBrush", false));
        arrayList.add(new FontStyleModel(R.font.alisandra, "alisandra", false));
        arrayList.add(new FontStyleModel(R.font.alsscrp, "alsscrp", false));
        arrayList.add(new FontStyleModel(R.font.andromeda, "andromeda", false));
        arrayList.add(new FontStyleModel(R.font.bigdey, "bigdey", false));
        arrayList.add(new FontStyleModel(R.font.blackjack, "blackjack", false));
        arrayList.add(new FontStyleModel(R.font.chopinscript, "ChopinScript", false));
        arrayList.add(new FontStyleModel(R.font.cinzeldecorative, "cinzeldecorative", false));
        arrayList.add(new FontStyleModel(R.font.cookie, "cookie", false));
        arrayList.add(new FontStyleModel(R.font.cooldots, "cooldots", false));
        arrayList.add(new FontStyleModel(R.font.dancingscript, "dancingscript", false));
        arrayList.add(new FontStyleModel(R.font.exotic350btbold, "exotic350btbold", false));
        arrayList.add(new FontStyleModel(R.font.feelinglovely, "lovely", false));
        arrayList.add(new FontStyleModel(R.font.javatext, "javatext", false));
        arrayList.add(new FontStyleModel(R.font.josefinsans, "josefinsans", false));
        arrayList.add(new FontStyleModel(R.font.majalla, "majalla", false));
        arrayList.add(new FontStyleModel(R.font.nautilus, "nautilus", false));
        return arrayList;
    }
}
