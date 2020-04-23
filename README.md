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

![Diseño apartado Web](https://github.com/euskalvirus/IpartekkuarentenaEjercicios/blob/master/Prueba_Alumnos_Profesores/Screenshoots/appclient.png)

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

Para configurar el acceso a la base de datos tenemos que acceder al fichero context.xml, que se encuentra en la ruta  appRest\WebContent\META-INF\context.xml.

Dentro tenemos que modificar los parametros name, url, username y pasword: 

~~~
    <?xml version="1.0" encoding="UTF-8"?>
<Context>
	<Resource 
	...
	name="jdbc/personas" 
	...
	url="jdbc:mysql://localhost:3306/alumnos?serverTimezone=Europe/Madrid" 
	username="root"
	password="admin" 
	.../>
   ...
</Context>
~~~

* name: Este parametro solo se modicaria si cada uno lo quiere, no es necesario (En este caso, ***personas***). Se utiliza en la clase ConnectionManager para obtener los datos para hacer la conexion a MySQL.

~~~
	public static Connection getConnection() {
			...
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/personas");
			...
	}
~~~

* url: ***alumnos*** es el nombre de la base de datos que utilizaremos.

* username: El nombre de usuario con el que nos loggearemos en MySQL.

* password: Contraseá del usuario.

### Detalle API rest con llamadas 

~~~ 
http://{Dominio}/apprest/api/personas 
~~~

**GET**

Se obtendra una lista de tod@s l@s alumn@s.

**POST**

Se enviara un objeto de tipo alumno, se creara si cumple las validacion y devolvera el usuario creado, sino devolvera un error.

~~~ 
http://{Dominio}/apprest/api/personas/{idpersona}
~~~

**GET**

Se obtendra el alumno de id especificado, sino mandara un error.

**PUT**

Se enviara un objeto de tipo alumno y se modificara el alumno del id especificado si los ids concuerdan y devolvera el usuario modificado, sino devolvera un error.

**DELETE**

Se eliminara el alumno de id especificado y se devolvera el alumno, sino devolvera un error.

~~~ 
http://{Dominio}/apprest/api/personas/{idpersona}/cursos 
~~~

**GET**

Se obtendra una Array con 2 listas. Una lista con los cursos comprados y otra con los cursos disponibles para ese alumno.

~~~
http://{Dominio}/apprest/api/personas/{idpersona}/cursos/{idCurso}
~~~

**POST**

Se creara una nueva compra para el usuario con referencia a idPersona relacionada con el curso con referencia a idCurso. Si ya existe la relación se devolvera un error y si todo va bien se devovlera al relacion personacurso.

**DELETE**

Se eliminara la compra para el usuario con referencia a idPersona relacionada con el curso con referencia a idCurso. Si se elimina correctamente se devuelve la relacion personacurso y sino existe tal relación se madara un error.

~~~ 
http://{Dominio}/apprest/api/cursos/
~~~

**GET**

Se obtendra una lista con todos los cursos existentes.
