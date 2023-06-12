package algonquin.cst2335.huyn0116;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class testFindingNumber {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);
    /**
     * This is a test case to test all the requirement for a password which
     * were 1 uppercase, 1 lowercase, 1 special, and 1 number. In this case,
     * the input is missing numbers, therefore a message is prompt
     * indicating the test case passed when a message prompted "You shall not passed".
     */
    @Test
    public void testFindingNumber() {
        //finding the view
        ViewInteraction appCompatEditText = onView((withId(R.id.textpwd)));
        //typing in the Password#$*
        appCompatEditText.perform(replaceText("Password#$*"), closeSoftKeyboard());

        //finding the button
        ViewInteraction materialButton = onView((withId(R.id.login)));
        //clicking the button
        materialButton.perform(click());

        //finding the textview
        ViewInteraction textView = onView((withId(R.id.textView)));
        //checking the test
        textView.check(matches(withText("You shall not Pass!")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
