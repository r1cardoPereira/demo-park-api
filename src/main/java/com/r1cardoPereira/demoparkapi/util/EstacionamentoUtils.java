package com.r1cardoPereira.demoparkapi.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Utility class for parking operations.
 * This class is not meant to be instantiated, hence the private constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstacionamentoUtils {

    /**
     * Generates a receipt for a parking operation.
     * The receipt is a string representation of the current date and time,
     * formatted to remove any non-numeric characters except for the first dash.
     *
     * @return a string representing the receipt.
     */
    public static String gerarRecibo(){

        LocalDateTime date = LocalDateTime.now();
        String recibo = date.toString().substring(0,19);
        return recibo.replace("-","")
                .replace(":","")
                .replace("T", "-");
    }
}