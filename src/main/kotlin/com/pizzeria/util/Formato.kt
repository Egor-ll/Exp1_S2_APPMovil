
package main.kotlin.com.pizzeria.util

import main.kotlin.com.pizzeria.model.Pedido

// Centralizamos la presentación de precios y pedidos para mantener un formato uniforme en el programa.
object Formato {

    fun formatearPrecio(valor: Double): String {

        // Mostramos los valores sin decimales, utilizando el formato de precios de nuestra pizzería.
        return "$" + "%.0f".format(valor)
    }

    fun formatearPedido(
        pedido: Pedido,
        subtotal: Double,
        descuento: Double,
        impuestos: Double,
        total: Double
    ): String {

        // Reunimos los productos del pedido en una lista, indicando su nombre,
        // cantidad y subtotal correspondiente.
        val productos = pedido.productos.joinToString("\n") {
            "- ${it.producto.nombre} x${it.cantidad}: " +
                    formatearPrecio(it.subtotal())
        }

        // Construimos el resumen con los datos del cliente y los valores calculados.
        // También contemplamos los casos en que el correo o las notas no estén disponibles.
        return """
            ==================================
                  PIZZERÍA - PEDIDO #${pedido.id}
            ==================================
            Cliente: ${pedido.cliente.nombre}
            Teléfono: ${pedido.cliente.telefono}
            Correo: ${pedido.cliente.correo ?: "Sin correo"}

            PRODUCTOS
            $productos

            Subtotal: ${formatearPrecio(subtotal)}
            Descuento: ${formatearPrecio(descuento)}
            Impuestos: ${formatearPrecio(impuestos)}
            TOTAL: ${formatearPrecio(total)}

            Estado: ${pedido.estado}
            Notas: ${pedido.notas ?: "Sin notas"}
            ==================================
        """.trimIndent()
    }
}
