import {useState} from 'react'
import {useNavigate} from 'react-router-dom'

import './chamados.css'
import '../../index.css'
import '../Sidebar/sidebar.css'
import '../Header/header.css'
import Sidebar from '../Sidebar/sidebar.jsx'
import Header from '../Header/header.jsx'

function Chamados() {
    return (
        <div className={"layout"}>
            <Sidebar/>
            <main className={"content"}>
                <Header titulo="Chamados"/>
            </main>
        </div>
    )
}

export default Chamados;

