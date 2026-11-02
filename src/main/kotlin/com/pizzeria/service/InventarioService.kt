
package main.kotlin.com.pizzeria.service

import main.kotlin.com.pizzeria.model.Producto

class InventarioService(
    // Mantenemos las existencias en un mapa para consultar y modificar cada ingrediente fácilmente.
    // Las cantidades iniciales representan el stock disponible al comenzar el funcionamiento de la pizzería.
    private val inventario: MutableMap<String, Int> = mutableMapOf(
        "Harina" to 100,
        "Queso" to 50,
        "Salsa de tomate" to 40,
        "Pepperoni" to 30,
        "Champiñones" to 20
    )
) {

    fun obtenerInventario(): Map<String, Int> {

        // Devolvemos una copia del inventario para consultar las existencias sin modificar el mapa original.
        return inventario.toMap()
    }

    fun verificarIngredientes(
        ingredientesNecesarios: Map<String, Int>
    ): List<String> {

        // Aquí reunimos los ingredientes que no cumplen con las cantidades solicitadas.
        val faltantes = mutableListOf<String>()

        for ((ingrediente, cantidad) in ingredientesNecesarios) {

            // Si el ingrediente no está registrado en el inventario, lo consideramos sin existencias.
            val disponible = inventario[ingrediente] ?: 0

            // Evaluamos cada situación para identificar si la cantidad solicitada es válida y si alcanza el stock.
            when {
                cantidad <= 0 ->
                    faltantes.add("$ingrediente: cantidad requerida inválida.")

                disponible < cantidad ->
                    faltantes.add(
                        "$ingrediente: necesitas $cantidad, " +
                                "pero solo hay $disponible. Reabastece el inventario."
                    )
            }
        }

        // Entregamos la lista de problemas encontrados para decidir si se puede continuar con el pedido.
        return faltantes
    }

    fun actualizarInventario(
        ingredientesNecesarios: Map<String, Int>
    ): Boolean {

        // Antes de descontar cualquier ingrediente, revisamos que todos tengan stock suficiente.
        val faltantes = verificarIngredientes(ingredientesNecesarios)

        // Si encontramos algún problema, informamos al usuario y evitamos modificar las existencias.
        if (faltantes.isNotEmpty()) {
            println("No se puede actualizar el inventario:")
            faltantes.forEach { println("- $it") }
            return false
        }

        // Como ya comprobamos que alcanza el stock, descontamos las cantidades utilizadas en la preparación.
        for ((ingrediente, cantidad) in ingredientesNecesarios) {
            inventario[ingrediente] =
                (inventario[ingrediente] ?: 0) - cantidad
        }

        return true
    }

    fun reabastecerIngrediente(
        ingrediente: String,
        cantidad: Int
    ) {

        // No permitimos agregar ingredientes sin nombre ni cantidades que no sean positivas.
        require(ingrediente.isNotBlank()) {
            "El nombre del ingrediente es obligatorio."
        }

        require(cantidad > 0) {
            "La cantidad de reposición debe ser mayor que cero."
        }

        // Sumamos las nuevas unidades a las existencias actuales.
        // Si el ingrediente todavía no existe, comenzamos su registro desde cero.
        inventario[ingrediente] =
            (inventario[ingrediente] ?: 0) + cantidad

        println("$ingrediente reabastecido correctamente.")
    }

    fun mostrarInventario(): String {

        // Ordenamos los ingredientes alfabéticamente para que el inventario sea más fácil de consultar.
        return inventario.entries
            .sortedBy { it.key }
            .joinToString("\n") { (ingrediente, cantidad) ->
                "$ingrediente: $cantidad unidades"
            }
    }
}
