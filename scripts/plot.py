"""results/results.csv -> results/plots/*.png.  Usage: python3 scripts/plot.py"""
import csv, os
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

rows = list(csv.DictReader(open("results/results.csv")))
for r in rows:
    r["n"] = int(r["n"]); r["time_ms"] = float(r["time_ms"]); r["ops"] = int(r["ops"])

def series(alg, inp):
    s = sorted((r for r in rows if r["algorithm"] == alg and r["input"] == inp), key=lambda r: r["n"])
    return [r["n"] for r in s], [r["time_ms"] for r in s], [r["ops"] for r in s]

os.makedirs("results/plots", exist_ok=True)

def finish(fig, ax, title, xl, yl, caption, name, logx=False, logy=False):
    ax.set_title(title); ax.set_xlabel(xl); ax.set_ylabel(yl); ax.grid(True, alpha=.3); ax.legend()
    if logx: ax.set_xscale("log")
    if logy: ax.set_yscale("log")
    fig.text(0.5, 0.01, caption, ha="center", fontsize=9, style="italic", wrap=True)
    fig.tight_layout(rect=(0, 0.06, 1, 1))
    fig.savefig(f"results/plots/{name}.png", dpi=150); plt.close(fig)

# 1. Recursive Edit Distance: calls vs n (log y)
fig, ax = plt.subplots(figsize=(7, 4.5))
for inp in ("identical", "random", "disjoint"):
    n, t, o = series("edit_recursive", inp); ax.plot(n, o, "o-", label=inp)
finish(fig, ax, "Recursive edit distance: number of calls", "string length n (chars)",
       "recursive calls (log scale)", "Notice: on 'disjoint' strings the calls grow ~5-6x per +1 char (exponential); identical strings stay linear.",
       "1_recursive_calls", logy=True)

# 2. DP table vs two rows: time vs n (log-log)
fig, ax = plt.subplots(figsize=(7, 4.5))
for alg in ("edit_table", "edit_two_rows"):
    n, t, o = series(alg, "random"); ax.plot(n, t, "o-", label=alg)
finish(fig, ax, "Edit distance DP: time vs size (random input)", "string length n (chars, log scale)",
       "time (ms, median of 5, log scale)", "Notice: for large n time grows ~4x per doubling of n (O(n*m)); two rows is not slower than the table (small-n times are noisy: JIT/GC).",
       "2_dp_time", logx=True, logy=True)

# 3. Search: comparisons vs n on the worst input
fig, ax = plt.subplots(figsize=(7, 4.5))
for alg in ("search_naive", "search_kmp"):
    n, t, o = series(alg, "worst"); ax.plot(n, o, "o-", label=alg)
finish(fig, ax, "String search, worst input (a^n, pattern a^99 b)", "text length n (chars)",
       "character comparisons (log scale)", "Notice: Naive does ~m*n comparisons (m=100), KMP stays near 2n: about 50x fewer.",
       "3_search_worst_ops", logy=True)

# 4. Search: ratio naive/kmp comparisons per input type
fig, ax = plt.subplots(figsize=(7, 4.5))
for inp in ("random", "worst", "all_match"):
    n, _, on = series("search_naive", inp); _, _, ok = series("search_kmp", inp)
    ax.plot(n, [a / b for a, b in zip(on, ok)], "o-", label=inp)
finish(fig, ax, "Search cost ratio: Naive / KMP comparisons", "text length n (chars)",
       "ratio (comparisons Naive / comparisons KMP)", "Notice: on random text the ratio is ~1 (random hides the difference); on periodic text it is ~50.",
       "4_search_ratio")

# 5. Search time on worst input
fig, ax = plt.subplots(figsize=(7, 4.5))
for alg in ("search_naive", "search_kmp"):
    n, t, o = series(alg, "worst"); ax.plot(n, t, "o-", label=alg)
finish(fig, ax, "String search time, worst input", "text length n (chars)",
       "time (ms, median of 5)", "Notice: wall-clock time confirms the counters: Naive grows with m*n, KMP stays small.",
       "5_search_worst_time")
print("plots written")
