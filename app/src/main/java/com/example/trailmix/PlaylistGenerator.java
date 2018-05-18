package com.example.trailmix;

/**
 * Created by andrewgoering on 3/31/18.
 */

import android.util.Log;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static Playlist generatePlaylist(ArrayList<String> paths, ArrayList<Integer> times, int targetTime) throws IOException {
        ArrayList<Song> songs = new ArrayList<Song>();
        for(int i = 0; i < paths.size(); i++) {
            songs.add(new Song(paths.get(i), times.get(i)));
        }
        return generatePlaylist(songs, targetTime);
    }
   /* public static Playlist generatePlaylist(ArrayList<String> titles, ArrayList<Long> ids, ArrayList<Integer> durations,int targetTime) throws IOException{
        return generatePlaylist(generateSongs(titles,ids,durations),targetTime);
    }*/

    public static Playlist generatePlaylist(ArrayList<Song> songs, int targetTime) throws IOException{
        removeShortSongs(songs);
        Log.d("Music", "Songs with ringtones removed: " + songs);
        if(songs.size() != 0) {
            //ArrayList<Song> origSongs = songs;
            Random rand = new Random();
            int shortListLength = 15;
            Playlist randomList = new Playlist();
            long startTime = System.currentTimeMillis();
            boolean hasPlaylist = false;
            SongTree tree;
            Playlist result;
            Playlist closestPlaylist = new Playlist();
            ArrayList<Song> songsClone1 = (ArrayList<Song>) songs.clone();
            do {
                int c = 0;
                //songs = (ArrayList<Song>) origSongs.clone();
                while (randomList.getTime() < targetTime - 1200 ) {
                    Log.d("Music", "In randomList while loop");

                    if (songs.size() > 0) {
                        int r = rand.nextInt(songs.size());
                        // System.out.println("r = " + r + ", songs.size() = " + songs.size());
                        System.out.println("r = " + r);

                        randomList.add(songs.get(r));
                        songs.remove(r);
                    } else {
                        randomList.add(randomList.getSongs().get(c++).copy());
                    }
                }
                Log.d("Music", "generated randomList");
                Log.d("Music", "randomList = " + randomList);

                Song[] shortSongList = new Song[shortListLength];
                // System.out.println("songs.size() hi = " + songs.size());
                int place = 0;
                if(songs.size()>= shortListLength) {
                    Log.d("Music", "songs.size > shortListLength");
                    for (int i = 0; i < shortSongList.length; i++) {
                        int r = rand.nextInt(songs.size());
                        shortSongList[i] = songs.get(r);
                        songs.remove(r);
                    }
                }
                //else if(songs.size()>0 && randomList.getSongs().size()>=shortListLength);
                else if(randomList.getSongs().size()>=shortListLength - songs.size()) {
                    Log.d("Music", "randomList.getSongs().size()>=shortListLength - songs.size()");
                    for(; songs.size()>0; place++) {
                        shortSongList[place] = songs.get(0);
                        songs.remove(0);
                    }
                    Playlist randListClone = Playlist.copy(randomList);
                    Log.d("Music", "randListClone = " + randListClone);
                    for (int i = place; i < shortListLength; i++) {
                        int r = rand.nextInt(randListClone.length());
                        Log.d("Music", "randListClone.length() = " + randListClone.length() + ", i = " + i + ", r = " +r);
                        shortSongList[i] = randListClone.getSongs().get(r);
                        Log.d("Music", "randListClone.length() = " + randListClone.length() + ", i = " + i + ", r = " +r);
                        randListClone.getSongs().remove(r);
                    }
                }

                else {
                    Log.d("Music", "song cloning required");
                    int i= 0;
                    ArrayList<Song> songsClone = (ArrayList<Song>) songs.clone();
                    Log.d("Music", "songsClone.size() = " + songsClone.size());
                    for(; songs.size()>0; place++) {
                        shortSongList[place] = songs.get(0);
                        songs.remove(0);
                    }

                    if(randomList.length()!=0) {
                        for (; i < randomList.length(); place++) {
                            shortSongList[place] = randomList.getSongs().get(place);
                        }
                        for (; place < shortListLength; place++) {
                            shortSongList[place] = randomList.getSongs().get(place % randomList.length()).copy();
                        }
                    }

                    for(; place < shortListLength; place++){
                        shortSongList[place] = songsClone.get(place % songsClone.size()).copy();
                    }
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
                songs = (ArrayList<Song>) songsClone1.clone();
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
            result.shuffle();
            return result;
        }
        else
            throw new IOException("No Songs besides ringtones");
    }

    /*public static ArrayList<Song> generateSongs(ArrayList<String> titles, ArrayList<Long> ids, ArrayList<Integer> durations){
        ArrayList<Song> songs = new ArrayList<Song>();
        if(titles.size() == ids.size()&& ids.size()==durations.size())
            for(int i = 0; i < titles.size(); i++)
                songs.add(new Song(titles.get(i),ids.get(i),durations.get(i)));

        return songs;
    }*/

    public static ArrayList<Song> generateSongs(ArrayList<String> titles, ArrayList<String> paths, ArrayList<Integer> durations){
        ArrayList<Song> songs = new ArrayList<Song>();
        if(titles.size() == paths.size()&& paths.size()==durations.size())
            for(int i = 0; i < titles.size(); i++)
                songs.add(new Song(titles.get(i),paths.get(i),durations.get(i)));

        return songs;
    }

    public static void removeShortSongs(ArrayList<Song> songs){
        System.out.println("Before Remove Short Songs: " +  songs);
        for(int i = 0; i < songs.size(); i++){
            if(songs.get(i).getTime() < 30) {
                songs.remove(i);
                i--;
            }
        }
        System.out.println("After Remove Short Songs: " +  songs);
    }

    public static void replaceSong(Playlist p, ArrayList<Song> songs, ArrayList<Song> origSongs, long replacementLength, int index, long time){
       // Log.d("Music", "PlaylistGenerator.replaceSong starting");
        removeShortSongs(songs);
        ArrayList<Song> playlistSongs = p.getSongs();
        int goodFitIndex = 0;
        if(songs.size() != 0) {
            Song goodFit;
            if(!p.getSongs().get(0).getPath().equals(p.getSongs().get(index).getPath())||p.getSongs().size()<2)
                goodFit = p.getSongs().get(0);
            else
                goodFit = p.getSongs().get(1);
            for (int i = 0; i < songs.size(); i++) {
                Song s = songs.get(i);
                if (Math.abs(s.getTime() - replacementLength) < Math.abs(goodFit.getTime() - replacementLength) && !s.getPath().equals(p.getSongs().get(index))) {
                    goodFit = s;
                    goodFitIndex = i;
                }
            }
            songs.remove(goodFitIndex);
            playlistSongs.remove(index);
            playlistSongs.add(goodFit);
        }
        else{
            Log.d("Music", "Your Playlist is too long!");
            if(origSongs.size() != 0) {
                Log.d("Music", "origSongs has songs in it");
                Song goodFit;
                if(!p.getSongs().get(0).getPath().equals(p.getSongs().get(index).getPath()))
                    goodFit = p.getSongs().get(0);
                else
                    goodFit = p.getSongs().get(1);
                for (Song i : origSongs) {
                    if (Math.abs(i.getTime() - replacementLength) < Math.abs(goodFit.getTime() - replacementLength) && !(i.getPath().equals(playlistSongs.get(index).getPath()))) {
                        goodFit = i;
                        Log.d("Music", "Found a good fit");
                    }
                }
                Log.d("Music", "done with loop");
                String replacedSongPath = playlistSongs.get(index).getPath();
                playlistSongs.remove(index);
                playlistSongs.add(goodFit);
                if(playlistSongs.get(0).getPath().equals(replacedSongPath) && System.currentTimeMillis()-time < 500)
                    replaceSong(p, songs, origSongs, p.getSongs().get(0).getTime(),0,time);
                Log.d("Music", "added and removed songs");
            }
        }
       // Log.d("Music", "PlaylistGenerator.replaceSong finished");

    }

}
