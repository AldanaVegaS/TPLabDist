# 🧿 Servidor de Horóscopo

Por ahora, el proyecto permite probar únicamente el **servidor de horóscopo**.

---

## Cómo ejecutar el proyecto

### 1. Compilar las clases Java

Abrir una terminal en el directorio del proyecto y ejecutar:

```
javac ServidorCentral.java
javac ServidorHoroscopo.java
javac Cliente.java
```

---

### 2. Ejecutar el servidor central

```
java Servidor
```

---

### 3. Ejecutar el servidor de horóscopo

Abrir otra terminal en el mismo directorio y ejecutar:

```
java ServidorHoroscopo
```

---

### 4. Ejecutar el cliente

Abrir una tercera terminal en el mismo directorio y ejecutar:

```
java Cliente
```

---

## Uso

* Ingresar texto y presionar **Enter**.
* Se pueden ingresar cualquiera de los signos del horóscopo.
* Si se ingresa un valor inválido, se mostrará un mensaje de error.

---

## Finalizar ejecución

* Escribir `exit` para cerrar el cliente
* O usar `Ctrl + C`
