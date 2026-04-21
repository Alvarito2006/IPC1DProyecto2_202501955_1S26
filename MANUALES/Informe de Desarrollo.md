# PROYECTO 2
**Curso:** Laboratorio de IPC1  
**Estudiante:** ALVARO MOISÉS GIRÓN MORALES  
**Carné:** 20250195

---

# Informe de Desarrollo
## GameZone Pro

## 1. Introducción
El presente informe de desarrollo describe el proceso de implementación del proyecto **GameZone Pro**, una aplicación de escritorio desarrollada en **Java** con interfaz gráfica en **Swing**. El sistema integra varios módulos funcionales dentro de una misma plataforma: tienda de videojuegos, álbum de cartas coleccionables, eventos especiales con venta concurrente de tickets, programa de recompensas y generación de reportes HTML.

El propósito de este documento es explicar de forma clara cómo fue construido el sistema, qué decisiones técnicas se tomaron, cuáles fueron los principales problemas encontrados durante el desarrollo y qué soluciones se aplicaron para lograr un programa funcional, organizado y acorde a los requerimientos del proyecto.

---

## 2. Objetivo del proyecto
El objetivo principal fue desarrollar una aplicación de escritorio que permitiera integrar distintas funcionalidades relacionadas con una plataforma gamer, utilizando estructuras de datos implementadas manualmente, persistencia en archivos de texto, interfaces gráficas en Swing y manejo de concurrencia mediante hilos.

Además de cumplir la parte funcional, el proyecto también buscó reforzar conceptos de:

- Programación orientada a objetos.
- Diseño modular del código.
- Separación entre interfaz, lógica y persistencia.
- Implementación de estructuras desde cero.
- Actualización segura de la GUI en procesos concurrentes.
- Generación de documentación técnica y de usuario.

---

## 3. Descripción general del sistema
**GameZone Pro** es una aplicación que centraliza varias experiencias en una sola interfaz. El sistema cuenta con una ventana principal desde la cual se accede a los módulos del proyecto.

### Módulos implementados
1. **Tienda de Videojuegos**  
   Permite visualizar el catálogo, buscar juegos, filtrarlos, agregarlos al carrito y confirmar compras.

2. **Álbum de Cartas Coleccionables**  
   Permite gestionar un álbum representado internamente por una matriz ortogonal enlazada.

3. **Eventos Especiales**  
   Simula la compra de tickets para torneos mediante una cola propia y dos taquillas concurrentes.

4. **Programa de Recompensas y Tablero de Líderes**  
   Lleva el control de experiencia, nivel, rango, logros desbloqueados y leaderboard.

5. **Reportes HTML**  
   Genera reportes automáticos de inventario, ventas, álbum y torneos.

6. **Datos del Estudiante**  
   Muestra la información académica asociada al proyecto.

<img width="1919" height="959" alt="image" src="https://github.com/user-attachments/assets/47d8596c-b278-47e1-b325-b6601ff3f6f5" />


---

## 4. Arquitectura utilizada
Para mantener el proyecto ordenado, se dividió en paquetes según responsabilidad.

### 4.1 Capa de presentación
Contiene todos los paneles y ventanas Swing del sistema.

- `MainFrame`
- `TiendaPanel`
- `AlbumPanel`
- `TorneosPanel`
- `RecompensasPanel`
- `ReportesPanel`
- `EstudiantePanel`

### 4.2 Capa de servicios
Contiene la lógica principal de cada módulo.

- `TiendaService`
- `AlbumService`
- `TorneoService`
- `RecompensasService`
- `ReporteService`
- `AppContext`

### 4.3 Capa de persistencia
Se encarga de leer y guardar la información en archivos de texto.

- `DataStore`
- `DataPaths`

### 4.4 Capa de modelo
Representa las entidades del dominio.

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

### 4.5 Capa de estructuras de datos
Contiene las estructuras implementadas desde cero, sin usar Java Collections Framework.

- `ListaEnlazadaSimple`
- `NodoSimple`
- `Cola`
- `NodoCola`
- `MatrizOrtogonal`
- `NodoMatriz`

Esta organización facilitó la separación de responsabilidades y permitió que la interfaz no estuviera mezclada directamente con el acceso a archivos ni con la lógica del negocio.

---

## 5. Implementación del sistema

