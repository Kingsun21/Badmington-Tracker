package fr.android.badmingtontracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LastResultsDetails extends AppCompatActivity {

    private long id = -1;
    TextView tvNom1;
    TextView tvNom2;
    TextView tvScore1;
    TextView tvScore2;
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_results_details);

        Bundle msg = getIntent().getExtras();

        if (msg != null) {
            id = msg.getLong(Database._ID);
            Database database = Database.getInstance(this);
            Match match = database.getMatch(id);
            tvNom1 = findViewById(R.id.viewNomJoueur1);
            tvNom2 = findViewById(R.id.viewNomJoueur2);
            tvScore1 = findViewById(R.id.viewScoreJoueur1);
            tvScore2 = findViewById(R.id.viewScoreJoueur2);
            tvDate = findViewById(R.id.viewDate);

            tvNom1.setText(match.getNomJoueur1()+ "  ");
            tvNom2.setText("  "+match.getNomJoueur2());
            tvScore1.setText(Integer.toString(match.getScoreJoueur1()));
            tvScore2.setText(Integer.toString(match.getScoreJoueur2()));
            tvDate.setText(match.getDate());
        }
    }
}
