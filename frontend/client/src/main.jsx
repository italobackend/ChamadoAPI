import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from './pages/Login/index.jsx'
import Dashboard from "./pages/Dashboard/dashboard.jsx";
import Chamados from "./pages/Chamados/chamados.jsx";
import Usuarios from "./pages/Usuarios/usuarios.jsx";
import Configuracoes from "./pages/Configuracoes/configuracoes.jsx";

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login/>}/>
                <Route path="/dashboard" element={<Dashboard/>}/>
                <Route path="/chamados" element={<Chamados/>}/>
                <Route path="/usuarios" element={<Usuarios/>}/>
                <Route path="/configuracoes" element={<Configuracoes/>}/>
            </Routes>
        </BrowserRouter>
    </StrictMode>)