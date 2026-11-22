# GameLibrary - Catálogo de videojuegos con MySQL
##### * Jose Martínez Gil *


GameLibrary es una aplicación de consola que he desarrollado en **Kotlin** para la asignatura de **Acceso a Datos de 2.º de DAM**.

Con este proyecto puedo gestionar videojuegos, crear jugadores y organizar los videojuegos en colecciones. La información se guarda en una base de datos **MySQL** y la conexión desde Kotlin se realiza mediante **JDBC**.

## Tecnologías utilizadas

- **Kotlin:** para desarrollar la aplicación.
- **MySQL:** para almacenar los datos.
- **JDBC:** para conectar Kotlin con MySQL y ejecutar las consultas SQL.
- **Docker:** para ejecutar MySQL en un contenedor.
- **Gradle:** para gestionar las dependencias.
- **IntelliJ IDEA:** para desarrollar y ejecutar el proyecto.

## Funcionalidades de la aplicación

He dividido la aplicación en varios menús para organizar las opciones.

### Videojuegos

- Consultar todos los videojuegos.
- Buscar un videojuego por su ID.
- Añadir videojuegos indicando título, género, año y puntuación.
- Modificar la puntuación de un videojuego.
- Eliminar videojuegos.

### Jugadores

- Crear jugadores.
- Eliminar jugadores por su ID.
- Acceder al menú de colecciones introduciendo el ID de un jugador existente.

### Colecciones

- Consultar únicamente las colecciones del jugador seleccionado.
- Crear una colección asociada a ese jugador.
- Buscar una de sus colecciones por ID y ver los videojuegos que contiene.
- Cambiar el nombre de sus colecciones.
- Añadir y quitar videojuegos de sus colecciones.
- Eliminar sus colecciones.

Para estas operaciones, la aplicación comprueba el ID del jugador y el propietario de cada colección antes de permitir los cambios.

## Organización del proyecto

He separado los menús, las operaciones y las consultas SQL para no tener todo el código en un solo archivo.

```text
GameLibrary-with-Database/
├── datos/
│   └── videogames.sql
├── src/main/kotlin/org/example/
│   ├── App.kt
│   ├── database/
│   │   └── ConnectionDB.kt
│   ├── model/
│   ├── repository/
│   └── service/
│       └── AppService.kt
├── build.gradle.kts
└── LEEME.md
```

- **`App.kt`:** contiene los menús y permite pasar de uno a otro.
- **`AppService.kt`:** pide los datos por consola y llama a los DAO.
- **`model`:** contiene las `data class` que representan las tablas.
- **`repository`:** contiene los DAO, donde he programado las consultas SQL.
- **`ConnectionDB.kt`:** establece la conexión con MySQL.

## Estructura de la base de datos

La base de datos se llama **`gamelibrary`** y está formada por cuatro tablas:

| Tabla | Campos principales | Función |
| --- | --- | --- |
| `videogames` | `id_videogame` (PK), `title`, `genre`, `year`, `rating` | Guarda los videojuegos. |
| `players` | `id_player` (PK), `name` | Guarda los jugadores. |
| `collections` | `id_collection` (PK), `name`, `id_player` (FK) | Guarda las colecciones y el jugador al que pertenecen. |
| `videogame_collection` | `id_videogame` (FK), `id_collection` (FK), `date_added` | Relaciona los videojuegos con las colecciones. |

Cada jugador puede tener varias colecciones, pero cada colección pertenece a un jugador. Una colección puede contener varios videojuegos y un mismo videojuego puede estar en varias colecciones. Por eso utilizo la tabla intermedia `videogame_collection`, que tiene una clave primaria compuesta por `id_videogame` e `id_collection`.

Las relaciones están definidas mediante claves foráneas. He configurado **`ON DELETE CASCADE`** para que, al eliminar un jugador, también se eliminen sus colecciones y las relaciones que dependan de ellas.

En las pruebas de la aplicación he trabajado con cinco videojuegos, tres jugadores y tres colecciones iniciales, cada una asociada a un jugador distinto.

El archivo **`datos/videogames.sql`** se utiliza para crear la estructura de la base de datos y cargar los datos iniciales. He mantenido los nombres de las tablas, clases y archivos de código en inglés, aunque en el enunciado aparezcan sus equivalentes en español.

## Cómo ejecutar el proyecto

### 1. Requisitos

Para ejecutar la aplicación se necesita:

- Un JDK compatible con la configuración de Gradle del proyecto.
- IntelliJ IDEA o un entorno que permita ejecutar Kotlin con Gradle.
- Docker Desktop en funcionamiento, con un contenedor de MySQL disponible.

### 2. Preparar MySQL con Docker

Para crear el contenedor de **MySQL 8.4**, se puede ejecutar este comando desde PowerShell. Hay que sustituir `tu_contrasena` por la contraseña que se quiera utilizar:

```powershell
docker run -d --name gamelibrary-mysql -e "MYSQL_ROOT_PASSWORD=tu_contrasena" -e "MYSQL_DATABASE=gamelibrary" -p 127.0.0.1:3306:3306 -v gamelibrary_mysql_data:/var/lib/mysql mysql:8.4
```

Si el contenedor ya existe pero está detenido, basta con iniciarlo:

```powershell
docker start gamelibrary-mysql
```

### 3. Importar las tablas y los datos

Desde la carpeta raíz del proyecto, se copia el script SQL al contenedor:

