/*
 * Copyright (C) 2014 CyanogenMod Project
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

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceViewHolder;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.settings.R;
import com.android.settings.Utils;

import nexus.provider.NexusSettings;
import static nexus.provider.NexusSettings.VIBRATOR_INTENSITY;

public class VibratorIntensityVolumePreference extends Preference implements
        SeekBar.OnSeekBarChangeListener
{
    private static final String TAG = "VibratorIntensityVolumePreference";

    private SeekBar mSeekBar;
    private TextView mTextValue;

    public VibratorIntensityVolumePreference(Context context) {
        this(context, null);
    }

    public VibratorIntensityVolumePreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(
                context, R.attr.customPreferenceStyle, android.R.attr.preferenceStyle));
    }

    public VibratorIntensityVolumePreference(Context context, AttributeSet attrs,
            int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public VibratorIntensityVolumePreference(Context context, AttributeSet attrs,
            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayoutResource(R.layout.preference_vibrator_control);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        mSeekBar = (SeekBar) holder.findViewById(R.id.vibrator_intensity);
        mTextValue = (TextView) holder.findViewById(R.id.vibrator_intensity_value);

        int currentVibratorIntensity = NexusSettings.getIntForCurrentUser(getContext(),
                VIBRATOR_INTENSITY, 100);

        mSeekBar.setOnSeekBarChangeListener(this);
		mSeekBar.setMin(0);
		mSeekBar.setMax(100);
        mSeekBar.setProgress(currentVibratorIntensity);
        mTextValue.setText(getContext().getString(R.string.vibrator_intensity_value_format,
                Utils.formatPercentage(currentVibratorIntensity)));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        if (seekBar == mSeekBar) {
            mTextValue.setText(getContext().getString(R.string.vibrator_intensity_value_format,
                    Utils.formatPercentage(progress)));
            if (fromTouch) {
                NexusSettings.putIntForCurrentUser(getContext(),
				        NexusSettings.VIBRATOR_INTENSITY, progress);
            }
        }
    }
}

