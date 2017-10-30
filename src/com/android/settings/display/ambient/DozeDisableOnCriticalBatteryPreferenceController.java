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
package com.android.settings.display.ambient;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.text.TextUtils;

import com.android.settings.R;
import com.android.settings.core.PreferenceController;

import nexus.provider.NexusSettings;
import static nexus.provider.NexusSettings.CRITICAL_DREAMING_BATTERY_PERCENTAGE;

public class DozeDisableOnCriticalBatteryPreferenceController extends PreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_DOZE_DISABLE_ON_CRITICAL_BATTERY = "doze_disable_on_critical_battery";

    public DozeDisableOnCriticalBatteryPreferenceController(Context context) {
        super(context);
    }

    @Override
    public String getPreferenceKey() {
        return KEY_DOZE_DISABLE_ON_CRITICAL_BATTERY;
    }

    @Override
    public void updateState(Preference preference) {
        final ListPreference listPreference = (ListPreference) preference;
        final int value = NexusSettings.getIntForCurrentUser(mContext, CRITICAL_DREAMING_BATTERY_PERCENTAGE, 15);
        int valueIndex = 2;

        switch (value) {
            case 5: valueIndex = 0; break;
            case 10: valueIndex = 1; break;
            case 15: valueIndex = 2; break;
            case 20: valueIndex = 3; break;
            case 25: valueIndex = 4; break;
        }

        listPreference.setEntries(new CharSequence[] { "5%", "10%", "15%", "20%", "25%" });
        listPreference.setEntryValues(new CharSequence[] { "5", "10", "15", "20", "25" });
        listPreference.setValueIndex(valueIndex);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final int value = Integer.parseInt((String) newValue);
        NexusSettings.putIntForCurrentUser(mContext, CRITICAL_DREAMING_BATTERY_PERCENTAGE, value);
        return true;
    }

    @Override
    public boolean isAvailable() {
        String name = Build.IS_DEBUGGABLE ? SystemProperties.get("debug.doze.component") : null;
        if (TextUtils.isEmpty(name)) {
            name = mContext.getResources().getString(
                    com.android.internal.R.string.config_dozeComponent);
        }
        return !TextUtils.isEmpty(name);
    }
}
