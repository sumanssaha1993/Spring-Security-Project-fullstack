import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import AuthService from '../service/AuthService';

export default function LogIn() {
 
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')

  const navigate = useNavigate();

  const handleUsername = (event) => {
    setUsername(event.target.value)
  }

  const handlePassword = (event) => {
    setPassword(event.target.value)
  }

  const handleLogin = (event) => {
    event.preventDefault()

    // AuthService.executeBasicAuthenticationService(username, password)
    // .then((response) => {
    //     console.log(response.data)
    //     localStorage.setItem('customer_name', response.data.name)
    //     localStorage.setItem('customer_id', response.data.id)
    //     localStorage.setItem('customer_email', response.data.email)
    //     localStorage.setItem('customer_pwd', response.data.pwd)
    //     sessionStorage.setItem('X-XSRF-TOKEN', response.data.csrfToken)
    //     AuthService.registerSuccessfulLogin(username, password)
    //     navigate("/contents", {})
    // }).catch((error) => {
    //     console.log(error)
    // })

    AuthService.executeBasicAuthenticationService(username, password)
            .then((response) => {
                AuthService.registerSuccessfulLoginForJwt(username, response.headers.authorization)
                localStorage.setItem('customer_name', response.data.name)
                localStorage.setItem('customer_id', response.data.id)
                localStorage.setItem('customer_email', response.data.email)
                localStorage.setItem('customer_pwd', response.data.pwd)
                navigate("/contents", {})
            }).catch((error) => {
                console.log(error)
            })

  }

  return (
    <>
        <div className="container-fluid" style={{color: 'white', marginTop: '5px'}}>
            <div className="row"></div>
            <div className="row">
                <div className="row" id="ds_news_block">
                    <h1 style={{color: 'black', textAlign: 'center'}}>Log In</h1>
                </div>
                <div className="row" id="ds_news_block" style={{marginTop: '10px'}}>
                    <div className="col-sm-4"></div>
                    <div className="col-sm-4">
                        <form onSubmit={handleLogin}>
                            <div className="row mb-3">
                                <label htmlFor="inputEmail3" style={{color: 'black'}} className="col-sm-2 col-form-label">Email</label>
                                <div className="col-sm-10">
                                    <input type="email" className="form-control" id="inputEmail3" onChange={handleUsername}/>
                                </div>
                            </div>
                            <div className="row mb-3">
                                <label htmlFor="inputPassword3" style={{color: 'black'}} className="col-sm-2 col-form-label">Password</label>
                                <div className="col-sm-10">
                                    <input type="password" className="form-control" id="inputPassword3" onChange={handlePassword}/>
                                </div>
                            </div>
                            <div className="d-grid gap-1 d-md-flex justify-content-md-end">
                                <button type="submit" className="btn btn-primary">Sign In</button>
                            </div>
                            <div className="my-3"></div>
                            <div className="d-grid gap-2 d-md-flex justify-content-md-end">
                                <b id='registerhere' style={{color: 'black'}}>New User ?</b>
                                <Link to="/register" className="btn btn-primary">Sign up</Link>
                            </div>
                        </form>
                    </div>
                    <div className="col-sm-4"></div>
                </div>
            </div>
            <div className="row"></div>
        </div>
    </>
  )
}
