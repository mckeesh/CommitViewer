import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConflictViewerUI extends JFrame {

	JTextArea textAreaA;
	JTextArea textAreaB;
	JTextArea textAreaSolved;
	
	
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
		
		this.setLayout(new GridLayout(1,3));
		this.add(scrollPanelA);
		this.add(scrollPanelB);
		this.add(scrollPanelSolved);
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
