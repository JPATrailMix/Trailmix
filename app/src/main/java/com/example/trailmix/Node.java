package com.example.trailmix;

/**
 * Created by andrewgoering on 3/31/18.
 */

public class Node {
    private Song song;
    private Playlist playlist;
    private Node leftChild;
    private Node rightChild;
    private SongTree songTree;
    private boolean isActive;

    public Node(SongTree songTree, Playlist playlist) {
        this.songTree = songTree;
        isActive = true;
        this.playlist = playlist;
        checkTime();
        //System.out.println(this);
    }

    public Node(SongTree songTree, Playlist playlist, Song song) {
        this.songTree = songTree;
        isActive = true;
        this.song = song;
        this.playlist = playlist;
        playlist.add(song);
        checkTime();
        //System.out.println(this);
    }

    public String toString() {
        String s = "";
        if(song != null)
            s = "Song = " + song.toString();
        s+="\nplaylist = " + playlist.toString();
        return s;
    }

    public void addSong(Song newSong) {
        if(isActive && rightChild == null) {
            rightChild = new Node(songTree, Playlist.copy(playlist), newSong);
            leftChild = new Node(songTree, Playlist.copy(playlist));
        }
        else if(isActive) {
            rightChild.addSong(newSong);
            leftChild.addSong(newSong);
        }
    }

    private void checkTime() {
        if(playlist.getTime() == songTree.getTargetTime()) {
            isActive = false;
            songTree.addPlaylist(playlist);
        }
        else if(song != null && songTree.getTargetTime() - playlist.getTime() < song.getTime())
            isActive = false;
        else
            isActive = true;
        songTree.submitPlaylistWithError(playlist);
        //System.out.println("submitting playlist with time " + playlist.getTime());
    }


}
