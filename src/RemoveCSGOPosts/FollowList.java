package RemoveCSGOPosts;


import java.util.ArrayList;


public class FollowList {

	public static ArrayList<Long> FollowListArray;

    public FollowList(){
    	FollowListArray = new ArrayList<Long>();
    	
        System.out.println("Followers List");
    }

 

    public void add(long user){
        if(!FollowListArray.contains(user)){
        	FollowListArray.add(user);    
        }
    }
    
    public void remove(long user){
        if(FollowListArray.contains(user)){
        	FollowListArray.remove(FollowListArray.indexOf(user));   
        }
    }
    
    public boolean contains(Long user){
        return FollowListArray.contains(user);
    }




}


