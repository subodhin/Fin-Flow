package com.finflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinflowApplication.class, args);

        //coding Qs for interviews
        //reverse a string!
        String str = "hello";
        String reversed ="";

        for(int i= str.length() -1;i>=0;i--){
            reversed += str.charAt(i);
        }

        System.out.println("Reversed string: " + reversed);
    }

}