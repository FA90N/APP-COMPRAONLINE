package es.leyanxia.miniamazon.usuarioView.recyclerPedido

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.modelos.Pedido
import es.leyanxia.miniamazon.recyclerPedidoDetalleCompartido.PedidoListener


class PedidoViewHolder (val item: View, val listener: PedidoListener)
    : RecyclerView.ViewHolder(item){
        val txtFecha = item.findViewById<TextView>(R.id.txtFecha)
        val txtResumen = item.findViewById<TextView>(R.id.txtResumen)
        val txtImporte = item.findViewById<TextView>(R.id.txtImporte)
        val txtEstado = item.findViewById<TextView>(R.id.txtEstado)
        lateinit var pedido:Pedido
    init {
        item.setOnClickListener {
            listener.onPedidoDetalle(pedido,adapterPosition)
        }
    }

    fun bind(p:Pedido,pos:Int){
        val f = SimpleDateFormat("dd 'de' MMMM 'de' yyyy , HH:mm:ss")
        txtFecha.text = f.format(p.fechaPedido)
        txtResumen.text=p.resumen
        txtImporte.text=p.importe.toString()+" €"
        txtEstado.text=(if(p.fechaEnvio==null) "Pendiente" else "Enviado")
        this.pedido=p
    }
}