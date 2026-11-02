
package main.kotlin.com.pizzeria.service

import main.kotlin.com.pizzeria.model.EstadoPedido
import main.kotlin.com.pizzeria.model.Pedido

class PedidoService(
    private val inventarioService: InventarioService,
    private val tasaImpuesto: Double = 0.19
) {

    // Guardamos los pedidos del día en un array con capacidad para 100 registros.
    // La variable cantidadPedidos nos permite saber cuántas posiciones están realmente ocupadas.
    private val pedidosDelDia = arrayOfNulls<Pedido>(100)
    private var cantidadPedidos = 0

    init {
        // Evitamos configurar una tasa de impuesto negativa, ya que no tendría sentido en los cálculos.
        require(tasaImpuesto >= 0.0) {
            "La tasa de impuesto no puede ser negativa."
        }
    }

    // Recuperamos solamente los pedidos que se han registrado, descartando las posiciones vacías del array.
    // Así podemos trabajar con una lista normal sin tener que revisar valores nulos en cada operación.
    private fun pedidosRegistrados(): List<Pedido> {
        return pedidosDelDia
            .take(cantidadPedidos)
            .filterNotNull()
    }

    // Sumamos los ingredientes de todos los productos del pedido.
    // Esto es necesario porque un cliente puede solicitar varias unidades de una misma pizza.
    private fun obtenerIngredientesNecesarios(
        pedido: Pedido
    ): Map<String, Int> {

        val necesarios = mutableMapOf<String, Int>()

        for (detalle in pedido.productos) {
            for ((ingrediente, cantidad) in detalle.producto.ingredientes) {

                // Si un ingrediente ya fue agregado, acumulamos su cantidad.
                // Si todavía no existe en el mapa, comenzamos desde cero.
                necesarios[ingrediente] =
                    (necesarios[ingrediente] ?: 0) +
                            cantidad * detalle.cantidad
            }
        }

        return necesarios
    }

    fun registrarPedido(pedido: Pedido): Boolean {

        // Antes de guardar el pedido, comprobamos que no exista otro con el mismo identificador.
        if (pedidosRegistrados().any { it.id == pedido.id }) {
            println("Ya existe un pedido con el ID ${pedido.id}.")
            return false
        }

        // El array tiene una capacidad definida, por lo que evitamos agregar pedidos cuando está lleno.
        if (cantidadPedidos >= pedidosDelDia.size) {
            println("Se alcanzo el limite diario de pedidos.")
            return false
        }

        // Revisamos si el inventario cuenta con todos los ingredientes que necesita este pedido.
        val faltantes = inventarioService.verificarIngredientes(
            obtenerIngredientesNecesarios(pedido)
        )

        // Si falta algún ingrediente, informamos cuál es y no registramos el pedido.
        if (faltantes.isNotEmpty()) {
            println("No se puede aceptar el pedido:")
            faltantes.forEach { println("- $it") }
            return false
        }

        // Si las comprobaciones fueron exitosas, almacenamos el pedido en la siguiente posición disponible.
        pedidosDelDia[cantidadPedidos] = pedido
        cantidadPedidos++

        return true
    }

    fun prepararPedido(pedido: Pedido): Boolean {

        // Comprobamos que el pedido exista antes de intentar prepararlo.
        if (pedidosRegistrados().none { it.id == pedido.id }) {
            println("El pedido no esta registrado.")
            return false
        }

        // Solo permitimos preparar pedidos que todavía se encuentren en preparación.
        if (pedido.estado != EstadoPedido.EN_PREPARACION) {
            println("El pedido no esta en preparacion.")
            return false
        }

        // Obtenemos nuevamente las cantidades necesarias para descontarlas del inventario.
        val ingredientes = obtenerIngredientesNecesarios(pedido)

        // Si no se pueden descontar los ingredientes, detenemos la preparación.
        if (!inventarioService.actualizarInventario(ingredientes)) {
            return false
        }

        // Cuando todo está correcto, cambiamos el estado del pedido a listo.
        pedido.cambiarEstado(EstadoPedido.LISTO)
        return true
    }

    // Utilizamos el cálculo definido en la clase Pedido para obtener el subtotal de los productos.
    fun calcularSubtotal(pedido: Pedido): Double =
        pedido.calcularSubtotal()

    fun calcularDescuento(pedido: Pedido): Double {
        val subtotal = calcularSubtotal(pedido)
        var descuento = 0.0

        // Aplicamos un 10 % de descuento cuando el cliente solicita más de tres pizzas.
        val cantidadPizzas = pedido.productos.sumOf { it.cantidad }

        if (cantidadPizzas > 3) {
            descuento += subtotal * 0.10
        }

        // Los clientes frecuentes reciben un 5 % adicional de descuento.
        if (pedido.cliente.frecuente) {
            descuento += subtotal * 0.05
        }

        // Evitamos que el descuento acumulado supere el valor total de los productos.
        return descuento.coerceAtMost(subtotal)
    }

    fun calcularImpuesto(pedido: Pedido): Double {

        // Primero descontamos los beneficios del cliente y luego calculamos el impuesto sobre ese monto.
        val baseImponible =
            calcularSubtotal(pedido) - calcularDescuento(pedido)

        return baseImponible * tasaImpuesto
    }

    fun calcularTotal(pedido: Pedido): Double {

        // Obtenemos el monto final sumando los impuestos y restando los descuentos al subtotal.
        return calcularSubtotal(pedido) -
                calcularDescuento(pedido) +
                calcularImpuesto(pedido)
    }

    // Entregamos una lista con los pedidos registrados, sin exponer directamente el array interno.
    fun obtenerPedidos(): List<Pedido> =
        pedidosRegistrados()

    // Obtenemos los pedidos que todavía no han salido a reparto.
    fun obtenerPedidosPendientes(): List<Pedido> =
        pedidosRegistrados().filter {
            it.estado != EstadoPedido.EN_CAMINO
        }

    // Obtenemos los pedidos que ya fueron asignados a reparto y están en camino.
    fun obtenerPedidosEnCamino(): List<Pedido> =
        pedidosRegistrados().filter {
            it.estado == EstadoPedido.EN_CAMINO
        }

    fun generarReporte(): String {
        val reporte = StringBuilder()

        // Recorremos únicamente las posiciones ocupadas del array para generar el reporte diario.
        for (pedido in pedidosDelDia.take(cantidadPedidos)) {

            // Esta comprobación evita procesar una posición vacía del array.
            if (pedido == null) continue

            // Separamos los pedidos para que cada uno tenga su propio bloque de información.
            if (reporte.isNotEmpty()) {
                reporte.append("\n\n")
            }

            // Reunimos los datos del cliente, productos, estado y valores calculados en un solo reporte.
            reporte.append(
                """
                Pedido #${pedido.id}
                Cliente: ${pedido.cliente.nombre}
                Teléfono: ${pedido.cliente.telefono}
                Correo: ${pedido.cliente.correo ?: "Sin correo"}
                Productos: ${pedido.productos.joinToString {
                    "${it.producto.nombre} x${it.cantidad}"
                }}
                Estado: ${pedido.estado}
                Subtotal: $${"%.0f".format(calcularSubtotal(pedido))}
                Descuento: $${"%.0f".format(calcularDescuento(pedido))}
                Impuestos: $${"%.0f".format(calcularImpuesto(pedido))}
                Total: $${"%.0f".format(calcularTotal(pedido))}
                Notas: ${pedido.notas ?: "Sin notas"}
                """.trimIndent()
            )
        }

        return reporte.toString()
    }
}
