package fr.android.badmingtontracker;


public class MatchDTO {
    // object for DB away
    private long id;
    private String player1;
    private String player2;
    private int score1;
    private int score2;
    private String date;
    private String winner;
    private String location;

    public MatchDTO(long id, String player1, String player2, int score1, int score2, String date, String winner, String location) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.score1 = score1;
        this.score2 = score2;
        this.date = date;
        this.winner = winner;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static MatchDTO translate(Match match) {
        return new MatchDTO(match.getId(),
                match.getNomJoueur1(),
                match.getNomJoueur2(),
                match.getScoreJoueur1(),
                match.getScoreJoueur2(),
                match.getDate(),
                match.getWinner(),
                match.getLocation());
    }
}
