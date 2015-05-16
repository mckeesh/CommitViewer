import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class ConflictViewerUI extends JFrame {

	JTextArea textAreaUnresolved;
	JTextArea textAreaSolved;
	
	JButton nextFileButton;
	JButton backFileButton;
	JButton randomFileButton;
	JButton nextCommitButton;
	JButton backCommitButton;
	JButton randomCommitButton;
	
	public static RevCommit commit;
	public static String fileName;
	private Repository repository;
	private UIDataRetriever dataRetriever;
	private Map<RevCommit, MetaMerge> commitToMergeMap;
	
	
    public ConflictViewerUI(int x, int y, Repository repo) {
    	repository = repo;
    	dataRetriever = new UIDataRetriever(repository);
    	
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	
		setTitle("A + B vs. Solved");
		setSize(screenSize.width-200, screenSize. height);
		setLocation(new Point(0,0));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JFrame buttonFrame = new JFrame();
		
		textAreaUnresolved = new JTextArea();
		textAreaUnresolved.setEditable(false);
		JScrollPane scrollPanelA = new JScrollPane(textAreaUnresolved);
		
//		textAreaB = new JTextArea();
//		textAreaB.setEditable(false);
//		JScrollPane scrollPanelB = new JScrollPane(textAreaB);
		
		textAreaSolved = new JTextArea();
		textAreaSolved.setEditable(false);
		JScrollPane scrollPanelSolved = new JScrollPane(textAreaSolved);
		
		JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanelA, scrollPanelSolved);
		splitPanel.setOneTouchExpandable(true);
		splitPanel.setDividerLocation(150);
		
		nextFileButton = new JButton("Next File");
		backFileButton = new JButton("Back File");
		randomFileButton = new JButton("Random File");
		nextFileButton.setMaximumSize(new Dimension(200, 150));
		backFileButton.setMaximumSize(new Dimension(200, 150));
		randomFileButton.setMaximumSize(new Dimension(200, 150));
		
		nextCommitButton = new JButton("Next Commit");
		backCommitButton = new JButton("Back Commit");
		randomCommitButton = new JButton("Random Commit");
		nextCommitButton.setMaximumSize(new Dimension(200, 150));
		backCommitButton.setMaximumSize(new Dimension(200, 150));
		randomCommitButton.setMaximumSize(new Dimension(200, 150));
		
		addActionListenersToButtons();
		
		buttonFrame.getContentPane().setLayout(new BoxLayout(buttonFrame.getContentPane(), BoxLayout.PAGE_AXIS));
				
		Box fileButtonBox = Box.createVerticalBox();
		fileButtonBox.add(nextFileButton);
		fileButtonBox.add(backFileButton);
		fileButtonBox.add(randomFileButton);
		
		Box commitButtonBox = Box.createVerticalBox();
		commitButtonBox.add(nextCommitButton);
		commitButtonBox.add(backCommitButton);
		commitButtonBox.add(randomCommitButton);
		
		buttonFrame.add(fileButtonBox);
		buttonFrame.add(commitButtonBox);
		
		this.add(splitPanel);
		
		buttonFrame.setVisible(true);
		buttonFrame.setSize(200, screenSize.height);
		buttonFrame.setLocation(new Point(screenSize.width-200, 0));
		buttonFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
//		Box pageBox = Box.createHorizontalBox();
//		pageBox.add(scrollPanelA);
////		pageBox.add(scrollPanelB);
//		pageBox.add(scrollPanelSolved);
//		JPanel buttonPanel = new JPanel();
//		buttonPanel.add(fileButtonBox);
//		buttonPanel.add(commitButtonBox);
//		buttonsBox.add(buttonPanel);
//		
//		buttonsBox.add(splitPanel);
//		this.add(buttonsBox);
//		this.add(fileButtonBox);
//		this.add(commitButtonBox);
//		this.add(splitPanel);
		
		setupData();
    }
    
    public void addActionListenersToButtons() {
    	nextFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
            	List<String> currentFilesForCommit = getCurrentFilesForCommit();
            	int index = currentFilesForCommit.indexOf(fileName);
            	if(index < currentFilesForCommit.size()-1){
            		changeFile(currentFilesForCommit.get(index+1));
            	}
            }
        });
    	
    	backFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	List<String> currentFilesForCommit = getCurrentFilesForCommit();
            	int index = currentFilesForCommit.indexOf(fileName);
            	if(index > 0){
            		changeFile(currentFilesForCommit.get(index-1));
            	}
            }
        });
    	
    	randomFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	List<String> currentFilesForCommit = getCurrentFilesForCommit();
            	int size = currentFilesForCommit.size();
            	if(size > 0){
            		int index = (int)(Math.random()*currentFilesForCommit.size());
            		changeFile(currentFilesForCommit.get(index));
            	}
            }
        });
    	
    	nextCommitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
            	List<RevCommit> currentCommits = getCurrentCommitList();
            	int indexOfCurrentCommit = currentCommits.indexOf(commit);
            	if(indexOfCurrentCommit < currentCommits.size()-1){
            		changeCommit(currentCommits.get(indexOfCurrentCommit+1));
            	}
            }
        });
    	
    	backCommitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	List<RevCommit> currentCommits = getCurrentCommitList();
            	int indexOfCurrentCommit = currentCommits.indexOf(commit);
            	if(indexOfCurrentCommit > 0){
            		changeCommit(currentCommits.get(indexOfCurrentCommit-1));
            	}
            }
        });
    	
    	randomCommitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	List<RevCommit> currentCommits = getCurrentCommitList();
            	int size = currentCommits.size();
            	if(size > 0){
            		int index = (int)(Math.random()*currentCommits.size());
            		changeCommit(currentCommits.get(index));
            	}
            }
        });
    }
    
    public void setTextUnresolved(String str){
    	textAreaUnresolved.setText(str);
    }
    
