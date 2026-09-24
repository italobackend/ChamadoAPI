import {useState} from 'react'
import {useNavigate} from 'react-router-dom'

import './configuracoes.css'
import '../../index.css'
import '../Sidebar/sidebar.css'
import '../Header/header.css'
import Sidebar from '../Sidebar/sidebar.jsx'
import Header from '../Header/header.jsx'

function Configuracoes() {
    return (
        <div className={"layout"}>
            <Sidebar/>
            <main className={"content"}>
                <Header titulo="Configurações"/>
                <Header descricao={"Defina configurações nesta tela"}/>
            </main>
        </div>
    )
}

export default Configuracoes;

