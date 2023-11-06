package com.example.customer.validations.impl;

import com.example.customer.validations.CustomerExceptionService;
import com.example.library.models.Customer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling exceptions related to the Customer.
 */
@Getter
@Component
@RequiredArgsConstructor
public class CustomerExceptionServiceImpl implements CustomerExceptionService {

  private String errorMessage;

  /**
   * Validates the Customer object and sets the error message if any validation fails.
   *
   * @param customer the Customer object to validate
   */
  @Override
  public void validate(Customer customer) {
    errorMessage = "";
    handlePhoneNumber(customer);
    handleAddress(customer);
  }

  @Override
  public void handlePhoneNumber(Customer customer) {
    String phoneNumber = customer.getPhoneNumber();
    if (phoneNumber == null || phoneNumber.isEmpty()) {
      errorMessage = "Phone number is required!";
      return;
    }

    String patterns = "^(\\+\\d{1,3}( )?)?(\\(\\d{3}\\)|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";

    Pattern pattern = Pattern.compile(patterns);
    Matcher matcher = pattern.matcher(phoneNumber);
    if (!matcher.matches()) {
      errorMessage = "Invalid phone number!";
    }
  }

  @Override
  public void handleAddress(Customer customer) {
    String address = customer.getAddress();
    if (address == null) {
      errorMessage = "Address is required!";
      return;
    }

    String[] addressComponents = address.split(",");
    if (addressComponents.length != 4) {
      errorMessage = "Invalid address format! "
        + "The address should include ZipCode, Street, City, and Country separated by commas.";
      return;
    }

    String zipCode = addressComponents[0].trim();
    String zipCodePattern = "^\\d{5}(?:-\\d{4})?$";
    if (!zipCode.matches(zipCodePattern)) {
      errorMessage = "Invalid ZipCode format! The ZipCode should be in the format XXXXX "
        + "or XXXXX-XXXX.";
    }
  }
}
