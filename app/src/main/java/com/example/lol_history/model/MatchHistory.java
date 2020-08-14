package com.example.lol_history.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MatchHistory {
    @SerializedName("gameId")
    private String gameId;

    @SerializedName("queueId")
    private int queueId;

    public int getQueueId() {
        return queueId;
    }

    @SerializedName("gameCreation")
    private long gameCreation;

    @SerializedName("gameDuration")
    private long gameDuration;

    @SerializedName("participants")
    private ArrayList<Participants> participants = new ArrayList<>();

    @SerializedName("participantIdentities")
    private ArrayList<ParticipantIdentities> participantIdentities = new ArrayList<>();

    public static class Participants {
        @SerializedName("championId")
        private int championId;

        @SerializedName("spell1Id")
        private int spell1Id;

        @SerializedName("spell2Id")
        private int spell2Id;

        @SerializedName("stats")
        private Stats stats;

        public int getChampionId() {
            return championId;
        }

        public int getSpell1Id() {
            return spell1Id;
        }

        public int getSpell2Id() {
            return spell2Id;
        }

        public Stats getStats() {
            return stats;
        }

        public static class Stats {
            @SerializedName("win")
            private boolean win;

            @SerializedName("item0")
            private int item0;

            @SerializedName("item1")
            private int item1;

            @SerializedName("item2")
            private int item2;

            @SerializedName("item3")
            private int item3;

            @SerializedName("item4")
            private int item4;

            @SerializedName("item5")
            private int item5;

            @SerializedName("item6")
            private int item6;

            @SerializedName("kills")
            private int kills;

            @SerializedName("deaths")
            private int deaths;

            @SerializedName("assists")
            private int assists;

            @SerializedName("perk0")
            private int perk0;

            @SerializedName("perkSubStyle")
            private int perkSubStyle;

            public boolean isWin() {
                return win;
            }

            public int getItem0() {
                return item0;
            }

            public int getItem1() {
                return item1;
            }

            public int getItem2() {
                return item2;
            }

            public int getItem3() {
                return item3;
            }

            public int getItem4() {
                return item4;
            }

            public int getItem5() {
                return item5;
            }

            public int getItem6() {
                return item6;
            }

            public int getKills() {
                return kills;
            }

            public int getDeaths() {
                return deaths;
            }

            public int getAssists() {
                return assists;
            }

            public int getPerk0() {
                return perk0;
            }

            public int getPerkSubStyle() {
                return perkSubStyle;
            }
        }
    }

    public static class ParticipantIdentities {
        @SerializedName("participantId")
        private int participantId;

        @SerializedName("player")
        private Player player;

        public static class Player {
            @SerializedName("accountId")
            private String accountId;

            @SerializedName("summonerName")
            private String summonerName;

            public String getAccountId() {
                return accountId;
            }

            public String getSummonerName() {
                return summonerName;
            }
        }

        public int getParticipantId() {
            return participantId;
        }

        public Player getPlayer() {
            return player;
        }
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public long getGameCreation() {
        return gameCreation;
    }

    public void setGameCreation(long gameCreation) {
        this.gameCreation = gameCreation;
    }

    public long getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(long gameDuration) {
        this.gameDuration = gameDuration;
    }

    public ArrayList<Participants> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participants> participants) {
        this.participants = participants;
    }

    public ArrayList<ParticipantIdentities> getParticipantIdentities() {
        return participantIdentities;
    }

    public void setParticipantIdentities(ArrayList<ParticipantIdentities> participantIdentities) {
        this.participantIdentities = participantIdentities;
    }
}
