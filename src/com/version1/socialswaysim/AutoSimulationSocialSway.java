package com.version1.socialswaysim;

import java.util.ArrayList;

/**
 * Created by bhaskarravi on 12/13/13.
 */
public class AutoSimulationSocialSway {

    public static void main(String[] args) {



        //INITIALIZE TRAINING DATASET
        ArrayList<Bid> bidDataTRAIN = new ArrayList<Bid>();
        ArrayList<AdGroup> adGroupDataTRAIN = new ArrayList<AdGroup>();
        DataParser objTRAIN = new DataParser();
        objTRAIN.readData(bidDataTRAIN, adGroupDataTRAIN, "/Users/bhaskarravi/Desktop/SimulationDBs/Autos_BiddingSim_TRAIN.csv");

        //INITIALIZING TESTING DATA
        ArrayList<Bid> bidDataTEST = new ArrayList<Bid>();
        ArrayList<AdGroup> adGroupDataTEST = new ArrayList<AdGroup>();
        DataParser objTEST = new DataParser();
        objTEST.readData(bidDataTEST, adGroupDataTEST, "/Users/bhaskarravi/Desktop/SimulationDBs/Autos_BiddingSim_TEST.csv");

        //INITIALIZE LIVE VECTORS -- these are the vectors that our regression will be inputted into our regression
        double[] time = new double[adGroupDataTEST.size()];
        double[] impressions = new double[adGroupDataTEST.size()];
        double[] clicks = new double[adGroupDataTEST.size()];
        double[] likes = new double[adGroupDataTEST.size()];

        //INITIALIZE BIDDING VECTORS
        double[] costPerAction = new double[bidDataTEST.size()];
        double[] costPerClick = new double[bidDataTEST.size()];
        double[] costPerThousand = new double[bidDataTEST.size()];

        //FILL LIVE VECTORS WITH PAST TRAINING DATA AND FILL BIDDING VECTORS WITH OUR BID DATA
        for(int count = 0; count < adGroupDataTEST.size(); count ++){

            //fill test vectors with training data
            time[count] = adGroupDataTRAIN.get(count).getTimeStamp();
            impressions[count] = adGroupDataTRAIN.get(count).getUniqueImpressions();
            clicks[count] = adGroupDataTRAIN.get(count).getUniqueClicks();
            likes[count] = adGroupDataTRAIN.get(count).getLikes();

            //fill bidding arrays with actual test data....not training of course
            costPerAction[count] = bidDataTEST.get(count).getCPA();
            costPerClick[count] = bidDataTEST.get(count).getCPC();
            costPerThousand[count] = bidDataTEST.get(count).getCPM();
        }

        //BUGCHECK -- EVERYTHING ABOVE IS WORKING CORRECTLY


        /**
         * ACTUAL CAMPAIGN STARTS NOW
         */

        //Header
        System.out.println("CAMPAIGN VERTICAL: Automotive");
        System.out.println("CAMPAIGN GOAL: 3000 Page Likes");
        System.out.println("ADVERTISEMENT TYPE: Page Post");
        System.out.println("BID TYPE: CPA, CPC, CPM");
        System.out.println("AD ACCOUNT: " + adGroupDataTEST.get(0).getAdAccountID());
        System.out.println("AD CAMPAIGN: " + adGroupDataTEST.get(0).getAdCampaignID());
        System.out.println("ADVERTISEMENT: " + adGroupDataTEST.get(0).getAdvertisementID());
        System.out.println("AD GROUP: " + adGroupDataTEST.get(0).getAdGroupID());
        System.out.println("==================================");
        System.out.println();

        System.out.printf("%-10s", "time");
        System.out.printf("%-10s", "CPM");
        System.out.printf("%-10s", "CPC");
        System.out.printf("%-10s", "CPA");
        System.out.printf("%-15s", "Impressions");
        System.out.printf("%-10s", "Clicks");
        System.out.printf("%-10s", "Likes");
        System.out.printf("%10s\n", "Hourly $");

        //INITIALIZE CAMPAIGN VARIABLES

        double totalCost = 0;   //Total Cost Incurred by the Campaign
        int totalLikes = 0;     //Total Likes Accrued by Campaign
        int likesGoal = 2500;   //Campaign Goal
        int totalImpressions = 0; //Total Impressions Accrued by Campaign
        int totalClicks = 0;    //Total Clicks Accrued by Campaign
        double totalCPM = 0;    //Total CPM Cost of Campaign
        double totalCPC = 0;    //Total CPC Cost of Campaign
        double totalCPA = 0;    //Total CPA Cost of Campaign
        double hourlyCost = 0;  //Cost per Hour
        int hour = 0;           //TIMESTAMP + 1

        //RUN ACTUAL CAMPAIGN

        //End Campaign if Goal is Exceeded (Goal can either be budgetary or action oriented)
        while(totalLikes < likesGoal){

            //Start the Campaign as you would hour 1
            if(hour == 0){

                //ACTUAL PLACEMENT OF BID
                time[hour] = adGroupDataTEST.get(hour).getTimeStamp();
                impressions[hour] = adGroupDataTEST.get(hour).getUniqueImpressions();
                clicks[hour] = adGroupDataTEST.get(hour).getUniqueClicks();
                likes[hour] = adGroupDataTEST.get(hour).getLikes();

                //If the hour is zero, just bid naturally...suck it up and pay the bills
                totalCPM += costPerThousand[hour];
                totalCPC += costPerClick[hour];
                totalCPA += costPerAction[hour];
                //hourlyCost = Math.round(bidDataTEST.get(hour).calculateCost(adGroupDataTEST.get(hour)) * 100.0) / 100.0;
                hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) + (costPerClick[hour]*clicks[hour]) + (costPerAction[hour]*likes[hour]);
                totalCost += hourlyCost;
                //UPDATE YOUR STATS
                totalImpressions += impressions[hour];
                totalClicks += clicks[hour];
                totalLikes += likes[hour];



                //PRINT RESULTS
                System.out.printf("%-10.0f", time[hour]);
                System.out.printf("%-10.2f", costPerThousand[hour]);
                System.out.printf("%-10.2f", costPerClick[hour]);
                System.out.printf("%-10.2f", costPerAction[hour]);
                System.out.printf("%-15.0f", impressions[hour]);
                System.out.printf("%-10.0f", clicks[hour]);
                System.out.printf("%-10.0f", likes[hour]);
                System.out.printf("%10.2f\n", hourlyCost);





            }
            else{

                //PERFORM REGRESSION AND CHECKS AS TO HOW TO BID -- CREATE AND FIT REGRESSION
                //Impressions Regression
                int degreeImpressions = 0;
                double r2final = -20000000.0;
                PolynomialRegressionMoorePenrose impressionsReg = new PolynomialRegressionMoorePenrose(time, impressions, degreeImpressions);
                while(impressionsReg.getR2() > r2final){
                    r2final = impressionsReg.getR2();
                    degreeImpressions++;
                    impressionsReg = new PolynomialRegressionMoorePenrose(time, impressions, degreeImpressions);
                }
                //Clicks Regression
                int degreeClicks = 0;
                r2final = -20000000.0;
                PolynomialRegressionMoorePenrose clicksReg = new PolynomialRegressionMoorePenrose(time, clicks, degreeClicks);
                while(clicksReg.getR2() > r2final){
                    r2final = clicksReg.getR2();
                    degreeClicks++;
                    clicksReg = new PolynomialRegressionMoorePenrose(time, clicks, degreeClicks);
                }
                //Likes Regression
                int degreeLikes = 0;
                r2final = -20000000.0;
                PolynomialRegressionMoorePenrose likesReg = new PolynomialRegressionMoorePenrose(time, likes, degreeLikes);
                while(likesReg.getR2() > r2final){
                    r2final = likesReg.getR2();
                    degreeLikes++;
                    likesReg = new PolynomialRegressionMoorePenrose(time, likes, degreeLikes);
                }




                //Logic
                    //If our # of likes are projected to go up in the current bidding hour, then start to bid CPA
                    //Otherwise bid whatever is projected to be less, the CPC, or the CPM cost

                //CHECK IF REG PREDICTS A SUPERSTAR
                if(likesReg.predict(hour+1) > 25 && likes[hour - 1] > 6){ //6 is the mean # of likes for the training set, 27 is the average of the high dist.
                    //Bid CPA AND THEN ADJUST THE DATASET WITH NEW VALUES
                    time[hour] = adGroupDataTEST.get(hour).getTimeStamp();
                    impressions[hour] = adGroupDataTEST.get(hour).getUniqueImpressions();
                    clicks[hour] = adGroupDataTEST.get(hour).getUniqueClicks();
                    likes[hour] = adGroupDataTEST.get(hour).getLikes();

                    //Pay the bills
                    totalCPM += costPerThousand[hour];
                    totalCPC += costPerClick[hour];
                    totalCPA += costPerAction[hour];
                    //hourlyCost = Math.round(bidDataTEST.get(hour).calculateCost(adGroupDataTEST.get(hour)) * 100.0) / 100.0;
                    hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) + (costPerClick[hour]*clicks[hour]) + (costPerAction[hour]*likes[hour]);
                    totalCost += hourlyCost;


                    //UPDATE YOUR STATS
                    totalImpressions += impressions[hour];
                    totalClicks += clicks[hour];
                    totalLikes += likes[hour];

                    //PRINT RESULTS
                    System.out.printf("%-10.0f", time[hour]);
                    System.out.printf("%-10.2f", costPerThousand[hour]);
                    System.out.printf("%-10.2f", costPerClick[hour]);
                    System.out.printf("%-10.2f", costPerAction[hour]);
                    System.out.printf("%-15.0f", impressions[hour]);
                    System.out.printf("%-10.0f", clicks[hour]);
                    System.out.printf("%-10.0f", likes[hour]);
                    System.out.printf("%10.2f\n", hourlyCost);

                }
                else{

                    //LIKES IS NOT A FACTOR, SO NOW THE GOAL TURNS INTO WHETHER WE SHOULD BID ON A CPM OR CPC BASIS
                    //LET'S DO WHATEVER IS CHEAPTER DUH

                    if((impressionsReg.predict(hour+1) * costPerThousand[hour]) / 1000 > (clicksReg.predict(hour+1) * costPerClick[hour])){
                        //BID CPC NOT CPM
                        time[hour] = adGroupDataTEST.get(hour).getTimeStamp();
                        impressions[hour] = adGroupDataTEST.get(hour).getUniqueImpressions();
                        clicks[hour] = adGroupDataTEST.get(hour).getUniqueClicks();
                        likes[hour] = adGroupDataTEST.get(hour).getLikes();
                        //ADJUST YOUR LIKES FOR NOT BIDDING CPA
                        likes[hour] = likes[hour] * .8;

                        //SET CPA AND CPM FOR HOUR TO 0
                        costPerThousand[hour] = 0;
                        costPerAction[hour] = 0;

                        //PAY THE BILLS
                        totalCPM += costPerThousand[hour];
                        totalCPC += costPerClick[hour];
                        totalCPA += costPerAction[hour];
                        //hourlyCost = Math.round(bidDataTEST.get(hour).calculateCost(adGroupDataTEST.get(hour)) * 100.0) / 100.0;
                        hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) + (costPerClick[hour]*clicks[hour]) + (costPerAction[hour]*likes[hour]);
                        totalCost += hourlyCost;


                        //UPDATE THOSE STATTSSSS
                        totalImpressions += impressions[hour];
                        totalClicks += clicks[hour];
                        totalLikes += likes[hour];

                        //PRINT RESULTS
                        System.out.printf("%-10.0f", time[hour]);
                        System.out.printf("%-10.2f", costPerThousand[hour]);
                        System.out.printf("%-10.2f", costPerClick[hour]);
                        System.out.printf("%-10.2f", costPerAction[hour]);
                        System.out.printf("%-15.0f", impressions[hour]);
                        System.out.printf("%-10.0f", clicks[hour]);
                        System.out.printf("%-10.0f", likes[hour]);
                        System.out.printf("%10.2f\n", hourlyCost);

                    }
                    else{

                        //ELSE JUST BID CPM

                        time[hour] = adGroupDataTEST.get(hour).getTimeStamp();
                        impressions[hour] = adGroupDataTEST.get(hour).getUniqueImpressions();
                        clicks[hour] = adGroupDataTEST.get(hour).getUniqueClicks();
                        likes[hour] = adGroupDataTEST.get(hour).getLikes();
                        //ADJUST YOUR LIKES FOR NOT BIDDING CPA
                        likes[hour] = likes[hour] * .8;

                        //SET CPA AND CPM FOR HOUR TO 0
                        costPerClick[hour] = 0;
                        costPerAction[hour] = 0;

                        //PAY THE BILLS
                        totalCPM += costPerThousand[hour];
                        totalCPC += costPerClick[hour];
                        totalCPA += costPerAction[hour];
                        //hourlyCost = Math.round(bidDataTEST.get(hour).calculateCost(adGroupDataTEST.get(hour)) * 100.0) / 100.0;
                        hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) + (costPerClick[hour]*clicks[hour]) + (costPerAction[hour]*likes[hour]);
                        totalCost += hourlyCost;

                        //UPDATE THOSE STATTSSSS
                        totalImpressions += impressions[hour];
                        totalClicks += clicks[hour];
                        totalLikes += likes[hour];

                        //PRINT RESULTS
                        System.out.printf("%-10.0f", time[hour]);
                        System.out.printf("%-10.2f", costPerThousand[hour]);
                        System.out.printf("%-10.2f", costPerClick[hour]);
                        System.out.printf("%-10.2f", costPerAction[hour]);
                        System.out.printf("%-15.0f", impressions[hour]);
                        System.out.printf("%-10.0f", clicks[hour]);
                        System.out.printf("%-10.0f", likes[hour]);
                        System.out.printf("%10.2f\n", hourlyCost);


                    }



                }

            }

            //BUGCHECK -- ARRAYS ARE INITIALIZING PROPERLY
            hour ++;

        }



        System.out.println();
        System.out.println("==================================");
        System.out.println("END CAMPAIGN");
        System.out.println();
        System.out.println("CAMPAIGN SUMMARY: ");
        System.out.println();


        System.out.println("TOTAL COST: " + Math.round(totalCost * 100.0) / 100.0);
        System.out.println("TOTAL IMPRESSIONS: " + totalImpressions);
        System.out.println("TOTAL CLICKS: " + totalClicks);

        System.out.println("TOTAL LIKES: " + totalLikes);
        System.out.println("AVERAGE CPM: " + Math.round(totalCPM / hour * 100.0) / 100.0 );
        System.out.println("AVERAGE CPC: " + Math.round(totalCPC / hour * 100.0) / 100.0 );
        System.out.println("AVERAGE CPA: " + Math.round(totalCPA / hour * 100.0) / 100.0 );
        System.out.println("CTR: " +  Math.round((totalClicks / (double) totalImpressions * 100)*100.0)/100.0 + "%" );



    }











}
