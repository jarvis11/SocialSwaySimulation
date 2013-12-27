package com.version1.socialswaysim;

import Jama.Matrix;


/**
 * Created by bhaskarravi on 12/9/13.
 * Confidential property of SocialSway. All rights reserved.
 *
 * The following class uses the Moore-Penrose pseudo inverse to calculate the coefficients of a polynomial regression
 * to the nth degree. This polynomial regression can then be trained to fit Facebook bidding data in order to accurately
 * predict bid type and amount during a given time T for a Facebook AdGroup.
*/


public class PolynomialRegressionMoorePenrose {

    private int degree; //degree of Polynomial
    private Matrix coeffs; //Matrix of Polynomial Coefficients
    private double SSE; //Sum of Squares due to error
    private double SST; //Total error sum
    private double error; //Non r2 standard error

    /**
     * Method: PolynomialRegressionMoorePenrose
     * EXECUTION OF A POLYNOMIAL REGRESSION USING THE MOORE PENROSE METHOD
     * finds coefficient matrix for the appropriate data set using the formula:
     * [coeffs] = PseudoInv(x) * y
     * @param x - vector of the independent variable
     * @param y - vector of the dependent variable with intent to predict
     * @param degree - degree of the fitted polynomial
     */

    public PolynomialRegressionMoorePenrose(double[] x, double[] y, int degree) {

        this.degree = degree;

        //Create matrices out of x and y vectors using Jama
        Matrix xM = createXMatrix(x);
        Matrix yM = createYMatrix(y);

        //MoorePenrose formula: PseudoInv(x) * y = w, w being a vector of weights
        Matrix A = getPseudoInverse(xM);
        coeffs = A.times(yM);

        //CALCULATE VARIABLES NEEDED TO DETERMINE R2
        //FIT CALCULATION USING COEFF OF DETERMINATION
        double sum = 0.0;
        for(int i = 0; i < x.length; i++){
            sum += y[i];
        }

        double mean = sum / x.length;

        for(int i = 0; i < x.length; i++){
            double dev = y[i] - mean;
            SST += dev*dev; //Calculate total total squared deviation from mean
        }

        //Sum of squares due to error calculation
        Matrix residuals = xM.times(coeffs).minus(yM);
        SSE = residuals.norm2() * residuals.norm2();

        //error calculation using standard in-sample error for validation purposes
        for(int i = 0; i < x.length; i++){
            error += Math.pow((y[i] - predict(x[i])),2) / x.length;
        }
    }

    /**
     * Method: createXMatrix
     * converts x-data array into matrix form
     * @param x - vector of the independent variable
     * @return - Matrix representation of the independent variable
     */

    public Matrix createXMatrix(double[] x){

        //INIT X MATRIX (a row for each x value X the number of terms in the polynomial)
        Matrix xMatrix = new Matrix(x.length, degree + 1);

        //comb through all x values
        for(int i = 0; i < x.length; i++){

            //for each x value create a point vector
            double[] pointVector = getXPointVector(x[i]);

            for(int j = 0; j < pointVector.length; j++){
                //distribute each x vector into the respective row of the xMatrix
                xMatrix.set(i, j, pointVector[j]);

            }
        }

        //matrix of each x value taken out to polynomial degree in the form 1, x, x^2, ...
        return xMatrix;
    }

    /**
     * Method: createYMatrix
     * converts y-data array into matrix form for compatibility with Jama
     * @param y - vector of the dependent variable with intent to predict
     * @return - Matrix representation of the dependent variable
     */

    public Matrix createYMatrix(double[] y){

        // n x 1 Matrix of all y values
        Matrix yMatrix = new Matrix(y.length, 1);
        for(int i = 0; i < y.length; i++){
            yMatrix.set(i, 0, y[i]);
        }
        return yMatrix;
    }

    /**
     * Method: getPseudoInverse
     * finds the pseudoinverse of a given Matrix x
     * @param x - Matrix who's pseudoinverse is to be calculated
     * @return - The pseudoinverse of X
     */


    //Finds and returns pseudoinverse of a given matrix x
    public Matrix getPseudoInverse(Matrix x){
        //compute and return pseudo-inverse of input matrix x

        if(x.getRowDimension() == x.getColumnDimension()){
            // if x is a square matrix, Jama's inverse method takes the standard inverse of the Matrix
            // return the formula to calculate the pseudoinverse of the matrix
            return (((x.transpose()).times(x)).inverse()).times(x.transpose());
        }
        else{
            //if x is not a square matrix, Jama's inverse method returns the pseudoinverse of the Matrix
            return x.inverse();
        }
    }


    /**
     * Method: getXPointVector
     * returns the x vector at a given point x
     * @param x - A single point x from the set x[]
     * @return - Weighted vector at the given point x
     */
    public double[] getXPointVector(double x){
        double[] xVector = new double[degree + 1];

        for (int i = 0; i <= degree; i++){
            //x vector takes the form of [x^0, x^1, ..., x^degree]
            xVector[i] = Math.pow(x, i);
        }

        return xVector;
    }

