"use strict"
let lista = document.getElementById('lista');
const urlBase = "http://localhost:8080/apprest/api/personas/";

let personas = [];
window.addEventListener('load', init());

/**
 * Función que se ejecuta al cargar la pagina
 */
function init() {

    let select = document.getElementById('despegable');

    //TODO llamada Ajax apra servicio REST

    select.addEventListener('change', function() {
        console.debug("select EventListener");
        let valor = this.value.toUpperCase();
        let buscador = document.getElementById('search').value;

        obtenerDatosRest(valor, buscador);
    })

    let buscador = document.getElementById('search');

    buscador.addEventListener('keyup', function() {
        console.debug("buscador EventListener");
        let valor = this.value;
        let listasexo = document.getElementById('despegable').value;

        obtenerDatosRest(listasexo, valor);
    })

    let nombre = document.getElementById('nombre');

    nombre.addEventListener('keyup', function() {
        console.debug("buscador EventListener");
        let valor = this.value;
        let listasexo = document.getElementById('despegable').value;
        let url = urlBase + "?filtro=" + valor;
        if (this.name != valor) {
            const promesa = ajax('GET', url, null);

            promesa.then(() => {
                nombre.classList.add('invalid');
                nombre.classList.remove('valid')
            }).catch(() => {
                nombre.classList.add('valid');
                nombre.classList.remove('invalid')
            })
        } else {
            nombre.classList.add('valid');
            nombre.classList.remove('invalid')
        }


    })

    let buscadorModal = document.getElementById('searchModal');

    buscadorModal.addEventListener('keyup', function(e) {
        console.debug("buscadorModal EventListener");

        let previous = document.getElementById("previous");
        let keyCode = e.keyCode;
        let valor = this.value;
        console.debug(this.value);
        if (valor.length > 2) {
            previous.value = valor.length;
            cargarCursosModal(event, "?filtro=" + valor);
        } else if ((valor.length == 2 && (keyCode == 8 || e.ctrlKey)) || (valor.length < 3 && previous.value > 2)) {
            previous.value = valor.length;
            console.debug("longitud: " + valor.length + ", keycode: " + keyCode)
            cargarCursosModal(event, "");
        }
    })


    obtenerDatosRest(select.value, buscador.value, true);
    initGallery();

}

/**
 * Carga la lista de Avatares del formulario
 */
function initGallery() {
    console.debug("initGallery")
    let divImagenes = document.getElementById('imagenes');
    divImagenes.innerHTML = "";
    for (let i = 1; i <= 7; i++) {
        divImagenes.innerHTML += `<img onclick="selectAvatar(event,null)" src="img/avatar${i}.png" alt="avatar${i}.png" id="avatar${i}.png" data-nombre="avatar${i}.png">`;
    }
}


/**
 * Se selecciona el avatar y se cargar el valir en el input con id 'Avatar'.
 * @param {*} evento 
 * @param {*} id 
 */
function selectAvatar(evento, id) {
    console.debug("selectAvatar");
    const avatares = document.querySelectorAll('#imagenes img');
    avatares.forEach(el => el.classList.remove('selected'));
    if (id == null) {
        console.debug("id == null");
        evento.target.classList.add('selected');
        document.getElementById('avatar').value = evento.target.dataset.nombre;
    } else {
        console.debug("id != null");
        let a = document.getElementById(id);
        a.classList.add('selected');
    }
}

/**
 * Se pinta la lista de los profesores
 * @param {*} arraypersonas 
 * @param {*} limpiar Boolean que determina si se limpiar el formulario o no
 */
function pintar(arraypersonas, limpiar) {
    console.debug('pintar');
    personas = arraypersonas;
    if (limpiar) {
        limpiarFormulario();
    }
    lista.innerHTML = '';
    for (let i = 0; i < arraypersonas.length; i++) {
        lista.innerHTML += `<li class="list-group-item"><p class="row">Cursos: ${arraypersonas[i].cursos.length}</p><div class="row persona"><div><img src="img/${arraypersonas[i].avatar}" alt="imagen avatar ">${arraypersonas[i].nombre}</div> 
            <div class="zoom"><i class="fa fa-pen" onclick="seleccionar(${arraypersonas[i].id})"></i> 
            <i class="fa fa-trash" onclick="eliminar(${arraypersonas[i].id})"></i></div></div></li>`
    }
}

