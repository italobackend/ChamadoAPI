import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from './pages/Home/index.jsx'
import Sidebar from './pages/Sidebar/sidebar.jsx'
import Header from './pages/Header/header.jsx'
import Dashboard from "./pages/Dashboard/dashboard.jsx";

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login/>}/>
                <Route path="/sidebar" element={<Sidebar/>}/>
                <Route path="/dashboard" element={<Dashboard/>}/>
            </Routes>
        </BrowserRouter>
    </StrictMode>
)