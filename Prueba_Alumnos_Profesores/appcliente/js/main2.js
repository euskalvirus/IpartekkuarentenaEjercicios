console.info('esto lo puedes ver depurando');
console.debug('es una traza de debug');
let nombre = 'asdasd';
console.trace(`Esto suele ser para tracear o decir ${nombre}`);
console.warn('mensaje de warking');
console.error('mensaje de error')

let lista = document.getElementById('lista');

const personas = [{
            "avatar": "img/avatar1.png",
            "nombre": "Mary",
            "sexo": "F"
        },
        {
            "avatar": "img/avatar2.png",
            "nombre": "Jenny",
            "sexo": "F"
        }, {
            "avatar": "img/avatar3.png",
            "nombre": "Jhon",
            "sexo": "M"
        }, {
            "avatar": "img/avatar4.png",
            "nombre": "Arthur",
            "sexo": "M"
        },
        {
            "avatar": "img/avatar5.png",
            "nombre": "Stephany",
            "sexo": "F"
        }, {
            "avatar": "img/avatar6.png",
            "nombre": "Naruto",
            "sexo": "M"
        }, {
            "avatar": "img/avatar7.png",
            "nombre": "Girama",
            "sexo": "M"
        }
    ]
    /*lista.innerHTML = '';
    for (let i = 0; i < personas.length; i++) {
        lista.innerHTML += `<li class="list-group-item "><img src=${personas[i].avatar} " alt="imagen avatar ">${personas[i].nombre} </li>`
    }

    //Segunda forma de hacer la iteración
    lista.innerHTML = '';
    personas.forEach(persona => {
        lista.innerHTML += `<li class="list-group-item "><img src=${persona.avatar} " alt="imagen avatar ">${persona.nombre} </li>`
    });*/

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

    //Creamos un objeto para realizar la REQUEST con Ajax
    var xhttp = new XMLHttpRequest();

    //CUIDADO este motodo funciona de forma asincrona
    //debemos meter nuestro codigo aqui dentro apra trabajar con los datos enteros
    xhttp.onreadystatechange = function() {
        //Recibimos la RESPONSE
        if (this.readyState == 4 && this.status == 200) {
            console.info('Peticion GET ' + url);
            console.debug(this.responseText);

            //Parsear el texto a Json
            const jsonData = JSON.parse(this.responseText);

            let listafiltrada = [];
            switch (listasexo.toUpperCase()) {
                case "M":
                    listafiltrada = filtarPorSexo("M", jsonData);
                    break;
                case "F":
                    listafiltrada = filtarPorSexo("F", jsonData);
                    break;
                case "T":
                    listafiltrada = jsonData;
                    break;
                default:
                    break;
            }

            if (buscador !== "") {
                listafiltrada = buscar(buscador, listafiltrada);
            }

            pintar(listafiltrada);
        } // this.readyState == 4 && this.status == 200

    }; // onreadystatechange

    //preparamos la peticion GET
    xhttp.open("GET", url, true);
    //enviar la peticion asincrona, meter el codigo en overreadystatechange
    xhttp.send();
}