package com.openclassrooms.realestatemanager;

import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LifecycleOwner;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.login.LoginActivity;
import com.openclassrooms.realestatemanager.repository.AgentRepository;
import com.openclassrooms.realestatemanager.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NavigationTest {

    private ActivityScenario<LoginActivity> loginActivity;
    private AgentRepository agentRepository;
    private PropertyDataRepository propertyDataRepository;
    private FirebaseAuth firebaseAuth;
    private UiDevice device;
    private final String userName = "Enzo";

    @Rule
    public ActivityScenarioRule<LoginActivity> lActivityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws InterruptedException, UiObjectNotFoundException {
        loginActivity = lActivityRule.getScenario();
        firebaseAuth = FirebaseAuth.getInstance();
        this.configureAgentRepository();
        this.configurePropertyRepository();
        device = UiDevice.getInstance(getInstrumentation());
        loginAndCreateUser();
        Thread.sleep(8000);
    }

    @After
    public void clearDown() {logoutAndDeleteUser();}

    /** Drawer Layout Test **/
    @Test
    public void displayNavigationViewWithCurrentUserName() throws InterruptedException {
        onView(ViewMatchers.withId(R.id.list_layout));
        Thread.sleep(2000);
        onView(withId(R.id.layout_drawer))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.user_name)).check(matches(withText(containsString(userName))));
        Thread.sleep(1000);
        onView(withId(R.id.layout_drawer)).perform(DrawerActions.close());
        Thread.sleep(1000);
    }

    @Test
    public void goToMap() throws InterruptedException {
        onView(ViewMatchers.withId(R.id.list_layout));
        Thread.sleep(2000);
        onView(withId(R.id.layout_drawer))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.map)).perform(click());
        Thread.sleep(1000);
        onView(ViewMatchers.withId(R.id.map_layout));
        Thread.sleep(1000);
        device.pressBack();
    }

    @Test
    public void goToSettings() throws InterruptedException {
        onView(ViewMatchers.withId(R.id.list_layout));
        Thread.sleep(2000);
        onView(withId(R.id.layout_drawer))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.settings)).perform(click());
        Thread.sleep(1000);
        onView(ViewMatchers.withId(R.id.settings_layout));
        Thread.sleep(1000);
        device.pressBack();
    }

    @Test
    public void clickOnItemShouldDisplayedDetails() throws InterruptedException {
        onView(ViewMatchers.withId(R.id.list_layout));
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.list_property)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(ViewMatchers.withId(R.id.details_view));
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.type_property))
                .check(matches(withText(containsString(LiveDataTestUtil.getValue(propertyDataRepository.getAllProperty()).get(0).getPropertyType()))));
        device.pressBack();
        Thread.sleep(2000);
    }

    @Test
    public void goToAddProperty() throws InterruptedException {
        onView(ViewMatchers.withId(R.id.list_layout));
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.floating_action_button)).perform(click());
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.main_feature_layout));
        Thread.sleep(2000);
        device.pressBack();
        Thread.sleep(2000);
    }


    /** Configure agent Repository **/
    private void configureAgentRepository(){
        agentRepository = Injection.provideAgentRepository();
    }

    /** Configure property Repository **/
    private void configurePropertyRepository(){
        LifecycleOwner lifecycle = mock(LifecycleOwner.class);
        propertyDataRepository = Injection.providePropertyRepository(lifecycle, ApplicationProvider.getApplicationContext());
    }

    public void loginAndCreateUser() throws InterruptedException, UiObjectNotFoundException {
        onView(withId(R.id.mail_sign_in)).perform(click());
        Thread.sleep(2000);
        device.pressBack();

        // Get Next Button
        UiObject nextBtn = device.findObject(new UiSelector()
                .instance(0)
                .className(Button.class));

        // Set Email
        UiObject emailInput = device.findObject(new UiSelector()
                .instance(0)
                .className(EditText.class));
        emailInput.setText("enzotouti@outlook.com");
        nextBtn.click();

        // Set Password
        UiObject passwordInput = device.findObject(new UiSelector()
                .instance(0)
                .className(EditText.class));
        Thread.sleep(2000);
        passwordInput.setText("azerty");
        nextBtn.click();
    }

    public void logoutAndDeleteUser() {
        // Logout
        String uid = firebaseAuth.getCurrentUser().getUid();
        onView(withId(R.id.layout_drawer))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.logout));
        onView(ViewMatchers.withId(R.id.login_activity));
        agentRepository.deleteAgent(uid);
    }

}
