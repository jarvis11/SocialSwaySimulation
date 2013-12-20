//package com.version1.socialswaysim;
//
//import java.util.ArrayList;
//
///**
// * Created by bhaskarravi on 12/16/13.
// */
//public class BiddingSimulationSocialSway {
//
//    public BiddingSimulationSocialSway (String campaignVertical, String dataSet, int goal, int actionHigh, int actionAvg) {
//
//        /**
//         * INITIALIZE PARAMETERS
//         */
//
//        //INITIALIZING TESTING DATA
//        ArrayList<Bid> bidDataTEST = new ArrayList<Bid>();
//        ArrayList<AdGroup> adGroupDataTEST = new ArrayList<AdGroup>();
//        DataParser objTEST = new DataParser();
//        objTEST.readData(bidDataTEST, adGroupDataTEST, dataSet);
//
//        //INITIALIZE LIVE ARRAYLISTS
//        ArrayList<Integer> timeList = new ArrayList<Integer>();
//        ArrayList<Integer> impressionsList = new ArrayList<Integer>();
//        ArrayList<Integer> clicksList = new ArrayList<Integer>();
//        ArrayList<Integer> likesList = new ArrayList<Integer>();
//
//        //INITIALIZE BIDDING VECTORS
//        double[] costPerAction = new double[bidDataTEST.size()];
//        double[] costPerClick = new double[bidDataTEST.size()];
//        double[] costPerThousand = new double[bidDataTEST.size()];
//
//        //FILL VECTORS WITH APPROPRIATE VALUES
//        for(int count = 0; count < bidDataTEST.size(); count++){
//
//            costPerAction[count] = bidDataTEST.get(count).getCPA();
//            costPerClick[count] = bidDataTEST.get(count).getCPC();
//            costPerThousand[count] = bidDataTEST.get(count).getCPM();
//
//        }
//
//        //Header
//        System.out.println("CAMPAIGN VERTICAL: " + campaignVertical);
//        System.out.println("CAMPAIGN GOAL: " + goal + " Page Likes");
//        System.out.println("ADVERTISEMENT TYPE: Sponsored Story Page Post");
//        System.out.println("BID TYPE: CPM, CPC, CPA");
//        System.out.println("AD ACCOUNT: " + adGroupDataTEST.get(0).getAdAccountID());
//        System.out.println("AD CAMPAIGN: " + adGroupDataTEST.get(0).getAdCampaignID());
//        System.out.println("ADVERTISEMENT: " + adGroupDataTEST.get(0).getAdvertisementID());
//        System.out.println("AD GROUP: " + adGroupDataTEST.get(0).getAdGroupID());
//        System.out.println("==================================");
//        System.out.println();
//
//        System.out.printf("%-10s", "Time");
//        System.out.printf("%-10s", "CPM");
//        System.out.printf("%-10s", "CPC");
//        System.out.printf("%-10s", "CPA");
//        System.out.printf("%-15s", "Impressions");
//        System.out.printf("%-10s", "Clicks");
//        System.out.printf("%-10s", "Likes");
//        System.out.printf("%10s\n", "Hourly $");
//
//        /**
//         * CAMPAIGN STARTS NOW
//         */
//
//        //INITIALIZE CAMPAIGN VARS
//        double totalCost = 0;   //Total Cost Incurred by the Campaign
//        int totalLikes = 0;     //Total Likes Accrued by Campaign
//        int totalImpressions = 0; //Total Impressions Accrued by Campaign
//        int totalClicks = 0;    //Total Clicks Accrued by Campaign
//        double totalCPM = 0;    //Total CPM Cost of Campaign
//        double totalCPC = 0;    //Total CPC Cost of Campaign
//        double totalCPA = 0;    //Total CPA Cost of Campaign
//        double hourlyCost = 0;  //Cost per Hour
//        int hour = 0;           //TIMESTAMP + 1
//
//        //RUN ACTUAL CAMPAIGN
//
//        while(totalLikes < goal){
//
//            //Start the Campaign as you would hour 1
//            if(hour < 12){
//                //MAKE YOUR BID (ADD APPROPRIATE VALUES TO YOUR ARRAYS)
//                timeList.add(adGroupDataTEST.get(hour).getTimeStamp());
//                impressionsList.add(adGroupDataTEST.get(hour).getUniqueImpressions());
//                clicksList.add(adGroupDataTEST.get(hour).getUniqueClicks());
//                likesList.add(adGroupDataTEST.get(hour).getLikes());
//
//                //MAKING CPC BID 0 -- MAKING CPA AND CPM BID ONLY
//                costPerClick[hour] = 0;
//
//                //If the hour is zero, just bid naturally...suck it up and pay the bills
//                totalCPM += costPerThousand[hour];
//                totalCPC += costPerClick[hour];
//                totalCPA += costPerAction[hour];
//                hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) +
//                        (costPerClick[hour]* clicksList.get(hour)) +
//                        (costPerAction[hour]* (clicksList.get(hour) + likesList.get(hour)));
//
//                totalCost += hourlyCost;
//
//                //UPDATE YOUR STATS
//                totalImpressions += impressionsList.get(hour);
//                totalClicks += clicksList.get(hour);
//                totalLikes += likesList.get(hour);
//
//                //PRINT RESULTS
//                System.out.printf("%-10.0f", (double)timeList.get(hour));
//                System.out.printf("%-10.2f", costPerThousand[hour]);
//                System.out.printf("%-10.2f", costPerClick[hour]);
//                System.out.printf("%-10.2f", costPerAction[hour]);
//                System.out.printf("%-15.0f", (double)impressionsList.get(hour));
//                System.out.printf("%-10.0f", (double)clicksList.get(hour));
//                System.out.printf("%-10.0f", (double)likesList.get(hour));
//                System.out.printf("%10.2f\n", hourlyCost);
//            }
//            else {
//                //PERFORM REGRESSION
//
//                //STEP 1: DISTRIBUTE ARRAYLISTS INTO ARRAYS
//                double[] time = new double[timeList.size()];
//                double[] impressions = new double[impressionsList.size()];
//                double[] clicks = new double[clicksList.size()];
//                double[] likes = new double[likesList.size()];
//
//                for(int count = 0; count < timeList.size(); count++){
//                    time[count] = timeList.get(count);
//                    impressions[count] = impressionsList.get(count);
//                    clicks[count] = clicksList.get(count);
//                    likes[count] = likesList.get(count);
//                }
//
//                //NOW INITIATE REGRESSIONS
//                //Impressions Regression
//                int degreeImpressions = 0;
//                double r2final = -20000000.0;
//                PolynomialRegressionMoorePenrose impressionsReg = new PolynomialRegressionMoorePenrose(time, impressions, degreeImpressions);
//                while(impressionsReg.getR2() > r2final){
//                    r2final = impressionsReg.getR2();
//                    degreeImpressions++;
//                    impressionsReg = new PolynomialRegressionMoorePenrose(time, impressions, degreeImpressions);
//                }
//                //Clicks Regression
//                int degreeClicks = 0;
//                r2final = -20000000.0;
//                PolynomialRegressionMoorePenrose clicksReg = new PolynomialRegressionMoorePenrose(time, clicks, degreeClicks);
//                while(clicksReg.getR2() > r2final){
//                    r2final = clicksReg.getR2();
//                    degreeClicks++;
//                    clicksReg = new PolynomialRegressionMoorePenrose(time, clicks, degreeClicks);
//                }
//                //Likes Regression
//                int degreeLikes = 0;
//                r2final = -20000000.0;
//                PolynomialRegressionMoorePenrose likesReg = new PolynomialRegressionMoorePenrose(time, likes, degreeLikes);
//                while(likesReg.getR2() > r2final){
//                    r2final = likesReg.getR2();
//                    degreeLikes++;
//                    likesReg = new PolynomialRegressionMoorePenrose(time, likes, degreeLikes);
//                }
//
//
//
//                //LOGIC
//                //If like count is projected as very high, then bid on the likes with the full CPA price
//                //else bid on CPM or CPC, whichever is cheaper
//
//                //CHECK IF REG PREDICTS A SUPERSTAR
//                if(likesReg.predict(hour+1) > actionHigh && likes[hour - 1] > actionAvg){ //9 is the mean # of likes for the training set, 36 is the average of the high dist.
//                    //MAKE CPA ONLY BID
//                    timeList.add(adGroupDataTEST.get(hour).getTimeStamp());
//                    impressionsList.add(adGroupDataTEST.get(hour).getUniqueImpressions());
//                    clicksList.add(adGroupDataTEST.get(hour).getUniqueClicks());
//                    likesList.add(adGroupDataTEST.get(hour).getLikes());
//
//                    //CPM AND CPC BID GO TO ZERO
//                    costPerThousand[hour] = 0;
//                    costPerClick[hour] = 0;
//
//                    //PAY THE BILLS
//                    totalCPM += costPerThousand[hour];
//                    totalCPC += costPerClick[hour];
//                    totalCPA += costPerAction[hour];
//                    hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) +
//                            (costPerClick[hour]* clicksList.get(hour)) +
//                            (costPerAction[hour]* (clicksList.get(hour) + likesList.get(hour)));
//                    totalCost += hourlyCost;
//
//                    //UPDATE YOUR STATS
//                    totalImpressions += impressionsList.get(hour);
//                    totalClicks += clicksList.get(hour);
//                    totalLikes += likesList.get(hour);
//
//                    //PRINT RESULTS
//                    System.out.printf("%-10.0f", (double)timeList.get(hour));
//                    System.out.printf("%-10.2f", costPerThousand[hour]);
//                    System.out.printf("%-10.2f", costPerClick[hour]);
//                    System.out.printf("%-10.2f", costPerAction[hour]);
//                    System.out.printf("%-15.0f", (double)impressionsList.get(hour));
//                    System.out.printf("%-10.0f", (double)clicksList.get(hour));
//                    System.out.printf("%-10.0f", (double)likesList.get(hour));
//                    System.out.printf("%10.2f\n", hourlyCost);
//                }
//                else{
//                    //LIKES IS NOT A MAJOR FACTOR ANY LONGER, SO LETS GO INTO COST SAVING MODE
//
//                    if((impressionsReg.predict(hour+1) * costPerThousand[hour]) / 1000 > (clicksReg.predict(hour+1) * costPerClick[hour]) ||
//                            likes[hour - 1] > actionAvg){
//                        //BID CPC NOT CPM
//                        timeList.add(adGroupDataTEST.get(hour).getTimeStamp());
//                        impressionsList.add(adGroupDataTEST.get(hour).getUniqueImpressions());
//                        clicksList.add(adGroupDataTEST.get(hour).getUniqueClicks());
//                        likesList.add(adGroupDataTEST.get(hour).getLikes());
//
//                        //CPC ONLY
//                        costPerAction[hour] = 0;
//                        costPerThousand[hour] = 0;
//
//                        //PAY THE BILLS
//                        totalCPM += costPerThousand[hour];
//                        totalCPC += costPerClick[hour];
//                        totalCPA += costPerAction[hour];
//                        hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) +
//                                (costPerClick[hour]* clicksList.get(hour)) +
//                                (costPerAction[hour]* (clicksList.get(hour) + likesList.get(hour)));
//                        totalCost += hourlyCost;
//
//                        //UPDATE YOUR STATS
//                        totalImpressions += impressionsList.get(hour) / 2;
//                        totalClicks += clicksList.get(hour);
//                        totalLikes += likesList.get(hour) /2;
//
//                        //PRINT RESULTS
//                        System.out.printf("%-10.0f", (double)timeList.get(hour));
//                        System.out.printf("%-10.2f", costPerThousand[hour]);
//                        System.out.printf("%-10.2f", costPerClick[hour]);
//                        System.out.printf("%-10.2f", costPerAction[hour]);
//                        System.out.printf("%-15.0f", (double)impressionsList.get(hour) / 2);
//                        System.out.printf("%-10.0f", (double)clicksList.get(hour));
//                        System.out.printf("%-10.0f", (double)likesList.get(hour) / 2);
//                        System.out.printf("%10.2f\n", hourlyCost);
//                    }
//                    else{
//                        //JUST BID CPM
//                        timeList.add(adGroupDataTEST.get(hour).getTimeStamp());
//                        impressionsList.add(adGroupDataTEST.get(hour).getUniqueImpressions());
//                        clicksList.add(adGroupDataTEST.get(hour).getUniqueClicks());
//                        likesList.add(adGroupDataTEST.get(hour).getLikes());
//
//                        //CPC ONLY
//                        costPerAction[hour] = 0;
//                        costPerClick[hour] = 0;
//
//                        //PAY THE BILLS
//                        totalCPM += costPerThousand[hour];
//                        totalCPC += costPerClick[hour];
//                        totalCPA += costPerAction[hour];
//                        hourlyCost = (costPerThousand[hour]*adGroupDataTEST.get(hour).getUniqueImpressions()/1000) +
//                                (costPerClick[hour]* clicksList.get(hour)) +
//                                (costPerAction[hour]* (clicksList.get(hour) + likesList.get(hour)));
//                        totalCost += hourlyCost;
//
//                        //UPDATE YOUR STATS
//                        totalImpressions += impressionsList.get(hour);
//                        totalClicks += clicksList.get(hour) / 2;
//                        totalLikes += likesList.get(hour) / 10;
//
//                        //PRINT RESULTS
//                        System.out.printf("%-10.0f", (double)timeList.get(hour));
//                        System.out.printf("%-10.2f", costPerThousand[hour]);
//                        System.out.printf("%-10.2f", costPerClick[hour]);
//                        System.out.printf("%-10.2f", costPerAction[hour]);
//                        System.out.printf("%-15.0f", (double)impressionsList.get(hour));
//                        System.out.printf("%-10.0f", (double)clicksList.get(hour) / 2);
//                        System.out.printf("%-10.0f", (double)likesList.get(hour) /10);
//                        System.out.printf("%10.2f\n", hourlyCost);
//
//                    }
//                }
//            }
//
//            hour++;
//        }
//
//        System.out.println();
//        System.out.println("==================================");
//        System.out.println("END CAMPAIGN");
//        System.out.println();
//        System.out.println("CAMPAIGN SUMMARY: ");
//        System.out.println();
//
//
//        System.out.println("TOTAL COST: " + Math.round(totalCost * 100.0) / 100.0);
//        System.out.println("TOTAL IMPRESSIONS: " + totalImpressions);
//        System.out.println("TOTAL CLICKS: " + totalClicks);
//
//        System.out.println("TOTAL LIKES: " + totalLikes);
//        System.out.println("AVERAGE CPM: " + Math.round(totalCPM / hour * 100.0) / 100.0 );
//        System.out.println("AVERAGE CPC: " + Math.round(totalCPC / hour * 100.0) / 100.0 );
//        System.out.println("AVERAGE CPA: " + Math.round(totalCPA / hour * 100.0) / 100.0 );
//        System.out.println("CTR: " +  Math.round((totalClicks / (double) totalImpressions * 100)*100.0)/100.0 + "%" );
//    }
//
//    public static void main(String[] args) {
//        BiddingSimulationSocialSway fashionBiddingSim = new BiddingSimulationSocialSway("Fashion",
//                "/Users/bhaskarravi/Desktop/SimulationDBs/Fashion_BiddingSim_TEST.csv", 3500, 25, 9);
//    }
//}
