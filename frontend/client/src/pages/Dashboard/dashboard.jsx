import {useState} from "react";
import {useNavigate} from "react-router-dom"

import './style.css'
import '../../index.css'
import '../Sidebar/sidebar.jsx'
import Sidebar from "../Sidebar/sidebar.jsx";

function Dashboard() {
    return (
        <Sidebar/>
    )
}

export default Dashboard