# ExpBlock

**ExpBlock** is a Java-based application designed for evaluating streaming record linkage on large datasets. It supports interactive execution and customizable settings for block sizes, output filenames, and input datasets.

---

## 📦 Setup Instructions

### 🔧 Prerequisites

1. [Visual Studio Code](https://code.visualstudio.com/) installed on your local machine.
2. Java Development Kit (JDK) version 17 or above.

### 📁 Dataset Preparation

Due to the large size of the datasets, download them manually from the following link:  
🔗 [Download Datasets](https://rb.gy/4cwd6y)

Once downloaded:

- Extract and copy all dataset files into the following directory in your project:  
  `ExpBlock-main/expblock/src/main/java/com/data`

---

## 🚀 Running the Project

1. Open the project in Visual Studio Code.
2. Navigate to `Main.java` and run the file.
3. Follow the interactive prompts:

---

### 🧑‍💻 User Prompts & Input Guide

**Step 1: Number of Blocks**  
You’ll be asked:

Please select the number of blocks (e.g., 1000, 100000, 10000000):

Enter the number of blocks you want to use.

**Step 2: Output File Name**  
Enter the output file name (e.g., results.txt):

Provide a custom output filename or press Enter to use the default `results.txt`.

**Step 3: Select First Dataset (Source File)**  
You'll be shown a list of available files in the `/data` folder:

1.mag.txt
2.test_voters_A.txt
3.test_voters_B.txt
4.dblp-b.txt
5.dblp-a.txt
6.aminer.txt


Choose the appropriate source file:
- `test_voters_A.txt` for NCVR dataset  
- `dblp-a.txt` for DBLP dataset  
- `mag.txt` for OAG dataset

**Step 4: Select Second Dataset (Target File)**  
Select the matching file:
- `test_voters_B.txt` for NCVR dataset  
- `dblp-b.txt` for DBLP dataset  
- `aminer.txt` for OAG dataset

**Step 5: Execution**  
The program will now process the data. Results will be saved to the file specified in Step 2 (e.g., `results.txt`).

---

## 📄 Output

- The final results will be stored in the output file (default: `results.txt`) in the root directory of the project.

---

Feel free to reach out if you face any setup issues or want to contribute to the project.
