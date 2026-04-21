# PROYECTO 2
Curso: Laboratorio de IPC1  
Estudiante: ALVARO MOISÉS GIRÓN MORALES  
Carné: 20250195

# Diagramas de Clases - GameZone Pro

## Descripción general
Este documento contiene los diagramas de clases principales del proyecto **GameZone Pro**.  
Los diagramas están organizados por bloques para que sea más fácil comprender la arquitectura del sistema, las relaciones entre clases, la separación por capas y las estructuras de datos implementadas desde cero.

---

## 1. Diagrama general de arquitectura

```mermaid
classDiagram
    class GameZoneProApp
    class AppContext
    class MainFrame
    class RefreshablePanel

    class TiendaPanel
    class AlbumPanel
    class TorneosPanel
    class RecompensasPanel
    class ReportesPanel
    class EstudiantePanel

    class TiendaService
    class AlbumService
    class TorneoService
    class RecompensasService
    class ReporteService

    class DataStore
    class DataPaths

    GameZoneProApp --> AppContext : inicializa
    GameZoneProApp --> MainFrame : lanza GUI
    MainFrame --> AppContext : usa

    MainFrame --> TiendaPanel
    MainFrame --> AlbumPanel
    MainFrame --> TorneosPanel
    MainFrame --> RecompensasPanel
    MainFrame --> ReportesPanel
    MainFrame --> EstudiantePanel

    TiendaPanel ..|> RefreshablePanel
    AlbumPanel ..|> RefreshablePanel
    TorneosPanel ..|> RefreshablePanel
    RecompensasPanel ..|> RefreshablePanel
    ReportesPanel ..|> RefreshablePanel
    EstudiantePanel ..|> RefreshablePanel

    AppContext --> TiendaService
    AppContext --> AlbumService
    AppContext --> TorneoService
    AppContext --> RecompensasService
    AppContext --> ReporteService

    AppContext --> DataStore : carga/guarda
    DataStore --> DataPaths : rutas
```

### Explicación
- `GameZoneProApp` es el punto de entrada del programa.
- `AppContext` centraliza los datos cargados desde archivos y crea los servicios principales.
- `MainFrame` administra la navegación entre módulos usando la interfaz gráfica Swing.
- Los paneles representan la capa visual.
- Los servicios contienen la lógica del negocio.
- `DataStore` y `DataPaths` manejan la persistencia de datos en archivos de texto.

---

## 2. Diagrama de estructuras de datos propias

```mermaid
classDiagram
    class ListaEnlazadaSimple~T~ {
        -NodoSimple~T~ cabeza
        -NodoSimple~T~ cola
        -int tamanio
        +agregar(T dato)
        +agregarAlInicio(T dato)
        +obtener(int indice)
        +establecer(int indice, T dato)
        +contiene(T dato)
        +eliminarEn(int indice)
        +eliminar(T dato)
    }

    class NodoSimple~T~ {
        -T dato
        -NodoSimple~T~ siguiente
    }

    class Cola~T~ {
        -NodoCola~T~ frente
        -NodoCola~T~ fin
        -int tamanio
        +encolar(T dato)
        +desencolar()
        +peek()
        +estaVacia()
        +tamanio()
        +representarComoTexto()
    }

    class NodoCola~T~ {
        -T dato
        -NodoCola~T~ siguiente
    }

    class MatrizOrtogonal {
        -int filas
        -int columnas
        -NodoMatriz inicio
        +obtenerNodo(int fila, int columna)
        +obtenerCarta(int fila, int columna)
        +colocarCarta(int fila, int columna, Carta carta)
        +colocarEnPrimeraVacia(Carta carta)
        +intercambiar(int filaUno, int columnaUno, int filaDos, int columnaDos)
        +contarCartas()
    }

    class NodoMatriz {
        -int fila
        -int columna
        -Carta carta
        -NodoMatriz arriba
        -NodoMatriz abajo
        -NodoMatriz izquierda
        -NodoMatriz derecha
    }

    ListaEnlazadaSimple *-- NodoSimple : compuesta por
    Cola *-- NodoCola : compuesta por
    MatrizOrtogonal *-- NodoMatriz : compuesta por
    NodoMatriz --> Carta : contiene
```

### Explicación
- `ListaEnlazadaSimple<T>` se usa en carrito, historial, logros, leaderboard, torneos y tickets vendidos.
- `Cola<T>` se utiliza para la fila de espera de compradores en torneos.
- `MatrizOrtogonal` modela el álbum de cartas mediante nodos enlazados en cuatro direcciones.
- Estas estructuras cumplen con la restricción del proyecto de no usar `ArrayList`, `LinkedList`, `Queue` ni otras colecciones del framework.

---

## 3. Diagrama del módulo Tienda

