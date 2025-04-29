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

![image](https://github.com/user-attachments/assets/e1e9879f-f26a-4758-b8b5-faf2b9028b13)

---

## 2. **Modelo de datos**

- `usuarios` (id, nombre, email, contraseña encriptada)
- `salas` (id, nombre, capacidad, tipo_precio)
- `funciones` (id, artista, fecha, hora, sala_id, tipo_show, duración)
- `entradas` (id, espectaculo_id, tipo_entrada, precio, vendida: boolean)

---

## 3. **Tecnologías elegidas**

- **Lenguaje**: Java 17
Elegí Java porque es un lenguaje que mejor conozco, es confiable y se usa mucho en proyectos grandes. La versión 17 es bastante actual, estable y tiene mejoras que lo hacen más cómodo de usar.
- **Backend**: Spring Boot (extensión del framework Spring)
Spring Boot ayuda a crear aplicaciones de backend más rápido. Viene con muchas cosas ya listas para usar, como la conexión a la base de datos y el manejo de errores. Además, es muy común en proyectos con Java, y como no lo habia utilizado antes, me gustaría probarlo y sumar experiencia.
- **Frontend**: React con Next.js
Usaré React porque es una herramienta muy buena para hacer páginas web interactivas. Next.js mejora todavía más React, ayudando a que las páginas carguen más rápido y se organicen mejor.
- **Base de datos**: PostgreSQL
Como el trabajo sigue un modelo de datos relacional, elegí usar un motor de base de datos relacional. En particular, opté por PostgreSQL porque es una base de datos muy completa y segura, ideal para este tipo de sistemas

---

### 4. **Testeo**

- Pruebas unitarias en servicios (FuncionService, EntradaService, etc).
- Test de integración de login/autenticación.
- Otras herramientas:
    - Git para control de versiones.
    - Maven para la gestión de dependencias en el backend.
    - NPM para gestionar las dependencias del frontend.
