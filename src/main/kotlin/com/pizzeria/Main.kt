
package main.kotlin.com.pizzeria

import main.kotlin.com.pizzeria.model.Cliente
import main.kotlin.com.pizzeria.model.DetallePedido
import main.kotlin.com.pizzeria.model.Pedido
import main.kotlin.com.pizzeria.model.Producto
import main.kotlin.com.pizzeria.model.Repartidor
import main.kotlin.com.pizzeria.service.InventarioService
import main.kotlin.com.pizzeria.service.PedidoService
import main.kotlin.com.pizzeria.service.RepartidorService
import main.kotlin.com.pizzeria.util.Formato
import main.kotlin.com.pizzeria.util.Validador

// Lee un número entero y vuelve a solicitarlo si el usuario ingresa un dato incorrecto.
fun leerEntero(mensaje: String): Int {
    while (true) {
        print(mensaje)

        try {
            return readln().trim().toInt()
        } catch (e: NumberFormatException) {
            println("Error: ingrese un numero valido.")
        }
    }
}

fun main() {

    // Inicializamos los servicios que administran el inventario y los pedidos.
    val inventario = InventarioService()
    val pedidos = PedidoService(inventario)

    // Registramos los repartidores que podrán recibir pedidos durante la jornada.
    val repartidores = RepartidorService(
        listOf(
            Repartidor(
                "Carlos Soto",
                "912345678",
                "carlos@pizzeria.cl"
            ),
            Repartidor(
                "Maria Torres",
                "987654321",
                "maria@pizzeria.cl"
            )
        )
    )

    // Definimos las pizzas disponibles, sus precios y los ingredientes que consume cada una.
    val menu = listOf(
        Producto(
            "Margarita",
            5990.0,
            mapOf(
                "Harina" to 1,
                "Queso" to 1,
                "Salsa de tomate" to 1
            )
        ),
        Producto(
            "Pepperoni",
            7990.0,
            mapOf(
                "Harina" to 1,
                "Queso" to 1,
                "Salsa de tomate" to 1,
                "Pepperoni" to 1
            )
        ),
        Producto(
            "Vegetariana",
            7490.0,
            mapOf(
                "Harina" to 1,
                "Queso" to 1,
                "Salsa de tomate" to 1,
                "Champiñones" to 1
            )
        )
    )

    // Cada pedido recibe un identificador distinto mientras el programa está en ejecución.
    var siguienteId = 1
    var continuar = true

    // Mantenemos el menú disponible hasta que el usuario decida salir.
    while (continuar) {

        println(
            """
            
            ==============================
                  PIZZERIA - MENU
            ==============================
            1. Realizar pedido
            2. Ver inventario
            3. Ver reporte de pedidos
            4. Ver repartidores
            5. Salir
            ==============================
            """.trimIndent()
        )

        try {
            // Leemos la opción del menú y utilizamos when para dirigirnos a la operación elegida.
            val opcion = leerEntero("Selecciona una opcion: ")

            when (opcion) {

                1 -> {
                    println("\n--- REGISTRO DEL CLIENTE ---")

                    // Solicitamos el nombre y evitamos continuar si está vacío.
                    print("Nombre: ")
                    val nombre = readln().trim()

                    require(nombre.isNotBlank()) {
                        "El nombre es obligatorio."
                    }

                    // Validamos el teléfono hasta recibir un número con el formato esperado.
                    print("Telefono: ")
                    var telefono = readln().trim()

                    while (!Validador.esTelefonoValido(telefono)) {
                        println("Error: ingrese un numero de telefono valido.")
                        print("Telefono: ")
                        telefono = readln().trim()
                    }

                    // Comprobamos que el correo tenga una estructura válida antes de guardar al cliente.
                    print("Correo electronico: ")
                    var correo = readln().trim()

                    while (!Validador.esCorreoValido(correo)) {
                        println("Error: ingrese un correo electronico valido.")
                        print("Correo electronico: ")
                        correo = readln().trim()
                    }

                    println("\n--- DATOS DEL CLIENTE ---")

                    // Solo aceptamos s o n para evitar interpretar respuestas incorrectas como un no.
                    var respuestaFrecuente: String

                    do {
                        print("Cliente frecuente? (s/n): ")
                        respuestaFrecuente = readln().trim()

                        if (!respuestaFrecuente.equals("s", true) &&
                            !respuestaFrecuente.equals("n", true)
                        ) {
                            println("Respuesta no valida. Ingrese s o n.")
                        }

                    } while (
                        !respuestaFrecuente.equals("s", true) &&
                        !respuestaFrecuente.equals("n", true)
                    )

                    val frecuente = respuestaFrecuente.equals("s", ignoreCase = true)

                    // Creamos el cliente con los datos que ya pasaron las validaciones.
                    val cliente = Cliente(
                        nombre = nombre,
                        telefono = telefono,
                        correo = correo,
                        frecuente = frecuente
                    )

                    println("\n--- MENU DE PIZZAS ---")

                    // Recorremos la lista y mostramos cada pizza con su número y descripción.
                    for ((indice, producto) in menu.withIndex()) {
                        println("${indice + 1}. ${producto.detallarProducto()}")
                        println()
                    }

                    // Solicitamos la selección y comprobamos que corresponda a una opción existente.
                    val seleccion = leerEntero("Selecciona una pizza: ")

                    require(seleccion in 1..menu.size) {
                        "La opcion de pizza no es valida."
                    }

                    // No permitimos cantidades iguales o inferiores a cero.
                    val cantidad = leerEntero("Cantidad de pizzas: ")

                    require(cantidad > 0) {
                        "La cantidad debe ser mayor que cero."
                    }

                    // Las notas son opcionales; si el usuario no escribe nada, conservamos null.
                    print("Notas del pedido (opcional): ")
                    val notas = readln().trim()
                        .takeIf { it.isNotBlank() }

                    // Obtenemos la pizza seleccionada. Restamos uno porque las listas comienzan en índice cero.
                    val productoSeleccionado = menu[seleccion - 1]

                    // Relacionamos los datos del cliente con los productos que formarán el pedido.
                    val pedido = Pedido(
                        id = siguienteId,
                        cliente = cliente,
                        productos = listOf(
                            DetallePedido(
                                productoSeleccionado,
                                cantidad
                            )
                        ),
                        notas = notas
                    )

                    println("\n--- VERIFICACION DE INGREDIENTES ---")

                    // Antes de guardar el pedido, comprobamos que exista suficiente inventario.
                    if (!pedidos.registrarPedido(pedido)) {
                        println("Pedido no registrado por falta de ingredientes.")
                    } else {

                        // El identificador se reserva una vez que el pedido queda registrado.
                        siguienteId++

                        println("Pedido registrado correctamente.")

                        // Descontamos los ingredientes y cambiamos el estado cuando la preparación resulta exitosa.
                        if (pedidos.prepararPedido(pedido)) {
                            println("Pedido preparado correctamente.")

                            // Buscamos un repartidor disponible para gestionar la entrega.
                            val repartidor = repartidores.asignarRepartidor(pedido)

                            // Si encontramos uno, iniciamos el proceso de entrega.
                            if (repartidor != null) {
                                repartidor.entregarPedido(pedido)
                            }

                            // Mostramos el resumen con los valores calculados por PedidoService.
                            println("\n--- RESUMEN DEL PEDIDO ---")

                            println(
                                Formato.formatearPedido(
                                    pedido = pedido,
                                    subtotal = pedidos.calcularSubtotal(pedido),
                                    descuento = pedidos.calcularDescuento(pedido),
                                    impuestos = pedidos.calcularImpuesto(pedido),
                                    total = pedidos.calcularTotal(pedido)
                                )
                            )

                        } else {
                            println(
                                "No fue posible preparar el pedido. " +
                                        "Revisa el inventario."
                            )
                        }
                    }
                }

                2 -> {
                    // Consultamos las cantidades disponibles de cada ingrediente.
                    println("\n--- INVENTARIO ACTUAL ---")
                    println(inventario.mostrarInventario())
                }

                3 -> {
                    // Generamos el reporte con los pedidos registrados durante la ejecución.
                    println("\n--- REPORTE DIARIO ---")

                    val reporte = pedidos.generarReporte()

                    // Evitamos mostrar un reporte vacío cuando todavía no hay pedidos.
                    if (reporte.isBlank()) {
                        println("Todavia no hay pedidos registrados.")
                    } else {
                        println(reporte)
                    }

                    // Mostramos cuántos pedidos siguen pendientes y cuántos llegaron a EN_CAMINO.
                    println("Pedidos pendientes: ${pedidos.obtenerPedidosPendientes().size}")
                    println("Pedidos en camino: ${pedidos.obtenerPedidosEnCamino().size}")
                }

                4 -> {
                    // Consultamos la disponibilidad actual de los repartidores.
                    println("\n--- REPARTIDORES ---")
                    println(repartidores.mostrarDisponibilidad())
                }

                5 -> {
                    // Cambiamos la condición del ciclo para finalizar el programa.
                    continuar = false
                    println("Gracias por utilizar Pizzeria.")
                }

                // Informamos cuando se selecciona una opción que no existe en el menú.
                else -> println("Selecciona una opcion entre 1 y 5.")
            }

        } catch (e: NumberFormatException) {
            // Capturamos entradas que no se puedan convertir a números.
            println("Error: debes ingresar un numero valido.")

        } catch (e: IllegalArgumentException) {
            // Mostramos el motivo cuando alguna validación de los datos no se cumple.
            println("Error: ${e.message ?: "Datos invalidos."}")

        } catch (e: IllegalStateException) {1
            // Controlamos errores que puedan aparecer durante el procesamiento de una operación.
            println("Error al procesar la operacion: ${e.message}")
        }
    }
}
