/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vineeth
 */
//package cs4348.project3.vineethsoma;


import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.*;
import java.io.* ; 
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.w3c.dom.Document;


public class WebBrowser extends JFrame {
    int defaultPort = 80 ; 
   // String host = null ; 
    //String path = null ; 
    
    private JTextField locationTextField ; 
    private JEditorPane displayEditorPane ; 
    private JPanel buttonPanel = new JPanel();
    
    Socket conn = null ; 
    PrintWriter out = null ; 
    BufferedReader in = null ; 
    WebBrowser(String url ){
        
        super("Web Browser") ;
        setSize(640,480)  ;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        locationTextField = new JTextField(35);
        locationTextField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionGo();
                }
            }
        });
        buttonPanel.add(locationTextField);
       
        JButton goButton = new JButton("GO");
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionGo();
            }
        });
        buttonPanel.add(goButton);

       connect(url) ; 
       getHTTP(url) ; 
       //Document doc = new Document() ; 
       
        
       displayEditorPane = new JEditorPane() ; 
       displayEditorPane.setContentType("text/html");
       displayEditorPane.setEditable(false);
       
       getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(displayEditorPane),
                BorderLayout.CENTER);
        try {
            // displayEditorPane.setText(this.parse());
            System.out.println(parse()) ; 
            displayEditorPane.setPage(url) ;
        } catch (IOException ex) {
            Logger.getLogger(WebBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        
        
    }
    private void actionGo(){
   //     connect(locationTextField.getText()) ; 
     //  getHTTP(locationTextField.getText()) ;
    //   System.out.println(parse()) ; 
        try { 
            displayEditorPane.setPage(locationTextField.getText()) ;
        } catch (IOException ex) {
            Logger.getLogger(WebBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public String getHost(String url){
        
        String host = null ; 
        
        if(url.contains("http://")){
            
            host = url.split("//", 2)[1] ; 
            host = host.split("/", 2)[0] ; 
            //System.out.println(host);
        }
        else{
            
            host = url.split("/", 2)[0] ; 
        }
        
        return host ; 
        
        
    }
    
    public String getPath(String url){
        String path ; 
        if(url.contains("http://")){
            
            path = url.split("//", 2)[1] ; 
            path = path.split("/", 2)[1] ; 
            path =  "/" + path ; 
            
        }
        else{
            
            path = url.split("/", 2)[0] ; 
            path = "/" + path ;  
        }
        return path ;
             
    }
    
    public int getPort(String url){
        int port = defaultPort; 
        
        if(this.getDelimiter(url).equals("/") ){
            
            port = defaultPort ; 
        }
        else if(this.getDelimiter(url).equals(":") ){
          //  String tmp = path.replaceAll("\\D+","");
            String tmp = url.split(":")[1] ;
            if(tmp.contains(":")){
                tmp = url.split(":")[1] ;
                port = Integer.parseInt(tmp) ;
            }
            else
                port = defaultPort ; 
            
        }
        
        return port ; 
        
    }
    
    public String getDelimiter( String url){
        
        String delim ; 
        
        if( url.contains(":")){
            
            delim =":" ; 
         }
        
        else {
            delim ="/" ; 
        }
        
        return delim ; 
        
    }
    public void connect(String url){
        // connecting to server 
        String host = getHost(url) ; 
        int port = getPort(url) ; 
        
        try{
            conn = new Socket( host, port) ; 
            out = new PrintWriter(conn.getOutputStream(), true ) ; 
            in = new BufferedReader( new InputStreamReader( conn.getInputStream()) ) ; 
            
        } catch (IOException ex) {
            Logger.getLogger(WebBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    public String parse() {
            
            String parsedCode = ""  ; 
        
        try {
            String line;
            line = in.readLine();
            if(line.contains("OK")){                                                //Check to ensure request was fulfilled
            //    while(!line.contains("<html>")){                                  //ignore everything till this point
              //      line = in.readLine();
             //   }
                while(line != null){                                              
                    System.out.println(line);  
                    parsedCode += line ; 
                 //   displayEditorPane.setText(line);// 
                    line = in.readLine();
                    
                }
            } 
            else{
                System.out.println("Error:" + line.replaceAll("HTTP/1.1", ""));
                System.exit(1);
            }
        } catch (IOException ex) {
            Logger.getLogger(WebBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        
        return parsedCode ; 
    }
    
    public void getHTTP(String url){
        
        String path = getPath(url) ; 
        String host = getHost(url) ; 
        
        out.print("GET "+ path + " HTTP/1.1\r\n"+ "Host: " + host +"\r\n" + "Connection: close\r\n\r\n" );
        out.flush() ;
        
        
    }
    
    public static void main(String[] args){
        
    String url = "";
    
    if (args.length == 1) {
      url = args[0];
    }
    else {
      url = "http://www.december.com/html/demo/hello.html";
    }

    WebBrowser w = new WebBrowser(url) ; 
    w.show();

       
    }
    
    
    
    
}
