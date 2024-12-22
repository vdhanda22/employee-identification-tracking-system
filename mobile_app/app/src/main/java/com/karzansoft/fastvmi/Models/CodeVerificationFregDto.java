package com.karzansoft.fastvmi.Models;

public class CodeVerificationFregDto {
    public int codeLenght;
    public String status;
    
    public String getStatusCode() {
        return status;
    }

    public void setStatusCode(String statusCode) {
        this.status = statusCode;
    }

    public int getCodeLenght() {
        return codeLenght;
    }

    public void setCodeLenght(int codeLenght) {
        this.codeLenght = codeLenght;
    }

}
