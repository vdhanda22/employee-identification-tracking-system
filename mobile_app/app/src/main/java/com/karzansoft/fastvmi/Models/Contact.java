package com.karzansoft.fastvmi.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Yasir on 4/17/2016.
 */
public class Contact {

    String code = "";
    Integer contactType;
    Integer contactSubType;
    String firstName="";
    String lastName="";
    String companyName="";
    String nationalId="";
    String contactNo="";
    String email;
    String name="";
    int id;
    /*int documentType;
    String documentNo;*/
    ArrayList<CustomerDocument> identityDocuments;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Integer getContactSubType() {
        return contactSubType;
    }

    public void setContactSubType(Integer contactSubType) {
        this.contactSubType = contactSubType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }*/

    public ArrayList<CustomerDocument> getIdentityDocuments() {
        return identityDocuments;
    }

    public void setIdentityDocuments(ArrayList<CustomerDocument> identityDocuments) {
        this.identityDocuments = identityDocuments;
    }
}
