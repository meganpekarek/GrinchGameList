package com.example.meganpekarek.grinchgame;

//import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meganpekarek.grinchgame.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.example.meganpekarek.grinchgame.Trivia;

public class MainActivity extends AppCompatActivity {

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    TextView trivia;
    TextView mission;
    TextView directions;
    TextView timer;
    ImageView house;
    ImageView house2;
    ImageView house3;
    int correct;
    int incorrect;
    int points = 50;
    int items;
    List<Trivia> list;
    String[] itemString = {"No houses", "House 1", "House 2", "House 3"};
    Random rand = new Random();
    int value = rand.nextInt(4);


    private HashMap<String, Trivia> descriptions;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jsonParse(getApplicationContext());
        init();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        menu.add("New Game");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setContentView(R.layout.activity_main);
        jsonParse(getApplicationContext());
        init();
        return true;
    }

    public List<Trivia> jsonParse(Context context) {
        list = null;
        Gson gson = new Gson();
        BufferedReader reader = null;
        try
        {
            reader  = new BufferedReader(new InputStreamReader(context.getAssets().open("QuestionInfo")));
            list = gson.fromJson(reader, new TypeToken<List<Trivia>>(){}.getType());
        }
        catch (IOException e) {
            System.out.println("Error reading vehicle spec assets file: " + e.getLocalizedMessage());
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error reading vehicle spec assets file: " + e.getLocalizedMessage());
                }
            }
        }
        return list;
    }

    public void init() {
        items = 0;
        points = 50;
        correct = 0;
        timer = (TextView) findViewById(R.id.timer);

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText(""+String.format("%d min %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timer.setText("done!");
                results(points);
            }
        }.start();

        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setText(list.get(value).getAnswerRight());
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setText(list.get(value).getAnswer1());
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setText(list.get(value).getAnswer2());
        btn4 = (Button) findViewById(R.id.btn4);
        btn4.setText(list.get(value).getAnswer3());

        mission = (TextView) findViewById(R.id.mission);
        mission.setText(getString(R.string.mission));

        directions = (TextView) findViewById(R.id.directions);
        directions.setText(getString(R.string.directions));

        trivia = (TextView) findViewById(R.id.trivia);
        trivia.setGravity(Gravity.CENTER);
        trivia.setText(getString(R.string.firstQ) + list.get(value).getQuestion());

        btn1.setOnClickListener(new MyClickListener());
        btn2.setOnClickListener(new MyClickListener());
        btn3.setOnClickListener(new MyClickListener());
        btn4.setOnClickListener(new MyClickListener());


    }

    public void display(String question, String ansRight, String ans2, String ans3, String ans4, String result, int points) {
        int random = rand.nextInt(4);
        if(points <= 0) {
            results(this.points);
        }
        if(correct >= 4 && correct < 6) {
            items = 1;
            house = (ImageView) findViewById(R.id.house_one);
            house.setImageResource(R.drawable.house_one);
            String text = result + "\n" + "You now have access to: " + itemString[items] +"\n" + "Score: " + points + "\n" + question;
            trivia.setGravity(Gravity.CENTER);
            this.trivia.setText(text);
        }
        else if(correct >= 6 && correct < 8) {
            items = 2;
            house2 = (ImageView) findViewById(R.id.house_two);
            house2.setImageResource(R.drawable.house_two);
            String text = result + " " + "You now have access to: " + itemString[items-1] +" and " + itemString[items] + "\n" + "Score: " + points + "\n" + question;
            trivia.setGravity(Gravity.CENTER);
            this.trivia.setText(text);
        }
        else if(correct >= 8) {
            items = 3;
            house3 = (ImageView) findViewById(R.id.house_three);
            house3.setImageResource(R.drawable.house_three);
            String text = result + " " + "You now have access to: " + itemString[items-2] +", " + itemString[items-1] + " and " +itemString[items] + "\n" + "Score: " + points + "\n" + question;
            trivia.setGravity(Gravity.CENTER);
            this.trivia.setText(text);
            results(points);
        }
        else {
            String text = result + " " + "Score: " + points + "\n" + question;
            trivia.setGravity(Gravity.CENTER);
            this.trivia.setText(text);
        }
        if(random == 0) {
            btn1.setText(ansRight);
            btn2.setText(ans2);
            btn3.setText(ans3);
            btn4.setText(ans4);
        }
        else if(random == 1) {
            btn1.setText(ans3);
            btn2.setText(ansRight);
            btn3.setText(ans2);
            btn4.setText(ans4);
        }
        else if(random == 2) {
            btn1.setText(ans3);
            btn2.setText(ans2);
            btn3.setText(ans4);
            btn4.setText(ansRight);
        }
        else {
            btn1.setText(ans3);
            btn2.setText(ans2);
            btn3.setText(ansRight);
            btn4.setText(ans4);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.meganpekarek.grinchgame/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.meganpekarek.grinchgame/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void results(int points) {
        if(items < 3) {
            trivia.setText("You didn't steal Christmas this year!");
            trivia.setTextSize(60);
        }
        else {
            trivia.setText("You stole Christmas! With an ending score of: " + this.points);
            trivia.setTextSize(60);
        }
        btn1.setVisibility(View.INVISIBLE);
        btn2.setVisibility(View.INVISIBLE);
        btn3.setVisibility(View.INVISIBLE);
        btn4.setVisibility(View.INVISIBLE);

    }

    public void buttonMsg(int points, int i, int value, String result, Button btn) {
        if (btn.getText().equals(list.get(value).getAnswerRight())) {
            this.points = points + list.get(value).getPointChange();
            result = "Correct!";
            correct++;
        } else {
            this.points = points - list.get(value).getPointChange();
            result = "InCorrect!";
            incorrect++;
        }
        if (i <= 9) {
            if (value < list.size()) {
                list.remove(value);
                if(this.points <= 0) {
                    results(this.points);
                }
                else if (value < list.size())
                    display(list.get(value).getQuestion(), list.get(value).getAnswerRight(), list.get(value).getAnswer1(), list.get(value).getAnswer2(), list.get(value).getAnswer3(), result, this.points);
                else
                    results(points);
            }
        }
    }


    private class MyClickListener implements View.OnClickListener {
        int i = 0;
        public void onClick(View view) {
            mission.setVisibility(View.INVISIBLE);
            directions.setVisibility(View.INVISIBLE);
            String result = "";
            i = i+1;
            if(i <= 9) {
                switch (view.getId()) {
                    case R.id.btn1:
                        buttonMsg(points, i, value, result, btn1);
                        break;
                    case R.id.btn2:
                        buttonMsg(points, i, value, result, btn2);
                        break;
                    case R.id.btn3:
                        buttonMsg(points, i, value, result, btn3);
                        break;
                    case R.id.btn4:
                        buttonMsg(points, i, value, result, btn4);
                        break;
                }
            }
            else
                results(points);
        }
    }
}

