package com.example.trailmix;

/**
 * Created by andrewgoering on 3/31/18.
 */

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SongTree {
    private Song[] songs;
    private ArrayList<Playlist> playlists;
    private Playlist closestPlaylist;
    private Node rootNode;
    private int targetTime;

    /**
     * Constructs song tree. Set node to true to run the actual tree algorithm on real nodes.
     * Set node to false to run the algorithm with the emulated tree algorithm.
     *
     * @param songs
     * @param targetTime
     * @param node
     */
    public SongTree(Song[] songs, int targetTime, boolean node) {
        if (node) {
            long startTime = System.currentTimeMillis();
            playlists = new ArrayList<Playlist>();
            this.songs = songs;
            Arrays.sort(this.songs);
            this.targetTime = targetTime;
            //System.out.println(arrayToString(songs));
            rootNode = new Node(this, new Playlist(new ArrayList<Song>()));

            //Feed songs into the root node.
            for (Song s : this.songs) {
                rootNode.addSong(s);
                //System.out.println("Closest Playlist has length: " + closestPlaylist.getTime());

                //stop running the algorithm if it has been running for longer than two seconds and a playlist has been found already.
                if (playlists.size() > 0 && System.currentTimeMillis() - startTime > 2000) {
                    Log.d("SongTree", "Stopping song tree because playlists.size() > 0 and it has been running for more than 2 seconds");
                    break;
                }
                // System.out.println(getPlaylists().size() + " possible playlist(s): " +
                // getPlaylists());
            }
        } else {
            playlists = new ArrayList<Playlist>();
            this.songs = songs;
            Arrays.sort(this.songs);
            //System.out.println(arrayToString(this.songs));
            this.targetTime = targetTime;
            ArrayList<Playlist> tree = new ArrayList<Playlist>();
            tree.add(new Playlist());
            //tree.get(0).add(this.songs[0]);
            for (Song s : this.songs) {
                int r = 0;
                int origSize = tree.size();
                for (int i = 0; i < origSize - r; i++) {
                    Playlist p = tree.get(i);
                    if (targetTime - p.getTime() > s.getTime()) {
                        Playlist newP = Playlist.copy(p);
                        newP.add(s);
                        tree.add(newP);
                        submitPlaylistWithError(newP);
                    } else if (targetTime - p.getTime() == s.getTime()) {
                        Playlist newP = Playlist.copy(p);
                        newP.add(s);
                        playlists.add(newP);
						/*tree.remove(i);
						i--;
						r++;*/
                    } else {
                        tree.remove(i);
                        i--;
                        r++;
                    }

                }
                //System.out.println(tree);
                //System.out.println(playlists.size());
                /*if(playlists.size() > 0)
                    break;*/
            }
        }
        Collections.sort(playlists);

    }

    /**
     * Allows nodes to submit playlists that are not the right length of time, as well as perfect ones.
     * This method keeps track of the closests inaccurate playlist, just in case a perfect one isn't found.
     *
     * @param p
     */
    public void submitPlaylistWithError(Playlist p) {
        if (closestPlaylist == null)
            closestPlaylist = p;
        closestPlaylist = Math.abs(targetTime - p.getTime()) < Math.abs(targetTime - closestPlaylist.getTime()) ? p : closestPlaylist;
    }

    /**
     * Method for submitting perfect playlists.
     *
     * @param playlist
     */
    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public int getTargetTime() {
        return targetTime;
    }

    /**
     * converts an array of integers to an array of song objects with lengths of those integers.
     *
     * @param times
     * @return
     */
    public static Song[] timesToSongs(int[] times) {
        Song[] songs = new Song[times.length];
        for (int i = 0; i < songs.length; i++)
            songs[i] = new Song(times[i]);
        return songs;
    }

    /**
     * Takes an array of songs and returns an array of their durations.
     *
     * @param songs
     * @return
     */
    public static int[] songsToTimes(Song[] songs) {
        int[] times = new int[songs.length];
        for (int i = 0; i < times.length; i++)
            times[i] = songs[i].getTime();
        return times;
    }

    /**
     * converts an array of integers to an array of song objects with lengths of those integers.
     *
     * @param songs
     * @return
     */
    public static int[] songsToTimes(ArrayList<Song> songs) {
        int[] times = new int[songs.size()];
        for (int i = 0; i < times.length; i++)
            times[i] = songs.get(i).getTime();
        return times;
    }

    public Playlist getClosestPlaylist() {
        return closestPlaylist;
    }
}