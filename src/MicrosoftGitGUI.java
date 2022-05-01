import git.tools.client.GitSubprocessClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MicrosoftGitGUI extends JFrame {

	JPanel titlePanel;
	JPanel inputPanel;
	JPanel outputPanel;
	JTextField pathTextField;
	JTextField descriptionTextField;

	public MicrosoftGitGUI() {
		// we can fix this later
		super("Microsoft GUI");
		
		// we can change the size later too
		this.setSize(1000,1000);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		//instantiation of panels
		titlePanel = new JPanel();
		titlePanel.setSize(new Dimension(1000,100));

		inputPanel = new JPanel();
		inputPanel.setSize(new Dimension(1000,100));

		outputPanel = new JPanel();
		outputPanel.setSize(new Dimension(1000,100));

		//creating components for title panel
		JLabel prototypeLabel = new JLabel("This application is a prototype. Things will be ugly " +
				"and may not work as intended.");
		//replace these next labels with images
		JLabel microsoftImagePlaceholder = new JLabel("Microsoft Image");
		JLabel quImagePlaceholder = new JLabel("QU Image");

		//creating components for input panel
		JLabel enterPathLabel = new JLabel("Enter the directory path of the project you would like to " +
				"turn into a GitHub repo:");
		JLabel enterDescLabel = new JLabel("(Optional) Enter a description for the repo:");

		pathTextField = new JTextField();
		pathTextField.setPreferredSize(new Dimension(400,30));
		descriptionTextField = new JTextField();
		descriptionTextField.setPreferredSize(new Dimension(400,30));

		ButtonGroup privacyButtons = new ButtonGroup();
		JRadioButton privateButton = new JRadioButton("Private");
		JRadioButton publicButton = new JRadioButton("Public");
		publicButton.setSelected(true);
		privacyButtons.add(privateButton);
		privacyButtons.add(publicButton);

		JButton createRepoButton = new JButton("Create Repo");

		createRepoButton.addActionListener(new CreateRepoListener());

		//creating components for output panel
		JLabel outputLabel = new JLabel("Here is the link to your brand new repo: ");
		JLabel link = new JLabel("Click here");
		outputLabel.setVisible(false);
		link.setVisible(false);

		//creating component for error handling, this will be added outside of a panel
		JLabel errorLabel = new JLabel("Something went wrong...");
		errorLabel.setVisible(false);

		//adding components for output panel
		outputPanel.add(outputLabel);
		outputPanel.add(link);

		//adding components for title panel
		titlePanel.add(microsoftImagePlaceholder);
		titlePanel.add(prototypeLabel);
		titlePanel.add(quImagePlaceholder);

		//adding components for input panel
		inputPanel.add(enterPathLabel);
		inputPanel.add(pathTextField);
		inputPanel.add(enterDescLabel);
		inputPanel.add(descriptionTextField);
		inputPanel.add(privateButton);
		inputPanel.add(publicButton);
		inputPanel.add(createRepoButton);

		//adding panels to frame
		this.add(titlePanel);
		this.add(inputPanel);
		this.add(outputPanel);

		this.add(errorLabel);

		this.setVisible(true);
	}
    public static void main(String[] args) {
    	new MicrosoftGitGUI();
    }

    private class CreateRepoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//git repo created
			
			//.gitignore file creation

			//README.md file creation

			//initial commit

			//GitHub repo created

			//git repo remote set to GitHub repo

			//initial commit pushed to GitHub

			//give user url to new repo
		}
	}
}
