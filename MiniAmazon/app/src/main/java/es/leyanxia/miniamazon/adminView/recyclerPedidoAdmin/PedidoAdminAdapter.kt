package es.leyanxia.miniamazon.adminView.recyclerPedidoAdmin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.adminView.MainActivityAdmin
import es.leyanxia.miniamazon.modelos.Pedido
import es.leyanxia.miniamazon.recyclerPedidoDetalleCompartido.PedidoListener
import es.leyanxia.miniamazon.usuarioView.recyclerPedido.PedidoViewHolder


class PedidoAdminAdapter(val datos: List<Pedido>, val listener: PedidoListener) :
    RecyclerView.Adapter<PedidoAdminViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoAdminViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.item_pedidos_admin, parent, false)
        return PedidoAdminViewHolder(item, listener)
    }

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onBindViewHolder(holder: PedidoAdminViewHolder, position: Int) {
        val p = datos[position]
        holder.bind(p)
    }

}