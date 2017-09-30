/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings;

import android.content.Context;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import com.android.settings.SeekBarPreference;
import com.android.settings.core.PreferenceController;
import com.android.settings.core.lifecycle.Lifecycle;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.display.mdnie.MdnieModePreferenceController;
import com.android.settings.display.mdnie.MdnieScenarioPreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import static android.provider.Settings.Secure.MDNIE_COLOR_CORRECTION_RED;
import static android.provider.Settings.Secure.MDNIE_COLOR_CORRECTION_GREEN;
import static android.provider.Settings.Secure.MDNIE_COLOR_CORRECTION_BLUE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DisplayMdnieSettings extends DashboardFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String TAG = "DisplayMdnieSettings";

	private static final String PATH_MDNIE_SENSOR_RGB = "/sys/class/mdnie/mdnie/sensorRGB";

	private SeekBarPreference color_corr_red;
	private SeekBarPreference color_corr_green;
	private SeekBarPreference color_corr_blue;

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.DISPLAY;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        final PreferenceScreen screen = getPreferenceScreen();
		addColorCorrectionPreferences(screen);
	}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mProgressiveDisclosureMixin.setTileLimit(4);
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.display_mdnie_settings;
    }

    @Override
    protected List<PreferenceController> getPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getLifecycle());
    }

    @Override
    protected int getHelpResource() {
        return R.string.help_uri_display;
    }

	private void addColorCorrectionPreferences(final PreferenceScreen screen) {
		final Preference.OnPreferenceChangeListener defaultChangeListener = this;
        PreferenceCategory category = new PreferenceCategory(getPrefContext());
        category.setTitle(R.string.mdnie_color_correction_group_title);
        screen.addPreference(category);

		if (FileUtils.exists(PATH_MDNIE_SENSOR_RGB) && FileUtils.isAccessible(PATH_MDNIE_SENSOR_RGB)) {
			int color_corr_red_value = Settings.Secure.getIntForCurrentUser(getContext(), MDNIE_COLOR_CORRECTION_RED, 255);
			color_corr_red = new SeekBarPreference(getPrefContext());
			color_corr_red.setKey("mdnie_color_correction_red");
			color_corr_red.setTitle(R.string.mdnie_color_correction_red_title);
			color_corr_red.setShowSummary(true);
			color_corr_red.setSummary(getString(R.string.mdnie_color_correction_red_summary, color_corr_red_value, ((color_corr_red_value * 100) / 255)));
			color_corr_red.setMax(255);
			color_corr_red.setProgress(color_corr_red_value);
			color_corr_red.setContinuousUpdates(true);
			color_corr_red.setOnPreferenceChangeListener(this);
			category.addPreference(color_corr_red);

			int color_corr_green_value = Settings.Secure.getIntForCurrentUser(getContext(), MDNIE_COLOR_CORRECTION_GREEN, 255);
			color_corr_green = new SeekBarPreference(getPrefContext());
			color_corr_green.setKey("mdnie_color_correction_green");
			color_corr_green.setTitle(R.string.mdnie_color_correction_green_title);
			color_corr_green.setShowSummary(true);
			color_corr_green.setSummary(getString(R.string.mdnie_color_correction_green_summary, color_corr_green_value, ((color_corr_green_value * 100) / 255)));
			color_corr_green.setMax(255);
			color_corr_green.setProgress(color_corr_green_value);
			color_corr_green.setContinuousUpdates(true);
			color_corr_green.setOnPreferenceChangeListener(this);
			category.addPreference(color_corr_green);

			int color_corr_blue_value = Settings.Secure.getIntForCurrentUser(getContext(), MDNIE_COLOR_CORRECTION_BLUE, 255);
			color_corr_blue = new SeekBarPreference(getPrefContext());
			color_corr_blue.setKey("mdnie_color_correction_blue");
			color_corr_blue.setTitle(R.string.mdnie_color_correction_blue_title);
			color_corr_blue.setShowSummary(true);
			color_corr_blue.setSummary(getString(R.string.mdnie_color_correction_blue_summary, color_corr_blue_value, ((color_corr_blue_value * 100) / 255)));
			color_corr_blue.setMax(255);
			color_corr_blue.setProgress(color_corr_blue_value);
			color_corr_blue.setContinuousUpdates(true);
			color_corr_blue.setOnPreferenceChangeListener(this);
			category.addPreference(color_corr_blue);
		}
	}

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == color_corr_red) {
			int value = Integer.parseInt(newValue.toString());
            Settings.Secure.putIntForCurrentUser(getContext(), MDNIE_COLOR_CORRECTION_RED, value);
			color_corr_red.setSummary(getString(R.string.mdnie_color_correction_red_summary, value, ((value * 100) / 255)));
			updateSensorRGB();
        } else if (preference == color_corr_green) {
			int value = Integer.parseInt(newValue.toString());
            Settings.Secure.putIntForCurrentUser(getContext(), MDNIE_COLOR_CORRECTION_GREEN, value);
			color_corr_green.setSummary(getString(R.string.mdnie_color_correction_green_summary, value, ((value * 100) / 255)));
			updateSensorRGB();
        } else if (preference == color_corr_blue) {
			int value = Integer.parseInt(newValue.toString());
            Settings.Secure.putIntForCurrentUser(getContext(), MDNIE_COLOR_CORRECTION_BLUE, value);
			color_corr_blue.setSummary(getString(R.string.mdnie_color_correction_blue_summary, value, ((value * 100) / 255)));
			updateSensorRGB();
        }
        return true;
    }

	private void updateSensorRGB() {
		int r = Settings.Secure.getIntForCurrentUser(getContext(), MDNIE_COLOR_CORRECTION_RED, 255);
		int g = Settings.Secure.getIntForCurrentUser(getContext(), MDNIE_COLOR_CORRECTION_GREEN, 255);
		int b = Settings.Secure.getIntForCurrentUser(getContext(), MDNIE_COLOR_CORRECTION_BLUE, 255);
		try {
			FileUtils.stringToFile(PATH_MDNIE_SENSOR_RGB, r + " " + g + " " + b + "\n");
		} catch (IOException e) { }
	}

    private static List<PreferenceController> buildPreferenceControllers(
            Context context, Lifecycle lifecycle) {
        final List<PreferenceController> controllers = new ArrayList<>();
        controllers.add(new MdnieModePreferenceController(context));
        controllers.add(new MdnieScenarioPreferenceController(context));
        return controllers;
    }

    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    final ArrayList<SearchIndexableResource> result = new ArrayList<>();

                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.display_mdnie_settings;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<PreferenceController> getPreferenceControllers(Context context) {
                    return buildPreferenceControllers(context, null);
                }
            };
}
