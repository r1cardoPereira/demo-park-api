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

    public static BigDecimal calcularCusto(LocalDateTime entrada, LocalDateTime saida) {
        long minutes = entrada.until(saida, ChronoUnit.MINUTES);
        double total = 0.0;
        if (minutes <= 15) {
            total = PRIMEIROS_15_MINUTES;
        } else if (minutes <= 60) {

            total = PRIMEIROS_60_MINUTES;
        } else {
            total = PRIMEIROS_60_MINUTES;
            long minutosAdicionais = minutes - 60;
            double custoAdicional = Math.ceil(minutosAdicionais / 15.0) * ADICIONAL_15_MINUTES;
            total += custoAdicional;
        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calcularDesconto(BigDecimal custo, long numeroDeVezes) {

        BigDecimal desconto = BigDecimal.ZERO;
        if (numeroDeVezes % 10 == 0 && numeroDeVezes > 0){
            desconto = custo.multiply(BigDecimal.valueOf(DESCONTO_PERCENTUAL));
        }
        return desconto.setScale(2, RoundingMode.HALF_EVEN);
    }
}