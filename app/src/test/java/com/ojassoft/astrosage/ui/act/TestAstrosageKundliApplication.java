package com.ojassoft.astrosage.ui.act;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

/**
 * Test application that initializes Firebase before the production lifecycle runs so Firebase Realtime Database
 * operations inside {@link AstrosageKundliApplication#onCreate()} do not crash under Robolectric.
 */
public class TestAstrosageKundliApplication extends AstrosageKundliApplication {

    @Override
    public void onCreate() {
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApplicationId("1:000:android:test")
                    .setApiKey("test-api-key")
                    .setDatabaseUrl("https://test.firebaseio.com")
                    .setProjectId("test-project")
                    .build();
            FirebaseApp.initializeApp(this, options);
        }
        super.onCreate();
    }
}
