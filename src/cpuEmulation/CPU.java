
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vineethsoma
 */
public class CPU {
    
    static Integer  timer, PC, AC, IR, X, Y , SP, systemStack, counter; 
    static boolean interruptStatus ; 
    
    
    public static void main(String args[])
   {
      try
      {            
	// String fileName = args[0] ;
        //  timer = Integer.parseInt( args[2] ) ; 
        // timer = 5 ; 
         timer = Integer.parseInt(args[1]); 
          PC = 0;  // program counter 
          SP = 999; // stack pointer 
          systemStack = 1999 ; // system stack pointer 
          IR = 0; // Instruction register 
          AC = 0; // Accumulator 
          X = 0;  
          Y = 0;
          counter = 0 ; 
          interruptStatus = true ; // interupt enabled 
         String fileName = args[0] ; 
          //String fileName = "sample2.txt" ; 
	 Runtime rt = Runtime.getRuntime();
         Process proc = rt.exec("java Memory "+ fileName);
         
        // testConsole(proc) ;  // debugging tool to check CPU register and memory after each IR command 
         executeUserProgram( proc) ; 
        // System.out.println(read(proc,10)) ;
	 proc.waitFor();

         int exitVal = proc.exitValue();

       //  System.out.println("\nMemory Process exited: " + exitVal);

      } 
      catch (Throwable t)
      {
	 t.printStackTrace();
      }
   
      
   }   
      static int read( Process proc , int address){
          
        //  if( address >=0 && address <= 1000){
        InputStream is = proc.getInputStream();
	OutputStream os = proc.getOutputStream();

        PrintWriter pw = new PrintWriter(os);

        String  command = "r:"+address ;  
        pw.printf(command + "\n");
        pw.flush() ; 
        
        Scanner sc = new Scanner(is);
	String line = sc.nextLine();
      //  System.out.println(line); 
        line = line.trim() ; 
        Integer value  = Integer.parseInt(line) ; 
        return value ;
          
          
          
             
      }
      
      static void write( Process proc , int address, int data){
  
        InputStream is = proc.getInputStream();
	OutputStream os = proc.getOutputStream();
         
        PrintWriter pw = new PrintWriter(os);
	
        String  command = "w:"+address+":"+data ; 
        pw.printf(command + "\n");
        pw.flush() ; 
        
        Scanner sc = new Scanner(is);
	//String line = sc.nextLine();
       // System.out.println(line); 
        
        
      }
    
   
    static void executeUserProgram( Process proc){

        int exit = 0 ; 
        while(exit != 1 && PC < 2000){
            // sysinfo(proc) ;     
            IR = read(proc,PC) ; // instruction loaded onto IR 
            PC ++ ;  // program counter incremented 
           counter++ ; 
            exit = instructionSet(proc, IR) ; // send to 
            if( interruptStatus == true){
                
            }
        }

        }

    static void testConsole( Process proc){

            int exit = 0 ; 
            Scanner in = new Scanner(System.in) ; 
            String[] com ; 
            String command ; 
            while(exit != 1 ){
                command = "" ; 
                command = in.nextLine() ; 
                com = command.split(" ") ; 

                switch( com[0]){

                    case "r":
                        System.out.println(read(proc,Integer.parseInt(com[1].trim()))) ; 
                        break ; 
                    case "w":
                        write(proc, Integer.parseInt(com[1].trim()),Integer.parseInt(com[2].trim()) ) ; 
                        break ; 
                    case "sys":
                        System.out.println("PC: "+ PC ) ; 
                        System.out.println("AC: "+ AC ) ;
                        System.out.println("SP: "+ SP ) ;
                        System.out.println("X: "+ X ) ;
                        System.out.println("Y: "+ Y ) ; 
                        break ;
                    case "exit":
                        break ;
                    case "IR":
                        PC ++  ; 
                        instructionSet(proc, Integer.parseInt(com[1].trim())) ; 
                    default: 
                        //System.out.println("Invalid command: "+com[0]);
                        break ; 


                }



            }

        }

    static void sysinfo(Process proc){ // degugging tool 

        System.out.println("\nPC: "+ PC ) ; 
           // System.out.println("here");
                    System.out.println("memory["+PC+"]:" + read(proc,PC) ) ;
                    System.out.println("AC: "+ AC ) ;
                    System.out.println("SP: "+ SP ) ;
                    System.out.println("IR: "+ IR) ;
                    System.out.println("X: "+ X ) ;
                    System.out.println("Y: "+ Y ) ; 
    }

