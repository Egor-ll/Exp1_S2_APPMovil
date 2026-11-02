
package main.kotlin.com.pizzeria.model

// Definimos una clase base para reunir los datos que comparten clientes y repartidores.
// Al utilizar open, permitimos que otras clases hereden sus propiedades y comportamientos.
open class Usuario(
    val nombre: String,
    val telefono: String,
    val correo: String?
) {

    // Este método entrega la información general de un usuario.
    // Lo dejamos como open para que las clases derivadas puedan personalizar su presentación.
    open fun mostrarDatos(): String {

        return """
            Nombre: $nombre
            Teléfono: $telefono
            Correo: ${correo ?: "Sin correo registrado"}
        """.trimIndent()
    }
}