/**
 * Se crea un nuevo rofesor o actualiza el profesor con los datos del formulario
 */
function guardar() {
    console.debug('dentro de guardar');
    let id = document.getElementById('id').value;
    let nombre = document.getElementById('nombre').value;
    let avatar = document.getElementById('avatar').value;
    let sexo = (document.getElementById('radioHombre').checked ? "H" : "M");

    let tempPersonas = personas;

    let op = document.getElementById('opcion').textContent;
    console.debug(document.getElementById('opcion'));
    let url = "";
    let promesa = {};
    if (validate(id, nombre, avatar, sexo, op)) {
        let persona = {
            "id": id,
            "nombre": nombre,
            "avatar": `${avatar}`,
            "sexo": sexo,
            "rol": { "id": 2, "nombre": 'profesor' }
        }



        switch (op) {
            case "MODIFICAR PROFESOR":
                console.debug("MODIFICAR PROFESOR");
                url = urlBase + id;
                promesa = ajax('PUT', url, persona);
                break;
            case "NUEVO PROFESOR":
                console.debug("NUEVO PROFESOR");
                url = urlBase;
                promesa = ajax('POST', url, persona);
                break;
            default:
                console.debug("default");
                break;
        }
    } else {
        if (document.getElementById('nombre').classList.contains('invalid')) {
            alert("El nombre no esta disponible");
        } else {
            alert("no has rellenado todos los campos");
        }
    }
    promesa.then(() => {
        if (op == "NUEVO ALUMNO") {
            document.getElementById('despegable').value = 't';
            document.getElementById('search').value = "";
        }
        let sex = document.getElementById('despegable').value;
        let filt = document.getElementById('search').value;
        obtenerDatosRest(sex.toUpperCase(), filt, true);
    });

    promesa.catch((error) => {
        console.debug("promesa catch");
        alert(error);
    })
}

/**
 * Valida que todos los datos necesarios para un nuevo profesor han sido introducidos
 * @param {*} id 
 * @param {*} nombre 
 * @param {*} avatar 
 * @param {*} sexo 
 * @param {*} op 
 */
function validate(id, nombre, avatar, sexo, op) {
    console.debug("validate");
    if (op === "NUEVO PROFESOR") { id = "1000"; }
    if (id !== "" && nombre !== "" && avatar !== "" && (sexo === 'M' || sexo === 'H')) {
        return true;
    } else {
        return false;
    }
}

/**
 * Se elimina el Profesor seleccionado
 * @param {*} indice 
 */
function eliminar(indice) {
    console.debug(`Eliminar`);
    console.debug(personas);
    let personasSeleccionada = personas.filter(persona => persona.id == indice)[0];
    // let personasSeleccionada = personas.find(persona => persona.id == indice);

    if (confirm(`¿Estas seguro que quieres eliminar a ${personasSeleccionada.nombre} ?`)) {
        const url = urlBase + indice;
        const promesa = ajax('DELETE', url, null);
        promesa.then(() => {
            console.debug("promesa then");
            obtenerDatosRest(document.getElementById('despegable').value, document.getElementById('search').value)
        });
        promesa.catch((error) => {
            console.debug("promesa catch");
            alert(error);
        })
    };

}

/**
 * Se cargan los datos del Profesor seleccionado en el formulario y se hace visible la seccion de cursos
 * @param {*} indice 
 */
