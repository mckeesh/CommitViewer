import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class ConflictViewerUI extends JFrame {

	JTextArea textAreaA;
	JTextArea textAreaB;
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
		setTitle("A vs. B vs. Solved");
		setSize(x, y);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		textAreaA = new JTextArea();
		textAreaA.setEditable(false);
		JScrollPane scrollPanelA = new JScrollPane(textAreaA);
		
		textAreaB = new JTextArea();
		textAreaB.setEditable(false);
		JScrollPane scrollPanelB = new JScrollPane(textAreaB);
		
		textAreaSolved = new JTextArea();
		textAreaSolved.setEditable(false);
		JScrollPane scrollPanelSolved = new JScrollPane(textAreaSolved);
		
		nextFileButton = new JButton("Next File");
		backFileButton = new JButton("Back File");
		randomFileButton = new JButton("Random File");
		nextFileButton.setMaximumSize(new Dimension(300, 150));
		backFileButton.setMaximumSize(new Dimension(300, 150));
		randomFileButton.setMaximumSize(new Dimension(300, 150));
		
		nextCommitButton = new JButton("Next Commit");
		backCommitButton = new JButton("Back Commit");
		randomCommitButton = new JButton("Random Commit");
		nextCommitButton.setMaximumSize(new Dimension(300, 150));
		backCommitButton.setMaximumSize(new Dimension(300, 150));
		randomCommitButton.setMaximumSize(new Dimension(300, 150));
		
		addActionListenersToButtons();
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		Box fileButtonBox = Box.createHorizontalBox();
		fileButtonBox.add(nextFileButton);
		fileButtonBox.add(backFileButton);
		fileButtonBox.add(randomFileButton);
		
		Box commitButtonBox = Box.createHorizontalBox();
		commitButtonBox.add(nextCommitButton);
		commitButtonBox.add(backCommitButton);
		commitButtonBox.add(randomCommitButton);
		
		Box pageBox = Box.createHorizontalBox();
		pageBox.add(scrollPanelA);
		pageBox.add(scrollPanelB);
		pageBox.add(scrollPanelSolved);
		
		this.add(fileButtonBox);
		this.add(commitButtonBox);
		this.add(pageBox);
		
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
    
    public void setTextA(String str){
    	textAreaA.setText(str);
    }
    
    public void setTextB(String str){
    	textAreaB.setText(str);
    }
    
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
    		setTextA("No Merge Conflicts for commit");
		    setTextB("No Merge Conflicts for commit");
		    setTextSolved("No Merge Conflicts for commit");
    	}
    	refreshApp();
    }
    
    public void setupData(){
    	commitToMergeMap = dataRetriever.walkRepo();
    	commit = (RevCommit)(commitToMergeMap.keySet().toArray()[0]);
    	MetaMerge firstMerge = commitToMergeMap.get(commitToMergeMap.keySet().toArray()[0]);
    	fileName = (String)(firstMerge.fileToA.keySet().toArray()[0]);
		String firstFileA = firstMerge.fileToA.get(fileName);
		String firstFileB = firstMerge.fileToB.get(fileName);
		String firstFileCombined= firstMerge.fileToCompletedMerge.get(fileName);
		setTextA(Formatter.formatDiff(firstFileA));
	    setTextB(Formatter.formatDiff(firstFileB));
	    setTextSolved(Formatter.formatDiff(firstFileCombined));
    }
    
    public void refreshApp(){
    	MetaMerge merge = commitToMergeMap.get(commit);
		String fileA = merge.fileToA.get(fileName);
		String fileB = merge.fileToB.get(fileName);
		String fileCombined= merge.fileToCompletedMerge.get(fileName);
		if(fileA != null && fileB != null && fileCombined != null){
			setTextA(Formatter.formatDiff(fileA));
		    setTextB(Formatter.formatDiff(fileB));
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
