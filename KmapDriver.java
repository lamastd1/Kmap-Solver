package kmapSolver;

public class KmapDriver { 
  
	  public static void main(String[] args) throws Exception {
	    
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	      
	      public void run() {
	        
	        
	      	FourVariable f = new FourVariable();
	        Help h = new Help();
					f.setVisible(true);
          h.setVisible(true);
	      }
	      
	    });
	    
	  }
	  
	} 


