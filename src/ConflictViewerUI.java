import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConflictViewerUI extends JFrame {

	JTextArea textAreaA;
	JTextArea textAreaB;
	JTextArea textAreaSolved;
	
	JButton nextButton;
	JButton backButton;
	JButton randomButton;
	
	int commitNumber = 0;
	
    public ConflictViewerUI(int x, int y) {
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
		
		nextButton = new JButton("Next");
		backButton = new JButton("Back");
		randomButton = new JButton("Random");
		nextButton.setMaximumSize(new Dimension(300, 150));
		backButton.setMaximumSize(new Dimension(300, 150));
		randomButton.setMaximumSize(new Dimension(300, 150));
		addActionListenersToButtons();
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(nextButton);
		buttonBox.add(backButton);
		buttonBox.add(randomButton);
		
		Box pageBox = Box.createHorizontalBox();
		pageBox.add(scrollPanelA);
		pageBox.add(scrollPanelB);
		pageBox.add(scrollPanelSolved);
		
		this.add(buttonBox);
		this.add(pageBox);
    }
    
    public void addActionListenersToButtons() {
    	nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                System.out.println();
            }
        });
    	
    	backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                System.out.println("You clicked the button");
            }
        });
    	
    	randomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                System.out.println("You clicked the button");
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
    
}
