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
lista.innerHTML = '';
for (let i = 0; i < personas.length; i++) {
    lista.innerHTML += `<li class="list-group-item "><img src=${personas[i].avatar} " alt="imagen avatar ">${personas[i].nombre} </li>`
}

//Segunda forma de hacer la iteración
lista.innerHTML = '';
personas.forEach(persona => {
    lista.innerHTML += `<li class="list-group-item "><img src=${persona.avatar} " alt="imagen avatar ">${persona.nombre} </li>`
});

window.addEventListener('load', init());

function init() {
    console.debug('Document Load and Ready');
}