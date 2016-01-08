package projectEDR;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class MyFrame extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MyFrame(){
		super("Choisissez le fichier de données");
		buildInterface();
		
	}
	public void buildInterface(){
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		
		Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)tailleEcran.getWidth()/3,(int)tailleEcran.getHeight()/5);
		this.setSize(tailleEcran);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
		
}
