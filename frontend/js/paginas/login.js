const formulario = document.getElementById('login-form');

formulario.addEventListener('submit', async (e) => {
    e.preventDefault();

    const login = document.getElementById('login').value;
    const senha = document.getElementById('senha').value;
    const mensagemErro = document.getElementById('mensagem-erro');

    try {
        const resposta = await fetch('http://localhost:7071/api/auth/login', {
            method: 'POST', headers: {
                'Content-Type': 'application/json',
            }, body: JSON.stringify({login: login, senha: senha}),
        });

        const dados = await resposta.json();

        if (!resposta.ok) {
            mensagemErro.textContent = dados.message || 'Login ou senha inválidos!';
            return;
        }

        localStorage.setItem('token', JSON.stringify(dados.token));
        window.location.href = '../../chamados.html';
    } catch (erro) {
        console.error('Erro na requisição: ', erro);
        mensagemErro.textContent = 'Não foi possível conectar ao servidor.';
    }

})