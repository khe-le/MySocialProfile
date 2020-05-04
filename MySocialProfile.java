import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MySocialProfile
{
    public String name;
    public String email;
    public String pass;
    public int classYear;
    public Event userEvents = new Event();
    public SinglyLinkedStack<String> posts = new SinglyLinkedStack<>();
    public ArrayList<String> friends = new ArrayList<>();
    
    public void createNewAcc()
    {
        Scanner user = new Scanner(System.in);

	System.out.print("Please enter your name: ");
        this.name = user.nextLine();
	    
        System.out.print("Please enter your email address: ");
        this.email = user.next();

        System.out.print("Please enter a password for your account: ");
        this.pass = user.next();

        System.out.print("Please enter your class year: ");
        this.classYear = user.nextInt();
        user.nextLine();  //catch the carriage. DO NOT REMOVE
       
        this.posts = new SinglyLinkedStack<>();
        this.friends = new ArrayList<>();
        //this.events = new Event();

        saveToRecord();
        System.out.print("---Your account has been successfully created---\n");
        

        user.close();
    }

    public void loadprofile(){
	    doStuff();
    }

    public void post()  //use the parameter one. ask for info from main
    {
    	Scanner in = new Scanner(System.in);
    	System.out.print("What is on your mind: ");
        String newPost = in.next();
        in.nextLine();   //to catch carriage 
    	
        posts.push(newPost);

    }
    public void addEvent()
    {
        userEvents.addEvent();
    }


    public void manageFriend()  //use the parameter one. ask for info from main
    {
    	Scanner in = new Scanner(System.in);
    	System.out.print("Please enter your friend's email address: ");
        String friend = in.next();
        in.nextLine();    //to catch carriage 
        
        if (friends.contains(friend))
        {	
        	System.out.print("True");
            friends.remove(friend);
        }
        else 
            friends.add(friend);
        
    }
 
    /*
     * Write or update user's info into a text file (replacing logout() method)
    */
    public void saveToRecord()
    {
        try 
        {
			FileWriter fileOut = new FileWriter("newFile.txt");
			BufferedWriter bufWriter = new BufferedWriter(fileOut);
			
			bufWriter.write(name + "\n" + email + "\n" + pass + "\n" + Integer.toString(classYear) + "\n" + userEvents.toString() + "\n" + posts.toString() + "\n" + friends.toString().replaceAll("\\s",""));
			//for the moment, havent write the event in yet
			bufWriter.close();
            fileOut.close();
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }      
    }   

    private void doStuff()
    {
            //this try-catch statement is needed around this file input code
            //because the FileInputStream may throw a FileNotFoundException
            try
            {
                Scanner fileIn = new Scanner(new FileInputStream("newFile.txt"));
                while (fileIn.hasNext()) { //while more of the input file is still available for reading
                    this.name = fileIn.nextLine();  //reads an entire line of input
                    System.out.println("Name: " + name);
        
                    this.email = fileIn.nextLine();
        
                    this.pass = fileIn.nextLine();
        
                    this.classYear = Integer.parseInt(fileIn.nextLine());
                    System.out.println("Year: " + classYear);
        
                    String events = fileIn.nextLine(); //read the entire line of event data. keep it as a string for now
        
                    Scanner eventsScanner = new Scanner(events);
                    String[] eventsArray = new String[10]; //will store the individual events for now
                    int i = 0; //array index counter
        
                    // so we tell the scanner to look for a quotation mark followed by a comma (",)
                    eventsScanner.useDelimiter("\","); //need the backslash in front of special characters like "
                    String e; //will hold each individual event
                    System.out.println("Events: ");
                    while (eventsScanner.hasNext())
                    {
                        e = eventsScanner.next();
                        e = e.substring(1, e.length()); //cut off the leading quotation mark of each event
                        eventsArray[i] = e;
                        i++;
        
                        Scanner eScanner = new Scanner(e); //yet another scanner just for this particular event
                        int month = eScanner.nextInt();
                        int day = eScanner.nextInt();
                        int year = eScanner.nextInt();
                        int hour = eScanner.nextInt();
                        int min = eScanner.nextInt();
        
                        String date = Integer.toString(month) + '/' + Integer.toString(day) +'/' + Integer.toString(year) + ' ' + Integer.toString(hour) + ':' + Integer.toString(min);
                        ArrayList<Integer> dateKey = new ArrayList<>();
                        dateKey.add(month);
                        dateKey.add(day);
                        dateKey.add(year);
                        dateKey.add(hour);
                        dateKey.add(min);
                        String desc = ""; //to hold the description of the event
                        while (eScanner.hasNext()){ //while there are words left...
                            desc = desc + " " + eScanner.next(); //reads the description one word at a time
                        }
                        if (!userEvents.passEvent(dateKey))
                        {
                            userEvents.addEvent(dateKey, desc);
                            System.out.println(date + ": " + desc);
                        }
                    }			
                    eventsScanner.close();
        
                    /* reads in next line and then breaks it into separate timeline posts/messages
                     * code is analogous to events, so refer to above comments for explanation. */
                    String posts = fileIn.nextLine();
                    Scanner postScanner = new Scanner(posts);
                    this.posts = new SinglyLinkedStack<>();
        
                    postScanner.useDelimiter("\",");
                    String message; 
                    System.out.print("Posts: ");
                    while (postScanner.hasNext()) 
                    {
                        message = postScanner.next();
                        message = message.substring(1, message.length());
                        this.posts.push(message);
                    }
                    this.posts.display();
                    postScanner.close();
        
        
                    /* reads in next line and then breaks it into separate friends
                     * now the delimiter is just a comma because there are no quotes around
                     * each email address.  so this is a bit simpler than above procedure.*/
                    String friends = fileIn.nextLine().replaceAll("\\[", "").replaceAll("\\]", "");
                    Scanner friendScanner = new Scanner(friends);
                    this.friends = new ArrayList<>();
        
                    friendScanner.useDelimiter(",");  
                    String friend;
                    System.out.print("Friends: ");
                    while (friendScanner.hasNext()) 
                    {
                        friend = friendScanner.next();
                        System.out.print(friend + ",");
                        this.friends.add(friend);
                    }
                    friendScanner.close();
                    System.out.println();
                    System.out.println("+++++++++++++++++++++++++++++++++");
                }
                }
                catch(FileNotFoundException ex) 
                {
                    System.out.println("File not Found");
                    System.exit(0);        
                }
        }
    
    

    public static void main(String[] args)
    {
        MySocialProfile profile = new MySocialProfile();
        //profile.createNewAcc();
        profile.loadprofile();
        //profile.post("Hello World");
        //System.out.println(profile.friends);
        //profile.manageFriend("kle2@conncol.edu");
        //profile.addEvent();
        profile.saveToRecord();
    }
}
