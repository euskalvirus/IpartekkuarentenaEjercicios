"use strict"
let lista = document.getElementById('lista');
const urlBase = "http://localhost:8080/apprest/api/personas/";

let personas = [];
window.addEventListener('load', init());

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

    console.debug('Document Load and Ready');
    obtenerDatosRest(select.value, buscador.value);
    initGallery();
}

function initGallery() {
    console.debug("initGallery")
    let divImagenes = document.getElementById('imagenes');
    divImagenes.innerHTML = "";
    for (let i = 1; i <= 7; i++) {
        divImagenes.innerHTML += `<img onclick="selectAvatar(event,null)" src="img/avatar${i}.png" alt="avatar${i}.png" id="avatar${i}.png" data-nombre="avatar${i}.png">`;
    }
}

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

function pintar(arraypersonas) {
    console.debug('pintar');
    personas = arraypersonas;
    limpiarFormulario();
    lista.innerHTML = '';
    for (let i = 0; i < arraypersonas.length; i++) {
        lista.innerHTML += `<li class="list-group-item"><img src="img/${arraypersonas[i].avatar}" alt="imagen avatar ">${arraypersonas[i].nombre} 
            <i class="fa fa-pen" onclick="seleccionar(${arraypersonas[i].id})"></i> 
            <i class="fa fa-trash" onclick="eliminar(${arraypersonas[i].id})"></i></li>`
    }
}

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
            "sexo": sexo
        }



        switch (op) {
            case "MODIFICAR ALUMNO":
                console.debug("MODIFICAR ALUMNO");
                url = urlBase + id;
                promesa = ajax('PUT', url, persona);
                break;
            case "NUEVO ALUMNO":
                console.debug("NUEVO ALUMNO");
                url = urlBase;
                promesa = ajax('POST', url, persona);
                break;
            default:
                console.debug("default");
                break;
        }
    } else {
        alert("no has rellenado todos los campos");
    }
    promesa.then(() => {
        console.debug("promesa then");
        obtenerDatosRest('T', "")
    });

    promesa.catch((error) => {
        console.debug("promesa catch");
        alert(error);
    })
}

function validate(id, nombre, avatar, sexo, op) {
    console.debug("validate");
    if (op === "NUEVO ALUMNO") { id = "1000"; }
    if (id !== "" && nombre !== "" && avatar !== "" && (sexo === 'M' || sexo === 'H')) {
        return true;
    } else {
        return false;
    }
}

