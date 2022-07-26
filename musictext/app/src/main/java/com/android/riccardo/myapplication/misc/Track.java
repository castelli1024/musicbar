package com.android.riccardo.myapplication.misc;

public class Track {
    private String name;
    private String artist;
    private double rank;

    public Track(String name, String artist, double rank) {
        this.name = name;
        this.artist = artist;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public double getRank() {
        return rank;
    }
}
