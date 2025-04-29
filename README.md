# TP-Tec-Avanzadas-Prog
## Enunciado:
Una pequeña sala de teatro independiente requiere que se gestione la venta de entradas para sus espectáculos. Cuentan con dos espacios una sala con capacidad para 70 personas y un anfiteatro a cielo abierto con una capacidad para 120 personas. La primera posee un costo variable en las entradas, las tipo A, tienen un costo del doble de las tipo B. El anfiteatro tiene precio único.
Para lo cual un usuario registrado en el sistema registra la siguiente información:
- Artista
- Fecha de la función
- Hora de la función
- Sala
- Precio de la entrada
- Duración
- Tipo de show (infantil, musical, obra de teatro)

En el proceso de carga se debe validar que no se superpongan los espectaculos en una misma fecha y hora, para permitir la carga de espectaculos un mismo día, debe haber una hora libre en la sala para su limpieza previa al inicio del proximo show. El sistema debe poder mostrar los espectaculos próximos a presentarse en la sala, así como los anteriores, en este ultimo caso por supuesto no debe poder permitir realizar la compra de entradas.

# Desarrollo:
## 1. **Diagrama de clases (versión básica inicial)**

- `Usuario`
    - Atributos: int id, String nombre, String email.
    - Métodos: Usuario crearUsuario(), void comprarEntrada(),
- `Sala`
    - Atributos: int nroSala, String nombreSala, int capacidad.
    - Métodos:
- `Funcion`
    - Atributos: int idEspectaculo, int nroSala, date fechaFuncion, int horaFuncion, int idArtistaFuncion, int duracion, String tipoFuncion.
    - Métodos: Funcion crearFuncion().
- `Entrada`
    - Atributos: idEntrada, idFuncion, idUsuario, precio
    - Métodos: calcularPrecio()

---

## 2. **Modelo de datos**

- `usuarios` (id, nombre, email, contraseña encriptada)
- `salas` (id, nombre, capacidad, tipo_precio)
- `funciones` (id, artista, fecha, hora, sala_id, tipo_show, duración)
- `entradas` (id, espectaculo_id, tipo_entrada, precio, vendida: boolean)

---

## 3. **Tecnologías elegidas**

- **Lenguaje**: Java 17
- **Backend**: Spring Boot (extensión del framework Spring)
- **Frontend**: React con Next.js
- **Base de datos**: PostgreSQL

---

### 4. **Testeo**

- Pruebas unitarias en servicios (FuncionService, EntradaService, etc).
- Test de integración de login/autenticación.
- Otras herramientas:
    - Git para control de versiones.
    - Maven para la gestión de dependencias en el backend.
    - NPM para gestionar las dependencias del frontend.
