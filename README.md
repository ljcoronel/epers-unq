# TP 6 - InfluxDB

Luego de que RDJ se metiera por el portal, nosotros lo seguimos, ignorando sus advertencias. Lo que siguió fue difícil de describir, como si fuéramos una única
mente con RDJ y su demonio, nos mostró en lo que se convertiría nuestro mundo en cuestión de poco tiempo: desastres por todos lados, incontables muertes
y sufrimiento, demonios invadiendo ciudades enteras, controlando a gran parte de la población. Grandes puertas donde estos espíritus malignos entraban y
salían constantemente.



<p align="center">
  <img src="sangre.png" />
</p>

Dolor y mucha sangre derramada. Gritos de desesperación nos dejaron sordos. Y luego de un gran flash, volvimos al aula 60.

Al volver, nos dimos cuenta que algo más había ocurrido. El tiempo parecía haberse adelantado. Pero aquel demonio seguía ahí, esperando a nuestro regreso.

Con sus resplandecientes ojos amarillos nos guió, caminando lentamente y sin decir ni una palabra más, hacia la zona más alejada del Auditorio. 
Allí, encontramos a una computadora abandonada encima de una vieja mesa de madera, dejada en un voice chat de Discord abierto.

`#Investigación-de-portales`
`#Antenas`
`#Métricas-y-zonas-peligrosas`

Sus canales de texto. Repletos de información sobre demonios y formas de detectarlos. Ahora, sabíamos que podíamos formar parte de algo mucho más grande.

Decidimos entrar al voice chat.

`"Al fin se conectaron, la alianza RDJ se podrá formar."`

Luego, esos susurros incomprensibles del demonio de RDJ, empezaron a cobrar sentido. Al lado de nuestros oídos, escuchamos:

`Su destino será finalmente cumplido.`

`AlianzaRDJ`

Nuestro destino.

<p align="center">
  <img src="antena.jpg" />
</p>

Gracias al Discord, comprendimos que hay más espíritus de los que creíamos; el mundo está lleno de demonios por todos lados. Descubrimos también que han aprendido pueden 
juntarse varios de ellos, estos pueden abrir portales que atraen a más de ellos desde el inframundo.

Hemos podido crear la Alianza RDJ con otros médiums de alrededor del mundo. Esto nos ha permitido desarrollar potentes tecnologías de detección,
y gracias a ellos pudimos desarrollar sensores de temperatura utilizando las antenas a lo largo del planeta.

Es bien sabido que los demonios bajan las temperaturas de las zonas en las que están circulando.
Los detectores nos proveen de métricas en tiempo real sobre la temperatura de cada ubicación, y con todo el equipo planteamos desarrollar un sistema para analizar estos datos y determinar posiciones de concentración de demonios.
El objetivo de la alianza, entendimos, es evitar que se abran más portales, y poder exorcizar a todos los demonios. Ahora que somos tantos, seremos imparables.

Con esta información, podemos guiar a nuestros médiums a exorcizar las zonas que más lo requieran, y finalizar este combate de una vez por todas.
Podemos acabar con esto.
Así que vamos a ello.

## Cambios a nuestra aplicación

Las ubicaciones ahora deberán registrar su temperatura, la cual cambiará si hay demonios en ella:

- Los espíritus demoníacos bajarán la temperatura en 0.2°C por su presencia en una ubicación.
- Si su nivel de conexión es de 0, se los considera fuera de este mundo, por lo que ya no afectan a la temperatura.

Por otra parte, se podrán crear portales que seguirán las siguientes normas:

- Bajarán la temperatura en 5°C **mientras** estén **abiertos**.
- Eligirán a un demonio como host, que debe no estar dominado ni tener un médium conectado y tener más de 0 de nivel de conexión.
- Tomará el nivel de conexión del host como energía.
- Creará demonios "Minions" en el lugar donde se abra el portal, utilizando 10 de energía por cada uno.
- Cada creación de minions dura exactamente 10 segundos.
- Se pueden crear varios portales a la vez.

También ahora los médiums se podrán reunir para meditar conjuntamente, exorcizando zonas enteras a la lejanía:

- Deberán buscarse mediante su energía espiritual en un rango de 3000kms de radio.
- Eligirán una ubicación a la cual exorcizar a pleno.
- Para ganar el combate, el maná de los médium en conjunto debe superar o ser igual que el nivel de conexión total de los demonios presentes en la zona.
- Los médium, si ganan el combate, deberán perder el 75% del maná total que utilizaron para atacar conjuntamente.
- Si estos fallan, deberá arrojarse una excepción: `throw new ManaInsuficiente()`.
- Todos los demonios libres serán exorcizados, llevando su nivel de conexión a 0 y sacándolos de este mundo.

## Servicios

