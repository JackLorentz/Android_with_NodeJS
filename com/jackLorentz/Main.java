package com.jackLorentz;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static int num;

    public static void main(String args[]){
        System.out.print("How many thread do you want ? ");
        if((num = scanner.nextInt()) > 0){
            (new Thread(new Server(12345, num))).start();
        }
    }
}
