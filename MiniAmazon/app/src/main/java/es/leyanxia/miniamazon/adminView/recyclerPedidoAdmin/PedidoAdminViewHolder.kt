package es.leyanxia.miniamazon.adminView.recyclerPedidoAdmin

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.modelos.Pedido
import es.leyanxia.miniamazon.recyclerPedidoDetalleCompartido.PedidoListener

class PedidoAdminViewHolder(val item: View, val listener: PedidoListener) :
    RecyclerView.ViewHolder(item) {
    val txtFecha = item.findViewById<TextView>(R.id.txtFecha)
    val txtResumen = item.findViewById<TextView>(R.id.txtResumen)
    val txtImporte = item.findViewById<TextView>(R.id.txtImporte)
    val txtCliente = item.findViewById<TextView>(R.id.txtCliente)
    lateinit var pedido: Pedido

    init {
        item.setOnClickListener {
            listener.onPedidoDetalle(pedido, adapterPosition)
        }
    }
    fun bind(p: Pedido) {
        val f = SimpleDateFormat("dd 'de' MMMM 'de' yyyy , HH:mm:ss")
        txtFecha.text = f.format(p.fechaPedido)
        txtResumen.text = p.resumen
        txtImporte.text = p.importe.toString() + " €"
        txtCliente.text = p.usuario?.nombre.toString()
        this.pedido = p
    }
}