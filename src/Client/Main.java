package Client;

import java.util.Scanner;

public class Main 
{
    static Scanner scanner= new Scanner (System.in);

    /** 
    * @return String
    */
    private static String command_request() 
    {
        System.out.println("\nType your desired command:");
        return scanner.nextLine();
    }
    
    public static void main (String[] args)
    {
        State state= new State();
        Handler handler= new Handler(state);

        // Handle commands
        String command;
        while (!(command = command_request()).equals("quit")) 
        {
            handler.handle (command);
        } 
    }
}
