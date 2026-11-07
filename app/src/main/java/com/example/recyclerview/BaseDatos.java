package com.example.recyclerview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {

    public static final String NOMBRE_BD = "inventario.db";
    public static final int VERSION = 1;

    public BaseDatos(Context context) {
        super(context, NOMBRE_BD, null, VERSION);
    }

    // Se ejecuta la primera vez: crea la tabla de productos
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE productos ("
                + "clave TEXT PRIMARY KEY, "
                + "nombre TEXT NOT NULL, "
                + "descripcion TEXT, "
                + "precio REAL NOT NULL, "
                + "existencias INTEGER NOT NULL, "
                + "categoria TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS productos");
        onCreate(db);
    }
}