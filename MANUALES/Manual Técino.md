# PROYECTO 2
**Curso:** Laboratorio de IPC1  
**Estudiante:** ALVARO MOISÉS GIRÓN MORALES  
**Carné:** 20250195

# Manual Técnico - GameZone Pro

## 1. Introducción
GameZone Pro es una aplicación de escritorio desarrollada en **Java** con interfaz gráfica en **Swing**. El sistema integra cuatro áreas principales: tienda de videojuegos, álbum de cartas coleccionables, eventos especiales con venta de tickets concurrente y un módulo de recompensas con leaderboard. Además, incorpora persistencia en archivos de texto y generación de reportes HTML.

Este manual técnico describe la arquitectura general del proyecto, la organización por paquetes, las estructuras de datos implementadas desde cero, la lógica de cada módulo, el manejo de persistencia y las principales clases y métodos del sistema.

---

## 2. Objetivo técnico del sistema
El objetivo del proyecto es demostrar el uso de:

- Programación orientada a objetos.
- Interfaces gráficas con Swing.
- Estructuras de datos implementadas manualmente.
- Persistencia de información en archivos `.txt`.
- Concurrencia con `Thread` y sincronización con `synchronized`.
- Generación de reportes HTML.

---

## 3. Arquitectura general
La aplicación sigue una arquitectura por capas sencilla, separando:

### 3.1 Capa de presentación
Contiene las pantallas y paneles Swing que interactúan con el usuario.

**Paquete:** `ipc1.gamezonepro.ui`

Clases principales:
- `MainFrame`
- `TiendaPanel`
- `AlbumPanel`
- `TorneosPanel`
- `RecompensasPanel`
- `ReportesPanel`
- `EstudiantePanel`
- `RefreshablePanel`

### 3.2 Capa de servicios
Contiene la lógica de negocio de cada módulo.

**Paquete:** `ipc1.gamezonepro.service`

Clases principales:
- `AppContext`
- `TiendaService`
- `AlbumService`
- `TorneoService`
- `RecompensasService`
- `ReporteService`

### 3.3 Capa de persistencia
Se encarga de leer y escribir archivos de datos.

**Paquete:** `ipc1.gamezonepro.persistence`

Clases principales:
- `DataStore`
- `DataPaths`

### 3.4 Capa de modelo
Representa las entidades del dominio del sistema.

**Paquete:** `ipc1.gamezonepro.model`

Clases principales:
- `Videojuego`
- `CarritoItem`
- `Compra`
- `CompraDetalle`
- `Carta`
- `Torneo`
- `TicketVenta`
- `UsuarioProgreso`
- `Logro`
- `LeaderboardEntry`
- `EstudianteInfo`

### 3.5 Capa de estructuras de datos
Implementa las estructuras pedidas en el proyecto sin usar colecciones de Java.

**Paquete:** `ipc1.gamezonepro.datastructures`

Clases principales:
- `NodoSimple`
- `ListaEnlazadaSimple`
- `NodoCola`
- `Cola`
- `NodoMatriz`
- `MatrizOrtogonal`

### 3.6 Capa utilitaria
Contiene estilos visuales y funciones auxiliares.

**Paquete:** `ipc1.gamezonepro.util`

Clases:
- `AppTheme`
- `FormatoUtil`

---

## 4. Flujo de ejecución del programa
1. La clase `GameZoneProApp` inicia la aplicación.
2. Se configura el `LookAndFeel` del sistema.
3. Se crea una instancia de `AppContext`.
4. `AppContext` carga datos desde los archivos de texto mediante `DataStore`.
5. Se inicializan los servicios del sistema.
6. Se crea `MainFrame`, que contiene el menú lateral y las pantallas del sistema.
7. Cada módulo interactúa con su servicio correspondiente.
8. Al cerrar la aplicación, `MainFrame` llama a `context.guardarTodo()` para persistir la información actual.

---

## 5. Estructuras de datos implementadas

## 5.1 Lista enlazada simple
Se utiliza para manejar datos secuenciales sin depender de `ArrayList` o `LinkedList`.

### Clases relacionadas
- `NodoSimple<T>`
- `ListaEnlazadaSimple<T>`

### Uso dentro del sistema
- Carrito de compras.
- Historial de compras.
- Catálogo de videojuegos.
- Catálogo de cartas.
- Lista de torneos.
- Tickets vendidos.
- Logros.
- Leaderboard.
- Notificaciones internas.

