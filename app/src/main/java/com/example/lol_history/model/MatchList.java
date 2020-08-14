package com.example.lol_history.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MatchList {
    @SerializedName("matches")
    private List<Match> match = new ArrayList<>();

    public static class Match {
        @SerializedName("gameId")
        private String gameId;

        @SerializedName("champion")
        private String champion;

        @SerializedName("queue")
        private String queue;

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public String getChampion() {
            return champion;
        }

        public void setChampion(String champion) {
            this.champion = champion;
        }

        public String getQueue() {
            return queue;
        }

        public void setQueue(String queue) {
            this.queue = queue;
        }
    }

    public List<Match> getMatch() {
        return match;
    }

    public void setMatch(List<Match> match) {
        this.match = match;
    }
}
