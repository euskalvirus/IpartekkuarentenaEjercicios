let lista = document.getElementById('lista');

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
}

function pintar(arraypersonas) {

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

    let op = document.getElementById('opcion').textContent;
    console.debug(document.getElementById('opcion'));
    if (validate(id, nombre, avatar, sexo, op)) {
        let persona = {
            "id": id,
            "nombre": nombre,
            "avatar": `${avatar}`,
            "sexo": sexo
        }

        switch (op) {
            case "MODIFICAR ALUMNO":
                let index = personas.findIndex(pers => pers.id == id);
                personas[index] = persona;
                break;
            case "NUEVO ALUMNO":
                let ultimoId = personas.slice(-1)[0].id;
                persona.id = ultimoId + 1;
                personas.push(persona);
                break;
            default:
                break;
        }
        pintar(personas);
        limpiarFormulario();
        //llamar servicio rest
    } else {
        alert("no has rellenado todos los campos");
    }
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
    let personasSeleccionada = personas.filter(persona => persona.id == indice)[0];
    console.debug(`Click eliminar, persona seleccionada %o`, personasSeleccionada);
    if (confirm(`¿Estas seguro que quieres eliminar a ${personasSeleccionada.nombre} ?`)) {
        personas = personas.filter(persona => persona.id != indice);
        limpiarFormulario()
        pintar(personas);
    }
}

function seleccionar(indice) {
    let personasSeleccionada = personas.filter(persona => persona.id == indice)[0];
    document.getElementById('opcion').name = "modificar";
    document.getElementById('opcion').textContent = "MODIFICAR ALUMNO"
    document.getElementById('id').value = personasSeleccionada.id;
    document.getElementById('nombre').value = personasSeleccionada.nombre;
    document.getElementById('avatar').value = personasSeleccionada.avatar;
    document.getElementById('sexo').value = personasSeleccionada.sexo;
}

function limpiarFormulario() {
    document.getElementById('opcion').name = "nuevo";
    document.getElementById('opcion').textContent = "NUEVO ALUMNO"
    document.getElementById('id').value = "";
    document.getElementById('nombre').value = "";
    document.getElementById('avatar').value = "";
    document.getElementById('sexo').value = "";
}

function filtarPorSexo(sexo, lista) {
    return array = lista.filter(persona => persona.sexo == sexo);
}

function buscar(indicionombre, lista) {
    //usar includes si quires que contenga y no empieze
    return lista.filter(persona => persona.nombre.toLowerCase().startsWith(indicionombre.toLowerCase()));
}

function obtenerDatosRest(listasexo, buscador) {
    const url = "http://localhost:8080/apprest/api/personas/";

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
                    reject('Ha habido un error en la obtencion de datos');
                }
            } // this.readyState == 4

        }; // onreadystatechange

        //preparamos la peticion GET
        xhttp.open(metodo, url, true);
        //enviar la peticion asincrona, meter el codigo en overreadystatechange
        xhttp.send();
    });
}