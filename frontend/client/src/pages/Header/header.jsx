import {useState} from "react";
import {useNavigate} from "react-router-dom"

import './header.css'
import '../../index.css'

function Header({titulo, descricao}) {
    return (
        <div className={"layout"}>
            <div className={"content"}>
                <header className="header">
                    <h1>{titulo}</h1>
                    <p>{descricao}</p>
                </header>
            </div>
        </div>
    )
}

export default Header