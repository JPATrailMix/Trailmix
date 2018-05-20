package com.example.trailmix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * @author Andy Goering
 * @date 05/18/2018
 * Class for modeling a playlist of Song objects.
 */

public class Playlist implements Comparable {
    private ArrayList<Song> songs;
    private int time;

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
        return "(Time = " + time/60.0 + " Songs = " + songs.toString() + ")";
    }

    @SuppressWarnings("unchecked")
    public static Playlist copy(Playlist original) {
        return new Playlist((ArrayList<Song>) original.getSongs().clone());
    }

    /**
     * Same algorithm as String class compareTo method.
     * @param o (object to compare this playlist to)
     * @return "distance" between the two playlists. Order determined
     * the same way the order of words in a dictionary is determined.
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

    /**
     * Shuffles the playlist randomly
     */
    public void shuffle(){
        Random rand = new Random();
        for(int i = songs.size()-1; i > 0; i--){
            int n = rand.nextInt(i+1);
            Song s = songs.get(n);
            songs.set(n,songs.get(i));
            songs.set(i,s);
        }

    }

    /**
     * Concatenates two Playlists. Does not check for duplicate Songs.
     * @param p1 (first playlist to concat)
     * @param p2 (second playlist to concat)
     * @return a playlist formed by the concatenation of the two arguments.
     */
    public static Playlist concatPlaylists(Playlist p1, Playlist p2) {
        Playlist newP = new Playlist();
        newP.getSongs().addAll((Collection) p1.getSongs());
        newP.getSongs().addAll((Collection) p2.getSongs());
        newP.calcTime();
        return newP;
    }
}
