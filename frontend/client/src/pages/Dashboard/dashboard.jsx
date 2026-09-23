import {useState} from "react";
import {useNavigate} from "react-router-dom";

import '../../index.css'
import '../Sidebar/sidebar.css'
import '../Header/header.css'
import Sidebar from "../Sidebar/sidebar.jsx"
import Header from "../Header/header.jsx";

function Dashboard() {
    return (
        <div className="layout">
            <Sidebar/>
            <main className={"content"}>
                <Header titulo="Dashboard"/>
            </main>
        </div>
    )
}

export default Dashboard;