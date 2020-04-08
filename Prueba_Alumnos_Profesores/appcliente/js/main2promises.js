let lista = document.getElementById('lista');

const personas = [];
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
        lista.innerHTML += `<li class="list-group-item "><img src=${arraypersonas[i].avatar} " alt="imagen avatar ">${arraypersonas[i].nombre} </li>`
    }

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
                    resolve(JSON.parse(this.responseText));
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