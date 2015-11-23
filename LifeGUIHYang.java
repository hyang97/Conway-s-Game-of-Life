/* Henry Yang, Period 3, 3/23/14
 * Around 2 hours.
 * This lab wasn't as bad as I thought it would be, since
 * most of the code had already been written with simpledraw
 * and life. The hardest part of this lab was making the grid
 * size variable and doing mathing all the coordinates and 
 * resizing magic.
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.io.*;

public class LifeGUIHYang extends MouseAdapter implements ActionListener, ChangeListener{
    Color[][] grid;
    Color col = Color.red;
    JRadioButton red = new JRadioButton("Red", true);
    JRadioButton green = new JRadioButton("Green");
    JRadioButton blue = new JRadioButton("Blue");
    JRadioButton custom = new JRadioButton("Custom");
    ButtonGroup colors = new ButtonGroup();
    JPanel buttons = new JPanel();
    
    JFrame window = new JFrame("Conway's Game of Life!!!");
    MyJPanel drawingPad = new MyJPanel();
    
    JMenuBar menu = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenu edit = new JMenu("Edit");
    JMenuItem open = new JMenuItem("Open", 'o');
    JMenuItem save = new JMenuItem("Save", 's');
    JMenuItem menuClear = new JMenuItem("Clear", 'c');
    JButton clear = new JButton("Clear");
    
    LifeHYang magic;
    
    
    JButton nextGen = new JButton("Next Gen");
    JToggleButton continuous = new JToggleButton("Run next generations");
    javax.swing.Timer delay;
    
    int delayTime = 1000;
    JSlider changeDelay;
    
    int frameWidth = 1200;
    int frameHeight = 660;
    
    int numGrid = 20;
    int gridLength = 600/numGrid * numGrid + 2;
    
    JSlider changeGrid;
    
    JLabel generationNumber = new JLabel("Generation 0");
    int genNum = 0;
    public static void main(String[] args){
        LifeGUIHYang test = new LifeGUIHYang();
        test.createGUI();
    }
    
    public void createGUI(){
        grid = new Color[numGrid][numGrid];
        for(int r = 0; r<numGrid; r++){
            for(int c = 0; c<numGrid; c++){
                grid[r][c] = Color.white;
            }
        }
        
        
        
        //create jframe
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        
        int screenWidth = (int)(window.getToolkit().getScreenSize().getWidth());
        int screenHeight = (int)(window.getToolkit().getScreenSize().getHeight());
        
        
        
        window.setBounds(screenWidth/2 - frameWidth/2, screenHeight/2 - frameHeight/2, frameWidth, frameHeight);
        window.setLayout(null);
        
        //add drawing grid
        window.add(drawingPad);
        
        drawingPad.setBounds(5,5,gridLength,gridLength);
        drawingPad.setBackground(Color.white);
        
        drawingPad.addMouseListener(this);
        drawingPad.addMouseMotionListener(this);
        System.out.println("added listener");
        //add color section and stuff
        colors.add(red);
        colors.add(green);
        colors.add(blue);
        colors.add(custom);
        
        buttons.add(red);
        buttons.add(green);
        buttons.add(blue);
        buttons.add(custom);
        buttons.setBorder(new TitledBorder("Drawing Color"));
        buttons.setBackground(null);
        
        //MyButtonListener this = new MyButtonListener();
        red.addActionListener(this);
        green.addActionListener(this);
        blue.addActionListener(this);
        custom.addActionListener(this);
        
        window.add(buttons);
        int boxWidth = 400;
        int boxHeight = 60;
        buttons.setBounds(1001-boxWidth/2, 20, boxWidth, boxHeight);
        
        //add nextGen button
        window.add(nextGen);
        int buttWidth = 100;
        int buttHeight = 40;
        nextGen.setBounds(1001 - buttWidth/2, 80, buttWidth, buttHeight);
        nextGen.addActionListener(this);
        
        //add continuous button
        window.add(continuous);
        int contWidth = 300;
        int contHeight = 40;
        continuous.setBounds(1001-contWidth/2, 120, contWidth, contHeight);
        continuous.addActionListener(this);
        
        //create timer object
        delay = new javax.swing.Timer(delayTime,this);
        
        //add JSlider to change delay
        changeDelay = new JSlider(SwingConstants.HORIZONTAL,0,500,250);
        changeDelay.setMajorTickSpacing(100);
        changeDelay.setMinorTickSpacing(25);
        changeDelay.setPaintTicks(true);
        changeDelay.setPaintLabels(true);
        changeDelay.setBorder(new TitledBorder("Delay Time (ms)"));
        
        window.add(changeDelay);
        int sliderWidth = 300;
        int sliderHeight = 60;
        changeDelay.setBounds(1001-sliderWidth/2, 160, sliderWidth, sliderHeight);
        changeDelay.addChangeListener(this);
        
        //add JSlider to change grid size
        changeGrid = new JSlider(SwingConstants.HORIZONTAL, 10, 100, 20);
        changeGrid.setMajorTickSpacing(90);
        changeGrid.setMinorTickSpacing(10);
        changeGrid.setPaintTicks(true);
        changeGrid.setPaintLabels(true);
        changeGrid.setBorder(new TitledBorder("Number of grids per side (>80 no grid lines)"));
        
        window.add(changeGrid);
        int gridSliderWidth = 300;
        int gridSliderHeight = 60;
        changeGrid.setBounds(1001-gridSliderWidth/2, 220, gridSliderWidth, gridSliderHeight);
        changeGrid.addChangeListener(this);
        
        //add clear button
        window.add(clear);
        int clearWidth = 100;
        int clearHeight = 60;
        clear.setBounds(1001-clearWidth/2, 280, clearWidth, clearHeight);
        clear.addActionListener(this);
        
        //add generation number jlabel
        window.add(generationNumber);
        int labelWidth = 200;
        int labelHeight = 50;
        generationNumber.setBounds(1001-labelWidth/2, 400, labelWidth, labelHeight);
        
        
        //add menu
        file.add(open);
        file.add(save);
        edit.add(menuClear);
        menu.add(file);
        menu.add(edit);
        window.setJMenuBar(menu);
        open.addActionListener(this);
        save.addActionListener(this);
        menuClear.addActionListener(this);
        
        System.out.println(col.getRed() +  " ");
        window.setVisible(true);
        
        
    }
    
    
    
    private class MyJPanel extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            
            int side = getWidth();
            int gridSize = side/numGrid;
            if(numGrid<80){
                for(int r = 0; r<numGrid; r++){
                    for(int c = 0; c<numGrid; c++){
                        g2.setColor(Color.black);
                        g2.drawRect(1 + c*gridSize, 1 + r*gridSize, gridSize, gridSize);
                        if(grid[r][c] != Color.white){
                            g2.setColor(grid[r][c]);
                            g2.fillRect(2 + c*gridSize, 2 + r*gridSize, gridSize-1, gridSize-1);
                        }
                        
                    }
                }
            }
            else{
                for(int r = 0; r<numGrid; r++){
                    for(int c = 0; c<numGrid; c++){
                        if(grid[r][c] != Color.white){
                            g2.setColor(grid[r][c]);
                            g2.fillRect(2 + c*gridSize, 2 + r*gridSize, gridSize-1, gridSize-1);
                        }
                        
                    }
                }
            }
            
            
            
        }
    }
    
    public void stateChanged(ChangeEvent e){
        if(e.getSource() == changeDelay){
            delayTime = changeDelay.getValue();
            delay.setDelay(delayTime);
        }
        else if(e.getSource() == changeGrid){
            numGrid = changeGrid.getValue();
            gridLength = 600/numGrid * numGrid + 2;
            grid = new Color[numGrid][numGrid];
            for(int r = 0; r<numGrid; r++){
                for(int c = 0; c<numGrid; c++){
                    grid[r][c] = Color.white;
                }
            }
            genNum = 0;
            generationNumber.setText("Generation " + genNum);
            drawingPad.setBounds(5,5,gridLength,gridLength);
            drawingPad.repaint();
        }
    }
    
    public void actionPerformed(ActionEvent e){
    
        if(e.getSource() == red){
            col = Color.red;
        }
        else if(e.getSource() == green){
            col = Color.green;
        }
        else if(e.getSource() == blue){
            col = Color.blue;
        }
        else if(e.getSource() == custom){
            col = JColorChooser.showDialog(null, "yay pretty colors", col);
        }
        else if(e.getSource() == menuClear || e.getSource() == clear){
            grid = new Color[numGrid][numGrid];
            for(int r = 0; r<numGrid; r++){
                for(int c = 0; c<numGrid; c++){
                    grid[r][c] = Color.white;
                }
            }
            genNum = 0;
            generationNumber.setText("Generation " + genNum);
            if(continuous.isSelected()){
                delay.stop();
                continuous.setSelected(false);
            }
            drawingPad.repaint();
        }
        else if(e.getSource() == nextGen){
            magic = new LifeHYang(grid);
            grid = magic.nextGeneration(grid,col);
            genNum++;
            generationNumber.setText("Generation " + genNum);
            drawingPad.repaint();
        }
        else if(e.getSource() == continuous){
            if(continuous.isSelected()){
                delay.start();
            }
            else if(!continuous.isSelected()){
                delay.stop();
            }
        }
        else if(e.getSource() == delay){
            magic = new LifeHYang(grid);
            grid = magic.nextGeneration(grid,col);
            genNum++;
            generationNumber.setText("Generation " + genNum);
            drawingPad.repaint();
        }
        else if(e.getSource() == open){
            JFileChooser chooseFile = new JFileChooser();
            chooseFile.showOpenDialog(null);
            
            try{
                File saveFile = chooseFile.getSelectedFile();
                Scanner in = new Scanner(saveFile);
                if(in.nextLine().compareTo("P3") != 0){
                    System.out.println("Wrong file format!");
                }
                else{
                    int counter = 1;
                    int row = 0;
                    int col = 0;
                    int maxColVal = 0;
                    while(in.hasNext()){
                        String line = in.nextLine();
                        if(line.charAt(0) == '#'){
                            
                        }
                        else{
                            if(counter == 1){
                                String dim1 = "";
                                String dim2 = "";
                                int spacePos = 0;
                                for(int i = 0; i<line.length(); i++){
                                    if(line.charAt(i) == ' '){
                                        spacePos = i;
                                        i = line.length();
                                        
                                    }
                                    else{
                                        dim1 += line.charAt(i);
                                    }
                                }
                                row = Integer.parseInt(dim1);
                                for(int i = spacePos+1; i<line.length(); i++){
                                    
                                    dim2 += line.charAt(i);
                                    
                                }
                                col = Integer.parseInt(dim2);
                                counter++;
                                
                                numGrid = row;
                                grid = new Color[numGrid][numGrid];
                                for(int r = 0; r<numGrid; r++){
                                    for(int c = 0; c<numGrid; c++){
                                        grid[r][c] = Color.white;
                                    }
                                }
                                
                                gridLength = 600/numGrid * numGrid + 2;
                                
                                
                            }
                            else if(counter == 2){
                                maxColVal = Integer.parseInt(line);
                                counter++;
                            }
                            else{
                                for(int i = 0; i<col; i++){
                                    int offset = i*12;
                                    String redSubstring = line.substring(offset, offset+3);
                                    String greenSubstring = line.substring(offset+4, offset+7);
                                    String blueSubstring = line.substring(offset + 8, offset+11);
                                    
                                    for(int x = 0; x<redSubstring.length(); x++){
                                        if(redSubstring.charAt(x) == ' '){
                                            redSubstring = redSubstring.substring(x+1);
                                            x--;
                                        }
                                    }
                                    for(int x = 0; x<greenSubstring.length(); x++){
                                        if(greenSubstring.charAt(x) == ' '){
                                            greenSubstring = greenSubstring.substring(x+1);
                                            x--;
                                        }
                                    }
                                    for(int x = 0; x<blueSubstring.length(); x++){
                                        if(blueSubstring.charAt(x) == ' '){
                                            blueSubstring = blueSubstring.substring(x+1);
                                            x--;
                                        }
                                    }
                                    
                                    
                                    
                                    int red = Integer.parseInt(redSubstring);
                                    int green = Integer.parseInt(greenSubstring);
                                    int blue = Integer.parseInt(blueSubstring);
                                    
                                    int rowPos = (counter-3)/row;
                                    int colPos = (counter-3)%row;
                                    
                                    grid[rowPos][colPos] = new Color(red,green,blue);
                                    
                                    
                                    
                                    counter++;
                                }
                            }
                        }
                        
                    }
                }
            }catch(IOException i){
                System.out.println(i.getMessage());
            }
            
            drawingPad.setBounds(5,5,gridLength,gridLength);
            drawingPad.repaint();
            
        }
        else if(e.getSource() == save){
            String end = "\r\n";
            JFileChooser chooseFile = new JFileChooser();
            chooseFile.showSaveDialog(null);
            try{
                
                File saveFile = chooseFile.getSelectedFile();
                FileWriter output = new FileWriter(saveFile);
                output.write("P3" + end);
                output.write("#this is a comment" + end);
                output.write(numGrid + " " + numGrid + end);
                output.write("255" + end);
                for(int r = 0; r<numGrid; r++){
                    for(int c = 0; c<numGrid; c++){
                        
                        output.write(String.format("%4s", (grid[r][c].getRed() + " ")));
                        output.write(String.format("%4s", (grid[r][c].getGreen() + " ")));
                        output.write(String.format("%4s", (grid[r][c].getBlue() + " ")));
                    }
                    output.write(end);
                }
                output.close();
            }catch(IOException i){
                System.out.println(i.getMessage());
            }
        }
        
    }

    public void mouseClicked(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1){
            int xPos = e.getX();
            int yPos = e.getY();
            int divisor = gridLength/numGrid;
            grid[yPos/divisor][xPos/divisor] = col;
            drawingPad.repaint();
        }
        else if(e.getButton() == MouseEvent.BUTTON3){
            int xPos = e.getX();
            int yPos = e.getY();
            int divisor = gridLength/numGrid;
            grid[yPos/divisor][xPos/divisor] = Color.white;
            drawingPad.repaint();
        }
    }
    
    public void mouseDragged(MouseEvent e) {
        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK){
            int xPos = e.getX();
            int yPos = e.getY();
            int divisor = gridLength/numGrid;
            grid[yPos/divisor][xPos/divisor] = col;
            drawingPad.repaint();
        }
        else if((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK){
            int xPos = e.getX();
            int yPos = e.getY();
            int divisor = gridLength/numGrid;
            grid[yPos/divisor][xPos/divisor] = Color.white;
            drawingPad.repaint();
        }
    }
    
}