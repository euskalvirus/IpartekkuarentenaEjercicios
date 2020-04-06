window.addEventListener('load', init());

function init() {
    todos();

    /*Array.from(poke).forEach(function(p) {
        console.debug(p);
        p.addEventListener('click', busqueda);
    });*/

}



function busqueda(urlPokemon) {
    let pokemon = "";


    const url = `https://pokeapi.co/api/v2/pokemon/${pokemon}`;
    var xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function() {

        if (this.readyState == 4 && this.status == 200) {

            const jsonData = JSON.parse(this.responseText);
            let pokemons;

            pokemon = jsonData;
            let listapokemon = document.getElementById(pokemon.name);
            let lista2 = document.getElementById('id' + pokemon.name);

            if (lista2.getElementsByClassName('list-group-item')[0].hidden) {
                lista2.innerHTML = "";
                pokemon.abilities.forEach(ability => lista2.innerHTML += `<li class="list-group-item  nav-item" href="${ability.ability.url}"  >
                            <a href="${ability.ability.url}" target="_black" class="nav-link  btn-danger">${ability.ability.name}</a></li>`);

                lista2.getElementsByClassName('list-group-item').hidden = false;
                listapokemon.classList.add('justify-content-around');

                document.getElementById("b" + pokemon.name).style.fontSize = "50px";

            } else {
                lista2.innerHTML = '<li class="list-group-item" hidden></li>';
                lista2.getElementsByClassName('list-group-item').hidden = true;
                listapokemon.classList.remove('justify-content-around');
                document.getElementById("b" + pokemon.name).style.fontSize = "";
            }
        } // this.readyState == 4 && this.status == 200

    }; // onreadystatechange

    //preparamos la peticion GET
    xhttp.open("GET", urlPokemon, true);
    //enviar la peticion asincrona, meter el codigo en overreadystatechange
    xhttp.send();
}

/*function todos() {

    const url = `https://pokeapi.co/api/v2/pokemon`;

    var xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function() {

        if (this.readyState == 4 && this.status == 200) {

            const jsonData = JSON.parse(this.responseText);
            let pokemons = jsonData.results;

            let lista = document.getElementById("lista");
            let lista2 = document.getElementById("lista2");
            lista.innerHTML = "";
            lista2.innerHTML = "";

            pokemons.forEach(pokemon => {
                console.debug(pokemon);
                let html = `<li> <input onclick="busqueda('${pokemon.url}')" type="button" class="pokemon" value='${pokemon.name}' url='${pokemon.url}' id='${pokemon.name}'></li> `;
                //let html = `<li class="pokemon" value="${pokemon.url}" id="${pokemon.name}" >${pokemon.name}</li>`;
                lista.innerHTML += html;

                lista2.innerHTML += `<li id="id${pokemon.name}" hidden></li>`
            }); // foreach

        } // this.readyState == 4 && this.status == 200

    }; // onreadystatechange

    //preparamos la peticion GET
    xhttp.open("GET", url, true);
    //enviar la peticion asincrona, meter el codigo en overreadystatechange
    xhttp.send();
}*/

function todos() {

    const url = `https://pokeapi.co/api/v2/pokemon`;

    var xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function() {

        if (this.readyState == 4 && this.status == 200) {

            const jsonData = JSON.parse(this.responseText);
            let pokemons = jsonData.results;

            let lista = document.getElementById("lista");

            lista.innerHTML = "";
            pokemons.forEach(pokemon => {
                let html = `<div id="${pokemon.name}" class='row'>
                                <li id="li${pokemon.name}" class="col-5"> 
                                    <input onclick="busqueda('${pokemon.url}')" type="button" class="pokemon list-group-item btn btn-secondary" value='${pokemon.name}' url='${pokemon.url}' id='b${pokemon.name}'></li>
                                <ul id="id${pokemon.name}" class="col-5 list-group habilidades">
                                    <li class="list-group-item" hidden></li></ul></div> `;
                //let html = `<li class="pokemon" value="${pokemon.url}" id="${pokemon.name}" >${pokemon.name}</li>`;
                lista.innerHTML += html;
            }); // foreach

        } // this.readyState == 4 && this.status == 200

    }; // onreadystatechange

    //preparamos la peticion GET
    xhttp.open("GET", url, true);
    //enviar la peticion asincrona, meter el codigo en overreadystatechange
    xhttp.send();
}