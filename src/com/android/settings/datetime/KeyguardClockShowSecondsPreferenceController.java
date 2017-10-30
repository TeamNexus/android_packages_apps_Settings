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
import android.os.Build;
import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.text.TextUtils;

import com.android.settings.R;
import com.android.settings.core.PreferenceController;

import nexus.provider.NexusSettings;
import static nexus.provider.NexusSettings.KEYGUARD_CLOCK_SHOW_SECONDS;

public class KeyguardClockShowSecondsPreferenceController extends PreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_KEYGUARD_CLOCK_SHOW_SECONDS = "keyguard_clock_show_seconds";

    private Context mContext;

    public KeyguardClockShowSecondsPreferenceController(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public String getPreferenceKey() {
        return KEY_KEYGUARD_CLOCK_SHOW_SECONDS;
    }

    @Override
    public void updateState(Preference preference) {
        final ListPreference listPreference = (ListPreference) preference;
        final int value = NexusSettings.getIntForCurrentUser(mContext, KEYGUARD_CLOCK_SHOW_SECONDS, 0);

        listPreference.setEntries(new CharSequence[] { "Hide", "Lockscreen only", "Doze only", "Lockscreen and Doze" });
        listPreference.setEntryValues(new CharSequence[] { "0", "1", "2", "3" });
        listPreference.setValueIndex(value);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final int value = Integer.parseInt((String) newValue);
        NexusSettings.putIntForCurrentUser(mContext, KEYGUARD_CLOCK_SHOW_SECONDS, value);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
