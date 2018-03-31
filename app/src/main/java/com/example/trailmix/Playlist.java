package com.example.trailmix;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by andrewgoering on 3/31/18.
 */

public class Playlist implements Comparable {
    private ArrayList<Song> songs;
    private int time;
    private int compareNum;

    public Playlist(ArrayList<Song> songs) {
        this.songs = songs;
        calcTime();
    }

    public Playlist() {
        songs = new ArrayList<Song>();
        calcTime();
    }

    public int length() {
        return songs.size();
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public int getTime() {
        return time;
    }

    public void add(Song song) {
        songs.add(song);
        calcTime();
    }

    private int calcTime() {
        time = 0;
        for (Song s : this.songs)
            time += s.getTime();
        return time;
    }

    public String toString() {
        return "(Time = " + time + " Songs = " + songs.toString() + ")";
    }

    @SuppressWarnings("unchecked")
    public static Playlist copy(Playlist original) {
        return new Playlist((ArrayList<Song>) original.getSongs().clone());
    }

    /*
     * Same algorithm as String class compareTo method.
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof Playlist) {
            int len1 = this.length();
            int len2 = ((Playlist) o).length();
            int lim = Math.min(len1, len2);
            int v1[] = SongTree.songsToTimes(songs);
            int v2[] = SongTree.songsToTimes(((Playlist) o).getSongs());

            int k = 0;
            while (k < lim) {
                int c1 = v1[k];
                int c2 = v2[k];
                if (c1 != c2) {
                    return c1 - c2;
                }
                k++;
            }
            return len1 - len2;
        } else
            throw new ClassCastException("Playlist object expected");

    }

    public static Playlist concatPlaylists(Playlist p1, Playlist p2) {
        Playlist newP = new Playlist();
        newP.getSongs().addAll((Collection) p1.getSongs());
        newP.getSongs().addAll((Collection) p2.getSongs());
        newP.calcTime();
        return newP;
    }
}
