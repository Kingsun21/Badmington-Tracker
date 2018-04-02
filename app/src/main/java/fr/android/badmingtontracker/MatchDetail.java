package fr.android.badmingtontracker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MatchDetail extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static String ACTION_INSERT = "insertDB";
    public static String ACTION_GET_ALL = "getAllDB";
    public static String ACTION_REMOVE_ALL = "removeAllDB";

    EditText nomJoueur1;
    EditText nomJoueur2;
    EditText scoreJoueur1;
    EditText scoreJoueur2;
    EditText date;
    TextWatcher tw;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
        context = this;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    date.setText(current);
                    date.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        };

        date = findViewById(R.id.date);
        date.addTextChangedListener(tw);
        nomJoueur1 = findViewById(R.id.nomJoueur1);
        nomJoueur2 = findViewById(R.id.nomJoueur2);
        scoreJoueur1 = findViewById(R.id.scoreJoueur1);
        scoreJoueur2 = findViewById(R.id.scoreJoueur2);
    }

    public void sendToDb(View view) {
        OkHttpHandler okHttpHandler = new OkHttpHandler();
        String winner = ((Integer.parseInt(scoreJoueur1.getText().toString())>Integer.parseInt(scoreJoueur2.getText().toString())) ? nomJoueur1.getText().toString() : nomJoueur2.getText().toString());
        Match match = new Match(nomJoueur1.getText().toString(),nomJoueur2.getText().toString(),Integer.parseInt(scoreJoueur1.getText().toString()), Integer.parseInt(scoreJoueur2.getText().toString()),date.getText().toString(), winner);
        Database db = Database.getInstance(this);
        long id = db.insertMatch(match);
        match.setId(id);
        MatchDTO matchDTO = MatchDTO.translate(match);
        okHttpHandler.execute(ACTION_INSERT, matchDTO);
        onBackPressed();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class OkHttpHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();
        String urlInsert = "http://46.101.44.44:8080/badmington";
        MediaType jsonApplication = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected Object doInBackground(Object[] objects) {
            // param => action, match
            Request.Builder builder = new Request.Builder();
            String action = (String) objects[0];
            Request request = null;
            switch (action) {
                case "insertDB" :
                    MatchDTO matchDTO = (MatchDTO) objects[1];
                    String jsonSend = gson.toJson(matchDTO);
                    RequestBody body = RequestBody.create(jsonApplication,jsonSend);
                    builder.url(urlInsert);
                    builder.post(body);
                    request = builder.build();
                    break;
                default:
                    break;
            }

            if (request != null) {
                try {
                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            String result = (String) o;
            if (result.endsWith("OK")) {
                Toast.makeText(context, result, Toast.LENGTH_LONG);
            }
        }
    }

}
