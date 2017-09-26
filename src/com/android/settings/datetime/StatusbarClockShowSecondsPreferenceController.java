/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.android.settings.datetime;

import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;

import com.android.settings.core.PreferenceController;
import com.android.settings.core.instrumentation.MetricsFeatureProvider;
import com.android.settings.overlay.FeatureFactory;

public class StatusbarClockShowSecondsPreferenceController extends PreferenceController implements
        Preference.OnPreferenceChangeListener {

    // has to be in sync with "frameworks/base/packages/SystemUI/src/com/android/systemui/statusbar/policy/Clock.java:67"
    private static final String CLOCK_SECONDS = "clock_seconds";
    private static final String KEY_CLOCK_SHOW_SECONDS = "statusbar_clock_show_seconds";

    private Context mContext;

    public StatusbarClockShowSecondsPreferenceController(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public String getPreferenceKey() {
        return KEY_CLOCK_SHOW_SECONDS;
    }

    @Override
    public void updateState(Preference preference) {
        boolean value = Settings.Secure.getBoolForCurrentUser(mContext, CLOCK_SECONDS, false);
        ((SwitchPreference) preference).setChecked(value);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean value = (Boolean) newValue;
        Settings.Secure.putBoolForCurrentUser(mContext, CLOCK_SECONDS, value);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
