# PROYECTO 2
**Curso:** Laboratorio de IPC1  
**Estudiante:** ALVARO MOISÉS GIRÓN MORALES  
**Carné:** 20250195

# GameZone Pro

## Descripción general
GameZone Pro es una aplicación de escritorio desarrollada en **Java Swing** que centraliza distintas funciones relacionadas con una plataforma gamer. El sistema integra módulos de tienda de videojuegos, álbum de cartas coleccionables, eventos especiales con venta concurrente de tickets, programa de recompensas, tablero de líderes y generación de reportes HTML.

El proyecto fue desarrollado siguiendo el enunciado del Proyecto 2 del curso, el cual solicita una aplicación gráfica en Java con estructuras de datos implementadas desde cero, persistencia en archivos de texto, reportes HTML y documentación técnica y de usuario. fileciteturn0file0

## Características principales

### 1. Tienda de videojuegos
- Carga el catálogo desde archivo de texto.
- Permite buscar videojuegos por nombre o código.
- Permite filtrar por género y plataforma.
- Muestra los juegos en una interfaz gráfica tipo tarjetas.
- Permite agregar productos al carrito.
- Valida stock antes de confirmar la compra.
- Registra el historial de compras.

### 2. Álbum de cartas coleccionables
- Implementa una matriz ortogonal con nodos enlazados en cuatro direcciones.
- Permite visualizar el álbum gráficamente.
- Muestra cartas vacías y ocupadas.
- Permite agregar cartas, buscarlas e intercambiarlas.
- Presenta un panel de detalles con estadísticas de la carta seleccionada.

### 3. Eventos especiales y venta de tickets
- Carga torneos disponibles desde archivo.
- Registra usuarios en cola para compra de tickets.
- Procesa ventas con múltiples hilos de ejecución.
- Usa `synchronized` para evitar conflictos al desencolar.
- Actualiza la interfaz en tiempo real durante la simulación de taquillas.
- Registra el historial de tickets vendidos.

### 4. Programa de recompensas y leaderboard
- Asigna experiencia por acciones dentro del sistema.
- Calcula nivel, rango y progreso del usuario.
- Administra logros desbloqueables.
- Muestra una tabla de líderes.
- Ordena el leaderboard con algoritmo manual.

### 5. Reportes HTML
- Genera reportes de inventario.
- Genera reportes de ventas.
- Genera reportes del álbum.
- Genera reportes de torneos.
- Abre automáticamente los reportes en el navegador.

### 6. Datos del estudiante
- Incluye una pantalla con la información académica del estudiante y una breve descripción del proyecto.

## Tecnologías utilizadas
- **Java**
- **Java Swing**
- **NetBeans**
- **Archivos `.txt` para persistencia**
- **HTML + CSS embebido para reportes**

## Estructuras de datos implementadas
Este proyecto implementa estructuras propias, tal como lo requiere el enunciado del curso. Entre ellas se incluyen lista enlazada simple, cola y matriz ortogonal con nodos enlazados, además del uso de concurrencia con threads y `synchronized`. fileciteturn0file0

- **Lista enlazada simple**
  - Carrito de compras
  - Historial de compras
  - Tickets vendidos
  - Logros
  - Otras colecciones internas del sistema

- **Cola implementada desde cero**
  - Fila de espera para venta de tickets en torneos

- **Matriz ortogonal**
  - Representación interna del álbum de cartas

## Estructura del repositorio
Según el enunciado, el repositorio debe incluir la carpeta del proyecto Java, el README, manual técnico, manual de usuario, archivo JAR y diagrama de clases. fileciteturn0file0

En este repositorio se incluye:

```text
PROYECTO 2/
├── PROYECTO2/
│   ├── src/
│   ├── data/
│   ├── dist/
│   ├── build/
│   ├── nbproject/
│   ├── build.xml
│   └── manifest.mf
├── INFORMES/
│   ├── ManualTecnico.md
│   ├── ManualUsuario.md
│   ├── DiagramaClases.md
└── README.md
```

## Organización del código fuente
El proyecto está organizado por paquetes para separar responsabilidades:

- `ipc1.gamezonepro.datastructures`: estructuras de datos implementadas desde cero.
- `ipc1.gamezonepro.model`: clases del dominio del sistema.
- `ipc1.gamezonepro.persistence`: manejo de rutas y persistencia de archivos.
- `ipc1.gamezonepro.service`: lógica principal de negocio.
- `ipc1.gamezonepro.ui`: interfaz gráfica de usuario con Swing.
- `ipc1.gamezonepro.util`: utilidades visuales y de formato.

## Archivos de datos
El sistema utiliza archivos de texto para cargar y guardar información del programa:

- `catalogo.txt`
- `cartas_catalogo.txt`
- `torneos.txt`
- `historial.txt`
- `tickets_vendidos.txt`
- `album.txt`
- `leaderboard.txt`
- `logros.txt`
- `perfil_usuario.txt`
- `estudiante.txt`

## Cómo ejecutar el proyecto
### Opción 1: desde NetBeans
1. Abrir la carpeta `PROYECTO2` como proyecto en NetBeans.
2. Compilar el proyecto.
3. Ejecutar la clase principal:
   `ipc1.gamezonepro.GameZoneProApp`

### Opción 2: usando el archivo JAR
1. Ir a la carpeta `PROYECTO2/dist/`
2. Ejecutar el archivo:
   `GameZonePro.jar`

## Reportes generados
Los reportes HTML se almacenan en la carpeta `INFORMES` del repositorio. El sistema genera archivos con marca de tiempo y los intenta abrir automáticamente en el navegador.

## Funcionalidad general de la interfaz
La aplicación usa una ventana principal con navegación entre módulos, lo cual coincide con el requerimiento del proyecto de tener una GUI que permita acceso a tienda, álbum, torneos, recompensas, reportes, datos del estudiante y salida del sistema. fileciteturn0file0

## Contenido entregable del proyecto
Este proyecto fue preparado para cumplir con los entregables solicitados:
- Proyecto Java completo
- Código fuente comentado
- README.md
- Manual técnico en PDF
- Manual de usuario en PDF
- Archivo `.jar`
- Diagrama de clases
- Informe de desarrollo

## Autor
**ALVARO MOISÉS GIRÓN MORALES**  
**Carné:** 20250195  
**Curso:** Laboratorio de IPC1
