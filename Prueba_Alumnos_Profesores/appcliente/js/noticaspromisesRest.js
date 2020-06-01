let noticas = [];

const urlBase = "http://localhost:8080/apprest/api/noticias";

window.addEventListener('load', init());

function init() {



    cargarNoticias();

}


function cargarNoticias() {
    let promesa = ajax('GET', urlBase, null);

    let noticiasDiv = document.getElementById("noticias");

    noticiasDiv.innerHTML = "";

    promesa.then(noticias => {

        noticias.forEach(el => {
            let fom = new Date(el.fecha).toISOString().slice(0, 19).replace('T', ', ');
            console.log(new Date(el.fecha).toISOString().slice(0, 19).replace('T', ''))

            noticiasDiv.innerHTML += `<div id="${el.id}" name="card${el.id}" class="card col-8 col-md-5" style="width: 18rem;">
            <img src="https://i.picsum.photos/id/${el.id}/300/200.jpg" class="card-img-top" alt="imagen card ${el.id}" style="height: 10rem;">
            <div class="card-body">
              <h5 class="card-title">${el.titulo} <h6>Created at: ${fom}<h6></h5>
              <p class="card-text">${el.contenido}</p>
              <a href="#" class="btn btn-primary">Go somewhere</a>
            </div>
          </div>`;

        });

    }).catch(error => {
        alert(error)
    });
};

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