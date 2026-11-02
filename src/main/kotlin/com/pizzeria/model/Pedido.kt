
package main.kotlin.com.pizzeria.model

// Definimos los estados por los que puede pasar un pedido durante su preparación y entrega.
enum class EstadoPedido {
    EN_PREPARACION,
    LISTO,
    EN_CAMINO
}

// Representamos cada producto solicitado y la cantidad que el cliente desea comprar.
data class DetallePedido(
    val producto: Producto,
    val cantidad: Int
) {

    // Evitamos que se registren cantidades iguales o inferiores a cero.
    init {
        require(cantidad > 0) {
            "La cantidad debe ser mayor que cero."
        }
    }

    // Calculamos cuánto cuesta este producto según las unidades solicitadas.
    fun subtotal(): Double = producto.precio * cantidad
}

class Pedido(
    val id: Int,
    val cliente: Cliente,
    val productos: List<DetallePedido>,
    val notas: String? = null
) {

    // Todo pedido comienza en preparación.
    // El private set evita que otras clases cambien el estado directamente.
    var estado: EstadoPedido = EstadoPedido.EN_PREPARACION
        private set

    // Comprobamos que el pedido tenga un identificador válido y al menos un producto.
    init {
        require(id > 0) {
            "El ID del pedido debe ser positivo."
        }

        require(productos.isNotEmpty()) {
            "El pedido debe contener al menos un producto."
        }
    }

    fun calcularSubtotal(): Double {

        // Sumamos los subtotales de todos los productos para obtener el valor antes de descuentos e impuestos.
        return productos.sumOf { it.subtotal() }
    }

    // Controlamos que el pedido avance solamente al siguiente estado permitido.
    fun cambiarEstado(nuevoEstado: EstadoPedido) {

        val cambioPermitido = when (estado) {
            EstadoPedido.EN_PREPARACION ->
                nuevoEstado == EstadoPedido.LISTO

            EstadoPedido.LISTO ->
                nuevoEstado == EstadoPedido.EN_CAMINO

            EstadoPedido.EN_CAMINO -> false
        }

        require(cambioPermitido) {
            "No se puede cambiar el pedido de $estado a $nuevoEstado."
        }

        estado = nuevoEstado
    }

    // Obtiene las notas del pedido y controla los casos en que no existen.
    fun obtenerNotas(): String {

        // let permite trabajar con las notas solamente si no son null.
        return notas?.let { nota ->

            // Si la nota tiene contenido, se devuelve; de lo contrario,
            // se muestra un mensaje indicando que no hay notas.
            if (nota.isNotBlank()) nota else "Sin notas"

        } ?: "Sin notas" // Valor alternativo cuando notas es null.
    }
}
