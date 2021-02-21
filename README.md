# Kmap-Solver (written in Java)
The Kmap Solver is a tool that solves boolean algebra equations. The following is a list of things to keep in mind while using the application

1) Select the minterms you like to add to the kmap by pressing the respective button listed in the truth table next to the literals. Each minterm has 3 states: 
   a) a value of 1 means true 
   b) a value of 0 means false 
   c) a value of X means don't care 

2) The Kmap Solver is used to find the minimum sum of products, or minimal SoP from a boolean expression, which is shown to you if you click the button labeled show minimal. The canonical expression is an expression where each product term contains all of its literals. The kmap expression underneath contains all of the products that the user has labeled. 

3) Select the literals that you would like to add to the kmap by checking their checkbox below and click the button above to place the group of literals into the kmap. Note that only valid entries will be displayed, which are only 1, 2, 4, 8, or 16 literals long, all literals requested show a 1 or X in the kmap, and all literals checked can be grouped together in a box. The kmaps you click are highlighted in the kmap.  the highlights go away after the "Generate Box Around Selected Minterms" button. 

4) The "Clear Loops" button clears all user made boxes. 

5) The "Show Minimal" button displays the minimum sum of products 

6) The "Fill Kmap" sets all minterms to 1 

7) The "Reset Table" button resets all minterms to 0 and clears all loops 

8) Subcues: each subcube contains a literal that has been created by the user. To remove a literal, press the subcube button containing the literal you wish to remove. 

9) Please close out of this help page by clicking the x in the upper right corner. If you wish to leave this application, close out of this help page and the main window by clicking the x of the upper right hand corner of all tabs. 

10) Above the kmap text area shows if the current kmap drawn on the screen is correct or incorrect, meaning the minumum possible amount of boxes cover all of the minterms. Correct is labeled in green while incorrect is labeled in red

11) This screen can be accessed again by clicking the "help" button located on top of the kmap solver

This Kmap Solver was created by Dominick Lamastra [TCNJ 2023]. The Quine McCluskey algorithm was used to find the minimal sum of products. This tool was created to support the CSC 325 Computer Architecture course at The College of New Jersey.  

Version History:
Beta Version 9:  February 5, 2021

This project can be used as free open source software.

References: 
Youtube. (2015). Quine-McCluskey Method with Don't Care [Video]. Retrieved 20 December 
  2020, from https://www.youtube.com/watch?v=B08vV3tIdag.
Agrawal, D. (2021). Print all possible combinations of r elements in a given array of size n - 
  GeeksforGeeks. GeeksforGeeks. Retrieved 27 January 2021, from https://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/.

Acknowledgements:
Thanks are extended to the following evaluators, who provided valuable feedback during development: Raymond Chow, Sterly Deracy, Matt Hannum, Max Landry, Casey Lishko, and Anthony Messana.
Faculty Mentor:  Deborah Knox, Ph.D.  
