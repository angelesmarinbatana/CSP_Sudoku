package csp_problems;

import java.io.*;
import java.util.*;

public class Sudoku implements CSPProblem<Square, Integer> {
    private final Map<Square, Variable<Square, Integer>> allVariables;
    private final Map<Square, Set<Square>> neighbors = new HashMap<>();
    private final List<Integer> DEFAULT_DOMAIN = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
    private final String filename;
    private final int size;

    public Sudoku(String filename) {
        this.filename = filename;
        this.size = 9;
        this.allVariables = loadPuzzleFromFile(filename);
        initializeNeighbors();
    }

    public Sudoku(int size) {
        this.filename = null;
        this.size = size;
        this.allVariables = createEmptyPuzzle(size);
        initializeNeighbors();
    }

    private Map<Square, Variable<Square, Integer>> loadPuzzleFromFile(String filename) {
        Map<Square, Variable<Square, Integer>> variables = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            for (int i = 0; i < size; i++) {
                if ((line = reader.readLine()) != null && !line.isEmpty()) {
                    String[] numbers = line.trim().split(" ");
                    for (int j = 0; j < size; j++) {
                        Square square = new Square(i, j);
                        int value = Integer.parseInt(numbers[j]);
                        Variable<Square, Integer> variable = (value > 0 && value <= 9)
                                ? new Variable<>(square, new LinkedList<>(List.of(value)))
                                : new Variable<>(square, new LinkedList<>(DEFAULT_DOMAIN));
                        variables.put(square, variable);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return variables;
    }

    private Map<Square, Variable<Square, Integer>> createEmptyPuzzle(int size) {
        Map<Square, Variable<Square, Integer>> variables = new HashMap<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Square square = new Square(i, j);
                variables.put(square, new Variable<>(square, new LinkedList<>(DEFAULT_DOMAIN)));
            }
        }
        return variables;
    }

    private void initializeNeighbors() {
        // Row and column neighbors
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Square sq = new Square(i, j);
                neighbors.putIfAbsent(sq, new HashSet<>());

                for (int k = 0; k < size; k++) {
                    if (k != j) neighbors.get(sq).add(new Square(i, k)); // row
                    if (k != i) neighbors.get(sq).add(new Square(k, j)); // column
                }
            }
        }

        // Box neighbors
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                Set<Square> boxSquares = new HashSet<>();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        boxSquares.add(new Square(boxRow * 3 + i, boxCol * 3 + j));
                    }
                }
                for (Square sq : boxSquares) {
                    neighbors.putIfAbsent(sq, new HashSet<>());
                    for (Square neighbor : boxSquares) {
                        if (!sq.equals(neighbor)) {
                            neighbors.get(sq).add(neighbor);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Map<Square, Variable<Square, Integer>> getAllVariables() {
        return allVariables;
    }

    @Override
    public List<Square> getNeighborsOf(Square sq) {
        return new ArrayList<>(neighbors.getOrDefault(sq, Collections.emptySet()));
    }

    @Override
    public List<Square> getPreAssignedVariables() {
        List<Square> preAssigned = new ArrayList<>();
        for (Map.Entry<Square, Variable<Square, Integer>> entry : allVariables.entrySet()) {
            if (entry.getValue().domain().size() == 1) {
                preAssigned.add(entry.getKey());
            }
        }
        return preAssigned;
    }

    @Override
    public void printPuzzle(Map<Square, Variable<Square, Integer>> allVariables) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Square sq = new Square(i, j);
                if (allVariables.get(sq).domain().size() == 1) {
                    System.out.print("[" + allVariables.get(sq).domain().getFirst() + "] ");
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
    }

    public int getSize() {
        return size;
    }
}
