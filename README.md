
# Pink Café 🎀
### Smart Café Search & Algorithm Performance Lab

**DAA Assignment 4 — Dynamic Programming and String Algorithms**

Student: Dauletkyzy Bayan  
Group: SE-2523  
University: Astana IT University

## About the Project

Pink Café is a smart café search application built with Java.

The application helps customers find drinks even when
they make spelling mistakes. It also allows users to
search for ingredients in the café menu.

The project combines a pink-themed graphical interface
with algorithm analysis and performance experiments.

## Features

- Beautiful café interface using Java Swing
- Interactive menu with drinks and desserts
- Smart spelling correction for drink names
- Ingredient search
- Algorithm performance comparison
- Experiment results and charts

## Algorithms

### Edit Distance
1. Recursive Edit Distance
2. Table Edit Distance
3. Two Rows Edit Distance

### String Search
4. Naive Search
5. Knuth-Morris-Pratt (KMP)

## Technologies

- Java 17
- Maven
- Java Swing
- JUnit 5

## How to Run

### Build the project
```bash
mvn package
```

### Run the GUI
```bash
java -cp target/classes pinkcafe.PinkCafeGUI
```

### Run Tests
```bash
mvn test
```

### Run Experiments
```bash
java -cp target/classes pinkcafe.ExperimentRunner
```

### Generate Plots
```bash
java -cp target/classes pinkcafe.PlotGenerator
```

## Experiment

The experiment compares the performance of five algorithms.

Metrics:
- Execution time in milliseconds
- Recursive calls
- DP operations
- Character comparisons

Each experiment is repeated five times.

Results are saved in:
- results/results.csv
- results/summary.csv
- results/plots/

Full analysis is available in REPORT.md.

## AI Usage Acknowledgement

ChatGPT was used to assist with project planning,
Java implementation, GUI design, testing,
and documentation.

The project includes algorithm experiments,
JUnit tests, and reproducible results.

## Conclusion

Pink Café demonstrates how dynamic programming
and string matching can solve practical search problems.

The project combines a creative user interface
with algorithm implementation and performance analysis.
