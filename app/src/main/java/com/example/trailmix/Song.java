package com.example.trailmix;

/**
 * Created by andrewgoering on 3/31/18.
 */

public class Song implements Comparable {
    private int time;

    public long getId() {
        return id;
    }

    private long id;
    private String path;
    private String name;
    private String album;
    private String artist;


    public Song() {
        this.id = -1;
        this.time = 0;
        this.path = "";
        this.name = "";
        this.album = "";
        this.artist = "";
    }

    public Song(int time) {
        this.id = -1;
        this.time = time;
        this.path = "";
        this.name = "";
        this.album = "";
        this.artist = "";
    }

    public Song(String path, int time) {
        this.id = -1;
        this.time = time;
        this.path = path;
        this.name = "";
        this.album = "";
        this.artist = "";
    }
    public Song(String name, String path, int time) {
        this.id = -1;
        this.time = time;
        this.path = path;
        this.name = name;
        this.album = "";
        this.artist = "";
    }

    public Song(String name, long id, int time) {
        this.id = id;
        this.time = time;
        this.path = "";
        this.name = name;
        this.album = "";
        this.artist = "";
    }

    public String toString() {
        return "" + name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Song)
            return time - ((Song)o).getTime();
        else
            throw new ClassCastException("Song object expected");
    }

    public Song copy() {
        return new Song(time);
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

}