    /**
     * Method: getCoeffs
     * returns a value of the coefficient matrix formed through the MP regression
     * @param index - index of coefficient matrix
     * @return - coefficient at index index
     */

    public double getCoeffs(int index){
        return coeffs.get(index,0);
    }

    /**
     * Method: getDegree
     * @return - degree of polynomial
     */

    public int getDegree(){
        return degree;
    }

    /**
     * Method: predict
     * predicts the y value for a given x value
     * @param x - point at which to predict value of polynomial
     * @return - value y at a given point x
     */

    public double predict(double x){
        double y = 0.0;

        //calculate xVector at a given point x
        double[] xVector =  getXPointVector(x);

        //plug x and corresponding weights into polynomial to find y
        for(int j = degree; j >= 0; j--){
            y += getCoeffs(j) * xVector[j];
        }

        return y;
    }

    /**
     * Method: getR2
     * calculates Coefficient of Determination to determine polynomial fit
     * @return - r2 value
     */

    public double getR2() {
        return 1.0 - SSE/SST;
    }

    /**
     * Method: getError
     * @return - standard error of polynomial
     */

    public double getError(){
        return error;
    }


    /**
     * Method: toString
     * Override of java toString method in order to print out polynomial
     */

    public String toString() {
        String s = "";
        int j = degree;

        while(Math.abs(getCoeffs(j)) < 1E-5){
            j--;
        }

        for(j = degree; j >=0; j--){
            if(j== 0) {
                s += String.format("%.2f ", getCoeffs(j));
            }
            else if (j==1){
                s += String.format("%.2f N + ", getCoeffs(j));
            }
            else {
                s+= String.format("%.2f N^%d + ", getCoeffs(j), j);
            }

        }

        return s+ "  (R^2 = " + String.format("%.3f", getR2()) + ")";
    }


    /**
     *
     *
     *
     * MULTI DIMENSIONAL POLYNOMIAL REGRESSION
     * The following section adapts the:
     *
     * getXPointVector
     * createXMatrix
     * predict
     * PolynomialRegressionMoorePenrose
     *
     * methods in order to account for two independent variables entering the regression.
     *
     *
     * METHODS STILL IN ALPHA STAGE -- NOT READY FOR DEPLOYMENT
     *
     */


    /**
     * Method: PolynomialRegressionMoorePenrose
     * @param x1 - first set of independent variables
     * @param x2 - second set of independent variables
     * @param y - vector of the dependent variable with intent to predict
     * @param degree - degree of the fitted polynomial
     *
     * Multi-dimension version of regression found above, which can take into account two
     * independent vectors in order to gain a 3-dimensional polynomial fit for y.
     */

    public PolynomialRegressionMoorePenrose(double[] x1, double[]x2, double[] y, int degree) {

        this.degree = degree;

        //Create matrices out of x and y vectors using Jama
        //xMatrix now takes into account 2 x vectors
        Matrix xM = createXMatrix(x1, x2);
        Matrix yM = createYMatrix(y);


        //MoorePenrose formula: PseudoInv(x) * y = w, w being a vector of weights
        Matrix A = getPseudoInverse(xM);
        coeffs = A.times(yM);


        //FIT CALCULATION USING COEFF OF DETERMINATION
        double sum = 0.0;
        for(int i = 0; i < x1.length; i++){
            sum += y[i];
        }

        double mean = sum / x1.length;

        for(int i = 0; i < x1.length; i++){
            double dev = y[i] - mean;
            SST += dev*dev; //Calculate total total squared deviation from mean
        }

        //Sum of squares due to error calculation
        Matrix residuals = xM.times(coeffs).minus(yM);
        SSE = residuals.norm2() * residuals.norm2();

        //error calculation using standard in-sample error for validation purposes
        for(int i = 0; i < x1.length; i++){

            //error takes into account prediction with both x vectors
            //predict y based on x: {x1, x2}
            error += Math.pow((y[i] - predict(x1[i], x2[i])),2) / x1.length;
        }


    }

    /**
     * Method: getXPointVector
     * @param x1 - independent variable x1 at point: (x1, x2)
     * @param x2 - independent variable x2 at point: (x1, x2)
     * @return - returns a vector containing value of points at each index of the multidimensional polynomial
     * polynomial takes the form: a + bx1 + cx2 + dx1x2 + ex1^2 + fx2^2 ...
     *
     * Modification of getXPointVector method to create a polynomial with 3 dimensional structure
     */

