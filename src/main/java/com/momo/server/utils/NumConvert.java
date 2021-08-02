package com.momo.server.utils;

public class NumConvert {

    /*
10진수 2진수로 변환하는 메소드(배열에 2진수 담도록 구현)
 */
    public static int[] decToBin(int num, int dec){
        int[] bin = new int[num];
        for (int i = 0; i < num; i++) {
            if (dec % 2 == 1) {
                bin[i] = 1;
            } else if (dec % 2 == 0) {
                bin[i] = 0;
            }
            dec = dec / 2;
        }
        return bin;
    }

    /*
    2진수 배열을 10진수로 변환하는 메소드
     */
    public static int binToDec(int num, int[] bin){
        int dec = 0;
        int power = 0;
        for (int i = 0; i < num; i++) {
            double pow = Math.pow(2, power);
            bin[i] = bin[i] * 1 * (int) pow;
            dec = dec + bin[i];
            power = power + 1;
        }
        return dec;
    }
}
