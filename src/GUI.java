/**
 * @class GUI
 * @description This class handles all the Java Swing GUI elements. Acts as the 
 * font end display for the game.
 * @author Damon Greenhalgh
 */

 // dependencies
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI{

    // Fields
    private JFrame frame;
    private JPanel gridPanel, optionsPanel, sliderPanel;
    private JLabel lblNumIterations;
    private JButton btnStart, btnClear, btnStep, btnRandom;
    private Font FONT;
    private GameBoard board;
    private ColorScanner colorScanner;
    private Color[][] colorMap;
    private Color DARKBLUE, DARKESTBLUE, GREEN, RED, ORANGE, PINK, PURPLE; 
    private Timer timer;
    private int rows, columns, scale, numIterations, delay; 
    private boolean timerState;


    /**
     * Constructor
     * Used to instantiate gui components.
     */
    public GUI() {
        // setup logical board
        scale = 2;   
        rows = 72 / scale;
        columns = 128 / scale;
        board = new GameBoard(rows, columns);

        // setup time delay(ms)
        delay = 400;   

        // setup grid color map
        File gradientFile = new File("design/gradient.png");
        colorScanner = ColorScanner.getColorScanner();
        colorScanner.scan(gradientFile, scale);
        colorMap = colorScanner.getColorMap();
        
        // setup preset colors
        DARKBLUE = new Color(36, 41, 46);
        DARKESTBLUE = new Color(31, 36, 40);
        GREEN = new Color(33, 255, 148);
        RED = new Color(255, 94, 81);
        ORANGE = new Color(255, 178, 67);
        PURPLE = new Color(129, 67, 255);
        PINK = new Color(219, 51, 255);

        // setup font
        FONT = new Font("Arial", Font.BOLD, 15);

        // setup frame
        frame = new JFrame(); 
        frame.setTitle("Conway's Game of Life");
        frame.setSize(1600, 980);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("design/icon.png").getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // setup panels
        gridPanel = new JPanel(new GridLayout(rows, columns));
        gridPanel.setPreferredSize(new Dimension(1600, 900));
        gridPanel.setBackground(DARKESTBLUE);

        optionsPanel = new JPanel(new FlowLayout());
        optionsPanel.setPreferredSize(new Dimension(200, 50));
        optionsPanel.setBackground(DARKESTBLUE);

        sliderPanel = new JPanel(new GridLayout(2, 1));
        sliderPanel.setBackground(DARKESTBLUE);

        // set up grid components
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                Node node = board.getNode(i, j);
                node.addActionListener(new MyGridListener());
                node.setBackground(DARKBLUE);
                node.setBorder(BorderFactory.createLineBorder(DARKESTBLUE, 1));
                gridPanel.add(node);
            }
        }

        // setup options components
        // start button, used to start/stop/continue the simulation
        btnStart = new JButton("START");
        btnStart.setPreferredSize(new Dimension(200, 30));
        btnStart.setFont(FONT);
        btnStart.setBackground(GREEN);
        btnStart.setForeground(DARKESTBLUE);
        btnStart.setBorderPainted(false);
        timer = new Timer(delay, new MyStepListener());
        timerState = true;
        btnStart.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(timerState) {
                    timer.start();
                    timerState = false;
                    btnStart.setText("STOP");
                    btnStart.setBackground(RED);
                } else {
                    timer.stop();
                    timerState = true;
                    btnStart.setText("CONTINUE");
                    btnStart.setBackground(ORANGE);
                }
            }
        });
        
        // clear button, used to clear the board and reset the number of iterations
        btnClear = new JButton("CLEAR");
        btnClear.setPreferredSize(new Dimension(100, 30));
        btnClear.setFont(FONT);
        btnClear.setForeground(DARKESTBLUE);
        btnClear.setBackground(PURPLE);
        btnClear.setBorderPainted(false);
        btnClear.addActionListener(new MyClearListener());

        // step button, used to generate the next step of the algorithm
        btnStep = new JButton("STEP");
        btnStep.setPreferredSize(new Dimension(100, 30));
        btnStep.setFont(FONT);
        btnStep.setForeground(DARKESTBLUE);
        btnStep.setBackground(PURPLE);
        btnStep.setBorderPainted(false);
        btnStep.addActionListener(new MyStepListener());

        // random button, randomzes each node on the board
        btnRandom = new JButton("RANDOM");
        btnRandom.setPreferredSize(new Dimension(200, 30));
        btnRandom.setFont(FONT);
        btnRandom.setForeground(DARKESTBLUE);
        btnRandom.setBackground(PINK);
        btnRandom.setBorderPainted(false);
        btnRandom.addActionListener(new MyRandomListener());

        // speed slider, used to change the delay of each new iteration
        // speed label
        JLabel lblSpeed = new JLabel("SPEED: 1x");
        lblSpeed.setPreferredSize(new Dimension(200, 10));
        lblSpeed.setFont(FONT);
        lblSpeed.setForeground(PURPLE);
        lblSpeed.setHorizontalAlignment(0);

        // speed slider
        JSlider sdrSpeed = new JSlider(1, 8);
        sdrSpeed.setBackground(DARKESTBLUE);
        sdrSpeed.setMinorTickSpacing(1);
        sdrSpeed.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                delay = 100 * Math.abs(sdrSpeed.getValue() - 8);
                timer.setDelay(Math.max(delay, 100));
                lblSpeed.setText("Speed: " + 250 * sdrSpeed.getValue() / 1000.0 + "x");
            }
        });

        // add slider components to slider panel
        sliderPanel.add(lblSpeed);
        sliderPanel.add(sdrSpeed);

        // number of iterations label, used to display the current number of iterations
        lblNumIterations = new JLabel("" + numIterations);
        lblNumIterations.setPreferredSize(new Dimension(100, 30));
        lblNumIterations.setFont(new Font("Arial", Font.BOLD, 30));
        lblNumIterations.setForeground(PURPLE);
        lblNumIterations.setHorizontalAlignment(0);

        // add options components to options panel
        optionsPanel.add(btnStart);
        optionsPanel.add(btnStep);
        optionsPanel.add(btnClear);
        optionsPanel.add(btnRandom);
        optionsPanel.add(sliderPanel);
        optionsPanel.add(lblNumIterations);

        // add panels to the frame
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(optionsPanel, BorderLayout.SOUTH);

        frame.setVisible(true); 
    }

    /**
     * Update
     * This method updates each node to display the correct status.
     */
    public void update() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                Node node = board.getNode(i, j);
                if(node.getState()) {    // alive
                    node.setBackground(colorMap[i][j]);
                } else {                 // dead
                    node.setBackground(DARKBLUE);
                }
            }
        }
    }

    private class MyRandomListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            board.random();
            update();
        }
    }

    private class MyStepListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            board.next();
            update();
            numIterations++;
            lblNumIterations.setText("" + numIterations);   
        }
    }

    private class MyClearListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            board.clear();
            update();
            timer.stop();
            timerState = true;
            btnStart.setText("START");
            btnStart.setBackground(GREEN);
            numIterations = 0;
            lblNumIterations.setText("" + numIterations);
        }
    }

    private class MyGridListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Node node = (Node) e.getSource();
            if(node.getState() == false) {
                node.setState(true);
                int[] coords = node.getCoords();
                node.setBackground(colorMap[coords[0]][coords[1]]);
            } else {
                node.setState(false);
                node.setBackground(DARKBLUE);
            }
        }
    }
}