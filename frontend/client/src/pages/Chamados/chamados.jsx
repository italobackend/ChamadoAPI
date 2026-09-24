import {useEffect, useState} from 'react'
import {useNavigate} from 'react-router-dom'

import './chamados.css'
import '../../index.css'
import '../Sidebar/sidebar.css'
import '../Header/header.css'
import Sidebar from '../Sidebar/sidebar.jsx'
import Header from '../Header/header.jsx'

function Chamados() {

    const [chamados, setChamados] = useState([])

    useEffect(() => {
        const token = localStorage.getItem('token')

        fetch("http://localhost:8080/api/chamados", {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })
            .then(resposta => resposta.json())
            .then(dados => {
                setChamados(dados)
            })
    }, [])

    return (
        <div className={"layout"}>
            <Sidebar/>
            <main className={"content"}>
                <Header titulo="Chamados"/>
                <Header descricao="Aqui estão todos os seus chamados"/>
                <div className={"pagina"}>
                    <table className={"tabela"}>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Descrição</th>
                            <th>Solicitante</th>
                            <th>Tipo</th>
                            <th>Status</th>
                            <th>Criado em</th>
                        </tr>
                        </thead>
                        <tbody>
                        {chamados.map((chamado) => (
                            <tr key={chamado.id}>
                                <td>{chamado.id}</td>
                                <td>{chamado.descricao}</td>
                                <td>{chamado.usuario}</td>
                                <td>{chamado.tipoChamado}</td>
                                <td>{chamado.status}</td>
                                <td>{chamado.criadoEm}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    )
}

export default Chamados;

