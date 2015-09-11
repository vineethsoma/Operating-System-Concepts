/*
Author: Vineeth Soma
This porject simulates the working of a Post Office by using semaphores 
*/
import java.util.concurrent.Semaphore ; 
import java.util.logging.Level;
import java.util.logging.Logger;

public class Project2 {
   
    final static int maxCapacity = 10 ; // post office capacity 
    final static int numberOfCustomers = 50 ; 
    final static int numberOfPostalWorkers = 3 ; 

    static Semaphore postOfficeLimit = new Semaphore(maxCapacity) ; // to allow only 10 customers into the po at a time 
    static Semaphore postalWorkerLimit = new Semaphore(numberOfPostalWorkers) ; // numbers of postal workers that can run at a time 
    static Thread [] customers  ;
    static Thread [] postalWorkers ;
    
    static Customer [] customerList = new Customer[numberOfCustomers];
    static PostalWorker [] postalWorkerList = new PostalWorker[numberOfPostalWorkers] ; 

    public static void main(String args[]){
        
        initializePostalWorkers() ;  // intializes and starts postal worker threads 
        initializeCustomers() ;     // initializes and starts customer threads 
        endCustomers() ;  // joins cutomers threads 
        endPostalWorkers() ;  // joins postal worker threads 
    
    }
    static void initializePostalWorkers(){
       
        postalWorkers = new Thread[numberOfPostalWorkers] ; 
        
        for( int i=0; i < numberOfPostalWorkers ; i++ ){
            
            postalWorkerList[i] = new PostalWorker(i) ; 
            postalWorkers[i] = new Thread(postalWorkerList[i]) ; 
            System.out.println("Postal Worker " +i+" created") ; 
            postalWorkers[i].start(); 
            
        }
    }   
    static void initializeCustomers(){
        
        customers = new Thread[numberOfCustomers] ; 
    
        for( int i = 0 ; i< customers.length ;i++)  { 
            
            customerList[i] = new Customer(postOfficeLimit,postalWorkerLimit, i, postalWorkerList) ; 
            customers[i] = new Thread(customerList[i]) ; 
            System.out.println("Customer " +i+ " created");
            customers[i].start() ;

     }
    }
    
    static void endCustomers(){
       for( int i = 0 ; i< customers.length ;i++) {
           try {
               customers[i].join();
            System.out.println("Joined Customer " + i );
           }catch (InterruptedException ex) {
               Logger.getLogger(Project2.class.getName()).log(Level.SEVERE, null, ex);
           }

       }  

    }
    
    static void endPostalWorkers(){
         for( int i=0; i < postalWorkers.length ; i++ ){
 
             try { 
                postalWorkers[i].join();
            System.out.println("Postal Worker " +i+" joined") ; 
                 
             } catch (InterruptedException ex) {
                 Logger.getLogger(Project2.class.getName()).log(Level.SEVERE, null, ex);
             }
             
             
        }          
    
    }
    
}