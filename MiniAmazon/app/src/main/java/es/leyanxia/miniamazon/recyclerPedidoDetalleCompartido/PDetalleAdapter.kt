package es.leyanxia.miniamazon.recyclerPedidoDetalleCompartido

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.modelos.LineaPedido

class PDetalleAdapter(val datos:List<LineaPedido>)
    :RecyclerView.Adapter<PDetalleViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PDetalleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.item_pedidos_detalle,parent,false)
        return PDetalleViewHolder(item)
    }

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onBindViewHolder(holder: PDetalleViewHolder, position: Int) {
        val p = datos[position]
        holder.bind(p)
    }
}