function seleccionar(indice) {
    console.debug(`seleccionar(${indice})`);
    document.getElementById('opcion').name = "modificar";
    document.getElementById('opcion').textContent = "MODIFICAR PROFESOR"
    const url = urlBase + "profesor/" + indice
    let tempPersonas = personas;
    const promesa = ajax('GET', url, null);

    promesa.then(persona => {
        console.debug("promesa then");
        document.getElementById('opcion').name = "modificar";
        document.getElementById('opcion').textContent = "MODIFICAR PROFESOR"
        document.getElementById('id').value = persona.id;

        let name = document.getElementById('nombre');
        name.classList.remove('valid');
        name.classList.remove('invalid');
        name.value = persona.nombre;
        name.name = persona.nombre;

        document.getElementById('avatar').value = persona.avatar;
        (persona.sexo == "H") ? document.getElementById('radioHombre').checked = true: document.getElementById('radioMujer').checked = true;
        selectAvatar(null, persona.avatar);
        personas = tempPersonas;
        console.debug(persona.id);

        let activos = document.getElementById("cursosactivos");
        activos.innerHTML = "";
        console.debug("Persona cursos: " + JSON.stringify(persona));
        persona.cursos.forEach(curso => {
            activos.innerHTML += `<div><img onclick="selectActivo(event,null)" id="${curso.id}" src="img/${curso.imagen}" alt="${curso.nombre}" title="${curso.nombre}"></div>`
        })

        obtenerTodosLosCursos();
        let bModal = document.getElementById("bMostrarModal");
        bModal.disabled = false;
        document.getElementById("cursos").removeAttribute("hidden");
    }).catch((error) => {
        console.debug("promesa catch");
        alert(error);
    });
}

/**
 * Se limpian todos los datos del formulario y se hace invisible la sección de cursos
 */
function limpiarFormulario() {
    console.debug("limpiarFormulario");
    document.getElementById('opcion').name = "nuevo";
    document.getElementById('opcion').textContent = "NUEVO PROFESOR"
    document.getElementById('id').value = "";

    let name = document.getElementById('nombre');
    name.classList.remove('valid');
    name.classList.remove('invalid');
    name.value = "";
    name.name = "";
    document.getElementById('avatar').value = "";
    document.getElementById('radioHombre').checked = true;

    const avatares = document.querySelectorAll('#imagenes img');
    avatares.forEach(el => el.classList.remove('selected'));
    let divCursos = document.getElementById("cursos");
    divCursos.disabled = true;

    document.getElementById("cursos").hidden = true;

    //obtenerTodosLosCursos();
    let bDel = document.getElementById("del");
    bDel.disabled = true;
    let bAdd = document.getElementById("add");
    bAdd.disabled = true;
    let bModal = document.getElementById("bMostrarModal");
    bModal.disabled = true;
}

/**
 * Se obtienen todos los cursos existentes
 */
function obtenerTodosLosCursos() {
    console.debug("obtenerTodosLosCursos")
    const url = "http://localhost:8080/apprest/api/cursos/profesor";

    const promesa = ajax('GET', url, null);

    promesa.then(cursos => {
        console.debug("dentro then");
        console.debug(JSON.stringify(cursos));
        let inactivos = document.getElementById("cursosinactivos");
        inactivos.innerHTML = "";

        cursos.forEach(curso => {
            inactivos.innerHTML += `<div><img onclick="selectInactivo(event,null)" id="${curso.id}" src="img/${curso.imagen}" alt="${curso.nombre}" title="${curso.nombre}"></div>`
        })
    });
}

/**
 * Se filtra la lista de Profesores segun el sexo
 * @param {*} sexo 
 * @param {*} lista 
 */
function filtarPorSexo(sexo, lista) {
    return lista.filter(persona => persona.sexo == sexo);
}

/**
 * Se activa al seleccionar un curso que el profesor no tiene comprado y da la opcion de comprarlo
 * @param {*} evento 
 * @param {*} id 
 */
function selectInactivo(evento, id) {
    console.debug("selectInactivo");
    const cursosIna = document.querySelectorAll('#cursosinactivos img');
    cursosIna.forEach(el => el.classList.remove('selected'));

    const cursosAc = document.querySelectorAll('#cursosactivos img');
    cursosAc.forEach(el => el.classList.remove('selected'));
    if (id == null) {
        console.debug("id == null");
        evento.target.classList.add('selected');
    } else {
        console.debug("id != null");
        let a = document.getElementById(id);
        a.classList.add('selected');
    }

    let bDel = document.getElementById("del");
    bDel.disabled = true;
    let bAdd = document.getElementById("add");
    bAdd.disabled = false;
}

