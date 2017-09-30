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
package com.android.settings.display.mdnie;

import android.content.Context;
import android.os.FileUtils;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;

import com.android.settings.R;
import com.android.settings.core.PreferenceController;

import static android.provider.Settings.Secure.MDNIE_SCENARIO;

import java.io.IOException;

public class MdnieScenarioPreferenceController extends PreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_MDNIE_SCENARIO = "mdnie_scenario";
	private static final String PATH_MDNIE_SCENARIO = "/sys/class/mdnie/mdnie/scenario";

    public MdnieScenarioPreferenceController(Context context) {
        super(context);
    }

    @Override
    public String getPreferenceKey() {
        return KEY_MDNIE_SCENARIO;
    }

    @Override
    public void updateState(Preference preference) {
        final ListPreference listPreference = (ListPreference) preference;
        final int value = Settings.Secure.getIntForCurrentUser(mContext, MDNIE_SCENARIO, 0);

        listPreference.setEntries(new CharSequence[] {
			"UI",
			"Video",
			"Camera",
			"Navigation",
			"Gallery",
			"VT",
			"Browser",
			"E-Book",
			"E-Mail",
			"HMT-8",
			"HMT-16",
		});
        listPreference.setEntryValues(new CharSequence[] {
			"0",
			"1",
			"4",
			"5",
			"6",
			"7",
			"8",
			"9",
			"10",
			"11",
			"12",
		});
        listPreference.setValueIndex(value);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final int value = Integer.parseInt((String) newValue);
        Settings.Secure.putIntForCurrentUser(mContext, MDNIE_SCENARIO, value);
		try {
			FileUtils.stringToFile(PATH_MDNIE_SCENARIO, String.valueOf(value));
		} catch (IOException e) { }
        return true;
    }

    @Override
    public boolean isAvailable() {
        return FileUtils.isFile(PATH_MDNIE_SCENARIO) && FileUtils.isAccessible(PATH_MDNIE_SCENARIO);
    }
}
