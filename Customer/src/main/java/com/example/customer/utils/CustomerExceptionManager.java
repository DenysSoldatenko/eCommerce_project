package com.example.customer.utils;

import com.example.library.models.Customer;
import com.example.library.services.implementations.CustomerServiceImpl;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling exceptions related to the Customer.
 */
@Getter
@Component
public class CustomerExceptionManager {

  private final CustomerServiceImpl customerService;
  private String message;

  @Autowired
  public CustomerExceptionManager(CustomerServiceImpl customerService) {
    this.customerService = customerService;
  }

  /**
   * Validates the Customer object and sets the error message if any validation fails.
   *
   * @param customer the Customer object to validate
   */
  public void validate(Customer customer) {
    message = "";
    handlePhoneNumber(customer);
    handleAddress(customer);
  }

  private void handlePhoneNumber(Customer customer) {
    String phoneNumber = customer.getPhoneNumber();
    if (phoneNumber == null || phoneNumber.isEmpty()) {
      message = "Phone number is required!";
      return;
    }

    String patterns = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
        + "|^(\\+\\d{1,3}( )?)?(\\d{3} ?){2}\\d{3}$"
        + "|^(\\+\\d{1,3}( )?)?(\\d{3} ?)(\\d{2} ?){2}\\d{2}$";

    Pattern pattern = Pattern.compile(patterns);
    Matcher matcher = pattern.matcher(phoneNumber);
    if (!matcher.matches()) {
      message = "Invalid phone number!";
    }
  }

  private void handleAddress(Customer customer) {
    String address = customer.getAddress();
    if (address == null) {
      message = "Address is required!";
      return;
    }

    String[] addressComponents = address.split(",");
    if (addressComponents.length != 4) {
      message = "Invalid address format! "
        + "The address should include ZipCode, Street, City, and Country separated by commas.";
      return;
    }

    String zipCode = addressComponents[0].trim();
    String zipCodePattern = "^[0-9]{5}(?:-[0-9]{4})?$";
    if (!zipCode.matches(zipCodePattern)) {
      message = "Invalid ZipCode format! The ZipCode should be in the format XXXXX or XXXXX-XXXX.";
    }
  }
}
