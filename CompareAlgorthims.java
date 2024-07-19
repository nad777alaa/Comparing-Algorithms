/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class CompareAlgorthims {
    public static void main(String[] args) {
        Welco welcomeWindow = new Welco();
        welcomeWindow.setVisible(true);
    }
}

class Welco extends JFrame {
    public Welco() {
        setTitle("Welcome");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Welcome to Sorting Visualization");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                sortowind sortSelectionWindow = new sortowind();
                sortSelectionWindow.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        panel.add(continueButton, BorderLayout.SOUTH);

        add(panel);
    }
}

class sortowind extends JFrame {
    public sortowind() {
        setTitle("Sort Selection");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Select a sorting algorithm:");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JComboBox<String> algorithmComboBox = new JComboBox<>(new String[]{"Bubble Sort", "Quick Sort", "Count Sort"});
        algorithmComboBox.setSelectedIndex(0); // Set default selection

        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                arraywindo arrayTypeSelectionWindow = new arraywindo((String) algorithmComboBox.getSelectedItem());
                arrayTypeSelectionWindow.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(algorithmComboBox, BorderLayout.CENTER);
        panel.add(continueButton, BorderLayout.SOUTH);

        add(panel);
    }
}

class arraywindo extends JFrame {
    private String seleco;

    public arraywindo(String algorithm) {
        this.seleco = algorithm;

        setTitle("Array Type Selection");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Select an array type:");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JComboBox<String> arrayTypeComboBox = new JComboBox<>(new String[]{"Random", "Sorted", "Inversely Sorted"});
        arrayTypeComboBox.setSelectedIndex(0);

        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SortingVisualization sortingVisualization = new SortingVisualization(seleco, (String) arrayTypeComboBox.getSelectedItem());
                sortingVisualization.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(arrayTypeComboBox, BorderLayout.CENTER);
        panel.add(continueButton, BorderLayout.SOUTH);

        add(panel);
    }
}

class SortingVisualization extends JFrame {
    private JLabel infoLabel;
    private JLabel comparisonsLabel;
    private JLabel interchangesLabel;
    private long startTime;

    public SortingVisualization(String algorithm, String arrayType) {
        setTitle("Sorting Visualization");
        setSize(800, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        SortingPanel sortingPanel = new SortingPanel();
        sortingPanel.setArrayType(arrayType);
        if (algorithm.equals("Bubble Sort")) {
            sortingPanel.startBubbleSort();
        } else if (algorithm.equals("Quick Sort")) {
            sortingPanel.startQuickSort();
        } else if (algorithm.equals("Count Sort")) {
            sortingPanel.startCountSort();
        }

        infoLabel = new JLabel("Sorting in progress...");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        comparisonsLabel = new JLabel("Comparisons: 0");
        comparisonsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        interchangesLabel = new JLabel("Interchanges: 0");
        interchangesLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(infoLabel);
        infoPanel.add(comparisonsLabel);
        infoPanel.add(interchangesLabel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(sortingPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton restartButton = new JButton("Restart");
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Welco welcomeWindow = new Welco();
                welcomeWindow.setVisible(true);
            }
        });
        bottomPanel.add(restartButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        startTime = System.currentTimeMillis();

        sortingPanel.setCompletionCallback(() -> {
            long endTime = System.currentTimeMillis();
            long runtime = endTime - startTime;
            infoLabel.setText("Sorting completed in " + runtime + " milliseconds.");
            restartButton.setEnabled(true);
        });
    }

    public void updateInfo(int comparisons, int interchanges) {
        comparisonsLabel.setText("Comparisons: " + comparisons);
        interchangesLabel.setText("Interchanges: " + interchanges);
    }

    public void finishSorting() {
        long endTime = System.currentTimeMillis();
        long runtime = endTime - startTime;
        infoLabel.setText("Sorting completed in " + runtime + " milliseconds.");
    }
}

class SortingPanel extends JPanel {
    private static final int DELAY = 1;
    private static final int COLUMN_WIDTH = 2;

    private int[] array;
    private int[] countArray;
    private int movingColumnIndex = -1;
    private String arrayType;
    private Thread sortingThread;
    private Runnable completionCallback;

    private int interchanges = 0;
    private int comparisons = 0;

