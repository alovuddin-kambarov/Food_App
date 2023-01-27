package com.aladdin.foodapp.utils;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.aladdin.foodapp.models.FoodHome;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ArrayToString {


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<FoodHome> rep(List<FoodHome> l) {
        List<FoodHome> unique = l.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(FoodHome::getId))), ArrayList::new));
        return unique;
    }

   }
