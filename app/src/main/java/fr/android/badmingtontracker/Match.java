package fr.android.badmingtontracker;

/**
 * Created by prit on 31/03/2018.
 */

public class Match {
    private int id;
    private String nomJoueur1;
    private String nomJoueur2;
    private int scoreJoueur1;
    private int scoreJoueur2;
    private String date;

    public Match(String nomJoueur1, String nomJoueur2, int scoreJoueur1, int scoreJoueur2, String date){
        this.date = date;
        this.nomJoueur1 = nomJoueur1;
        this.nomJoueur2 = nomJoueur2;
        this.scoreJoueur1 = scoreJoueur1;
        this.scoreJoueur2= scoreJoueur2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomJoueur1() {
        return nomJoueur1;
    }

    public void setNomJoueur1(String nomJoueur1) {
        this.nomJoueur1 = nomJoueur1;
    }

    public String getNomJoueur2() {
        return nomJoueur2;
    }

    public void setNomJoueur2(String nomJoueur2) {
        this.nomJoueur2 = nomJoueur2;
    }

    public int getScoreJoueur1() {
        return scoreJoueur1;
    }

    public void setScoreJoueur1(int scoreJoueur1) {
        this.scoreJoueur1 = scoreJoueur1;
    }

    public int getScoreJoueur2() {
        return scoreJoueur2;
    }

    public void setScoreJoueur2(int scoreJoueur2) {
        this.scoreJoueur2 = scoreJoueur2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
