package com.version1.socialswaysim;

import java.util.ArrayList;

/**
 * Created by bhaskarravi on 12/19/13.
 */
public class StHealthSimulationAdaptiveBidding {

    public static void main(String[] args) {
        //Initialize both bid and adgroup datasets
        ArrayList<Bid> bidData = new ArrayList<Bid>();
        ArrayList<AdGroup> adGroupData = new ArrayList<AdGroup>();

        //Parse data

        DataParser obj = new DataParser();
        obj.readData(bidData, adGroupData, "/Users/bhaskarravi/Desktop/SimulationDBs/HealthAB/Health_BiddingSim_TEST_AdaptiveBidding.csv");

        //bidData contains all bidding data by hour
        //adGroupData contains all adgroup data by hour

        //Header
        System.out.println("CAMPAIGN VERTICAL: Health");
        System.out.println("CAMPAIGN GOAL: 2500 Page Likes");
        System.out.println("ADVERTISEMENT TYPE: Sponsored Story Page Post");
        System.out.println("BID TYPE: CPA, CPC, CPM");
        System.out.println("AD ACCOUNT: " + adGroupData.get(0).getAdAccountID());
        System.out.println("AD CAMPAIGN: " + adGroupData.get(0).getAdCampaignID());
        System.out.println("ADVERTISEMENT: " + adGroupData.get(0).getAdvertisementID());
        System.out.println("AD GROUP: " + adGroupData.get(0).getAdGroupID());
        System.out.println("==================================");
        System.out.println();

        System.out.printf("%-10s", "Time");
        System.out.printf("%-10s", "CPM");
        System.out.printf("%-10s", "CPC");
        System.out.printf("%-10s", "CPA");
        System.out.printf("%-15s", "Impressions");
        System.out.printf("%-10s", "Clicks");
        System.out.printf("%-10s", "Likes");
        System.out.printf("%10s\n", "Hourly $");


        //Init Campaign Vars
        double[] time = new double[adGroupData.size()];
        double[] impressions = new double[adGroupData.size()];
        double[] clicks = new double[adGroupData.size()];
        double[] likes = new double[adGroupData.size()];
        double[] medCPA = new double[bidData.size()];
        double[] highCPA = new double[bidData.size()];
        double[] lowCPA = new double[bidData.size()];
        double[] costPerClick = new double[bidData.size()];
        double[] costPerThousand = new double[bidData.size()];

        double totalCost = 0;
        int totalLikes = 0;
        int likesGoal = 2500;
        int totalImpressions = 0;
        int totalClicks = 0;
        double totalCPM = 0;
        double totalCPC = 0;
        double totalCPA = 0;

        //Init arrays

        for(int count = 0; count < adGroupData.size(); count ++){
            time[count] = adGroupData.get(count).getTimeStamp();
            impressions[count] = adGroupData.get(count).getUniqueImpressions();
            clicks[count] = adGroupData.get(count).getUniqueClicks();
            likes[count] = adGroupData.get(count).getLikes();

            //Bid array same size as our adgroup array
            medCPA[count] = bidData.get(count).getMedCPA();
            highCPA[count] = bidData.get(count).getHighCPA();
            lowCPA[count] = bidData.get(count).getLowCPA();
            costPerClick[count] = bidData.get(count).getCPC();
            costPerThousand[count] = bidData.get(count).getCPM();
        }


        //run campaign while likes is less than likes goal
        double currentCost;
        int i = 0;

        while(totalLikes < likesGoal){

            costPerClick[i] = 0;
            costPerThousand[i] = 0;
            //highCPA[i] = 0;
            medCPA[i] = 0;
            lowCPA[i] = 0;

            totalCPM += costPerThousand[i];
            totalCPC += costPerClick[i];
            totalCPA += highCPA[i];


            totalImpressions += impressions[i];
            totalClicks += clicks[i];
            totalLikes += likes[i];
            currentCost = (costPerThousand[i]*adGroupData.get(i).getUniqueImpressions()/1000) +
                    (costPerClick[i]*clicks[i]) + (highCPA[i]*(likes[i]));
            totalCost += currentCost;



            System.out.printf("%-10.0f",time[i]);
            System.out.printf("%-10.2f", costPerThousand[i]);
            System.out.printf("%-10.2f", costPerClick[i]);
            System.out.printf("%-10.2f", highCPA[i]);
            System.out.printf("%-15.0f", impressions[i]);
            System.out.printf("%-10.0f", clicks[i]);
            System.out.printf("%-10.0f", likes[i]);
            System.out.printf("%10.2f\n", currentCost);


            i++;

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
        System.out.println("AVERAGE CPM: " + Math.round(totalCPM / i * 100.0) / 100.0 );
        System.out.println("AVERAGE CPC: " + Math.round(totalCPC / i * 100.0) / 100.0 );
        System.out.println("AVERAGE CPA: " + Math.round(totalCost / totalLikes * 100.0) / 100.0 );
        System.out.println("CTR: " +  Math.round((totalClicks / (double) totalImpressions * 100)*100.0)/100.0 + "%" );
    }
}
