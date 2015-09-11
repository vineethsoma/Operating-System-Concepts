import java.util.concurrent.Semaphore ; 
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostalWorker implements Runnable {
    int id ;  // recognizes postal worker uniquely 
    static long [] taskTimes = { 1000 , 1500, 2000 }  ; // times for task0, task1 , task2
    
    Semaphore idlePostalWorker = new Semaphore(1) ;  // used to check if postal worker is idle or not 
    Semaphore waitingForCustomer = new Semaphore(0) ;  // used to wait for customer to initiate a task 
    static Semaphore scale = new Semaphore(1) ;    // used as scale resource 
    
    public Customer  currentCustomer = null ;
    
    PostalWorker(int n){
        
        id = n ; 
    }
    @Override
    public void run() {
        while(true){
            waitingForCustomer() ;  // waits for customer till cutomer releases waitingForCustomer 
            assistingCurrentCustomer() ;  // executes after aquiring idle postal worker. PO worker is busy till task finishes, 
            
        }
    }
    
    void waitingForCustomer(){
        
        try {
            waitingForCustomer.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(PostalWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    void assistingCurrentCustomer(){

        doTask(currentCustomer.task) ; 
        
        System.out.println("Postal Worker " +id+ " finished assisting Customer " + currentCustomer.id);
     
        currentCustomer.taskDone() ; 
        
    }
    
    void stopWaitingForCustomer(){ // customer uses this function for postworker to start assisting 
        
        waitingForCustomer.release();
        
    }
    
    public boolean idlePostalWorkerFound(){ // customer finds idle postworker using this function 

        return idlePostalWorker.tryAcquire()  ; 
    }
    public void postalWorkerIdle(){ // customer sets postal worker to idle after task is done. 

        idlePostalWorker.release();
}
    
    void doTask(int task) {
        
        switch(task)
        {
            
            case 0: 
                System.out.println("Customer " +currentCustomer.id+ " asks Postal Worker "+id+" to buy stamps");
                buyStamps() ; 
                break ; 
                
            case 1:
                System.out.println("Customer " +currentCustomer.id+ " asks Postal Worker "+id+" to mail a letter");

                mailLetter() ; 
                break ; 
                
            case 2:
                System.out.println("Customer " +currentCustomer.id+ " asks Postal Worker "+id+" to mail a package");

                mailPackage() ; 
                break ;
                
            default: 
                // do nothing 
                System.out.println("Error somewhere" ) ; 
                break ; 
                
        }
        
        
       
    }
    void buyStamps(){
        
        try { 

             Thread.sleep(taskTimes[0]);

        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    void mailLetter(){
        
        try { 

                        Thread.sleep(taskTimes[1]);
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void mailPackage(){
         try { 
             scale.acquire();
             System.out.println("Scale in use by postal worker "+id);
             
             Thread.sleep(taskTimes[2]);
             scale.release();

        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}



