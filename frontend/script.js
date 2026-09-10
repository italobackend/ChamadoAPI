document.querySelector('form').addEventListener('submit', function (evento) {
    evento.preventDefault();

    const usuario = document.querySelector('#login').value;
    const password = document.querySelector('#senha').value;

    fetch('http://localhost:8080/api/auth/login', {
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({login: usuario, senha: password})})
            .then(resposta => resposta.json())
            .then(dados => console.log('Response backend: ', dados))
            .catch(erro => console.log('Deu erro: ', erro));

    console.log('Usuário digitado: ', usuario);
    console.log('Senha digitada: ', password);
})