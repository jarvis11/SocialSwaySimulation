package com.version1.socialswaysim;

import java.util.ArrayList;

/**
* Created by bhaskarravi on 12/10/13.
*
* Simulation of the MoorePenrose method for fitting a polynomial regression
* on a Facebook dataset.
*
* Hypothesis: Moore-Penrose is an appropriate method to try an approximate Ads API data
*
*
*/
public class MoorePenroseSimulationTraining {

    public static void main(String[] args) {


        //Initialize both bid and adgroup datasets
        ArrayList<Bid> bidData = new ArrayList<Bid>();
        ArrayList<AdGroup> adGroupData = new ArrayList<AdGroup>();

        //Parse data

        DataParser obj = new DataParser();
        obj.readData(bidData, adGroupData, "/Users/bhaskarravi/Desktop/SimulationDBs/Autos_BiddingSim_TRAIN.csv");


        //Init Campaign Vars
        double[] time = new double[adGroupData.size()];
        double[] impressions = new double[adGroupData.size()];
        double[] clicks = new double[adGroupData.size()];
        double[] likes = new double[adGroupData.size()];
        double[] costPerAction = new double[bidData.size()];
        double[] costPerClick = new double[bidData.size()];
        double[] costPerThousand = new double[bidData.size()];

        //Init Arrays

        for(int count = 0; count < adGroupData.size(); count ++){

            time[count] = adGroupData.get(count).getTimeStamp();
            impressions[count] = adGroupData.get(count).getUniqueImpressions();
            clicks[count] = adGroupData.get(count).getUniqueClicks();
            likes[count] = adGroupData.get(count).getLikes();

            //Bid array same size as our adgroup array
            costPerAction[count] = bidData.get(count).getCPA();
            costPerClick[count] = bidData.get(count).getCPC();
            costPerThousand[count] = bidData.get(count).getCPM();
        }


        //FOR TRAINING PURPOSES ONLY
        //calculate based off r2
        double r2final = 0.0;

        int degreeClicks = 0;
        int degreeLikes = 0;
        int degreeLikesMultiVar = 0;

        PolynomialRegressionMoorePenrose clicksReg = new PolynomialRegressionMoorePenrose(time, clicks, degreeClicks);
        PolynomialRegressionMoorePenrose likesReg = new PolynomialRegressionMoorePenrose(time, likes, degreeLikes);
        PolynomialRegressionMoorePenrose likesRegMultiVar = new PolynomialRegressionMoorePenrose(time, clicks, likes, degreeLikesMultiVar);


        //clicks regression
        while(clicksReg.getR2() > r2final){
            r2final = clicksReg.getR2();
            degreeClicks++;

            clicksReg = new PolynomialRegressionMoorePenrose(time, clicks, degreeClicks);

        }

        r2final = -2000000.0;
        //likes regression
        while(likesReg.getR2() > r2final){
            r2final = likesReg.getR2();
            degreeLikes++;

            likesReg = new PolynomialRegressionMoorePenrose(time, likes, degreeLikes);

        }



        r2final = -2000000.0;
        while(likesRegMultiVar.getR2() > r2final){
            r2final = likesRegMultiVar.getR2();
            degreeLikesMultiVar++;

            likesRegMultiVar = new PolynomialRegressionMoorePenrose(time, clicks, likes, degreeLikesMultiVar);
        }





        System.out.println(clicksReg.getDegree());
        System.out.println(clicksReg);
        System.out.println();

        System.out.println(likesReg.getDegree());
        System.out.println(likesReg);
        System.out.println(likesReg.predict(180));
        System.out.println();


        System.out.println(likesRegMultiVar.getDegree());
        System.out.println(likesRegMultiVar.getR2());

        System.out.println(likesRegMultiVar.predict(180, 172));
        System.out.println(likesRegMultiVar.predict(189));
//        System.out.println(likesRegMultiVar.print2DMatrix());








    }
}
