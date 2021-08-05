package kmapSolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.border.*;

/*******************************************************
Dominick Lamastra
4 variable Kmap Solver using Quine McCluskey algorithm
completed January 2021
finalized August 2021
*******************************************************/



public class FourVariable extends JFrame implements ActionListener {
  
  private JLabel[] binaryValues; // holds the format of the binary representations of the numbers in the kmap
  private JButton[] bits; // holds the buttons to change the state of the binary values
  private static JLabel[] map; // displays the state of the current minterm on the kmap
  private JButton[] subcubes; // keeps track of what loops have already been created
  
  // different arrays to hold the borders for each respective length of bits captured in the border and location of bits
  private static JLabel[] oneWordFullBorders;
  private static JLabel[] twoWordFullBorders;
  private static JLabel[] fourWordFullBorders;
  private static JLabel[] eightWordFullBorders;
  private static JLabel[][] twoWordWrapAroundBorders;
  private static JLabel[][] fourWordWrapAroundBorders;
  private static JLabel[][] eightWordWrapAroundBorders;
  private JLabel[] sixteenWordBorder;
  private static JLabel[] corners;
  
  private static boolean[] addBorder; // determines which bits are valid for receiving a border 
  
  // allows the user to select bits to potentially receive a border
  private JCheckBox[] boxBits; 
  private boolean[] addToKmap;
   
  private int[][] locations; // the locations of where each minterm is placed on the map
  private JButton compute; // starts the process for a border to be placed on a specific set of minterms in the kmap
  private JButton fillKmap; // the user clicks this button if they want the kmap to be filled with 1's
  private JButton reset; // the user clicks this button if they want the kmap to be filled with 0's
  private JButton showMinimal; // the user clicks this button if they want to see a minimum sum of products
  private JButton clearLoops; // the user clicks this button to set all loops to invisible
  private JButton change; // the user clicks this button if they want to change to a 3 variable Kmap
  private JButton help; // takes the user to a page with more information
  private JButton about; // takes the user to the credits page
  private static JLabel status; // shows the user if they have solved the correct kmap
  private static JTextArea showCanonical; // the area where the Canonical expression is shown
  private static JTextArea showKmap; // the area where the user's inputed loops are shown
  private static JTextArea showMinimalSoP; // the area where the minimum sum of products are shown
  private static String canonical; // the canonical expression
  private static String kmap; // what loops the user has created
  private static String minimalSoP = ""; // the minimum sum of products for a given canonical expression
  private static JLabel minimalSoPLabel; // labels the minimal sum of products
  
  private int[] countArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Used to implement the menu where the user selects the boxes
  private static String[] MintermArray = {"0","1","X"}; // possible symbols that can be added to the kmap
  private Color[] colors = {new Color(0, 51, 102), new Color(255, 153, 102), new Color(153, 150, 204), new Color(0, 0, 0),
  new Color(102, 153, 0), new Color(102, 255, 153), new Color(0, 102, 0), new Color(51, 51, 0),
  new Color(204, 153, 0), new Color(100, 100, 100), new Color(255, 153, 51), new Color(255, 51, 0),
  new Color(153, 0, 51), new Color(255, 51, 204), new Color(102, 0, 102), new Color(102, 102, 153)}; // used to give every value in the kmap a new color
  
  private JPanel pane = new JPanel(); // JPanel used for the scroll bar
  private static JScrollPane minScrollPane;
  
  // constructor
  public FourVariable() {
     
    // set up the JFrame and pane
    setTitle("Kmap Solver with Four Variables"); 
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    setBounds(x - 350, 0, 700, (int)(dimension.getHeight() * 0.9));
    pane.setLayout(new GroupLayout(pane));
    this.pane.setBounds(0, 0, 1000, 1000);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);
    
    // add a scroll pane
    this.pane.setPreferredSize(new Dimension(1000, 1000));
    final JScrollPane scrollp = new JScrollPane(this.pane, 
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.add(scrollp);
    
    binaryValues = new JLabel[16]; // the bits 0000-1111
    bits = new JButton[16]; // a 0, 1 , or an X
    map = new JLabel[16]; // the 0's and 1's in the kmap
    boxBits = new JCheckBox[16]; // the boxes below the Kmap
    addToKmap = new boolean[16]; // to check if the value is checked or not in Box bits
    addBorder = new boolean[16]; // to check if the value should be added to a border
    subcubes = new JButton[16]; // keeps track of what loops have already been created
    locations = new int[16][4]; // the locations of the map      

    
    
    
    // Used to label the columns of the Kmap
    JLabel[] greyCodeHorizontal = new JLabel[4];
    greyCodeHorizontal[0] = new JLabel("00");
    greyCodeHorizontal[1] = new JLabel("01");
    greyCodeHorizontal[2] = new JLabel("11");
    greyCodeHorizontal[3] = new JLabel("10");
    
    // Used to label the rows of the Kmap
    JLabel[] greyCodeVertical = new JLabel[4]; 
    greyCodeVertical[0] = new JLabel("00");
    greyCodeVertical[1] = new JLabel("01");
    greyCodeVertical[2] = new JLabel("11");
    greyCodeVertical[3] = new JLabel("10");
    
    // Used to show that AB is labeling the rows
    JLabel rowLabel = new JLabel("AB");
    Dimension size = rowLabel.getPreferredSize();
    rowLabel.setBounds(172, 185, size.width, size.height);
    pane.add(rowLabel);
    
    // Used to show that CD is labeling the columns
    JLabel columnLabel = new JLabel("CD");
    size = columnLabel.getPreferredSize();
    columnLabel.setBounds(200, 150, size.width, size.height);
    pane.add(columnLabel);
    
    // Array declarations of the borders that will have 4 visible lines
    oneWordFullBorders = new JLabel[16];
    twoWordFullBorders = new JLabel[24];
    fourWordFullBorders = new JLabel[17];
    eightWordFullBorders = new JLabel[6];
    sixteenWordBorder = new JLabel[1];
    corners = new JLabel[4];
    
     // Array declarations of the borders that will have 3 visible lines
    twoWordWrapAroundBorders = new JLabel[8][2];
    fourWordWrapAroundBorders = new JLabel[6][2];
    eightWordWrapAroundBorders = new JLabel[2][2];
    
    // Adds the grey code that is along the rows and columns of the Kmap
    for (int i = 0; i < 4; i++) {
      greyCodeHorizontal[i].setBounds(250 + (i * 50), 160, 20, 20);
      greyCodeVertical[i].setBounds(200, 210 + (i * 60), 20, 20);
      pane.add(greyCodeHorizontal[i]);
      pane.add(greyCodeVertical[i]);
    }
    
    int constant = 15;
    int downMapCount = 1;
    int sideMapCount = 0;
    int downCheckboxCount = 0;
    int sideCheckboxCount = 0;
    
    // loops through 1-16 and initializes the beginning arrays.
    for (int i = 0; i < 16; i++) {
    
      addToKmap[i] = false; // initially, nothing is checked
      
      // sets up the subcube buttons
      subcubes[i] = new JButton("");
      subcubes[i].setBounds(530, 120 + constant, 100, 15);
      subcubes[i].addActionListener(this);
      pane.add(subcubes[i]); 
      subcubes[i].setVisible(true);
      constant = constant + 30;
      
      //adds the 1 bit values in the kmap
      bits[i] = new JButton(MintermArray[0]);
      bits[i].setBounds(85, (130 + (i * 30)), 50, 15);
      bits[i].addActionListener(this);
      pane.add(bits[i]);
      
      // adds the fourdigit values in binary
      binaryValues[i] = new JLabel(fourDigits(i));
      binaryValues[i].setBounds(50 , (130 + (i * 30)), 35, 15); 
      pane.add(binaryValues[i]);
      
      // adds 0's in all locations of the map
      map[i] = new JLabel("0");
      map[i].setVisible(true);
         
      //count where the kmap should place each value
      if (i % 4 == 0) {
        downMapCount++;
      }
      int down = 60;
      
      // set the proper location for each value in the kmap
      locations[i][0] = 250 + (sideMapCount * 50);
      locations[i][1] = 90 + (down * downMapCount);
      locations[i][2] = 20;
      locations[i][3] = 20;
      map[i].setBounds(locations[i][0], locations[i][1], locations[i][2], locations[i][3]);
      sideMapCount++;
      if (sideMapCount == 4) {
        sideMapCount = 0;
      } 
      pane.add(map[i]);
        
      // create a checkbox for each value in the kmap
      boxBits[i] = new JCheckBox(fourDigits(i));  
      boxBits[i].addActionListener(this);
      
      if (i % 4 == 0){
        boxBits[i].setBounds(150, locations[i][1] - sideCheckboxCount + 250, 75, 15);
      } else if (i % 4 == 1) {
        boxBits[i].setBounds(250, locations[i][1] - sideCheckboxCount + 250, 75, 15);
      } else if (i % 4 == 2) {
        boxBits[i].setBounds(450, locations[i][1] - sideCheckboxCount + 250, 75, 15);
      } else if (i % 4 == 3) {
        boxBits[i].setBounds(350, locations[i][1] - sideCheckboxCount + 250, 75, 15);
      }
      downCheckboxCount++;
      
      if (downCheckboxCount == 4) {
        downCheckboxCount = 0; 
        sideCheckboxCount = sideCheckboxCount + 10;
      }
      pane.add(boxBits[i]);
      
    }
    sideCheckboxCount = sideCheckboxCount - 10;
    for (int i = 8; i < 16; i++) {
      if (i >= 8 && i < 12) {
        int tmp = 0;
        int tmp4 = 0;
        tmp = boxBits[i].getY();
        tmp4 = boxBits[i + 4].getY();
        boxBits[i].setBounds(boxBits[i].getX(), tmp4, 75, 15);
        boxBits[i + 4].setBounds(boxBits[i].getX(), tmp, 75, 15);
      }
    }
    // swaps to put the map elements and check boxes in the correct order   
    for (int i = 0; i < map.length; i++) {
      JLabel tmpLabel = new JLabel();
      if (i % 4 == 2) {
        tmpLabel = map[i];
        map[i] = map[i + 1];
        map[i + 1] = tmpLabel;
      } 
    }
    for (int i = 8; i < 12; i++) {
      JLabel tmpLabel = new JLabel();
      tmpLabel = map[i];
      map[i] = map[i + 4];
      map[i + 4] = tmpLabel;
    }
    
    // Labels the Kmap
    JLabel kmapHeader = new JLabel("Kmap");
    kmapHeader.setBounds(310, 135, 60, 15);
    kmapHeader.setVisible(true);
    pane.add(kmapHeader);
    
    // Labels the truth table
    JLabel tableLabel = new JLabel("ABCD");
    tableLabel.setBounds(50, 115, 60, 15);
    tableLabel.setVisible(true);
    pane.add(tableLabel);
    
    // Creates a button the user will use to get more information
    help = new JButton("Help");
    help.setBounds(495, 50, 80, 15);
    help.addActionListener(this);
    pane.add(help);
    
    // Creates a button the user will use to get more information
    about = new JButton("About");
    about.setBounds(575, 50, 80, 15);
    about.addActionListener(this);
    pane.add(about);
    
    // Creates a button the user will use to switch variable length
    change = new JButton("Switch to 3 Variable");
    size = change.getPreferredSize();
    change.setBounds(240, 100, size.width, size.height);
    change.addActionListener(this);
    pane.add(change);
    
    // Creates a button the user will press when they have a group of binary values to group
    compute = new JButton("Generate Box Around Selected Minterms");
    compute.setBounds(190, 430, 275, 15);
    compute.addActionListener(this);
    pane.add(compute); 
    
    // the user clicks this button to set all loops to invisible
    clearLoops = new JButton("Clear Loops");
    clearLoops.setBounds(5, 50, 130, 15);
    clearLoops.addActionListener(this);
    pane.add(clearLoops); 
   
    // Creates a button that the user can press to solve the kmap
    showMinimal = new JButton("Show Minimal");
    showMinimal.setBounds(135, 50, 130, 15);
    showMinimal.addActionListener(this);
    pane.add(showMinimal); 
    
    // Creates a button the user will press when to fill kmap values with 1's
    fillKmap = new JButton("Fill Kmap");
    fillKmap.setBounds(395, 50, 100, 15);
    fillKmap.addActionListener(this);
    pane.add(fillKmap); 
    
    // Creates a button the user will press when to fill kmap values with 0's
    reset = new JButton("Reset Table");
    reset.setBounds(265, 50, 130, 15);
    reset.addActionListener(this);
    pane.add(reset);
  
    // Creates a Label to label the subcubes
    JLabel subcubeLabel = new JLabel("subcubes:");
    subcubeLabel.setBounds(530, 115, 130, 15);
    subcubeLabel.setVisible(true);
    pane.add(subcubeLabel);
   
    // Creates a space the user can see the canonical expression
    JLabel canonicalLabel = new JLabel("Canonical:");
    canonicalLabel.setBounds(20, 610, 100, 20);
    canonicalLabel.setVisible(true);
    pane.add(canonicalLabel);
    
    showCanonical = new JTextArea("", 2, 1);
    showCanonical.setBounds(20, 630, 600, 30);
    showCanonical.setLineWrap(true);
    showCanonical.setWrapStyleWord(true);
    showCanonical.setEditable(false);
    pane.add(showCanonical);
    showCanonical.setVisible(true);
    
    canonical = "";
    
    // Creates a space where the user can see their current sum of products
    JLabel KmapLabel = new JLabel("Kmap:");
    KmapLabel.setBounds(20, 660, 40, 20);
    pane.add(KmapLabel);
    KmapLabel.setVisible(true);
    
    showKmap = new JTextArea("", 2, 1);
    showKmap.setBounds(20, 680, 600, 30);
    showKmap.setLineWrap(true);
    showKmap.setWrapStyleWord(true);
    showKmap.setEditable(false);
    pane.add(showKmap);
    showKmap.setVisible(true);
    
    kmap = "";
    
    // Creates a space where the user can see their current sum of products
    status = new JLabel("(Correct!)");
    status.setBounds(80, 660, 100, 20);
    status.setForeground(new Color(0, 115, 0));
    pane.add(status);
    status.setVisible(true);
    
    // Creates a space the user can see the minimal SoP if they want
    minimalSoPLabel = new JLabel("Minimum Sum Of Products: ");
    minimalSoPLabel.setBounds(20, 710, 300, 20);
    pane.add(minimalSoPLabel);
    minimalSoPLabel.setVisible(false);
    
    minScrollPane = new JScrollPane();
    minScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    minScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    showMinimalSoP = new JTextArea("0", 30, 30);
    showMinimalSoP.setBounds(20, 730, 600, 30);
    showMinimalSoP.setLineWrap(true);
    showMinimalSoP.setWrapStyleWord(true);
    showMinimalSoP.setEditable(false);
    minScrollPane.setBounds(20, 730, 600, 100);
    minScrollPane.getViewport().setBackground(Color.WHITE);
    minScrollPane.getViewport().add(showMinimalSoP);
    pane.add(minScrollPane);
    repaint();
    minScrollPane.setVisible(false);
  
    minimalSoP = "";
    
    //build the list of possible groupings
    buildSixteen();
    buildEight();
    buildFour();
    buildTwo();
    buildOne();  
    grid();
    
    }
    
