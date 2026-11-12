

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

// This server uses UDP, not TCP
// UDP sends one line = one packet, no permanent connection
public class localServer {
    public static void main(String[] args) {
        // Port 5555 - this is the port the server will listen on
        int port = 5555;

        try {
            // Create a UDP socket that listens on port 5555
            DatagramSocket socket = new DatagramSocket(port);
            System.out.println("Server started. Waiting for client on localhost port " + port);

            // Buffer to hold one incoming packet. 2048 bytes is enough for one short line.
            byte[] receiveBuffer = new byte[2048];

            // Keep the server running forever, waiting for packets
            while (true) {
                // Create an empty packet to receive data into
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                // Wait here until one packet arrives. This blocks until a packet comes.
                socket.receive(receivePacket);

                // Convert the bytes in the packet to a String (a line of text)
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());

                // Get the address and port of the client who sent this packet
                // In UDP we need this to reply, because there is no permanent connection
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                System.out.println("Received packet: " + message);

                // Check if the line starts with "Hello" - this is our real client
                if (message.startsWith("Hello")) {
                    System.out.println("This is a real client message, it starts with Hello");

                    // Build the reply, for example "Hell01" for "Hello Server!0"
                    // We take the number at the end of the message and add 1
                    // Example: message = "Hello Server!0" -> reply = "Hell01"
                    String numberPart = message.substring(message.lastIndexOf("!") + 1);
                    int t = Integer.parseInt(numberPart.trim());
                    String reply = "Hell0" + (t + 1);

                    // Convert the reply String to bytes
                    byte[] replyData = reply.getBytes();

                    // Create a packet to send back to the same client address and port
                    DatagramPacket replyPacket = new DatagramPacket(replyData, replyData.length, clientAddress, clientPort);

                    // Send the reply packet
                    socket.send(replyPacket);
                    System.out.println("Sent reply packet: " + reply);

                // Check if the line starts with "stop" - this means client wants to stop
                } else if (message.startsWith("stop")) {
                    System.out.println("Client said stop, sending stop confirmation");
                    byte[] replyData = "stop".getBytes();
                    DatagramPacket replyPacket = new DatagramPacket(replyData, replyData.length, clientAddress, clientPort);
                    socket.send(replyPacket);

                // If the line does not start with Hello or stop, ignore it
                // This is how we ignore Minecraft ping or other junk
                } else {
                    System.out.println("Ignoring packet, it does not start with Hello or stop: " + message);
                    // Do not reply, just go back and wait for the next packet
                }

                // Clear the buffer for the next packet, so old data does not stay
                receiveBuffer = new byte[2048];
            }

        } catch (Exception e) {
            // If anything goes wrong, print the error
            e.printStackTrace();
        }
    }
}