### Métodos principales
- `agregar(T dato)`
- `agregarAlInicio(T dato)`
- `obtener(int indice)`
- `eliminarEn(int indice)`
- `limpiar()`
- `tamanio()`
- `estaVacia()`
- `getCabeza()`

### Ventajas en este proyecto
- Permite inserción directa al inicio para el historial.
- Facilita recorridos manuales con nodos.
- Cumple con la restricción de no usar Java Collections Framework.

---

## 5.2 Cola implementada desde cero
Se utiliza para modelar la fila de usuarios en la compra de tickets de torneos.

### Clases relacionadas
- `NodoCola<T>`
- `Cola<T>`

### Métodos principales
- `encolar(T dato)`
- `desencolar()`
- `peek()`
- `estaVacia()`
- `tamanio()`

### Característica importante
El método `desencolar()` está sincronizado con `synchronized`, evitando condiciones de carrera cuando dos taquillas procesan la misma cola al mismo tiempo.

---

## 5.3 Matriz ortogonal
Se utiliza para representar el álbum de cartas como una cuadrícula enlazada por referencias.

### Clases relacionadas
- `NodoMatriz`
- `MatrizOrtogonal`

### Referencias de cada nodo
- `arriba`
- `abajo`
- `izquierda`
- `derecha`
- `carta`

### Métodos principales
- `obtenerNodo(int fila, int columna)`
- `obtenerCarta(int fila, int columna)`
- `asignarCarta(int fila, int columna, Carta carta)`
- `colocarEnPrimeraVacia(Carta carta)`
- `intercambiar(int filaUno, int columnaUno, int filaDos, int columnaDos)`
- `contarCartas()`
- `filaCompleta(int fila)`
- `contieneCodigo(String codigoCarta)`

### Aplicación en el proyecto
- Inserción de cartas en la primera posición libre.
- Intercambio de cartas entre celdas.
- Conteo de cartas y validación de filas completas.
- Representación visual del álbum en Swing.

---

## 6. Descripción de clases principales

## 6.1 Clase `GameZoneProApp`
Es la clase principal del sistema.

### Responsabilidades
- Iniciar la aplicación.
- Configurar el estilo visual del sistema.
- Ejecutar la interfaz sobre el hilo de eventos de Swing con `SwingUtilities.invokeLater()`.

### Método principal
- `main(String[] args)`

---

## 6.2 Clase `AppContext`
Centraliza los datos cargados y las instancias de servicios del sistema.

### Responsabilidades
- Cargar archivos base.
- Mantener el estado global del sistema.
- Compartir servicios entre paneles.
- Guardar toda la información antes de cerrar.

### Atributos importantes
- Información del estudiante.
- Catálogo de videojuegos.
- Catálogo de cartas.
- Historial de compras.
- Álbum.
- Torneos.
- Tickets vendidos.
- Usuario actual.
- Logros.
- Leaderboard.

### Métodos principales
- `getTiendaService()`
- `getAlbumService()`
- `getTorneoService()`
- `getRecompensasService()`
- `getReporteService()`
- `getEstudianteInfo()`
- `guardarTodo()`

---

## 6.3 Clase `TiendaService`
Contiene la lógica de negocio del módulo de tienda.

### Responsabilidades
- Filtrar videojuegos.
- Administrar el carrito.
- Confirmar compras.
- Validar stock.
- Generar historial de compras.
- Otorgar experiencia al usuario por compras.

### Métodos principales
- `getCatalogo()`
- `getCarrito()`
- `getHistorial()`
- `filtrarCatalogo(String genero, String plataforma, String busqueda)`
- `agregarAlCarrito(Videojuego videojuego)`
- `actualizarCantidad(int indice, int cantidad)`
- `eliminarItemCarrito(int indice)`
- `getTotalCarrito()`
- `confirmarCompra()`

### Clase interna
`ResultadoCompra` encapsula el resultado de una compra:
- éxito de la operación
- mensaje principal
- advertencias de stock

---

## 6.4 Clase `AlbumService`
Administra la lógica del álbum de cartas.

