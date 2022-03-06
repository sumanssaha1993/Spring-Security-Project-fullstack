import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import ContentService from '../service/ContentService';
import Content from './Content';

export default function Contents() {

    const [contents, setContents] = useState([])
    const navigate = useNavigate();

    useEffect(() => {
        ContentService.retrieveAllContents()
        .then((response) => {
            console.log(response.data)
            setContents(response.data)
        }).catch((error) => {
            console.log(error)
            if(error.response.status === 401){
                navigate("/logout", {})
            }
        })
        // eslint-disable-next-line
    }, []);

  return (
    <>
        <h1 className='text-center' style={{color: 'black', marginTop: '5px'}}>Get exclusive range of contents</h1>
        <div className="container my-3">
            <div className="row">
                {contents.map((element)=>{
                    return  <div className="col-md-4" key={element.id}>
                    <Content contentid={element.id} title={element.name} description={element.description} />
                </div>
                })}
            </div>
        </div>
  </>
  )
}
