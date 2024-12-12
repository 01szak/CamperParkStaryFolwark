package CPSF.com.demo;

import java.util.regex.Pattern;
/*
This regex in local part allows:
- numeric values from 0 to 9
- both uppercase and lowercase letters from a to z
- underscore “_”, hyphen “-“, and dot “.”
Dot isn’t allowed at the start and end of the local part
Consecutive dots aren’t allowed.
A maximum of 64 characters are allowed
In domain part:
- allows numeric values from 0 to 9
- allow both uppercase and lowercase letters from a to z
“-” and dot “.” aren’t allowed at the start and end of the domain part
No consecutive dots
from:
https://www.baeldung.com/java-email-validation-regex
*/
public class EmailValidator {
   private static final String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
           + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static boolean check(String email){
        return Pattern.compile(regexPattern).matcher(email).matches();
    }
}
