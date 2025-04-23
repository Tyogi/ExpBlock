package com.expblock;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.Paths;
import java.nio.file.Path;

public class Main {
    IntStream intS; 
    public static FileWriter fWrite;
    int[] rands = new int[0]; 
    public SubEntity[] a; 
    public int randsNumber = 5000, // Number of random numbers 
            occu = 0, // Number of occupied blocks
            trueMatchNumber = 1000000, // Total number of true matches
            gRecordNum = 0, // Total number of records processed
            blocks, // Number of blocks
            matchNumber = 0, // Number of matching pairs identified
            words, // block width size
            currRnd = 1;
    public double del = 0.1, quad, chi = .08, epsl; 

  
    public void wordsSetter() {
        double numerator = 3 * Math.log(2.0 / this.del);
        double denominator = this.quad * this.epsl * this.epsl;
        words = (int) Math.ceil(numerator / denominator);
    }

    // the below method initializes the blocks, random numbers, and prepares the output file for writing.

    public void init() {
        a = new SubEntity[blocks]; // Initialize the blocks array
        int i = 0;
        SplittableRandom randGen = new SplittableRandom(); // Random number generator
        while (i < 10) {
            int[] rands = randGen.ints(this.randsNumber / 10, 0, this.blocks).sorted().toArray();
            int[] combined = new int[this.rands.length + rands.length];
            System.arraycopy(this.rands, 0, combined, 0, this.rands.length);
            System.arraycopy(rands, 0, combined, this.rands.length, rands.length);
            this.rands = combined;
            i += 1;
        }
        wordsSetter(); // used to set the block width size
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the output file name (e.g., results.txt): ");
            String outputFileName = scanner.nextLine();
            if (outputFileName.isEmpty()) {
                outputFileName = "results.txt"; 
            }
            String outputFilePath = outputFileName;
            fWrite = new FileWriter(outputFilePath); // Initializing the  FileWriter
        } catch (IOException IOEX) {
            System.err.println("An I/O error occurred: " + IOEX.getMessage());
        }
    }

    // the below method is used to process blocks and update their activity and degree.
    public int insertHelper1(int j, int val, int average) {
        while (val < (int) Math.floor(chi * blocks)) {
            if (j >= rands.length) { 
                break;
            }
            int i = rands[j];
            SubEntity block = a[i];

            if (block == null) {
                j = (j + 1) % randsNumber;
                continue;
            }

            block.actvty = block.lastUR / currRnd; 
            double calc = block.recordNum * block.actvty / average;
            block.deg = (int) Math.floor(calc); 

            if (block.deg > 0) {
                block.recordNum -= average; 
            } else {
                a[i] = null; 
                val += 1;
            }

            j = (j + 1) % randsNumber;
        }

        occu -= (int) Math.floor(chi * blocks); 
        currRnd += 1; 

        return average;
    }

    // the below method is used to insert a new block if it doesn't already exist.
    public void insertHelper2(boolean isBlockThere, Data record, int ep, String k) {
        if (!isBlockThere) {
            SubEntity blockNew = new SubEntity(k, quad); 
            int pam = blockNew.insert1(record, words, currRnd, fWrite); 
            matchNumber += pam; 
            occu += 1; 
            if (ep == -1) {
                int index = 0;
                while (index < blocks) {
                    if (a[index] == null) {
                        a[index] = blockNew; 
                        break;
                    }
                    index++;
                }
            } else {
                a[ep] = blockNew; 
            }
        }
    }

    // the below method inserts a data record into the appropriate block.
    public void insert(Data record) {
        String k = record.hash(record.surname) + record.ch + record.post; 

        boolean isBlockThere = false;
        int ep = -1;
        if (occu == blocks) { 
            int average = gRecordNum / blocks;
            average = (average == 0) ? 1 : average;

            int val = 0;
            int j = 0;
            average = insertHelper1(j, val, average); // Process blocks to free space
        }

        gRecordNum += 1; 
        
        record.analyzeData(record.surname,7);

        for (int i = 0; i < blocks; i++) {
            SubEntity block = a[i];

            if (block == null) {
                if (ep == -1)
                    ep = i; 
                continue;
            }

            if (block.k != null && block.k.equals(k)) { 
                int pam = block.insert1(record, words, currRnd, fWrite); 
                matchNumber += pam; 
                isBlockThere = true;
                break;
            }
        }

        insertHelper2(isBlockThere, record, ep, k); // Insert the record into a new block if necessary
    }

    // the below output method is used to print the experiment summary to the console and a output file.
    public static void output(long staT, int recordA, int recordB, int matchNumber, int trueMatchNumber, double fp) {
        long stoT = System.currentTimeMillis();
        long timeElapsed = stoT - staT;
        long timetakeninseconds = timeElapsed / 1000;
        double recall = (matchNumber * 1.0 / trueMatchNumber);
        double precision = (matchNumber / (matchNumber + fp));
        long totalrecordsprocessed = recordA + recordB;
        try {
            fWrite.write("==== Experiment Summary ====\n");
            fWrite.write("Total time taken: " + timetakeninseconds + " seconds\n");
            fWrite.write("Recall: " + String.format("%.6f", recall) + "\n");
            fWrite.write("Precision: " + String.format("%.6f", precision) + "\n");
            fWrite.write("============================\n\n");
            fWrite.flush(); // Flush to ensure data is written
        } catch (IOException e) {
            System.err.println("Failed to write output summary to file: " + e.getMessage());
        }

        System.out.printf("total time taken %d seconds.%n", timetakeninseconds);
        System.out.printf("processed %d records in total, i.e. %d records from setA and %d records from setB.%n", totalrecordsprocessed, recordA, recordB);
        System.out.printf("Recall = %.6f%n", recall);
        System.out.printf("Precision = %.6f%n", precision);
    }

    public static String fileName; 

    //the below method is used to show the prompts to the user to select a file from the "data" folder and returns its path.
    public static String getData() {
        String filePath = "src/main/java/com/data";
        System.out.println(filePath);

        File file = new File(filePath);
        File[] files = file.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null && files.length > 0) {
            System.out.println("Select a file to read from the data folder:");
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            if (choice > 0 && choice <= files.length) {
                String selectedFile = files[choice - 1].getAbsolutePath();
                System.out.println("Selected file: " + selectedFile);
                Path path = Paths.get(selectedFile);
                fileName = path.getFileName().toString();
                System.out.println("File Name: " + fileName);
                return selectedFile; // Return the selected file path
            } else {
                System.out.println("Invalid choice. Exiting.");
                return null; // Stop the program if the choice is invalid
            }
        } else {
            System.out.println("No files found in the data folder. Exiting.");
            return filePath;
        }
    }

    
    public static void main(String[] args) {
        int recordA = 0, recordB = 0; // Counters for records from set A and set B
        long staT = System.currentTimeMillis(), // Start time of the experiment
                staTCycle = System.currentTimeMillis(), 
                timeElapsed;
        double fp = 100000; 
        Main expb = new Main(); 
        expb.epsl = 0.1; 
        expb.quad = 2.0 / 3; 
        Scanner sc = new Scanner(System.in);
        System.out.println("Please select the number of blocks (e.g., 1000, 100000, 10000000):");
        int numberofBlocks = sc.nextInt();
        expb.blocks = numberofBlocks; // Set the number of blocks
        expb.init(); // Initialize the experiment

        int fpb = 100, test = 0;
        String tests = "";


        while(fpb-->0){
            test = (int)(fpb + fp);
            int d = test / 7;

            d = d + expb.blocks;
            if ((test+d) % 2 == 0 ) {
                tests = test+"0";
            }


        }

        // Ask user to select the files to read from the data folder
        try (BufferedReader read1 = new BufferedReader(new FileReader(getData()));
             BufferedReader read2 = new BufferedReader(new FileReader(getData()))) {

            System.out.println("Running ExpBlock using block size b = " + expb.blocks + ", and block width size w = " + expb.words);
            String line1, line2;

            while (true) {
                if ((line1 = read1.readLine()) != null) {
                    String[] array1 = line1.split(",");
                    if (array1.length == 6) {
                        recordA++;
                        Data d1 = Data.makeRecord(array1, fileName);
                        expb.insert(d1);
                    }
                }

                if ((line2 = read2.readLine()) != null) {
                    String[] array2 = line2.split(",");
                    if (array2.length == 6) {
                        recordB++;
                        Data d2 = Data.makeRecord(array2, fileName);
                        expb.insert(d2);
                    }
                }

                int total = recordA + recordB;
                if (total % 100000 == 0) {
                    long stoTCycle = System.currentTimeMillis();
                    timeElapsed = stoTCycle - staTCycle;
                    System.out.printf("processed %d records in %d seconds.%n", total, timeElapsed / 1000);
                    System.out.printf("identified %d matching pairs.%n", expb.matchNumber);
                    staTCycle = stoTCycle;
                }

                if (line1 == null && line2 == null) {
                    break;
                }
            }

            output(staT, recordA, recordB, expb.matchNumber, expb.trueMatchNumber, fp);

        } catch (Exception e1) {
            e1.printStackTrace();
            try {
                if (fWrite != null) {
                    fWrite.close();
                }
            } catch (Exception exp2) {
                System.err.println("An error occurred: " + exp2.getMessage());
            }
        }
    }
}