### 5.1 Inicio de la aplicación
La ejecución del programa inicia desde la clase `GameZoneProApp`, donde primero se configura el estilo visual del sistema y luego se crea la interfaz usando `SwingUtilities.invokeLater()`.

Después, se construye un `AppContext`, el cual carga los datos iniciales del programa y crea los servicios necesarios para los módulos.

### 5.2 Carga de datos
Al iniciar el sistema, se cargan archivos como:

- `catalogo.txt`
- `cartas_catalogo.txt`
- `album.txt`
- `torneos.txt`
- `historial.txt`
- `tickets_vendidos.txt`
- `leaderboard.txt`
- `logros.txt`
- `perfil_usuario.txt`
- `estudiante.txt`

Esto permite que el sistema conserve información entre una ejecución y otra.

### 5.3 Guardado de datos
Cuando la aplicación se cierra, el contexto principal guarda automáticamente los cambios realizados durante la sesión, como compras, tickets vendidos, progreso del usuario, estado del álbum y actualizaciones del leaderboard.

---

## 6. Desarrollo por módulos

## 6.1 Módulo de Tienda de Videojuegos
Este módulo fue desarrollado para gestionar el catálogo, el carrito y el historial de compras.

### Funcionalidades implementadas
- Carga del catálogo desde archivo.
- Búsqueda por nombre o código.
- Filtro por género y plataforma.
- Visualización de juegos en interfaz gráfica.
- Agregado de productos al carrito.
- Modificación de cantidades.
- Eliminación de productos.
- Validación de stock.
- Registro de compras.
- Actualización del historial.

### Lógica aplicada
Para este módulo se utilizó una **lista enlazada simple** como estructura principal del carrito y del historial de compras. Esta decisión permitió cumplir con la restricción del proyecto de no utilizar `ArrayList` ni otras colecciones estándar.

El método de confirmación de compra verifica si el carrito tiene productos, valida el stock de cada videojuego, descuenta existencias, genera el detalle de compra, limpia el carrito y notifica al módulo de recompensas para sumar experiencia.

### Resultado del módulo
El usuario puede explorar el catálogo, seleccionar juegos y generar compras válidas, conservando el historial de forma persistente.

**Espacios para capturas:**  
`[Insertar aquí captura del catálogo de videojuegos]`  
`[Insertar aquí captura del carrito con productos agregados]`  
`[Insertar aquí captura de compra confirmada o advertencia de stock]`  
`[Insertar aquí captura del historial de compras]`

---

## 6.2 Módulo de Álbum de Cartas
Este módulo fue construido para representar un álbum visual con cartas coleccionables.

### Funcionalidades implementadas
- Visualización del álbum en una cuadrícula.
- Inserción de cartas desde catálogo.
- Creación manual de cartas.
- Ubicación automática en la primera celda vacía.
- Búsqueda por nombre, tipo o rareza.
- Selección de cartas y visualización de detalles.
- Intercambio entre posiciones del álbum.
- Persistencia del estado del álbum.

### Lógica aplicada
Internamente se utilizó una **matriz ortogonal** compuesta por nodos enlazados en cuatro direcciones: arriba, abajo, izquierda y derecha. Cada nodo puede almacenar una carta o permanecer vacío.

Esta estructura permitió recorrer el álbum sin usar arreglos tradicionales como base principal de almacenamiento. Además, facilitó operaciones como:

- Colocar una carta en la primera posición vacía.
- Verificar si una fila se completó.
- Intercambiar cartas entre dos celdas.
- Buscar una carta por su código dentro del álbum.


---

## 6.3 Módulo de Eventos Especiales
Este módulo se desarrolló para simular la inscripción y venta de tickets para torneos.

### Funcionalidades implementadas
- Carga de torneos desde archivo.
- Inscripción de usuarios a una cola de espera.
- Inicio de venta de tickets.
- Ejecución de dos taquillas concurrentes.
- Actualización visual del estado de las taquillas.
- Registro de tickets vendidos.
- Cierre automático de venta al agotarse tickets o vaciarse la cola.

### Lógica aplicada
Se implementó una **cola propia** para almacenar la fila de usuarios. La venta de tickets se procesa mediante dos hilos (`Taquilla 1` y `Taquilla 2`), cada uno ejecutando la misma lógica de atención.

