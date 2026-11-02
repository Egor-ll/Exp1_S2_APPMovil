
package main.kotlin.com.pizzeria.service

import main.kotlin.com.pizzeria.model.EstadoPedido
import main.kotlin.com.pizzeria.model.Pedido
import main.kotlin.com.pizzeria.model.Repartidor

class RepartidorService(
    // Recibimos la lista de repartidores que trabajarán durante la jornada.
    private val repartidores: List<Repartidor>
) {

    fun buscarRepartidorDisponible(): Repartidor? {

        // Buscamos el primer repartidor que tenga disponibilidad.
        // El resultado puede ser null si todos se encuentran ocupados.
        return repartidores.find { it.disponible }
    }

    fun asignarRepartidor(pedido: Pedido): Repartidor? {

        // Antes de asignar a alguien, comprobamos que el pedido haya terminado su preparación.
        if (pedido.estado != EstadoPedido.LISTO) {
            println("El pedido debe estar listo antes de asignar un repartidor.")
            return null
        }

        // Consultamos la lista para encontrar a alguien que pueda realizar la entrega.
        val repartidor = buscarRepartidorDisponible()

        // Si no encontramos a nadie, informamos la situación y detenemos la asignación.
        if (repartidor == null) {
            println("No hay repartidores disponibles.")
            return null
        }

        // Informamos quién quedó asignado y devolvemos el repartidor para continuar con la entrega.
        println("Repartidor asignado: ${repartidor.nombre}")
        return repartidor
    }

    fun obtenerRepartidores(): List<Repartidor> {

        // Entregamos una copia de la lista para consultar los repartidores sin exponer la colección original.
        return repartidores.toList()
    }

    fun mostrarDisponibilidad(): String {

        // Preparamos un resumen para identificar rápidamente quién está disponible y quién está ocupado.
        return repartidores.joinToString("\n") {
            "${it.nombre}: ${if (it.disponible) "Disponible" else "Ocupado"}"
        }
    }
}
