package es.leyanxia.miniamazon.usuarioView.bd

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class CarritoSQLITE(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {
    companion object {
        const val DB_NAME = "Carrito.db"
        const val VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE " + CarritoContract.TABLE_CARRITO + " (" +
                    CarritoContract._ID + " INTEGER PRIMARY KEY, " +
                    CarritoContract.IDPRODUCTO + " INTEGER NOT NULL, " +
                    CarritoContract.UNIDADES + " INTEGER NOT NULL " + ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertar(carrito: Carrito) {

        val db = writableDatabase
        val values = ContentValues()
        values.put(CarritoContract.IDPRODUCTO, carrito.idProducto)
        values.put(CarritoContract.UNIDADES, carrito.unidades)
        val id = db.insert(
            CarritoContract.TABLE_CARRITO, null, values
        )
        carrito.id = id
    }

    @SuppressLint("Range")
    fun comprobar(id: Int): Carrito? {
        val db = readableDatabase
        val c = db.query(
            CarritoContract.TABLE_CARRITO, null,
            CarritoContract.IDPRODUCTO + " = ? ",
            arrayOf(id.toString()),
            null, null, null
        )
        var car: Carrito? = null
        if (c.moveToNext()) {
            car = Carrito()
            car.id = c.getLong(c.getColumnIndex(CarritoContract._ID))
            car.idProducto = c.getInt(c.getColumnIndex(CarritoContract.IDPRODUCTO))
            car.unidades = c.getInt(c.getColumnIndex(CarritoContract.UNIDADES))

        }
        return car
    }

    fun actualizar(carrito: Carrito) {
        var c = carrito.unidades
        val db = writableDatabase
        val values = ContentValues()
        values.put(CarritoContract.UNIDADES, c)
        db.update(
            CarritoContract.TABLE_CARRITO, values,
            CarritoContract._ID + " = ? ",
            arrayOf(carrito.id.toString())
        )
    }

    fun listado(): Cursor {
        val bd = readableDatabase
        val cursor = bd.query(
            CarritoContract.TABLE_CARRITO,
            null, null, null, null, null, null
        )
        return cursor
    }

    fun addEjercicio(id: Long) {
        val db = readableDatabase
        db.execSQL(
            "UPDATE ${CarritoContract.TABLE_CARRITO} " +
                    "set ${CarritoContract.UNIDADES}=${CarritoContract.UNIDADES}+1  where ${CarritoContract._ID}=?",
            arrayOf(id.toString())
        )

    }

    fun restarEjercicio(id: Long) {
        val db = readableDatabase
        db.execSQL(
            "UPDATE ${CarritoContract.TABLE_CARRITO} " +
                    "set ${CarritoContract.UNIDADES}=${CarritoContract.UNIDADES}-1  where ${CarritoContract._ID}=?",
            arrayOf(id.toString())
        )

    }

    fun borrar(id: Long) {
        val db = writableDatabase
        db.delete(
            CarritoContract.TABLE_CARRITO,
            CarritoContract._ID + "= ?",
            arrayOf(id.toString())
        )
    }

    fun borrarTodo() {
        val db = writableDatabase
        db.delete(
            CarritoContract.TABLE_CARRITO,
            null, null
        )

    }

    @SuppressLint("Range")
    fun cargar(id: Long): Carrito? {
        val db = readableDatabase
        val c = db.query(
            CarritoContract.TABLE_CARRITO, null,
            CarritoContract._ID + " = ? ",
            arrayOf(id.toString()),
            null, null, null
        )
        var car: Carrito? = null
        if (c.moveToNext()) {
            car = Carrito()
            car.id = c.getLong(c.getColumnIndex(CarritoContract._ID))
            car.idProducto = c.getInt(c.getColumnIndex(CarritoContract.IDPRODUCTO))
            car.unidades = c.getInt(c.getColumnIndex(CarritoContract.UNIDADES))

        }
        return car
    }

    @SuppressLint("Range")
    fun cargarTodo(): ArrayList<Carrito> {
        val db = readableDatabase
        val c = db.query(
            CarritoContract.TABLE_CARRITO,
            null, null, null, null, null, null
        )
        var car: Carrito? = null
        var lista = arrayListOf<Carrito>()
        while (c.moveToNext()) {
            car = Carrito()
            car.idProducto = c.getInt(c.getColumnIndex(CarritoContract.IDPRODUCTO))
            car.unidades = c.getInt(c.getColumnIndex(CarritoContract.UNIDADES))
            lista.add(car)
        }
        return lista
    }


}