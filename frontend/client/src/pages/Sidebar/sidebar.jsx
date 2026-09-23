import {useEffect, useState} from 'react'
import {Link, useNavigate} from 'react-router-dom'
import './sidebar.css'
import '../../index.css'

import {LayoutDashboard, ClipboardList, Users, Bolt} from "lucide-react";

function Sidebar() {

    return (
        <div className={"sidebar"}>
            <div className={"sidebar-content"}>
                <h2>Polidesk</h2>
                <nav className="links">
                    <Link to={'/dashboard'}><LayoutDashboard size={18}/>Dashboard</Link>
                    <Link to={'/chamados'}><ClipboardList size={18}/>Chamados</Link>
                    <Link to={'/usuarios'}><Users size={18}/>Usuários</Link>
                    <Link to={'/configuracoes'}><Bolt size={18}/>Configurações</Link>
                </nav>
            </div>
        </div>
    )
}

export default Sidebar