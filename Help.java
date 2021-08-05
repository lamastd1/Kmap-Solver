package kmapSolver;

import javax.swing.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Help extends JFrame implements ActionListener {
  
  private JPanel pane = new JPanel();
  private JButton about; // sends user to about page
  private JButton threeVariable; // sends user to 3 variable kmap
  private JButton fourVariable; // sends user to 4 variable kmap
  
  public Help (){
   
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    setBounds(x - 350, 50, 700, 800);
    pane.setLayout(null);
    this.pane.setBounds(0, 0, 800, 1000);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  
    this.pane.setPreferredSize(new Dimension(800, 1000));
    final JScrollPane scrollp = new JScrollPane(this.pane, 
    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.add(scrollp);
    setVisible(true);
     
    // labels the help page
    JLabel help = new JLabel("HELP");
    help.setBounds(315, 0, 100, 50);
    help.setFont(help.getFont().deriveFont(28f));
    pane.add(help);
    help.setVisible(true);
   
    // creates a help page for the user
    JTextArea textArea = new JTextArea("1) Select the minterms you like to add to the kmap by pressing the respective button listed in the truth table next to the literals. Each minterm has "
       + "3 states: \n   a) a value of 1 means true \n   " + "b) a value of 0 means false \n   " + "c) a value of X means don't care \n\n"
       + "2) The Kmap Solver is used to find the minimum sum of products, or minimal SoP from a boolean expression, which is shown to you if you "
       + "click the button labeled show minimal. The canonical expression is an expression where each product term contains all of its literals. The kmap expression underneath "
       + "contains all of the products that the user has labeled. \n\n"
       + "3) Select the literals that you would like to add to the kmap by checking their checkbox below and click the "
       + "button above to place the group of literals into the kmap. Note that only valid entries will be displayed, which are only 1, 2, 4, 8, or 16 "
       + "literals long, all literals requested show a 1 or X in the kmap, and all literals checked can be grouped together in a box. The kmaps you click are highlighted in the kmap. "
       + " the highlights go away after the \"Generate Box Around Selected Minterms\" button. \n\n"
       + "4) The \"Clear Loops\" button clears all user made boxes. \n\n"
       + "5) The \"Show Minimal\" button displays the minimum sum of products \n\n"
       + "6) The \"Fill Kmap\" sets all minterms to 1 \n\n"
       + "7) The \"Reset Table\" button resets all minterms to 0 and clears all loops \n\n"
       + "8) Subcues: each subcube contains a literal that has been created by the user. To remove a literal, press the subcube button containing the literal you wish to remove. \n\n"
       + "9) Please close out of this help page by clicking the x in the upper right corner. If you wish to leave this application, close out of this help page and the main window by "
       + "clicking the x of the upper right hand corner of all tabs. \n\n"
       + "10) Above the kmap text area shows if the current kmap drawn on the screen is correct or incorrect, meaning the minumum possible amount of boxes cover all of the minterms. "
       + "Correct is labeled in green while incorrect is labeled in red\n\n"
       + "11) This screen can be accessed again by clicking the \"help\" button located on top of the kmap solver\n"
       + "12) Click a button above to get started!", 2, 1);
    textArea.setBounds(10, 100, 660, 700);
    textArea.setFont(textArea.getFont().deriveFont(14f));
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setEditable(false);
    pane.add(textArea);
    textArea.setVisible(true);
    
    // Creates buttons that will send the user to a different page of the program
    threeVariable = new JButton("Three Variable Kmap");
    threeVariable.setBounds(10, 60, 200, 30);
    threeVariable.addActionListener(this);
    pane.add(threeVariable); 
    threeVariable.setVisible(true);
    
    fourVariable = new JButton("Four Variable Kmap");
    fourVariable.setBounds(235, 60, 200, 30);
    fourVariable.addActionListener(this);
    pane.add(fourVariable); 
    fourVariable.setVisible(true);
    
    about = new JButton("About");
    about.setBounds(460, 60, 200, 30);
    about.addActionListener(this);
    pane.add(about); 
    about.setVisible(true);
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // the user clicked the three variable button 
    if (e.getSource() == threeVariable) {  
      ThreeVariable t = new ThreeVariable();
      t.setVisible(true);
      this.setVisible(false);
    }
    
    // the user clicked the four variable button
    if (e.getSource() == fourVariable) {
      FourVariable f = new FourVariable();
      f.setVisible(true);
      this.setVisible(false);
    }
    
    // user clicked the about button
    if (e.getSource() == about) {
      About a = new About();
      a.setVisible(true);
      this.setVisible(false);
    }
  }
}
