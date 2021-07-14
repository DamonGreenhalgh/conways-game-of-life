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

public class GUI extends JFrame{

    // Fields
    private JPanel gridPanel, optionsPanel, sliderPanel;
    private JLabel lblNumIterations, lblSpeed, lblTheme;
    private JButton btnStart, btnReset, btnStep, btnRandom, btnBrush, btnQuit;
    private JSlider sdrSpeed;
    private JComboBox<ThemeType> cmbTheme; 
    private JComboBox<BrushType> cmbBrush;
    private Font font;
    private GameBoard board;
    private ColorScanner colorScanner;
    private Color[][] colorMap;
    private Color fgColor, bgColor, startColor, stopColor, continueColor, mainColor; 
    private Timer timer;
    private File gradientFile;
    private BrushType brush;
    private int rows, columns, scale, numIterations, delay; 
    private boolean timerState, brushState;

    /**
     * Constructor
     * Used to instantiate gui components.
     */
    public GUI() {
        
        // setup color scanner
        colorScanner = ColorScanner.getColorScanner();
        
        // setup colors
        fgColor = CustomColor.DARKESTBLUE;
        bgColor = CustomColor.DARKBLUE;
        startColor = CustomColor.GREEN;
        stopColor = CustomColor.RED;
        continueColor = CustomColor.ORANGE;
        mainColor = CustomColor.PURPLE;

        // setup font
        font = new Font("Arial", Font.BOLD, 15);

        // setup logical board
        scale = 1;   
        rows = 51 / scale;    
        columns = 96 / scale;       
        board = new GameBoard(rows, columns);

        // setup frame
        setTitle("Conway's Game of Life");
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("design/icon.png").getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setUndecorated(true);

        // setup panels
        gridPanel = new JPanel(new GridLayout(rows, columns));
        gridPanel.setPreferredSize(new Dimension(1920, 1020));

        optionsPanel = new JPanel();
        optionsPanel.setPreferredSize(new Dimension(1920, 50));

        sliderPanel = new JPanel(new GridLayout(2, 1));

        // set up grid components
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                Node node = board.getNode(i, j);
                node.addActionListener(new MyGridListener());
                gridPanel.add(node);
            }
        }

        // start button, used to start/stop/continue the simulation
        btnStart = new JButton("START");
        btnStart.setPreferredSize(new Dimension(200, 30));
        btnStart.setFont(font);
        btnStart.setBorderPainted(false);
        timer = new Timer(400, new MyStepListener());
        timerState = true;
        btnStart.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(timerState) {
                    timer.start();
                    timerState = false;
                    btnStart.setText("STOP");
                    btnStart.setBackground(stopColor);
                } else {
                    timer.stop();
                    timerState = true;
                    btnStart.setText("CONTINUE");
                    btnStart.setBackground(continueColor);
                }
            }
        });
        
        // reset button, used to clear the board and reset the number of iterations
        btnReset = new JButton("RESET");
        btnReset.setPreferredSize(new Dimension(100, 30));
        btnReset.setFont(font);
        btnReset.setBorderPainted(false);
        btnReset.addActionListener(new MyClearListener());

        // step button, used to generate the next step of the algorithm
        btnStep = new JButton("STEP");
        btnStep.setPreferredSize(new Dimension(100, 30));
        btnStep.setFont(font);
        btnStep.setBorderPainted(false);
        btnStep.addActionListener(new MyStepListener());

        // random button, randomzes each node on the board
        btnRandom = new JButton("RANDOM");
        btnRandom.setPreferredSize(new Dimension(150, 30));
        btnRandom.setFont(font);
        btnRandom.setBorderPainted(false);
        btnRandom.addActionListener(new MyRandomListener());

        // brush/eraser button, used to swap between brush mode and eraser mode
        brushState = true;
        brush = BrushType.SINGLE;
        btnBrush = new JButton("BRUSH");
        btnBrush.setPreferredSize(new Dimension(120, 30));
        btnBrush.setFont(font);
        btnBrush.setBorderPainted(false);
        btnBrush.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(brushState) {
                    brushState = false;
                    btnBrush.setText("ERASER");
                } else {
                    brushState = true;
                    btnBrush.setText("BRUSH");
                }
            }
        });

        // preset combobox, draws preset structure on the grid
        cmbBrush = new JComboBox<BrushType>(BrushType.values());
        cmbBrush.setFont(font);
        cmbBrush.setForeground(mainColor);
        cmbBrush.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unchecked") JComboBox<BrushType> box = (JComboBox<BrushType>)e.getSource();
                brush = (BrushType)box.getSelectedItem();
            }
        });

        // speed slider, used to change the delay of each new iteration
        // speed label
        lblSpeed = new JLabel("SPEED: 1x");
        lblSpeed.setPreferredSize(new Dimension(200, 10));
        lblSpeed.setFont(font);
        lblSpeed.setHorizontalAlignment(0);

        // speed slider
        sdrSpeed = new JSlider(1, 8);
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
        lblNumIterations.setForeground(mainColor);
        lblNumIterations.setHorizontalAlignment(0);

        //theme label
        lblTheme = new JLabel("THEME: ");
        lblTheme.setPreferredSize(new Dimension(70, 30));
        lblTheme.setFont(font);
        
        // theme combobox, used to change themes for the application
        cmbTheme = new JComboBox<ThemeType>(ThemeType.values());
        cmbTheme.setFont(font);
        cmbTheme.setForeground(mainColor);
        cmbTheme.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unchecked") JComboBox<ThemeType> box = (JComboBox<ThemeType>)e.getSource();
                ThemeType selectedTheme = (ThemeType)box.getSelectedItem();
                changeTheme(selectedTheme);
            }
        });

        

        // quit
        btnQuit = new JButton("QUIT");
        btnQuit.setFont(font);
        btnQuit.setPreferredSize(new Dimension(100, 30));
        btnQuit.setBorderPainted(false);
        btnQuit.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        // default theme is dark
        changeTheme(ThemeType.LIGHT);

        // add options components to options panel
        optionsPanel.add(btnStart);
        optionsPanel.add(btnStep);
        optionsPanel.add(btnReset);
        optionsPanel.add(btnRandom);
        optionsPanel.add(btnBrush);
        optionsPanel.add(cmbBrush);
        optionsPanel.add(sliderPanel);
        optionsPanel.add(lblNumIterations);
        optionsPanel.add(lblTheme);
        optionsPanel.add(cmbTheme);
        optionsPanel.add(btnQuit);
        optionsPanel.add(btnQuit);

        // add panels to the frame
        add(gridPanel, BorderLayout.CENTER);
        add(optionsPanel, BorderLayout.SOUTH);
        setVisible(true); 
    }

    /**
     * Update
     * This method updates each node to display the correct color.
     */
    public void update() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                Node node = board.getNode(i, j);
                if(node.getState()) {    // alive
                    node.setBackground(colorMap[i][j]);
                } else {                 // dead
                    node.setBackground(bgColor);
                }
            }
        }
    }

    /**
     * ChangeTheme
     * This method changes the theme of the application by change background and 
     * foreground colors of the components
     * 
     * @param theme    the theme to change to
     */
    public void changeTheme(ThemeType theme) {

        // set foreground and background colors
        switch (theme) {
            case DARK: {
                // dark theme
                fgColor = CustomColor.DARKESTBLUE;
                bgColor = CustomColor.DARKBLUE;
                gradientFile = new File("design/gradient-dark.png");
                break;
            } case FLAT: {
                // flat theme
                fgColor = CustomColor.WHITE;
                bgColor = CustomColor.OFFWHITE;
                gradientFile = new File("design/flat.png");
                break;
            } case LIGHT: {
                // light theme
                fgColor = CustomColor.WHITE;
                bgColor = CustomColor.OFFWHITE;
                gradientFile = new File("design/gradient-light.png");
                break;
            }
        }

        // generate color map for background
        colorScanner.scan(gradientFile, scale);
        colorMap = colorScanner.getColorMap();

        // edit component colors
        gridPanel.setBackground(fgColor);
        optionsPanel.setBackground(fgColor);
        sliderPanel.setBackground(fgColor);

        btnQuit.setBackground(stopColor);
        btnQuit.setForeground(fgColor);

        btnStart.setBackground(startColor);
        btnStart.setForeground(fgColor);

        btnStep.setBackground(mainColor);
        btnStep.setForeground(fgColor);
        
        btnReset.setBackground(mainColor);
        btnReset.setForeground(fgColor);

        btnRandom.setBackground(mainColor);
        btnRandom.setForeground(fgColor);

        lblSpeed.setForeground(mainColor);
        sdrSpeed.setBackground(fgColor);

        cmbBrush.setBackground(fgColor);
        cmbTheme.setBackground(fgColor);

        btnBrush.setBackground(mainColor);
        btnBrush.setForeground(fgColor);

        lblTheme.setBackground(bgColor);
        lblTheme.setForeground(mainColor);

        btnQuit.setBackground(stopColor);
        btnQuit.setForeground(fgColor);
        
        
        // edit node colors
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                Node node = board.getNode(i, j);
                node.setBorder(BorderFactory.createLineBorder(fgColor, 1));
                if(node.getState()) {
                    node.setBackground(colorMap[i][j]);
                } else {
                    node.setBackground(bgColor);
                }
            }
        }
    }

    /**
     * DrawPreset
     * Draws the preset structure on the grid, the location of the mouse is the
     * top left corner of the preset structure.
     * 
     * @param pt        the preset structure to draw
     * @param row       the row index to start drawing at
     * @param column    the column index to start drawing at
     */
    public void drawPreset(BrushType pt, int row, int column) {
        board.clear();
        board.preset(pt, brushState, row, column);
        update();
    }

    /**
     * MyRandomListener
     * This is a listener for the random button, calls the random method for the
     * board object.
     */
    private class MyRandomListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            board.random();
            update();
        }
    }

    /**
     * MyStepListener
     * Listener for the step button, generates the next iteration and updates the
     * board for a single iteration.
     */
    private class MyStepListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean deadLocked = board.next();
            update();
            numIterations++;
            lblNumIterations.setText("" + numIterations);
            
            // occurs when the game hits a static state where the need of incrementing 
            // numIterations becomes redundant.
            if(deadLocked) {
                timer.stop();
                timerState = true;

                // disable buttons
                btnStart.setEnabled(false);
                btnStep.setEnabled(false);
                btnRandom.setEnabled(false);
                btnBrush.setEnabled(false);

                // style
                btnStart.setText("CONTINUE");
                btnStart.setBackground(bgColor);
                btnStep.setBackground(bgColor);
                btnRandom.setBackground(bgColor);
                btnBrush.setBackground(bgColor);

                // popup to display number of iterations
                // ?
            }
        }
    }

    /**
     * MyClearListener
     * Listener for the clear button, clears the board by setting all nodes to false.
     */
    private class MyClearListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            board.clear();
            update();
            timer.stop();
            timerState = true;
            numIterations = 0;

            // enable buttons
            btnStart.setEnabled(true);
            btnStep.setEnabled(true);
            btnRandom.setEnabled(true);
            btnBrush.setEnabled(true);

            // style
            btnStart.setText("START");
            btnStart.setBackground(startColor);
            btnStep.setBackground(mainColor);
            btnRandom.setBackground(mainColor);
            btnBrush.setBackground(mainColor);
            lblNumIterations.setText("" + numIterations);
        }
    }

    /**
     * MyGridListener
     * Listener for nodes on the grid, sets the node to dead or alive depending on its
     * current state.
     */
    private class MyGridListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Node node = (Node) e.getSource();
            int[] coords = node.getCoords();
            board.preset(brush, brushState, coords[0], coords[1]);
            update();
            
        }
    }
}