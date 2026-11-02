
package main.kotlin.com.pizzeria.model

// Extendemos Usuario para representar a los clientes de la pizzería.
// Reutilizamos sus datos básicos y agregamos la condición de cliente frecuente.
class Cliente(
    nombre: String,
    telefono: String,
    correo: String?,
    val frecuente: Boolean = false
) : Usuario(nombre, telefono, correo) {

    // Personalizamos la presentación de los datos para incluir la información de fidelización.
    // Utilizamos override porque estamos reemplazando el método heredado de Usuario.
    override fun mostrarDatos(): String {

        return """
            ${super.mostrarDatos()}
            Cliente frecuente: ${if (frecuente) "Sí" else "No"}
        """.trimIndent()
    }
}
