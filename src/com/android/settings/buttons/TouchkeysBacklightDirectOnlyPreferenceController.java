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
package com.android.settings.buttons;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.provider.Settings;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.text.TextUtils;

import com.android.settings.core.PreferenceController;

import nexus.hardware.Touchkeys;
import nexus.provider.NexusSettings;
import static nexus.provider.NexusSettings.TOUCHKEYS_BACKLIGHT_DIRECT_ONLY;

public class TouchkeysBacklightDirectOnlyPreferenceController extends PreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_TOUCHKEYS_BACKLIGHT_DIRECT_ONLY = "touchkeys_backlight_direct_only";

    public TouchkeysBacklightDirectOnlyPreferenceController(Context context) {
        super(context);
    }

    @Override
    public String getPreferenceKey() {
        return KEY_TOUCHKEYS_BACKLIGHT_DIRECT_ONLY;
    }

    @Override
    public void updateState(Preference preference) {
        boolean value = NexusSettings.getBoolForCurrentUser(mContext, TOUCHKEYS_BACKLIGHT_DIRECT_ONLY, true);
        ((SwitchPreference) preference).setChecked(value);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean value = (Boolean) newValue;
        NexusSettings.putBoolForCurrentUser(mContext, TOUCHKEYS_BACKLIGHT_DIRECT_ONLY, value);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return Touchkeys.isBacklightSupported();
    }
}
