package com.sotnyk.tictactoe;

import java.util.Scanner;

import static java.lang.System.out;

/**
 * Created by Serge on 13/03/2016.
 */
public class IoUtils {
    static boolean askYesNo(String prompt) {
        do {
            out.print(prompt);
            Scanner scanIn = new Scanner(System.in);
            String answer = scanIn.nextLine().toLowerCase();
            //scanIn.close();
            if (answer.equals("y")) return true;
            if (answer.equals("n")) return false;
        } while (true);
    }

    static int askNumber(String prompt, int min, int max){
        do {
            out.print(prompt);
            Scanner scanIn = new Scanner(System.in);
            String answer = scanIn.nextLine();
            try{
                int result = Integer.parseInt(answer);
                if (result<=max && result>=min)
                    return result;
            } catch (NumberFormatException ex){}
        } while (true);
    }
}
