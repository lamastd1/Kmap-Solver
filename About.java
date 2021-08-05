package kmapSolver;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class About extends JFrame implements ActionListener {
  
  private JPanel pane = new JPanel();
  private JButton help; // sends user to help page
  private JButton threeVariable; // sends user to 3 variable kmap
  private JButton fourVariable; // sends user to 4 variable kmap
  
  public About () {
    
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    setBounds(x - 350, 50, 700, 800);
    pane.setLayout(null);
    this.pane.setBounds(0, 0, 800, 1000);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);

    // labels the about page
    JLabel about = new JLabel("ABOUT");
    about.setBounds(300, 0, 100, 50);
    about.setFont(about.getFont().deriveFont(28f));
    pane.add(about);
    about.setVisible(true);
   
    // creates an about page
    JTextArea textArea = new JTextArea("This Kmap Solver was created by Dominick Lamastra [TCNJ 2023]. The Quine McCluskey algorithm was used to find the minimal sum of products. This tool was created to support the CSC 325 Computer Architecture course at The College of New Jersey.  \n"
       + "\n"
       + "Version History:\n"
       + "Beta Version 9:  February 5, 2021\n"
       + "Full Version 1.0: August 5, 2021\n"
       + "\n"
       + "This project can be used as free open source software."
       + "\n\n"
       + "References: \n"
       + "Youtube. (2015). Quine-McCluskey Method with Don't Care [Video]. Retrieved 20 December \n  2020, from https://www.youtube.com/watch?v=B08vV3tIdag.\n"
       + "Agrawal, D. (2021). Print all possible combinations of r elements in a given array of size n - \n  GeeksforGeeks. GeeksforGeeks. Retrieved 27 January 2021, from https://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/."
       + "\n\n"
       + "Acknowledgements:\n"
       + "Thanks are extended to the following evaluators, who provided valuable feedback during development: Raymond Chow, Sterly Deracy, Matt Hannum, Max Landry, Casey Lishko, and Anthony Messana.\n"
       + "Faculty Mentor:  Deborah Knox, Ph.D.  ", 2, 1);
    textArea.setBounds(10, 100, 660, 700);
    textArea.setFont(textArea.getFont().deriveFont(14f));
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setEditable(false);
    pane.add(textArea);
    textArea.setVisible(true);
   
    this.pane.setPreferredSize(new Dimension(2000, 2000));
    final JScrollPane scrollp = new JScrollPane(this.pane, 
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.add(scrollp);
    setVisible(true);
    
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
    
    help = new JButton("Help");
    help.setBounds(460, 60, 200, 30);
    help.addActionListener(this);
    pane.add(help); 
    help.setVisible(true);
    
  }
  
  @Override
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
    if (e.getSource() == help) {
      Help h = new Help();
      h.setVisible(true);
      this.setVisible(false);
    }
  }
}
