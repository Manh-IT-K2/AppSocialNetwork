//package com.example.frontend.utils;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.spotify.android.appremote.api.ConnectionParams;
//import com.spotify.android.appremote.api.Connector;
//import com.spotify.android.appremote.api.SpotifyAppRemote;
//import com.spotify.protocol.types.Track;
//
//public class SpotifyManager {
//
//    private static final String CLIENT_ID = "7a62e7d585ce4831a222beb840aec87a";
//    private static final String REDIRECT_URI = "echobond://auth/callback";
//    private SpotifyAppRemote mSpotifyAppRemote;
//
//    public void connect(Context context, SpotifyConnectionListener listener) {
//        ConnectionParams connectionParams = new ConnectionParams.Builder(CLIENT_ID).setRedirectUri(REDIRECT_URI).showAuthView(true).build();
//
//        SpotifyAppRemote.connect(context, connectionParams, new Connector.ConnectionListener() {
//
//            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
//                mSpotifyAppRemote = spotifyAppRemote;
//                Log.d("SpotifyManager", "Connected! Yay!");
//                if (listener != null) {
//                    listener.onConnected();
//                }
//            }
//
//            public void onFailure(Throwable throwable) {
//                Log.e("SpotifyManager", throwable.getMessage(), throwable);
//                if (listener != null) {
//                    listener.onFailure(throwable);
//                }
//            }
//        });
//    }
//
//    public interface SpotifyConnectionListener {
//        void onConnected();
//
//        void onFailure(Throwable throwable);
//    }
//
//    public void disconnect() {
//        if (mSpotifyAppRemote != null) {
//            SpotifyAppRemote.disconnect(mSpotifyAppRemote);
//        }
//    }
//
//    public SpotifyAppRemote getSpotifyAppRemote() {
//        return mSpotifyAppRemote;
//    }
//
//    public void playPlaylist(String playlistUri) {
//        if (mSpotifyAppRemote != null) {
//            mSpotifyAppRemote.getPlayerApi().play(playlistUri);
//        }
//    }
//
//    public void subscribeToPlayerState() {
//        if (mSpotifyAppRemote != null) {
//            mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(playerState -> {
//                final Track track = playerState.track;
//                if (track != null) {
//                    Log.d("SpotifyManager", track.name + " by " + track.artist.name);
//                }
//            });
//        }
//    }
//}
