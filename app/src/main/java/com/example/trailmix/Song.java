package com.example.trailmix;

/**
 * @author Andy Goering
 * @date 05/18/2018
 * This class models a song. Stores song path and metadata.
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
        return "" + name + ", " + time + "s";
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Compares the length of this song with that of another
     * @param o (object to perform the comparison with)
     * @return returns difference in time between this Song and the argument
     */
    @Override
    public int compareTo(Object o) {
        if(o instanceof Song)
            return time - ((Song)o).getTime();
        else
            throw new ClassCastException("Song object expected");
    }

    public Song copy() {
        return new Song(name,path,time);
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
