package numan.todolist;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class InstructionsScreenTest {

    @Rule
    public ActivityTestRule<InstructionsScreen> myNewTask = new ActivityTestRule<InstructionsScreen>(InstructionsScreen.class);

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(InstructionsScreen.class.getName(), null, false);

    @Test
    public void onOptionsItemSelected() {

        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 3000);
        assertNotNull(secondActivity);
    }
}