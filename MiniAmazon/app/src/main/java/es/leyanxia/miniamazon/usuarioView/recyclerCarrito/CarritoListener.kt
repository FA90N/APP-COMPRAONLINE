package es.leyanxia.miniamazon.usuarioView.recyclerCarrito

interface CarritoListener {
    fun onSumarProducto(id:Long)
    fun onRestarProducto(id:Long)

}