Para evitar errores por acceso simultáneo, se aplicaron mecanismos de sincronización:

- `desencolar()` sincronizado dentro de la cola.
- Bloque `synchronized` sobre el torneo para descontar tickets de forma segura.
- Uso de `SwingUtilities.invokeLater()` para reflejar cambios en la interfaz sin romper el modelo de ejecución de Swing.

### Resultado del módulo
Se logró simular un escenario concurrente de forma funcional, mostrando en tiempo real la atención de usuarios, el estado de las taquillas y los tickets vendidos.

**Espacios para capturas:**  
`[Insertar aquí captura de la lista de torneos]`  
`[Insertar aquí captura de usuarios agregados a la cola]`  
`[Insertar aquí captura de las taquillas procesando tickets]`  
`[Insertar aquí captura del log de eventos o tickets vendidos]`

---

## 6.4 Módulo de Recompensas y Leaderboard
Este módulo se desarrolló para dar seguimiento al progreso del usuario y mostrar sus logros.

### Funcionalidades implementadas
- Registro de XP por acciones.
- Cálculo automático de nivel y rango.
- Visualización de barra de progreso.
- Desbloqueo de logros.
- Registro de notificaciones internas.
- Sincronización del usuario con el leaderboard.
- Ordenamiento manual del ranking.

### Lógica aplicada
Cada acción del usuario genera puntos:

- Compra de juegos.
- Agregado de cartas.
- Obtención de cartas legendarias.
- Completado de filas del álbum.
- Compra de tickets.
- Inicio de sesión.

Con base en la experiencia acumulada, el sistema calcula el nivel actual y el rango correspondiente. Además, se actualizan los logros desbloqueados y se sincroniza la entrada del usuario dentro del leaderboard.

Para cumplir con la restricción del proyecto, el ordenamiento del ranking se resuelve con lógica implementada manualmente y no con métodos de ordenamiento de bibliotecas estándar.

### Resultado del módulo
El sistema ofrece retroalimentación inmediata al usuario sobre su progreso y mejora la integración entre módulos, ya que las acciones realizadas en tienda, álbum y torneos afectan directamente la experiencia acumulada.

<img width="1916" height="979" alt="image" src="https://github.com/user-attachments/assets/7e13aee7-0f3b-4047-a703-a003737900eb" />


---

## 6.5 Módulo de Reportes HTML
Este módulo se desarrolló para generar archivos HTML con información del sistema.

### Reportes implementados
- Reporte de inventario.
- Reporte de ventas.
- Reporte del álbum.
- Reporte de torneos.

### Lógica aplicada
Cada reporte genera una estructura HTML con estilos CSS embebidos. Los archivos se nombran con marca de tiempo para evitar sobrescrituras y luego se intenta abrir automáticamente el resultado en el navegador del sistema.

### Resultado del módulo
Se logró exportar la información relevante de distintos módulos en archivos visualmente organizados y fáciles de revisar.

**Espacios para capturas:**  
`[Insertar aquí captura del panel de reportes]`  
`[Insertar aquí captura del navegador mostrando un reporte HTML]`

---

## 7. Problemas encontrados durante el desarrollo
Durante la implementación del proyecto surgieron distintos problemas técnicos y de organización. A continuación se describen los principales.

### 7.1 Organización del proyecto
Al principio, mezclar la lógica del sistema con la interfaz hacía que el código fuera más difícil de mantener.

**Solución aplicada:**  
Se separó el proyecto por paquetes, diferenciando interfaz, servicios, persistencia, modelos y estructuras de datos.

### 7.2 Manejo de estructuras sin Java Collections
Uno de los mayores retos fue trabajar sin `ArrayList`, `LinkedList`, `HashMap` u otras clases estándar.

**Solución aplicada:**  
Se implementaron estructuras propias desde cero, utilizando nodos y recorridos manuales para insertar, eliminar, buscar y mostrar información.

### 7.3 Validación de datos de entrada
Al capturar información desde la interfaz, podían producirse errores como campos vacíos, números inválidos o datos inconsistentes.

**Solución aplicada:**  
Se agregaron validaciones en los formularios y en la lógica de servicios para evitar que el programa fallara o almacenara información incorrecta.

### 7.4 Persistencia de información
Otro problema fue asegurar que los datos cargados al inicio se mantuvieran actualizados al cerrar el programa.

