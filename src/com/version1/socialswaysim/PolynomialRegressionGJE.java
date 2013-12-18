package com.version1.socialswaysim;



import Jama.LUDecomposition;
import Jama.Matrix;

/**
 * Created by bhaskarravi on 12/9/13.
 */


public class PolynomialRegressionGJE {

    private int degree;
    private Matrix coeffs;
    private double SSE;
    private double SST;

    public PolynomialRegressionGJE(double[] x, double[] y, int degree){

        this.degree = degree;
       // n = x.length;

        double[][] gaussJordanArray = new double[degree][degree + 1];
        Matrix gaussJordanMatrix = new Matrix(degree, degree + 1);

        for(int row = 0; row < degree; row++){
            for(int col = 0; col < degree + 1; col++){
                for (int i = 0; i < x.length; i++){
                    gaussJordanArray[row][col] += Math.pow(x[i], row + col);
                    gaussJordanMatrix.set(row, col, Math.pow(x[i], row + col));
                }
            }
        }







        //A = new Matrix(gaussJordanMatrix);











    }


}
