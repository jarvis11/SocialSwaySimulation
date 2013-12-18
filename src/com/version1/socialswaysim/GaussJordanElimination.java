package com.version1.socialswaysim;

import Jama.Matrix;

/**
 * Created by bhaskarravi on 12/9/13.
 */
public class GaussJordanElimination {


    public static void swap(double[][] gaussJordanMatrix, int row1, int row2){
        double temp;

        for(int i = 0; i < gaussJordanMatrix.length; i++){
            temp = gaussJordanMatrix[row1][i];
            gaussJordanMatrix[row1][i] = gaussJordanMatrix[row2][i];
            gaussJordanMatrix[row2][i] = temp;

        }

    }

    public static void divide(double[][] gaussJordanMatrix, int row, double diviser){
        for(int i = 0; i < gaussJordanMatrix.length; i++){
            gaussJordanMatrix[row][i] = gaussJordanMatrix[row][i] / diviser;
        }

    }

    public static void multiplyAndSubtract(double[][] gaussJordanMatrix, int row, int col){
        for(int i = 0; i < gaussJordanMatrix.length; i++)      // start from second index in row because first index is presumably 1 from divide()
        {
            if(i != row && gaussJordanMatrix[i][col] != 0){
                for(int j = 0; j < gaussJordanMatrix[0].length; j++){
                    gaussJordanMatrix[i][j] = gaussJordanMatrix[i][j] - gaussJordanMatrix[i][col]*gaussJordanMatrix[row][j];
                }
            }




        }

    }

    public static void gaussianElimination(double[][] gaussJordanMatrix){
        int row = 0;
        int col = 0;
        int currentRow = 0;
        int currentCol = 0;

        while(row < gaussJordanMatrix.length && col < gaussJordanMatrix[0].length){

            //find the first non zero entry in our col
            while(currentRow < gaussJordanMatrix.length && gaussJordanMatrix[currentRow][currentCol] == 0){
                currentRow ++;
            }

            if(currentRow != row){
                swap(gaussJordanMatrix, currentRow, row);
            }

            if(gaussJordanMatrix[row][col] != 1){
                divide(gaussJordanMatrix, row, gaussJordanMatrix[row][col]);
            }

            multiplyAndSubtract(gaussJordanMatrix, row, col);
            row++;

        }
    }

    public static void main(String[] args) {
        double[][] X = new double[5][4];

        for(int m = 0; m <= 4; m++){
            for(int n = 0; n <= 3; n++){
                X[m][n] = m+n;
                System.out.print(X[m][n] + " ");
            }
            System.out.println();
        }

        System.out.print(X);
        gaussianElimination(X);

        System.out.print(X);

    }














}