```powershell
docker cp datos/videogames.sql gamelibrary-mysql:/tmp/videogames.sql
```

Después se abre MySQL:

```powershell
docker exec -it gamelibrary-mysql mysql -u root -p gamelibrary
```

Una vez introducida la contraseña, se importa el archivo:

```sql
SOURCE /tmp/videogames.sql;
```

Si la base de datos ya tiene los datos cargados, no es necesario repetir la importación.

### 4. Configurar la conexión en IntelliJ IDEA

En `ConnectionDB.kt` tengo seleccionado MySQL:

```kotlin
private val databaseType = "mysql"
```

La conexión utiliza `localhost:3306`, el usuario `root` y la base de datos `gamelibrary`.

Para indicar la contraseña sin escribirla directamente en el código, en **Run > Edit Configurations** se añade esta variable de entorno:

```text
MYSQL_PASSWORD=tu_contrasena
```

Debe coincidir con la contraseña utilizada al crear el contenedor.

### 5. Iniciar la aplicación

Abro el proyecto en IntelliJ IDEA, espero a que Gradle cargue las dependencias y ejecuto `App.kt`.

Si la conexión funciona, aparece el menú principal. Desde ahí puedo entrar en el menú de videojuegos o en el de jugadores. Para añadir un videojuego a una colección, la aplicación pide una fecha en formato `yyyy-MM-dd HH:mm:ss`, por ejemplo, `2026-11-22 15:00:00`.

## Decisiones de diseño

### Organización mediante submenús

En el enunciado aparece un único menú con todas las opciones, pero he preferido dividirlo en varios submenús para que no estén todas mezcladas.

He creado un menú principal con acceso a **videojuegos** y **jugadores**. Desde el menú de jugadores se puede entrar en el de **colecciones**. Así puedo tener agrupadas las opciones de cada apartado y volver fácilmente al menú anterior.

### Acceso de jugadores mediante su ID

He decidido que, para entrar al menú de colecciones, primero haya que introducir el **ID del jugador**. La aplicación comprueba en MySQL que el jugador existe y, si lo encuentra, muestra su nombre y permite continuar. Si no existe, vuelve al menú de jugadores.

No es un inicio de sesión con contraseña: es una forma sencilla de seleccionar al jugador dentro de una aplicación de consola.

### Colecciones asociadas a cada jugador

La tabla `collections` incluye `id_player` como clave foránea. Cuando creo una colección desde el menú de un jugador, se guarda asociada a ese ID.

He decidido que **cada jugador pueda ver y gestionar solamente sus propias colecciones**. Cuando entra en el menú, el listado muestra únicamente las colecciones que tienen su `id_player`.

Además, antes de consultar una colección, cambiar su nombre, eliminarla o añadir y quitar videojuegos, la aplicación comprueba que esa colección pertenece al jugador seleccionado. Para ello, comparo el `idPlayer` de la colección con el ID del jugador que está utilizando el menú.

Si intenta utilizar una colección de otro jugador, la operación se cancela y se muestra un mensaje. He añadido estas comprobaciones para que cada jugador tenga sus colecciones separadas, sin complicar el funcionamiento de la aplicación.

### Separación entre App, AppService y DAO

Al principio tenía casi todas las funciones en `App.kt` y el archivo se estaba haciendo demasiado largo. Por eso he trasladado las operaciones a `AppService.kt` y he dejado los menús en `App.kt`.

Los DAO siguen encargándose de las consultas SQL. He mantenido esta organización porque me resulta más fácil localizar las funciones y entender qué hace cada parte del programa.

### Uso de MySQL y transacciones

He utilizado MySQL con Docker para trabajar con una base de datos cliente-servidor. En los DAO utilizo `PreparedStatement` para ejecutar las consultas con parámetros.

Para añadir un videojuego a una colección he utilizado una transacción JDBC: desactivo `autoCommit`, confirmo la inserción mediante `commit()` y utilizo `rollback()` si se produce un error SQL.

## Comprobaciones realizadas

Durante el desarrollo he ido probando las funciones de los DAO y las opciones de los menús con MySQL.

He comprobado las operaciones de creación, consulta, modificación y eliminación de videojuegos y colecciones, así como la creación y eliminación de jugadores. También he probado cómo añadir y quitar videojuegos de una colección y cómo consultar su contenido.

Para comprobar la gestión de errores de la transacción, intenté añadir dos veces el mismo videojuego a una colección. MySQL rechazó la segunda inserción por la clave primaria compuesta y pude comprobar la gestión del error mediante `rollback()`.

Después de trasladar las funciones a `AppService.kt`, volví a ejecutar la aplicación y comprobé que seguía conectándose a MySQL y que podía navegar por sus menús.

Por último, probé la gestión de colecciones entrando con distintos jugadores. Comprobé que cada uno veía sus propias colecciones y que no podía consultar, modificar ni eliminar las de otro jugador. También comprobé que se rechazaban los intentos de añadir o quitar videojuegos de colecciones ajenas.

## Conclusión

Con esta práctica he aprendido a conectar una aplicación Kotlin con MySQL mediante JDBC, a organizar los datos en tablas relacionadas y a utilizar los DAO para separar las consultas SQL del resto del programa.

También he trabajado con transacciones, he añadido comprobaciones para que cada jugador gestione sus propias colecciones y he reorganizado el proyecto para tener el código más ordenado y facilitar su mantenimiento.