  // takes an event based on a specific action
  public void actionPerformed(ActionEvent e) {
    
    // the frame switches to a three variable kmap
    if (e.getSource() == change) {  
      ThreeVariable t = new ThreeVariable();
      t.setVisible(true);
      this.setVisible(false);
    }
    
    // the help frame opens
    if (e.getSource() == help) {
      Help h = new Help();
      h.setVisible(true);
      this.setVisible(false);
    }
    
    // the about frame opens
    if (e.getSource() == about) {
      About a = new About();
      a.setVisible(true);
      this.setVisible(false);
    }
    
    // all the users loops created will be cleared
    if (e.getSource() == clearLoops) {  
      fullPlacement(oneWordFullBorders, 0, "clear");
      fullPlacement(twoWordFullBorders, 0, "clear");
      fullPlacement(fourWordFullBorders, 0, "clear");
      fullPlacement(eightWordFullBorders, 0, "clear");
      fullPlacement(sixteenWordBorder, 0, "clear");
      fullPlacement(corners, 0, "clear");
      wrapAroundPlacement(twoWordWrapAroundBorders, 0, "clear");
      wrapAroundPlacement(fourWordWrapAroundBorders, 0, "clear");
      wrapAroundPlacement(eightWordWrapAroundBorders, 0, "clear");
      setStatus();
    }
    
    // runs the methods that add boxes into the kmap if specified by the user
    if (e.getSource() == compute) {
      int count = 0; // used to count how many values will be grouped in the kmap
      for (int i = 0; i < 16; i++) {
        map[i].setOpaque(false);
        map[i].repaint();
        if (addToKmap[i] == true) {
          addToKmap[i] = false;
          boxBits[i].setSelected(false);
          if (map[i].getText() != MintermArray[0]) {
            count++;
            addBorder[i] = true;
          } else {
            count = 20;
          }
        }
      }
      // call method that checks if the boxes checked by the user are valid to be boxed
      if (count == 1) {
        checkOne("");
        setStatus();
      } else if (count == 2) {       
        checkTwo("");
        setStatus();
      } else if (count == 4) {
        checkFour("");
        setStatus();
      } else if (count == 8) {
        checkEight("");
        setStatus();
      } else if (count == 16) {
        checkSixteen("1");
        setStatus();
      }
      
      // reset the variables used for the next use
      count = 0;
      for (int i = 0; i < 16; i++) {
        addBorder[i] = false;
      }
    } 
    
    // fills kmap with 1's 
    if (e.getSource() == fillKmap) {
      for (int i = 0; i < 16; i++) {
        boxBits[i].setSelected(false);
        addToKmap[i] = false;
        bits[i].setText(MintermArray[1]);
        map[i].setText(MintermArray[1]);
        countArray[i] = 1;
      }
      canonicalExpression();
      findMinimalSoP();
      setStatus();
    }
    
    // resets kmap to 0's
    if (e.getSource() == reset) {
      for (int i = 0; i < 16; i++) {
        map[i].setOpaque(false);
        boxBits[i].setSelected(false);
        addToKmap[i] = false;
        bits[i].setText(MintermArray[0]);
        map[i].setText(MintermArray[0]);
        map[i].repaint();
        countArray[i] = 0;
        
        fullPlacement(oneWordFullBorders, 0, "clear");
        fullPlacement(twoWordFullBorders, 0, "clear");
        fullPlacement(fourWordFullBorders, 0, "clear");
        fullPlacement(eightWordFullBorders, 0, "clear");
        fullPlacement(sixteenWordBorder, 0, "clear");
        wrapAroundPlacement(twoWordWrapAroundBorders, 0, "clear");
        wrapAroundPlacement(fourWordWrapAroundBorders, 0, "clear");
        wrapAroundPlacement(eightWordWrapAroundBorders, 0, "clear");    
      }
      canonical = "";
      kmap = "";
      showCanonical.setText(canonical);
      showKmap.setText(kmap);
      findMinimalSoP();
      setStatus();
    }
    
    // show a minimal sum of products to the user using Quine McCluskey algorithm
    if (e.getSource() == showMinimal) {
      if (showMinimal.getText().equals("Show Minimal")) {
        showMinimal.setText("Hide Minimal");
      } else {
        showMinimal.setText("Show Minimal");
      }
      if (minimalSoPLabel.isVisible() == true && showMinimalSoP.isVisible() == true) {
        minimalSoPLabel.setVisible(false);
        showMinimalSoP.setVisible(false);
        minScrollPane.setVisible(false);
      } else {
        minimalSoPLabel.setVisible(true);
        showMinimalSoP.setVisible(true);
        minScrollPane.setVisible(true);
      }
      repaint();
    } 
    
    // Gives the correct output on the Kmap if the user is creating the Kmap 
    for (int i = 0; i < 16; i++) {
      // if the user clicked the subcube, then the user can remove the loop from the kmap
      if (e.getSource() == subcubes[i]) {
        String str = subcubes[i].getText();
        int count = 0;
        for (int j = 0; j < str.length(); j++) {
          if (Character.isLetter(str.charAt(j))) {
            count++;
          }
        }
        // will check the loop for a match in the correct length literal method
        if (count == 4) {
          checkOne(str);
        } else if (count == 3) {
          checkTwo(str);
        } else if (count == 2) {
          checkFour(str);
        } else if (count == 1) {
          checkEight(str);
        } else if (str == "1") {
          checkSixteen(str);
        }
        setStatus();
      }  
      // changes the bits in the kmap based on the action by the user 
      if (e.getSource() == bits[i]) {
        // Changes the respective but from a 0 to a 1
        if (countArray[i] % 3 == 0) {    
          bits[i].setText(MintermArray[1]);      
          map[i].setText(MintermArray[1]);
          countArray[i]++;
        // Changes the respective bits from a 1 to a X  
        } else if (countArray[i] % 3 == 1) {
          bits[i].setText(MintermArray[2]);      
          map[i].setText(MintermArray[2]);
          countArray[i]++;
        // Changes the respective bits from a X to a 0 
        } else if (countArray[i] % 3 == 2) {
          bits[i].setText(MintermArray[0]);      
          map[i].setText(MintermArray[0]);
          countArray[i]++;
        }
        canonicalExpression();
        findMinimalSoP();
        setStatus();
      }
    }
    // if the user checked a box to solve the Kmap
    for (int i = 0; i < 16; i++) {
      if (e.getSource() == boxBits[i]) {
        if (addToKmap[i] == false) {
          addToKmap[i] = true;
          map[i].setOpaque(true);
          map[i].setBackground(Color.YELLOW);
          map[i].repaint();
        } else {
          addToKmap[i] = false;
          map[i].setOpaque(false);
          map[i].repaint();
        }
      }
    }
  }
  
  // takes a binary value and makes it four digits wide
  public static String fourDigits(int num) { 
    String digits =  Integer.toBinaryString(num);
    if (num < 2) {
      return ("000" + digits);
    } else if (num < 4) {
      return ("00" + digits);
    } else if (num < 8) {
      return ("0" + digits);
    }
    return (digits);
  }
  
  // add a string to a kmap expression
  public static void addKmapExpression (String s) {
    if (kmap.isEmpty() == true) {
      kmap = s;
    } else {
      kmap = (kmap + "+" + s);
    }
    showKmap.setText(kmap);
  }
  
  // remove a string from a kmap expression 
  public static void removeKmapExpression (String s) {
    for (int m = 0; m < kmap.length(); m++) {
      if ((int)kmap.charAt(m) == 43) {
        s = "\\+" + s;
        break;
      }
    }
    kmap = kmap.replaceFirst(s, "");
    showKmap.setText(kmap);
  }
  
  public static void setStatus() {
    
    kmap = showKmap.getText();
    ArrayList<String> literalsKmap = new ArrayList<String>(); 
    String[] kmapArr = kmap.split("\\+", 16);
    String tmp = "";
    for (int i = 0; i < kmapArr.length; i++) {
      if (kmapArr[i] != null) { 
        literalsKmap.add(kmapArr[i]);
      }
    }
    Collections.sort(literalsKmap);
    for (int i = 0; i < literalsKmap.size(); i++) {
      tmp = tmp + (literalsKmap.get(i)) + "+";
    }
    if (tmp.length() > 0) {
      tmp = tmp.substring(0, tmp.length() - 1);
    }
    
    String lines[] = showMinimalSoP.getText().split("\r?\n");
    for (int i = 0; i < lines.length; i++) {
      if (lines[i].equals(tmp)) {
        if (status.getText() == "(Incorrect)") {
          status.setText("(Correct!)");
          status.setForeground(new Color(0, 115, 0));
          return;
        }
      }
    }
    
    if (lines[0].equals("0")) {
      if (status.getText() == "(Incorrect)") {
        status.setText("(Correct!)");
        status.setForeground(new Color(0, 115, 0));
        return;
      }
    }
    if (status.getText() == "(Correct!)") {
      status.setText("(Incorrect)");
      status.setForeground(new Color(115, 0, 0));
    }
    kmap = "";
  }
  
