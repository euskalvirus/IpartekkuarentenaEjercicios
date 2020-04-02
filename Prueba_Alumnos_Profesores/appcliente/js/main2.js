console.info('esto lo puedes ver depurando');
console.debug('es una traza de debug');
let nombre = 'asdasd';
console.trace(`Esto suele ser para tracear o decir ${nombre}`);
console.warn('mensaje de warking');
console.error('mensaje de error')

var lista = document.getElementById('lista');

let nombres = ['Mary', 'Jenny', 'Jhon', 'Arthur', 'Stephany', 'Naruto', 'Girama'];
lista.innerHTML = '';
for (let i = 0; i < 7; i++) {
    lista.innerHTML += `<li class="list-group-item "><img src="img/avatar${i+1}.png " alt="imagen avatar ">${nombres[i]}</li>`
}