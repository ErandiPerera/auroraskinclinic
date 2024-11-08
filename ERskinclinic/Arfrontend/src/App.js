import logo from './logo.svg';
import './App.css';

import { Link, Navigate, Route, BrowserRouter as Router, Routes } from "react-router-dom";
import ErAppointments from "./pages/erappointments";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer, toast} from "react-toastify";
import "bootstrap/dist/css/bootstrap.min.css";
import Container from "react-bootstrap/Container";
import Navbar from "react-bootstrap/Navbar";

function App() {
  return (
    
    <Router>
      <ToastContainer position="top-right" 
        autoClose={5000} 
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover></ToastContainer>
      <>
      
        <Navbar  bg="dark" data-bs-theme="dark">
          <Container className="justify-content-center" > 
            <Navbar.Brand href="/" >Aurora Skin Care </Navbar.Brand>
          </Container>
        </Navbar>
      </>
      <div>
        <Routes>
          
        <Route path="/" element={<Navigate to="/erappointments" />} />
        <Route path="/erappointments" element={<ErAppointments />} />
        </Routes>
      </div>
    </Router>
  );
}


export default App;