  public void findMinimalSoP() {
    
    // arrays used to hold the minterm values for each step of the algorithm 
    String zeros[] = new String[16];
    String ones[] = new String[16];
    String twos[] = new String[16];
    String threes[] = new String[16];
    String fours[] = new String[16];
    String totalMinterms[] = new String[16];
    String oneGroupedMinterms[] = new String[16];
    String twoGroupedMintermsA[] = new String[25];
    String twoGroupedMintermsB[] = new String[25];
    String twoGroupedMintermsC[] = new String[25];
    String twoGroupedMintermsD[] = new String[25];
    String fourGroupedMintermsAB[] = new String[25];
    String fourGroupedMintermsBC[] = new String[25];
    String fourGroupedMintermsCD[] = new String[25];
    String eightGroupedMintermsABC[] = new String[8];
    String eightGroupedMintermsBCD[] = new String[8];
    String sixteenGroupedMintermsABCD[] = new String[4];
    
    // reset the minimal sum of products variables
    minimalSoP = "";
    showMinimalSoP.setText(minimalSoP);
     
    // fills totalMinterms with original minterms defined in the kmap 
    int count = 0; 
    String[] values = new String[16];
    for (int i = 0; i < 16; i++) {
      if (map[i].getText() == MintermArray[1]) {
        values[i] = binaryValues[i].getText();
        totalMinterms[count] = Integer.toString(i);
        count++;
      } else if (map[i].getText() == MintermArray[2]) {
        values[i] = binaryValues[i].getText();
        totalMinterms[count] = Integer.toString(i) + "dc";
        count++;
      }       
    }
    
    // if all numbers in kmap are 0 then the minimum is 0
    if (count == 0) {
      minimalSoP = "0";
      showMinimalSoP.setText(minimalSoP);
    // if all numbers in kmap are 1 then the minimum is 1
    } else if (count == 16) {   
      minimalSoP = "1";
      showMinimalSoP.setText(minimalSoP);
    } else {
      String[] start = new String[16]; // used to find the starting literals
      count = 0;   

    for (int i = 0; i < 16; i++) { // find starting literals
      oneGroupedMinterms[i] = binaryValues[i].getText(); // used to collect the 4 bit binary values
      if (map[i].getText() != MintermArray[0]) {
        if (i == 0) {
          zeros[0] = values[i];
          start[count] = values[i];
          count++;
        } else if (i == 1 || i == 2 || i == 4 || i == 8) {
          ones[i] = values[i];
          start[count] = values[i];
          count++;
        } else if (i == 3 || i == 5 || i == 6 || i == 9 || i == 10 || i == 12) {
          twos[i] = values[i];
          start[count] = values[i];
          count++;
        } else if (i == 7 || i == 11 || i == 13 || i == 14) {
          threes[i] = values[i];
          start[count] = values[i];
          count++;
        } else if (i == 15) {
          fours[15] = values[i];
          start[count] = values[i];
          count++;
        }
      } 
    } 
    
    // performs the first round of the Quine-McCluskey method and updates start, combines possible minterms to make products of length 2
    String[] oneLiteralA = round1(zeros, ones, twoGroupedMintermsA, oneGroupedMinterms);
    String[] oneLiteralB = round1(ones, twos, twoGroupedMintermsB, oneGroupedMinterms);     
    String[] oneLiteralC = round1(twos, threes, twoGroupedMintermsC, oneGroupedMinterms);   
    String[] oneLiteralD = round1(threes, fours, twoGroupedMintermsD, oneGroupedMinterms);
    
    // have start only be including the minterms that are used in the problem and correctly rearranges it to match the map positioning      
    int i = 0;
    while(start[i] != null) {
      if (zeros[0] != null && start[i].charAt(0) == zeros[0].charAt(0) && start[i].charAt(1) == zeros[0].charAt(1) && start[i].charAt(2) == zeros[0].charAt(2) && start[i].charAt(3) == zeros[0].charAt(3)) {
        start[i] = zeros[0];
      } else if (fours[15] != null && start[i].charAt(0) == fours[15].charAt(0) && start[i].charAt(1) == fours[15].charAt(1) && start[i].charAt(2) == fours[15].charAt(2) && start[i].charAt(3) == fours[15].charAt(3)) { 
        start[i] = fours[15];
      } else {
        for (int j = 0; j < 16; j++) {
          if (ones[j] != null) {
            if (start[i].charAt(0) == ones[j].charAt(0) && start[i].charAt(1) == ones[j].charAt(1) && start[i].charAt(2) == ones[j].charAt(2) && start[i].charAt(3) == ones[j].charAt(3)) {
              start[i] = ones[j];
            }
          } else if (twos[j] != null) {
            if (start[i].charAt(0) == twos[j].charAt(0) && start[i].charAt(1) == twos[j].charAt(1) && start[i].charAt(2) == twos[j].charAt(2) && start[i].charAt(3) == twos[j].charAt(3)) {
              start[i] = twos[j];
            }
          } else if (threes[j] != null) {
            if (start[i].charAt(0) == threes[j].charAt(0) && start[i].charAt(1) == threes[j].charAt(1) && start[i].charAt(2) == threes[j].charAt(2) && start[i].charAt(3) == threes[j].charAt(3)) {
              start[i] = threes[j];
            }
          }
        }
      }
      i++;
    }
    // combines the literals from round 1 into larger arrays
    String[] oneLiteralAB = arrayMaker(oneLiteralA, oneLiteralB) ;
    String[] oneLiteralBC = arrayMaker(oneLiteralB, oneLiteralC) ;
    String[] oneLiteralCD = arrayMaker(oneLiteralC, oneLiteralD) ;
    String[] oneLiteralABC = arrayMaker(oneLiteralAB, oneLiteralBC);
    String[] oneLiteralBCD = arrayMaker(oneLiteralBC, oneLiteralCD);
    String[] oneLiteralABCD = arrayMaker(oneLiteralABC, oneLiteralBCD);

    // combines the minterm arrays from round 1 into larger arrays
    String[] twoGroupedMintermsAB = arrayMaker(twoGroupedMintermsA, twoGroupedMintermsB);
    String[] twoGroupedMintermsBC = arrayMaker(twoGroupedMintermsB, twoGroupedMintermsC);
    String[] twoGroupedMintermsCD = arrayMaker(twoGroupedMintermsC, twoGroupedMintermsD);
    String[] twoTotal = arrayMaker(twoGroupedMintermsAB, twoGroupedMintermsC);
    twoTotal = arrayMaker(twoTotal, twoGroupedMintermsD);
      
    // performs round 2 of the Quine-McCluskey method, combines possible minterms to make products of length 4
    String[] twoLiteralAB = round2(oneLiteralA, oneLiteralB, fourGroupedMintermsAB, oneLiteralAB, twoGroupedMintermsAB);
    String[] twoLiteralBC = round2(oneLiteralB, oneLiteralC, fourGroupedMintermsBC, oneLiteralBC, twoGroupedMintermsBC);
    String[] twoLiteralCD = round2(oneLiteralC, oneLiteralD, fourGroupedMintermsCD, oneLiteralCD, twoGroupedMintermsCD); 
    
    // redo twoLiteralAB, twoLiteralBC, and twoLiteralCD to add the possible checks (denoted as c's)
    oneLiteralAB = arrayMaker(oneLiteralA, oneLiteralB);
    oneLiteralBC = arrayMaker(oneLiteralB, oneLiteralC);
    oneLiteralCD = arrayMaker(oneLiteralC, oneLiteralD);
     
    // combines the literals from round 2 into larger arrays      
    String[] twoLiteralABC = arrayMaker(twoLiteralAB, twoLiteralBC);
    String[] twoLiteralBCD = arrayMaker(twoLiteralBC, twoLiteralCD);
    String[] twoLiteralABCD = arrayMaker(twoLiteralABC, twoLiteralCD); 
      
    // combines the minterms from round 2 into larger arrays
    String[] fourGroupedMintermsABC = arrayMaker(fourGroupedMintermsAB, fourGroupedMintermsBC);
    String[] fourGroupedMintermsBCD = arrayMaker(fourGroupedMintermsBC, fourGroupedMintermsCD);
    String[] fourTotal = arrayMaker(fourGroupedMintermsABC, fourGroupedMintermsCD);
    
    // performs round 2 again of the Quine McCluskey method, combines possible minterms to make products of length 8
    String[] threeLiteralABC = round2(twoLiteralAB, twoLiteralBC, eightGroupedMintermsABC, twoLiteralABC, fourGroupedMintermsABC);
    String[] threeLiteralBCD = round2(twoLiteralBC, twoLiteralCD, eightGroupedMintermsBCD, twoLiteralBCD, fourGroupedMintermsBCD);
   
    oneLiteralABC = arrayMaker(oneLiteralAB, oneLiteralBC);
    oneLiteralBCD = arrayMaker(oneLiteralBC, oneLiteralCD);

    // combines the minterms from round 2 into larger arrays    
    String[] eightGroupedMintermsABCD = arrayMaker(eightGroupedMintermsABC, eightGroupedMintermsBCD);
    
    // preforms round 2 again of the Quine McCluskey method, combines possible minterms to make products of length 16     
    String[] threeLiteralABCD = round2(threeLiteralABC, threeLiteralBCD, sixteenGroupedMintermsABCD, oneLiteralABCD, eightGroupedMintermsABCD);
    
    oneLiteralABCD = arrayMaker(oneLiteralABC, oneLiteralBCD);
    threeLiteralABCD = arrayMaker(threeLiteralABC, threeLiteralBCD);
    
    // uses the total arrays to make one final array     
    String[] total = arrayMaker (totalMinterms, twoTotal);
    total = arrayMaker (total, fourTotal);
    total = arrayMaker (total, eightGroupedMintermsABCD);
    
    // uses the literal arrays to make one final array
    String[] literals = arrayMaker(oneLiteralABCD, twoLiteralABCD);
    literals = arrayMaker (literals, threeLiteralABCD);
    literals = arrayMaker (start, literals);
    
    // uses the final arrays to find the point implicants
    findPI(total, literals, totalMinterms);
    
    } 
  }
  
  // add a string to a minimum sum of products expression  
  public static void addMinimalSoPExpression (String s) {

    if (minimalSoP.contains(s)) {
      return;
    }
    if (minimalSoP == null || minimalSoP.isEmpty() == true) {
      minimalSoP = s;
    } else {
      minimalSoP = (minimalSoP + "+" + s);
    }
  }
  
  public String[] round1 (String[] x, String[] y, String[] z, String[] m) { // x is comparing with y
  
    String[] newArray = new String[25]; // the array that will be returned with the literals
    int endCount = 0;
    String endValue = ""; 
    int count = 0;
    
    // compare x and y and if 3 characters match then they can be combined
    for (int r = 0; r < 16; r++) {
      for (int t = 0; t < 16; t++) {
        if (x[r] != null && y[t] != null) {
          for (int i = 0; i < 4; i++) { 
            if (x[r].charAt(i) == y[t].charAt(i)) {
              count++;
            }
          }
        }
        // create the new combined literal
        if (count == 3) {
            for (int j = 0; j < 4; j++) {
              if (x[r].charAt(j) == y[t].charAt(j)) {
                endValue = endValue + x[r].charAt(j);
              } else {
                endValue = endValue + "_";
              } 
            }         
            // puts placeholders in between the minterms
            String s = "";
            for (int i = 0; i < 16; i++) {  
              if ((x[r].charAt(0) == m[i].charAt(0)) && (x[r].charAt(1) == m[i].charAt(1)) &&
              (x[r].charAt(2) == m[i].charAt(2)) && (x[r].charAt(3) == m[i].charAt(3))) {
                s = s + Integer.toString(i) + "x";
              }
              if ((y[t].charAt(0) == m[i].charAt(0)) && (y[t].charAt(1) == m[i].charAt(1)) &&
              (y[t].charAt(2) == m[i].charAt(2)) && (y[t].charAt(3) == m[i].charAt(3))) {
                s = s + Integer.toString(i) + "x";
              }
            }
            // removes the final x  
            s = s.substring(0, s.length() - 1); 
            
            // adds c to the end of the string, meaning that it has already been checked
            x[r] = x[r] + "c";
            y[t] = y[t] + "c";
            
            // add the new minterm to the array that holds combined minterms
            for (int i = 0; i < z.length; i++) {
              if (z[i] == null) {
                z[i] = s;
                s = "";
                break;
              }
            }
            // adds the new literal with the _ to the proper array        
            newArray[endCount] = endValue;
            endValue = "";
            endCount++;
          }
        count = 0;
      }
    }
    return (newArray);
  }
  
