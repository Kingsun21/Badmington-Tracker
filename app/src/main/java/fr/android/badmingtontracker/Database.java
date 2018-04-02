package fr.android.badmingtontracker;

/**
 * Created by prit on 30/03/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class Database extends SQLiteOpenHelper {
    private static Database instance;

    private static final String DBNAME = "matchdb";
    private static final String TABLENAME = "MatchDetail";
    public static final String _ID = "_id";
    private static final String PLAYER1 = "Player1";
    private static final String PLAYER2 = "Player2";
    private static final String SCORE1 = "score1";
    private static final String SCORE2 = "score2";
    public static final String WINNER = "winner";
    private static final String DATE = "date";
    private static final String LOCATION = "street";
    private static final String SQLSELECTALL = "SELECT  * FROM "+TABLENAME;

    private static final String CREATE_DATABASE = "CREATE TABLE "+ TABLENAME
            + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + PLAYER1 + " TEXT, "
            + PLAYER2 + " TEXT, "
            + SCORE1 + " INTEGER, "
            + SCORE2 + " INTEGER, "
            + WINNER + " TEXT, "
            + DATE + " TEXT, "
            + LOCATION + " TEXT);";

    public static Database getInstance(Context context){
        if (instance == null){
            instance = new Database(context);
        }
        return instance;
    }

    public Database(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DATABASE);
    }

    public void initialisationMatch(Match match) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYER1, match.getNomJoueur1());
        values.put(PLAYER2, match.getNomJoueur2());
        values.put(SCORE1, match.getScoreJoueur1());
        values.put(SCORE2, match.getScoreJoueur2());
        values.put(DATE, match.getDate());
        database.insert(TABLENAME, null,values);
        database.close();
    }

    public int nbRows(){
        SQLiteDatabase database = getReadableDatabase();
        int numberRows = (int) DatabaseUtils.queryNumEntries(database,TABLENAME);
        return numberRows;
    }

    public long insertMatch(Match match) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYER1, match.getNomJoueur1());
        values.put(PLAYER2, match.getNomJoueur2());
        values.put(SCORE1, match.getScoreJoueur1());
        values.put(SCORE2, match.getScoreJoueur2());
        values.put(DATE, match.getDate());
        values.put(WINNER, match.getWinner());
        long idInsert = database.insert(TABLENAME, null, values);
        if(nbRows()>5){
            deleteMatch(idInsert - 5);
        }
        database.close();
        return idInsert;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        onCreate(db);
    }


    public Match getMatch(long id){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLENAME, new String[]{_ID, PLAYER1, PLAYER2, SCORE1, SCORE2, DATE, WINNER},_ID + "=?",new String[]{String.valueOf(id)}, null,null,null);
        Match match = new Match();
        boolean toFirst = cursor.moveToFirst();
        if (cursor != null && toFirst) {
            match.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
            match.setNomJoueur1(cursor.getString(cursor.getColumnIndex(PLAYER1)));
            match.setNomJoueur2(cursor.getString(cursor.getColumnIndex(PLAYER2)));
            match.setScoreJoueur1(cursor.getInt(cursor.getColumnIndex(SCORE1)));
            match.setScoreJoueur2(cursor.getInt(cursor.getColumnIndex(SCORE2)));
            match.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
            match.setWinner(cursor.getString(cursor.getColumnIndex(WINNER)));
            cursor.close();
        }
        return match;
    }

    public Cursor getContacts(){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(SQLSELECTALL, null);
    }


    public void resetDB(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        onCreate(db);

    }

    public void deleteMatch(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLENAME,_ID + " = ?",
                new String[] {String.valueOf(id)});
        db.close();
    }

}