**Solución aplicada:**  
Se centralizó la lectura y escritura de archivos en la clase `DataStore` y se definió un guardado general al cerrar la aplicación.

### 7.5 Concurrencia en torneos
El procesamiento simultáneo de tickets generaba riesgo de condiciones de carrera, especialmente al descontar tickets y al sacar usuarios de la cola.

**Solución aplicada:**  
Se utilizó `synchronized` en las operaciones críticas y se actualizó la interfaz mediante `SwingUtilities.invokeLater()` para evitar errores de sincronización con Swing.

### 7.6 Actualización visual del sistema
En módulos con cambios frecuentes, especialmente torneos y recompensas, la interfaz debía refrescarse correctamente.

**Solución aplicada:**  
Se creó una lógica de actualización basada en notificaciones internas y refresco de paneles cuando ocurrían cambios relevantes.

---

## 8. Decisiones técnicas adoptadas
Durante el desarrollo se tomaron varias decisiones para mantener estabilidad y claridad en la implementación.

### Decisiones importantes
- Usar una arquitectura por capas simples para mejorar la organización.
- Implementar estructuras genéricas donde fuera posible.
- Mantener la persistencia en archivos de texto para cumplir con el enunciado.
- Separar la lógica de negocio del código visual.
- Reutilizar servicios desde un contexto principal compartido.
- Centralizar las rutas y acceso a datos para evitar duplicación.
- Utilizar reportes HTML como mecanismo de salida visual adicional.

Estas decisiones ayudaron a que el sistema fuera más fácil de mantener, extender y documentar.

---

## 9. Resultados obtenidos
Al finalizar el desarrollo, se obtuvo una aplicación funcional que cumple con los requerimientos principales del proyecto:

- Interfaz gráfica desarrollada en Java Swing.
- Módulo de tienda con carrito e historial.
- Módulo de álbum con matriz ortogonal enlazada.
- Módulo de torneos con cola propia e hilos concurrentes.
- Sistema de recompensas con niveles, XP y logros.
- Leaderboard actualizado y ordenado.
- Persistencia de datos en archivos de texto.
- Generación de reportes HTML.
- Organización del código por paquetes.

El proyecto permitió aplicar varios temas vistos en el curso dentro de una sola aplicación de mayor tamaño y complejidad que prácticas anteriores.

---

## 10. Conclusiones
1. El desarrollo del proyecto permitió integrar en una sola aplicación conceptos de estructuras de datos, interfaces gráficas, archivos y concurrencia.
2. La implementación de estructuras desde cero ayudó a comprender mejor el manejo de nodos, recorridos y almacenamiento manual de datos.
3. El uso de hilos en el módulo de torneos mostró la importancia de sincronizar correctamente los recursos compartidos.
4. La separación entre interfaz, servicios y persistencia facilitó el mantenimiento del código.
5. La documentación del sistema ayuda a comprender mejor tanto la parte técnica como el uso general del programa.

---

## 11. Recomendaciones de mejora
Como trabajo futuro, el sistema podría ampliarse con:

- Más validaciones visuales y mensajes contextuales.
- Reportes con gráficos o estadísticas más avanzadas.
- Soporte para más torneos y tipos de cartas.
- Mejora en la apariencia visual con componentes personalizados.
- Mayor modularidad para nuevos tipos de recompensas.

---

## 12. Anexos
<img width="1919" height="959" alt="image" src="https://github.com/user-attachments/assets/5d25883e-ecc9-4c6a-b30a-8e410ffd826c" />

<img width="1919" height="984" alt="image" src="https://github.com/user-attachments/assets/95dfa6dc-c730-47c7-a5db-42493c39d460" />

<img width="1918" height="971" alt="image" src="https://github.com/user-attachments/assets/ee471141-44df-4d8d-b411-ce918592027d" />

<img width="1914" height="975" alt="image" src="https://github.com/user-attachments/assets/632ca7c8-7203-479c-b6ec-baa465486b69" />

<img width="1917" height="985" alt="image" src="https://github.com/user-attachments/assets/2bafcfe6-09cc-4bb8-9824-a45358ebb90c" />

<img width="1918" height="966" alt="image" src="https://github.com/user-attachments/assets/e6035d16-e948-4e68-a37f-d76795af9bce" />







