package com.expblock;

import java.io.*;
import java.util.*;

//SubEntity class extends Entity and provides additional functionality specific to processing data records and matching logic.

public class SubEntity extends Entity {

   
    public SubEntity(String k, double qual) {
        super(k, qual);
    }

    // the below method checks if the gap between two data records is within a threshold.
    public boolean checkGap(Data r, Data r1) {
        boolean b1 = changeGap(r1.surname, r.surname) <= 2;
        boolean b2 = changeGap(r1.name, r.name) <= 2;

        return b1 && b2;
    }

    
    public String getStr(Data d, Data d1) {
        return String.format("%s %s %s %s %s matched with %s %s %s %s %s",
            d1.identity, d1.surname, d1.name, d1.town, d1.post,
            d.identity, d.surname, d.name, d.town, d.post);
    }

    //the below method checks if the identity numbers of two data records match.
     
    public boolean checkIdentityNumber(Data data, Data data1) {
        String ch = "_";
        int index = 0;
        String i = data.identity, i1 = data1.identity;
        String s = "", s1 = "";

        if (i.indexOf(ch) <= 0) {
            s = i.substring(1);
        } else {
            index = i.indexOf(ch);
            s = i.substring(1, index);
        }

        if (i1.indexOf(ch) <= 0) {
            s1 = i1.substring(1);
        } else {
            index = i1.indexOf(ch);
            s1 = i1.substring(1, index);
        }

        return s.equals(s1);
    }

    // the below Helper method is to write a match result to the output file.
     
    public int insert1Helper1(Data data, Data data1, FileWriter fWrite, int matchPNumbers) {
        String res = getStr(data, data1);

        try {
            fWrite.append(res).append(System.lineSeparator());
        } catch (IOException ioEx) {
            System.err.println("Failed to write result: " + ioEx.getMessage());
        }

        matchPNumbers += 1;
        return matchPNumbers;
    }

    //the below Helper method is used to manage the block's data records and update its state.
    
    public void insert1Helper2(int omega, Data data, Random rand, int ro) {
        if (arrayList.size() < omega) {
            arrayList.add(data);
        } else {
            ArrayList<Data> list = new ArrayList<>();

            for (Data data1 : arrayList) {
                if (rand.nextDouble() < (1 - this.qual)) {
                    data1.survs += 1;
                    list.add(data1);
                }
            }

            arrayList = list;
        }

        lastUR = ro;
        recordNum += 1;
    }

    //the below method inserts a data record into the block and checks for matches.

    public int insert1(Data data, int omega, int ro, FileWriter fWrite) {
        Random rand = new Random();
        int matchPNumbers = 0;

        for (Data data1 : arrayList) {
            if (data1.place.equals(data.place)) {
                continue;
            }

            this.compNumbers += 1;

            if (checkGap(data, data1) && checkIdentityNumber(data, data1)) {
                matchPNumbers = insert1Helper1(data, data1, fWrite, matchPNumbers);
            }
        }

        insert1Helper2(omega, data, rand, ro);

        return matchPNumbers;
    }
}