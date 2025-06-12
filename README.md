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
## 1. **Diagrama de clases**

![image](https://github.com/user-attachments/assets/cb79e194-4599-4a30-8089-67dd3b2bc5a7)

---

## 2. **Modelo de datos**

![image](https://github.com/user-attachments/assets/ce049bc1-53c8-4460-bd7f-96ea34048597)

---

## 3. **Tecnologías elegidas**

- **Lenguaje**: Java 21.

Elegí Java porque es el lenguaje que mejor conozco, es confiable y se usa mucho en proyectos grandes. Se eligió la versión 21 porque es la última versión LTS (Long-Term Support) publicada por Oracle.

- **Backend**: Spring Boot (extensión del framework Spring).

Spring Boot ayuda a crear aplicaciones de backend más rápido. Viene con muchas cosas ya listas para usar, como la conexión a la base de datos y el manejo de errores. Además, es muy común en proyectos con Java, y como no lo habia utilizado antes, me gustaría probarlo y sumar experiencia.

- **Base de datos**: PostgreSQL.

Como el trabajo sigue un modelo de datos relacional, elegí usar un motor de base de datos relacional. En particular, opté por PostgreSQL porque es una base de datos muy completa y segura, ideal para este tipo de sistemas.

---

### 4. **Testeo**

- Pruebas unitarias en servicios, controllers y repositorios con Spring Boot Test.


## Proceso de despliegue

### 1 - Clonar el repositorio:
```git clone https://github.com/EloyRomero81/TP-Tec-Avanzadas-Prog.git```

### 2 - Moverse a la carpeta del proyecto
```cd TP-Tec-Avanzadas-Prog```

### 3 - Levantar la API con Docker
```docker-compose up --build```

