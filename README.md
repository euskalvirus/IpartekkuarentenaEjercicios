# IpartekkuarentenaEjercicios
## titulo h2
### titulo h3
#### titulo h4
Projecto que incluye dos porjectos, AppRest(rest api con Java Jax-Rs) y AppCliente(HTML5, CSS3, JS)

Esto seria otro parrafo

- elemento1
    - elementohijo1
    - elementohijo2
- elemento2

### configuración base datos
para configurar la bases datos cambiar el fichero **config.xml** 

~~~
    db.name = "root";
    db.pass = "root";
~~~

[Ejemplo de enlace pulsa para ir a el](https://github.com/ipartek/alumnos-profesores)

![Imagen avatar1](https://github.com/euskalvirus/IpartekkuarentenaEjercicios/blob/master/Prueba_Alumnos_Profesores/appcliente/img/avatar1.png)


En este projecto se anidan dos subprojectos, AppRest y AppCliente. Mediante el AppRest trabajaremos la parte del servidor web donde se haran todas las llamadas Rest (Get, Post, Put y Delete). Y mediante AppCliente trabajaremos la parte visual, es decir la pagina web.

**IMAGEN**

## AppClient

### Introducción

En este subprojecto se implementaran los HTML, CSS y JavaScript. El diseño es un SPA(Single Page Application) y RESTful.

### Tecnología usada

* HTML5
* CSS3
* JavaScript

### Configuración

Para la última version el fichero .js ultilizado es main2promisesRest.js, dentro se tendran que modificar la urlBase, que es la ruta principal para la mayoría de llamadas:

    const urlBase = "http://localhost:8080/apprest/api/personas/";
    
La otra constante que hay que modificar es la url para obtener los cursos (que se encuentra en 2 funciones):

~~~
function obtenerTodosLosCursos() {
    ...
    const url = "http://localhost:8080/apprest/api/cursos/";
    ...
}

function cargarCursosModal(evento, filtro) {
    ...
    const url = "http://localhost:8080/apprest/api/cursos/" + filtro;
    ...
}
~~~
    
## AppRest

### Introducción

En este subprojecto se implementara el apartado del servidor, donde se trataran todas las llamadas y se manipularan todos los datos que tengamos en la base de datos.

### Tecnologñia usada

* En el servidor se usa el lenguaje Java. 
* En la parte de la DDBB se usa MySQL.
* Para las dependencia se usa Maven.

### Configuración
