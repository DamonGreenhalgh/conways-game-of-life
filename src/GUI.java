/**
 * @class GUI
 * @description This class handles all the Java Swing GUI elements.
 * @author Damon Greenhalgh
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI{
    private GameBoard board;
    private int size;    
    private int numIterations;
    private JLabel lblNumIterations;

    public GUI() {
        this.board = new GameBoard(25);
        size = board.getSize();

        // create frame and panels
        JFrame frame = new JFrame();  

        JPanel gridPanel = new JPanel(new GridLayout(size, size));

        GridLayout optionsLayout = new GridLayout(1, 4);
        optionsLayout.setHgap(5);
        optionsLayout.setVgap(5);
        JPanel optionsPanel = new JPanel(optionsLayout);
        
        // set up grid panel
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                // grid details
                Node node = board.getNode(i, j);
                node.addActionListener(new MyGridListener());
                node.setBackground(Color.WHITE);
                gridPanel.add(node);
            }
        }

        // set up options panel
        // start/stop button, used to start/stop the simulation
        JButton btnStartStop = new JButton("Start");
        btnStartStop.addActionListener(new AbstractAction(){
            private Timer timer = new Timer(1000, new nextIteration());
            private boolean timerState = true;
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
            public void actionPerformed(ActionEvent e) {
                board.clear();
                update();
                numIterations = 0;
                lblNumIterations.setText("Number of Iterations: " + numIterations);
            }
        });

        // number of iterations label, used to display the current number of iterations
        lblNumIterations = new JLabel("Number of Iterations: " + numIterations);
        lblNumIterations.setHorizontalAlignment(0);

        // add options components to optionPanel
        optionsPanel.add(btnStartStop);
        optionsPanel.add(btnClear);
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

    private class nextIteration implements ActionListener {
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
            board.display();
        }
    }
}

