package com.version1.socialswaysim;

/**
 * Created by bhaskarravi on 12/12/13.
 * Confidential property of SocialSway. All rights reserved.
 *
 * The Bid class maps to the Bid object found in Facebook's Ads API.
 * This object should connect to a mirrored object on our front end system.
 *
 */

public class Bid {
    private double CPM;
    private double CPC;
    private double CPA;

    public Bid(double CPM, double CPC, double CPA){
        this.CPM = CPM;
        this.CPC = CPC;
        this.CPA = CPA;
    }

    public double getCPM(){return CPM;}

    public double getCPC(){return CPC;}

    public double getCPA(){return CPA;}

    /**
     * Method: Calculate Cost
     * @param adGroup - Calculates the total cost incurred by an ad group
     * @return - Returns the total cost incurred by the adGroup
     *
     * NOTE: METHOD IS NOT COMPLETE. CPA ONLY TAKES INTO ACCOUNT LIKES
     */


    public double calculateCost(AdGroup adGroup){
        double totalCost = CPM * adGroup.getUniqueImpressions() / 1000 +
                           CPC * adGroup.getUniqueClicks() +
                           CPA * adGroup.getLikes(); //AT THE MOMENT THIS IS ONLY SET UP FOR LIKES

        return totalCost;
    }
}
