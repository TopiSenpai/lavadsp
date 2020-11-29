package com.github.natanbc.lavadsp.volume;

import com.github.natanbc.lavadsp.ConverterPcmAudioFilter;
import com.github.natanbc.lavadsp.util.FloatToFloatFunction;
import com.sedmelluq.discord.lavaplayer.filter.FloatPcmAudioFilter;

/**
 * Updates the effect volume, with a multiplier ranging from 0 to 5.
 */
public class VolumePcmAudioFilter extends ConverterPcmAudioFilter<VolumeConverter> {
    private volatile float volume = 1.0f;

    public VolumePcmAudioFilter(FloatPcmAudioFilter downstream, int channelCount, int bufferSize) {
        super(VolumeConverter::new, downstream, channelCount, bufferSize);
    }

    public VolumePcmAudioFilter(FloatPcmAudioFilter downstream, int channelCount) {
        super(VolumeConverter::new, downstream, channelCount);
    }

    /**
     * Returns the volume multiplier. 1.0 means unmodified.
     *
     * @return The current volume.
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Sets the volume multiplier. 1.0 means unmodified.
     *
     * @param volume Volume to use.
     *
     * @return {@code this}, for chaining calls.
     */
    public VolumePcmAudioFilter setVolume(float volume) {
        for(VolumeConverter converter : converters()) {
            converter.setVolume(volume);
        }
        this.volume = volume;
        return this;
    }

    /**
     * Updates the volume multiplier, using a function that accepts the current value
     * and returns a new value.
     *
     * @param function Function used to map the depth.
     *
     * @return {@code this}, for chaining calls
     */
    public VolumePcmAudioFilter updateVolume(FloatToFloatFunction function) {
        return setVolume(function.apply(volume));
    }

    @Override
    public void process(float[][] input, int offset, int length) throws InterruptedException {
        //don't call the native library if volume is (close to) 1.0
        if(Math.abs(1.0 - volume) < 0.02) {
            downstream.process(input, offset, length);
            return;
        }
        super.process(input, offset, length);
    }
}