  public String[] round2 (String[] x, String[] y, String[] z, String[] m, String[] w) { // x is comparing with y
    
    // the array that will be returned with the literals
    String[] newArray = new String[25];
    int endCount = 0;
    String endValue = "";
    int count = 0;
    int lengthX = 0;
    int lengthY = 0;
    int lengthM = 0;
    for (int i = 0; i < x.length; i++) {
      if (x[i] == null) {
        break;
      } 
      lengthX++;
    }
    for (int i = 0; i < y.length; i++) {
      if (y[i] == null) {
        break;
      } 
      lengthY++;
    }
    for (int i = 0; i < m.length; i++) {
      if (m[i] == null) {
        break;
      } 
      lengthM++;
    }
    
    // compare x and y and if 3 characters match then they can be combined
    for (int r = 0; r < lengthX; r++) {
      for (int t = 0; t < lengthY; t++) {
        if (x[r] != null && y[t] != null) {
          for (int i = 0; i < 4; i++) {
            if (x[r].length() > 0 && y[t].length() > 0 && x[r].charAt(i) == y[t].charAt(i)) {
              count++;
            }
          }
        }
        if (count == 3) { 
            String s = ""; 
            // puts placeholders in between the minterms
            for (int i = 0; i < lengthM; i++) {
              if ((x[r].charAt(0) == m[i].charAt(0)) && (x[r].charAt(1) == m[i].charAt(1)) &&
              (x[r].charAt(2) == m[i].charAt(2)) && (x[r].charAt(3) == m[i].charAt(3))) {
                s = s + w[i] + "x";
              }
              if ((y[t].charAt(0) == m[i].charAt(0)) && (y[t].charAt(1) == m[i].charAt(1)) &&
              (y[t].charAt(2) == m[i].charAt(2)) && (y[t].charAt(3) == m[i].charAt(3))) {
                s = s + w[i] + "x";
              }
            }
            if (s.length() > 0) {
              s = s.substring(0, s.length() - 1);
            }
            
            // create the new combined literal
            for (int j = 0; j < 4; j++) {
              if (x[r].charAt(j) == y[t].charAt(j)) {
                endValue = endValue + x[r].charAt(j);
              } else {
                endValue = endValue + "_";
              }
            }
            // adds c to the end of the string, meaning that it has already been checked
            x[r] = x[r] + "c";
            y[t] = y[t] + "c";
            
            // initialize variables to check for duplicates
            count = 0;
            int c = 0;
            boolean check = false;
            
            // add the new minterm to the array that holds combined minterms
            for (int i = 0; i < z.length; i++) {
              if (z[i] != null) {
                c++;
              }
            }
            // checks for duplicates
            for (int i = 0; i < newArray.length; i++) {
              if (newArray[i] != null) {
                if (newArray[i].equals(endValue)) {
                  check = true;
                  break;
                }
              }
            }
            // add the new minterm to the array that holds combined minterms
            if (check == false) {
              z[endCount] = s;
              newArray[endCount] = endValue;
              endCount++;
            }
          }
          endValue = "";
          count = 0;
      }
    }
    return (newArray);
  }
  
  // combines two arrays into one array
  public String[] arrayMaker (String[] q, String[] p) {
    int qCount = 0;
    int pCount = 0;
    for (int i = 0; i < q.length; i++) {
      if (q[i] != null) {
        qCount++;
      }
    }
    for (int i = 0; i < p.length; i++) {
      if (p[i] != null) {
        pCount++;
      }
    }
    String[] n = new String[pCount + qCount];
    for (int i = 0; i < qCount; i++) {
      n[i] = q[i];
    }
    int subtract = 0;
    for (int i = 0; i < pCount; i++) {
      boolean add = true;
      for (int j = 0; j < qCount; j++) {
        if (p[i] == q[j]) {
          add = false;
          subtract++;
        }
      }
      if (add == true) {
        n[i + qCount - subtract] = p[i];
      }
    }
    return (n);
  }
  
  public void findPI (String[] t, String[] l, String[] m) {
    
    String realM[] = new String[m.length]; // used to remove the don't cares
    int count = 0;
    // removes the don't cares from the total count 
    for (int i = 0; i < m.length; i++) {
      if (m[i] != null) {
        if (m[i].endsWith("dc") == false) {
          realM[count] = m[i];
          count++;
        }
      }
    }
    // if there are no non-don't cares, then there are automatically no prime implicants
    if (count == 0) {
      return;
    }
    count = 0; // reset count to 0
    
    // initial variables
    boolean dc = false;
    String str = "";
    String tmp = "";
    String optionsL[] = new String[64]; 
    String optionsT[][] = new String[32][16];
    String placers[] = {"A", "B", "C", "D"};

    // add an x to the end of the totals and find your options for optionsL and optionsT
    for (int i = 0; i < l.length; i++) {
      if (l[i].length() == 4 && t[i].endsWith("dc") == false) {
        t[i] = t[i] + "x";
        int countT = 0;
        for (int k = 0; k < t[i].length(); k++) {
          if (Character.isDigit(t[i].charAt(k))) {
            tmp = tmp + t[i].charAt(k);
          } else if (t[i].charAt(k) == 'x') {
            optionsT[count][countT] = tmp;
            tmp = "";
            countT++;
          } 
        }
        count++;
        for (int j = 0; j < l[i].length(); j++) {
          if (l[i].charAt(j) == '0') {
            if (j != l[i].length()) {
              str = str + placers[j] +  "\'";
            } else {
              str = str + placers[j] +  "\'";
            }
          } else if (l[i].charAt(j) == '1') {
            if (j != l[i].length()) {
              str = str + placers[j];
            } else {
              str = str + placers[j];
            }
          }
        }
        optionsL[count - 1] = str;
        str = "";
      }
    }
    // find the amount of minterms without nulls
    int lengthM = 0;
    for (int i = 0; i < realM.length; i++) {
      if (realM[i] != null) {
        lengthM++;
      }
    }
    
    // variables for prime implicants
    boolean primeImplicants[][] = new boolean[count][lengthM];
    boolean processed[][] = new boolean[count][lengthM];
   
    for (int r = 0; r < primeImplicants.length; r++) {
      for (int c = 0; c < primeImplicants[0].length; c++) {
        primeImplicants[r][c] = false;
        processed[r][c] = false;
      }
    }
    
    // find the prime implicants that should be marked
    for (int r = 0; r < optionsT.length; r++) {
      for (int c = 0; c < optionsT[0].length; c++) {
        for (int k = 0; k < lengthM; k++) {
          if (optionsT[r][c] != null) {
            if (realM[k] != null) {         
              if (optionsT[r][c].equals(realM[k])) {
                primeImplicants[r][k] = true;
              }
            }
          }
        }
      }
    }  
    
    // used to save initial states of these arrays for finding multiple minimal SoPs
    boolean[][] keepP = new boolean[processed.length][processed[0].length];
    for (int i = 0; i < processed.length; i++) {
      for (int j = 0; j < processed[0].length; j++)
      keepP[i][j] = processed[i][j];
    }   
    int realOL = 0;
    for (int i = 0; i < optionsL.length; i++) {
      if (optionsL[i] != null) {
        realOL++;
      }
    }
    
    // finds the essential prime implicants
    int finalCount = 0;
    int holdR = 0;  
    for (int c = 0; c < primeImplicants[0].length; c++) {
      for (int r = 0; r < primeImplicants.length; r++) {
        if (primeImplicants[r][c] == true) {
          finalCount++;
          holdR = r;
        }
      }
      if (finalCount == 1) {
        finalCount = 0;
        addMinimalSoPExpression(optionsL[holdR]);
        for (int j = 0; j < processed[0].length; j++) {
          if (primeImplicants[holdR][j] == true) {
            processed[holdR][j] = true;
            for (int i = 0; i < processed.length; i++) {
              if (primeImplicants[i][j] == true) {
                processed[i][j] = true;
              }
            }
          }
        }
      }
      finalCount = 0;
    }
    int length = 0;
    for (int r = 0; r < primeImplicants.length; r++) {
      for (int c = 0; c < primeImplicants[0].length; c++) {
        if (primeImplicants[r][c] == true) {
          length++;
          if (length == 8) {
            addMinimalSoPExpression(optionsL[r]);
            for (int j = 0; j < processed[0].length; j++) {
              if (primeImplicants[r][j] == true) {
                processed[r][j] = true;
                for (int i = 0; i < processed.length; i++) {
                  if (primeImplicants[i][j] == true) {
                    processed[i][j] = true;
                  }
                }
              }
            }
          }
        }
      }
      length = 0;
    }
    boolean done = true;    
    // if the essential prime implicants covered everything, we don't have to check for multiple minimal SoP's
  
    outer: for (int row = 0; row < primeImplicants.length; row++) {
      for (int c = 0; c < primeImplicants[0].length; c++) {
        if (primeImplicants[row][c] == true && processed[row][c] == false) {
          done = false;
          break outer;
        }
      }
    }
    
    for (int i = 0; i < t.length; i++) {
      if (t[i] != null) {
        if (t[i].endsWith("dc")) {
          dc = true;
          break;
        }
      }
    }
    if (done == true && dc == false) {
      order(minimalSoP);
      minimalSoP = "";
      return;
    } else {
      int[] spreadOL = new int[realOL];
      for (int i = 0; i < realOL; i++) {
        spreadOL[i] = i;
      }      
      minimalSoP = "";
      for (int i = 0; i <= realOL; i++) {
        if (showMinimalSoP.getText().isEmpty()) {
          setup(spreadOL, realOL, i, optionsL, primeImplicants, keepP); 
        }
      }
    }
    minimalSoP = showMinimalSoP.getText();
    String lines[] = minimalSoP.split("\r?\n");
    int holdLetters = 31;
    int changeCount = 0;
    for (int i = 0; i < lines.length; i++) {
      int oldLetters = 0;
      for (int j = 0; j < lines[i].length(); j++) {
        if (!(lines[i].equals("OR")) && (Character.isLetter(lines[i].charAt(j)))) {
          oldLetters++;
        }
      }
      if (oldLetters < holdLetters && oldLetters != 0) {
        holdLetters = oldLetters;
        changeCount++;
      }
      if (changeCount == 2) {
        for (int j = 0; j < i; j++) {
          lines[j] = "";
        }
        changeCount--;
      }
    }
    minimalSoP = "";
    for (int i = 0; i < lines.length; i++) {
      if (lines[i] != "") {
        minimalSoP = minimalSoP + lines[i] + "\n"; 
      }
    }
    showMinimalSoP.setText(minimalSoP);
    minimalSoP = "";
    return;
  } 
  
  // used to find all minimal SoP's
  public static void findAll(int arr[], int data[], int start, int size, int index, int rOL, String[] ol, boolean[][] pi, boolean[][] p) { 
    
    if (index == rOL) { 
      for (int j = 0; j < rOL; j++) { 
        // adds the literal to the potential minimal SoP
        addMinimalSoPExpression(ol[data[j]]);
        for (int c = 0; c < pi[0].length; c++) {
          if (pi[data[j]][c] == true) {           
            p[data[j]][c] = true;
            for (int row = 0; row < pi.length; row++) {
              if (pi[row][c] == true) {
                p[row][c] = true;
              }
            }
          }
        }
      }
   
      // checks to see if the potential minimal SoP covers the prime implicant table
      boolean check = true;
      outer: for (int row = 0; row < pi.length; row++) {
        for (int c = 0; c < pi[0].length; c++) {
          if (pi[row][c] == true && p[row][c] == false) {
            check = false;
            break outer;
          }
        }
      }
      if (check == true) {
        int currPlus = 0;
        for (int i = 0; i < minimalSoP.length(); i++) {
          if (minimalSoP.charAt(i) == '+') {
           currPlus++; 
          }
        }
        if (currPlus + 1 == rOL) {
          order(minimalSoP);       
          minimalSoP = "";
        }       
      } else {
        minimalSoP = "";
      }
      return; 
    } 

    // keeps re calling findAll until every possible combination has been exhausted
    for (int i = start; i <= size && size - i + 1 >= rOL - index; i++) { 
      data[index] = arr[i]; 
      boolean[][] keepP = new boolean[p.length][p[0].length];
      for (int row = 0; row < p.length; row++) {
        for (int j = 0; j < p[0].length; j++)
        keepP[row][j] = p[row][j];
      }
      findAll(arr, data, i + 1, size, index + 1, rOL, ol, pi, keepP); 
    } 
  } 

  // creates an array to hold each index of realOL to use
  public static void setup(int arr[], int size, int rOL, String[] ol, boolean[][] pi, boolean[][] p) { 
    int data[] = new int[rOL]; 
    findAll(arr, data, 0, size - 1, 0, rOL, ol, pi, p); 
    
  } 

