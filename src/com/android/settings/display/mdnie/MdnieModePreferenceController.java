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

import nexus.display.MdnieManager;
import nexus.hardware.MdnieDisplay;
import nexus.provider.NexusSettings;
import static nexus.provider.NexusSettings.MDNIE_MODE;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class MdnieModePreferenceController extends PreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_MDNIE_MODE = "mdnie_mode";

    public MdnieModePreferenceController(Context context) {
        super(context);
    }

    @Override
    public String getPreferenceKey() {
        return KEY_MDNIE_MODE;
    }

    @Override
    public void updateState(Preference preference) {
        final ListPreference listPreference = (ListPreference) preference;
        int value = NexusSettings.getIntForCurrentUser(mContext, MDNIE_MODE, 0);

		HashMap<String, String> modes = MdnieDisplay.getMdnieModes();
		Set<String> keys = modes.keySet();
		Collection<String> values = modes.values();

        listPreference.setEntries(keys.toArray(new CharSequence[keys.size()]));
        listPreference.setEntryValues(values.toArray(new CharSequence[values.size()]));

		int index = 0;
		for (String mapValue : values) {
			if (mapValue.equals(Integer.toString(value)))
				break;
			index++;
		}

        listPreference.setValueIndex(index);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final int value = Integer.parseInt((String) newValue);
        NexusSettings.putIntForCurrentUser(mContext, MDNIE_MODE, value);
        MdnieManager.applyMdnieMode(mContext);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return MdnieDisplay.supportsMdnieMode();
    }
}