```mermaid
classDiagram
    class TiendaPanel
    class TiendaService {
        -ListaEnlazadaSimple~Videojuego~ catalogo
        -ListaEnlazadaSimple~CarritoItem~ carrito
        -ListaEnlazadaSimple~Compra~ historial
        -RecompensasService recompensasService
        +filtrarCatalogo(String genero, String plataforma, String busqueda)
        +agregarAlCarrito(Videojuego videojuego)
        +actualizarCantidad(int indice, int cantidad)
        +eliminarItemCarrito(int indice)
        +getTotalCarrito()
        +confirmarCompra()
    }

    class Videojuego {
        -String codigo
        -String nombre
        -String genero
        -double precio
        -String plataforma
        -int stock
        -String descripcion
    }

    class CarritoItem {
        -Videojuego videojuego
        -int cantidad
        +getSubtotal()
    }

    class Compra {
        -LocalDateTime fechaHora
        -ListaEnlazadaSimple~CompraDetalle~ detalles
        -double total
        +getCantidadItems()
    }

    class CompraDetalle {
        -String codigoJuego
        -String nombreJuego
        -int cantidad
        -double precioUnitario
        +getSubtotal()
    }

    TiendaPanel --> TiendaService : usa
    TiendaService --> Videojuego : administra
    TiendaService --> CarritoItem : crea y actualiza
    TiendaService --> Compra : registra
    Compra *-- CompraDetalle : contiene
    CarritoItem --> Videojuego : referencia
    TiendaService --> RecompensasService : notifica XP
```

### Explicación
- `TiendaPanel` muestra el catálogo, carrito e historial.
- `TiendaService` concentra la lógica de búsqueda, filtrado, carrito y confirmación de compra.
- `CarritoItem` encapsula un videojuego y la cantidad solicitada.
- `Compra` representa una venta confirmada y agrupa varios `CompraDetalle`.

---

## 4. Diagrama del módulo Álbum de cartas

```mermaid
classDiagram
    class AlbumPanel
    class AlbumService {
        -MatrizOrtogonal album
        -ListaEnlazadaSimple~Carta~ catalogoCartas
        -RecompensasService recompensasService
        +agregarCartaDesdeCatalogo(String codigo)
        +agregarCartaManual(Carta carta)
        +intercambiar(int filaUno, int columnaUno, int filaDos, int columnaDos)
        +contarCartas()
        +buscarCartaCatalogo(String codigo)
        +coincideBusqueda(Carta carta, String criterio)
    }

    class Carta {
        -String codigo
        -String nombre
        -String tipo
        -String rareza
        -int ataque
        -int defensa
        -int salud
        -String rutaImagen
        +copiar()
    }

    class MatrizOrtogonal
    class NodoMatriz

    AlbumPanel --> AlbumService : usa
    AlbumService --> MatrizOrtogonal : administra
    AlbumService --> Carta : agrega/busca
    MatrizOrtogonal *-- NodoMatriz : contiene
    NodoMatriz --> Carta : almacena
    AlbumService --> RecompensasService : actualiza logros/XP
```

### Explicación
- `AlbumPanel` renderiza visualmente el álbum en Swing.
- `AlbumService` encapsula la lógica de agregar cartas, intercambiar posiciones y filtrar contenido.
- `MatrizOrtogonal` y `NodoMatriz` forman la base del álbum matricial.
- `Carta` representa la información de cada carta coleccionable.

---

## 5. Diagrama del módulo Torneos y tickets

```mermaid
classDiagram
    class TorneosPanel
    class TorneoService {
        -ListaEnlazadaSimple~Torneo~ torneos
        -ListaEnlazadaSimple~TicketVenta~ ticketsVendidos
        -ListaEnlazadaSimple~String~ logEventos
        -RecompensasService recompensasService
        -String estadoTaquillaUno
        -String estadoTaquillaDos
        +inscribirUsuario(Torneo torneo, String nombreUsuario)
        +iniciarVenta(Torneo torneo)
    }

    class TaquillaWorker {
        +run()
    }

    class Torneo {
        -String id
        -String nombre
        -String juego
        -String fecha
        -String hora
        -double precioTicket
        -int ticketsDisponibles
        -int ticketsIniciales
        -int ticketsVendidos
        -Cola~String~ colaEspera
        -boolean ventaActiva
        +incrementarTicketsVendidos()
    }

    class TicketVenta {
        -LocalDateTime fechaHora
        -String torneoId
        -String torneoNombre
        -String comprador
        -String taquilla
        -double precio
    }

    class Cola~T~
    class NodoCola~T~

    TorneosPanel --> TorneoService : usa
    TorneoService --> Torneo : administra
    TorneoService --> TicketVenta : registra ventas
    TorneoService --> RecompensasService : notifica XP
    TorneoService *-- TaquillaWorker : crea hilos
    Torneo *-- Cola~String~ : cola de espera
    Cola~T~ *-- NodoCola~T~ : compuesta por
```