### Responsabilidades
- Gestionar inserción de cartas.
- Buscar cartas del catálogo.
- Colocar cartas en la primera celda vacía.
- Intercambiar posiciones.
- Verificar logros relacionados con el álbum.
- Mantener la persistencia del estado del álbum.

### Operaciones esperadas
- Agregar carta.
- Buscar carta.
- Intercambiar cartas.
- Obtener detalles de una celda.
- Notificar progreso a recompensas.

---

## 6.5 Clase `TorneoService`
Administra el módulo de eventos especiales y venta de tickets.

### Responsabilidades
- Administrar la lista de torneos.
- Registrar usuarios en la cola de espera.
- Procesar tickets mediante múltiples hilos.
- Guardar historial de tickets vendidos.
- Actualizar la interfaz en tiempo real.

### Aspectos técnicos clave
- Uso de una cola propia.
- Uso de al menos dos hilos de taquilla.
- Protección de acceso concurrente.
- Comunicación con Swing usando actualizaciones seguras.

---

## 6.6 Clase `RecompensasService`
Administra experiencia, niveles, rango, logros y leaderboard.

### Responsabilidades
- Registrar XP por acciones del usuario.
- Calcular nivel y rango.
- Desbloquear logros.
- Generar notificaciones pendientes.
- Actualizar el leaderboard.

### Funciones típicas
- Registrar inicio de sesión.
- Otorgar puntos por compra.
- Otorgar puntos por carta legendaria.
- Detectar filas completas del álbum.
- Consumir notificaciones.

---

## 6.7 Clase `ReporteService`
Genera los reportes HTML del sistema.

### Responsabilidades
- Crear archivos HTML con estilos incrustados.
- Escribir el contenido según el tipo de reporte.
- Guardar los reportes en la carpeta correspondiente.
- Abrir automáticamente los reportes en el navegador del sistema.

### Reportes que genera
- Inventario de tienda.
- Ventas.
- Álbum.
- Torneos.

---

## 6.8 Clase `DataStore`
Administra la persistencia completa del sistema.

### Responsabilidades
- Crear archivos base si no existen.
- Cargar información inicial desde archivos de texto.
- Guardar información modificada al cierre del programa.

### Archivos manejados
- `catalogo.txt`
- `cartas_catalogo.txt`
- `historial.txt`
- `album.txt`
- `torneos.txt`
- `tickets_vendidos.txt`
- `leaderboard.txt`
- `perfil_usuario.txt`
- `logros.txt`
- `estudiante.txt`

---

## 6.9 Clase `MainFrame`
Es la ventana principal de la aplicación.

### Responsabilidades
- Construir el menú lateral.
- Mostrar el panel activo mediante `CardLayout`.
- Refrescar información global del usuario.
- Mostrar notificaciones de logros.
- Guardar el estado al cerrar la aplicación.

### Características destacadas
- Usa `CardLayout` para cambiar entre módulos.
- Centraliza el refresco de paneles.
- Muestra nivel y XP en la parte superior.
- Incluye evento de cierre controlado.

---

## 7. Descripción de módulos del sistema

## 7.1 Módulo de Tienda
Permite explorar videojuegos, agregarlos al carrito y confirmar compras.

### Componentes involucrados
- `TiendaPanel`
- `TiendaService`
- `Videojuego`
- `CarritoItem`
- `Compra`
- `CompraDetalle`

### Flujo técnico
1. Se carga el catálogo desde `catalogo.txt`.
2. El usuario filtra o busca videojuegos.
3. El sistema recorre la lista enlazada y arma el catálogo filtrado.
4. El usuario agrega productos al carrito.
5. Al confirmar, se valida stock.
6. Se actualizan existencias.
7. Se genera un objeto `Compra` con detalles.
8. La compra se agrega al historial y se limpia el carrito.

---

## 7.2 Módulo de Álbum
Administra el álbum de cartas coleccionables mediante una matriz ortogonal.

### Componentes involucrados
- `AlbumPanel`
- `AlbumService`
- `Carta`
- `MatrizOrtogonal`
- `NodoMatriz`

### Flujo técnico
1. Se carga la estructura del álbum.
2. Se renderiza visualmente en una cuadrícula Swing.
3. El usuario agrega una carta.
4. El sistema busca la primera posición libre.
5. Se actualiza la interfaz y el progreso del usuario.
6. Se permite buscar o intercambiar cartas.

---

