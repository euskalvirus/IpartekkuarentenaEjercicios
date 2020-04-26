# IpartekkuarentenaEjercicios

En este projecto se anidan dos subprojectos, AppRest y AppCliente. Mediante el AppRest trabajaremos la parte del servidor web donde se haran todas las llamadas Rest (Get, Post, Put y Delete). Y mediante AppCliente trabajaremos la parte visual, es decir la pagina web.

## Imagen

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

* En el servidor se usa el lenguaje Java (JAX-RS para el servicio RESTful). 
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

El Dump de la base de datos se encuentra en [Prueba_Alumnos_Profesores\appRest\Dump_Alumnos.sql](https://github.com/euskalvirus/IpartekkuarentenaEjercicios/blob/master/Prueba_Alumnos_Profesores/appRest/Dump_Alumnos.sql)

### Detalle API rest con llamadas 

Se obtendra un Objeto de tipo Error, siempre que la respuesta no sea un 200 o 201.

~~~ 
http://{Dominio}/apprest/api/personas 
~~~

**GET**

Se obtendra una lista de tod@s l@s alumn@s.

- Código 200: si va bien. Se obtiene un ArrayList de tipo Persona.
	
- Código 500: Error de servidor.

Se obtiene como respuesta un array de objetos persona.

**POST**

Se enviara un objeto de tipo alumno, se creara si cumple las validacion y devolvera el usuario creado, sino devolvera un error.

- Código 201: Si todo va bien. Se obtiene un Objeto de tipo Persona con el ID generado.

- Código 400: Si los datos enviados no son correctos.

- Código 409: Si existe algun conflicto, por ejemplo nombre repetido.

- Código 500: Error de servidor.

Se envia envian las variables id (Vacío), nombre, avatar y sexo y se recibe un objeto tipo persona pero sin lista de cursos.

~~~ 
http://{Dominio}/apprest/api/personas/{idpersona}
~~~

**GET**

Se obtendra el alumno de id especificado, sino mandara un error.

- Código 200: Si todo va bien. Se obtiene un Objeto de tipo Persona.

- Código 404: Si no se encuentra el alumno.

- Código 500: Error de servidor.

Se obtiene como respuesta un objetos de tipo persona.

**PUT**

Se enviara un objeto de tipo alumno y se modificara el alumno del id especificado si los ids concuerdan y devolvera el usuario modificado, sino devolvera un error.

- Código 201: Si todo va bien. Se obtiene un Objeto de tipo Persona actualizado.

- Código 400: Datos incorrectos.

- Código 404: Si no se encuentra el alumno a actualizar.

- Código 500: Error de servidor.



**DELETE**

Se eliminara el alumno de id especificado y se devolvera el alumno, sino devolvera un error.

- Código 200: Si todo va bien. Se obtiene un Objeto de tipo Persona.

- Código 404: Si no se encuentra el alumno a eliminar.

- Código 409: Si existe un clonflicto, por ejemplo que el alumno tenga cursos asociados.

~~~ 
http://{Dominio}/apprest/api/personas/{idpersona}/cursos 
~~~

**GET**

Se obtendra una Array con 2 listas. Una lista con los cursos comprados y otra con los cursos disponibles para ese alumno.

- Código 200: Si todo va bien. Se obtiene un ArrayList de Cursos.

- Código 404: Si no se encuentra el alumno.

- Código 500: Error de servidor.

Se obtendra un objeto con 2 arraylist:

	{'cursosIn' : [{Objeto Curso,...}],
	'cursosNotIn' : [{Objeto Curso,...}]}

~~~
http://{Dominio}/apprest/api/personas/{idpersona}/cursos/{idCurso}
~~~

**POST**

Se creara una nueva compra para el usuario con referencia a idPersona relacionada con el curso con referencia a idCurso. Si ya existe la relación se devolvera un error y si todo va bien se devovlera al relacion personacurso.

- Código 200: Si todo va bien. Se devuelve un objeto PersonaCurso.

- Código 404: Si no se encuentra el alumno, el curso o los 2.

- Código 409: Si existe un clonflicto, por ejemplo que la relación alumno y curso ya exista.

**DELETE**

Se eliminara la compra para el usuario con referencia a idPersona relacionada con el curso con referencia a idCurso. Si se elimina correctamente se devuelve la relacion personacurso y sino existe tal relación se madara un error.

- Código 200: Si todo va bien. Se devuelve un objeto PersonaCurso.

- Código 404: Si no se encuentra la relación alumno y curso a eliminar.

- Código 409: Si existe un clonflicto.

~~~ 
http://{Dominio}/apprest/api/cursos/
~~~

**GET**

Se obtendra una lista con todos los cursos existentes.

- Código 200: Si todo va bien. Se obtiene un ArrayList de Cursos

- Código 500: Error de servidor.

Estructura de los objetos:

- Objeto Error:

~~~ 
	{'error': string}
~~~ 

 - Objeto Persona:

~~~ 
 	{'id': int,
	'nombre' : string,
	'avatar' : string.png,
	'sexo' : string (M -> mujer, H -> Hombre),
	'rol' : Objeto Rol,
	'cursos': [{Cursos1,Curso2...}]}
~~~ 

- Objeto Curso:

~~~ 
	{'id' : int,
	'nombre' : string,
	'imagen' : string,
	'precio' : double,
	profesor : Objeto Persona} 
~~~ 
	
- Objeto PersonaCurso:

~~~ 
	{'persona' : Objeto Persona,
	'curso' : Objeto Curso,
	'precio' : double}
~~~ 

## Tags o Versiones

### V1.0

En esta versión se cumple la funcionalidad del CRUD para alumno.(Crear, Ver, Editar y Eliminar)

### V2.1

En esta versión se pueden comprar y eliminar cursos para el alumno. Se vera que al seleccionar el alumno a modificar se cargaran las lista para sus cursos y para los cursos que puede comprar.

### V2.2

En esta versión se ha añadido un botón con el que activaremos un modal en donde podremos comprar cursos para el alumno. Ademas se han agregado animaciones para cuando se añaden y se eliminan cursos  de las lista, bien cuando se hace en las listas directamente, bien cuando se hace en el modal.

### V2.3

Cambio en el diseño de la página web.
