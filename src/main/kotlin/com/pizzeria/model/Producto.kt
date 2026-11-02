
package main.kotlin.com.pizzeria.model

// Representamos cada producto del menú con su información y los ingredientes necesarios para prepararlo.
data class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientes: Map<String, Int>
) {

    // Comprobamos los datos al crear un producto para evitar registrar información incorrecta.
    init {

        // Todo producto debe tener un nombre que permita identificarlo en el menú.
        require(nombre.isNotBlank()) {
            "El nombre del producto no puede estar vacío."
        }

        // Evitamos que una pizza tenga un precio negativo.
        require(precio >= 0) {
            "El precio no puede ser negativo."
        }

        // Cada ingrediente debe tener una cantidad positiva para poder calcular el consumo de stock.
        require(ingredientes.values.all { it > 0 }) {
            "Las cantidades de ingredientes deben ser positivas."
        }
    }

    fun detallarProducto(): String {

        // Recorremos el mapa para mostrar los ingredientes junto con las cantidades necesarias.
        val detalleIngredientes = ingredientes.entries.joinToString(", ") {
            "${it.key}: ${it.value}"
        }

        // Preparamos la descripción que se muestra cuando el cliente consulta las pizzas disponibles.
        return """
            Producto: $nombre
            Precio: $${"%.0f".format(precio)}
            Ingredientes: $detalleIngredientes
        """.trimIndent()
    }
}
