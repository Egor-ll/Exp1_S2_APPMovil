
package main.kotlin.com.pizzeria.model

// Extendemos Usuario para reutilizar los datos generales de cada trabajador.
// Agregamos la disponibilidad para controlar quién puede realizar una entrega.
class Repartidor(
    nombre: String,
    telefono: String,
    correo: String?,
    var disponible: Boolean = true
) : Usuario(nombre, telefono, correo) {

    fun entregarPedido(pedido: Pedido) {

        // Antes de iniciar una entrega, comprobamos que el repartidor no tenga otro pedido asignado.
        require(disponible) {
            "El repartidor $nombre no está disponible."
        }

        // Un pedido solamente puede salir a reparto cuando su preparación ha finalizado.
        require(pedido.estado == EstadoPedido.LISTO) {
            "El pedido debe estar listo antes de salir a reparto."
        }

        // Cambiamos el estado del pedido para indicar que ya salió a reparto.
        pedido.cambiarEstado(EstadoPedido.EN_CAMINO)

        // Marcamos al repartidor como ocupado para evitar que reciba otra asignación.
        disponible = false

        println("El repartidor $nombre lleva el pedido ${pedido.id}.")
    }

    // Sobrescribimos el método heredado para incluir la disponibilidad del repartidor.
    override fun mostrarDatos(): String {
        return """
            ${super.mostrarDatos()}
            Disponibilidad: ${if (disponible) "Disponible" else "Ocupado"}
        """.trimIndent()
    }
}
