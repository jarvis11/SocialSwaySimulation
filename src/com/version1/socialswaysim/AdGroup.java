package com.version1.socialswaysim;


/**
 * Created by bhaskarravi on 11/29/13.
 * Confidential property of SocialSway. All rights reserved.
 *
 * The AdGroup class maps to the AdGroup object found in Facebook's Ads API.
 * This object should connect to a mirrored object on our front end system.
 *
 */

public class AdGroup {

    private int adAccountID;
    private int adCampaignID;
    private int advertisementID;
    private int adGroupID;
    private int timeStamp;
    private int uniqueImpressions;
    private int uniqueClicks;
    private int likes;
    private int uniqueTitleClicks;
    private int rsvpYes;
    private int rsvpMaybe;
    private int postLikes;
    private int comments;
    private int uniquePhotoViews;
    private int uniqueLinkClicks;
    private int uniqueVideoPlays;
    private int uniqueQuestionVotes;

    /**
     * Partial AdGroup initialization. Many parameters are missing, and object is still in Alpha stage.
     * Initialization parameters should ideally be split into their own objects to mimic the tracking spec, targeting spec,
     * and bidding objects on Facebook.
     *
     * @param adAccountID - ID of the AdAccount that the AdGroup belongs to
     * @param adCampaignID - ID of the AdCampaign that the AdGroup belongs to
     * @param advertisementID - ID of the Creative that is initialized within the AdGroup
     * @param adGroupID - ID of the AdGroup
     * @param timeStamp - Current Timestamp of the AdGroup
     * @param uniqueImpressions - Number of unique impressions accrued by the AdGroup
     * @param uniqueClicks - Number of unique clicks accrued by the AdGroup
     * @param likes - Number of likes accrued by the AdGroup
     * @param uniqueTitleClicks - Number of unique title clicks accrued by the AdGroup
     * @param rsvpYes - Number of rsvp yes' accrued by the AdGroup
     * @param rsvpMaybe - Number of rsvp maybes accrued by the AdGroup
     * @param postLikes - Number of post likes accrued by the AdGroup
     * @param comments - Number of comments accrued by the AdGroup
     * @param uniquePhotoViews - Number of unique photo views accrued by the AdGroup
     * @param uniqueLinkClicks - Number of unique link clicks accrued by the AdGroup
     * @param uniqueVideoPlays - Number of unique video plays accrued by the AdGroup
     * @param uniqueQuestionVotes - Number of unique question vots accrued by the AdGroup
     */

    public AdGroup (int adAccountID, int adCampaignID, int advertisementID, int adGroupID, int timeStamp, int uniqueImpressions,
                    int uniqueClicks, int likes, int uniqueTitleClicks, int rsvpYes, int rsvpMaybe, int postLikes, int comments,
                    int uniquePhotoViews, int uniqueLinkClicks, int uniqueVideoPlays, int uniqueQuestionVotes) {

        this.adAccountID = adAccountID;
        this.adCampaignID = adCampaignID;
        this.advertisementID = advertisementID;
        this.adGroupID = adGroupID;
        this.timeStamp = timeStamp;
        this.uniqueImpressions = uniqueImpressions;
        this.uniqueClicks = uniqueClicks;
        this.likes = likes;
        this.uniqueTitleClicks = uniqueTitleClicks;
        this.rsvpYes = rsvpYes;
        this.rsvpMaybe = rsvpMaybe;
        this.postLikes = postLikes;
        this.comments = comments;
        this.uniquePhotoViews = uniquePhotoViews;
        this.uniqueLinkClicks = uniqueLinkClicks;
        this.uniqueVideoPlays = uniqueVideoPlays;
        this.uniqueQuestionVotes = uniqueQuestionVotes;



    }

    public int getAdAccountID(){
        return adAccountID;
    }

    public int getAdCampaignID(){
        return adCampaignID;
    }

    public int getAdvertisementID(){
        return advertisementID;
    }

    public int getAdGroupID(){
        return adGroupID;
    }

    public int getTimeStamp(){
        return timeStamp;

    }

    public int getUniqueImpressions(){
        return uniqueImpressions;
    }

    public int getUniqueClicks(){
        return uniqueClicks;
    }

    public int getLikes(){
        return likes;
    }

    public int getUniqueTitleClicks(){
        return uniqueTitleClicks;
    }

    public int getRsvpYes(){
        return  rsvpYes;
    }

    public int getRsvpMaybe(){
        return rsvpMaybe;
    }

    public int getPostLikes(){
        return postLikes;
    }

    public int getComments(){
        return  comments;
    }

    public int getUniquePhotoViews(){
        return uniquePhotoViews;
    }

    public int getUniqueLinkClicks(){
        return uniqueLinkClicks;
    }

    public int getUniqueVideoPlays(){
        return uniqueVideoPlays;
    }

    public int getUniqueQuestionVotes(){
        return uniqueQuestionVotes;
    }

    /**
     * Method: calculateCTR
     * Calculates the unique CTR of the AdGroup
     * @return - unique Clicks / unique Impressions
     */

    public double calculateCTR(){

        //given in percentage
        double traditionalCTR = uniqueClicks / (double) uniqueImpressions * 100;


        return (double)Math.round(traditionalCTR*100)/100;


    }

    /**
     * Method: calculateSCTR
     * Possible technique to calculate our own proprietary weighted CTR
     * @return - weighted SCTR
     *
     * Method is in pre alpha and needs to be completed.
     * Possible technique: Expectation Maximization.
     */

    public double calculateSCTR(){

        double w1 = .25;
        double w2 = 1.3;
        double w3 = 0;
        double w4 = 0;
        double w5 = .5;
        double w6 = .25;
        double w7 = 0;
        double w8 = 0;
        double w9 = 0;
        double w10 = 0;

        double sCTR = ((uniqueTitleClicks * w1 +
                likes * w2 +
                rsvpYes * w3 +
                rsvpMaybe * w4 +
                postLikes *w5 +
                comments * w6 +
                uniquePhotoViews * w7 +
                uniqueLinkClicks * w8 +
                uniqueVideoPlays * w9 +
                uniqueQuestionVotes * w10) /
                ((double) uniqueImpressions)) * 100;

        return (double)Math.round(sCTR*100)/100;


    }
}
