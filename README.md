# DAA Assignment 4 - Dynamic Programming and String Algorithms

Edit Distance (recursive / table / two rows) and string search (Naive / KMP) with counters and an experiment.

## Build and test
```bash
mvn test
```

## Run the experiment (writes results/results.csv)
```bash
mvn -q compile exec:java
python3 scripts/plot.py        # needs matplotlib; writes results/plots/*.png
```
The generator uses a fixed seed (42), so the counters are identical on every run (times depend on the machine).

## Layout
- `src/main/java/daa/edit` - three Edit Distance versions (state / transition / base case in comments)
- `src/main/java/daa/search` - `NaiveSearch`, `KmpSearch` (`KmpSearch.lps` is public)
- `src/main/java/daa/experiment` - `InputGenerator`, `ExperimentRunner`, `CsvWriter`
- `src/test/java/daa` - JUnit 5 tests
- `results/` - `results.csv`, `plots/`
- `REPORT.md` - Experiment Design Card, plots, answers

## AI use acknowledgement
TODO: write honestly which parts you did with AI help (e.g. "Claude helped with the project skeleton; I wrote the report and checked all results myself").