## 7.3 Módulo de Torneos
Gestiona torneos y venta de tickets con procesamiento concurrente.

### Componentes involucrados
- `TorneosPanel`
- `TorneoService`
- `Torneo`
- `TicketVenta`
- `Cola`
- `NodoCola`

### Flujo técnico
1. Se cargan los torneos desde `torneos.txt`.
2. El usuario se inscribe a un torneo.
3. Su nombre se agrega a la cola.
4. Dos hilos procesan la venta de tickets.
5. Cada hilo intenta desencolar al siguiente usuario.
6. Se descuenta un ticket disponible.
7. Se registra la venta.
8. La interfaz muestra estados y resultados en tiempo real.

---

## 7.4 Módulo de Recompensas
Administra la progresión del usuario.

### Componentes involucrados
- `RecompensasPanel`
- `RecompensasService`
- `UsuarioProgreso`
- `Logro`
- `LeaderboardEntry`

### Funciones técnicas
- Acumular XP.
- Calcular nivel actual.
- Determinar rango.
- Desbloquear logros.
- Ordenar leaderboard manualmente.
- Preparar mensajes de notificación.

---

## 7.5 Módulo de Reportes
Permite generar reportes HTML con datos actualizados del sistema.

### Componentes involucrados
- `ReportesPanel`
- `ReporteService`

### Salidas generadas
- Archivo HTML con CSS embebido.
- Apertura automática en navegador.
- Reportes guardados para consulta posterior.

---

## 8. Persistencia de datos
El sistema utiliza archivos de texto para conservar la información entre sesiones.

### Ventajas de este enfoque
- Simplicidad de implementación.
- Facilidad de inspección manual.
- Cumplimiento con lo solicitado en el proyecto.

### Momento de carga
Los datos se cargan al iniciar la aplicación desde `AppContext`.

### Momento de guardado
Los datos se guardan:
- cuando el usuario realiza cambios relevantes mediante el callback de mutación, y
- al cerrar la aplicación.

---

## 9. Concurrencia y sincronización
El módulo de torneos implementa concurrencia real mediante hilos.

### Elementos usados
- `Thread`
- `Runnable`
- `Thread.sleep()`
- `synchronized`
- actualización segura para interfaz Swing

### Problema resuelto
Sin sincronización, dos taquillas podrían intentar vender el mismo ticket o atender al mismo usuario. Por eso, la cola protege la operación de extracción y el sistema coordina el acceso a recursos compartidos.

---

## 10. Interfaz gráfica
La GUI fue desarrollada completamente en Swing.

### Componentes usados
- `JFrame`
- `JPanel`
- `JButton`
- `JLabel`
- `JTable`
- `JScrollPane`
- `JOptionPane`
- `JProgressBar`
- `CardLayout`

### Características
- Menú lateral para navegar entre módulos.
- Cabecera con información de nivel y XP.
- Paneles independientes por módulo.
- Actualización dinámica de datos después de operaciones.

---

## 11. Restricciones técnicas cumplidas
En este proyecto se respetan las restricciones principales del enunciado:

- No se utilizan `ArrayList`, `LinkedList`, `Queue`, `Stack` ni estructuras equivalentes del framework de colecciones.
- Las estructuras de datos principales fueron implementadas desde cero.
- El módulo de torneos utiliza concurrencia y sincronización.
- La aplicación funciona sobre interfaz gráfica Swing.
- La persistencia se maneja con archivos de texto.
- El leaderboard se ordena con lógica manual.

---

## 12. Posibles mejoras futuras
- Agregar validaciones más detalladas en entradas de usuario.
- Incorporar más estadísticas en los reportes.
- Mejorar la personalización visual de cartas y torneos.
- Agregar más animaciones o retroalimentación visual en Swing.
- Separar aún más la capa de presentación de la lógica de negocio.

---

## 13. Conclusión
GameZone Pro es un proyecto que integra varios temas fundamentales de IPC1 en una sola aplicación: estructuras dinámicas, interfaces gráficas, persistencia, concurrencia y programación orientada a objetos. La organización por paquetes y servicios permite mantener el código más claro, reutilizable y fácil de documentar.

Desde el punto de vista técnico, los componentes más importantes del sistema son la lista enlazada simple, la cola propia sincronizada y la matriz ortogonal, ya que sostienen la lógica principal de los módulos del proyecto.
