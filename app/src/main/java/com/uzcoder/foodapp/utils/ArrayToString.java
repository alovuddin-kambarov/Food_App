package com.uzcoder.foodapp.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.stream.Collectors;

public class ArrayToString {


    @RequiresApi(api = Build.VERSION_CODES.N)
    public String rep(List<String> l) {
        String s = l.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining("||", "", ""));
        return s;
    }


}
