package com.expblock;

import java.util.*;

//Entity class represents a block and provides basic functionality

public class Entity {
    int ed = 0, deg = 0, recordNum = 0, compNumbers = 0, lastUR = 0;
    String k;
    ArrayList<Data> arrayList = new ArrayList<>();
    double actvty = 0.0, qual = 0.0;
   
  //the below method calculates the gap (edit distance) between two strings.

    public int changeGap(String s1, String s2) {
        int len1 = s1.length(), len2 = s2.length();
        int[][] dist = new int[len1 + 1][len2 + 1];
        return changeGapHelper(s1, s2, dist, len1, len2);
        // return ed;
    }
    public Entity(String k, double q) {
        this.k = k;
        this.qual = q;
    }
    //the below method is used to calculate the edit distance using dynamic programming.


    public int changeGapHelper(String s1, String s2, int[][] dist, int len1, int len2) {
        int i = 0, j = 1;

        while (i <= len1) {
            dist[i][0] = i;
            i += 1;
        }
        while (j <= len2) {
            dist[0][j] = j;
            j += 1;
        }

        i = 1;
        j = 1;
        while (i <= len1) {
            while (j <= len2) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dist[i][j] = Math.min(dist[i - 1][j - 1] + cost, Math.min(dist[i - 1][j] + 1, dist[i][j - 1] + 1));
                j += 1;
            }
            i += 1;
        }

        return ed = dist[len1][len2];
    }

}
