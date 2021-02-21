package kmapSolver;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;

public class About extends JFrame {
  
  private JPanel pane = new JPanel();
  
  public About () {
    
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    setBounds(x - 300, 50, 700, 800);
    pane.setLayout(null);
    this.pane.setBounds(0, 0, 2000, 2000);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    // labels the about page
    JLabel about = new JLabel("ABOUT");
    about.setBounds(300, 50, 100, 40);
    about.setFont(about.getFont().deriveFont(28f));
    pane.add(about);
    about.setVisible(true);
   
    // creates an about page
    JTextArea textArea = new JTextArea("This Kmap Solver was created by Dominick Lamastra [TCNJ 2023]. The Quine McCluskey algorithm was used to find the minimal sum of products. This tool was created to support the CSC 325 Computer Architecture course at The College of New Jersey.  \n"
       + "\n"
       + "Version History:\n"
       + "Beta Version 9:  February 5, 2021\n"
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
   
  }
}
