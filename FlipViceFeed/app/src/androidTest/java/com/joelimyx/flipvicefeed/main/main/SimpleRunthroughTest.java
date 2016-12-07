package com.joelimyx.flipvicefeed.main.main;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.joelimyx.flipvicefeed.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SimpleRunthroughTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void simpleRunthroughTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.begin_button), withText("Let's begin"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withText("Continue"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.testList), withText("Finish"), isDisplayed()));
        appCompatButton3.perform(click());

        onView(withId(R.id.main_recyclerview))
        .perform(actionOnItemAtPosition(1, click()));

        ViewInteraction shareButton = onView(
                allOf(withId(R.id.fb_share_button), withText("Share"), isDisplayed()));
        shareButton.perform(click());

        ViewInteraction imageView = onView(
                allOf(withClassName(is("android.widget.ImageView")), isDisplayed()));
        imageView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.toolbar_layout)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open Nav Drawer"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.app_bar)))),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Tech"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Notification Setting"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction switchCompat = onView(
                allOf(withId(R.id.notifiction_switch), isDisplayed()));
        switchCompat.perform(click());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.wed_checkbox), isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.save_button), withText("Save"), isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton5.perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText("Topic Filter"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatImageButton6 = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton6.perform(click());

    }

}
