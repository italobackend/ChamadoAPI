document.querySelector('form').addEventListener('submit', function (evento) {
    evento.preventDefault();

    const usuario = document.querySelector('#login').value;
    const password = document.querySelector('#senha').value;

    fetch('http://localhost:8080/api/auth/login', {
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({login: usuario, senha: password})
    })
        .then(resposta => {
            if (!resposta.ok) {
                return resposta.json().then(dadosErro => {
                    throw new Error(dadosErro.mensagem);
                })
            }
            return resposta.json();
        })
        .then(dados => {
            localStorage.setItem('token', dados.token)
            window.location.href = 'home.html'
        })
        .catch(erro => console.log('Erro: ', erro.message));
})