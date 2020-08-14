package com.example.lol_history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lol_history.model.SummonerIDinfo;
import com.example.lol_history.model.SummonerRankInfo;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    MainActivityViewModel viewModel;

    ConstraintLayout infoLayout;
    TextView summonerName;
    ImageView summonerRank;
    TextView gameType;
    TextView summonerTier;
    TextView summonerRankPoint;
    TextView summonerWinRate;
    TextView summonerWinLose;

    RecyclerView historyList;
    SwipeRefreshLayout swipeRefreshLayout;

    ConstraintLayout inputLayout;
    EditText editTextSummoner;
    Button buttonSearch;

    ProgressBar progressBar;

    InputMethodManager inputMethodManager;

    boolean isVisibleInfoLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        viewModel.getSummonerIDinfoiveData().observe(this, new Observer<SummonerIDinfo>() {
            @Override
            public void onChanged(SummonerIDinfo summonerIDinfo) {
                if(summonerIDinfo == null) {
                    Toast notExistToast = Toast.makeText(getApplicationContext(), R.string.not_exist_summoner, Toast.LENGTH_SHORT);
                    notExistToast.show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getSummonerRankInfoLiveData().observe(this, new Observer<SummonerRankInfo>() {
            @Override
            public void onChanged(SummonerRankInfo summonerRankInfo) {
                if (summonerRankInfo != null) {
                    inputLayout.setVisibility(View.GONE);
                    isVisibleInfoLayout = true;
                    setRankInfo(summonerRankInfo);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getHistoryAdapterLiveData().observe(this, new Observer<HistoryAdapter>() {
            @Override
            public void onChanged(HistoryAdapter historyAdapter) {
                if (historyAdapter == null) {
                    Toast historyErrorToast = Toast.makeText(getApplicationContext(), R.string.history_error, Toast.LENGTH_SHORT);
                    historyErrorToast.show();
                } else {
                    historyList.setAdapter(historyAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                }

                progressBar.setVisibility(View.GONE);
            }
        });

        infoLayout = findViewById(R.id.info_layout);
        summonerRank = findViewById(R.id.iv_summoner_rank);
        summonerName = findViewById(R.id.tv_summoner_name);
        gameType = findViewById(R.id.tv_game_type);
        summonerTier = findViewById(R.id.tv_summoner_tier);
        summonerRankPoint = findViewById(R.id.tv_summoner_rank_point);
        summonerWinRate = findViewById(R.id.tv_summoner_win_rate);
        summonerWinLose = findViewById(R.id.tv_summoner_win_lose);

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.searchSummoner(summonerName.getText().toString());
            }
        });
        historyList = findViewById(R.id.rv_history);
        historyList.setLayoutManager(new LinearLayoutManager(this));
        historyList.setHasFixedSize(true);

        inputLayout = findViewById(R.id.input_layout);
        editTextSummoner = findViewById(R.id.et_input_summoner);
        buttonSearch = findViewById(R.id.btn_input_summoner);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                inputMethodManager.hideSoftInputFromWindow(editTextSummoner.getWindowToken(), 0);
                viewModel.searchSummoner(editTextSummoner.getText().toString());
                editTextSummoner.setText("");
            }
        });

        progressBar = findViewById(R.id.loading);
    }

    @Override
    public void onBackPressed() {
        if (isVisibleInfoLayout) {
            infoLayout.setVisibility(View.GONE);
            inputLayout.setVisibility(View.VISIBLE);
            isVisibleInfoLayout = !isVisibleInfoLayout;
        } else {
            finish();
        }
    }

    private void setRankInfo(SummonerRankInfo summonerRankInfo) {
        setTierEmblem(summonerRankInfo.getTier());
        summonerName.setText(summonerRankInfo.getSummonerName());

        if (summonerRankInfo.getTier().equals("UNRANKED")) {
            gameType.setText("");
            summonerTier.setText("");
            summonerRankPoint.setText("");
            summonerWinRate.setText("");
            summonerWinLose.setText("");
        } else {
            String windAndLosses = summonerRankInfo.getWins() + getResources().getString(R.string.win) + " " + summonerRankInfo.getLoses() + getResources().getString(R.string.defeat);
            double rate = (double) (summonerRankInfo.getWins()) / (double) (summonerRankInfo.getLoses() + summonerRankInfo.getWins()) * 100;

            gameType.setText(summonerRankInfo.getQueueType());
            summonerTier.setText(String.valueOf(summonerRankInfo.getTier() + " " + summonerRankInfo.getRank()));
            summonerRankPoint.setText(String.valueOf(summonerRankInfo.getLeaguePoints()) + "LP");
            summonerWinRate.setText(String.format(Locale.getDefault(), "%.2f%%", rate));
            summonerWinLose.setText(windAndLosses);
        }

        infoLayout.setVisibility(View.VISIBLE);
    }

    private void setTierEmblem(String tier) {
        switch (tier) {
            case "UNRANKED":
                summonerRank.setImageResource(R.drawable.emblem_unranked);
                break;
            case "IRON":
                summonerRank.setImageResource(R.drawable.emblem_iron);
                break;
            case "BRONZE":
                summonerRank.setImageResource(R.drawable.emblem_bronze);
                break;
            case "SILVER":
                summonerRank.setImageResource(R.drawable.emblem_silver);
                break;
            case "GOLD":
                summonerRank.setImageResource(R.drawable.emblem_gold);
                break;
            case "PLATINUM":
                summonerRank.setImageResource(R.drawable.emblem_platinum);
                break;
            case "DIAMOND":
                summonerRank.setImageResource(R.drawable.emblem_diamond);
                break;
            case "MASTER":
                summonerRank.setImageResource(R.drawable.emblem_master);
                break;
            case "GRANDMASTER":
                summonerRank.setImageResource(R.drawable.emblem_grandmaster);
                break;
            case "CHALLENGER":
                summonerRank.setImageResource(R.drawable.emblem_challenger);
                break;
        }
    }
}
