import axios from 'axios';
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';
import validator from 'validator'

export default function Registration(props) {

  const validationCheck = {
    nameField: {id:'inputName4', isFine: true, error: 'Name is not correct'},
    emailField: {id:'inputEmail4', isFine: true, error: 'Email Id is not correct'},
    phoneNumberField: {id:'inputPhoneNumber4', isFine: true, error: 'Phone number is not correct'},
    passwordField: {id:'inputPassword4', isFine: true, error: 'Password length should be greater than five and less than 10 and should contain only lower case and upper case characters, numbers and special characters'}
  }

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phoneNumber, setPhoneNumber] = useState(0);
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const handleOnNameChange = (evevnt) => {
    setName(evevnt.target.value)
    validationCheck.nameField.isFine = true
  }

  const handleOnEmailChange = (evevnt) => {
    var email = evevnt.target.value
    if(validator.isEmail(email)){
        setEmail(email)
        validationCheck.emailField.isFine = true
    }
    else{
        validationCheck.emailField.isFine = false
    }
  }

  const handleOnPhoneNumberChange = (evevnt) => {
    var mobile = evevnt.target.value
    if(eval(mobile)>999999999 && eval(mobile)<10000000000){
        setPhoneNumber(mobile)
        validationCheck.phoneNumberField.isFine = true
    }
    else{
        validationCheck.phoneNumberField.isFine = false
    }
  }

  const validatePassword = (evevnt) => {
    var password = evevnt.target.value
    var ck_password =  /^[A-Za-z0-9!@#$%^&*()_]{0,}$/;
    if(password.length>=5 && password.length<10 && ck_password.test(password)){
        setPassword(password)
        validationCheck.passwordField.isFine = true
    }
    else{
        validationCheck.passwordField.isFine = false
    }
  }

  const validateSubmit = (event) => {
    event.preventDefault()

    if(!name){
        validationCheck.nameField.isFine = false
        document.getElementById(validationCheck.nameField.id).focus()
        return false
    }
    else if(!email){
        validationCheck.emailField.isFine = false
        document.getElementById(validationCheck.emailField.id).focus()
        return false
    }
    else if(!phoneNumber){
        validationCheck.phoneNumberField.isFine = false
        document.getElementById(validationCheck.phoneNumberField.id).focus()
        return false
    }
    else if(!password){
        validationCheck.passwordField.isFine = false
        document.getElementById(validationCheck.passwordField.id).focus()
        return false
    }
    else{
        if(validationCheck.nameField.isFine && validationCheck.emailField.isFine && validationCheck.phoneNumberField.isFine && validationCheck.passwordField.isFine){
           
            var payload = {
                email:email,
                name:name,
                phonenumber:phoneNumber.toString(),
                pwd:password
            };

            postData('http://localhost:8080/public/add', payload)
        }
        else{
            return false
        }
    }
  }

  const postData = (baseURL, payload) => {
    axios.post(baseURL, payload)
    .then((response) => {
        console.log(response.data);
        navigate("/", {})
    }).catch(error => {
        console.log(error);
    });
  }

  return (
    <div className='container'>
        <div className="row" style={{color: 'black', marginTop: '5px'}}>
            <h1 className='text-center'>Register yourself</h1>
        </div>
        <div className="row">
            <div className="col-sm-4"></div>
            <div className="col-sm-4">
                <form className="row g-3" onSubmit={validateSubmit}>
                    <div className="col-md-6">
                        <label htmlFor="inputName4" className="form-label">Name</label>
                        <input type="text" className="form-control" id="inputName4" onChange={handleOnNameChange} required/>
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="inputEmail4" className="form-label">Email</label>
                        <input type="email" className="form-control" id="inputEmail4" onChange={handleOnEmailChange} required/>
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="inputPhoneNumber4" className="form-label">Phone Number</label>
                        <input type="number" className="form-control" id="inputPhoneNumber4" onChange={handleOnPhoneNumberChange} required/>
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="inputPassword4" className="form-label">Password</label>
                        <input type="password" className="form-control" id="inputPassword4" onChange={validatePassword} required/>
                    </div>
                    <div className="col-12">
                        <button type="submit" className="btn btn-primary">Sign Up</button>
                    </div>
                </form>
            </div>
            <div className="col-sm-4"></div>
        </div>
        <div className="row my-3" ></div>
    </div>
  )
}
