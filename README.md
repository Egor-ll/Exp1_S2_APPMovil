Sistema de Gestión de Pizzería

Asignatura: Desarrollo App Móviles (PMY2201)
Actividad: Semana 2 – Aplicando utilidades de Kotlin
Entorno de desarrollo: IntelliJ IDEA
Lenguaje: Kotlin
JDK: Java 21

# Descripción del proyecto

Este proyecto consiste en el desarrollo de un sistema de gestión para una pizzería, utilizando Kotlin y aplicando los conceptos fundamentales de programación vistos durante la segunda semana de la asignatura Desarrollo App Móviles.

La aplicación permite gestionar pedidos, registrar clientes, controlar el inventario de ingredientes y asignar repartidores, incorporando validaciones y manejo de errores para mantener un funcionamiento correcto.

El objetivo principal es aplicar estructuras de control, arreglos, funciones, colecciones, programación orientada a objetos y manejo de valores nulos mediante un caso práctico.

# Funcionalidades principales

Gestión de clientes

Registro del nombre y datos de contacto.

Validación del número telefónico.

Validación del correo electrónico.

Identificación de clientes frecuentes.

Gestión de productos y pedidos

Catálogo de pizzas disponibles.

Selección de productos y cantidades.

Registro de notas adicionales.

Cálculo de subtotal, descuentos e impuestos.

Generación de un resumen detallado del pedido.

Control de inventario

Registro de ingredientes y cantidades disponibles mediante un Map.

Verificación de disponibilidad antes de aceptar pedidos.

Descuento automático de ingredientes al preparar un pedido.

Mensajes de advertencia cuando faltan ingredientes.

Funcionalidad para reabastecer el inventario.

Gestión de repartidores

Registro de repartidores disponibles.

Búsqueda mediante el método find.

Asignación de repartidores a pedidos preparados.

Actualización de su disponibilidad.

Estados de los pedidos

La aplicación contempla los siguientes estados:

EN_PREPARACION: el pedido se encuentra en preparación.

LISTO: el pedido está preparado.

EN_CAMINO: el pedido fue asignado a un repartidor y salió a entrega.

Se incorporaron validaciones para controlar las transiciones permitidas entre estados.

# Tecnologías y conceptos utilizados

Kotlin: lenguaje principal del proyecto.

IntelliJ IDEA: entorno de desarrollo.

Java 21: entorno de ejecución.

Conceptos de programación aplicados:

Variables, tipos de datos y operadores.

Condicionales if y when.

Ciclos for y estructuras iterativas.

Arreglos para almacenar pedidos.

Manipulación y formato de strings.

Funciones reutilizables.

Colecciones: List, Map y MutableMap.

Programación orientada a objetos.

Clases, objetos, herencia y polimorfismo.

Encapsulamiento mediante modificadores de acceso.

Manejo de excepciones con try-catch.

Null safety mediante ?., ?: y let.

# Estructura del proyecto

La organización del código se distribuye en los siguientes paquetes:

Pizzeria/
│
└── src/
    └── main/
        └── kotlin/
            └── com/
                └── pizzeria/
                    │
                    ├── Main.kt
                    │
                    ├── model/
                    │   ├── Producto.kt
                    │   ├── Pedido.kt
                    │   ├── Usuario.kt
                    │   ├── Cliente.kt
                    │   └── Repartidor.kt
                    │
                    ├── service/
                    │   ├── InventarioService.kt
                    │   ├── PedidoService.kt
                    │   └── RepartidorService.kt
                    │
                    └── util/
                        ├── Validador.kt
                        └── Formato.kt

Descripción de los paquetes:

model: contiene las clases que representan los productos, pedidos, clientes, usuarios y repartidores.

service: concentra la lógica de gestión de pedidos, inventario y repartidores.

util: contiene herramientas para validar datos y dar formato a la información.

Main.kt: punto de entrada de la aplicación y menú principal.

# Requisitos para ejecutar el proyecto

Para ejecutar la aplicación se necesita:

IntelliJ IDEA.

JDK 21 instalado.

Proyecto Kotlin correctamente configurado.

# Ejecución

Abrir IntelliJ IDEA.

Seleccionar Open y buscar la carpeta del proyecto.

Esperar a que IntelliJ cargue la configuración.

Localizar el archivo Main.kt.

Ejecutar la función principal main().

Al iniciar, se mostrará un menú con las siguientes opciones:

==============================
      PIZZERIA - MENU
==============================
1. Realizar pedido
2. Ver inventario
3. Ver reporte de pedidos
4. Ver repartidores
5. Salir
==============================

# Pruebas realizadas

Durante la validación del proyecto se realizaron pruebas funcionales para comprobar:

Rechazo de teléfonos y correos electrónicos inválidos.

Validación de respuestas del cliente.

Registro y preparación de pedidos.

Cálculo de descuentos, impuestos y total.

Actualización de las cantidades del inventario.

Asignación y disponibilidad de repartidores.

Generación del resumen y reporte de pedidos.

Control de transiciones de estados no permitidas.

Manejo de errores y valores nulos.

Las pruebas realizadas permitieron comprobar el funcionamiento de los principales procesos de la aplicación.

# Conclusión

El desarrollo de este proyecto permitió aplicar los principales conceptos de Kotlin mediante la construcción de una solución práctica para la gestión de una pizzería.

Se implementaron estructuras de control, arreglos, colecciones, funciones y programación orientada a objetos, además de mecanismos de validación y manejo de errores.

La aplicación integra sus principales procesos en una estructura organizada por paquetes, facilitando la comprensión, mantenimiento y continuidad del desarrollo.

Proyecto académico – Desarrollo App Móviles (PMY2201)
Semana 2 – Aplicando utilidades de Kotlin