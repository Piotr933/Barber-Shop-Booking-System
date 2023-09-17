package com.piotrzawada.BarberShopBookingSystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * The class representing a response message.
 * This class is used to encapsulate messages for various operations.
 * @author Piotr Zawada
 * @version 0.3.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    String message;
}
