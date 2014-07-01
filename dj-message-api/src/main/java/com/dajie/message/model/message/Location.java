package com.dajie.message.model.message;

/**
 * Created by wills on 3/24/14.
 */
public class Location extends ChatContent {

    private long lat;

    private long lng;

    private long alt;

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLng() {
        return lng;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

    public long getAlt() {
        return alt;
    }

    public void setAlt(long alt) {
        this.alt = alt;
    }
}
