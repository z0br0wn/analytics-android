package com.segment.analytics.sample;

import android.app.Application;
import android.widget.Toast;
import com.segment.analytics.Analytics;
import com.urbanairship.UAirship;
import com.urbanairship.extension.segment.UrbanAirshipIntegration;

public class SampleApp extends Application {

  private static final String ANALYTICS_WRITE_KEY = "<redacted>";

  @Override public void onCreate() {
    super.onCreate();

    // Initialize a new instance of the Analytics client.
    Analytics.Builder builder = new Analytics.Builder(this, ANALYTICS_WRITE_KEY) //
        .trackApplicationLifecycleEvents() //
        .recordScreenViews()
        .logLevel(Analytics.LogLevel.VERBOSE);

    // Set the initialized instance as a globally accessible instance.
    builder.use(UrbanAirshipIntegration.FACTORY);
    Analytics.setSingletonInstance(builder.build());

    // Now anytime you call Analytics.with, the custom instance will be returned.
    Analytics analytics = Analytics.with(this);

    // If you need to know when integrations have been initialized, use the onIntegrationReady
    // listener.
    analytics.onIntegrationReady("Segment.io", new Analytics.Callback() {
      @Override public void onReady(Object instance) {
        Toast.makeText(SampleApp.this, "Segment integration!", Toast.LENGTH_LONG).show();
      }
    });

    Analytics.with(this).onIntegrationReady(UrbanAirshipIntegration.URBAN_AIRSHIP_KEY, new Analytics.Callback() {
      @Override public void onReady(Object instance) {
        UAirship airship = (UAirship) instance;
        airship.getPushManager().setUserNotificationsEnabled(true);
      }
    });
  }
}
