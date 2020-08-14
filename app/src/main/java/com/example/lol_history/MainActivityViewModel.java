package com.example.lol_history;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lol_history.model.MatchHistory;
import com.example.lol_history.model.MatchList;
import com.example.lol_history.model.SummonerIDinfo;
import com.example.lol_history.model.SummonerRankInfo;
import com.example.lol_history.retrofit.APIClient;
import com.example.lol_history.retrofit.RiotAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<SummonerIDinfo> summonerIDinfoiveData;
    private MutableLiveData<SummonerRankInfo> summonerRankInfoLiveData;
    private MutableLiveData<HistoryAdapter> historyAdapterLiveData;

    private String summonerName = "";

    private SummonerIDinfo summonerIDinfo = null;

    private ArrayList<MatchHistory> matchHistories = new ArrayList<>();

    private RiotAPI riotAPI = APIClient.getRiotClient().create(RiotAPI.class);

    public MainActivityViewModel() {
        summonerIDinfoiveData = new MutableLiveData<>();
        summonerRankInfoLiveData = new MutableLiveData<>();
        historyAdapterLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<SummonerIDinfo> getSummonerIDinfoiveData() {
        return summonerIDinfoiveData;
    }

    public MutableLiveData<SummonerRankInfo> getSummonerRankInfoLiveData() {
        return summonerRankInfoLiveData;
    }

    public MutableLiveData<HistoryAdapter> getHistoryAdapterLiveData() {
        return historyAdapterLiveData;
    }

    public void searchSummoner(String summonerName) {
        this.summonerName = summonerName;

        matchHistories.clear();
        getSummonerIdINfo();
    }

    private void getSummonerIdINfo() {
        riotAPI.getSummonerIdInfo(summonerName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SummonerIDinfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(SummonerIDinfo idInfo) {
                summonerIDinfo = idInfo;
                summonerName = idInfo.getName();
                getSummonerRankInfo(idInfo.getId());
                getMatchHistoryList(idInfo.getAccountId());
                Log.d("TESTLOG", "[getSummonerIdInfo] id: " + idInfo.getId());
            }

            @Override
            public void onError(Throwable e) {
                Log.d("TESTLOG", "[getSummonerIdInfo] exception: " + e);
                summonerIDinfoiveData.setValue(null);
            }
        });
    }

    private void getSummonerRankInfo(String summonerId) {
        riotAPI.getSummonerRankInfo(summonerId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<SummonerRankInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<SummonerRankInfo> summonerRankInfos) {
                setSummonerRankinfo(summonerRankInfos);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("TESTLOG", "[getSummonerRankInfo] exception: " + e);
            }
        });
    }

    private void getMatchHistoryList(String accountId) {
        riotAPI.getMatchHistoryList(accountId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<MatchList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(MatchList matchList) {
                int count = 0;
                for (MatchList.Match match : matchList.getMatch()) {
                    if (count < 15) {
                        getMatchHistory(match.getGameId(), accountId);
                        count++;
                        Log.d("TESTLOG", "matchId" + count + " : " + match.getGameId());
                    } else {
                        break;
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("TESTLOG", "[getMatchHistoryList] exception: " + e);
            }
        });
    }

    private void getMatchHistory(String matchId, String accountId) {
        riotAPI.getMatchHistory(matchId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<MatchHistory>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(MatchHistory matchHistory) {
                matchHistories.add(matchHistory);
                if(matchHistories.size() > 14) {
                    HistoryAdapter historyAdapter = new HistoryAdapter(matchHistories, accountId);

                    historyAdapterLiveData.setValue(historyAdapter);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("TESTLOG", "[getMatchHistory] exception: " + e);
                historyAdapterLiveData.setValue(null);
            }
        });
    }

    private void setSummonerRankinfo(List<SummonerRankInfo> summonerRankinfos) {
        SummonerRankInfo soloRankInfo = null;
        SummonerRankInfo flexRankInfo = null;
        int soloRankTier = 0;
        int flexRankTier = 0;

        if(summonerRankinfos.isEmpty()) {
            SummonerRankInfo unRankInfo = new SummonerRankInfo();
            unRankInfo.setTier("UNRANKED");
            unRankInfo.setRank("");
            unRankInfo.setSummonerName(summonerName);
            summonerRankInfoLiveData.setValue(unRankInfo);
        } else {
            for (SummonerRankInfo info: summonerRankinfos) {
                if (info.getQueueType().equals("RANKED_SOLO_5x5")) {
                    soloRankInfo = info;
                    soloRankTier = calcTier(info.getTier(), info.getRank(), info.getLeaguePoints());
                } else if (info.getQueueType().equals("RANKED_FLEX_5x5")) {
                    flexRankInfo = info;
                    flexRankTier = calcTier(info.getTier(), info.getRank(), info.getLeaguePoints());
                }

                if (soloRankTier < flexRankTier) {
                    summonerRankInfoLiveData.setValue(flexRankInfo);
                } else {
                    summonerRankInfoLiveData.setValue(soloRankInfo);
                }
            }
        }
    }

    private int calcTier(String tier, String rank, int lp) {
        int tierNum = 0;
        switch (tier) {
            case "IRON":
                break;
            case "BRONZE":
                tierNum = 1000;
                break;
            case "SILVER":
                tierNum = 2000;
                break;
            case "GOLD":
                tierNum = 3000;
                break;
            case "PLATINUM":
                tierNum = 4000;
                break;
            case "DIAMOND":
                tierNum = 5000;
                break;
            case "MASTER":
                tierNum = 6000;
                break;
            case "GRANDMASTER":
                tierNum = 7000;
                break;
            case "CHALLENGER":
                tierNum = 8000;
                break;
        }
        switch (rank) {
            case "I":
                tierNum += 700;
                break;
            case "II":
                tierNum += 500;
                break;
            case "III":
                tierNum += 300;
                break;
            case "IV":
                tierNum += 100;
                break;
        }
        tierNum += lp;
        return tierNum;
    }
}
