import {useState} from "react";
import {useNavigate} from "react-router-dom"

import './index.css'
import '../../index.css'

function Login() {

    const [login, setLogin] = useState('')
    const [senha, setSenha] = useState('')
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)
    const navigate = useNavigate()

    const handleLogin = async (event) => {
        event.preventDefault();
        setLoading(true)
        setError('')

        try {
            const resposta = await fetch('http://localhost:7071/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    login, senha
                }),
            })

            const dados = await resposta.json()

            if (!resposta.ok) {
                throw new Error(dados.message || 'Erro ao tentar fazer login.');
            }
            localStorage.setItem('token', dados.token);
            alert("Login direcionado com sucesso!")
            console.log(dados.token);
            navigate('/dashboard');
        } catch (err) {
            setError(err.message)
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="login-container">
            <div className="form-container">
                <h1>Faça seu log-in</h1>
                <form className="login-form" onSubmit={handleLogin}>
                    <input type="text"
                           placeholder="Seu usuário"
                           name={"login"}
                           value={login}
                           onChange={(e) => setLogin(e.target.value)}
                    />
                    <input type="password"
                           placeholder="Sua senha"
                           name={"senha"}
                           value={senha}
                           onChange={(e) => setSenha(e.target.value)}
                    />
                    <button type={"submit"}
                            className={"btn-login"} disabled={loading}>{loading ? 'Entrando...' : 'Entrar'}
                    </button>
                    {error && <p className={"erro-login"}>{error}</p>}
                </form>
            </div>
        </div>
    )
}

export default Login