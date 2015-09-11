
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vineethsoma
 */
public class Memory {
    
   static int [] memory ; 
  static int arraySize = 2000 ;
   
     
    
    
    public static void main(String args[]) throws IOException{
        memory = new int [arraySize] ; 
     
        Scanner sc = new Scanner(System.in); // gets the input stream form proc
        int address= 0; // gets the input here
        String fileName ;

          fileName = args[0] ; 
    //    fileName = "sample2.txt";
        String command ; 

        loadToMemory(parse(getFileContent(fileName))); //gets content and prases away comments and one '\n' 
    //   printArray(memory,100) ;
        boolean exit = false ; 
        
        while(!exit && sc.hasNext()){
            command = sc.nextLine() ; 
            
            if(!command.contains("-1"))    
            switch(command.charAt(0) ){
                
                case'r':
                    
                    read( command ) ; 
                    
                    break ;
                    
                case 'w':
                    
                    write( command) ; 
                    break ; 
                    
                case '!':
                    
                    exit = true ; 
                    break ; 
                default:
                    
                    
                    break ; 
            }
            
            else 
                exit = true ; 
            

            
        }
                
                
                
 
        
   }
    
    
 
    
    static int read(String command){
        
        String [] commandArray = command.split(":") ; 
        
        int address = Integer.parseInt(commandArray[1].trim()) ; 
        System.out.println( memory[address]) ; 
        return 0 ;
    }
    
    static void write( String command){
        int address = 0 ; 
        int data = 0 ; 
        
        String [] commandArray = command.split(":") ; 
        
        address = Integer.parseInt(commandArray[1].trim()) ; 
        data = Integer.parseInt(commandArray[2].trim()) ;
        
        memory[address] = data ; 
   
    }
    
    static String getFileContent( String fileName) throws FileNotFoundException, IOException{
        
        BufferedReader br = new BufferedReader(new FileReader(fileName)) ;
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        
        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        String everything = sb.toString();
        
        return everything ; 
    
    }
    
    static String[] parse(String content){ //removes comments and  '\n'
        String[] tempbuffer={}; 
        String [] buffer = {} ; 
       
        
        if(content != null){
           String adjusted = content.replaceAll("(?m)^[ \t]*\r?\n", "");
            
            tempbuffer = adjusted.split("\n") ; 
            
            for( int j = 0 ; j < tempbuffer.length ; j++ ){
                
                
                    if(tempbuffer[j].contains("//")){
                        
                            buffer = tempbuffer[j].split("//") ;
                        
                          
                            {
                                tempbuffer[j] = buffer[0].trim() ; 
                            }
                    }
                    
                    
                    else{
                        buffer = tempbuffer[j].split(" ") ;
                    tempbuffer[j] =  buffer[0].trim() ;
                    }
                    
                        
            }           
        }   
                StringTokenizer st  ; 
                String newArray[] = new String[arraySize] ; 
                int k = 0 ; 
                for(int i = 0 ; i < tempbuffer.length ; i++){
                   
                   if( tempbuffer[i].length() != 0 ){ 
                    newArray[k] = tempbuffer[i] ; 
                            k++ ; 
                            }
                
        //System.out.println(tempbuffer[i] + " length: " + tempbuffer[i].length()  ) ;
        
                }
                
                
                
                
       /* for ( int i = 0 ; i < newArray.length ; i++){
            
            System.out.println(newArray[i]); 
        } */
    
        
        return newArray; 
    }
    
    static void loadToMemory(String [] command){
 
        int j=0 , index = 0; 
        while( command[j] != null  ){

           
                 if(  command[j].contains(".") ){
                    index =  Integer.parseInt(command[j].substring(1, command[j].length()) ) ; // gets the value after '.'
                 }
                 else if(  !command[j].contains(".") && command[j].length() != 0 )
                    memory[index] =  Integer.parseInt(command[j]) ;
              
              
            index++ ; 
            j++ ; 
            }
           
          
        }
        
    
    
    
    static void printArray(int [] array , int n ){
        
        for(int i= 0; i< n ; i++)
            System.out.println( array[i]);
        
        
      
    }
}
