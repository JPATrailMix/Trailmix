package com.example.trailmix;

/**
 * @author Andy Goering
 * @date 05/18/2018
 * Class with a set of static methods for generating and manipulating playlists and for related tasks.
 */

import android.util.Log;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PlaylistGenerator {

    /**
     * Generates a playlist of a specified duration using an ArrayList of songs.
     * @param songs
     * @param targetTime
     * @return playlist of desired time
     * @throws IOException
     */
    public static Playlist generatePlaylist(ArrayList<Song> songs, int targetTime) throws IOException{
        removeShortSongs(songs); //Get rid of songs less than 30s long
        Log.d("Music", "Songs with ringtones removed: " + songs);
        if(songs.size() != 0) {  // Check to make sure there are songs in the ArrayList before doing anything
            Random rand = new Random();
            int shortListLength = 15; //Length of list of songs to use to build the SongTree.
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

                //add random songs to the playlist until the playlist is 20 min less than the target time.
                while (randomList.getTime() < targetTime - 1200 ) {
                    Log.d("Music", "In randomList while loop");

                    if (songs.size() > 0) {
                        int r = rand.nextInt(songs.size());
                        // System.out.println("r = " + r + ", songs.size() = " + songs.size());
                        System.out.println("r = " + r);

                        randomList.add(songs.get(r));
                        songs.remove(r);
                    } else {
                        randomList.add(randomList.getSongs().get(c++).copy()); //If there aren't songs left in the ArrayList, clone songs that have already been used and use them.
                    }
                }
                Log.d("Music", "generated randomList");
                Log.d("Music", "randomList = " + randomList);

                Song[] shortSongList = new Song[shortListLength];
                int place = 0;
                if(songs.size()>= shortListLength) { // If there are enough unused songs left, randomly pick all songs for the short list from the list of unused songs.
                    Log.d("Music", "songs.size > shortListLength");
                    for (int i = 0; i < shortSongList.length; i++) {
                        int r = rand.nextInt(songs.size());
                        shortSongList[i] = songs.get(r);
                        songs.remove(r);
                    }
                }

                //Otherwise, if the total number of songs on the device is enough to make the short list to feed the SongTree, use all the unused songs first and then start repeating.
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

                else {  // If there aren't enough songs, used or not, to make the short list for the SongTreee, use everything that there is and then start repeating them.
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
                tree = new SongTree(shortSongList, targetTime - randomList.getTime(), true); //construct the tree
                hasPlaylist = tree.getPlaylists().size() > 0;
                Playlist p = tree.getClosestPlaylist();
                if (closestPlaylist == null)
                    closestPlaylist = p;
                closestPlaylist = Math.abs(targetTime - p.getTime()) < Math.abs(targetTime - closestPlaylist.getTime()) ? p
                        : closestPlaylist;
                System.out.println(p.getTime());
                songs = (ArrayList<Song>) songsClone1.clone();
            } while (!hasPlaylist && System.currentTimeMillis() - startTime < 50); //If, once in a million years due to weird user inputs (long songs, few songs, etc.), a perfect playlist isn't found, try again with another set of random songs for the tree, as long as very little time has elapsed since the algorithm has begun.
            System.out.println("Algorithm took: " + (System.currentTimeMillis() - startTime) + "ms to complete");
            if (hasPlaylist) // If perfect playlists are available, randomly pick one and concatenate it with the the random list originally generated.
                result = Playlist.concatPlaylists(randomList,
                        tree.getPlaylists().get(rand.nextInt(tree.getPlaylists().size())));
            else { //Otherwise concatenate the random list with the best playlist the SongTree arrived at
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

    /**
     * Takes the ArrayLists of song metadata and converts them to Song objects.
     * @param titles
     * @param paths
     * @param durations
     * @return ArrayList of Song objects
     */
    public static ArrayList<Song> generateSongs(ArrayList<String> titles, ArrayList<String> paths, ArrayList<Integer> durations){
        ArrayList<Song> songs = new ArrayList<Song>();
        if(titles.size() == paths.size()&& paths.size()==durations.size())
            for(int i = 0; i < titles.size(); i++)
                songs.add(new Song(titles.get(i),paths.get(i),durations.get(i)));
        return songs;
    }

    /**
     * Removes Songs with durations less than 30s from an ArrayList of Songs.
     * @param songs
     */
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

    /**
     *
     * @param p (The playlist to operate on)
     * @param songs (ArrayList of unused Songs)
     * @param origSongs (Original ArrayList of songs, including used songs)
     * @param replacementLength
     * @param index (Index in p of Song to be replaced)
     * @param time (Duration of replacement.
     */
    public static void replaceSong(Playlist p, ArrayList<Song> songs, ArrayList<Song> origSongs, long replacementLength, int index, long time){
       // Log.d("Music", "PlaylistGenerator.replaceSong starting");
        removeShortSongs(songs);
        ArrayList<Song> playlistSongs = p.getSongs();
        int goodFitIndex = 0;
        if(songs.size() != 0) { //If there are unused songs.
            Song goodFit;

            //Need to find a Song of duration replacementLength. Set the baseline as the length of the first song in the playlist, as long as that song isn't the song being skipped.
            if(!p.getSongs().get(0).getPath().equals(p.getSongs().get(index).getPath())||p.getSongs().size()<2)
                goodFit = p.getSongs().get(0);
            else // Otherwise take the second song.
                goodFit = p.getSongs().get(1);

            //Run through all the songs to see which one has a length closest to replacementLength
            for (int i = 0; i < songs.size(); i++) {
                Song s = songs.get(i);
                if (Math.abs(s.getTime() - replacementLength) < Math.abs(goodFit.getTime() - replacementLength) && !s.getPath().equals(p.getSongs().get(index))) {
                    goodFit = s;
                    goodFitIndex = i;
                }
            }

            //update playlist
            songs.remove(goodFitIndex);
            playlistSongs.remove(index);
            playlistSongs.add(goodFit);
        }
        else{ //If there aren't unused Songs.
            //Log.d("Music", "Your Playlist is too long!");
            if(origSongs.size() != 0) { //Make sure there are Songs in origSongs to work with before trying to do anything.
                Log.d("Music", "origSongs has songs in it");
                Song goodFit;

                //Need to find a Song of duration replacementLength. Set the baseline as the length of the first song in the playlist, as long as that song isn't the song being skipped.
                if(!p.getSongs().get(0).getPath().equals(p.getSongs().get(index).getPath()))
                    goodFit = p.getSongs().get(0);
                else // Otherwise take the second song.
                    goodFit = p.getSongs().get(1);

                //Run through all the songs to see which one has a length closest to replacementLength
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

                //But if, because there are so few songs in the users library, you had duplicates of the same song right next to each other, run the replace method again as long as it hasn't been trying for too long.
                if(playlistSongs.get(0).getPath().equals(replacedSongPath) && System.currentTimeMillis()-time < 500)                     replaceSong(p, songs, origSongs, p.getSongs().get(0).getTime(),0,time);
                Log.d("Music", "added and removed songs");
            }
        }
       // Log.d("Music", "PlaylistGenerator.replaceSong finished");

    }

}
