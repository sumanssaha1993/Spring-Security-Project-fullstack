import {
  BrowserRouter as Router,
  Routes,
  Route
} from "react-router-dom";
import Contents from "./components/Contents";
import LogIn from "./components/LogIn";
import Logout from "./components/Logout";
import Registration from "./components/Registration";


function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route exact path="/" element={<LogIn key='login'/>}/>
          <Route exact path="/register" element={<Registration key='register'/>}/>
          <Route exact path="/contents" element={<Contents key='contents'/>}/>
          <Route exact path="/logout" element={<Logout key='logout'/>}/>
        </Routes>
      </Router>
    </>
  );
}

export default App;
