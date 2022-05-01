import git.tools.client.GitSubprocessClient;
import github.tools.client.GitHubApiClient;
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
		//TODO: replace these next labels with images
		JLabel microsoftImagePlaceholder = new JLabel("Microsoft Image");
		JLabel quImagePlaceholder = new JLabel("QU Image");

		//creating components for input panel
		JLabel enterPathLabel = new JLabel("Enter the directory path of the project you would like to " +
				"turn into a GitHub repo:");
		JLabel enterDescLabel = new JLabel("(Optional) Enter a description for the repo:");
		JLabel usernameLabel = new JLabel("Enter your GitHub username: ");
		JLabel tokenLabel = new JLabel("Make sure your API token is in \'token.txt\'");

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
			String gitInit = gitSubprocessClient.gitInit();

			//.gitignore file creation
			File gitIgnore = new File(pathTextField.getText(),".gitignore");
			FileWriter gitIgnoreFW = null;
			BufferedWriter gitIgnoreBW = null;
			PrintWriter gitIgnorePW = null;
			try{
				gitIgnore.createNewFile();
				gitIgnoreFW = new FileWriter(".gitignore",true);
				gitIgnoreBW = new BufferedWriter(gitIgnoreFW);
				gitIgnorePW = new PrintWriter(gitIgnoreBW);

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
				//TODO: error handling
				System.out.println("error with writing to gitignore");
			} finally {
				try{
					gitIgnorePW.close();
					gitIgnoreBW.close();
					gitIgnoreFW.close();
				} catch (IOException e2){
					//TODO: error handling
					System.out.println("error closing gitignore writers");
				}
			}

			//README.md file creation
			File readme = new File(pathTextField.getText(),"README.md");
			FileWriter readmeFW = null;
			BufferedWriter readmeBW = null;
			PrintWriter readmePW = null;
			String[] splitPath = pathTextField.getText().split("/");
			String projectName = splitPath[splitPath.length-1];
			System.out.println(projectName);
			try{
				readme.createNewFile();
				readmeFW = new FileWriter("README.md",true);
				readmeBW = new BufferedWriter(gitIgnoreFW);
				readmePW = new PrintWriter(gitIgnoreBW);

				readmePW.println(projectName);
				readmePW.flush();

			} catch(IOException e1){
				//TODO: error handling
				System.out.println("error writing to readme");
			} finally {
				try{
					readmePW.close();
					readmeBW.close();
					readmeFW.close();
				} catch (IOException e2){
					//TODO: error handling
					System.out.println("error closing readme writers");
				}
			}

			System.out.println(gitSubprocessClient.gitStatus());

			//initial commit
			String addAll = gitSubprocessClient.gitAddAll();
			System.out.println(addAll);
			String commit = gitSubprocessClient.gitCommit("initial commit");
			System.out.println(commit);

			//GitHub repo created
			File tokenFile = new File("token.txt");
			String token = "";
			try{
				Scanner fileScanner = new Scanner(tokenFile);
				token = fileScanner.nextLine();
				fileScanner.close();
			} catch (IOException e3){
				//TODO: error handling
				System.out.println("error reading token");
			}

			System.out.println("token: " + token);

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
			System.out.println("gitRemoteAdd: " + gitRemoteAdd);

			System.out.println(gitSubprocessClient.gitStatus());

			//initial commit pushed to GitHub
			String push = gitSubprocessClient.gitPush("master");
			System.out.println("push: " + push);

			//give user url to new repo
			link.setText(url);
			link.setVisible(true);
			outputLabel.setVisible(true);
		}
	}
}
