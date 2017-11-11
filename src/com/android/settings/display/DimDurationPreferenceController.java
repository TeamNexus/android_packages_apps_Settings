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
package com.android.settings.display;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.text.TextUtils;

import com.android.settings.R;
import com.android.settings.core.PreferenceController;

import nexus.hardware.AmbientDisplay;
import nexus.provider.NexusSettings;
import static nexus.provider.NexusSettings.SCREEN_DIM_DURATION;

public class DimDurationPreferenceController extends PreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_SCREEN_DIM_DURATION = "screen_dim_duration";

    public DimDurationPreferenceController(Context context) {
        super(context);
    }

    @Override
    public String getPreferenceKey() {
        return KEY_SCREEN_DIM_DURATION;
    }

    @Override
    public void updateState(Preference preference) {
        final ListPreference listPreference = (ListPreference) preference;
        final int value = NexusSettings.getIntForCurrentUser(mContext, SCREEN_DIM_DURATION, 7000);
        int valueIndex = 4;

        switch (value) {
            case 0: valueIndex = 0; break;
            case 1000: valueIndex = 1; break;
            case 2000: valueIndex = 2; break;
            case 5000: valueIndex = 3; break;
            case 7000: valueIndex = 4; break;
            case 10000: valueIndex = 5; break;
            case 20000: valueIndex = 6; break;
            case 30000: valueIndex = 7; break;
            case 60000: valueIndex = 8; break;
            case 120000: valueIndex = 9; break;
        }

        listPreference.setEntries(new CharSequence[] { "Disabled", "1 second", "2 seconds", "5 seconds", "7 seconds", "10 seconds", "20 seconds", "30 seconds", "1 minute", "2 minutes" });
        listPreference.setEntryValues(new CharSequence[] { "0", "1000", "2000", "5000", "7000", "10000", "20000", "30000", "60000", "120000" });
        listPreference.setValueIndex(valueIndex);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final int value = Integer.parseInt((String) newValue);
        NexusSettings.putIntForCurrentUser(mContext, SCREEN_DIM_DURATION, value);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
