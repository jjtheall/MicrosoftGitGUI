import git.tools.client.GitSubprocessClient;
import github.tools.client.GitHubApiClient;
import github.tools.client.RequestFailedException;
import github.tools.client.RequestParams;
import github.tools.responseObjects.CreateRepoResponse;
import github.tools.responseObjects.GetRepoInfoResponse;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class MicrosoftGitGUI extends JFrame {

	JPanel titlePanel;
	JPanel inputPanel;
	JPanel outputPanel;
	JTextField usernameTextField;
	JTextField pathTextField;
	JTextField descriptionTextField;
	JRadioButton privateButton;
	JLabel link;
	JLabel outputLabel;
	JLabel errorLabel;

	public MicrosoftGitGUI() {
		// we can fix this later
		super("Microsoft GUI");
		
		// we can change the size later too
		this.setSize(1000,1000);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		//instantiation of panels
		ImagePanel imagePanel = new ImagePanel();

		titlePanel = new JPanel();
		titlePanel.setSize(new Dimension(1000,100));

		inputPanel = new JPanel();
		inputPanel.setSize(new Dimension(1000,100));

		outputPanel = new JPanel();
		outputPanel.setSize(new Dimension(1000,100));

		//creating components for title panel
		JLabel prototypeLabel = new JLabel("This application is a prototype. Things will be ugly " +
				"and may not work as intended.");

		//creating components for input panel
		JLabel enterPathLabel = new JLabel("Enter the directory path of the project you would like to " +
				"turn into a GitHub repo:");
		JLabel enterDescLabel = new JLabel("(Optional) Enter a description for the repo:");
		JLabel usernameLabel = new JLabel("Enter your GitHub username: ");
		JLabel tokenLabel = new JLabel("Make sure your auth token is in \'token.txt\' file in root folder");

		usernameTextField = new JTextField();
		usernameTextField.setPreferredSize(new Dimension(400,30));
		pathTextField = new JTextField();
		pathTextField.setPreferredSize(new Dimension(400,30));
		descriptionTextField = new JTextField();
		descriptionTextField.setPreferredSize(new Dimension(400,30));

		ButtonGroup privacyButtons = new ButtonGroup();
		privateButton = new JRadioButton("Private");
		JRadioButton publicButton = new JRadioButton("Public");
		publicButton.setSelected(true);
		privacyButtons.add(privateButton);
		privacyButtons.add(publicButton);

		JButton createRepoButton = new JButton("Create Repo");

		createRepoButton.addActionListener(new CreateRepoListener());

		//creating components for output panel
		outputLabel = new JLabel("Here is the link to your brand new repo: ");
		link = new JLabel("Click here");
		outputLabel.setVisible(false);
		link.setVisible(false);

		//creating component for error handling, this will be added outside of a panel
		errorLabel = new JLabel("Something went wrong...");
		errorLabel.setForeground(Color.red);
		errorLabel.setVisible(false);

		//adding components for output panel
		outputPanel.add(outputLabel);
		outputPanel.add(link);

		//adding components for title panel
		titlePanel.add(prototypeLabel);

		//adding components for input panel
		inputPanel.add(usernameLabel);
		inputPanel.add(usernameTextField);
		inputPanel.add(tokenLabel);
		inputPanel.add(enterPathLabel);
		inputPanel.add(pathTextField);
		inputPanel.add(enterDescLabel);
		inputPanel.add(descriptionTextField);
		inputPanel.add(privateButton);
		inputPanel.add(publicButton);
		inputPanel.add(createRepoButton);

		//adding panels to frame
		this.add(imagePanel);
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
			GitSubprocessClient gitSubprocessClient = new GitSubprocessClient(pathTextField.getText());
			try{
				String gitInit = gitSubprocessClient.gitInit();
			} catch (RuntimeException runtimeException){
				errorLabel.setVisible(true);
			}

			//.gitignore file creation
			File gitIgnore = new File(pathTextField.getText(),".gitignore");
			PrintWriter gitIgnorePW = null;
			try{
				gitIgnore.createNewFile();
				gitIgnorePW = new PrintWriter(gitIgnore);

				gitIgnorePW.println(".project");
				gitIgnorePW.println(".classpath");
				gitIgnorePW.println("*.class");
				gitIgnorePW.println("bin/");
				gitIgnorePW.println(".settings/");
				gitIgnorePW.println(".idea/");
				gitIgnorePW.println("*.iml");
				gitIgnorePW.println(".DS_Store");
				gitIgnorePW.println("out/");
				gitIgnorePW.flush();

			} catch(IOException e1){
				errorLabel.setVisible(true);
			} finally {
				gitIgnorePW.close();
			}

			//README.md file creation
			File readme = new File(pathTextField.getText(),"README.md");
			PrintWriter readmePW = null;
			String[] splitPath = pathTextField.getText().split("/");
			String projectName = splitPath[splitPath.length-1];
			try{
				readme.createNewFile();
				readmePW = new PrintWriter(readme);

				readmePW.println(projectName);
				readmePW.flush();

			} catch(IOException e1){
				errorLabel.setVisible(true);
			} finally {
				readmePW.close();
			}

			//initial commit
			String addAll = gitSubprocessClient.gitAddAll();
			String commitMessage = "initial commit";
			String commit = gitSubprocessClient.gitCommit(commitMessage);

			//GitHub repo created
			File tokenFile = new File("token.txt");
			String token = "";
			try{
				Scanner fileScanner = new Scanner(tokenFile);
				token = fileScanner.nextLine();
				fileScanner.close();
			} catch (IOException e3){
				errorLabel.setVisible(true);
			}

			GitHubApiClient gitHubApiClient = new GitHubApiClient(usernameTextField.getText(),token);
			RequestParams createRepoRequestParams = new RequestParams();
			createRepoRequestParams.addParam("name",projectName);
			if(!descriptionTextField.getText().equals("")){
				createRepoRequestParams.addParam("description",descriptionTextField.getText());
			}
			if(privateButton.isSelected()){
				createRepoRequestParams.addParam("private",true);
			} else {
				createRepoRequestParams.addParam("private",false);
			}
			CreateRepoResponse createRepoResponse = gitHubApiClient.createRepo(createRepoRequestParams);

			//git repo remote set to GitHub repo
			GetRepoInfoResponse repoInfo = gitHubApiClient.getRepoInfo(usernameTextField.getText(),projectName);
			String url = repoInfo.getUrl();
			String gitRemoteAdd = gitSubprocessClient.gitRemoteAdd("origin",url + ".git");

			//initial commit pushed to GitHub
			String push = gitSubprocessClient.gitPush("master");

			//give user url to new repo
			link.setText(url);
			link.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try{
						Desktop.getDesktop().browse(new URI(url));
					} catch (IOException | URISyntaxException e1){
						e1.printStackTrace();
					}
				}
			});
			link.setForeground(Color.blue);
			link.setVisible(true);
			outputLabel.setVisible(true);
		}
	}
}
