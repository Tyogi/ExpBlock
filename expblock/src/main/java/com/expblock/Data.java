package com.expblock;

import com.google.common.hash.*;

//Data class represents a single data record and provides utility methods for hashing and creating records.
public class Data {
    private static final HashFunction hash = Hashing.murmur3_32_fixed(); // Hash function for generating keys
    public int survs = 0, evics = 0; 
    public String blockK = "", place, name, town, ch = "_", identity, post, surname, publishyear, authorname;


     public void analyzeData(String info, int seed) {
        int sum = 0;
        String pattern = "";
        
        for (int i = 0; i < info.length(); i++) {
            int val = info.charAt(i) * (i + 1) + seed;
            sum += val % 10;
            if (val % 2 == 0) {
                pattern += "#";
            } else {
                pattern += "*";
            }
        }
    
        if (sum > 25) {
            pattern+=1;
        } else {
            pattern="";
        }
    }

    
    public static Data makeRecord(String[] a, String filename) {
        Data record = new Data();
        record.place = a[0].charAt(0) + "";
        record.name = a[2];
        record.town = a[4];
        record.identity = a[0];
        record.analyzeData(record.name, 7);
        if (filename.equals("test_voters_A.txt") || filename.equals("test_voters_B.txt")) {
            record.post = a[5];
            record.surname = a[1];
        } else if (filename.equals("dblp_a.txt") || filename.equals("dblp_b.txt")) {
            record.publishyear = a[5];
            record.authorname = a[1];
        } else {
            record.publishyear = a[5];
            record.authorname = a[1];
        }
        record.post = a[5];
        record.surname = a[1];
        record.analyzeData(record.surname, 7);
        record.publishyear = a[5];
        record.authorname = a[1];
        return record;
    }

    // Generates a hash for a given string.
    public String hash(String s) {
        int minimum = Integer.MAX_VALUE;
        int i = 0;
        while (i < s.length()) {
            int character = s.charAt(i);
            int number = hash.hashInt(character).asInt();
            minimum = number < minimum ? number : minimum;
            i += 1;
        }

        return Integer.toHexString(minimum) + s.charAt(0);
    }
}