### Explicación
- `TorneosPanel` muestra torneos, cola, log y estado de taquillas.
- `TorneoService` administra inscripciones y el proceso concurrente de venta de tickets.
- `TaquillaWorker` representa la clase interna que ejecuta cada hilo de venta.
- `Torneo` mantiene la cola de espera de compradores y el estado de la venta.
- `TicketVenta` guarda el historial de tickets procesados.

---

## 6. Diagrama del módulo Recompensas

```mermaid
classDiagram
    class RecompensasPanel
    class RecompensasService {
        -UsuarioProgreso usuario
        -ListaEnlazadaSimple~Logro~ logros
        -ListaEnlazadaSimple~LeaderboardEntry~ leaderboard
        -ListaEnlazadaSimple~String~ notificaciones
        +registrarInicioSesion()
        +registrarCompra(double montoTotal, int cantidadJuegos)
        +registrarCartaAgregada(Carta carta, boolean filaCompletaNueva)
        +sincronizarCartasEnAlbum(int cantidad)
        +registrarTicketComprado(String torneoId)
        +getNivel()
        +getRango()
        +getXpActualNivel()
        +getMetaNivel()
    }

    class UsuarioProgreso {
        -String nombre
        -String carnet
        -int xp
        -double dineroGastado
        -int juegosComprados
        -int cartasEnAlbum
        -int cartasLegendarias
        -int filasCompletas
        -ListaEnlazadaSimple~String~ torneosParticipados
        +sumarXp(int puntos)
        +registrarTorneoSiNoExiste(String torneoId)
        +getCantidadTorneosDistintos()
    }

    class Logro {
        -String nombre
        -String descripcion
        -boolean desbloqueado
        -String fechaDesbloqueo
    }

    class LeaderboardEntry {
        -String nombre
        -int xp
        -boolean usuarioActual
    }

    RecompensasPanel --> RecompensasService : usa
    RecompensasService --> UsuarioProgreso : actualiza
    RecompensasService --> Logro : desbloquea
    RecompensasService --> LeaderboardEntry : ordena/consulta
```

### Explicación
- `RecompensasPanel` presenta la información visual de XP, nivel, rango, logros y leaderboard.
- `RecompensasService` centraliza la lógica de gamificación del sistema.
- `UsuarioProgreso` almacena el estado acumulado del jugador.
- `Logro` representa cada logro desbloqueable.
- `LeaderboardEntry` modela cada fila del tablero de líderes.

---

## 7. Diagrama de persistencia y reportes

```mermaid
classDiagram
    class DataPaths {
        +BASE_DIR
        +CATALOGO
        +CARTAS_CATALOGO
        +HISTORIAL
        +ALBUM
        +TORNEOS
        +TICKETS
        +LOGROS
        +LEADERBOARD
        +ESTUDIANTE
        +PERFIL_USUARIO
    }

    class DataStore {
        +cargarCatalogo()
        +guardarCatalogo(...)
        +cargarCatalogoCartas()
        +cargarHistorial()
        +guardarHistorial(...)
        +cargarAlbum()
        +guardarAlbum(...)
        +cargarTorneos()
        +guardarTorneos(...)
        +cargarTicketsVendidos()
        +guardarTicketsVendidos(...)
        +cargarLogros()
        +guardarLogros(...)
        +cargarLeaderboard()
        +guardarLeaderboard(...)
        +cargarEstudianteInfo()
        +cargarUsuarioProgreso()
        +guardarUsuarioProgreso(...)
    }

    class ReporteService {
        +generarInventario(...)
        +generarVentas(...)
        +generarAlbum(...)
        +generarTorneos(...)
    }

    class AppContext

    AppContext --> DataStore : carga inicial
    AppContext --> ReporteService : genera reportes
    DataStore --> DataPaths : usa rutas
```

### Explicación
- `DataPaths` centraliza las rutas de los archivos de datos.
- `DataStore` se encarga de leer y escribir archivos `.txt` con la información persistente del sistema.
- `ReporteService` genera los reportes HTML solicitados por el proyecto.

---

## 8. Resumen de relaciones más importantes

### Relaciones de composición
- `ListaEnlazadaSimple` contiene `NodoSimple`.
- `Cola` contiene `NodoCola`.
- `MatrizOrtogonal` contiene `NodoMatriz`.
- `Compra` contiene varios `CompraDetalle`.
- `Torneo` contiene una `Cola<String>` para la fila de espera.

### Relaciones de dependencia
- Los paneles dependen de sus servicios correspondientes.
- Los servicios dependen de los modelos y estructuras de datos.
- `AppContext` coordina la inicialización global del sistema.
- `DataStore` depende de `DataPaths` para localizar los archivos.

### Relaciones funcionales
- `TiendaService`, `AlbumService` y `TorneoService` notifican eventos a `RecompensasService`.
- `ReporteService` toma información del contexto general para generar archivos HTML.
- `MainFrame` reúne todos los paneles y permite la navegación entre módulos.


