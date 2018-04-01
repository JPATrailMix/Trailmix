package com.example.trailmix;

/**
 * Created by andrewgoering on 3/31/18.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SongTree {
    private Song[] songs;
    private ArrayList<Playlist> playlists;
    private Playlist closestPlaylist;
    private Node rootNode;
    private int targetTime;

    public SongTree(Song[] songs, int targetTime, boolean node) {
        if (node) {
            playlists = new ArrayList<Playlist>();
            this.songs = songs;
            Arrays.sort(this.songs);
            this.targetTime = targetTime;
            //System.out.println(arrayToString(songs));
            rootNode = new Node(this, new Playlist(new ArrayList<Song>()));
            for (Song s : this.songs) {
                rootNode.addSong(s);
                //System.out.println("Closest Playlist has length: " + closestPlaylist.getTime());
				if(playlists.size()>0) {
					System.out.println("Stopping song tree because playlists.size() > 0");
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
                if(playlists.size() > 0)
                    break;
            }
        }
        Collections.sort(playlists);

    }

    public void submitPlaylistWithError(Playlist p) {
        if(closestPlaylist == null)
            closestPlaylist = p;
        closestPlaylist = Math.abs(targetTime - p.getTime()) < Math.abs(targetTime - closestPlaylist.getTime())? p : closestPlaylist;
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public int getTargetTime() {
        return targetTime;
    }

    public static Song[] timesToSongs(int[] times) {
        Song[] songs = new Song[times.length];
        for (int i = 0; i < songs.length; i++)
            songs[i] = new Song(times[i]);
        return songs;
    }

    public static int[] songsToTimes(Song[] songs) {
        int[] times = new int[songs.length];
        for (int i = 0; i < times.length; i++)
            times[i] = songs[i].getTime();
        return times;
    }

    public static int[] songsToTimes(ArrayList<Song> songs) {
        int[] times = new int[songs.size()];
        for (int i = 0; i < times.length; i++)
            times[i] = songs.get(i).getTime();
        return times;
    }

    public Playlist getClosestPlaylist() {
        return closestPlaylist;
    }

    public static String arrayToString(Object[] os) {
        String s = "[";
        for (int i = 0; i < os.length - 1; i++)
            s += os[i].toString() + ",";
        s += os[os.length - 1] + "]";
        return s;
    }

    public static String arrayToString(int[] os) {
        String s = "[";
        for (int i = 0; i < os.length - 1; i++)
            s += os[i] + ",";
        s += os[os.length - 1] + "]";
        return s;
    }
}
