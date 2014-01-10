package com.version1.socialswaysim;

import java.util.ArrayList;

/**
 * Created by bhaskarravi on 12/21/13.
 */
public class SocialSwayTelecomSimOCPM {

    public static void main(String[] args) {

        /**
         * INITIALIZE PARAMETERS
         */

        //INITIALIZING TESTING DATA
        ArrayList<Bid> bidDataTEST = new ArrayList<Bid>();
        ArrayList<AdGroup> adGroupDataTEST = new ArrayList<AdGroup>();
        DataParser objTEST = new DataParser();
        objTEST.readData(bidDataTEST, adGroupDataTEST, "/Users/bhaskarravi/Desktop/SimulationDBs/Telecom/TelecomBiddingSimABTest.csv");

        //INITIALIZE LIVE ARRAYLISTS
        ArrayList<Integer> timeList = new ArrayList<Integer>();
        ArrayList<Integer> impressionsList = new ArrayList<Integer>();
        ArrayList<Integer> clicksList = new ArrayList<Integer>();
        ArrayList<Double> likesList = new ArrayList<Double>();

        //INITIALIZE BIDDING VECTORS
        double[] medCPA = new double[bidDataTEST.size()];
        double[] highCPA = new double[bidDataTEST.size()];
        double[] lowCPA = new double[bidDataTEST.size()];
        double[] costPerClick = new double[bidDataTEST.size()];
        double[] costPerThousand = new double[bidDataTEST.size()];

        //FILL VECTORS WITH APPROPRIATE VALUES
        for(int count = 0; count < bidDataTEST.size(); count++){

            medCPA[count] = bidDataTEST.get(count).getMedCPA();
            highCPA[count] = bidDataTEST.get(count).getHighCPA();
            lowCPA[count] = bidDataTEST.get(count).getLowCPA();
            costPerClick[count] = bidDataTEST.get(count).getCPC();
            costPerThousand[count] = bidDataTEST.get(count).getCPM();

        }

        //Header
        System.out.println("CAMPAIGN VERTICAL: Telecom");
        System.out.println("CAMPAIGN GOAL: 6000 Page Likes");
        System.out.println("ADVERTISEMENT TYPE: Sponsored Story Page Post");
        System.out.println("BID TYPE: CPM, CPC, CPA, oCPM");
        System.out.println("AD ACCOUNT: " + adGroupDataTEST.get(0).getAdAccountID());
        System.out.println("AD CAMPAIGN: " + adGroupDataTEST.get(0).getAdCampaignID());
        System.out.println("ADVERTISEMENT: " + adGroupDataTEST.get(0).getAdvertisementID());
        System.out.println("AD GROUP: " + adGroupDataTEST.get(0).getAdGroupID());
        System.out.println("==================================");
        System.out.println();

        System.out.printf("%-10s", "Time");
        System.out.printf("%-10s", "oCPM");
        System.out.printf("%-10s", "CPC");
        System.out.printf("%-10s", "CPA");
        System.out.printf("%-15s", "Impressions");
        System.out.printf("%-10s", "Clicks");
        System.out.printf("%-10s", "Likes");
        System.out.printf("%10s\n", "Hourly $");

        /**
         * CAMPAIGN STARTS NOW
         */

        //INITIALIZE CAMPAIGN VARS
        double totalCost = 0;   //Total Cost Incurred by the Campaign
        double totalLikes = 0;     //Total Likes Accrued by Campaign
        int likesGoal = 6000;   //Campaign Goal
        int totalImpressions = 0; //Total Impressions Accrued by Campaign
        int totalClicks = 0;    //Total Clicks Accrued by Campaign
        double totalCPM = 0;    //Total CPM Cost of Campaign
        double totalCPC = 0;    //Total CPC Cost of Campaign
        double totalCPA = 0;    //Total CPA Cost of Campaign
        double hourlyCost;      //Cost per Hour
        int hour = 0;           //TIMESTAMP + 1

        //RUN ACTUAL CAMPAIGN

        while(totalLikes < likesGoal){

            //Start the Campaign as you would hour 1
            if(hour < 12){
                //MAKE YOUR BID (ADD APPROPRIATE VALUES TO YOUR ARRAYS)
                timeList.add(adGroupDataTEST.get(hour).getTimeStamp());
                impressionsList.add(adGroupDataTEST.get(hour).getUniqueImpressions());
                clicksList.add(adGroupDataTEST.get(hour).getUniqueClicks());
                likesList.add((double)adGroupDataTEST.get(hour).getLikes());

                //MAKING CPC BID 0 -- MAKING CPA AND CPM BID ONLY
                costPerClick[hour] = 0;
                costPerThousand[hour] = 0;

                //START BY BIDDING LOW
                medCPA[hour] = 0;
                highCPA[hour] = 0;


                //If the hour is zero, just bid naturally...suck it up and pay the bills
                totalCPM += costPerThousand[hour];
                totalCPC += costPerClick[hour];
                totalCPA += lowCPA[hour];
                hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) +
                        (costPerClick[hour]* clicksList.get(hour)) +
                        (lowCPA[hour]* (likesList.get(hour)*.5));

                totalCost += hourlyCost;

                //UPDATE YOUR STATS
                totalImpressions += impressionsList.get(hour);
                totalClicks += clicksList.get(hour);
                totalLikes += likesList.get(hour)*.5;


                //PRINT RESULTS
                System.out.printf("%-10.0f", (double)timeList.get(hour));
                System.out.printf("%-10.2f", costPerThousand[hour]);
                System.out.printf("%-10.2f", costPerClick[hour]);
                System.out.printf("%-10.2f", lowCPA[hour]);
                System.out.printf("%-15.0f", (double)impressionsList.get(hour));
                System.out.printf("%-10.0f", (double)clicksList.get(hour));
                System.out.printf("%-10.2f", likesList.get(hour) * .5);
                System.out.printf("%10.2f\n", hourlyCost);
            }
            else {
                //PERFORM REGRESSION

                //STEP 1: DISTRIBUTE ARRAYLISTS INTO ARRAYS
                double[] time = new double[timeList.size()];
                double[] impressions = new double[impressionsList.size()];
                double[] clicks = new double[clicksList.size()];
                double[] likes = new double[likesList.size()];

                for(int count = 0; count < timeList.size(); count++){
                    time[count] = timeList.get(count);
                    impressions[count] = impressionsList.get(count);
                    clicks[count] = clicksList.get(count);
                    likes[count] = likesList.get(count);
                }

                //NOW INITIATE REGRESSIONS
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



                //LOGIC
                //If like count is projected as very high, then bid full CPA PRICE
                //else bid on CPM or CPC, whichever is cheaper

                //CHECK IF REG PREDICTS A SUPERSTAR
                if(likesReg.predict(hour+1) > 50 && likes[hour - 1] > 20){ //use rolling averages instead of purely training set data
                    //MAKE CPA ONLY BID
                    timeList.add(adGroupDataTEST.get(hour).getTimeStamp());
                    impressionsList.add(adGroupDataTEST.get(hour).getUniqueImpressions());
                    clicksList.add(adGroupDataTEST.get(hour).getUniqueClicks());
                    likesList.add((double)adGroupDataTEST.get(hour).getLikes());

                    //CPM AND CPC BID GO TO ZERO
                    costPerThousand[hour] = 0;
                    costPerClick[hour] = 0;
                    lowCPA[hour] = 0;
                    medCPA[hour] = 0;

                    //PAY THE BILLS
                    totalCPM += costPerThousand[hour];
                    totalCPC += costPerClick[hour];
                    totalCPA += highCPA[hour];
                    hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) +
                            (costPerClick[hour]* clicksList.get(hour)) +
                            (highCPA[hour]* (likesList.get(hour)));
                    totalCost += hourlyCost;

                    //UPDATE YOUR STATS
                    totalImpressions += impressionsList.get(hour);
                    totalClicks += clicksList.get(hour);
                    totalLikes += likesList.get(hour);
                     //PRINT RESULTS
                    System.out.printf("%-10.0f", (double)timeList.get(hour));
                    System.out.printf("%-10.2f", costPerThousand[hour]);
                    System.out.printf("%-10.2f", costPerClick[hour]);
                    System.out.printf("%-10.2f", highCPA[hour]);
                    System.out.printf("%-15.0f", (double)impressionsList.get(hour));
                    System.out.printf("%-10.0f", (double)clicksList.get(hour));
                    System.out.printf("%-10.2f", likesList.get(hour));
                    System.out.printf("%10.2f\n", hourlyCost);
                }
                else{
                    //LIKES IS NOT A MAJOR FACTOR ANY LONGER, SO LETS GO INTO COST SAVING MODE
                    //IF YOU ARE GOING TO CAPTURE SOME LIKES BUT NOT A LOT BID MED

                    if(likesReg.predict(hour+1) < 50 && likes[hour - 1] > 10){

                        if(impressionsReg.predict(hour+1) * costPerThousand[hour] / 1000 >
                                likesReg.predict(hour+1)*medCPA[hour])
                        {
                            timeList.add(adGroupDataTEST.get(hour).getTimeStamp());
                            impressionsList.add(adGroupDataTEST.get(hour).getUniqueImpressions());
                            clicksList.add(adGroupDataTEST.get(hour).getUniqueClicks());
                            likesList.add((double)adGroupDataTEST.get(hour).getLikes());

                            //MED CPA
                            highCPA[hour] = 0;
                            lowCPA[hour] = 0;
                            costPerClick[hour] = 0;
                            costPerThousand[hour] = 0;

                            //PAY THE BILLS
                            totalCPM += costPerThousand[hour];
                            totalCPC += costPerClick[hour];
                            totalCPA += medCPA[hour];
                            hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) +
                                    (costPerClick[hour]* clicksList.get(hour)) +
                                    (medCPA[hour]* (likesList.get(hour)*.75));
                            totalCost += hourlyCost;

                            //UPDATE YOUR STATS
                            totalImpressions += impressionsList.get(hour);
                            totalClicks += clicksList.get(hour);
                            totalLikes += likesList.get(hour) * .75;


                            //PRINT RESULTS
                            System.out.printf("%-10.0f", (double)timeList.get(hour));
                            System.out.printf("%-10.2f", costPerThousand[hour]);
                            System.out.printf("%-10.2f", costPerClick[hour]);
                            System.out.printf("%-10.2f", medCPA[hour]);
                            System.out.printf("%-15.0f", (double)impressionsList.get(hour));
                            System.out.printf("%-10.0f", (double)clicksList.get(hour));
                            System.out.printf("%-10.2f",  likesList.get(hour) * .75);
                            System.out.printf("%10.2f\n", hourlyCost);

                        }
                        else{
                            //BID oCPM INSTEAD OF CPA
                            timeList.add(adGroupDataTEST.get(hour).getTimeStamp());
                            impressionsList.add(adGroupDataTEST.get(hour).getUniqueImpressions());
                            clicksList.add(adGroupDataTEST.get(hour).getUniqueClicks());
                            likesList.add((double)adGroupDataTEST.get(hour).getLikes());

                            //MED CPA
                            highCPA[hour] = 0;
                            lowCPA[hour] = 0;
                            medCPA[hour] = 0;
                            costPerClick[hour] = 0;

                            //PAY THE BILLS
                            totalCPM += costPerThousand[hour];
                            totalCPC += costPerClick[hour];
                            totalCPA += medCPA[hour];
                            hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) +
                                    (costPerClick[hour]* clicksList.get(hour)) +
                                    (medCPA[hour]* (likesList.get(hour)*.75));
                            totalCost += hourlyCost;

                            //UPDATE YOUR STATS
                            totalImpressions += impressionsList.get(hour);
                            totalClicks += clicksList.get(hour);
                            totalLikes += likesList.get(hour) * .9;


                            //PRINT RESULTS
                            System.out.printf("%-10.0f", (double)timeList.get(hour));
                            System.out.printf("%-10.2f", costPerThousand[hour]);
                            System.out.printf("%-10.2f", costPerClick[hour]);
                            System.out.printf("%-10.2f", medCPA[hour]);
                            System.out.printf("%-15.0f", (double)impressionsList.get(hour));
                            System.out.printf("%-10.0f", (double)clicksList.get(hour));
                            System.out.printf("%-10.2f",  likesList.get(hour) * .9);
                            System.out.printf("%10.2f\n", hourlyCost);

                        }


                    }
                    else{
                        //JUST BID low
                        timeList.add(adGroupDataTEST.get(hour).getTimeStamp());
                        impressionsList.add(adGroupDataTEST.get(hour).getUniqueImpressions());
                        clicksList.add(adGroupDataTEST.get(hour).getUniqueClicks());
                        likesList.add((double)adGroupDataTEST.get(hour).getLikes());

                        //LOW CPA ONLY
                        highCPA[hour] = 0;
                        medCPA[hour] = 0;
                        costPerClick[hour] = 0;
                        costPerThousand[hour] = 0;

                        //PAY THE BILLS
                        totalCPM += costPerThousand[hour];
                        totalCPC += costPerClick[hour];
                        totalCPA += lowCPA[hour];
                        hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) +
                                (costPerClick[hour]* clicksList.get(hour)) +
                                (lowCPA[hour]* (likesList.get(hour)*.5));
                        totalCost += hourlyCost;

                        //UPDATE YOUR STATS
                        totalImpressions += impressionsList.get(hour);
                        totalClicks += clicksList.get(hour);
                        totalLikes += likesList.get(hour)*.5;


                        //PRINT RESULTS
                        System.out.printf("%-10.0f", (double)timeList.get(hour));
                        System.out.printf("%-10.2f", costPerThousand[hour]);
                        System.out.printf("%-10.2f", costPerClick[hour]);
                        System.out.printf("%-10.2f", lowCPA[hour]);
                        System.out.printf("%-15.0f", (double)impressionsList.get(hour));
                        System.out.printf("%-10.0f", (double)clicksList.get(hour));
                        System.out.printf("%-10.2f",  likesList.get(hour) * .5);
                        System.out.printf("%10.2f\n", hourlyCost);

                    }
                }
            }

            hour++;
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