/**
 * Se activa al seleccionar un curso que el profesor tiene comprado y da la opcion de eliminarlo
 * @param {*} evento 
 * @param {*} id 
 */
function selectActivo(evento, id) {
    console.debug("selectInactivo");
    const cursosIna = document.querySelectorAll('#cursosinactivos img');
    cursosIna.forEach(el => el.classList.remove('selected'));

    const cursosAc = document.querySelectorAll('#cursosactivos img');
    cursosAc.forEach(el => el.classList.remove('selected'));
    if (id == null) {
        console.debug("id == null");
        evento.target.classList.add('selected');
    } else {
        console.debug("id != null");
        let a = document.getElementById(id);
        a.classList.add('selected');
    }
    let bDel = document.getElementById("del");
    bDel.disabled = false;
    let bAdd = document.getElementById("add");
    bAdd.disabled = true;
}

/**
 * Se hace visible el modal de comprar cursos para el Profesor
 * @param {*} evento 
 * @param {*} id 
 */
function selectCursoModal(evento, id) {
    console.debug("selectCursoModal");
    const cursosModal = document.querySelectorAll('#cursosModal img');
    cursosModal.forEach(el => el.classList.remove('selected'));

    if (id == null) {
        console.debug("id == null");
        evento.target.classList.add('selected');
    } else {
        console.debug("id != null");
        let a = document.getElementById(id);
        a.classList.add('selected');
    }

    let bAdd = document.getElementById("bAgregarCursoModal");
    bAdd.disabled = false;

}

/**
 * Se filtra la lista buscando que el nombre del profesor contenga el texto introducido
 * @param {*} indicionombre 
 * @param {*} lista 
 */
function buscar(indicionombre, lista) {
    //usar startsWith si quires empieze por y no que contenga
    return lista.filter(persona => persona.nombre.toLowerCase().includes(indicionombre.toLowerCase()));
}

/**
 * Es la funcion que se usa para buscar los usuarios segun los filtros de sexo o texto contenido en el nombre
 * @param {*} listasexo 
 * @param {*} buscador 
 * @param {*} limpiar 
 */
function obtenerDatosRest(listasexo, buscador, limpiar) {
    console.debug("obtenerDatosRest");
    const url = urlBase + "?rol=" + 2;

    const promesa = ajax('GET', url, null);

    promesa.then((pers) => {
        personas = pers;
        console.debug("promesa then");
        let listafiltrada = [];
        switch (listasexo.toUpperCase()) {
            case "H":
                listafiltrada = filtarPorSexo("H", personas);
                break;
            case "M":
                listafiltrada = filtarPorSexo("M", personas);
                break;
            case "T":
                listafiltrada = personas;
                break;
            default:
                break;
        }

        if (buscador !== "") {
            listafiltrada = buscar(buscador, listafiltrada);
        }

        pintar(listafiltrada, limpiar);
    });

    promesa.catch((error) => {
        console.debug("promesa catch");
        alert(error);
    })
}


/**
 * Es la función principal para hacer las llamadas RESTful
 * @param {*} metodo 
 * @param {*} url 
 * @param {*} datos 
 */
function ajax(metodo, url, datos) {
    console.debug("ajax");
    console.debug(metodo + " " + url)
    return new Promise((resolve, reject) => {
        //Creamos un objeto para realizar la REQUEST con Ajax
        var xhttp = new XMLHttpRequest();

        //CUIDADO este motodo funciona de forma asincrona
        //debemos meter nuestro codigo aqui dentro apra trabajar con los datos enteros
        xhttp.onreadystatechange = function() {
            //Recibimos la RESPONSE
            if (this.readyState == 4) {
                if (this.status == 200 || this.status == 201) {
                    console.debug("Llamada ajax GONE OK");
                    resolve(JSON.parse(this.responseText));
                } // this.status == 200
                else {
                    console.debug("Llamada ajax GONE WRONG");
                    reject(Error(JSON.parse(this.responseText)));
                }
            } // this.readyState == 4

        }; // onreadystatechange

        //preparamos la peticion
        xhttp.open(metodo, url, true);
        var undefinded;
        //enviar la peticion asincrona, meter el codigo en overreadystatechange
        switch (datos) {
            case undefinded:
                console.debug("undefinded");
                xhttp.send();
                break;
            default:
                var pers = JSON.stringify(datos);
                console.debug(`Metodo default ${metodo}`);
                xhttp.setRequestHeader("Content-type", "application/json");
                xhttp.send(pers);
                console.debug(`Enviados datos`);
                break;

        }

    });
}

