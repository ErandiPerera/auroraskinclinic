import { Fragment, useEffect, useRef, useState } from "react";
import { deleteArAppointments, getAllArAppointments, saveArAppointments, 
  searchArAppointmentsByDate, searchArAppointmentsByID, 
  searchArAppointmentsByName, 
  updateArAppointments } from "../../endpoints";
import { Button,Checkbox,Col,Form,Input,Row,Select,Space,Table,Tag } from "antd";
import axios from "axios";
import { AddKey, api_url, 
  insertString,
  errorMessageDelete, 
  errorMessageRet, 
  successMessageDelete, 
  errorMessageUpdate, 
  UpdateKey, 
  successMessageUpdate,
  dermatologistRoute,
  arAppointmentRoute,
  successMessageUInsert} from "../../constants";
import { toast } from "react-toastify";
import {
  Label,
  Modal,
  ModalBody,
  ModalFooter,
  ModalHeader,
} from "reactstrap";
import "antd/dist/antd";
import { render } from "@testing-library/react";
import { useForm } from "antd/es/form/Form";
import { DeleteFilled, EditFilled, FilePdfOutlined, PlusOutlined } from "@ant-design/icons";
import PdfInvoicePopup from "./invoice";



const ArAppointments =()=>{
    const [arAppointments, setAppointments] = useState();
    const [dermatologist, setDermatologists] = useState([]);
    const[treatmentType, setTreatmentTypes] = useState([]);
  const [modal, setModal] = useState(false);
  const [header, setHeader] = useState(AddKey);
  const [modalState, setModalState] = useState(AddKey)
  const [selectedDate, setSelectedDate] = useState("");
    const [availableTimeSlots, setAvailableTimeSlots] = useState([]);
  const [updateArAppointmentID, setUpdateArAppointmentID] = useState();
  const [showInvoicePopup, setShowInvoicePopup] = useState(false); 
  const [currentInvoiceData, setCurrentInvoiceData] = useState(null);
  const [form] = Form.useForm(); 
  
  const handleDateChange = (e) => {
    const date = e.target.value;
    setSelectedDate(date); 
    fetchTimeSlots(date); 
};

    const columns = [
      {
        title: "Appointment ID",
        dataIndex: "id",
        key: "id",
      }, 
      {
        title: "Patient Name",

        key: "patientName",
        render: (text, record) => `${record.firstName} ${record.lastName}`,
      },
      {
        title: "Appointment Date",
        dataIndex: "appointmentDate",
        key: "appointmentDate",
      },
      {
        title: "Appointment Time",
        key: "appointmentTime",
        render: (record) => `${record.selectedTimeSlot.timeSlot}`,
      },
      {
        title: "Treatment Type",
        dataIndex: "treatmentType",
        key: "treatmentType",
      },
      {
        title: "Dermatologist",
        dataIndex: "dermatologist",
        key: "dermatologits",
      },
      {
        title: "Treatment Price",
        dataIndex: "treatmentPrice",
        key: "treatmentPrice",
      },
      {
        title: "Tax",
        dataIndex: "tax",
        key: "tax",
      },
      {
        title: "Total Price",
        dataIndex: "totalPrice",
        key: "totalPrice",
      },
      {
        title: "Paid",
        render: (record) => {
          return record.paid ? (
            <Checkbox disabled={true} checked={true} />
          ) : (
            <Checkbox disabled={true} checked={false} />
          );
        },
        key: "paid",
      },
      {
        align: "center",
        title: () => {
          return (
            <tr>
              <td>
                <Button 
                
                  style={{ color: "white", backgroundColor:"black" }}
                  onClick={() => toggle(AddKey)}
                >Add Appointment 
                  <PlusOutlined />
                </Button>
              </td>
            </tr>
          );
        },
        render: (_, record) => {
          return (
            <tr key={record.id}>
              
               <td>
               <Button onClick={() => { setCurrentInvoiceData(record); setShowInvoicePopup(true); }}>
                  <FilePdfOutlined />
                </Button>
              </td>
              <td>
                <Button onClick={() => toggle(UpdateKey, record)}>
                  <EditFilled />
                </Button>
              </td>
              <td>
                <Button onClick={() => { deletefromID(record.id);
                    }}>
                  <DeleteFilled/>
                </Button>
              </td>
            </tr>
          );
        },
        dataIndex: "actions",
      },
    ];
    const invoiceRef = useRef();

    //Calling getall 
    const getall = async () => {
      try {
        const response = await getAllArAppointments();
        console.log("getall response:", response); 
        if (response?.data?.data) {
          setAppointments(response.data.data); 
        } else {
          toast.error("Failed to load appointments. No data received.");
        }
      } catch (err) {
        toast.error("Error retrieving data.");
        console.error("Error during getall:", err);
      }
    };
   
    //Calling update 
    const update = async () => { 
      try {
        const formValues = await form.validateFields();
        formValues.id = updateArAppointmentID;
        const response = await updateArAppointments(formValues);
        console.log("Update response:", response); 
    
        if (response.data.responseId == 1) {
          await getall();
          toast.success(successMessageUpdate);
        } else {
          const alertMessage = response?.data?.responseAlert;
          toast.error(`Appointment is already booked: ${alertMessage}`); 
        }
      } catch (err) {
        console.error("Error during update:", err); 
        toast.error(`An unexpected error occurred: ${err.message || errorMessageUpdate}`);
      }
    };
    
    const toggle = (key, record) => {
      if (record) {
        setUpdateArAppointmentID(record.id);
        form.setFieldValue("firstName", record.firstName);
      form.setFieldValue("lastName", record.lastName);
      form.setFieldValue("nic", record.nic);
      form.setFieldValue("email", record.email);
      form.setFieldValue("phoneNo", record.phoneNo);
        form.setFieldValue("appointmentDate", record.appointmentDate);
        form.setFieldValue("appointmentTime", record.selectedTimeSlot.timeSlot);
        form.setFieldValue("dermatologist", record.dermatologist);
        form.setFieldValue("treatmentType", record.treatmentType);
        form.setFieldValue("paid", record.paid);
      }
  
      setModalState(key);
      if (key === UpdateKey) {
        setHeader(UpdateKey + " Appointment  ( ID:" + record.id + ")");
      } else {
        setHeader(insertString + " Appointment");
      }
      setModal(!modal);
    };
    
    //Calling delete 
    const deletefromID = (id) => {
      deleteArAppointments(id)
        .then((response) => {
          if (response.data.responseId == 1) {
            toast.success(successMessageDelete);
            getall();
          } else {
            toast.error(errorMessageDelete);
          }
        })
        .catch((err) => toast.error(errorMessageRet));
    };
    //Calling search from ID
    const searchfromID = () => {
      setAppointments([]); 
  
      searchArAppointmentsByID(keyword)
        .then((response) => {
          const appointmentData = response.data.data;
          
         
          if (Array.isArray(appointmentData)) {
            setAppointments(appointmentData);
          } else {
            setAppointments([]);  
          }
        })
        .catch((err) => {
          toast.error(errorMessageRet); 
        });
    };
    //search using date
  
    const searchfromDate = () => {
      setAppointments([]); 
      searchArAppointmentsByDate(keywordDate)
        .then((response) => {
          const appointmentData = response?.data?.data;
          
          if (Array.isArray(appointmentData)) {
            setAppointments(appointmentData);
          } else {
            setAppointments([]); 
          }
        })
        .catch((err) => {
          toast.error(errorMessageRet); 
        });
    };
    //Search using name
    const searchfromName = () => {
      setAppointments([]); 
  
      searchArAppointmentsByName(keywordName)
        .then((response) => {
          const appointmentData = response.data.data;
         
          if (Array.isArray(appointmentData)) {
            setAppointments(appointmentData);
          } else {
            setAppointments([]);  
          }
        })
        .catch((err) => {
          console.error(err); 
          toast.error(errorMessageRet);
        });
    };
    //Calling insert 
    const insert = async () => {
      try {
        const formValues = await form.validateFields();
        saveArAppointments(formValues)
          .then((response) => {
            if (response.data.responseId == 1) {
              getall();
              toast.success(successMessageUInsert);
            } else {
              const alertMessage = response?.data?.responseAlert;
              toast.error(`Appointment is already booked: ${alertMessage}`); 
            }
          })
          .catch((err) => toast.error(errorMessageUpdate));
      } catch (err) {}
    };
    
  
    useEffect(() => {
    
      getall();

      const fetchDermatologists = async () => {
        try {
            const response = await axios.get('http://localhost:8080/app/dermatologists');
            console.log("Fetched dermatologists:", response.data); // Check the data structure
            setDermatologists(response.data);
        } catch (error) {
            console.error("Error fetching dermatologists:", error);
        }
    };
    
    fetchDermatologists();

    const fetchTreatementType = async () => {
      try {
          const response = await 
          axios.get('http://localhost:8080/app/TreatmentType');
          console.log("Fetched TreatmentType:", response.data); 
          setTreatmentTypes(response.data);
      } catch (error) {
          console.error("Error fetching TreatmentType:", error);
      }
  };
  
  fetchTreatementType();

}, []);

useEffect(() => {
  if (selectedDate) { 
      fetchTimeSlots(selectedDate); 
  }
}, [selectedDate]);
  
const fetchTimeSlots = async (date) => {
  try {
      const response = await 
      axios.get('http://localhost:8080/app/available-time-slots', {
          params: { date } 
      });
      setAvailableTimeSlots(response.data);
  } catch (error) {
      console.error("Error fetching time slots:", error);
  }
};
     
    const Submit = () => {
      if (modalState == AddKey) {
        insert();
      } else {
        update();
      }
    };
    const [keyword, setKeyword] = useState("");
    const [keywordDate, setKeywordDate] = useState("");
    const [keywordName, setKeywordName] = useState("");
    return (
      <Fragment>
        <br></br>
        <Row>
          <Col md={3}></Col>
          <Col md={6}>
            <Row>
              <Col md={10}>
                <Input
                  value={keyword}
                  onChange={(e) => {
                    setKeyword(e.target.value);
                  }}
                  placeholder="Enter Appointment Id"
                />
              </Col>
              <Col md={4}>
                <Button
                  onClick={() => {
                    searchfromID();
                  }}
                >
                  Search
                </Button>
              </Col>
            </Row>
          </Col>
          <Col md={6}>
            <Row>
              <Col md={10}>
                <Input
                  value={keywordName}
                  onChange={(e) => {
                    setKeywordName(e.target.value);
                  }}
                  placeholder="Enter FirstName or LastName"
                />
              </Col>
              <Col md={4}>
                <Button
                  onClick={() => {
                    searchfromName();
                  }}
                >
                  Search
                </Button>
              </Col>
            </Row>
          </Col>
          <Col md={6}>
            <Row>
              <Col md={10}>
                <Input
                  value={keywordDate}
                  type="date"
                  onChange={(e) => {
                    setKeywordDate(e.target.value);
                  }}
                  placeholder="search Appointment by DateTime"
                />
              </Col>
              <Col md={4}>
                <Button
                  onClick={() => {
                    searchfromDate();
                  }}
                >
                  Search
                </Button>
              </Col>
            </Row>
          </Col>
        </Row>
  
        <br></br>
        <Row>
          <Col md={3}></Col>
          <Col md={18}>
            <Table columns={columns} dataSource={arAppointments && arAppointments} />
          </Col>
          <Col md={3}></Col>
        </Row>
        <Modal isOpen={modal} toggle={() => toggle(AddKey)} size="md">
          <ModalHeader toggle={() => toggle(AddKey)}>{header}</ModalHeader>
          <ModalBody>
            <Form form={form} initialValues={{ appointment: -1}}>
              <Row>
              <Col md={11}>
                  <Label>First Name <span style={{ color: "red" }}>*</span></Label>
                  <Form.Item
                    name={"firstName"}
                    rules={[{ required: true, message: "Required" }]}
                  >
                    <Input type="text" />
                  </Form.Item>
                </Col>
                <Col md={2}></Col>
                <Col md={11}>
                <Label>Last Name <span style={{ color: "red" }}>*</span></Label>
                  <Form.Item
                    name={"lastName"}
                    rules={[{ required: true, message: "Required" }]}
                  >
                    <Input type="text" />
                  </Form.Item>
                </Col>
              </Row>
              <Row>
              <Col md={11}>
                  <Label>NIC <span style={{ color: "red" }}>*</span></Label>
                  <Form.Item
                    name={"nic"}
                    rules={[{ required: true, message: "Required" }]}
                  >
                    <Input type="text" />
                  </Form.Item>
                </Col>
                <Col md={2}></Col>
                <Col md={11}>
                <Label>Email <span style={{ color: "red" }}>*</span></Label>
                  <Form.Item
                    name={"email"}
                    rules={[{ required: true, message: "Required" }]}
                  >
                    <Input type="text" />
                  </Form.Item>
                </Col>
              </Row>
              <Row>
              <Col md={18}>
                  <Label>Phone Number <span style={{ color: "red" }}>*</span></Label>
                  <Form.Item
                    name={"phoneNo"}
                    rules={[{ required: true, message: "Required" }]}
                  >
                    <Input type="text" />
                  </Form.Item>
                </Col>
              </Row>
              <Row>
              <Col md={11}>
        <Label>Date <span style={{ color: "red" }}>*</span></Label>
        <Form.Item name={"appointmentDate"} rules={[{ required: true, message: "Required" }]}>
        <input 
                type="date" 
                value={selectedDate} 
                onChange={(e) => {
                  handleDateChange(e);
                }} 
            />
        </Form.Item>
    </Col>
    <Col md={2}></Col>
    <Col md={11}>
        <Label>Time <span style={{ color: "red" }}>*</span></Label>
        <Form.Item name={"selectedTimeSlot"} rules={[{ required: true, message: "Required" }]}>
            <Select>
  {availableTimeSlots.map((timeSlot) => (
    <Select.Option key={timeSlot.timeSlot} value={timeSlot.timeSlot}> 
      {timeSlot.timeSlot} 
    </Select.Option>
  ))}
</Select>
        </Form.Item>
    </Col>
              </Row>
              <Row>
                <Col md={24}>
                  <Label>Dermatologist</Label>
                  <Form.Item
                    name={"dermatologist"}
                    rules={[
                      { required: true, message: "Required" },
                      {
                        validator: (_, value) => {
                          if (value === -1) {
                            return Promise.reject();
                          } else {
                            return Promise.resolve();
                          }
                        },
                        message: "Required",
                      },
                    ]}
                  >
                    <Select>
        <Select.Option value={-1}>---Select Dermatologist---</Select.Option>
        {Array.isArray(dermatologist) && dermatologist.length > 0 ? (
            dermatologist.map((dermatologist) => (
                <Select.Option key={dermatologist} value={dermatologist}>
                    {dermatologist}
                </Select.Option>
            ))
        ) : (
            <Select.Option disabled>No dermatologists available</Select.Option>
        )}
            </Select>
                  </Form.Item>
                </Col>
              </Row>
              <Row>
                <Col md={24}>
                  <Label>Treatement Type</Label>
                  <Form.Item
                    name={"treatmentType"}
                    rules={[
                      { required: true, message: "Required" },
                      {
                        validator: (_, value) => {
                          if (value === -1) {
                            return Promise.reject();
                          } else {
                            return Promise.resolve();
                          }
                        },
                        message: "Required",
                      },
                    ]}
                  >
                 <Select
                      
                    >
                      <Select.Option value={-1}>---Select Treatment Type---</Select.Option>
                        {Array.isArray(treatmentType) && treatmentType.length > 0 ? (
                          treatmentType.map((treatmentType) => (
                      <Select.Option key={treatmentType} value={treatmentType}>
                    {treatmentType}
                      </Select.Option>
                     ))
                 ) : (
                   <Select.Option disabled>No Treatment Type available</Select.Option>
                   )}
                  </Select>
                  </Form.Item>
                </Col>
              </Row>
              <Row>
              <Col>
                <Form.Item valuePropName="checked" name={"paid"}>
                  <Checkbox>Paid</Checkbox>
                </Form.Item>
              </Col>
            </Row>
            </Form>
          </ModalBody>
          <ModalFooter>
            <Button
              onClick={() => {
                Submit();
              }}
              color="default"
              variant="solid"
            >
              {"Submit"}
            </Button>
          </ModalFooter>
        </Modal>
        <PdfInvoicePopup invoiceData={currentInvoiceData} visible={showInvoicePopup} 
        onClose={() => setShowInvoicePopup(false)} />
      </Fragment>
      
    );
  };

export default ArAppointments;