    public SortingPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(700, 400));
    }

    public void setArrayType(String arrayType) {
        this.arrayType = arrayType;
    }

    public void setCompletionCallback(Runnable completionCallback) {
        this.completionCallback = completionCallback;
    }

    public void startBubbleSort() {
        if (isSortingInProgress()) {
            return;
        }

        initializeArray();

        sortingThread = new Thread(this::bublosorto);
        sortingThread.start();
    }

    public void startQuickSort() {
        if (isSortingInProgress()) {
            return;
        }

        initializeArray();

        sortingThread = new Thread(this::qoqosorto);
        sortingThread.start();
    }

    public void startCountSort() {
        if (isSortingInProgress()) {
            return;
        }

        initializeArray();

        sortingThread = new Thread(this::cocosorto);
        sortingThread.start();
    }

    private boolean isSortingInProgress() {
        return sortingThread != null && sortingThread.isAlive();
    }

    private void initializeArray() {
        int size = 10000;
        switch (arrayType) {
            case "Random":
                array = generatorandmo(size);
                break;
            case "Sorted":
                array = generatosorto(size);
                break;
            case "Inversely Sorted":
                array = generatoinverso(size);
                break;
        }
    }

    private void bublosorto() {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                if (array[j] > array[j + 1]) {
                    swap(array, j, j + 1);
                    interchanges++;
                    movingColumnIndex = j + 1;
                    try {
                        TimeUnit.MILLISECONDS.sleep(DELAY);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    repaint();
                }
            }
        }
        movingColumnIndex = -1;
        repaint();
        SwingUtilities.invokeLater(() -> {
            ((SortingVisualization) SwingUtilities.getWindowAncestor(this)).updateInfo(comparisons, interchanges);
            ((SortingVisualization) SwingUtilities.getWindowAncestor(this)).finishSorting();
            if (completionCallback != null) {
                completionCallback.run();
            }
        });
    }

    private void qoqosorto() {
        qoqosorto(array, 0, array.length - 1);
        SwingUtilities.invokeLater(() -> {
            ((SortingVisualization) SwingUtilities.getWindowAncestor(this)).updateInfo(comparisons, interchanges);
            ((SortingVisualization) SwingUtilities.getWindowAncestor(this)).finishSorting();
            if (completionCallback != null) {
                completionCallback.run();
            }
        });
    }

    private void qoqosorto(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            qoqosorto(arr, low, pi - 1);
            qoqosorto(arr, pi + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            comparisons++;
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
                interchanges++;
                movingColumnIndex = j;
                try {
                    TimeUnit.MILLISECONDS.sleep(DELAY);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                repaint();
            }
        }
        swap(arr, i + 1, high);
        movingColumnIndex = -1;
        try {
            TimeUnit.MILLISECONDS.sleep(DELAY);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        repaint();
        return i + 1;
    }

    private void cocosorto() {
        int max = findMax(array);
        countArray = new int[max + 1];
        for (int value : array) {
            countArray[value]++;
            movingColumnIndex = value;
            try {
                TimeUnit.MILLISECONDS.sleep(DELAY);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            repaint();
        }

        int index = 0;
        for (int i = 0; i < countArray.length; i++) {
            while (countArray[i] > 0) {
                array[index++] = i;
                countArray[i]--;
                movingColumnIndex = i;
                try {
                    TimeUnit.MILLISECONDS.sleep(DELAY);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                repaint();
            }
        }

        movingColumnIndex = -1;
        repaint();
        SwingUtilities.invokeLater(() -> {
            ((SortingVisualization) SwingUtilities.getWindowAncestor(this)).updateInfo(comparisons, interchanges);
            ((SortingVisualization) SwingUtilities.getWindowAncestor(this)).finishSorting();
            if (completionCallback != null) {
                completionCallback.run();
            }
        });
    }

    private int findMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int value : arr) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                int x = i * COLUMN_WIDTH;
                int height = array[i];
                int y = getHeight() - height;
                if (i == movingColumnIndex) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(x, y, COLUMN_WIDTH, height);
            }
        }
    }

    private int[] generatorandmo(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * size);
        }
        return array;
    }

    private int[] generatosorto(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    private int[] generatoinverso(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }
}
