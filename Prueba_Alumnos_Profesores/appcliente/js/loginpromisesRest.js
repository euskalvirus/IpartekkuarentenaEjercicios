let lista = document.getElementById('lista');

window.addEventListener('load', init());

function init() {

    let loginBtn = document.getElementById('loginBtn');

    loginBtn.addEventListener('click', function() {
        let email = document.getElementById('email').value;
        let password = document.getElementById('password').value;

        let login = { 'email': email, 'password': password };

        const url = "http://localhost:8080/apprest/api/login/";

        const promesa = ajax('POST', url, login);

        promesa.then((token) => {
            console.debug("promesa then");
            window.location = "http://127.0.0.1:5500/appcliente/index.html";

        }).catch((error) => {
            console.debug("error promesa");
            console.debug(error);
            alert(error);

        });

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
                    let token = this.responseText;
                    resolve(token);
                } // this.status == 200
                else {
                    console.debug("Llamada ajax GONE WRONG");
                    reject(Error(JSON.parse(this.responseText)));
                }
            } // this.readyState == 4

        }; // onreadystatechange

        //preparamos la peticion GET
        xhttp.open(metodo, url, true);

        var undefinded;
        //enviar la peticion asincrona, meter el codigo en overreadystatechange
        switch (datos) {
            case undefinded:
                console.debug("No has ingresado ningun dato");
                break;
            default:
                var login = JSON.stringify(datos);
                console.debug(`Metodo default ${metodo}`);
                xhttp.setRequestHeader("Content-type", "application/json");

                //enviar la peticion asincrona, meter el codigo en overreadystatechange
                xhttp.send(login);
                console.debug(`Enviados datos`);
                break;

        }


    });
}