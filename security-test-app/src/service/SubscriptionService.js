import axios from 'axios'
import { USER_NAME_SESSION_ATTRIBUTE_TOKEN } from './AuthService';

const API_URL = 'http://localhost:8080'
const SUBSCRIPTION_API_URL = `${API_URL}/users/subscribe`

class SubscriptionService {

    subscribeContent(details) {

        axios.defaults.xsrfHeaderName = "X-CSRFTOKEN";
        axios.defaults.xsrfCookieName = "csrftoken";

        return  axios({
                    method: 'post',
                    url: SUBSCRIPTION_API_URL,
                    data: details,
                    headers: { 
                        'Content-Type': 'application/json',
                        'emailid': localStorage.getItem('customer_email'),
                        'Authorization': `${sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_TOKEN)}` 
                    }

                });

        // return axios.post(SUBSCRIPTION_API_URL,
        //     details,
        //     { 
        //         headers: { 
        //             'Content-Type': 'application/json',
        //             'Authorization': sessionStorage.getItem('user_token') 
        //         }, 
        //     });
    }

}

export default new SubscriptionService()