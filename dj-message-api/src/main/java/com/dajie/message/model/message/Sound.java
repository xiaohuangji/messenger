package com.dajie.message.model.message;

/**
 * Created by wills on 3/24/14.
 */
public class Sound extends ChatContent {

    private int duration;

    private String soundUrl;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }
}
