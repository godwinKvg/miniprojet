package com.ensah.core.utils;

import lombok.Data;

@Data
public class ResponseTransfer {
    public String message;
    public  ResponseTransfer(String message){
         this.message=message;
    }
}
