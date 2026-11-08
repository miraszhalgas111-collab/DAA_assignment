# Report - Assignment 4: City Search & Spell-Check

## 1. Experiment Design Card (written BEFORE the first run)

| Field | Content |
|---|---|
| **Question** | Edit Distance: does the cost of the recursive version really explode while table / two rows grow like n*m? String search: does KMP stay linear while Naive reaches m*n on a bad input? |
| **Sizes (n)** | Recursive: 3, 6, 9, 11, 12 (exponential, larger sizes take minutes). Table / two rows: 200, 400, 800, 1600, 3200 (each size doubles; the largest is 16x the smallest; the table at 3200 needs ~40 MB). Search: text 10k, 20k, 40k, 80k, 160k chars, pattern length 100 (20 for random). |
| **Inputs** | Edit: `random` (alphabet 4, typical case), `disjoint` (a^n vs b^n, no common char: worst case for recursion), `identical` (best case for recursion). Search: `random` (alphabet 4, typical case), `worst` (a^n vs a^99 b: Naive re-reads almost the whole pattern at every start), `all_match` (a^n vs a^100: many overlapping matches). |
| **Metrics** | Edit: number of recursive calls / computed cells (shows repeated work, independent of the laptop). Search: number of character comparisons. Time: median of 5 runs (after warm-up). |
| **Prediction** | Recursive: calls multiply by a constant factor with every +1 in n on `disjoint` (exponential), stay linear on `identical`. Table and two rows: cells = n^2 exactly, time x4 when n doubles, two rows needs far less memory but the same time. Naive: random ~ n, worst ~ m*n (about 100n). KMP: always about 2n or less. |
| **Plots** | 1) recursive calls vs n, log y: is it exponential? 2) DP time vs n, log-log: is it O(n*m), and does two rows cost time? 3) search comparisons vs n on the worst input: Naive vs KMP. 4) ratio Naive/KMP per input type: does random input hide the problem? 5) search time on the worst input: does the clock agree with the counters? |

(If you change the card later, write here what you changed and why.)

## 2. Plots and Results
TODO: insert `results/plots/1_...png` ... `5_...png`, each with its one-sentence takeaway.

## 3. Prediction vs Result
TODO (use your own words and numbers from results.csv). Things I noticed in the data:
- `disjoint` recursion: 67.6M calls at n=11, 377M at n=12 (about 5.6x per extra char).
- `random` recursion is NOT monotonic (855k calls at n=12, 5.2M at n=11) because every n uses a different random pair. Explain this honestly.
- Naive on `worst`: 15.99M comparisons at n=160000 (= about 100*n); KMP: 320k (= 2n). On `random` the ratio is about 1.
- Table is noisy at small n (JIT, allocation); two rows was even faster.

## 4. Short answers
TODO (3-5 sentences each): slowest input for search / max n for recursive / two rows gain and loss / which search for the city platform.

## 5. One paragraph on time
TODO.
