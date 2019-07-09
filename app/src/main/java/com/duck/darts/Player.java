package com.duck.darts;


import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Player {

    private int score;
    private String name;

    private ArrayList<String> playerItems = new ArrayList<String>();

    private ArrayAdapter<String> listAdapter;

    private ListView list;

    public void addScore(int score){
        if(score >= 0 && score <=240){
            this.score += score;
            playerItems.add("" + score);
            listAdapter.notifyDataSetChanged();
        }
    }

    public ArrayAdapter<String> getListAdapter() {
        return listAdapter;
    }

    public int getScore() {
        return score;
    }

    public int getRemaining(int tot){
        return tot-score;
    }


    public String getName() {
        return name;
    }

    public ArrayList<String> getPlayerItems() {
        return playerItems;
    }

    public ListView getList() {
        return list;
    }

    public Player(String name, Context context) {
        this.name = name;
        listAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1,
                playerItems);


        list = new ListView(context);
    }

    public void removeItem(int index){
        score -= Integer.parseInt(playerItems.get(index));
        playerItems.remove(index);
        listAdapter.notifyDataSetChanged();
    }
}
