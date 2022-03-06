import axios from 'axios'
import { USER_NAME_SESSION_ATTRIBUTE_TOKEN } from './AuthService';

const API_URL = 'http://localhost:8080'
const CONTENTS_API_URL = `${API_URL}/users`

class ContentService {

    retrieveAllContents() {
        return axios.get(`${CONTENTS_API_URL}/contents`,
        { 
            headers: { Authorization: sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_TOKEN),
                     emailid: localStorage.getItem('customer_email')
                   } 
        }
        );
    }

}

export default new ContentService()