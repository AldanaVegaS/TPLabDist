# Sistema Distribuido de Horóscopo y Clima mediante Sockets

Este proyecto implementa un sistema distribuido basado en una arquitectura **cliente-servidor**, compuesto por un **Servidor Central** y dos **servidores secundarios** (Horóscopo y Clima). El cliente envía una solicitud que es procesada y coordinada por el servidor central.

---

## Componentes del sistema

- **Cliente**: Interfaz de usuario que envía solicitudes.
- **Servidor Central**: Recibe la petición, la valida y delega consultas.
- **Servidor Horóscopo**: Procesa el signo zodiacal.
- **Servidor Clima**: Procesa la fecha.

---

## Ejecución básica

### 1. Compilar las clases

Desde el directorio del proyecto:


```
javac ServidorCentral.java
javac ServidorHoroscopo.java
javac ServidorClima.java
javac Cliente.java
````

---

### 2. Ejecutar el servidor central

```
java ServidorCentral
```

---

### 3. Ejecutar el servidor de horóscopo

En otra terminal:

```
java ServidorHoroscopo
```

---

### 4. Ejecutar el servidor del clima

En otra terminal:

```
java ServidorClima
```

---

### 5. Ejecutar el cliente

En una tercera terminal:

```
java Cliente
```

---

## Ejecución avanzada

Para el nivel avanzado, ejecutar desde el directorio raíz del proyecto `TPLabDist`.

### 🔹 Compilación

```
javac -cp "lib/*" Avanzado/*.java
```

---

### 🔹 Ejecución en Windows (en terminales separadas)

```
java -cp "lib/;." Avanzado.ServerCentral
java -cp "lib/;." Avanzado.ServerClima
java -cp "lib/;." Avanzado.ServerHoroscopo
java -cp "lib/;." Avanzado.Cliente
```

---

### 🔹 Ejecución en Linux / macOS (en terminales separadas)

```
java -cp "lib/*:." Avanzado.ServerCentral
java -cp "lib/*:." Avanzado.ServerClima
java -cp "lib/*:." Avanzado.ServerHoroscopo
java -cp "lib/*:." Avanzado.Cliente
```

---

## Uso del sistema

1. Ejecutar todos los servidores antes de iniciar el cliente.
2. Ingresar:
   - Un **signo zodiacal**
   - Una **fecha en formato `dd/mm/aaaa`**
3. El sistema devolverá una respuesta combinada con:
   - Horóscopo
   - Clima

---

## Finalización

- Escribir `exit` en el cliente para finalizar la ejecución.
- O utilizar `Ctrl + C` en cualquier terminal.
