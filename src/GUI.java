/**
 * @class GUI
 * @description This class handles all the Java Swing GUI elements.
 * @author Damon Greenhalgh
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI{
    private GameBoard board;
    private int size;    
    private int numIterations;
    private JLabel lblNumIterations;
    private int delay;
    private Timer timer;
    private boolean timerState;

    public GUI() {
        this.board = new GameBoard(20);
        this.size = board.getSize();
        this.delay = 1000;    // one second delay

        // create frame and panels
        JFrame frame = new JFrame();  
        JPanel gridPanel = new JPanel(new GridLayout(size, size));
        JPanel optionsPanel = new JPanel(new GridLayout(1, 5));
        JPanel sliderPanel = new JPanel(new GridLayout(2, 1));

        ((GridLayout)optionsPanel.getLayout()).setHgap(10);
        ((GridLayout)optionsPanel.getLayout()).setVgap(10);
        
        // set up grid components
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                // grid details
                Node node = board.getNode(i, j);
                node.addActionListener(new MyGridListener());
                node.setBackground(Color.WHITE);
                gridPanel.add(node);
            }
        }

        // set up options components
        // start/stop button, used to start/stop the simulation
        timer = new Timer(delay, new MyStopStartListener());
        timerState = true;
        JButton btnStartStop = new JButton("Start");
        btnStartStop.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(timerState) {
                    timer.start();
                    timerState = false;
                    btnStartStop.setText("Stop");
                } else {
                    timer.stop();
                    timerState = true;
                    btnStartStop.setText("Start");
                }
            }
        });
        
        // clear button, used to clear the board and reset the number of iterations
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                board.clear();
                update();
                timer.stop();
                timerState = true;
                btnStartStop.setText("Start");
                numIterations = 0;
                lblNumIterations.setText("Number of Iterations: " + numIterations);
            }
        });

        // speed slider, used to change the delay of each new iteration
        JLabel lblSpeed = new JLabel("Speed: 1x");
        lblSpeed.setHorizontalAlignment(0);

        JSlider sdrSpeed = new JSlider(1, 8);
        sdrSpeed.setPaintTicks(true);
        sdrSpeed.setMinorTickSpacing(1);
        sdrSpeed.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                delay = 250 * Math.abs(sdrSpeed.getValue() - 8);
                timer.setDelay(Math.max(delay, 250));
                lblSpeed.setText("Speed: " + 250 * sdrSpeed.getValue() / 1000.0 + "x");
            }
        });

        // add slider components to slider panel
        sliderPanel.add(lblSpeed);
        sliderPanel.add(sdrSpeed);

        // number of iterations label, used to display the current number of iterations
        lblNumIterations = new JLabel("Number of Iterations: " + numIterations);
        lblNumIterations.setHorizontalAlignment(0);

        // add options components to optionPanel
        optionsPanel.add(btnStartStop);
        optionsPanel.add(btnClear);
        optionsPanel.add(sliderPanel);
        optionsPanel.add(lblNumIterations);

        // add panels to frame
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(optionsPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Conway's Game of Life");
        frame.setSize(800, 850);
        frame.setVisible(true);
    }

    public void update() {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                Node node = board.getNode(i, j);
                if(node.getState()) {
                    node.setBackground(Color.BLACK);
                } else {
                    node.setBackground(Color.WHITE);
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
            lblNumIterations.setText("Number of Iterations: " + numIterations);   
        }
    }

    private class MyGridListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Node node = (Node) e.getSource();
            if(node.getState() == false) {
                node.setState(true);
                node.setBackground(Color.BLACK);
            } else {
                node.setState(false);
                node.setBackground(Color.WHITE);
            }
        }
    }
}

