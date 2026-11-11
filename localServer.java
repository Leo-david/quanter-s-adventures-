
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.FileReader;

public class localServer {
    public static void main(String[] args) {
        int port = 5555;
        
       try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Waiting for client on port " + port);
            
            Socket clientSocket = serverSocket.accept(); // waits here until client connects
            System.out.println("Client connected!");
            int t = 0;
            // Streams to send and receive text
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            while(t < 20){
            String messageFromClient = in.readLine(); // read 1 line
            System.out.println("Client says: " + messageFromClient);
                 
                 // sending file
                 if(t == 22){
                      out.println("Readd");
                      String path = "goo.txt";
                      FileReader fr = new FileReader(path);
                      BufferedReader br = new BufferedReader(fr);
                      String dat;
                      while((dat = br.readLine()) != null){
                           System.out.println(dat);
                           out.println(dat);
                      }
                      out.println("stop");
                      br.close();
                 }
                 // end of sending files
                 
                 t++;
                 out.println("Hell0" + t); // send reply
            }
            clientSocket.close();
            System.out.println("Connection closed");
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}