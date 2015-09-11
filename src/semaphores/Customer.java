import java.util.concurrent.Semaphore ; 
import java.util.Random ; 
import java.util.logging.Level;
import java.util.logging.Logger;

public class Customer implements Runnable{
    
    int id ; // uniquely identifies each customer 
    public  int task ; // 0= buy stamps , 1 = mail a letter, 2 = mail a package
    static Semaphore poLimit  ; //Post Office capacity 
    static Semaphore pwLimit ; // postal worker limit 
    
    Semaphore taskDone = new Semaphore(0) ;  // used to make customer wait till PO worker finishes with task 
    PostalWorker [] postalWorker ; 
    PostalWorker assistingPostalWorker ;  // stores the address for postal worker assisting this customer 
    
    Customer(Semaphore postOfficeLimit, Semaphore postalWorkerLimit, int n, PostalWorker [] postalWorker ){
        
        poLimit = postOfficeLimit; 
        pwLimit = postalWorkerLimit ; 
        id = n ; 
        this.postalWorker = postalWorker ; 
    }    

    @Override
    public void run(){
        
        enterPostOffice() ;  // customer acquires poLimit 
        assignTask() ; // customer is randomly assigned a task 
        waitForIdlePostalWorker() ; // customer waits  to obtain idle postal worker, done by acquiring idlePostalWorker 
        waitForTaskToBeDone() ;  // waits for potal worker to  release taskDone 
        exitPostOffice() ; // customer relases poLimit 
        
    }
    
    void enterPostOffice() {
        
        try { 
            poLimit.acquire() ;
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Customer "+id+" entered the Post Office");
        
    }
    
    void assignTask(){
        
        int randomNumber = new Random().nextInt(3) ; 
        
        task = randomNumber ;         
        
    }
    
    void waitForIdlePostalWorker(){
        try {
            pwLimit.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean foundWorker = false ; 
        int i = 0 ; 
        while(foundWorker == false){
            
            if(postalWorker[i].idlePostalWorkerFound()){
                
                foundWorker = true ; 
                postalWorker[i].currentCustomer = this ; 
                postalWorker[i].stopWaitingForCustomer();
                
                assistingPostalWorker= postalWorker[i] ; 
                
            }
            else if ( i == 2){
                
                i = -1 ; 
            }
            
            i++ ; 
            
        }
        
    }
    
    void waitForTaskToBeDone(){
        
        try {
            taskDone.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    void taskDone(){ // releases taskdone, idlePostalWorker and pwLimit 
        
        taskDone.release();
        assistingPostalWorker.postalWorkerIdle();
        pwLimit.release();
        
    }
    void exitPostOffice(){
        
        poLimit.release(); 
        System.out.println("Customer "+id+" exited the Post Office");
      

    }    
}