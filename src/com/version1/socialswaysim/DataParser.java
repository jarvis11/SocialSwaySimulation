package com.version1.socialswaysim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.lang.Integer;
import  java.math.BigDecimal;

/**
 * Created by bhaskarravi on 11/29/13.
 * Confidential property of SocialSway. All rights reserved.
 *
 * The DataParser class allows one to quickly parse through a Facebook data set and enter the data into
 * the appropriate, workable Java objects.
 *
 */

public class DataParser {

    private ArrayList<Bid> bidSet;
    private ArrayList<AdGroup> adGroupSet;
    private String csv;

    public void readData(ArrayList<Bid> bidSet, ArrayList<AdGroup> adGroupSet, String csv){
        this.csv = csv;

        String csvFile = csv;
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        this.bidSet = bidSet;
        this.adGroupSet = adGroupSet;

        int counter = 0;
        Bid counterBid;
        AdGroup counterGroup;
        boolean firstLine = true;


        try{

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] group = line.split(csvSplitBy);
                if(firstLine == true) {
                    firstLine = false;
                    continue;
                }
                else{
                    counterBid = new Bid(
                            Double.parseDouble(group[0]),
                            Double.parseDouble(group[1]),
                            Double.parseDouble(group[2]),
                            Double.parseDouble(group[3]),
                            Double.parseDouble(group[4]));

                    bidSet.add(counter, counterBid);

                    counterGroup = new AdGroup(
                            Integer.parseInt(group[5]),
                            Integer.parseInt(group[6]),
                            Integer.parseInt(group[7]),
                            Integer.parseInt(group[8]),
                            Integer.parseInt(group[9]),
                            Integer.parseInt(group[10]),
                            Integer.parseInt(group[11]),
                            Integer.parseInt(group[12]),
                            0,0,0,0,0,0,0,0,0);


                    adGroupSet.add(counter, counterGroup);

                    counter++;
                }


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();

                }

                catch ( IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
