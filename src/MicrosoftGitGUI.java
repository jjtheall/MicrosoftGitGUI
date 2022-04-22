import javax.swing.JFrame;

public class MicrosoftGitGUI extends JFrame {

	public MicrosoftGitGUI() {
		// we can fix this later
		super("Microsoft GUI");
		
		// we can change the size later too
		this.setSize(1000,1000);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
    public static void main(String[] args) {
    	new MicrosoftGitGUI();
    }
}
