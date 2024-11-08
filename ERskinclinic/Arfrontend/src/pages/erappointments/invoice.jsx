import React, { useRef, useEffect } from "react";
import { Modal, Button } from "antd";
import jsPDF from "jspdf";
import html2canvas from "html2canvas";
import { ModalBody, ModalHeader } from "reactstrap";

const PdfInvoicePopup = ({ invoiceData, visible, onClose }) => {
  const invoiceRef = useRef(null);

  useEffect(() => {
    if (visible) {
      console.log("PdfInvoicePopup opened");
    }
  }, [visible]);

  const generatePDF = () => {
    const input = invoiceRef.current;
    html2canvas(input, { scale: 2 }).then((canvas) => {
      const imgData = canvas.toDataURL("image/png");
      const pdf = new jsPDF("p", "mm", "a4");
      const imgWidth = 210; // A4 width in mm
      const pageHeight = 297; // A4 height in mm
      const imgHeight = (canvas.height * imgWidth) / canvas.width;
      let heightLeft = imgHeight;
      let position = 0;

      pdf.addImage(imgData, "PNG", 0, position, imgWidth, imgHeight);
      heightLeft -= pageHeight;

      while (heightLeft > 0) {
        position = heightLeft - imgHeight;
        pdf.addPage();
        pdf.addImage(imgData, "PNG", 0, position, imgWidth, imgHeight);
        heightLeft -= pageHeight;
      }

      pdf.save(`invoice_${invoiceData.id}.pdf`);
    });
  };

  return (
    <Modal
      title="Invoice"
      open={visible}
      onCancel={onClose}
      footer={null}
    >
      <ModalHeader />
      <ModalBody>
        {invoiceData ? (
          <div ref={invoiceRef} style={styles.invoiceContainer}>
            <header style={styles.header}>
              <h1>INVOICE</h1>
              <div>
                <p>Invoice ID: {invoiceData.id}</p>
                <p>Appointment ID: {invoiceData.id}</p>
                <p>Date: {new Date().toLocaleDateString()}</p>
              </div>
            </header>

            <section style={styles.billToSection}>
              <h3>Patient Information:</h3>
              <p>
                Name: {`${invoiceData.firstName} ${invoiceData.lastName}`}
              </p>
              <p>Phone: {invoiceData.phoneNo}</p>
              <p>NIC: {invoiceData.nic}</p>
            </section>

            <section style={styles.detailsSection}>
              <h3>Appointment Details:</h3>
              <p>Dermatologist: {invoiceData.dermatologist}</p>
              <p>Appointment Date: {invoiceData.appointmentDate}</p>
              <p>Appointment Time: {invoiceData.selectedTimeSlot.timeSlot}</p>
              <p>Registration Fee: {invoiceData.registrationFee}</p>
              <p>Registration Fee Paid Status: {invoiceData.paid ? "Yes" : "No"}</p>
            </section>

            <section style={styles.detailsSection}>
              <h3>Charges:</h3>
              <p>Registration Fee: {invoiceData.registrationFee}</p>
              <p>Treatment Cost: {invoiceData.treatmentPrice}</p>
              <p>Tax: {invoiceData.tax}</p>
              <h2>Total Amount: {invoiceData.totalPrice}</h2>
            </section>
          </div>
        ) : null}
        <Button onClick={generatePDF} type="primary" style={styles.button}>
          Download PDF
        </Button>
      </ModalBody>
    </Modal>
  );
};

const styles = {
  invoiceContainer: {
    width: "460px",
    margin: "0 auto",
    padding: "20px",
    border: "1px solid #ddd",
    borderRadius: "8px",
    fontFamily: "Arial, sans-serif",
    fontSize: "12px",
  },
  header: {
    display: "flex",
    justifyContent: "space-between",
    borderBottom: "1px solid #ddd",
    paddingBottom: "10px",
    marginBottom: "20px",
  },
  billToSection: {
    marginBottom: "20px",
  },
  detailsSection: {
    marginBottom: "20px",
  },
  button: {
    padding: "10px 20px",
    fontSize: "14px",
    cursor: "pointer",
  },
};

export default PdfInvoicePopup;