    static int instructionSet(Process proc, int IR){
        
        switch(IR){
            
            case 1: //Load value AC = memory[PC]										//load instruction

                AC = read(proc,PC);
               // sysinfo(proc) ; 
                PC++;
                counter++ ; 
                break; 
                
            case 2: // Load addr AC = memory[memory[PC]]
                
                AC = read( proc, read(proc,PC) ); 
                PC++ ; 
                counter++ ; 
                break ;
                
            case 3: //  LoadInd addr AC = memory[memory[memory[PC]]]
                
                AC = read(proc,read( proc, read(proc,PC) ) ) ; 
                PC++ ; 
                counter++ ; 
                break ; 
            case 4: // LoadIndX addr AC = memory[[memory[PC]+X]
                
                AC = read( proc, read(proc,PC)  + X) ; 
              //  sysinfo(proc) ; 
                PC++ ; 
                counter++ ; 
                break ;
                
            case 5: // AC = memory[[memory[PC] +Y] 
                
                AC = read( proc, read(proc,PC)  + Y) ; 
                PC++ ; 
                counter++ ; 
                break ;  
                
            case 6: // LoadSpX AC = memory[SP + X]
                
                AC = read( proc, SP + 1 + X) ; 
                 
                break ; 
             
            case 7: // Store addr memory[PC] = AC 
                
                write(proc,PC,AC) ;  
                PC++ ; 
                counter++ ; 
                break ; 
            
            case 8: //Get 
                int minimum = 1 ; 
                int maximum = 100 ; 
                AC = minimum + (int)(Math.random()*maximum); 
                break ; 
                
            case 9: // Put port  
                int port = read(proc, PC) ;
                
                if(port ==1)
                    System.out.print(AC);
                else if(port==2)  {                 
                    char [] c = Character.toChars(AC) ;
                    // System.out.print(AC > 0 && AC < 27 ? String.valueOf((char)(AC + 64)) : null + " here ");
                    System.out.print(c[0]) ; 
                } 
                else 
                    System.out.println("Invalid port");
                //sysinfo(proc) ; 
                    PC++ ; 
                    counter++ ; 
                    
                break ; 
            case 10: // AddX
                AC += X  ; 
                break ; 
            case 11: // AddY
                AC += Y  ; 
                break ; 
            case 12: // SubX 
                AC -= X  ; 
                break ; 
            case 13: // SubY
                AC -= Y  ; 
                break ; 
            case 14: //CopyToX
                X = AC ; 
                break ; 
            case 15: //CopyFromX
                AC = X ; 
                break ;     
            case 16: //CopyToY
                Y = AC ; 
                break ; 
            case 17: //CopyFromY
                AC = Y ; 
                break ;
            case 18: //CopyToSp
                SP = AC ; 
                break ; 
            case 19: //CopyFromSp
                AC = SP ; 
                break ;
            case 20: // Jump addr 
                PC = read(proc,PC) ; 
                break ; 
            case 21: // JumpIfEqual addr
                if(AC==0)
                   PC = read(proc,PC) ;
                else 
                    PC++ ;
                counter++ ; 
                break ; 
            case 22: //JumpIfNotEqual addr
                if(AC!=0)
                   PC = read(proc,PC) ;
                else 
                    PC ++ ;
                break; 
            case 23: // Call addr 
                // need to write code 
                int addr = read(proc, PC ) ;
                write(proc,SP,PC + 1) ; 
               // sysinfo(proc) ; 
                SP-- ; 
                PC = addr ; 
                break ; 
            case 24: // Ret 
                SP++ ; 
                PC = read( proc, SP ) ; 
                break ; 
                
            case 25: // IncX 
                X++; 
                break ; 
            case 26: //DecX
                X-- ; 
                break ; 
            case 27: // Push 
                write(proc, SP, AC) ;
                SP-- ; 
                break ; 
            case 28: // Pop 
                SP++ ; 
                AC = read(proc,SP ) ; 
                break ; 
            case 29: // Set System mode, switch stack, push SP and PC, set new SP and PC 
                interruptStatus = false ; 
                write( proc, systemStack, PC) ;
                systemStack-- ;
                
                write( proc, systemStack, IR) ;
                systemStack-- ;
                
                write( proc, systemStack, AC) ;
                systemStack-- ;
                
               write( proc, systemStack, X) ;
                systemStack-- ;
                
                write( proc, systemStack, Y) ;
                systemStack-- ;
                
                write( proc, systemStack, SP) ;
                systemStack-- ;
                
                PC =1500 ; 
                SP = systemStack ; 
                
                break ; 
            case 30: // IREt
                // need to write code 
                systemStack++ ; 
                SP = read( proc, systemStack ) ; 
                
                systemStack++ ; 
                Y = read( proc, systemStack ) ;
                
                systemStack++ ; 
                X = read( proc, systemStack ) ;
                
                systemStack++ ; 
                AC = read( proc, systemStack ) ;
                
                systemStack++ ; 
                IR = read( proc, systemStack ) ;
                
                systemStack++ ; 
                PC = read( proc, systemStack ) ;
                
                interruptStatus = true ; 
               
                break ; 
            case 50: // End 
                write(proc,-1,0) ;
                return 1 ; 
            case -1:
                write(proc,-1,0) ; // memeory process stops when address is -1
            case -2:
                
                break ; 
            default:
                break ; 
                
            }
        return 0 ; 
    }
    
}