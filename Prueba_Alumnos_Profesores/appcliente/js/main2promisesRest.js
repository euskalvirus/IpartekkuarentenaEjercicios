"use strict"
let lista = document.getElementById('lista');
const urlBase = "http://localhost:8080/apprest/api/personas/";

let personas = [];
window.addEventListener('load', init());

function init() {

    let select = document.getElementById('despegable');

    //TODO llamada Ajax apra servicio REST

    select.addEventListener('change', function() {
        let valor = this.value.toUpperCase();
        let buscador = document.getElementById('search').value;

        obtenerDatosRest(valor, buscador);
    })

    let buscador = document.getElementById('search');

    buscador.addEventListener('keyup', function() {
        let valor = this.value;
        let listasexo = document.getElementById('despegable').value;

        obtenerDatosRest(listasexo, valor);
    })
    console.debug('Document Load and Ready');
    obtenerDatosRest(select.value, buscador.value);
    initGallery();
}

function initGallery() {
    let divImagenes = document.getElementById('imagenes');
    divImagenes.innerHTML = "";
    for (let i = 1; i <= 7; i++) {
        divImagenes.innerHTML += `<img onclick="selectAvatar(event,null)" src="img/avatar${i}.png" alt="avatar${i}.png" id="avatar${i}.png" data-nombre="avatar${i}.png">`;
    }
}

function selectAvatar(evento, id) {
    const avatares = document.querySelectorAll('#imagenes img');
    avatares.forEach(el => el.classList.remove('selected'));
    if (id == null) {
        evento.target.classList.add('selected');
        document.getElementById('avatar').value = evento.target.dataset.nombre;
    } else {
        let a = document.getElementById(id);
        a.classList.add('selected');
    }
}

function pintar(arraypersonas) {
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
    let sexo = document.getElementById('sexo').value;

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
    promesa.then(pers => obtenerDatosRest('T', ""));

    promesa.catch((error) => {
        alert(error);
    })
}

function validate(id, nombre, avatar, sexo, op) {
    if (op === "NUEVO ALUMNO") { id = "1000"; }
    if (id !== "" && nombre !== "" && avatar !== "" && (sexo === 'F' || sexo === 'M')) {
        return true;
    } else {
        return false;
    }
}

function eliminar(indice) {
    console.debug(JSON.stringify(personas));
    let personasSeleccionada = personas.filter(persona => persona.id == indice)[0];
    console.debug(`Click eliminar, persona seleccionada %o`, personasSeleccionada);
    if (confirm(`¿Estas seguro que quieres eliminar a ${personasSeleccionada.nombre} ?`)) {
        const url = urlBase + indice;
        const promesa = ajax('DELETE', url, null);
        promesa.then(pers => { obtenerDatosRest('T', "") });
        promesa.catch((error) => {
            alert(error);
        })
    };

}

function seleccionar(indice) {
    document.getElementById('opcion').name = "modificar";
    document.getElementById('opcion').textContent = "MODIFICAR ALUMNO"
    const url = urlBase + indice
    let tempPersonas = personas;
    const promesa = ajax('GET', url, null);

    promesa.then(persona => {
        document.getElementById('id').value = persona.id;
        document.getElementById('nombre').value = persona.nombre;
        document.getElementById('avatar').value = persona.avatar;
        document.getElementById('sexo').value = persona.sexo;
        selectAvatar(null, persona.avatar);
        personas = tempPersonas;
    });
}

function limpiarFormulario() {
    document.getElementById('opcion').name = "nuevo";
    document.getElementById('opcion').textContent = "NUEVO ALUMNO"
    document.getElementById('id').value = "";
    document.getElementById('nombre').value = "";
    document.getElementById('avatar').value = "";
    document.getElementById('sexo').value = "";

    const avatares = document.querySelectorAll('#imagenes img');
    avatares.forEach(el => el.classList.remove('selected'));
}

function filtarPorSexo(sexo, lista) {
    return lista.filter(persona => persona.sexo == sexo);
}

function buscar(indicionombre, lista) {
    //usar includes si quires que contenga y no empieze
    return lista.filter(persona => persona.nombre.toLowerCase().startsWith(indicionombre.toLowerCase()));
}

function obtenerDatosRest(listasexo, buscador) {
    const url = urlBase;

    const promesa = ajax('GET', url, null);

    promesa.then((personas) => {
        let listafiltrada = [];
        switch (listasexo.toUpperCase()) {
            case "M":
                listafiltrada = filtarPorSexo("M", personas);
                break;
            case "F":
                listafiltrada = filtarPorSexo("F", personas);
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
    return new Promise((resolve, reject) => {
        //Creamos un objeto para realizar la REQUEST con Ajax
        var xhttp = new XMLHttpRequest();

        //CUIDADO este motodo funciona de forma asincrona
        //debemos meter nuestro codigo aqui dentro apra trabajar con los datos enteros
        xhttp.onreadystatechange = function() {
            //Recibimos la RESPONSE
            if (this.readyState == 4) {
                if (this.status == 200 || this.status == 201) {
                    personas = JSON.parse(this.responseText);
                    resolve(personas);
                } // this.status == 200
                else {
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
                console.debug(`Metodo ${metodo} ${url} ${pers}`);
                xhttp.setRequestHeader("Content-type", "application/json");
                xhttp.send(pers);
                console.debug(`Enviados datos`);
                break;

        }

    });
}