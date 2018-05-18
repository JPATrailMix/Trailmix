package com.example.trailmix;

/**
 * Class modeling independent node for the SongTree. The node knows about the nodes above and below it, but nothing else.
 * It has a means of returning playlists to the SongTree.
 */

public class Node {
    private Song song;
    private Playlist playlist;
    private Node leftChild;
    private Node rightChild;
    private SongTree songTree;
    private boolean isActive;

    /**
     * Method for constructing a Node without providing it with a song to immediately add to its playlist.
     * @param songTree
     * @param playlist
     */
    public Node(SongTree songTree, Playlist playlist) {
        this.songTree = songTree;
        isActive = true;
        this.playlist = playlist;
        checkTime();
        //System.out.println(this);
    }

    /**
     * Method for constructing a Node and providing it with a song to immediately add to its playlist.
     * @param songTree
     * @param playlist
     * @param song
     */
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

    /**
     * If the node already has children, it will just forward the song to the children.
     * If the node doesn't have children, it will create two children and give the song to one of them to tack onto its playlist.
     * @param newSong
     */
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

    /**
     * checks the playlist total time. If it is longer tha
     */
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
