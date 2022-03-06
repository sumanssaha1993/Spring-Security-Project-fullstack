import React, { useEffect } from 'react'
import { useNavigate } from 'react-router-dom';

export default function Logout() {

    const navigate = useNavigate();

    useEffect(() => {
        sessionStorage.clear();
        navigate("/", {})

        // eslint-disable-next-line
    }, []);


  return (
    <div>
        <h2>Logging you out........</h2>
    </div>
  )
}
