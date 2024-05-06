package es.leyanxia.miniamazon.modelos

import java.util.Date

class Pedido {
    var idPedido:Int=0
    var fechaPedido: Date?=null
    var idUsuario:Int =0
    var resumen: String=""
    var importe: Double=0.0
    var fechaEnvio: Date?=null
    var lineas : ArrayList<LineaPedido>? =null
    var usuario : Usuario ?=null

}