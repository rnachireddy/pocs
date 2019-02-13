/**
 * 
 */
package com.test;

/**
 * @author rnachireddy
 *
 */
public class CheckNumeric {
	public static void main(String[] args) {

        String string = "123415sdafsdf574564543";
        boolean numeric = true;

        numeric = string.matches("-?\\d+(\\.\\d+)?");

        if(numeric)
            System.out.println(string + " is a number");
        else
            System.out.println(string + " is not a number");
    }
	
	public boolean validateIsNumeric(String name, String value){
		 boolean numeric = true;
		 numeric = name.matches("-?\\d+(\\.\\d+)?");
		 if(numeric){
			 return true;
		 } else{
			//TODO RETURN SERVICEException
			 return false;
		 }
	}
}