  // puts the minimal sop in order
  public static void order (String str) {
    
    // keep track of how many literals the potential minimal SoP contains
    int currPlus = 0;
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == '+') {
       currPlus++; 
      }
    }
    // split each literal in its own array and store it in an arrayList to sort it
    ArrayList<String> literalsAL = new ArrayList<String>(); 
    String[] literalsA = str.split("\\+", 16);
    String tmp = "";
    for (int i = 0; i < literalsA.length; i++) {
      if (literalsA[i] != null) { 
        literalsAL.add(literalsA[i]);
      }
    }
    Collections.sort(literalsAL);
    for (int i = 0; i < literalsAL.size(); i++) {
      tmp = tmp + (literalsAL.get(i)) + "+";
    }
    if (tmp.length() > 0) {
      tmp = tmp.substring(0, tmp.length() - 1);
    }
    
    // check to see how many plus signs the other lines of the current minimal SoP's have
    // if the potential minimum SoP is too long, it will not be added
    int holdHighPlus = 0;
    String lines[] = showMinimalSoP.getText().split("\r?\n");
    for (int i = 0; i < lines.length; i++) {
      int oldPlus = 0;
      for (int j = 0; j < lines[i].length(); j++) {
        if (lines[i].charAt(j) == '+') {
          oldPlus++;
        }
      }
      if (oldPlus > holdHighPlus) {
        holdHighPlus = oldPlus;
      }
    }
    if ((holdHighPlus < currPlus) && showMinimalSoP.getText().isEmpty() == false) {
      return;
    }
    
    if (showMinimalSoP.getText().contains(tmp) == false) {
      if (showMinimalSoP.getText().isEmpty()) {
        showMinimalSoP.setText(tmp);
      } else {
        showMinimalSoP.setText(showMinimalSoP.getText() + "\nOR\n" + tmp);
      }
    }
  }
  
  // collection of methods that build the literals specific to the number of boxes and check if the user's checked boxes can produce a box 
  public void buildOne() { 
  
    for (int i = 0; i < 16; i++) {
      oneWordFullBorders[i] = new JLabel();
    }
    for (int i = 0; i < 16; i++) {
      // preforms series of swaps to assign the correct location of each number on the map
      if ((i % 4 < 2) && (i < 8)) {
        oneWordFullBorders[i].setBounds(locations[i][0], locations[i][1], locations[i][2] - 10, locations[i][3]);
      } else if ((i % 4 == 2) && (i < 8)) {
        oneWordFullBorders[i + 1].setBounds(locations[i][0], locations[i][1], locations[i][2] - 10, locations[i][3]);
      } else if ((i % 4 == 3) && (i < 8)) {       
        oneWordFullBorders[i - 1].setBounds(locations[i][0], locations[i][1], locations[i][2] - 10, locations[i][3]); 
      }else if ((i % 4 < 2) && (i < 12)) {
        oneWordFullBorders[i + 4].setBounds(locations[i][0], locations[i][1], locations[i][2] - 10, locations[i][3]);
      } else if ((i == 10)) {
        oneWordFullBorders[i + 4 + 1].setBounds(locations[i][0], locations[i][1], locations[i][2] - 10, locations[i][3]);
      } else if ((i == 11)) {
        oneWordFullBorders[i + 4 - 1].setBounds(locations[i][0], locations[i][1], locations[i][2] - 10, locations[i][3]);
      } else if ((i % 4 < 2) && (i >= 12)) {
        oneWordFullBorders[i - 4].setBounds(locations[i][0], locations[i][1], locations[i][2] - 10, locations[i][3]);
      } else if ((i == 14)) {
        oneWordFullBorders[i - 4 + 1].setBounds(locations[i][0], locations[i][1], locations[i][2] - 10, locations[i][3]);
      } else {
        oneWordFullBorders[i - 4 - 1].setBounds(locations[i][0], locations[i][1], locations[i][2] - 10, locations[i][3]);
      }
      oneWordFullBorders[i].setBorder(new MatteBorder(1, 1, 1, 1, Color.GREEN));
      oneWordFullBorders[i].setVisible(false);
      pane.add(oneWordFullBorders[i]);
    } 
  }
  public void checkOne(String s) {
  
    String literal = "";
    
    // creates the literals of the expressions
    for (int i = 0; i < 16; i++) {
      if (addBorder[i] == true) {
        if (i < 8) {
          literal = literal + "A\'";
        } else {
          literal = literal + "A"; 
        }
        if (i < 4 || (i >= 8 && i < 12)) {
          literal = literal + "B\'";
        } else {
          literal = literal + "B";
        }
        if (i % 4 < 2) {
          literal = literal + "C\'";
        } else {
          literal = literal + "C";
        }
        if (i % 2 == 0) {
          literal = literal + "D\'";
        } else {
          literal = literal + "D";
        }      
        fullPlacement(oneWordFullBorders, i, literal); 
        break;
      }
    } 
    if (s != "") { 
      int location = 15;
      if (s.contains("A\'")) {
        location = location - 8;
      } 
      if (s.contains("B\'")) {
        location = location - 4;
      } 
      if (s.contains("C\'")) {
        location = location - 2;
      } 
      if (s.contains("D\'")) {
        location = location - 1;
      } 
      fullPlacement(oneWordFullBorders, location, s);  
    } 
  }  
  public void buildTwo() {
  
    int j = 0;
    int sideCount = 0;
    int downCount = 0;
    
    // 2x1 full borders horizontal
    for (j = 0; j < 12; j++) {
      twoWordFullBorders[j] = new JLabel();
      twoWordFullBorders[j].setBounds(locations[sideCount][0] - 5, locations[downCount * 4][1], 70, 20);
      twoWordFullBorders[j].setBorder(new MatteBorder(1, 1, 1, 1, Color.RED)); 
      pane.add(twoWordFullBorders[j]);
      twoWordFullBorders[j].setVisible(false);
      sideCount++;
      if (sideCount == 3) {
        sideCount = 0;
        downCount++;
      }
    }
    // 2x1 full borders vertical
    sideCount = 0;
    downCount = 0;
    for (j = j; j < 24; j++) {
      twoWordFullBorders[j] = new JLabel();
      twoWordFullBorders[j].setBounds(locations[sideCount][0] - 5, locations[downCount * 4][1], 20, 80);
      twoWordFullBorders[j].setBorder(new MatteBorder(1, 1, 1, 1, Color.RED)); 
      pane.add(twoWordFullBorders[j]);
      twoWordFullBorders[j].setVisible(false);
      downCount++;
      if (downCount == 3) {
        downCount = 0;
        sideCount++;
      }
    }
    
    // Two Wrap Around Vertical 
    j = 0;
    for (j = j; j < 4; j++) {
      twoWordWrapAroundBorders[j][0] = new JLabel();
      twoWordWrapAroundBorders[j][1] = new JLabel();
    
      twoWordWrapAroundBorders[j][0].setBounds(locations[0][0] - 2 - 30, locations[0 + (j * 4)][1], 40, 20);
      twoWordWrapAroundBorders[j][1].setBounds(locations[3][0] - 2, locations[3 + (j * 4)][1], 40, 20);
    
      twoWordWrapAroundBorders[j][0].setBorder(new MatteBorder(1, 0, 1, 1, Color.BLUE));
      twoWordWrapAroundBorders[j][1].setBorder(new MatteBorder(1, 1, 1, 0, Color.BLUE));
    
      pane.add(twoWordWrapAroundBorders[j][0]); 
      pane.add(twoWordWrapAroundBorders[j][1]); 
    
      twoWordWrapAroundBorders[j][0].setVisible(false);
      twoWordWrapAroundBorders[j][1].setVisible(false);
    }
    
    // Two Wrap Around Horizontal
    for (j = j; j < 8; j++) {
      int x = j - 4;
      twoWordWrapAroundBorders[j][0] = new JLabel();
      twoWordWrapAroundBorders[j][1] = new JLabel();
    
      twoWordWrapAroundBorders[j][0].setBounds(locations[0 + x][0] - 2, 180, 10, 60);
      twoWordWrapAroundBorders[j][1].setBounds(locations[12 + x][0] - 2, locations[12][1] - 2, 10, 60);
    
      twoWordWrapAroundBorders[j][0].setBorder(new MatteBorder(0, 1, 1, 1, Color.BLUE));
      twoWordWrapAroundBorders[j][1].setBorder(new MatteBorder(1, 1, 0, 1, Color.BLUE));
    
      pane.add(twoWordWrapAroundBorders[j][0]); 
      pane.add(twoWordWrapAroundBorders[j][1]); 
    
      twoWordWrapAroundBorders[j][0].setVisible(false);
      twoWordWrapAroundBorders[j][1].setVisible(false);
    }
  } 
  public void checkTwo(String s) {

    // boolean variable that signify if a kmap expression is visible or not
    boolean notAnotBnotC = true;
    boolean notAnotBD = true;
    boolean notAnotBC = true;
    boolean notABnotC = true;
    boolean notABD = true;
    boolean notABC = true;
    boolean ABnotC = true;
    boolean ABD = true;
    boolean ABC = true;
    boolean AnotBnotC = true;
    boolean AnotBD = true;
    boolean AnotBC = true;
    boolean notAnotCnotD = true;
    boolean BnotCnotD = true;
    boolean AnotCnotD = true;
    boolean notAnotCD = true;
    boolean BnotCD = true;
    boolean AnotCD = true;
    boolean notACD = true;
    boolean BCD = true;
    boolean ACD = true;
    boolean notACnotD = true;
    boolean BCnotD = true;
    boolean ACnotD = true;
    boolean notAnotBnotD = true;
    boolean notABnotD = true;
    boolean ABnotD = true;
    boolean AnotBnotD = true;
    boolean notBnotCnotD = true;
    boolean notBnotCD = true;
    boolean notBCD = true;
    boolean notBCnotD = true;
    
    String str = "";
    // eliminate all incorrect answers by looping through all characters in the kmap
    for (int i = 0; i < 16; i++) {
      str = binaryValues[i].getText();
      if (str.charAt(0) == '0' && str.charAt(1) == '0' && str.charAt(2) == '0' && addBorder[i] == false) {
        notAnotBnotC = false;
      } 
      if (str.charAt(0) == '0' && str.charAt(1) == '0' && str.charAt(3) == '1' && addBorder[i] == false) {
        notAnotBD = false;
      } 
      if (str.charAt(0) == '0' && str.charAt(1) == '0' && str.charAt(2) == '1' && addBorder[i] == false) {
        notAnotBC = false;
      }
      if (str.charAt(0) == '0' && str.charAt(1) == '1' && str.charAt(2) == '0' && addBorder[i] == false) {
        notABnotC = false;
      }
      if (str.charAt(0) == '0' && str.charAt(1) == '1' && str.charAt(3) == '1' && addBorder[i] == false) {
        notABD = false;
      } 
      if (str.charAt(0) == '0' && str.charAt(1) == '1' && str.charAt(2) == '1' && addBorder[i] == false) {
        notABC = false;
      } 
      if (str.charAt(0) == '1' && str.charAt(1) == '1' && str.charAt(2) == '0' && addBorder[i] == false) {
        ABnotC = false;
      }
      if (str.charAt(0) == '1' && str.charAt(1) == '1' && str.charAt(3) == '1' && addBorder[i] == false) {
        ABD = false;
      }
      if (str.charAt(0) == '1' && str.charAt(1) == '1' && str.charAt(2) == '1' && addBorder[i] == false) {
        ABC = false;
      } 
      if (str.charAt(0) == '1' && str.charAt(1) == '0' && str.charAt(2) == '0' && addBorder[i] == false) {
        AnotBnotC = false;
      } 
      if (str.charAt(0) == '1' && str.charAt(1) == '0' && str.charAt(3) == '1' && addBorder[i] == false) {
        AnotBD = false;
      }
      if (str.charAt(0) == '1' && str.charAt(1) == '0' && str.charAt(2) == '1' && addBorder[i] == false) {
        AnotBC = false;
      }
      if (str.charAt(0) == '0' && str.charAt(2) == '0' && str.charAt(3) == '0' && addBorder[i] == false) {
        notAnotCnotD = false;
      } 
      if (str.charAt(1) == '1' && str.charAt(2) == '0' && str.charAt(3) == '0' && addBorder[i] == false) {
        BnotCnotD = false;
      } 
      if (str.charAt(0) == '1' && str.charAt(2) == '0' && str.charAt(3) == '0' && addBorder[i] == false) {
        AnotCnotD = false;
      }
      if (str.charAt(0) == '0' && str.charAt(2) == '0' && str.charAt(3) == '1' && addBorder[i] == false) {
        notAnotCD = false;
      }
      if (str.charAt(1) == '1' && str.charAt(2) == '0' && str.charAt(3) == '1' && addBorder[i] == false) {
        BnotCD = false;
      }
      if (str.charAt(0) == '1' && str.charAt(2) == '0' && str.charAt(3) == '1' && addBorder[i] == false) {
        AnotCD = false;
      }
      if (str.charAt(0) == '0' && str.charAt(2) == '1' && str.charAt(3) == '1' && addBorder[i] == false) {
        notACD = false;
      }
      if (str.charAt(1) == '1' && str.charAt(2) == '1' && str.charAt(3) == '1' && addBorder[i] == false) {
        BCD = false;
      } 
      if (str.charAt(0) == '1' && str.charAt(2) == '1' && str.charAt(3) == '1' && addBorder[i] == false) {
        ACD = false;
      } 
      if (str.charAt(0) == '0' && str.charAt(2) == '1' && str.charAt(3) == '0' && addBorder[i] == false) {
        notACnotD = false;
      }
      if (str.charAt(1) == '1' && str.charAt(2) == '1' && str.charAt(3) == '0' && addBorder[i] == false) {
        BCnotD = false;
      } 
      if (str.charAt(0) == '1' && str.charAt(2) == '1' && str.charAt(3) == '0' && addBorder[i] == false) {
        ACnotD = false;
      } 
      if (str.charAt(0) == '0' && str.charAt(1) == '0' && str.charAt(3) == '0' && addBorder[i] == false) {
        notAnotBnotD = false;
      }
      if (str.charAt(0) == '0' && str.charAt(1) == '1' && str.charAt(3) == '0' && addBorder[i] == false) {
        notABnotD = false;
      }
      if (str.charAt(0) == '1' && str.charAt(1) == '1' && str.charAt(3) == '0' && addBorder[i] == false) {
        ABnotD = false;
      }
      if (str.charAt(0) == '1' && str.charAt(1) == '0' && str.charAt(3) == '0' && addBorder[i] == false) {
        AnotBnotD = false;
      }
      if (str.charAt(1) == '0' && str.charAt(2) == '0' && str.charAt(3) == '0' && addBorder[i] == false) {
        notBnotCnotD = false;
      }
      if (str.charAt(1) == '0' && str.charAt(2) == '0' && str.charAt(3) == '1' && addBorder[i] == false) {
        notBnotCD = false;
      }
      if (str.charAt(1) == '0' && str.charAt(2) == '1' && str.charAt(3) == '1' && addBorder[i] == false) {
        notBCD = false;
      }
      if (str.charAt(1) == '0' && str.charAt(2) == '1' && str.charAt(3) == '0' && addBorder[i] == false) {
        notBCnotD = false;
      }
    } 
    // if the boolean variable of the kmap is true, add it to the kmap
    if (notAnotBnotC == true || s.equals("A\'B\'C\'")) {
      fullPlacement(twoWordFullBorders, 0, "A\'B\'C\'");
    } else if (notAnotBD == true || s.equals("A\'B\'D")) {
      fullPlacement(twoWordFullBorders, 1, "A\'B\'D");
    } else if (notAnotBC == true || s.equals("A\'B\'C")) {
      fullPlacement(twoWordFullBorders, 2, "A\'B\'C");
    } else if (notABnotC == true || s.equals("A\'BC\'")) {
      fullPlacement(twoWordFullBorders, 3, "A\'BC\'");
    } else if (notABD == true || s.equals("A\'BD")) {
      fullPlacement(twoWordFullBorders, 4, "A\'BD");
    } else if (notABC == true || s.equals("A\'BC")) {
      fullPlacement(twoWordFullBorders, 5, "A\'BC");
    } else if (ABnotC == true || s.equals("ABC\'")) {
      fullPlacement(twoWordFullBorders, 6, "ABC\'");
    } else if (ABD == true || s.equals("ABD")) {
      fullPlacement(twoWordFullBorders, 7, "ABD");
    } else if (ABC == true || s.equals("ABC")) {
      fullPlacement(twoWordFullBorders, 8, "ABC");
    } else if (AnotBnotC == true || s.equals("AB\'C\'")) {
      fullPlacement(twoWordFullBorders, 9, "AB\'C\'");
    } else if (AnotBD == true || s.equals("AB\'D")) {
      fullPlacement(twoWordFullBorders, 10, "AB\'D");
    } else if (AnotBC == true || s.equals("AB\'C")) {
      fullPlacement(twoWordFullBorders, 11, "AB\'C");
    } else if (notAnotCnotD == true || s.equals("A\'C\'D\'")) {
      fullPlacement(twoWordFullBorders, 12, "A\'C\'D\'");
    } else if (BnotCnotD == true || s.equals("BC\'D\'")) {
      fullPlacement(twoWordFullBorders, 13, "BC\'D\'");
    } else if (AnotCnotD == true || s.equals("AC\'D\'")) {
      fullPlacement(twoWordFullBorders, 14, "AC\'D\'");
    } else if (notAnotCD == true || s.equals("A\'C\'D")) {
      fullPlacement(twoWordFullBorders, 15, "A\'C\'D");
    } else if (BnotCD == true || s.equals("BC\'D")) {
      fullPlacement(twoWordFullBorders, 16, "BC\'D");
    } else if (AnotCD == true || s.equals("AC\'D")) {
      fullPlacement(twoWordFullBorders, 17, "AC\'D");
    } else if (notACD == true || s.equals("A\'CD")) {
      fullPlacement(twoWordFullBorders, 18, "A\'CD");
    } else if (BCD == true || s.equals("BCD")) {
      fullPlacement(twoWordFullBorders, 19, "BCD");
    } else if (ACD == true || s.equals("ACD")) {
      fullPlacement(twoWordFullBorders, 20, "ACD");
    } else if (notACnotD == true || s.equals("A\'CD\'")) {
      fullPlacement(twoWordFullBorders, 21, "A\'CD\'");
    } else if (BCnotD == true || s.equals("BCD\'")) {
      fullPlacement(twoWordFullBorders, 22, "BCD\'");
    } else if (ACnotD == true || s.equals("ACD\'")) {
      fullPlacement(twoWordFullBorders, 23, "ACD\'");     
    // wrap around
    } else if (notAnotBnotD == true || s.equals("A\'B\'D\'")) {
      wrapAroundPlacement(twoWordWrapAroundBorders, 0, "A\'B\'D\'");
    } else if (notABnotD == true || s.equals("A\'BD\'")) {
      wrapAroundPlacement(twoWordWrapAroundBorders, 1, "A\'BD\'");
    } else if (ABnotD == true || s.equals("ABD\'")) {
      wrapAroundPlacement(twoWordWrapAroundBorders, 2, "ABD\'");
    } else if (AnotBnotD == true || s.equals("AB\'D\'")) {
      wrapAroundPlacement(twoWordWrapAroundBorders, 3, "AB\'D\'");
    } else if (notBnotCnotD == true || s.equals("B\'C\'D\'")) {
      wrapAroundPlacement(twoWordWrapAroundBorders, 4, "B\'C\'D\'");
    } else if (notBnotCD == true || s.equals("B\'C\'D")) {
      wrapAroundPlacement(twoWordWrapAroundBorders, 5, "B\'C\'D");
    } else if (notBCD == true || s.equals("B\'CD")) {
      wrapAroundPlacement(twoWordWrapAroundBorders, 6, "B\'CD");
    } else if (notBCnotD == true || s.equals("B\'CD\'")) {
      wrapAroundPlacement(twoWordWrapAroundBorders, 7, "B\'CD\'");
    }
    
    // reset the boolean variables
    notAnotBnotC = true;
    notAnotBD = true;
    notAnotBC = true;
    notABnotC = true;
    notABD = true;
    notABC = true;
    ABnotC = true;
    ABD = true;
    ABC = true;
    AnotBnotC = true;
    AnotBD = true;
    AnotBC = true;
    notAnotCnotD = true;
    BnotCnotD = true;
    AnotCnotD = true;
    notAnotCD = true;
    BnotCD = true;
    AnotCD = true;
    BCD = true;
    ACD = true;
    notACnotD = true;
    BCnotD = true;
    ACnotD = true;
    notAnotBnotD = true;
    notABnotD = true;
    ABnotD = true;
    AnotBnotD = true;
    notBnotCnotD = true;
    notBnotCD = true;
    notBCD = true;
    notBCnotD = true;
  } 
  public void buildFour() {
    
    // Four Full Vertical and Horizontal Squares
    int j = 0;
    int downCount = 0;
    int sideCount = 0;
    for (j = 0; j < 9; j++) {
      fourWordFullBorders[j] = new JLabel();
      fourWordFullBorders[j].setBounds(locations[sideCount][0] - 10, locations[downCount * 4][1], 84, 84);
      fourWordFullBorders[j].setBorder(new MatteBorder(1, 1, 1, 1, Color.YELLOW));
      pane.add(fourWordFullBorders[j]);
      fourWordFullBorders[j].setVisible(false);
      sideCount++;
      if (sideCount == 3) {
        sideCount = 0;
        downCount++;
      }
    }
    // Four 4x1 vertical
    for (j = j; j < 13; j++) {
      int x = j - 9;
      fourWordFullBorders[j] = new JLabel();
      fourWordFullBorders[j].setBounds(locations[x + 4][0] - 10, locations[0][1], 20, 200);
      pane.add(fourWordFullBorders[j]);
      fourWordFullBorders[j].setBorder(new MatteBorder(1, 1, 1, 1, Color.YELLOW));
      fourWordFullBorders[j].setVisible(false);
    }
    
    // Four 4x1 horizontal
    for (j = j; j < 17; j++) {
      int x = j - 13;
      fourWordFullBorders[j] = new JLabel();
      fourWordFullBorders[j].setBounds(locations[0][0] - 10, locations[x * 4][1], 180, 20);
      pane.add(fourWordFullBorders[j]);
      fourWordFullBorders[j].setBorder(new MatteBorder(1, 1, 1, 1, Color.YELLOW));
      fourWordFullBorders[j].setVisible(false);
    }
    // Four Wrap Around Vertical
    j = 0; 
    for (j = j; j < 3; j++) {
      fourWordWrapAroundBorders[j][0] = new JLabel();
      fourWordWrapAroundBorders[j][1] = new JLabel();
    
      fourWordWrapAroundBorders[j][0].setBounds(locations[0][0] - 2 - 30, locations[0 + (j * 4)][1], 40, locations[12][1] + 20 - locations[8][1]);
      fourWordWrapAroundBorders[j][1].setBounds(locations[3][0] - 2, locations[3 + (j * 4)][1] - 2, 40, locations[15][1] + 20 - locations[11][1]);
    
      fourWordWrapAroundBorders[j][0].setBorder(new MatteBorder(1, 0, 1, 1, Color.BLUE));
      fourWordWrapAroundBorders[j][1].setBorder(new MatteBorder(1, 1, 1, 0, Color.BLUE));
    
      pane.add(fourWordWrapAroundBorders[j][0]); 
      pane.add(fourWordWrapAroundBorders[j][1]); 
    
      fourWordWrapAroundBorders[j][0].setVisible(false);
      fourWordWrapAroundBorders[j][1].setVisible(false);
    }
    
    // Four Wrap Around Horizontal 
    for (j = j; j < 6; j++) {
      int x = j - 3;
      fourWordWrapAroundBorders[j][0] = new JLabel();
      fourWordWrapAroundBorders[j][1] = new JLabel();
    
      fourWordWrapAroundBorders[j][0].setBounds(locations[0 + x][0] - 2, 180, 60, 60);
      fourWordWrapAroundBorders[j][1].setBounds(locations[12 + x][0] - 2, locations[12][1] - 2, 60, 60);
    
      fourWordWrapAroundBorders[j][0].setBorder(new MatteBorder(0, 1, 1, 1, Color.BLUE));
      fourWordWrapAroundBorders[j][1].setBorder(new MatteBorder(1, 1, 0, 1, Color.BLUE));
    
      pane.add(fourWordWrapAroundBorders[j][0]); 
      pane.add(fourWordWrapAroundBorders[j][1]); 
    
      fourWordWrapAroundBorders[j][0].setVisible(false);
      fourWordWrapAroundBorders[j][1].setVisible(false);
    }
    
    // Four Wrap Around Vertical 
    for (j = j; j < 6; j++) {
      int x = j - 3;
      fourWordWrapAroundBorders[j][0] = new JLabel();
      fourWordWrapAroundBorders[j][1] = new JLabel();
    
      fourWordWrapAroundBorders[j][0].setBounds(locations[0][0] - 2 - 30, locations[0 + (x * 4)][1], 40, locations[12][1] + 20 - locations[8][1]);
      fourWordWrapAroundBorders[j][1].setBounds(locations[3][0] - 2, locations[3 + (x * 4)][1] - 2, 40, locations[15][1] + 20 - locations[11][1]);
    
      fourWordWrapAroundBorders[j][0].setBorder(new MatteBorder(1, 0, 1, 1, Color.BLUE));
      fourWordWrapAroundBorders[j][1].setBorder(new MatteBorder(1, 1, 1, 0, Color.BLUE));
    
      pane.add(fourWordWrapAroundBorders[j][0]); 
      pane.add(fourWordWrapAroundBorders[j][1]); 
    
      fourWordWrapAroundBorders[j][0].setVisible(false);
      fourWordWrapAroundBorders[j][1].setVisible(false);
    }
    j = 0;
    for (j = 0; j < 4; j++) {
      corners[j] = new JLabel();  
    }
    
    corners[0].setBorder(new MatteBorder(0, 0, 1, 1, Color.YELLOW));
    corners[1].setBorder(new MatteBorder(0, 1, 1, 0, Color.YELLOW));
    corners[2].setBorder(new MatteBorder(1, 0, 0, 1, Color.YELLOW));
    corners[3].setBorder(new MatteBorder(1, 1, 0, 0, Color.YELLOW));
    corners[0].setBounds(locations[0][0] - 10, locations[0][1], 20, 20);
    corners[1].setBounds(locations[3][0] - 10, locations[3][1], 20, 20);
    corners[2].setBounds(locations[12][0] - 10, locations[12][1], 20, 20);
    corners[3].setBounds(locations[15][0] - 10, locations[15][1], 20, 20);
    
    j = 0;
    for (j = 0; j < 4; j++) {
      pane.add(corners[j]);
      corners[j].setVisible(false);
    }
  } 
  public void checkFour(String s) {
  
    String str = "";
    // boolean variable that signify if a kmap expression is visible or not
    boolean notAnotC = true;
    boolean notAD = true;
    boolean notAC = true;
    boolean BnotC = true;
    boolean BD = true;
    boolean BC = true;
    boolean AnotC = true;
    boolean AD = true;
    boolean AC = true;
    boolean notCnotD = true;
    boolean notCD = true;
    boolean CD = true;
    boolean CnotD = true;
    boolean notAnotB = true;
    boolean notAB = true;
    boolean AB = true;
    boolean AnotB = true;
    boolean notAnotD = true;
    boolean BnotD = true;
    boolean AnotD = true;
    boolean notBnotC = true;
    boolean notBD = true;
    boolean notBC = true;
    boolean notBnotD = true;
    
    // eliminate all incorrect answers by looping through all characters in the kmap
    for (int i = 0; i < 16; i++) {
      str = binaryValues[i].getText();
      if (str.charAt(0) == '0' && str.charAt(2) == '0' && addBorder[i] == false) {
        notAnotC = false;
      } 
      if (str.charAt(0) == '0' && str.charAt(3) == '1' && addBorder[i] == false) {
        notAD = false;
      } 
      if (str.charAt(0) == '0' && str.charAt(2) == '1' && addBorder[i] == false) {
        notAC = false;
      }
      if (str.charAt(1) == '1' && str.charAt(2) == '0' && addBorder[i] == false) {
        BnotC = false;
      } 
      if (str.charAt(1) == '1' && str.charAt(3) == '1' && addBorder[i] == false) {
        BD = false;
      } 
      if (str.charAt(1) == '1' && str.charAt(2) == '1' && addBorder[i] == false) {
        BC = false;
      }
      if (str.charAt(0) == '1' && str.charAt(2) == '0' && addBorder[i] == false) {
        AnotC = false;
      }
      if (str.charAt(0) == '1' && str.charAt(3) == '1' && addBorder[i] == false) {
        AD = false;
      }
      if (str.charAt(0) == '1' && str.charAt(2) == '1' && addBorder[i] == false) {
        AC = false;
      }
      if (str.charAt(2) == '0' && str.charAt(3) == '0' && addBorder[i] == false) {
        notCnotD = false;
      } 
      if (str.charAt(2) == '0' && str.charAt(3) == '1' && addBorder[i] == false) {
        notCD = false;
      } 
      if (str.charAt(2) == '1' && str.charAt(3) == '1' && addBorder[i] == false) {
        CD = false;
      }
      if (str.charAt(2) == '1' && str.charAt(3) == '0' && addBorder[i] == false) {
        CnotD = false;
      } 
      if (str.charAt(0) == '0' && str.charAt(1) == '0' && addBorder[i] == false) {
        notAnotB = false;
      } 
      if (str.charAt(0) == '0' && str.charAt(1) == '1' && addBorder[i] == false) {
        notAB = false;
      }
      if (str.charAt(0) == '1' && str.charAt(1) == '1' && addBorder[i] == false) {
        AB = false;
      }
      if (str.charAt(0) == '1' && str.charAt(1) == '0' && addBorder[i] == false) {
        AnotB = false;
      }
      if (str.charAt(0) == '0' && str.charAt(3) == '0' && addBorder[i] == false) {
        notAnotD = false;
      }
      if (str.charAt(1) == '1' && str.charAt(3) == '0' && addBorder[i] == false) {
        BnotD = false;
      }
      if (str.charAt(0) == '1' && str.charAt(3) == '0' && addBorder[i] == false) {
        AnotD = false;
      }
      if (str.charAt(1) == '0' && str.charAt(2) == '0' && addBorder[i] == false) {
        notBnotC = false;
      }
      if (str.charAt(1) == '0' && str.charAt(3) == '1' && addBorder[i] == false) {
        notBD = false;
      }
      if (str.charAt(1) == '0' && str.charAt(2) == '1' && addBorder[i] == false) {
        notBC = false;
      }
      if (str.charAt(1) == '0' && str.charAt(3) == '0' && addBorder[i] == false) {
        notBnotD = false;
      }
    }
    
    // if the boolean variable of the kmap is true, add it to the kmap  
    if (notAnotC == true  || s.equals("A\'C\'")) {
      fullPlacement(fourWordFullBorders, 0, "A\'C\'");
    } else if (notAD == true || s.equals("A\'D")) {
      fullPlacement(fourWordFullBorders, 1, "A\'D");
    } else if (notAC == true || s.equals("A\'C")) {
      fullPlacement(fourWordFullBorders, 2, "A\'C");
    } else if (BnotC == true || s.equals("BC\'")) {
      fullPlacement(fourWordFullBorders, 3, "BC\'");
    } else if (BD == true || s.equals("BD")) {
      fullPlacement(fourWordFullBorders, 4, "BD");
    } else if (BC == true || s.equals("BC")) {
      fullPlacement(fourWordFullBorders, 5, "BC");
    } else if (AnotC == true || s.equals("AC\'")) {
      fullPlacement(fourWordFullBorders, 6, "AC\'");
    } else if (AD == true || s.equals("AD")) {
      fullPlacement(fourWordFullBorders, 7, "AD");
    } else if (AC == true || s.equals("AC")) {
      fullPlacement(fourWordFullBorders, 8, "AC");
    } else if (notCnotD == true || s.equals("C\'D\'")) {
      fullPlacement(fourWordFullBorders, 9, "C\'D\'");
    } else if (notCD == true || s.equals("C\'D")) {
      fullPlacement(fourWordFullBorders, 10, "C\'D");
    } else if (CD == true || s.equals("CD")) {
      fullPlacement(fourWordFullBorders, 11, "CD");
    } else if (CnotD == true || s.equals("CD\'")) {
      fullPlacement(fourWordFullBorders, 12, "CD\'");
    } else if (notAnotB == true || s.equals("A\'B\'")) {
      fullPlacement(fourWordFullBorders, 13, "A\'B\'");
    } else if (notAB == true || s.equals("A\'B")) {
      fullPlacement(fourWordFullBorders, 14, "A\'B");
    } else if (AB == true || s.equals("AB")) {
      fullPlacement(fourWordFullBorders, 15, "AB");
    } else if (AnotB == true || s.equals("AB\'")) {
      fullPlacement(fourWordFullBorders, 16, "AB\'");
    } else if (notAnotD == true || s.equals("A\'D\'")) {
      wrapAroundPlacement(fourWordWrapAroundBorders, 0, "A\'D\'");
    } else if (BnotD == true || s.equals("BD\'")) {
      wrapAroundPlacement(fourWordWrapAroundBorders, 1, "BD\'");
    } else if (AnotD == true || s.equals("AD\'")) {
      wrapAroundPlacement(fourWordWrapAroundBorders, 2, "AD\'");
    } else if (notBnotC == true || s.equals("B\'C\'")) {
     wrapAroundPlacement(fourWordWrapAroundBorders, 3, "B\'C\'");
    } else if (notBD == true || s.equals("B\'D")) {
      wrapAroundPlacement(fourWordWrapAroundBorders, 4, "B\'D");
    } else if (notBC == true || s.equals("B\'C")) {
      wrapAroundPlacement(fourWordWrapAroundBorders, 5, "B\'C");
    } else if (notBnotD == true || s.equals("B\'D\'")) {
      fullPlacement(corners, 0, "B\'D\'");
    }
    
    // reset the boolean variables
    notAnotC = true;
    notAD = true;
    notAC = true;
    BnotC = true;
    BD = true;
    BC = true;
    AnotC = true;
    AD = true;
    AC = true;
    notCnotD = true;
    notCD = true;
    CD = true;
    CnotD = true;
    notAnotB = true;
    notAB = true;
    AB = true;
    AnotB = true;
    notAnotD = true;
    BnotD = true;
    AnotD = true;
    notBnotC = true;
    notBD = true;
    notBC = true;
  } 
  public void buildEight() {
  
     // Eight Full Vertical
    int j = 0;
    int count = 0;
    for (j = j; j < 3; j++) {
      eightWordFullBorders[j] = new JLabel();
      if (count % 2 == 1) {
        eightWordFullBorders[j].setBounds(locations[0][0] - 7, locations[count * 4][1] - 2, 179, 84);
      } else {
        eightWordFullBorders[j].setBounds(locations[0][0] - 2, locations[count * 4][1] - 2, 169, 84);
      }
      if (j == 0) {
        eightWordFullBorders[j].setBorder(new MatteBorder(1, 1, 1, 1, Color.BLUE));
      } else if (j == 1) {
        eightWordFullBorders[j].setBorder(new MatteBorder(1, 1, 1, 1, Color.RED));
      } else {
        eightWordFullBorders[j].setBorder(new MatteBorder(1, 1, 1, 1, Color.GREEN));
      } 
      pane.add(eightWordFullBorders[j]); 
      eightWordFullBorders[j].setVisible(false);
      count++;  
    }
    
    // Eight Full Horizontal
    count = 0;
    for (j = j; j < 6; j++) {
      eightWordFullBorders[j] = new JLabel();
      if (count % 2 == 1) {
        eightWordFullBorders[j].setBounds(locations[count * 1][0] - 7, locations[0][1] - 2, 84, locations[12][1] + 20 - locations[0][1]);
      } else {
        eightWordFullBorders[j].setBounds(locations[count * 1][0] - 2, locations[0][1] - 2, 84, locations[15][1] + 20 - locations[3][1]);
      }
      if (j == 4) {
        eightWordFullBorders[j].setBorder(new MatteBorder(1, 1, 1, 1, Color.BLUE));
      } else if (j == 4) {
        eightWordFullBorders[j].setBorder(new MatteBorder(1, 1, 1, 1, Color.RED));
      } else {
        eightWordFullBorders[j].setBorder(new MatteBorder(1, 1, 1, 1, Color.GREEN));
      } 
      pane.add(eightWordFullBorders[j]); 
      eightWordFullBorders[j].setVisible(false);
      count++;  
    }
    
    // Eight Wrap Around horizontal
    eightWordWrapAroundBorders[0][0] = new JLabel();
    eightWordWrapAroundBorders[0][1] = new JLabel();
    
    eightWordWrapAroundBorders[0][0].setBounds(locations[0][0] - 2, 180, 160, 60);
    eightWordWrapAroundBorders[0][1].setBounds(locations[12][0] - 2, locations[12][1] - 2, 160, 60);
    
    eightWordWrapAroundBorders[0][0].setBorder(new MatteBorder(0, 1, 1, 1, Color.BLUE));
    eightWordWrapAroundBorders[0][1].setBorder(new MatteBorder(1, 1, 0, 1, Color.BLUE));
    
    pane.add(eightWordWrapAroundBorders[0][0]); 
    pane.add(eightWordWrapAroundBorders[0][1]); 
    
    eightWordWrapAroundBorders[0][0].setVisible(false);
    eightWordWrapAroundBorders[0][1].setVisible(false);
    count++;
    
    // Eight Wrap Around Vertical
    eightWordWrapAroundBorders[1][0] = new JLabel();
    eightWordWrapAroundBorders[1][1] = new JLabel();
    
    eightWordWrapAroundBorders[1][0].setBounds(locations[0][0] - 2 - 30, locations[0][1], 40, locations[12][1] + 20 - locations[0][1]);
    eightWordWrapAroundBorders[1][1].setBounds(locations[3][0] - 2, locations[3][1] - 2, 40, locations[15][1] + 20 - locations[3][1]);
    
    eightWordWrapAroundBorders[1][0].setBorder(new MatteBorder(1, 0, 1, 1, Color.BLUE));
    eightWordWrapAroundBorders[1][1].setBorder(new MatteBorder(1, 1, 1, 0, Color.RED));
    
    pane.add(eightWordWrapAroundBorders[1][0]); 
    pane.add(eightWordWrapAroundBorders[1][1]); 
    
    eightWordWrapAroundBorders[1][0].setVisible(false);
    eightWordWrapAroundBorders[1][1].setVisible(false);
    count++;
  }  
  public void checkEight(String s) {
    
    String str = "";
    // boolean variable that signify if a kmap expression is visible or not
    boolean notA = true;
    boolean B = true;
    boolean A = true;
    boolean notC = true;
    boolean D = true;
    boolean C = true;
    boolean notB = true;
    boolean notD = true;
    
    // eliminate all incorrect answers by looping through all characters in the kmap
    for (int i = 0; i < 16; i++) {
      str = binaryValues[i].getText();
      if (str.charAt(0) == '0' && addBorder[i] == false) {
        notA = false;
      }
      if (str.charAt(1) == '1' && addBorder[i] == false) {
        B = false;
      }
      if (str.charAt(0) == '1' && addBorder[i] == false) {
        A = false;
      }
      if (str.charAt(2) == '0' && addBorder[i] == false) {
        notC = false;
      } 
      if (str.charAt(3) == '1' && addBorder[i] == false) {
        D = false;
      } 
      if (str.charAt(2) == '1' && addBorder[i] == false) {
        C = false;
      }
      if (str.charAt(1) == '0' && addBorder[i] == false) {
        notB = false;
      }
      if (str.charAt(3) == '0' && addBorder[i] == false) {
        notD = false;
      }
    }

    // if the boolean variable of the kmap is true, add it to the kmap
    if (notA == true || s.equals("A\'")) {
        fullPlacement(eightWordFullBorders, 0, "A\'");
    } else if (B == true || s.equals("B")) {
        fullPlacement(eightWordFullBorders, 1, "B");
    } else if (A == true || s.equals("A")) {
        fullPlacement(eightWordFullBorders, 2, "A");
    } else if (notC == true || s.equals("C\'")) {
      fullPlacement(eightWordFullBorders, 3, "C\'");
    } else if (D == true || s.equals("D")) {
      fullPlacement(eightWordFullBorders, 4, "D");
    } else if (C == true || s.equals("C")) {
      fullPlacement(eightWordFullBorders, 5, "C");
    } else if (notB == true || s.equals("B\'")) {
      wrapAroundPlacement(eightWordWrapAroundBorders, 0, "B\'");
    } else if (notD == true || s.equals("D\'")) {
      wrapAroundPlacement(eightWordWrapAroundBorders, 1, "D\'");
    }
    
    // reset the boolean variables     
    notA = true;
    B = true;
    A = true;
    notC = true;
    D = true;
    C = true;
    notB = true;
    notD = true;
  }  
  public void buildSixteen() {
  
    sixteenWordBorder[0] = new JLabel();
    sixteenWordBorder[0].setBounds(locations[0][0], locations[0][1], 160, 200);
    sixteenWordBorder[0].setBorder(BorderFactory.createLineBorder(Color.RED, 1)); 
    pane.add(sixteenWordBorder[0]);
    sixteenWordBorder[0].setVisible(false);
  } 
  public void checkSixteen(String s) {
    fullPlacement(sixteenWordBorder, 0, s);
  } 
  
  // Helper methods for building the Labels
  public void fullPlacement (JLabel[] arr, int location, String str) {
    // clear all the loops in the kmap
    if (str.equals("clear")) {
      for (int i = 0; i < arr.length; i++) {
        if (arr[i].isVisible() == true) {
          arr[i].setVisible(false);
        }
      }
      kmap = "";
      showKmap.setText(kmap);
      for (int i = 0; i < subcubes.length; i++) {
        subcubes[i].setText("");      
      }
      repaint();
      return;
    }

    if (arr[location].isVisible() == false) {
      int count = 0;
      // adds the subcube of the literal
      for (int i = 0; i < subcubes.length; i++) {
        if (subcubes[i].getText().equals("")) {
          subcubes[i].setForeground(colors[i]);
          subcubes[i].setText(str);
          count = i;
          break;
        }     
      }   
      
      //replace the colors with the temp colors     
      // represents the corners
      if (str.equals("B\'D\'")) {
        for (int i = 0; i < 4; i++) {
          Border tmpBorder = arr[i].getBorder();
          Insets tmpInsets = (tmpBorder.getBorderInsets(arr[i]));
          arr[i].setBorder(new MatteBorder(tmpInsets, colors[count]));
          arr[i].setVisible(true);
        }
      } else {
        // represents the rest of the full's
        Border tmpBorder = arr[location].getBorder();
        Insets tmpInsets = (tmpBorder.getBorderInsets(arr[location]));
        arr[location].setBorder(new MatteBorder(tmpInsets, colors[count]));
        arr[location].setVisible(true);
      } 
    } else {
   
      // represents the corners
      if (str.equals("B\'D\'")) {
        for (int i = 0; i < 4; i++) {
          arr[i].setVisible(false);
        }
        for (int i = 0; i < subcubes.length; i++) {
          if (subcubes[i].getText().equals(str)) {
            subcubes[i].setText("");
            break;
          }     
        }
      } else {
        // represents the rest of the full's
        arr[location].setVisible(false);
        for (int i = 0; i < subcubes.length; i++) {
          if (subcubes[i].getText().equals(str)) {
            subcubes[i].setText("");
            break;
          }     
        }
      }
    }
    kmapExpression();
    repaint();
  }
  public void wrapAroundPlacement(JLabel[][] arr, int location, String str) {
  
    if (str.equals("clear")) {
      for (int i = 0; i < arr.length; i++) {
        if (arr[i][0].isVisible() == true && arr[i][1].isVisible() == true) {
          arr[i][0].setVisible(false);
          arr[i][1].setVisible(false);
        }
      }
      kmap = "";
      showKmap.setText(kmap);
      for (int i = 0; i < subcubes.length; i++) {
        subcubes[i].setText("");      
      }
      repaint();
      return;
    }
    if (arr[location][0].isVisible() == true && arr[location][1].isVisible() == true) {
      arr[location][0].setVisible(false);
      arr[location][1].setVisible(false);
 
      for (int i = 0; i < subcubes.length; i++) {
        if (subcubes[i].getText().equals(str)) {
          subcubes[i].setText("");
          break;
        }     
      }
    } else { //remove the literal from the kmap  
      
      // adds the subcube of the literal
      int count = 0;
      for (int i = 0; i < subcubes.length; i++) {
        if (subcubes[i].getText().equals("")) {
          subcubes[i].setForeground(colors[i]);
          subcubes[i].setText(str);
          count = i;
          break;
        }     
      }
      //replace the colors with the temp colors
      Border tmpBorder0 = arr[location][0].getBorder();
      Border tmpBorder1 = arr[location][1].getBorder();
      Insets tmpInsets0 = tmpBorder0.getBorderInsets(arr[location][0]);
      Insets tmpInsets1 = tmpBorder1.getBorderInsets(arr[location][1]);
      arr[location][0].setBorder(new MatteBorder(tmpInsets0, colors[count]));
      arr[location][1].setBorder(new MatteBorder(tmpInsets1, colors[count]));
      arr[location][0].setVisible(true);
      arr[location][1].setVisible(true);
    }
    kmapExpression();
    repaint();
  }
   //modifies the canonical expression
   public void canonicalExpression() {
 
   // creates literals to replace the binary values with the letter literals ABCD
   String[] possibleCanonicals = new String[16];
   for (int i = 0; i < 16; i++) {
     String literal = "";
     if (i < 8) {
       literal = literal + "A\'";
     } else {
       literal = literal + "A"; 
     }
     if (i < 4 || (i >= 8 && i < 12)) {
       literal = literal + "B\'";
     } else {
       literal = literal + "B";
     }
     if (i % 4 < 2) {
       literal = literal + "C\'";
     } else {
       literal = literal + "C";
     }
     if (i % 2 == 0) {
       literal = literal + "D\'";
     } else {
       literal = literal + "D";
     }  
     possibleCanonicals[i] = literal;
   }
   
   // add the binary values to the canonical list with a 1 in the truth table
   for (int i = 0; i < map.length; i++) {
     if (map[i].getText() == MintermArray[1]) {
       canonical = canonical + possibleCanonicals[i] + "+";
     }
   } 
   if (canonical.length() > 0) {
     canonical = canonical.substring(0, canonical.length() - 1); 
   }
   showCanonical.setText(canonical);
   canonical = "";
 }
 
 // modifies the kmap expression after a change in subcubes has been detected
 public void kmapExpression() {
   
   for (int i = 0; i < subcubes.length; i++) {
     if (!(subcubes[i].getText().equals(""))) {
       kmap = kmap + subcubes[i].getText() + "+";
     }
   }
   if (kmap.length() > 0) {
     kmap = kmap.substring(0, kmap.length() - 1); 
   }
   showKmap.setText(kmap);
   kmap = "";
 }
 
 public void grid() {
   
   // creates the borders of the grid for the kmap
   JLabel topGrid = new JLabel();
   topGrid.setBounds(170, 200, 320, 200);
   topGrid.setBorder(new MatteBorder(2, 0, 0, 0, Color.BLACK));
   pane.add(topGrid);
   topGrid.setVisible(true);
   
   JLabel leftGrid = new JLabel();
   leftGrid.setBounds(220, 150, 220, 270);
   leftGrid.setBorder(new MatteBorder(0, 2, 0, 0, Color.BLACK));
   pane.add(leftGrid);
   leftGrid.setVisible(true);
   
   JLabel rightGrid = new JLabel();
   rightGrid.setBounds(440, 150, 440, 270);
   rightGrid.setBorder(new MatteBorder(0, 2, 0, 0, Color.BLACK));
   pane.add(rightGrid);
   rightGrid.setVisible(true);
   
   JLabel border = new JLabel();
   border.setBounds(170, 150, 320, 270);
   border.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
   pane.add(border);
   border.setVisible(true);
   
   // divides the row label and the column label
   JLabel squareA = new JLabel();
   squareA.setBounds(170, 150, 10, 10);
   squareA.setOpaque(true);
   squareA.setBackground(Color.BLACK);
   pane.add(squareA);
   squareA.setVisible(true);
   
   JLabel squareB = new JLabel();
   squareB.setBounds(180, 160, 10, 10);
   squareB.setOpaque(true);
   squareB.setBackground(Color.BLACK);
   pane.add(squareB);
   squareB.setVisible(true);
   
   JLabel squareC = new JLabel();
   squareC.setBounds(190, 170, 10, 10);
   squareC.setOpaque(true);
   squareC.setBackground(Color.BLACK);    
   pane.add(squareC);
   squareC.setVisible(true);
   
   JLabel squareD = new JLabel();
   squareD.setBounds(200, 180, 10, 10);
   squareD.setOpaque(true);
   squareD.setBackground(Color.BLACK);    
   pane.add(squareD);
   squareD.setVisible(true);
   
   JLabel squareE = new JLabel();
   squareE.setBounds(210, 190, 10, 10);
   squareE.setOpaque(true);
   squareE.setBackground(Color.BLACK);
   pane.add(squareE);
   squareE.setVisible(true);
   
 }
  
}