package es.leyanxia.miniamazon.usuarioView.recyclerPedido

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.modelos.Pedido
import es.leyanxia.miniamazon.recyclerPedidoDetalleCompartido.PedidoListener

class PedidoAdapter(val datos: List<Pedido>,val listener: PedidoListener)
    :RecyclerView.Adapter<PedidoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.item_pedidos,parent,false)
        return  PedidoViewHolder(item, listener)
    }

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val p = datos[position]
        holder.bind(p,position)

    }
}