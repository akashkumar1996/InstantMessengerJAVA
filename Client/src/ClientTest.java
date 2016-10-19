import javax.swing.JFrame;

public class ClientTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client program = new Client("127.0.0.1");
		program.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		program.startRunning();
	}

}
