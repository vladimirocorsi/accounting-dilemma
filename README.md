# accounting-dilemma

THE PROBLEM

Danielle, the accountant of your company, needs help with a problem she is facing.
She needs to deal with a document containing a “Bank Transfer” and a list of “Due payments” represented as decimal numbers with 2 decimal places:

- An entry in the “Due payments” list represents the amount the company should receive as payment for a given product which has been recently sold

- The “Bank Transfer” value represents the amount of a bank transfer which sums up payments of sold items in a specific period of time (as they appear in the company’s account statement).

Danielle tells you that the “Bank Transfer” amount represents the sum of part of the amounts in the “Due payments” list. This is because the amount transferred in the company’s account is necessarily the sum of part of the due payments of recently sold items.

The problem is that Danielle does not know to which due payments the bank transfer corresponds. Can you build a small application/tool for Danielle so she could use it to speed up her day to day work.


THE SOLUTION

The approach used for solving this problem is inspired by the subset sum problem as described in https://en.wikipedia.org/wiki/Subset_sum_problem.

The solution is implemented as a simple Java project built with Maven.

USAGE

The input of the program is a text file containing one number per line. First number represents the bank transfer. The other numbers represent the due payments. Each number has exactly 2 decimals.

For the above example, the input file would contain:

74.06
22.75
59.33
34.22
27.21
17.09
100.99

The output file, named “output.txt” contains the list of found payments, one payment per line, sorted in ascending order. If no solution is found, the file will contain the text “NO SOLUTION” on the first line.

For the above example, the output file would contain:

17.09
22.75
34.22

So, given a jar artifact named find_payments.jar, we can issue the command

java -jar find_payments.jar ./my_input.txt

and find our solution in ./output.txt.
