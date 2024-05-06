package es.leyanxia.miniamazon.usuarioView.recyclerCarrito

import android.annotation.SuppressLint
import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.usuarioView.bd.CarritoContract


class CarritoAdapter(
    var cursor: Cursor,
    val listener: CarritoListener,
    val life: LifecycleCoroutineScope
) : RecyclerView.Adapter<CarritoViewHolder>() {
    init {
        setHasStableIds(true)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(item, listener, life)
    }

    override fun getItemCount(): Int {
        return cursor.count
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        cursor.moveToPosition(position)
        holder.bind(cursor)
    }

    @SuppressLint("Range")
    override fun getItemId(position: Int): Long {
        cursor.moveToPosition(position)
        return cursor.getLong(cursor.getColumnIndex(CarritoContract._ID))
    }

    fun changerCursor(newCursor: Cursor) {
        cursor.close()
        cursor = newCursor
        notifyDataSetChanged()
    }
}