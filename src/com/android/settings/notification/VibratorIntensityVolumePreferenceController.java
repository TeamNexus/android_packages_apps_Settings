/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.android.settings.notification;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.preference.PreferenceScreen;

import com.android.settings.Utils;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnStop;

/**
 * Base class for preference controller that handles VolumeSeekBarPreference
 */
public class VibratorIntensityVolumePreferenceController
        extends AdjustVolumeRestrictedPreferenceController
{
    private static final String KEY_VIBRATOR_INTENSITY = "vibrator_intensity";

    private Vibrator mVibrator;

    public VibratorIntensityVolumePreferenceController(Context context) {
        super(context);
        mVibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public String getPreferenceKey() {
        return KEY_VIBRATOR_INTENSITY;
    }

    @Override
    public boolean isAvailable() {
        return mVibrator.hasVibrator() && mVibrator.hasAmplitudeControl();
    }
}
