package game.view;
import game.control.Game;
import game.logics.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * ShopPanel is a shopping panel for one specified item. The panel holds
 * the picture of the item, the information text for the item and a button
 * that allows the buying of the item.
 * 
 * @implements ActionListener
 * @extends JPanel
 * 
 * @author Merituuli Melkko
 * */
public class ShopPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Player player;
	private Item item;
	
	/**
	 * @param game that this is used in
	 * @param item that this is for
	 * */
	public ShopPanel(Game game, Item item){
		super();
		this.player = game.getPlayer();
		this.item = item;
		
		this.setMaximumSize(new Dimension(131, 175));
		this.setBackground(Color.WHITE);
		
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.add(Box.createRigidArea(new Dimension(0,15)));
		
		JLabel label = new JLabel(MazeCanvas.getItemIcon(this.item.getNumber()));
		label.setAlignmentX(CENTER_ALIGNMENT);
		this.add(label);
		this.add(Box.createRigidArea(new Dimension(0,15)));
		
		JLabel label2 = new JLabel("<html><center>" + this.item 
				+ "</center></html>");
		label2.setAlignmentX(CENTER_ALIGNMENT);
		label2.setMaximumSize(new Dimension(label2.getPreferredSize()));
		this.add(label2);
		this.add(Box.createRigidArea(new Dimension(0,10)));
		
		BaseButton button = new BaseButton("Buy for " + this.item.getWorth());
		button.addActionListener(this);
		button.setAlignmentX(CENTER_ALIGNMENT);
		this.add(button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.player.buy(this.item.getClass().newInstance());
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
		this.repaint();
	}

}
