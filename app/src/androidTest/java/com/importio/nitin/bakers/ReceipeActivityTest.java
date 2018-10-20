package com.importio.nitin.bakers;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ReceipeActivityTest {
    public static final String INGREDIENTS = "Ingredients:";

    @Rule
    public ActivityTestRule<Home> activityTestRule = new ActivityTestRule<>(Home.class);

    @Test
    public void clickReceipe_opensItemListActivity() {
        //ItemListActivity contains a text view at top with Ingredients: as its text
        onView(ViewMatchers.withId(R.id.receipe_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withText(INGREDIENTS)).check(matches(isDisplayed()));
    }
}
