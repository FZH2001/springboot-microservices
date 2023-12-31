package com.example.fundtransferservice.service;

import com.codingerror.model.AddressDetails;
import com.codingerror.model.HeaderDetails;
import com.codingerror.service.CodingErrorPdfInvoiceCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReceiptGeneratorService {


    LocalDate ld= LocalDate.now();
    String pdfName= ld+".pdf";
    CodingErrorPdfInvoiceCreator cepdf=new CodingErrorPdfInvoiceCreator(pdfName);
    public void createDocument() throws FileNotFoundException {
        cepdf.createDocument();
    }
    public void createHeaderDetails(String invoiceNo){
        HeaderDetails header=new HeaderDetails();
        header.setInvoiceNo(invoiceNo).setInvoiceDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).build();
        cepdf.createHeader(header);
    }

    public void createAddress(String beneficiaryName,String address,String email){
        AddressDetails addressDetails=new AddressDetails();
        addressDetails
                .setBillingName(beneficiaryName)
                .setBillingAddress(address)
                .setBillingEmail(email)
                .build();

        cepdf.createAddress(addressDetails);
    }

    public void createTermsAndConditions(){
        List<String> TncList=new ArrayList<>();
        TncList.add("1. The Seller shall not be liable to the Buyer directly or indirectly for any loss or damage suffered by the Buyer.");
        TncList.add("2. The Seller warrants the product for one (1) year from the date of shipment");
        String imagePath="src/main/resources/ce_logo_circle_transparent.png";
        cepdf.createTnc(TncList,false,imagePath);
    }



}
