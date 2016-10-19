import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String Message;
	private String ServerIP;
	private Socket connection;
	
	public Client(String host){
		super("Instant Messenger - Client Side");
		ServerIP = host;
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
							output.writeObject("CLIENT - " + actionCommand);
							output.flush();
							showMessage("\nCLIENT - " + actionCommand);
						}catch(IOException ioException){
							chatWindow.append("\n Unknown Error!!!");
						}
					}
				}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow),BorderLayout.CENTER);
		setSize(500,400);
		setVisible(true);
	}
	
	public void startRunning(){
		try{
			while(true){
				try{
					connectToServer();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException){
					showMessage("\n Client Ended Connection!!!");
				}finally{
					closeCrap();
				}
			}
		}catch(IOException ioexception){
			ioexception.printStackTrace();
		}
	}

	private void connectToServer() throws IOException{
		// TODO Auto-generated method stub
		showMessage(" Attempting for connection...");
		connection = new Socket(InetAddress.getByName(ServerIP), 6789);
		showMessage(" \nNow connected to " + connection.getInetAddress().getHostName());
	}
	
	private void closeCrap() {
		// TODO Auto-generated method stub
		showMessage("\nClosing connections!!!");
		ableToType(false);
		try{
			output.close();
			input.close();
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
		}while(!Message.equals("SERVER - END"));
	}
}
