package com.duck.darts;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

@SuppressWarnings("Convert2Diamond")
public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener, AdapterView.OnItemLongClickListener, View.OnLongClickListener {


    private int totScore = 300;
    private ArrayList<Player> players = new ArrayList<Player>();
    private TableLayout table ;

    private void setTotScore(int totScore) {
        if (totScore > 0) {
            this.totScore = totScore;
        } else {
            displayToast("[FATAL ERROR] total score invalid, using default");
            this.totScore = 300;
        }
    }

    private void createPlayers(int playerNum) {

        for (int i = 0; i < playerNum; i++) {

            players.add(new Player("Player" + (i + 1), getApplicationContext()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        createPlayers(getIntent().getIntExtra("playerCount", 2));
        table = findViewById(R.id.table);
        initComponents();

    }

    private void initComponents() {


        LinearLayout layout = getNewLayout();
        TableRow row = getNewTableRow();

        float font   = ((-360f/14f)/15)*(float)players.size()+(360f/14f);
        Log.d("density: " , "dp: " + font);


        for (Player player : players) {

            TextView label = new TextView(getApplicationContext());
            label.setId(View.generateViewId());
            label.setText(player.getName());
            label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, font);
            label.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            label.setOnLongClickListener(this);
            layout.addView(label);


        }

        row.addView(layout);
        table.addView(row);


        layout = getNewLayout();
        row = getNewTableRow();

        for (Player player : players) {

            EditText input = new EditText(getApplicationContext());
            input.setId(View.generateViewId());
            input.setTag(player.getName() + "Input");
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            layout.addView(input);
        }
        row.addView(layout);
        table.addView(row);


        layout = getNewLayout();
        row = getNewTableRow();

        for (Player player : players) {

            Button button = new Button(getApplicationContext());
            button.setId(View.generateViewId());
            button.setTag(player.getName() + "Button");
            button.setText(player.getName() + " confirm");
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, font/2);
            button.setOnClickListener(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            button.setOnClickListener(this);
            layout.addView(button);

        }
        row.addView(layout);
        table.addView(row);

        createLists();
    }

    private void createLists() {


        LinearLayout layout = getNewLayout();
        TableRow row = getNewTableRow();

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();

        display.getSize(size);

        for (Player player : players) {

            ListView list = player.getList();
            list.setId(View.generateViewId());

            list.setLayoutParams(new LinearLayout.LayoutParams(ListView.LayoutParams.MATCH_PARENT, size.y - 1100, 1.0f));

            list.setAdapter(player.getListAdapter());

            GradientDrawable border = new GradientDrawable();
            border.setColor(0xFFFFFF);
            border.setStroke(1, 0x000000);
            list.setBackground(border);
            list.setOnItemLongClickListener(this);
            layout.addView(list);
        }

        row.addView(layout);
        table.addView(row);
    }

    private LinearLayout getNewLayout() {
        LinearLayout layout = new LinearLayout(getApplicationContext());
        TableRow.LayoutParams layoutLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1);

        layout.setLayoutParams(layoutLayoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        return layout;

    }

    private TableRow getNewTableRow() {
        TableRow row = new TableRow(getApplicationContext());

        TableRow.LayoutParams rowLayoutParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        row.setLayoutParams(rowLayoutParam);
        return row;
    }

    private void addScore(Player player, int score) {
        if (score >= 0 && score <= 240) {
            player.addScore(score);
            displayToast("[INFO] Remaining: " + player.getRemaining(totScore));
        } else {
            displayToast("[INFO] value out of range");
        }
    }

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateTotalScore() {
        try {
            setTotScore(Integer.parseInt(((EditText) findViewById(R.id.totalScore)).getText().toString()));
        } catch (NumberFormatException nfe) {
            displayToast("[FATAL ERROR] unable to parse tot Score");
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (((EditText) findViewById(R.id.totalScore)).getText().length() > 20)
            updateTotalScore();
        return false;
    }

    @Override
    public void onClick(View view) {
        displayToast("Button pressed: " + ((Button) view).getText().toString());


        try {
            String playerName = ((Button) view).getText().toString().replace(" confirm", "");
            EditText input = getInputByPlayerName(playerName);
            addScore(getPlayerByName(playerName), Integer.parseInt(input.getText().toString()));
            input.setText("");
        } catch (NumberFormatException nfe) {
            displayToast("Unable to read input, No number set?");
        } catch (NullPointerException npe) {
            displayToast(npe.getMessage());
        }


    }

    private Player getPlayerByName(String name) {
        for (Player player :
                players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    private EditText getInputByPlayerName(String name) {
        String finalString = name + "Input";

        TableRow row = (TableRow) table.getChildAt(3);
        LinearLayout layout = (LinearLayout) row.getChildAt(0);

        for (int i = 0; i < layout.getChildCount(); i++) {
            EditText input = (EditText) layout.getChildAt(i);
            String inputTag = (input).getTag().toString();

            if (inputTag.equals(finalString)) {
                return input;
            }
        }

        return null;
    }

    @Override
    public boolean onLongClick(View view) {

        displayToast("[INFO] Remaining: " + getPlayerByName(((TextView) view).getText().toString()).getRemaining(totScore));
        return false;
    }

    @org.jetbrains.annotations.Nullable
    private Player getPlayerByAdapter(Adapter adapter) {
        for (Player player :
                players) {
            if (player.getListAdapter().equals(adapter)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        try {
            getPlayerByAdapter(adapterView.getAdapter()).removeItem(i);
        } catch (NullPointerException npe) {

            displayToast("[FATAL ERROR] Unable to find player by adapter");
        }
        return false;
    }
}
