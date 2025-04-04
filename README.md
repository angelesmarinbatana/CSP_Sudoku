# CSP_Sudoku

### Overview
This project implements a constraint satisfaction problem (CSP) solver for Sudoku by implementing 
* backtracking search
* minimum remaining values (MRV) heuristics
* maintaining arch consistency (MAC) with AC-3 algorithm.
---
### Project structure 
The project contains 3 maain folders 
1. **core_algorithms**
   * Implements MAC algorithm (backtracking and AC-3) and MRV heuristic 
3. **csp_problems**
   * Sudoku.java
     * makes the CSP problem with variables, domains, and neighbors
   * Square.java
     * represents a cell (row, column) on a Sudoku grid
   * CSPProblem.java
     * the interface for CSP problem definition
5. **csp_solutions**
   * extends generic solver from core_algorithms for Sudoku
