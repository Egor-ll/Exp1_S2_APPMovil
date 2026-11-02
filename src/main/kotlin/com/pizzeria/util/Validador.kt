
package main.kotlin.com.pizzeria.util

// Centralizamos aquí las validaciones para reutilizarlas cuando se registran los datos de un cliente.
object Validador {

    fun esTelefonoValido(telefono: String): Boolean {
        // Quitamos espacios al inicio y al final antes de revisar el número.
        val limpio = telefono.trim()

        // Comprobamos que solo contenga caracteres permitidos y que tenga entre 8 y 15 dígitos.
        return limpio.matches(
            Regex("""^\+?[0-9 ()-]{8,20}$""")
        ) && limpio.count { it.isDigit() } in 8..15
    }

    fun esCorreoValido(correo: String): Boolean {
        // Normalizamos el texto para evitar que los espacios externos afecten la validación.
        val limpio = correo.trim()

        // Utilizamos una expresión regular para comprobar la estructura básica de un correo electrónico.
        val patron = Regex(
            """^[A-Za-z0-9_%+-]+(?:\.[A-Za-z0-9_%+-]+)*@[A-Za-z0-9-]+(?:\.[A-Za-z0-9-]+)*\.[A-Za-z]{2,}$"""
        )

        return patron.matches(limpio)
    }

    fun validarTelefono(telefono: String?): String {
        // El teléfono puede venir como null; usamos ?. y orEmpty() para trabajar sin provocar una excepción.
        val limpio = telefono?.trim().orEmpty()

        // Si el número no cumple el formato, entregamos un valor predeterminado.
        return if (esTelefonoValido(limpio)) {
            limpio
        } else {
            "(000) 000-0000"
        }
    }

    fun validarCorreo(correo: String?): String {
        // Aplicamos el mismo tratamiento al correo, ya que también puede recibirse como null.
        val limpio = correo?.trim().orEmpty()

        // Conservamos el correo si es válido; de lo contrario, utilizamos un valor alternativo.
        return if (esCorreoValido(limpio)) {
            limpio
        } else {
            "correo@invalido.com"
        }
    }
}