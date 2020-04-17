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
    console.debug('Document Load and Ready');
    obtenerDatosRest(select.value, buscador.value);
    initGallery();
}

function initGallery() {
    selectAvatar("initGallery")
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
        selectAvatar("id == null");
        evento.target.classList.add('selected');
        document.getElementById('avatar').value = evento.target.dataset.nombre;
    } else {
        selectAvatar("id != null");
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
    let personasSeleccionada = personas.filter(persona => persona.id == indice)[0];

    if (confirm(`¿Estas seguro que quieres eliminar a ${personasSeleccionada.nombre} ?`)) {
        const url = urlBase + indice;
        const promesa = ajax('DELETE', url, null);
        promesa.then(() => {
            console.debug("promesa then");
            obtenerDatosRest('T', "")
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
    });

    promesa.catch((error) => {
        console.debug("promesa catch");
        alert(error);
    })
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
}

function filtarPorSexo(sexo, lista) {
    return lista.filter(persona => persona.sexo == sexo);
}

function buscar(indicionombre, lista) {
    //usar startsWith si quires empieze por y no que contenga
    return lista.filter(persona => persona.nombre.toLowerCase().includes(indicionombre.toLowerCase()));
}

function obtenerDatosRest(listasexo, buscador) {
    console.debug("obtenerDatosRest");
    const url = urlBase;

    const promesa = ajax('GET', url, null);

    promesa.then((personas) => {
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
                    personas = JSON.parse(this.responseText);
                    resolve(personas);
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