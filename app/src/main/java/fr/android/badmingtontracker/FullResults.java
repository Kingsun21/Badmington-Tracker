package fr.android.badmingtontracker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FullResults extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    ListView listView;
    ArrayAdapter<String> adapter;
    String[] resultats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_results);
        context = this;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listView = (ListView) findViewById(R.id.listview_full);
        OkHttpHandler okHttpHandler = new OkHttpHandler();
        okHttpHandler.execute(MatchDetail.ACTION_GET_ALL);
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
            // Handle the camera action
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
        String urlGetAllDB = "http://46.101.44.44:8080/badmington/all";
        String urlRemoveAllDB = "http://46.101.44.44:8080/badmington/all";
        MediaType jsonApplication = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected Object doInBackground(Object[] objects) {
            // param => action, match
            Request.Builder builder = new Request.Builder();
            String action = (String) objects[0];
            Request request = null;
            switch (action) {
                case "getAllDB":
                    builder.url(urlGetAllDB);
                    builder.get();
                    request = builder.build();
                    break;
                case "removeAllDB":
                    builder.url(urlRemoveAllDB);
                    builder.delete();
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
            } else if (result.contains("{")) {
                Type listType = new TypeToken<ArrayList<MatchDTO>>() {
                }.getType();
                List<MatchDTO> matchDTOList = gson.fromJson(result, listType);
                resultats = new String[matchDTOList.size()];
                for (int i = 0; i < matchDTOList.size(); i++) {
                    MatchDTO matchDTO = matchDTOList.get(i);
                    String line = matchDTO.getId() +
                            " - " +
                            matchDTO.getPlayer1() + " (" + matchDTO.getScore1() + ") " +
                            " - " +
                            " (" + matchDTO.getScore2() + ") " + matchDTO.getPlayer2() +
                            " - DATE : " + matchDTO.getDate();
                    resultats[i] = line;
                }
                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_activated_1, resultats);
                listView.setAdapter(adapter);
            }
        }
    }


}
