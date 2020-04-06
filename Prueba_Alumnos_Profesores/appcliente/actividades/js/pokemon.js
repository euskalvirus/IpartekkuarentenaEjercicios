window.addEventListener('load', init());

function init() {
    todos();

    /*Array.from(poke).forEach(function(p) {
        console.debug(p);
        p.addEventListener('click', busqueda);
    });*/

}



function busqueda(PokemonName) {
    let pokemon;

    const url = `https://pokeapi.co/api/v2/pokemon/${PokemonName}`;
    var xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function() {

        if (this.readyState == 4 && this.status == 200) {

            const jsonData = JSON.parse(this.responseText);

            pokemon = jsonData;
            let lineaapokemon = document.getElementById(pokemon.name);
            let lista2 = document.getElementById('id' + pokemon.name);

            if (lista2.hidden) {
                lista2.innerHTML = `<img id="imagenpokemon" src="${pokemon.sprites.front_default}" class="card-img-top imgpokemon" alt="...">
                <div class="card-body row">
                    <h5 id="${pokemon.name}titulo" class="card-title col-12">$pokemon.name # $pokemon.id</h5>
                    <div id="${pokemon.name}habilidades" class="col">
                        <p class="card-text">Habilidades</p>
                        <div id="${pokemon.name}listahabilidades">
                            <p>habilidad 1</p>
                            <p>habilidad 2</p>
                        </div>
                    </div>
                    <div id="${pokemon.name}stats" class="col">
                        <p class="card-text">Stats</p>
                        <div id="${pokemon.name}listastats">
                            <p>stat 1</p>
                            <p>stat 2</p>
                        </div>
                    </div>`
                let titulo = document.getElementById(`${pokemon.name}titulo`);
                titulo.innerText = `${pokemon.name} #${pokemon.id}`;
                let habilidades = "";
                pokemon.abilities.forEach(ability => habilidades += `<p class="list-group-item  nav-item" href="${ability.ability.url}">
                            <a href="${ability.ability.url}" target="_black" class="nav-link  btn-danger">${ability.ability.name}</a></p>`);

                console.debug(habilidades);

                let stats = "";
                pokemon.stats.forEach(stat => stats += `<p class="list-group-item  nav-item">${stat.stat.name}:${stat.base_stat}, ${stat.effort}`)
                lista2.hidden = false;

                document.getElementById(`${pokemon.name}listahabilidades`).innerHTML = habilidades;
                document.getElementById(`${pokemon.name}listastats`).innerHTML = stats;

                document.getElementById("b" + pokemon.name).style.fontSize = "50px";

            } else {
                lista2.innerHTML = '';
                lista2.hidden = true;
                document.getElementById("b" + pokemon.name).style.fontSize = "18px";
            }
        } // this.readyState == 4 && this.status == 200

    }; // onreadystatechange

    //preparamos la peticion GET
    xhttp.open("GET", url, true);
    //enviar la peticion asincrona, meter el codigo en overreadystatechange
    xhttp.send();
}

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
                                    <input onclick="busqueda('${pokemon.name}')" type="button" class="pokemon list-group-item btn btn-secondary" value='${pokemon.name}' url='${pokemon.url}' id='b${pokemon.name}'></li>
                                    <div id="id${pokemon.name}" class="card col" style="width: 18rem;" hidden>
                                    </div>
                                </div> `;
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

/*
function busqueda(PokemonName) {
    let pokemon;

    const url = `https://pokeapi.co/api/v2/pokemon/${PokemonName}`;
    var xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function() {

        if (this.readyState == 4 && this.status == 200) {

            const jsonData = JSON.parse(this.responseText);

            pokemon = jsonData;
            let lineaapokemon = document.getElementById(pokemon.name);
            let lista2 = document.getElementById('id' + pokemon.name);

            if (lista2.getElementsByClassName('list-group-item')[0].hidden) {
                lista2.innerHTML = "";

                pokemon.abilities.forEach(ability => lista2.innerHTML += `<li class="list-group-item  nav-item" href="${ability.ability.url}"  >
                            <a href="${ability.ability.url}" target="_black" class="nav-link  btn-danger">${ability.ability.name}</a></li>`);

                lista2.getElementsByClassName('list-group-item').hidden = false;
                lineapokemon.classList.add('justify-content-around');

                document.getElementById("b" + pokemon.name).style.fontSize = "50px";

            } else {
                lista2.innerHTML = '<li class="list-group-item" hidden></li>';

                lineapokemon.classList.remove('justify-content-around');
                document.getElementById("b" + pokemon.name).style.fontSize = "";
            }
        } // this.readyState == 4 && this.status == 200

    }; // onreadystatechange

    //preparamos la peticion GET
    xhttp.open("GET", url, true);
    //enviar la peticion asincrona, meter el codigo en overreadystatechange
    xhttp.send();
}
*/