Deberán extender `UbicacionService`, modificándolo de la siguiente manera:

- `Ubicacion ubicacionMasPeligrosa()` - Recupera la ubicación con la menor temperatura respecto a la inicial, que será la que más demonios posea.

- `Ubicacion ubicacionConMasTransitoDeEspiritus(long segundos)` Recupera la ubicación con mayor cantidad de cambios de temperatura hace la cantidad de segundos dados, que será aquella que sea la más utilizada para moverse.

El service `EspirituService`, por su parte:

- `PortalDemonios crearPortal(PortalDemonios portalDemonios)` - Deberá soportar crear portales que invoquen a espíritus con las reglas ya propuestas, sin estancar al programa:

También se deberá agregar a `MediumService` el siguiente método:

- `void meditar(Long ubicacionIdDemonios, Double latitudMediums, Double longitudMediums);` - Deberá derrotar a los espíritus de la ubicación dada, buscando a médiums en un rango de hasta 3000 kms a la redonda, meditando entre todos para lograrlo, con las reglas ya planteadas.

Por otro lado, el método `mover` e `invocar` de `MediumService` ahora deberán también efectuar los cambios de temperatura correspondientes, dado el caso que mueva consigo a un espíritu.

A su vez, se deberá añadir siguiente método a `EstadísticaService`, modificándolo de la siguiente manera:

- `List<EventoPortalDTO> EventosDePortalesOcurridos()` - Recupera los eventos de portales ocurridos, basándose en la baja extrema de temperatura repentina de las ubicaciones.

El DTO `EventoPortalDTO` es un objeto tal que:
```java
public EventoPortalDTO(Long ubicacionId, Instant tiempo, Double intensidad);
```





## Concurrencia

Para la creación de portales, es importante poder tener varios corriendo al mismo tiempo para poder observar cambios notorios en la temperatura (y para no bloquear al programa entero por mucho tiempo).

Para ello se pide hacer una sencilla implementación de concurrencia:

### Crear threads

Un thread permite ejecución en paralelo. Para poder crear un thread se puede utilizar el siguiente constructor, que recibe una función por parámetro:

```java
Thread thread = new Thread(() -> {});
```

Y luego para correrlo deberán usar el siguiente método:

```java
thread.start();
```
*Importante NO utilizar el método run() en vez de start, ya que esto ejecutará en el thread principal, en vez de crear uno aparte.*

### Sleep

Para frenar la ejecución de un thread momentáneamente se puede utilizar el siguiente método, que recibe tiempo en milisegundos:
```java
Thread.sleep(int ms);
```

*(Es posible que se tenga que utilizar un try catch para evitar problemas con Java).*

### Métodos synchronized

Se pedirá que los métodos de sus repository que modifiquen la temperatura de alguna manera, deban llevar en su firma:

```java
public synchronized void cambiarTemperatura();
```

Esto es para que no se puedan ocurrir al mismo tiempo cambios en la temperatura, evitando romper la C de ACID.

### Se pide:
- Implementar los requerimientos utilizando una base de datos de tipo time-series InfluxDB.
- Persistir las temperaturas de cada ubicación en Influx.
- Extiendan el `UbicacionController`, `EspirituController`, `MediumController`, y el `ReporteController` incluyendo los nuevos métodos implementados.
- Creen test unitarios para cada unidad de código entregada que prueben todas las funcionalidades pedidas, con casos favorables y desfavorables.

## Consideraciones
- Utilicen la documentación oficial del lenguaje Flux para lograr resolver los métodos propuestos.
- No se puede borrar (directamente) de InfluxDB Cloud, tendrán que buscar una alternativa a esto.

## *~~Bonus~~* Grafana

Por el gran volúmen de datos, se nos ha pedido una manera sencilla de visualizar los datos que insertemos en la nueva base.
Las queries eran muy útiles, pero difíciles de leer, lo cual nos hizo precisar de una alternativa más simple.

Se deberá utilizar un dashboard de Grafana para poder ilustrar los cambios en las temperaturas.

### En particular:
- Se pedirá una screenshot (o un video) de la temperatura de una ubicación cayendo fuertemente debido al uso de portales con un gráfico.
- Se pedirá una screenshot (o un video) de un gráfico de tipo heatmap para ver cuáles son las ubicaciones más frías en el momento.

## *~~Bonus 2~~* Sensor

El equipo de desarrolladores ha considerado la idea de instalar un sensor en la UNQ para vigilar la presencia de demonios en ella.

Se deberá utilizar un sensor (o bien, un programa que emule serlo) para poder actualizar la temperatura de la ubicación donde el sensor se encuentre, junto a una screenshot mostrando su funcionamiento ante un medio frío
(En caso que sea virtual, bajando la temperatura por debajo de los 5°C).

 

