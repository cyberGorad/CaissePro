package com.example.caisse_pro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "caisse_pro_db";
    private static final int DATABASE_VERSION = 1;

    // Nom de la table
    private static final String TABLE_CAISSE = "caisse";
    
    // Colonnes de la table
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MONTANT = "montant";

    // SQL de création de la table
    private static final String CREATE_TABLE_CAISSE = "CREATE TABLE " + TABLE_CAISSE + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MONTANT + " REAL);";

    // Constructeur de la classe
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crée la table caisse lors de la première exécution de l'application
        db.execSQL(CREATE_TABLE_CAISSE);

        // Insère un montant initial dans la table
        ContentValues values = new ContentValues();
        values.put(COLUMN_MONTANT, 20000.0); // Montant initial de la caisse (20000 AR)
        db.insert(TABLE_CAISSE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si la base de données est mise à jour, on supprime l'ancienne version
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAISSE);
        onCreate(db);
    }

    // Fonction pour récupérer le montant actuel de la caisse
    public double getCaisseAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CAISSE, new String[]{COLUMN_MONTANT}, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            double montant = cursor.getDouble(cursor.getColumnIndex(COLUMN_MONTANT));
            cursor.close();
            return montant;
        } else {
            return 0.0; // Retourne 0 si la table est vide
        }
    }

    // Fonction pour mettre à jour le montant de la caisse
    public void updateCaisseAmount(double montant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MONTANT, montant);

        // Met à jour le montant de la caisse
        db.update(TABLE_CAISSE, values, null, null);
    }
}