    public double[] getXPointVector(double x1, double x2){
        double[] xVector = new double[(degree+1)*(degree+2) / 2];
        int i;
        int j;
        int index;

        //loop determining all independent x1 terms and adding them to vector
        for(index = 0; index <= degree; index++) {
            xVector[index] = Math.pow(x1, index);
        }

        //loop determining all independent x2 terms and adding them to vector
        for(j = 1; j <= degree; j++) {
            xVector[index] = Math.pow(x2, j);
            index++;

        }

        //loop calculating all dependent x1 and x2 combinations
        for(i = 1; i < degree; i++)
        {
            for(j = 1; j < degree; j++) {
                if(i+j <= degree){
                    xVector[index] = Math.pow(x1, i) * Math.pow(x2, j);
                    index++;
                }
            }
        }
        return xVector;
    }

    /**
     * Method: createXMatrix
     * @param x1 - first set of independent variables
     * @param x2 - second set of independent variables
     * @return - Matrix representation of the independent variable set
     *
     * Modification of createXMethod to create a matrix off two x-sets
     */

    public Matrix createXMatrix(double[] x1, double[] x2){

        Matrix xMatrix = new Matrix(x1.length, (degree+1)*(degree+2) / 2);

        //comb through all x values
        for(int i = 0; i < x1.length; i++){

            //for each x value create a point vector
            double[] pointVector = getXPointVector(x1[i], x2[i]);

            for(int j = 0; j < pointVector.length; j++){
                //distribute each x vector into the respective row of the xMatrix
                xMatrix.set(i, j, pointVector[j]);
            }
        }
        return xMatrix;
    }


    /**
     * Method: predict
     * @param x1 - independent variable x1 at point: (x1, x2)
     * @param x2 - independent variable x2 at point: (x1, x2)
     * @return - prediction y at point (x1, x2)
     *
     * Modification of prediction method to take into account multiple dimensions
     */


    public double predict(double x1, double x2){
        double y = 0.0;

        //calculate xVector at a given point x
        double[] xVector =  getXPointVector(x1, x2);

        //plug x and corresponding weights into polynomial to find y
        for(int j = degree; j >= 0; j--){
            y += getCoeffs(j) * xVector[j];
        }

        return y;
    }

    /**
     * Method: print2DMatrix
     * @return - returns a string containing printout of polynomial
     *
     */

    public String print2DMatrix() {
        String s = "";
        int j = degree;
        int index;

        while(Math.abs(getCoeffs(j)) < 1E-5){
            j--;
        }

        for(index = 1; index <= degree; index++){
            if (index==1){
                s += String.format("%.2f N1 + ", getCoeffs(index));
            }
            else {
                s+= String.format("%.2f N1^%d + ", getCoeffs(index), index);
            }

        }

        for(j = 1; j <= degree; j++)
        {
            if(j==1){
                s += String.format("%.2f N2 + ", getCoeffs(index));
            }
            else {
                s+= String.format("%.2f N2^%d + ", getCoeffs(index), j);
            }
            index++;
        }

        for(int i = 1; i < degree; i++ ){
            for(j = 1; j < degree; j++ ){
                if(i+j <= degree){
                    if(i == 1 && j != 1){
                        s+= String.format("%.2f N1 N2^%d + ", getCoeffs(index), j);
                    }
                    else if(j == 1 && i != 1){
                        s+= String.format("%.2f N1^%d N2 + ", getCoeffs(index), i);
                    }
                    else if(i == 1 && j == 1){
                        s+= String.format("%.2f N1 N2 + ", getCoeffs(index));
                    }
                    else{
                        s+= String.format("%.2f N1^%d N2^%d + ", getCoeffs(index), i, j);
                    }

                    index++;
                }
            }
        }

        //Add constant to equation
        s += String.format("%.2f", getCoeffs(0));



        return s+ "  (R^2 = " + String.format("%.3f", getR2()) + ")";
    }

    /**
     *
     * FOR TESTING PURPOSES ONLY
     *
     */

    public static void main(String[] args) {
        double[] x1 = { 10, 20, 40, 80, 160, 200 };
        double[] x2 = {10, 200, 450, 800, 100, 2000};
        double[] y = { 100, 350, 1500, 6700, 20160, 40000 };
        double[] CTR = {0.1, 0.057, .027, .012, .00794, .005};
        PolynomialRegressionMoorePenrose regression = new PolynomialRegressionMoorePenrose(x1, x2,  y, 2);
        PolynomialRegressionMoorePenrose regression1D = new PolynomialRegressionMoorePenrose(x2, y, 3);
        PolynomialRegressionMoorePenrose regression3 = new PolynomialRegressionMoorePenrose(x1, y, CTR, 1);

        System.out.println(regression1D);
        System.out.println(regression3.print2DMatrix());
        System.out.println(regression.predict(40, 450));


//        System.out.println(regression3.print2DMatrix());
        System.out.println(regression3.predict(80, 6700));

        //System.out.println(regression3.print2DMatrix());
//        System.out.println(regression);
//        System.out.println(regression.getDegree());
//        System.out.println(regression.predict(200));

    }

}