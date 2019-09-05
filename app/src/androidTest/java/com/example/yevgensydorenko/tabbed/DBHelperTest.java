package com.example.yevgensydorenko.tabbed;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DBHelperTest {

    DBHelper dbHelper;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DBHelper(context, "testDB");
        String[][] inputRow = {{"date", "2019-08-08"}, {"time", "40"}, {"temperature", "23"}};
        dbHelper.insert(dbHelper, inputRow, "statistic");
    }

    @After
    public void tearDown() throws Exception {
        dbHelper.clearTable( "statistic");
    }

    @Test
    public void testSelect() {
        String[] expected = {"40"};
        String[] output;
        output = dbHelper.select(dbHelper, "statistic", "time");
        assertArrayEquals(expected, output);

    }
}