function eliminar(indice) {
    console.debug(`Eliminar`);
    console.debug(personas);
    let personasSeleccionada = personas.filter(persona => persona.id == indice)[0];

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

function seleccionar(indice) {
    console.debug(`seleccionar(${indice})`);
    document.getElementById('opcion').name = "modificar";
    document.getElementById('opcion').textContent = "MODIFICAR ALUMNO"
    const url = urlBase + indice
    let tempPersonas = personas;
    const promesa = ajax('GET', url, null);

    promesa.then(persona => {
        console.debug("promesa then");
        document.getElementById('id').value = persona.id;
        document.getElementById('nombre').value = persona.nombre;
        document.getElementById('avatar').value = persona.avatar;
        (persona.sexo == "H") ? document.getElementById('radioHombre').checked = true: document.getElementById('radioMujer').checked = true;
        selectAvatar(null, persona.avatar);
        personas = tempPersonas;
        console.debug(persona.id);
        obtenerListasCursos(persona.id);
    }).catch((error) => {
        console.debug("promesa catch");
        alert(error);
    });
}

function obtenerListasCursos(idPersona) {
    console.debug("obtenerListasCursos")
    const url = urlBase + idPersona + "/cursos/";

    const promesa = ajax('GET', url, null);

    promesa.then(arrayCursos => {
        console.debug("dentro then");
        let activos = document.getElementById("cursosactivos");
        let inactivos = document.getElementById("cursosinactivos");
        inactivos.innerHTML = "";
        activos.innerHTML = "";
        console.debug(JSON.stringify(arrayCursos));
        arrayCursos.cursosNotIn.forEach(cursos => {
            inactivos.innerHTML += `<img onclick="selectInactivo(event,null)" id="${cursos.id}" src="img/${cursos.imagen}" alt="${cursos.nombre}">`
        })

        arrayCursos.cursosIn.forEach(cursos => {
            activos.innerHTML += `<img onclick="selectActivo(event,null)" id="${cursos.id}" src="img/${cursos.imagen}" alt="${cursos.nombre}">`
        })

        let bDel = document.getElementById("del");
        bDel.disabled = true;
        let bAdd = document.getElementById("add");
        bAdd.disabled = true;
    });
}

function limpiarFormulario() {
    console.debug("limpiarFormulario");
    document.getElementById('opcion').name = "nuevo";
    document.getElementById('opcion').textContent = "NUEVO ALUMNO"
    document.getElementById('id').value = "";
    document.getElementById('nombre').value = "";
    document.getElementById('avatar').value = "";
    document.getElementById('radioHombre').checked = true;

    const avatares = document.querySelectorAll('#imagenes img');
    avatares.forEach(el => el.classList.remove('selected'));
    let divCursos = document.getElementById("cursos");
    divCursos.disabled = true;

    obtenerTodosLosCursos();
    let bDel = document.getElementById("del");
    bDel.disabled = true;
    let bAdd = document.getElementById("add");
    bAdd.disabled = true;
}

function obtenerTodosLosCursos() {
    console.debug("obtenerTodosLosCursos")
    const url = "http://localhost:8080/apprest/api/cursos/";

    const promesa = ajax('GET', url, null);

    promesa.then(cursos => {
        console.debug("dentro then");
        let inactivos = document.getElementById("cursosinactivos");
        let activos = document.getElementById("cursosactivos");
        inactivos.innerHTML = "";
        activos.innerHTML = "";

        cursos.forEach(curso => {
            inactivos.innerHTML += `<img class="disabled" id="${curso.id}" src="img/${curso.imagen}" alt="${curso.nombre}" disabled>`
        })
    });
}

function filtarPorSexo(sexo, lista) {
    return lista.filter(persona => persona.sexo == sexo);
}

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

function buscar(indicionombre, lista) {
    //usar startsWith si quires empieze por y no que contenga
    return lista.filter(persona => persona.nombre.toLowerCase().includes(indicionombre.toLowerCase()));
}

function obtenerDatosRest(listasexo, buscador) {
    console.debug("obtenerDatosRest");
    const url = urlBase;

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

        pintar(listafiltrada);
    });

    promesa.catch((error) => {
        console.debug("promesa catch");
        alert(error);
    })
}

/**
 * 
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

    let url = urlBase + idPersona + "/cursos/" + idCurso;

    console.debug(curso)
    console.debug(curso.innerHTML)
    console.debug(curso.outerHTML)
    let promesa = ajax('POST', url, null);

    promesa.then(() => {
        let activos = document.getElementById("cursosactivos");
        curso.classList.remove("selected");
        curso.setAttribute("onclick", "selectActivo(event,null)");
        console.debug(curso);
        activos.innerHTML += curso.outerHTML;
        curso.parentNode.removeChild(curso);
        evento.target.disabled = true;
    }).catch(error => {
        console.debug("promesa catch");
        alert(error);
    });
}

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
    let url = urlBase + idPersona + "/cursos/" + idCurso;
    let promesa = ajax('DELETE', url, null);

    promesa.then(() => {
        let inactivos = document.getElementById("cursosinactivos");
        curso.classList.remove("selected");
        console.debug(curso);
        curso.setAttribute("onclick", "selectInactivo(event,null)");
        inactivos.innerHTML += curso.outerHTML;
        curso.parentNode.removeChild(curso);
        evento.target.disabled = true;
    }).catch(error => {
        console.debug("promesa catch");
        alert(error);
    });
}

function cargarCursosModal(evento, filtro) {
    console.debug("dentro de cargarCursosModal");
    let listaModal = document.getElementById("cursosModal");
    listaModal.innerHTML = "";

    const url = "http://localhost:8080/apprest/api/cursos/" + filtro;

    const promesa = ajax('GET', url, null);

    promesa.then(cursos => {
        console.log(cursos);
        cursos.forEach(curso => {
            listaModal.innerHTML += `<img class="disabled" id="${curso.id}" src="img/${curso.imagen}" alt="${curso.nombre}" disabled>`
        })
    }).catch(error => {
        alert(error);
    });
}