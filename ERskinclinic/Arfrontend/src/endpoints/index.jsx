
import axios from "axios"
import{api_url, arAppointmentRoute } from "../constants"


   export const getAllArAppointments=async()=>{
    try{
       const response = await axios.get(api_url + arAppointmentRoute);
       return response;
    }catch (error){
       return error;
    }
   }
   export const saveArAppointments=async(arAppointment)=>{
       try{
           const response = await axios.post(api_url + arAppointmentRoute, arAppointment);
           return response;
        }catch (error){
           return error;
        }
      }
   export const updateArAppointments=async(arAppointment)=>{
       try{
           const response = await axios.put(api_url + arAppointmentRoute+`/${arAppointment.id}`, arAppointment);
           return response;
        }catch (error){
           return error;
        }      
   }
   export const deleteArAppointments=async(id)=>{
       try {
           const response = await axios.delete(api_url + arAppointmentRoute+`/${id}`);
           return response; 
         } catch (error) {
           return error; 
         }
   }
   export const searchArAppointmentsByID=async(id)=>{
       try {
           const response = await axios.get(api_url + arAppointmentRoute+`/${id}`);
           return response; 
         } catch (error) {
           return error;
  }
   
   }
   export const searchArAppointmentsByDate = async (date) => {
      try {
        const response = await axios.get(
          api_url + arAppointmentRoute + `/date/${date}`
        );
        return response;
      } catch (err) {
        return err;
      }
    };
    export const searchArAppointmentsByName = async (searchTerm) => {
      try {
        const response = await axios.get(api_url + arAppointmentRoute + `/search/${searchTerm}`);
        return response;
      } catch (error) {
        return error;
      }
    };
