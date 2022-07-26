package com.android.riccardo.myapplication.musictext;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {

        mainActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch() {

    }

    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }


}