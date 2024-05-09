package com.r1cardoPereira.demoparkapi.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for parking operations.
 * This class is not meant to be instantiated, hence the private constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstacionamentoUtils {

    private static final double PRIMEIROS_15_MINUTES = 5.00;
    private static final double PRIMEIROS_60_MINUTES = 9.25;
    private static final double ADICIONAL_15_MINUTES = 1.75;
    private static final double DESCONTO_PERCENTUAL = 0.30;

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

    /**
     * Calculates the cost of parking based on the time of entry and exit.
     * The cost is calculated as follows:
     * - For the first 15 minutes, the cost is PRIMEIROS_15_MINUTES.
     * - For the first 60 minutes, the cost is PRIMEIROS_60_MINUTES.
     * - For each additional 15 minutes after the first hour, the cost is ADICIONAL_15_MINUTES.
     * The cost is rounded to 2 decimal places using HALF_EVEN rounding mode.
     *
     * @param entrada the time of entry.
     * @param saida the time of exit.
     * @return the cost of parking as a BigDecimal.
     */
    public static BigDecimal calcularCusto(LocalDateTime entrada, LocalDateTime saida) {
        long minutes = entrada.until(saida, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {
            total = PRIMEIROS_15_MINUTES;
        } else if (minutes <= 60) {
            total = PRIMEIROS_60_MINUTES;
        } else {
            long addicionalMinutes = minutes - 60;
            Double totalParts = ((double) addicionalMinutes / 15);
            if (totalParts > totalParts.intValue()) { // 4.66 > 4
                total += PRIMEIROS_60_MINUTES + (ADICIONAL_15_MINUTES * (totalParts.intValue() + 1));
            } else { // 4.0
                total += PRIMEIROS_60_MINUTES + (ADICIONAL_15_MINUTES * totalParts.intValue());
            }
        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    /**
     * Calculates the discount based on the cost and the number of times a customer has parked.
     * The discount is calculated as follows:
     * - If the number of times a customer has parked is a multiple of 10, the discount is 30% of the cost.
     * - Otherwise, the discount is 0.
     * The discount is rounded to 2 decimal places using HALF_EVEN rounding mode.
     *
     * @param custo the cost of parking.
     * @param numeroDeVezes the number of times a customer has parked.
     * @return the discount as a BigDecimal.
     */
    public static BigDecimal calcularDesconto(BigDecimal custo, long numeroDeVezes) {
        BigDecimal desconto = ((numeroDeVezes > 0) && (numeroDeVezes % 10 == 0))
                ? custo.multiply(new BigDecimal(DESCONTO_PERCENTUAL))
                : new BigDecimal(0);
        return desconto.setScale(2, RoundingMode.HALF_EVEN);
    }
}