package com.android.riccardo.myapplication.musictext;

import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateUserActivityTest {
    @Rule
    public ActivityTestRule<CreateUserActivity> c = new ActivityTestRule<CreateUserActivity>(CreateUserActivity.class);
    private CreateUserActivity createUserActivity = null;

    @Before
    public void setUp() throws Exception {
        createUserActivity = c.getActivity();
    }

    @Test
    public void LaunchTest() {
        String user = "Luca";
    }

    @After
    public void tearDown() throws Exception {
        createUserActivity = null;
    }

}