package com.example.lol_history.retrofit;

import com.example.lol_history.model.MatchHistory;
import com.example.lol_history.model.MatchList;
import com.example.lol_history.model.SummonerIDinfo;
import com.example.lol_history.model.SummonerRankInfo;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface RiotAPI {

    @Headers({"Accept: application/json", "X-Riot-Token: " + BaseUrl.RIOT_API_KEY})
    @GET(BaseUrl.RIOT_API_GET_SUMMONER + "{userId}")
    Single<SummonerIDinfo> getSummonerIdInfo(@Path("userId") String userId);

    @Headers({"Accept: application/json", "X-Riot-Token: " + BaseUrl.RIOT_API_KEY})
    @GET(BaseUrl.RIOT_API_GET_RANK + "{summonerId}")
    Single<List<SummonerRankInfo>> getSummonerRankInfo(@Path("summonerId") String summonerId);

    @Headers({"Accept: application/json", "X-Riot-Token: " + BaseUrl.RIOT_API_KEY})
    @GET(BaseUrl.RIOT_API_GET_MATCH_LIST + "{accountId}")
    Single<MatchList> getMatchHistoryList(@Path("accountId") String accountId);

    @Headers({"Accept: application/json", "X-Riot-Token: " + BaseUrl.RIOT_API_KEY})
    @GET(BaseUrl.RIOT_API_GET_MATCH + "{matchId}")
    Single<MatchHistory> getMatchHistory(@Path("matchId") String matchId);
}
