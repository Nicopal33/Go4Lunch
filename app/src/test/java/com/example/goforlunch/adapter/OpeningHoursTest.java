package com.example.goforlunch.adapter;

import com.example.goforlunch.R;
import com.example.goforlunch.model.restaurants.OpeningHours;
import com.example.goforlunch.model.restaurants.ResultRestau;

import junit.framework.TestCase;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class OpeningHoursTest extends TestCase {

    public void testGetRestOpeningHours() {

        OpeningHours openingHours = null;
        ResultRestau resultRestau = new ResultRestau();
        resultRestau.setOpeningHours(openingHours);
        ListRestRecyclerViewAdapter adapter = new ListRestRecyclerViewAdapter(null);

        int resultOpeningHours = adapter.getRestOpeningHours(resultRestau);
        assertEquals(R.string.unknown, resultOpeningHours);


    }
}