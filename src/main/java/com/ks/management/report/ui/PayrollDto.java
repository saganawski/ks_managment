package com.ks.management.report.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PayrollDto {
    private String firstName;
    private String lastName;
    private BigDecimal hourlyRate;
    private BigDecimal regularHours;
    private BigDecimal hourlyPay;
    private BigDecimal overtimeRate;
    private BigDecimal overtimeHours;
    private BigDecimal overtimePay;
    private BigDecimal miles;
    private BigDecimal mileagePay;
    private BigDecimal totalPay;

    public static BigDecimal calculateOvertimePay(BigDecimal overtimeRate, BigDecimal overtimeHours){
        if(!overtimeRate.equals(new BigDecimal(0.0)) && !overtimeHours.equals(new BigDecimal(0.0)) ){
            return  overtimeRate.multiply(overtimeHours).setScale(2,RoundingMode.CEILING);
        }
        return new BigDecimal(0.0);
    }

    public static BigDecimal calculateRegularHours(BigDecimal totalMinutes, BigDecimal overtimeMinutes){
        BigDecimal regularHours = new BigDecimal(0.0);
        if(totalMinutes != null && !totalMinutes.equals(new BigDecimal(0.0))){
            if(overtimeMinutes != null && !overtimeMinutes.equals(new BigDecimal(0.0))){
                regularHours = totalMinutes.subtract(overtimeMinutes).divide(BigDecimal.valueOf(60), RoundingMode.CEILING).setScale(2);
            }else{
                regularHours = totalMinutes.divide(BigDecimal.valueOf(60),RoundingMode.CEILING).setScale(2);
            }
        }
        return regularHours;
    }

    public static BigDecimal calculateOvertimeRate(BigDecimal totalOvertimeMinutes, BigDecimal payRate) {
        if(totalOvertimeMinutes != null && !totalOvertimeMinutes.equals(new BigDecimal(0.0))){
            return payRate.multiply(new BigDecimal(1.5)).setScale(2,BigDecimal.ROUND_CEILING);
        }else{
            return  new BigDecimal(0.0);
        }
    }

    public static BigDecimal calculateHourlyPay(BigDecimal payRate, BigDecimal regularHours) {
        BigDecimal hourlyPay = new BigDecimal(0.0);
        if(payRate != null && !payRate.equals(new BigDecimal(0.0))){
            if(regularHours != null && !regularHours.equals(new BigDecimal(0.0))){
                hourlyPay = payRate.multiply(regularHours).setScale(2,RoundingMode.CEILING);
            }
        }
        return  hourlyPay;
    }

    public static BigDecimal calculateOvertimeHours(BigDecimal totalOvertimeMinutes) {
        if(!totalOvertimeMinutes.equals(new BigDecimal(0.0))){
            return totalOvertimeMinutes.divide(BigDecimal.valueOf(60),BigDecimal.ROUND_CEILING).setScale(2);
        }
        return new BigDecimal(0.0);
    }

    public static BigDecimal calculateMileagePay(BigDecimal totalMileage) {
        if(!totalMileage.equals(new BigDecimal(0.0))){
            return totalMileage.multiply(new BigDecimal(0.56)).setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        return new BigDecimal(0.0);
    }

    public static BigDecimal calculateTotalPay(BigDecimal hourlyPay, BigDecimal overtimePay, BigDecimal mileagePay) {
        BigDecimal totalPay = hourlyPay.add(overtimePay)
                .add(mileagePay)
                .setScale(2,BigDecimal.ROUND_HALF_UP);
        return totalPay;
    }
}
