package numan.todolist;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class CreateNewTaskActivity {

    @Rule
    public ActivityTestRule<MainActivity> myNewTask = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Test
    public void onClickCreateNewTaskButton() throws Exception {
        onView(withId(R.id.NewTask)).perform(click());
        onView(withId(R.id.input_title)).check(matches(isDisplayed()));


    }

}
