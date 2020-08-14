package com.example.lol_history;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lol_history.model.MatchHistory;
import com.example.lol_history.parser.ChampionParser;
import com.example.lol_history.parser.QueueParser;
import com.example.lol_history.parser.RuneParser;
import com.example.lol_history.parser.SpellParser;
import com.example.lol_history.retrofit.BaseUrl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    private ArrayList<MatchHistory> matchHistories;
    private Context context;
    private String accountId;
    private int[] itemId= new int[]{R.id.iv_item_0,R.id.iv_item_1,R.id.iv_item_2,R.id.iv_item_3,R.id.iv_item_4,R.id.iv_item_5,R.id.iv_item_6};

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout resultLayout;
        TextView result;
        TextView durationTime;

        TextView gameType;
        ImageView championPortrait;
        ImageView spell1;
        ImageView spell2;
        ImageView keystoneRune;
        ImageView secondaryRune;

        TextView kda;
        ImageView[] item = new ImageView[7];

        ViewHolder(View itemView) {
            super(itemView);
            resultLayout = itemView.findViewById(R.id.result_layout);
            result = itemView.findViewById(R.id.tv_result);
            durationTime = itemView.findViewById(R.id.tv_duration_time);

            gameType = itemView.findViewById(R.id.tv_game_type);
            championPortrait = itemView.findViewById(R.id.iv_champion_portrait);
            spell1 = itemView.findViewById(R.id.iv_summoner_spell_1);
            spell2 = itemView.findViewById(R.id.iv_summoner_spell_2);
            keystoneRune = itemView.findViewById(R.id.iv_keystone_rune);
            secondaryRune = itemView.findViewById(R.id.iv_secondary_rune);

            kda = itemView.findViewById(R.id.tv_kda);
            for (int i = 0; i < item.length; i++) {
                item[i] = itemView.findViewById(itemId[i]);
            }
        }
    }

    HistoryAdapter(ArrayList<MatchHistory> matchHistories, String accountId) {
        Collections.sort(matchHistories, (p1, p2) -> Long.compare(p1.getGameCreation(), p2.getGameCreation()));
        Collections.reverse(matchHistories);

        this.matchHistories = matchHistories;
        this.accountId = accountId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.match_history_item, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchHistory matchHistory = matchHistories.get(position);
        int playerIndex = getPlayerIndex(matchHistory);

        if (matchHistory.getParticipants().get(playerIndex).getStats().isWin()) {
            holder.resultLayout.setBackgroundColor(context.getColor(R.color.colorWin));
            holder.result.setText(R.string.win);
        } else {
            holder.resultLayout.setBackgroundColor(context.getColor(R.color.colorDefeat));
            holder.result.setText(R.string.defeat);
        }

        holder.durationTime.setText(getDurationTime(matchHistory.getGameDuration()));

        holder.gameType.setText(getQueueType(matchHistory.getQueueId()));

        Glide.with(context).load(getChampionPortraitURL(matchHistory, playerIndex)).into(holder.championPortrait);

        Glide.with(context).load(getSpellImageURL(matchHistory, playerIndex, 1)).into(holder.spell1);
        Glide.with(context).load(getSpellImageURL(matchHistory, playerIndex, 2)).into(holder.spell2);

        Glide.with(context).load(getRuneImageURL(matchHistory, playerIndex, 1)).into(holder.keystoneRune);
        Glide.with(context).load(getRuneImageURL(matchHistory, playerIndex, 2)).into(holder.secondaryRune);

        holder.kda.setText(getKDA(matchHistory, playerIndex));

        for (int i = 0; i< itemId.length; i++) {
            String itemUrl = getItemImageURL(matchHistory, playerIndex, i);
            if (!itemUrl.isEmpty()) {
                Glide.with(context).load(itemUrl).into(holder.item[i]);
            }
        }
    }

    public int getItemCount() {
        return matchHistories.size();
    }

    private int getPlayerIndex(MatchHistory matchHistory) {
        int i = 0;
        for (MatchHistory.ParticipantIdentities participantIdentities : matchHistory.getParticipantIdentities()) {
            if (participantIdentities.getPlayer().getAccountId().equals(accountId)) {
                break;
            } else {
                i++;
            }
        }
        return i;
    }

    private String getDurationTime(long secondTime) {
        long min = secondTime / 60;
        long second = secondTime % 60;

        return min + ":" + second;
    }

    private String getQueueType(int queueId) {
        QueueParser parser = new QueueParser(context);

        return parser.getQueueType(queueId);
    }

    private String getChampionPortraitURL(MatchHistory matchHistory, int playerIndex) {
        int championId = matchHistory.getParticipants().get(playerIndex).getChampionId();
        ChampionParser parser = new ChampionParser(context);
        String championName = parser.getChampionName(championId);

        String imageUrl = BaseUrl.RIOT_DATA_DRAGON_GET_CHAMPION_PORTRAIT;
        imageUrl += championName + ".png";
        return imageUrl;
    }

    private String getSpellImageURL(MatchHistory matchHistory, int playerIndex, int spellIndex) {
        int spell = 0;
        if (spellIndex == 1) {
            spell = matchHistory.getParticipants().get(playerIndex).getSpell1Id();
        } else if (spellIndex == 2) {
            spell = matchHistory.getParticipants().get(playerIndex).getSpell2Id();
        }

        SpellParser parser = new SpellParser(context);
        String spellName = parser.getSpellName(spell);

        String imageUrl = BaseUrl.RIOT_DATA_DRAGON_GET_SPELL_IMAGE;
        imageUrl += spellName + ".png";
        return imageUrl;
    }

    private String getRuneImageURL(MatchHistory matchHistory, int playerIndex, int runeIndex) {
        int runeId = 0;
        if (runeIndex == 1) {
            runeId = matchHistory.getParticipants().get(playerIndex).getStats().getPerk0();
        } else if (runeIndex == 2) {
            runeId = matchHistory.getParticipants().get(playerIndex).getStats().getPerkSubStyle();
        }

        RuneParser parser = new RuneParser(context);
        String icon = parser.getRuneIcon(runeId);

        String imageUrl = BaseUrl.RIOT_DATA_DRAGON_GET_RUNE_IMAGE;
        imageUrl += icon;
        return imageUrl;
    }

    private String getKDA(MatchHistory matchHistory, int playerIndex) {
        int kills = matchHistory.getParticipants().get(playerIndex).getStats().getKills();
        int deaths = matchHistory.getParticipants().get(playerIndex).getStats().getDeaths();
        int assists = matchHistory.getParticipants().get(playerIndex).getStats().getAssists();

        return kills + " / " + deaths + " / " + assists;
    }

    private String getItemImageURL(MatchHistory matchHistory, int playerIndex, int itemIndex) {
        int itemId = 0;
        switch (itemIndex) {
            case 0:
                itemId = matchHistory.getParticipants().get(playerIndex).getStats().getItem0();
                break;
            case 1:
                itemId = matchHistory.getParticipants().get(playerIndex).getStats().getItem1();
                break;
            case 2:
                itemId = matchHistory.getParticipants().get(playerIndex).getStats().getItem2();
                break;
            case 3:
                itemId = matchHistory.getParticipants().get(playerIndex).getStats().getItem3();
                break;
            case 4:
                itemId = matchHistory.getParticipants().get(playerIndex).getStats().getItem4();
                break;
            case 5:
                itemId = matchHistory.getParticipants().get(playerIndex).getStats().getItem5();
                break;
            case 6:
                itemId = matchHistory.getParticipants().get(playerIndex).getStats().getItem6();
                break;
        }

        if (itemId != 0) {
            String imageUrl = BaseUrl.RIOT_DATA_DRAGON_GET_ITEM_IMAGE;
            imageUrl += itemId + ".png";
            return imageUrl;
        } else {
            return "";
        }
    }
}