//    public void setTextB(String str){
//    	textAreaB.setText(str);
//    }
    
    public void setTextSolved(String str){
    	
    	textAreaSolved.setText(str);
    }
    
    public void changeFile(String fileNamee){
    	fileName = fileNamee;
    	refreshApp();
    }
    
    public void changeCommit(RevCommit com){
    	commit = com;
    	List<String> files = getCurrentFilesForCommit();
    	if(files.size() > 0){
    		fileName = files.get(0);
    	} else {
    		setTextUnresolved("No Merge Conflicts for commit");
//		    setTextB("No Merge Conflicts for commit");
		    setTextSolved("No Merge Conflicts for commit");
    	}
    	refreshApp();
    }
    
    public void setupData(){
    	commitToMergeMap = dataRetriever.walkRepo();
    	commit = (RevCommit)(commitToMergeMap.keySet().toArray()[0]);
    	MetaMerge firstMerge = commitToMergeMap.get(commitToMergeMap.keySet().toArray()[0]);
    	fileName = (String)(firstMerge.fileToA.keySet().toArray()[0]);
		String comboFile = firstMerge.fileToUnresolved.get(fileName);
		String firstFileSolved= firstMerge.fileToCompletedMerge.get(fileName);
		if(comboFile != null && firstFileSolved != null){
			setTextUnresolved(Formatter.formatDiff(comboFile));
			setTextSolved(Formatter.formatDiff(firstFileSolved));
		}
    }
    
    public void refreshApp(){
    	MetaMerge merge = commitToMergeMap.get(commit);
    	String comboFile = merge.fileToUnresolved.get(fileName);
		String fileCombined= merge.fileToCompletedMerge.get(fileName);
		if(comboFile != null && fileCombined != null){
			setTextUnresolved(Formatter.formatDiff(comboFile));
		    setTextSolved(Formatter.formatDiff(fileCombined));
		}
    }
    
    private List<RevCommit> getCurrentCommitList(){
    	return new ArrayList<RevCommit>(commitToMergeMap.keySet());
    }
    
    private List<String> getCurrentFilesForCommit(){
    	MetaMerge merge = commitToMergeMap.get(commit);
    	return new ArrayList<String>(merge.fileToA.keySet());
    }
}