/**
 * Función que compra el curso para el Profesor
 * @param {*} evento 
 */
function agregarCurso(evento) {
    let idPersona = document.getElementById("id").value;
    let idCurso = "";
    const cursosIna = document.querySelectorAll('#cursosinactivos img');

    let curso;
    cursosIna.forEach(el => {
        if (el.classList.contains('selected')) {
            idCurso = el.id;
            curso = el;
        }
    });

    let url = "http://localhost:8080/apprest/api/" + "cursos/" + idCurso + "/profesor/" + idPersona;

    console.debug(curso)
    console.debug(curso.innerHTML)
    console.debug(curso.outerHTML)
    let promesa = ajax('PUT', url, null);

    promesa.then(() => {
        let activos = document.getElementById("cursosactivos");
        curso.classList.remove("selected");
        curso.setAttribute("onclick", "selectActivo(event,null)");
        console.debug(curso);
        //curso.parentNode.removeChild(curso);

        let outer = curso.parentElement.cloneNode(true);
        outer.classList.add('animated', 'bounceInRight');
        curso.parentElement.classList.add('animated', 'bounceOutLeft');

        //2 opciones para eliminar el elemento de la lista
        //curso.parentNode.removeChild(curso);
        //curso.remove();
        setTimeout(function() {
            curso.parentElement.remove();
            activos.innerHTML += outer.outerHTML;
            setTimeout(function() {
                activos.lastChild.classList.remove('animated', 'bounceInRight');
            }, 800);
        }, 500);

        evento.target.disabled = true;

        let select = document.getElementById('despegable');
        let buscador = document.getElementById('search');
        obtenerDatosRest(select.value, buscador.value, false)
    }).catch(error => {
        console.debug("promesa catch");
        alert(error);
    });
}

/**
 * Función que elimina el curso para el Profesor
 * @param {*} evento 
 */
function eliminarCurso(evento) {
    let idPersona = document.getElementById("id").value;
    let idCurso = "";

    const cursosAc = document.querySelectorAll('#cursosactivos img');

    let curso;
    cursosAc.forEach(el => {
        if (el.classList.contains('selected')) {
            idCurso = el.id;
            curso = el;
        }
    });
    let url = "http://localhost:8080/apprest/api/" + "cursos/" + idCurso + "/profesor/" + idPersona;
    let promesa = ajax('DELETE', url, null);

    promesa.then(() => {
        let inactivos = document.getElementById("cursosinactivos");
        curso.classList.remove("selected");
        console.debug(curso);
        curso.setAttribute("onclick", "selectInactivo(event,null)");
        let outer = curso.parentElement.cloneNode(true);
        outer.classList.add('animated', 'bounceInLeft');
        curso.parentNode.classList.add('animated', 'bounceOutRight');

        //2 opciones para eliminar el elemento de la lista
        //curso.parentNode.removeChild(curso);
        //curso.remove();
        setTimeout(function() {
            curso.parentNode.remove();
            inactivos.innerHTML += outer.outerHTML;
            setTimeout(function() {
                inactivos.lastChild.classList.remove('animated', 'bounceInLeft');
            }, 800);


        }, 300);

        evento.target.disabled = true;

        let select = document.getElementById('despegable');
        let buscador = document.getElementById('search');
        obtenerDatosRest(select.value, buscador.value, false)
    }).catch(error => {
        console.debug("promesa catch");
        alert(error);
    });
}

/**
 * Funcion que carga los cursos que el Profesor no tiene comprados en la lista del Modal
 * @param {*} evento 
 * @param {*} filtro 
 */
