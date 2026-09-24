import {useEffect, useState} from 'react'
import {useNavigate} from 'react-router-dom'

import './chamados.css'
import '../../index.css'
import '../Sidebar/sidebar.css'
import '../Header/header.css'
import Sidebar from '../Sidebar/sidebar.jsx'
import Header from '../Header/header.jsx'


const colunas = ['Em aberto', 'Em andamento', 'Conluído', 'Arquivado']

function Chamados() {

    const [chamados, setChamados] = useState([])
    const [modalAberto, setModalAberto] = useState(false)
    const [descricao, setDescricao] = useState('')
    const [solicitante, setSolicitante] = useState('')
    const [tipoChamado, setTipoChamado] = useState('')
    const [criadoEm, setCriadoEm] = useState(Date.now)

    const formatarData = (texto) => {
        return new Date(texto).toLocaleString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
        })
    }

    const token = localStorage.getItem('token')

    const listarChamados = () => {
        fetch('http://localhost:7071/api/chamados',
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        )

            .then(resposta => resposta.json())
            .then(dados => setChamados(dados))
    }

    useEffect(() => {
        listarChamados()
    }, [])


    const handleCriar = async (evento) => {
        console.log("o handle foi chamado")
        evento.preventDefault()

        const resposta = await fetch(`http://localhost:7071/api/chamados`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify({descricao, tipoChamado}),
        })

        if (resposta.ok) {
            setModalAberto(false)
            setDescricao('')
            setSolicitante('')
            setTipoChamado('')
            setCriadoEm(Date.now)
            listarChamados()
        } else if (tipoChamado === '') {
            alert("É preciso selecionar um tipo de chamado para continuar")
        } else {
            alert("Erro ao criar chamado")
        }
    }


    return (
        <div className={"layout"}>
            <Sidebar/>
            <main className={"content"}>
                <Header titulo="Chamados"/>
                <Header descricao="Aqui estão todos os seus chamados"/>
                <div className={"pagina"}>
                    <button className={"btn-novo"} onClick={() => setModalAberto(true)}>
                        Novo chamado
                    </button>

                    <div className={"kanban"}>
                        <div className={"kanban-coluna"}>
                            <h3>Em aberto</h3>
                        </div>
                        <div className={"kanban-coluna"}>
                            <h3>Em andamento</h3>
                        </div>
                        <div className={"kanban-coluna"}>
                            <h3>Concluído</h3>
                        </div>
                        <div className={"kanban-coluna"}>
                            <h3>Arquivado</h3>
                        </div>
                    </div>

                </div>

                {modalAberto && (
                    <div className={"modal-fundo"}>
                        <div className={"modal"}>
                            <h2>Cadastre um novo chamado</h2>

                            <form className={"modal-form"} onSubmit={handleCriar}>
                                <select
                                    value={tipoChamado}
                                    onChange={e => setTipoChamado(e.target.value)}
                                    required
                                >

                                    <option value="">Tipo de chamado</option>
                                    <option value="INSTALACAO">Instalação</option>
                                    <option value="MANUTENCAO_SOFTWARE">Software</option>
                                    <option value="MANUTENCAO_DISPOSITIVO">Dispositivo</option>
                                    <option value="REDE">Rede</option>
                                </select>

                                <textarea
                                    placeholder="Descreva aqui o problema ocorrido"
                                    value={descricao}
                                    onChange={e => setDescricao(e.target.value)}
                                    required
                                />
                                <div className={"modal-btn"}>
                                    <button type={"button"} onClick={e => setModalAberto(false)}>
                                        Cancelar
                                    </button>
                                    <button type={"submit"}>
                                        Abrir chamado
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}
            </main>
        </div>
    )
}

export default Chamados;

