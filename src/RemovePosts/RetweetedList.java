package RemovePosts;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;




public class RetweetedList {
	
	 String filename = "retweetlist.txt";
	 
	 String path = System.getProperty("user.home") + File.separator + "Documents"+ File.separator + "KripTech"+ File.separator + "TwitterContestBot";
	
	 ArrayList<Long> tweets;
	 PrintWriter printWriter;
	 
    public RetweetedList(String id){
    	
    	
        tweets = new ArrayList<Long>();

       try{
    	   printWriter = new PrintWriter(new FileOutputStream(new File(path + File.separator+ id + File.separator + this.filename),true));
       }
       catch(Exception ex){
    	   
    	   EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						  JOptionPane.showMessageDialog(null, 
				 				  "Unable to create file for retweets or config", "Error", JOptionPane.WARNING_MESSAGE);
					} catch (Exception e) {
						
					}
				}
			});
    	   
    
       }
       
       File file = new File(path + File.separator + "config.txt");

	      try {
			if (file.createNewFile()){
			    System.out.println("File is created!");
			  }else{
			    System.out.println("File already exists.");
			  }
		} catch (IOException e) {
			e.printStackTrace();
		}


       
       System.out.println("Created Retweet List and config file");
    }

    
    public void LoadOnStartup(String id) throws FileNotFoundException{
  
            Scanner file = new Scanner(new File(path + File.separator + id + File.separator + this.filename));
            
            while(file.hasNextLong()){
                long current = file.nextLong();
                tweets.add(current);
            }
            file.close();
    }

    
    public boolean contains(long id){
        return tweets.contains(id);
    }

    public void add(long id){
    	
        if(!tweets.contains(id)){
        	
            tweets.add(id);
            
            printWriter.println(id);
            
            printWriter.flush();
        	printWriter.close();
        }
    }
}