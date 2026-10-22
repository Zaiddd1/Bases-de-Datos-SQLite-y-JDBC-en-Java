<h1 align="center">🗄️ Bases de Datos SQLite y JDBC en Java — Lenguajes de Programación III</h1>

<p align="center">
  <em>Conexión a SQLite con JDBC, operaciones insertar / recuperar / actualizar / borrar,
  <code>PreparedStatement</code> y manejo de transacciones (<code>commit</code> y <code>rollback</code>).</em>
</p>

<p align="center">
  <img alt="Lenguaje" src="https://img.shields.io/badge/Java-8%2B-orange?logo=openjdk&logoColor=white">
  <img alt="Base de datos" src="https://img.shields.io/badge/SQLite-JDBC-003b57?logo=sqlite&logoColor=white">
  <img alt="Curso" src="https://img.shields.io/badge/Curso-LP%20III-6f42c1">
  <img alt="Sesión" src="https://img.shields.io/badge/Sesi%C3%B3n-08-brightgreen">
  <img alt="Estado" src="https://img.shields.io/badge/Compila-OK-success">
</p>

---

## 📌 Sobre este repositorio

Reúne el código de la **Sesión N.° 08** del curso **Lenguajes de Programación III**
(Escuela Profesional de Ingeniería de Sistemas — Universidad Católica de Santa María),
dedicada a las **bases de datos SQLite y JDBC en Java**.

Cada carpeta es un programa independiente. Todo el código **compila con JDK 21** y se probó con
`sqlite-jdbc-3.42.0.0`, la versión que indica la guía.

---

## 🗂️ Estructura del repositorio

```
Bases-de-Datos-SQLite-y-JDBC-en-Java/
├── actividades/
│   ├── actividad-01-conexion-sqlite/         Conectar, crear la tabla emp, insertar y mostrar
│   ├── actividad-02-preparedstatement/       Insertar con parámetros (?) usando PreparedStatement
│   ├── actividad-03-transacciones/           setAutoCommit(false), commit y rollback
│   └── actividad-04-crud-completo/           Experiencia de práctica: insertar, recuperar, actualizar y borrar
├── ejercicios/
│   ├── ejercicio-01-menu-productos/          Menú por consola; los cambios se confirman con una clave
│   └── ejercicio-02-gestor-consultas/        Gestor con arreglo de objetos y consultas hechas desde Java
└── README.md
```

---

## 🧪 Actividades

El código de las actividades 1 a 3 es el de la guía con **ajustes mínimos** para que compile y se pueda
ejecutar varias veces: se usan `DROP TABLE IF EXISTS` / `CREATE TABLE IF NOT EXISTS`, `insert or replace`
cuando el `id` podía repetirse, y en la actividad 2 se corrigió `setInt(1,55)` por `setInt(3,55)`
(el 55 es la edad, no el id).

| # | Carpeta | Qué hace |
|---|---|---|
| 1 | `actividad-01-conexion-sqlite` | Abre `ejemplo.db`, crea la tabla `emp (id, name, age)`, inserta 2 registros y los muestra. |
| 2 | `actividad-02-preparedstatement` | Inserta un registro con `PreparedStatement` y `setInt` / `setString`. |
| 3 | `actividad-03-transacciones` | Inserta 2 registros y hace `commit`; inserta uno más y hace `rollback`: ese último no queda guardado. |
| 4 | `actividad-04-crud-completo` | Base `practica.db` con la tabla `emp` de 3 campos: insertar, mostrar, actualizar y borrar con `PreparedStatement`, y un ejemplo de `rollback`. |

## 📝 Ejercicios propuestos

Usan la tabla `productos (id, nombre, precio, stock)` de la base `tienda.db`.

| # | Carpeta | Qué hace |
|---|---|---|
| 1 | `ejercicio-01-menu-productos` | Menú por consola para **ingresar, borrar, actualizar y mostrar**. Los cambios se confirman (`commit`) solo si el usuario escribe la clave (`1234`); si no, se revierten (`rollback`). |
| 2 | `ejercicio-02-gestor-consultas` | La clase `Gestor` guarda los registros en un arreglo de `Producto` y su método `consultar(...)` filtra, ordena y limita **desde Java**, según lo que indique el usuario: **a)** campos a mostrar, **b)** condición sobre un campo, **c)** orden ascendente o descendente, **d)** cantidad máxima de registros. |

Cada carpeta incluye `salida-ejemplo.txt` con una ejecución de ejemplo.

---

## ▶️ Cómo compilar y ejecutar

1. Necesitas un **JDK 8 o superior**.
2. Descarga el conector SQLite (no se incluye en el repositorio):
   [`sqlite-jdbc-3.42.0.0.jar`](https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.42.0.0/sqlite-jdbc-3.42.0.0.jar)
   y guárdalo, por ejemplo, en una carpeta `lib/`.
3. Compila y ejecuta **desde dentro de la carpeta** del programa (las bases `.db` se crean ahí).

En Windows:

```bash
cd ejercicios/ejercicio-01-menu-productos
javac -encoding UTF-8 *.java
java -cp ".;../../lib/sqlite-jdbc-3.42.0.0.jar" Main
```

En Linux o macOS se cambia `;` por `:` en el `-cp`. Las clases principales son `Principal` (actividades 1 a 3),
`Practica` (actividad 4) y `Main` (ejercicios).

---

## 🧠 Conceptos cubiertos

`SQLite` · `JDBC` · `DriverManager` · `Connection` · `Statement` · `PreparedStatement` · `ResultSet` ·
`executeUpdate` / `executeQuery` · `setAutoCommit` · `commit` · `rollback` · `ArrayList`

---

<p align="center"><sub>
Material con fines educativos — Universidad Católica de Santa María · Ingeniería de Sistemas · Lenguajes de Programación III · Sesión N.° 08
</sub></p>
