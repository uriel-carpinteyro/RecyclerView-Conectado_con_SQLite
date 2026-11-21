package com.example.recyclerview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BaseDatos extends SQLiteOpenHelper {

    public static final String NOMBRE_BD = "inventario.db";
    public static final int VERSION = 1;

    private static final String COLUMNAS =
            "clave, nombre, descripcion, precio, existencias, categoria";

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

    // ---------------------- CONSULTAS (R de CRUD) ----------------------

    /** SELECT * FROM productos: regresa todos los registros guardados. */
    public List<Producto> consultarTodos() {
        List<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();          // abre la BD solo para leer
        try (Cursor cursor = db.rawQuery(
                "SELECT " + COLUMNAS + " FROM productos ORDER BY nombre", null)) {
            while (cursor.moveToNext()) {                    // recorre registro por registro
                lista.add(leerProducto(cursor));
            }
        }
        return lista;
    }

    /** Búsqueda específica: por clave exacta o por parte del nombre. */
    public List<Producto> buscar(String texto) {
        List<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT " + COLUMNAS + " FROM productos "
                + "WHERE clave = ? OR nombre LIKE ? ORDER BY nombre";
        String[] argumentos = {texto, "%" + texto + "%"};    // los ? se reemplazan de forma segura
        try (Cursor cursor = db.rawQuery(sql, argumentos)) {
            while (cursor.moveToNext()) {
                lista.add(leerProducto(cursor));
            }
        }
        return lista;
    }

    /** Convierte la fila actual del Cursor en un objeto Producto. */
    private Producto leerProducto(Cursor c) {
        return new Producto(
                c.getString(0),     // clave
                c.getString(1),     // nombre
                c.getString(2),     // descripcion
                c.getDouble(3),     // precio
                c.getInt(4),        // existencias
                c.getString(5));    // categoria
    }

    // ---------------------- ACTUALIZAR (U de CRUD) ----------------------

    /**
     * UPDATE productos SET ... WHERE clave = ?
     * La clave es la llave primaria, por eso NO se modifica: solo se usa para
     * identificar cuál registro se va a cambiar.
     * @return cuántos registros se modificaron (1 si todo salió bien, 0 si no existe la clave)
     */
    public int actualizar(Producto producto) {
        SQLiteDatabase db = getWritableDatabase();            // abre la BD en modo escritura

        ContentValues valores = new ContentValues();          // nuevos valores de los campos
        valores.put("nombre", producto.nombre);
        valores.put("descripcion", producto.descripcion);
        valores.put("precio", producto.precio);
        valores.put("existencias", producto.existencias);
        valores.put("categoria", producto.categoria);

        int filas = db.update(
                "productos",                                  // tabla que se modifica
                valores,                                      // nuevos valores
                "clave = ?",                                  // condición para identificar el registro
                new String[]{producto.clave});                // el ? se reemplaza de forma segura

        db.close();                                           // cierra la conexión
        return filas;
    }

    // ---------------------- DATOS DE EJEMPLO ----------------------

    /** Cuenta cuántos productos hay guardados. */
    public int contar() {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM productos", null)) {
            return cursor.moveToFirst() ? cursor.getInt(0) : 0;
        }
    }

    /** Si la tabla está vacía, inserta algunos productos para poder probar las consultas. */
    public void insertarEjemplosSiEstaVacia() {
        if (contar() > 0) {
            return;
        }
        insertarEjemplo("001", "Monitor", "Monitor LED de 24 pulgadas", 2500, 10, "Pantallas");
        insertarEjemplo("002", "Teclado", "Teclado inalámbrico", 450, 25, "Accesorios");
        insertarEjemplo("003", "Mouse", "Mouse óptico USB", 250, 40, "Accesorios");
        insertarEjemplo("004", "Audífonos", "Audífonos con micrófono", 600, 15, "Audio");
        insertarEjemplo("005", "Memoria USB", "Memoria USB de 64 GB", 180, 50, "Almacenamiento");
    }

    private void insertarEjemplo(String clave, String nombre, String descripcion,
                                 double precio, int existencias, String categoria) {
        ContentValues valores = new ContentValues();
        valores.put("clave", clave);
        valores.put("nombre", nombre);
        valores.put("descripcion", descripcion);
        valores.put("precio", precio);
        valores.put("existencias", existencias);
        valores.put("categoria", categoria);
        getWritableDatabase().insert("productos", null, valores);
    }
}