/**
 * @class GUI
 * @description This class handles all the Java Swing GUI elements.
 * @author Damon Greenhalgh
 */

 // DEPENDENCIES
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class GUI{
    private GameBoard board;
    private ColorScanner colorScanner;
    private Color[][] colorMap;
    private int rows, columns, numIterations, delay;   
    private Color DARKBLUE, DARKESTBLUE, GREEN, RED, SATBLUE; 
    private JLabel lblNumIterations;
    private Timer timer;
    private boolean timerState;
    

    public GUI() {
        // FIELDS
        // setup logical board
        board = new GameBoard(36, 64);
        rows = board.getRows();
        columns = board.getColumns();

        // setup time delay(ms)
        delay = 400;   

        // setup grid active color
        colorScanner = new ColorScanner();
        colorScanner.scan(new File("gradient-3.png"));
        colorMap = colorScanner.getColorMap();
        
        // setup preset colors
        DARKBLUE = new Color(36, 41, 46);
        DARKESTBLUE = new Color(31, 36, 40);
        GREEN = new Color(33, 255, 148);
        RED = new Color(255, 94, 81);
        SATBLUE = new Color(33, 255, 148);

        // create frame and panels
        JFrame frame = new JFrame();  

        JPanel gridPanel = new JPanel(new GridLayout(rows, columns));
        gridPanel.setPreferredSize(new Dimension(1400, 900));
        gridPanel.setBackground(DARKESTBLUE);

        JPanel optionsPanel = new JPanel(new FlowLayout());
        optionsPanel.setPreferredSize(new Dimension(200, 50));
        optionsPanel.setBackground(DARKESTBLUE);

        JPanel sliderPanel = new JPanel(new GridLayout(2, 1));
        sliderPanel.setBackground(DARKESTBLUE);

        // set up grid components
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                // grid details
                Node node = board.getNode(i, j);
                node.addActionListener(new MyGridListener());
                node.setBackground(DARKBLUE);
                node.setBorder(BorderFactory.createLineBorder(DARKESTBLUE, 1));
                gridPanel.add(node);
            }
        }

        // set up options components
        // start/stop button, used to start/stop the simulation
        JButton btnStartStop = new JButton("Start");
        btnStartStop.setPreferredSize(new Dimension(200, 30));
        btnStartStop.setBackground(GREEN);
        btnStartStop.setForeground(DARKESTBLUE);
        btnStartStop.setBorderPainted(false);
        timer = new Timer(delay, new MyStopStartListener());
        timerState = true;
        btnStartStop.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(timerState) {
                    timer.start();
                    timerState = false;
                    btnStartStop.setText("Stop");
                    btnStartStop.setBackground(RED);
                } else {
                    timer.stop();
                    timerState = true;
                    btnStartStop.setText("Start");
                    btnStartStop.setBackground(GREEN);
                }
            }
        });
        
        // clear button, used to clear the board and reset the number of iterations
        JButton btnClear = new JButton("Clear");
        btnClear.setPreferredSize(new Dimension(100, 30));
        btnClear.setForeground(DARKESTBLUE);
        btnClear.setBackground(SATBLUE);
        btnClear.setBorderPainted(false);
        btnClear.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                board.clear();
                update();
                timer.stop();
                timerState = true;
                btnStartStop.setText("Start");
                btnStartStop.setBackground(GREEN);
                numIterations = 0;
                lblNumIterations.setText("Iteration # " + numIterations);
            }
        });

        // speed slider, used to change the delay of each new iteration
        JLabel lblSpeed = new JLabel("Speed: 1x");
        lblSpeed.setPreferredSize(new Dimension(100, 10));
        lblSpeed.setForeground(SATBLUE);
        lblSpeed.setHorizontalAlignment(0);

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
        lblNumIterations = new JLabel("Iteration # " + numIterations);
        lblNumIterations.setPreferredSize(new Dimension(100, 30));
        lblNumIterations.setForeground(SATBLUE);
        lblNumIterations.setHorizontalAlignment(0);

        // add options components to optionPanel
        optionsPanel.add(btnStartStop);
        optionsPanel.add(btnClear);
        optionsPanel.add(sliderPanel);
        optionsPanel.add(lblNumIterations);

    
        // add panels to frame
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(optionsPanel, BorderLayout.SOUTH);
        
        frame.setIconImage(new ImageIcon("icon.png").getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Conway's Game of Life");
        frame.setSize(1600, 900 + 50);
        frame.setVisible(true);
    }

    

    public void update() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                Node node = board.getNode(i, j);
                if(node.getState()) {
                    node.setBackground(colorMap[i][j]);
                } else {
                    node.setBackground(DARKBLUE);
                }
            }
        }
    }

    private class MyStopStartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            board.next();
            update();
            numIterations++;
            lblNumIterations.setText("Iteration # " + numIterations);   
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

