import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ServerSocket server;
	private Socket connection;
	
	public Server(){
		super("Instant Messenger");
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						sendMessage(event.getActionCommand());
						userText.setText("");
					}

					private void sendMessage(String actionCommand) {
						// TODO Auto-generated method stub
						try{
							output.writeObject("SERVER - " + actionCommand);
							output.flush();
							showMessage("\nSERVER - " + actionCommand);
						}catch(IOException ioException){
							chatWindow.append("\n Unknown Error!!!");
						}
					}
				}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(500,400);
		setVisible(true);
	}
	
	public void startRunning(){
		try{
			server = new ServerSocket(6789, 100);
			while(true){
				try{
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException){
					showMessage("\n Server Ended Connection!!!");
				}finally{
					closeCrap();
				}
			}
		}catch(IOException ioexception){
			ioexception.printStackTrace();
		}
	}

	private void closeCrap() {
		// TODO Auto-generated method stub
		showMessage("\nClosing connections!!!");
		ableToType(false);
		try{
			input.close();
			output.close();
			connection.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
		
	}

	private void showMessage(final String string) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						chatWindow.append(string);
					}
				}
		);
	}

	private void whileChatting() throws IOException{
		// TODO Auto-generated method stub
		String Message = "You are now connected...";
		showMessage(Message);
		ableToType(true);
		do{
			try{
				Message = (String) input.readObject();
				showMessage("\n" + Message);
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("\n Unknown Error!!!!");
			}
		}while(!Message.equals("CLIENT - END"));
	}

	private void ableToType(final boolean b) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						userText.setEditable(b);
					}
				}
		);
	}

	private void setupStreams() throws IOException{
		// TODO Auto-generated method stub
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Connection is set up!!!!\n");
	}

	private void waitForConnection() throws IOException{
		// TODO Auto-generated method stub
		showMessage(" Waiting for people to connect...");
		connection = server.accept();
		showMessage(" \nNow connected to " + connection.getInetAddress().getHostName());
	}
}