function cargarCursosModal(evento, filtro) {
    console.debug("dentro de cargarCursosModal");

    let listaModal = document.getElementById("cursosModal");
    listaModal.innerHTML = "";

    const url = "http://localhost:8080/apprest/api/cursos/profesor/" + filtro;

    const promesa = ajax('GET', url, null);

    promesa.then(cursos => {
        let id = document.getElementById('id').value;
        console.debug(id);
        if (id !== undefined) {
            console.debug(personas);
            let pSeleccionada = personas.find(persona => persona.id == id);
            console.debug(pSeleccionada.cursos.length);
            if (pSeleccionada.cursos.length != 0) {
                cursos = cursos.filter(curso => {
                    let busqueda = pSeleccionada.cursos.find(c => c.id == curso.id);
                    if (busqueda == undefined) {
                        return curso;
                    }

                })
            }
        }
        console.log(cursos);
        cursos.forEach(curso => {
            listaModal.innerHTML += `<div><img onclick=selectCursoModal(event,null) id="${curso.id}" src="img/${curso.imagen}" alt="${curso.nombre}, ${curso.precio}€"> Nombre: ${curso.nombre} Precio: ${curso.precio}€</div>`
        })
    }).catch(error => {
        alert(error);
    });
}

/**
 * Acciones que hacer al hacer visible el Modal
 */
$('#modalCursos').on('shown.bs.modal', function(event) {
    cargarCursosModal(event, "");
    let bAdd = document.getElementById("bAgregarCursoModal");
    bAdd.disabled = true;
})

/**
 * Acciones que hacer al hacer invisibel el Modal
 */
$('#modalCursos').on('hidden.bs.modal', function(event) {
    document.getElementById('searchModal').value = "";
    document.getElementById('cursosModal').innerHTML = "";
    let bAdd = document.getElementById("bAgregarCursoModal");
    bAdd.disabled = true;
})

/**
 * Funcion que agrega un curso al profesor desde el Modal
 * @param {*} evento 
 */
function agregarCursoModal(evento) {
    let idPersona = document.getElementById("id").value;
    let idCurso = "";
    const cursosModal = document.querySelectorAll('#cursosModal img');

    let curso;
    let img;
    cursosModal.forEach(el => {
        if (el.classList.contains('selected')) {
            idCurso = el.id;
            img = el;
            curso = img.parentNode;
        }
    });

    let url = "http://localhost:8080/apprest/api/" + "cursos/" + idCurso + "/profesor/" + idPersona;

    let promesa = ajax('PUT', url, null);

    promesa.then((el) => {
        console.debug("then");
        let activos = document.getElementById("cursosactivos");
        img.classList.remove("selected");

        img.parentNode.setAttribute("onclick", "selectActivo(event,null)");
        //activos.innerHTML += curso.outerHTML;
        const inactivos = document.querySelectorAll('#cursosinactivos img');

        let outer = `<div class="animated bounceInRight"><img onclick="selectActivo(event,null)" id="${el.id}" src="img/${el.imagen}" alt="${el.nombre}" title="${el.nombre}"></div>`;

        curso.classList.add('animated', 'bounceOutLeft');

        //2 opciones para eliminar el elemento de la lista
        //curso.parentNode.removeChild(curso);
        //curso.remove();
        setTimeout(function() {
            img.parentElement.remove();
            inactivos.forEach(curso => {
                if (curso.id == idCurso) {
                    curso.parentElement.classList.add('animated', 'bounceOutLeft');
                    setTimeout(function() {
                        curso.parentElement.remove();
                    }, 800);
                }
            });
            setTimeout(function() {
                activos.innerHTML += outer;
                setTimeout(function() {
                    activos.lastChild.classList.remove('animated', 'bounceInRight');
                }, 800);


            }, 300);


        }, 800);

        //$('#modalCursos').modal('hide');

        let select = document.getElementById('despegable');
        let buscador = document.getElementById('search');

        //init();
        obtenerDatosRest(select.value, buscador.value, false)
            //Promise.all(obtenerDatosRest(select.value, buscador.value)).then(seleccionar(idPersona));

    }).catch(error => {
        console.debug("promesa catch");
        alert(error);
    });
}