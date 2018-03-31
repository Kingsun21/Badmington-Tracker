package fr.android.badmingtontracker;

/**
 * Created by prit on 30/03/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class Database extends SQLiteOpenHelper {
    private static Database instance;

    private static final String DBNAME = "matchdb";
    private static final String TABLENAME = "MatchDetail";
    public static final String _ID = "_id";
    private static final String PLAYER1 = "Player1";
    private static final String PLAYER2 = "Player2";
    public static final String SCORE1 = "score1";
    public static final String SCORE2 = "score2";
    private static final String WINNER = "winner";
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

        public void insertMatch(Match match) {
            SQLiteDatabase database = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(_ID, match.getId());
            values.put(PLAYER1, match.getNomJoueur1());
            values.put(PLAYER2, match.getNomJoueur2());
            values.put(SCORE1, match.getScoreJoueur1());
            values.put(SCORE2, match.getScoreJoueur2());
            values.put(DATE, match.getDate());
            database.insert(TABLENAME, null,values);
            database.close();
        }

        public void updateMatch(Match match) {
            SQLiteDatabase database = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(_ID, match.getId());
            values.put(PLAYER1, match.getNomJoueur1());
            values.put(PLAYER2, match.getNomJoueur2());
            values.put(SCORE1, match.getScoreJoueur1());
            values.put(SCORE2, match.getScoreJoueur2());
            values.put(DATE, match.getDate());
            database.update(TABLENAME, values,_ID+"=?", new String[]{String.valueOf(match.getId())});
            database.close();
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        onCreate(db);
    }

    /*
        public Coordonnees getLocalisation(){
            int id = 1;
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.query(TABLENAME, new String[]{_ID, LATITUDE, LONGITUDE},_ID + "=?",new String[]{String.valueOf(id)}, null,null,null);
            Coordonnees coordonnees = new Coordonnees();
            boolean toFirst = cursor.moveToFirst();
            if (cursor != null && toFirst) {
                coordonnees.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
                coordonnees.setLatitude(cursor.getInt(cursor.getColumnIndex(LATITUDE)));
                coordonnees.setLongitude(cursor.getInt(cursor.getColumnIndex(LONGITUDE)));
                cursor.close();
            }
            return coordonnees;
        }
    */
    public void resetDB(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        onCreate(db);

    }
}