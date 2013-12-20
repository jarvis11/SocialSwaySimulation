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
    private double medCPA;
    private double highCPA;
    private double lowCPA;

    public Bid(double CPM, double CPC, double medCPA, double highCPA, double lowCPA){
        this.CPM = CPM;
        this.CPC = CPC;
        this.medCPA = medCPA;
        this.highCPA = highCPA;
        this.lowCPA = lowCPA;
    }

    public double getCPM(){return CPM;}

    public double getCPC(){return CPC;}

    public double getMedCPA(){return medCPA;}

    public double getHighCPA(){return highCPA;}

    public double getLowCPA(){return lowCPA;}





}
