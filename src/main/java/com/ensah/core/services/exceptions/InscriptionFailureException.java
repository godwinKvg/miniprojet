package com.ensah.core.services.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InscriptionFailureException  extends  Exception{
       private  String message;

}
