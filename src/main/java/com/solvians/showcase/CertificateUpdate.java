package com.solvians.showcase;


import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class CertificateUpdate implements Callable<String> {

    private long epoch;
    private String isinValue;
    private double bidPrice;
    private int bidSize;
    private double askPrice;
    private int askSize;
    private int checkDigit;

    public CertificateUpdate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        this.epoch = Instant.now().toEpochMilli();
        this.isinValue = getIsinValue(random);
        this.bidPrice = random.nextDouble(100.00, 200.00 + 0.001);
        this.bidSize = random.nextInt(1000, 5000 + 1);
        this.askPrice = random.nextDouble(100.00, 200.00 + 0.001);
        this.askSize = random.nextInt(1000, 10000 + 1);
        this.checkDigit = getCheckDigitValue();
    }

    public long getEpoch() {
        return epoch;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    public String getIsinValue() {
        return isinValue;
    }

    public void setIsinValue(String isinValue) {
        this.isinValue = isinValue;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public int getBidSize() {
        return bidSize;
    }

    public void setBidSize(int bidSize) {
        this.bidSize = bidSize;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public int getAskSize() {
        return askSize;
    }

    public void setAskSize(int askSize) {
        this.askSize = askSize;
    }

    public int getCheckDigit() {
        return checkDigit;
    }

    public void setCheckDigit(int checkDigit) {
        this.checkDigit = checkDigit;
    }

    private String getIsinValue(ThreadLocalRandom random) {
        StringBuilder isinValue = new StringBuilder();

        // first 2 characters: random uppercase alphabets
        char c1 = (char) random.nextInt('A', 'Z' + 1);
        char c2 = (char) random.nextInt('A', 'Z' + 1);

        isinValue.append(c1).append(c2);

        // next 9 characters: random alphanumeric characters
        for (int i = 0; i < 9; i++) {
            int randomNumber = random.nextInt(1, 9);
            if (randomNumber % 2 == 0) { // even number, append a digit
                isinValue.append(randomNumber); // append a digit
            } else {
                isinValue.append((char) ('A' + randomNumber)); // append a random uppercase letter
            }
        }

//        // 1 check digit
//        int totalSum = isinValue.toString().chars().sum();
//        if ((totalSum % 10) % 2 == 0) {
//            // if the total sum is even append number
//            isinValue.append(totalSum % 10);
//        } else {
//            // append letter
//            isinValue.append((char) ('A' + (totalSum % 26)));
//        }

        return isinValue.toString();
    }

    private int getCheckDigitValue() {
        char[] ch = isinValue.toCharArray();
        int[] convertedValues = new int[ch.length];

//        7.1 Convert any letters to numbers by the conversion table below, e.g. “DE123456789” will be converted to “13 14 1 2 3 4 5 6 7 8 9”

        for (int i = 0; i < ch.length; i++) {
            if (ch[i] >= 'A' && ch[i] <= 'Z') {
                convertedValues[i] = ch[i] - 55; // A -> 10 Z-> 35
            } else {
                convertedValues[i] = ch[i] - '0';
            }
        }

//        7.2 Starting from the rightmost digit, double the value of every second digit (if doubling results in a number greater than 9, subtract 9 from it)

        for (int i = convertedValues.length - 1; i >= 0; i -= 2) {
            convertedValues[i] *= 2;
        }

//        7.3 Sum all the digits (after doubling and adjusting for values greater than 9)
        int sum = 0;

        for (int num : convertedValues) {
            if (num > 9) {
                while (num > 0) {
                    sum += num % 10;
                    num /= 10;
                }
            } else {
                sum += num;
            }
        }

//        7.4 The check digit is the number that, when added to the sum, results in a multiple of 10. If the check digit is 10, use 0 instead.
        return (sum / 10 + 1) * 10 - sum; // (54/10 + 1) * 10 - 54 = 60 - 54 = 6
    }

    @Override
    public String toString() {
        return epoch + "," + isinValue + checkDigit + "," + String.format("%.2f", bidPrice) + "," + bidSize + "," + String.format("%.2f", askPrice) + "," + askSize;
    }

    @Override
    public String call() throws Exception {
        return toString();
    }
}
