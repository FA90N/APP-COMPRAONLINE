package es.leyanxia.miniamazon.recyclerPedidoDetalleCompartido

import es.leyanxia.miniamazon.modelos.Pedido

interface PedidoListener {
    fun onPedidoDetalle(pedido: Pedido, pos:Int)
}