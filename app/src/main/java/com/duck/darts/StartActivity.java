package com.duck.darts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        findViewById(R.id.startButton).setOnClickListener(this);
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        try {
            Intent intent = new Intent(this, com.duck.darts.MainActivity.class);
            int playerNumber = Integer.parseInt(((TextView) findViewById(R.id.editText)).getText().toString());
            if (playerNumber > 0 && playerNumber <= 8) {
                intent.putExtra("playerCount", playerNumber);
                startActivity(intent);
            }

        } catch (NumberFormatException nfe) {
            displayToast("Invalid input");
        } catch (NullPointerException npe) {
            displayToast("Invalid input");
        }
    }
}
