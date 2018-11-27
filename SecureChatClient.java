import java.io.*;
import java.math.BigInteger;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/** Primitive chat client. 
 * This client connects to a server so that messages can be typed and forwarded
 * to all other clients.  Try it out in conjunction with ImprovedChatServer.java.
 * You will need to modify / update this program to incorporate the secure 
 * elements as specified in the Assignment description.  Note that the PORT used 
 * below is not the one required in the assignment -- for your SecureChatClient 
 * be sure to change the port that so that it matches the port specified for the 
 * secure  server.
 * @author Sherif Khattab
 * Adapted from Dr. John Ramirez's CS 1501 Assignment 4
 */
@SuppressWarnings("serial")
public class SecureChatClient extends JFrame implements Runnable, ActionListener {

    public static final int PORT = 8765;

    ObjectInputStream myReader;
    ObjectOutputStream myWriter;
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName;
	Socket connection;
	SymCipher cypher;

    public SecureChatClient ()
    {
        try {

        myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
        serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
        InetAddress addr =
                InetAddress.getByName(serverName);
        connection = new Socket(addr, PORT);   // Connect to server with new
                                               // Socket
        myWriter =	new ObjectOutputStream(connection.getOutputStream());
		myWriter.flush();
		myReader = new ObjectInputStream(connection.getInputStream());
		BigInteger E = (BigInteger) myReader.readObject();
		System.out.println("E recieved: " + E);
		BigInteger N = (BigInteger) myReader.readObject();
		System.out.println("N recieved: " + N);
		String cyphertype = (String) myReader.readObject();
		System.out.println("Cypher type: " + cyphertype);
		if(cyphertype.equals("Add")) cypher = new Add128();
		else cypher = new Substitute();
		BigInteger key = new BigInteger(1,cypher.getKey());
		System.out.println("Key Sent: " + key);
		myWriter.writeObject(key.modPow(E, N));myWriter.flush();
		myWriter.writeObject(cypher.encode(myName));myWriter.flush();        
		// Send name to Server.  Server will need
                                    // this to announce sign-on and sign-off
                                    // of clients

        this.setTitle(myName);      // Set title to identify chatter

        Box b = Box.createHorizontalBox();  // Set up graphical environment for
        outputArea = new JTextArea(8, 30);  // user
        outputArea.setEditable(false);
        b.add(new JScrollPane(outputArea));

        outputArea.append("Welcome to the Chat Group, " + myName + "\n");

        inputField = new JTextField("");  // This is where user will type input
        inputField.addActionListener(this);

        prompt = new JLabel("Type your messages below:");
        Container c = getContentPane();

        c.add(b, BorderLayout.NORTH);
        c.add(prompt, BorderLayout.CENTER);
        c.add(inputField, BorderLayout.SOUTH);

        Thread outputThread = new Thread(this);  // Thread is to receive strings
        outputThread.start();                    // from Server

		addWindowListener(
                new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    { try {
						myWriter.writeObject(cypher.encode("CLIENT CLOSING"));myWriter.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
                      System.exit(0);
                     }
                }
            );

        setSize(500, 200);
        setVisible(true);

        }
        catch (Exception e)
        {
            System.out.println("Problem starting client!");
        }
    }

    public void run()
    {
        while (true)
        {
             try {
                String currMsg = cypher.decode((byte [])myReader.readObject());
			    outputArea.append(currMsg+"\n");
             }
             catch (Exception e)
             {
                System.out.println(e +  ", closing client!");
                break;
             }
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e)
    {
        String currMsg = e.getActionCommand();      // Get input value
        inputField.setText("");
        try {
			myWriter.writeObject(cypher.encode(myName + ":" + currMsg));
		} catch (IOException e1) {
			e1.printStackTrace();
		}   // Add name and send it
    }                                               // to Server

    public static void main(String [] args)
    {
         SecureChatClient JR = new SecureChatClient();
         JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}