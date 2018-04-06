package com.example.trailmix;

/**
 * Created by andrewgoering on 3/31/18.
 */

import java.util.ArrayList;
import java.util.Random;

public class PlaylistGenerator {
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

    public static Song[] createRandomSongList(int numSongs, int lowTime, int highTime) {
        Random rand = new Random();
        int[] times = new int[numSongs];
        for (int i = 0; i < numSongs; i++)
            times[i] = rand.nextInt(highTime - lowTime) + lowTime;
        return timesToSongs(times);

    }

    public static Playlist generatePlaylist(ArrayList<String> ids, ArrayList<Integer> times, int targetTime) {
        ArrayList<Song> songs = new ArrayList<Song>();
        for(int i = 0; i < ids.size(); i++) {
            songs.add(new Song(ids.get(i), times.get(i)));
        }
        return generatePlaylist(songs, targetTime);
    }
    public static Playlist generatePlaylist(ArrayList<String> titles, ArrayList<String> paths, ArrayList<Integer> durations,int targetTime){
        return generatePlaylist(generateSongs(titles,paths,durations),targetTime);
    }

    public static Playlist generatePlaylist(ArrayList<Song> songs, int targetTime) {
        ArrayList<Song> origSongs = songs;
        Random rand = new Random();
        int shortListLength = 30;
        Playlist randomList = new Playlist();
        long startTime = System.currentTimeMillis();
        boolean hasPlaylist = false;
        SongTree tree;
        Playlist result;
        Playlist closestPlaylist = new Playlist();
        do {
            int c = 0;
            songs = (ArrayList<Song>) origSongs.clone();
            while (randomList.getTime() < targetTime - 1200 || (c > 0 && randomList.getTime() < targetTime)) {
                if (songs.size() > 0) {
                    int r = rand.nextInt(songs.size());
                    // System.out.println("r = " + r + ", songs.size() = " + songs.size());
                    System.out.println("r = " + r);

                    randomList.add(songs.get(r));
                    songs.remove(r);
                }
                else {
                    randomList.add(randomList.getSongs().get(c++).copy());
                }
            }

            Song[] shortSongList = new Song[Math.min(shortListLength, songs.size())];
            // System.out.println("songs.size() hi = " + songs.size());
            for (int i = 0; i < shortSongList.length; i++) {
                int r = rand.nextInt(songs.size());
                shortSongList[i] = songs.get(r);
                songs.remove(r);
            }
            System.out.println("short list length is " + shortSongList.length);
            long time = System.currentTimeMillis();
            tree = new SongTree(shortSongList, targetTime - randomList.getTime(), true);
            hasPlaylist = tree.getPlaylists().size() > 0;
            // System.out.println(tree.getPlaylists().size() + " playlist(s) hi");// +
            // tree.getPlaylists());
            // System.out.println("time: " + (System.currentTimeMillis() - time) + "\n");
            Playlist p = tree.getClosestPlaylist();
            if (closestPlaylist == null)
                closestPlaylist = p;
            closestPlaylist = Math.abs(targetTime - p.getTime()) < Math.abs(targetTime - closestPlaylist.getTime()) ? p
                    : closestPlaylist;
            System.out.println(p.getTime());
        } while (!hasPlaylist && System.currentTimeMillis() - startTime < 50);
        System.out.println("Algorithm took: " + (System.currentTimeMillis() - startTime) + "ms to complete");
        if (hasPlaylist)
            result = Playlist.concatPlaylists(randomList,
                    tree.getPlaylists().get(rand.nextInt(tree.getPlaylists().size())));
        else {
            result = Playlist.concatPlaylists(randomList, tree.getClosestPlaylist());
            System.out.println("Doesn't have playlist");

        }

        System.out.println("Final PlayList (out of tree " + tree.getPlaylists().size() + " possibilities: " + result);
        return result;
    }

    public static ArrayList<Song> generateSongs(ArrayList<String> titles, ArrayList<String> paths, ArrayList<Integer> durations){
        ArrayList<Song> songs = new ArrayList<Song>();
        if(titles.size() == paths.size()&& paths.size()==durations.size())
            for(int i = 0; i < titles.size(); i++)
                songs.add(new Song(titles.get(i),paths.get(i),durations.get(i)));

        return songs;
    }

    /*public static void main(String[] args) {
        // TODO Auto-generated method stub
        int targetTime = 60 * 60;
        Song[] songs = createRandomSongList(5, 120, 300);
        System.out.println("Songs: " + Arrays.toString(songs));
        System.out.println("Playlist: " + generatePlaylist(new ArrayList<Song>(Arrays.asList(songs)), targetTime));

    }*/

}
