import javax.swing.JFrame;

public class Runner {
	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(600,600);
		UI ui = new UI();
		frame.addMouseListener(ui);
		frame.add(ui);
		
	}
}
