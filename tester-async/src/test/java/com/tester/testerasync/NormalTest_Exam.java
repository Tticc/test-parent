package com.tester.testerasync;

import org.junit.Test;

import java.util.Arrays;
import java.util.Scanner;

public class NormalTest_Exam {
//5 5
//    HELLOWORLD
//            CPUCY
//    EKLQH
//            CHELL
//    LROWO
//            DGRBC

    @Test
    public void test_splitWorld(){
        String str = "DGRBCOWORL";
        for (int i = 0; i < str.length(); i++) {
            System.out.print("\""+str.substring(i,i+1)+"\",");
        }
    }

    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String s = scanner.nextLine();
//        String[] s1 = s.split(" ");
//        int row = Integer.valueOf(s1[0]);
//        int column = Integer.valueOf(s1[1]);
//
//        String word = scanner.nextLine();
//
//        String[] wordLetter = new String[word.length()];
//        for (int i = 0; i < word.length(); i++) {
//            wordLetter[i] = word.substring(i,i+1);
//        }
//
//        String[] letter = new String[row*column];
//        int count = 0;
//        for (int i = 0; i < row; i++) {
//            String rowStr = scanner.nextLine();
//            for (int j = 0; j < column; j++) {
//                letter[count++] = rowStr.substring(j,j+1);
//            }
//        }
//        String[] letterCopy = Arrays.copyOfRange(letter, 0, letter.length);
//
        int row = 5;
        int column = 5;
        String[] wordLetter = new String[]{"c","B","R"};
        String[] letter = new String[]{"C","P","U","C","Y","E","K","L","Q","H","C","H","E","L","L","L","R","O","W","O","D","G","R","B","C"};
        String[] letterCopy = Arrays.copyOfRange(letter, 0, letter.length);


        int targetRow = -1;
        int targetColumn = -1;
        for (int i = 0; i < letter.length; i++) {
            int wordIndex = 0;
            if(letter[i].equals(wordLetter[wordIndex])){
                targetRow = i/row+1;
                targetColumn = i%row+1;
                wordIndex++;
//                letterCopy[i] = null;
                int tempi = i;
                for (int j = 1; j < wordLetter.length; j++) {
                    if(tempi%column > 0 && wordLetter[wordIndex].equals(letterCopy[tempi-1])){
//                        letterCopy[tempi-1] = null;
                        tempi = tempi-1;
                    }else if(tempi%column < column-1 && wordLetter[wordIndex].equals(letterCopy[tempi+1])){
//                        letterCopy[tempi+1] = null;
                        tempi = tempi+1;
                    }else if(tempi/row > 0 && wordLetter[wordIndex].equals(letterCopy[tempi-column])){
//                        letterCopy[tempi-column] = null;
                        tempi = tempi-column;
                    }else if(tempi/row < row-1 && wordLetter[wordIndex].equals(letterCopy[tempi+column])){
//                        letterCopy[tempi+column] = null;
                        tempi = tempi+column;
                    }else {
                        letterCopy = Arrays.copyOfRange(letter, 0, letter.length);
                        targetRow = -1;
                        targetColumn = -1;
                        break;
                    }
                    wordIndex++;
                }
                if(wordIndex>=wordLetter.length){
                    break;
                }
            }
        }
        if(targetRow == -1){
            System.out.println("NO");
        }else{
            System.out.print(targetRow+" "+targetColumn);
        }
    }



    public static void main_sortStudent(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        int len = scanner.nextInt();
//        int[] height = new int[len];
//        int[] weight = new int[len];
//        int[] index = new int[len];
//        for (int i = 0; i < len; i++) {
//            height[i] = scanner.nextInt();
//            index[i] = i+1;
//        }
//        for (int i = 0; i < len; i++) {
//            weight[i] = scanner.nextInt();
//        }
        for (int i = 0; i < 100; i++) {
            System.out.print((i+1)+" ");
        }
        System.out.println();

        int len = 4;
        int[] height = new int[]{100,100,120,130};
        int[] weight = new int[]{40,30,60,50};
        int[] index = new int[]{1,2,3,4};


        for (int i = 0; i < len-1; i++) {
            for (int j = i+1; j < len; j++) {
                if(height[i] > height[j] || (height[i] == height[j] && weight[i] > weight[j] )){
                    // swap height
                    int temp = height[i];
                    height[i] = height[j];
                    height[j] = temp;

                    // swap weight
                    temp = weight[i];
                    weight[i] = weight[j];
                    weight[j] = temp;

                    // swap index
                    temp = index[i];
                    index[i] = index[j];
                    index[j] = temp;
                }
            }
        }

        for (int i = 0; i < len; i++) {
            System.out.print(index[i]+" ");
        }


    }







    public static void main_winCount(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int len = scanner.nextInt();
        int[] nums = new int[len];
        for (int i = 0; i < len; i++) {
            nums[i] = scanner.nextInt();
        }
        int winSize = scanner.nextInt();

        if(winSize > len){
            winSize = len;
        }
        int max = 0;
        for (int i = 0; i <= len - winSize; i++) {
            int tempMax = 0;
            for (int j = i; j < winSize+i; j++) {
                tempMax+=nums[j];
            }
            max = tempMax > max ? tempMax : max;
        }
        System.out.println(max);
    }


}
