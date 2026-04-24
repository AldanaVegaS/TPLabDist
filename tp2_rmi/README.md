# TP2 RMI

Este proyecto implementa una arquitectura distribuida con **tres niveles de complejidad**:

## Niveles del trabajo

### 1) Básico/Medio
Un cliente solicita datos al **servidor central**, que actúa como intermediario y gestiona la obtención de información desde los servicios correspondientes (clima y horóscopo). Además los servidores deben poder atender **múltiples clientes en paralelo**.

### 2) Avanzado
El servidor central incorpora una **caché**: antes de comunicarse con los servidores de clima y horóscopo, consulta si la información ya está disponible localmente.

---

## Ejecución en nivel Básico/Meido

### 1. Compilar

Ubicarse en la carpeta que contiene `TPLabDist` y ejecutar:

```bash
javac -d . $(find TPLabDist/tp2_rmi/basico -name "*.java")
```

### 2. Levantar procesos

Abrir una terminal por cada instancia y ejecutar **en este orden**:

1. Servidor de clima  

```bash
java TPLabDist.tp2_rmi.basico.server_clima.ServerClima
```

2. Servidor de horóscopo  

```bash
java TPLabDist.tp2_rmi.basico.server_horoscopo.ServerHoroscopo
```

3. Servidor central  

```bash
java TPLabDist.tp2_rmi.basico.server_central.ServerCentral
```

4. Cliente  

```bash
java TPLabDist.tp2_rmi.basico.cliente.Cliente
```

## Uso del sistema

1. Ejecutar todos los servidores antes de iniciar el cliente.
2. Ingresar:
   - Un **signo zodiacal**
   - Una **fecha en formato `dd/mm/aaaa`**
3. El sistema devolverá una respuesta combinada con:
   - Horóscopo
   - Clima

## Ejecución simultánea de instancias de cliente

1. Ubicarse en y habilitar script:

```bash
chmod +x TPLabDist/tp2_rmi/basico/instancias_clientes.sh 
```

2. Ejecutar script:

```bash
./TPLabDist/tp2_rmi/basico/instancias_clientes.sh 
```