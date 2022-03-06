import React from 'react'
import { useNavigate } from 'react-router-dom';
import SubscriptionService from '../service/SubscriptionService'

export default function Content(props) {

    const navigate = useNavigate();

    const addSubscription = () => {
        let detail = {
            custid: parseInt(localStorage.getItem('customer_id')),
            contentid:props.contentid
        }
        SubscriptionService.subscribeContent(detail)
        .then((response) => {
            console.log(response.data)
        }).catch((error) => {
            console.log(error)
            if(error.response.status === 401){
              navigate("/logout", {})
            }
        })
    }

  return (
    <div className="my-3">
      <div className="card">
        <div className="card-body">
          <h5 className="card-title">{props.title}</h5>
          <p className="card-text">{props.description}</p>
          <button className="btn btn-warning" onClick={addSubscription}>Subscribe</button>
        </div>
      </div>
    </div>
  )
}
