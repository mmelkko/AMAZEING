package game.view;
import game.control.*;
import game.logics.*;
import game.results.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.*;

/**
 * The main window of the program. Creates all views and provides the methods
 * to manage which one is shown.
 * 
 * @extends JFrame
 * 
 * @author Merituuli Melkko
 * */
public class GameWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	private JPanel base, bottompane, resultpane, endpicturepane;
	private BasePanel startbase, gamebase, pausebase, intervalbase, resultbase,
	endbase;
	private BaseButton continuebutton, exitbutton, helpbutton, nextbutton, 
	resultbutton, returnbutton, savebutton, startbutton;
	private JLabel bottomtime, bottompoints;
	private Game game;
	private JDialog askname;

	/**
	 * @param game that this is used in
	 * */
	public GameWindow(Game game){
		this.game = game;
		this.setLayout(new BorderLayout());
		this.setTitle("AMAZEING");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setFont(new Font("Verdana", Font.PLAIN, 10));

		this.bottompane = new JPanel();
		this.bottompane.setBackground(new Color(237,28,36));
		this.bottomtime = new JLabel();
		this.bottompoints = new JLabel();
		this.bottomtime.setFont(new Font("Verdana", Font.BOLD, 10));
		this.bottompoints.setFont(new Font("Verdana", Font.BOLD, 10));
		this.bottomtime.setForeground(Color.BLACK);
		this.bottompoints.setForeground(Color.BLACK);
		this.bottomtime.setText("TIME: 00:00");
		this.bottompoints.setText("POINTS: 0");
		this.bottompane.add(this.bottomtime);
		this.bottompane.add(Box.createRigidArea(new Dimension(325,0)));
		this.bottompane.add(this.bottompoints);
		this.add(this.bottompane, BorderLayout.PAGE_END);

		this.base = new JPanel();
		this.base.setLayout(new CardLayout());

		this.continuebutton = new BaseButton("Continue Game");
		this.exitbutton = new BaseButton("Exit");
		this.helpbutton = new BaseButton("Help"); 
		this.nextbutton = new BaseButton("Begin Level");
		this.resultbutton = new BaseButton("Hall of Fame"); 
		this.returnbutton = new BaseButton("Return to Main");
		this.savebutton = new BaseButton("Save Results");
		this.startbutton = new BaseButton("Start Game");

		this.startbutton.setActionCommand("Start");
		this.continuebutton.setActionCommand("Continue");
		this.exitbutton.setActionCommand("Exit");
		this.helpbutton.setActionCommand("Help");
		this.nextbutton.setActionCommand("Next");
		this.resultbutton.setActionCommand("Results");
		this.returnbutton.setActionCommand("Return");

		JPanel shoppingPanel = new JPanel();
		shoppingPanel.setMaximumSize(new Dimension(500, 175));
		BoxLayout layout = new BoxLayout(shoppingPanel, BoxLayout.X_AXIS);
		shoppingPanel.setLayout(layout);
		shoppingPanel.add(Box.createRigidArea(new Dimension(25,0)));
		shoppingPanel.add(new ShopPanel(this.game, new Key()));
		shoppingPanel.add(Box.createRigidArea(new Dimension(25,0)));
		shoppingPanel.add(new ShopPanel(this.game, new Bananapeel()));
		shoppingPanel.add(Box.createRigidArea(new Dimension(25,0)));
		shoppingPanel.add(new ShopPanel(this.game, new Bomb()));
		shoppingPanel.setOpaque(false);

		JPanel labelHolder = new JPanel();
		JLabel label = new JLabel("<html><center>" +
				"AMAZEING is a game with three mazes for you to solve " +
				"and try to survive in.<br>You are playing with a violet " +
				"little duck that wants to go through the maze<br> all " +
				"the while avoiding the fireball-enemies with moustaches.<br>" +
				"<br>You can move your charecter with the arrow keys on your" +
				" keyboard.<br>The game pauses with [space] and items are" +
				" used with the corresponding number<br>below. To open doors " +
				"press [a] right next to them as many times as<br>the red " +
				"number on the lock shows.<br><br>" +
				" Collect apples and treasures for extra points,<br> but please" +
				" note that enemies can also pick them up!<br>" +
		"<br> Good luck!</html>");
		label.setMaximumSize(new Dimension(label.getPreferredSize()));
		label.setAlignmentX(CENTER_ALIGNMENT);
		label.setForeground(Color.WHITE);
		labelHolder.setMaximumSize(new Dimension(500,210));
		labelHolder.setOpaque(false);
		labelHolder.add(label);

		this.resultpane = new JPanel();
		this.resultpane.setMaximumSize(new Dimension(500, 200));
		ResultTable grid = this.game.getResultTable();
		JTable table = new JTable(grid); 
		grid.addTableModelListener(table);
		this.resultpane.add(table);
		this.resultpane.add(Box.createRigidArea(new Dimension(0,15)));
		this.resultpane.add(table.getTableHeader());
		this.resultpane.setOpaque(false);
		this.resultpane.add(table);

		this.endpicturepane = new JPanel();
		this.endpicturepane.setMaximumSize(new Dimension(500, 250));
		this.endpicturepane.setOpaque(false);

		ArrayList<BaseButton> startbuttons = new ArrayList<BaseButton>();
		startbuttons.add(startbutton);
		startbuttons.add(helpbutton);
		startbuttons.add(resultbutton);
		startbuttons.add(exitbutton);

		ArrayList<BaseButton> pausebuttons = new ArrayList<BaseButton>();
		pausebuttons.add(this.continuebutton);
		pausebuttons.add(this.returnbutton);
		pausebuttons.add(this.exitbutton);

		ArrayList<BaseButton> intervalbuttons = new ArrayList<BaseButton>();
		intervalbuttons.add(this.nextbutton);
		intervalbuttons.add(this.exitbutton);

		ArrayList<BaseButton> resultbuttons = new ArrayList<BaseButton>();
		resultbuttons.add(this.savebutton);
		resultbuttons.add(this.returnbutton);

		ArrayList<BaseButton> endbuttons = new ArrayList<BaseButton>();
		endbuttons.add(this.returnbutton);
		endbuttons.add(this.exitbutton);

		this.startbase = new BasePanel(this.game, startbuttons, 
				new ImageIcon("src/images/logo.gif"), null);
		this.gamebase = new BasePanel(this.game, null, null, null);
		this.pausebase = new BasePanel(this.game, pausebuttons, null, 
				labelHolder);
		this.intervalbase = new BasePanel(this.game, intervalbuttons, 
				new ImageIcon("src/images/shoplogo.png"), shoppingPanel);
		this.resultbase = new BasePanel(this.game, resultbuttons, null, 
				this.resultpane);
		this.endbase = new BasePanel(this.game, endbuttons, null, 
				this.endpicturepane);

		this.base.add(this.startbase, "START");
		this.base.add(this.gamebase, "GAME");
		this.base.add(this.pausebase, "PAUSE");
		this.base.add(this.intervalbase, "INTERVAL");
		this.base.add(this.resultbase, "RESULTS");
		this.base.add(this.endbase, "END");
		this.add(this.base, BorderLayout.CENTER);

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} 
		catch (Exception e) {}

		this.openView(Game.START);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * Opens a new JDialog that asks the Player of a name. This name is needed
	 * to make a proper ResultRow and if the player does not want to give one,
	 * the JDialog closes down and goes directly to the end-view.
	 * */
	public boolean askForResults(){
		this.askname = new JDialog(this);
		this.askname.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		Object[] possibilities = null;
		String answer = (String) JOptionPane.showInputDialog(
				this.askname,
				" So... What do you call yourself?",
				"Hello. You seem to have won.",
				JOptionPane.PLAIN_MESSAGE,
				null,
				possibilities, null);
		this.askname.setVisible(true);

		if ((answer != null) && (answer.length() > 0)) {
			ResultRow row = new ResultRow(answer, 
					Calendar.getInstance().getTime(), 
					this.game.getPlayer().getPoints());
			this.game.getResultTable().addRow(row);
			this.askname.dispose();
			return true;
		}
		this.askname.dispose();

		return false;
	}

	/**
	 * Switches the desired panel to be the one on top in the CardLayout
	 * 
	 * @param int the number of the panel to be switched as the topmost one.
	 * */
	public void openView(int which){
		BasePanel panel = null;

		if (which == Game.START){
			CardLayout cl = (CardLayout)(this.base.getLayout());
			cl.show(this.base, "START");
			panel = this.startbase;

		} else if (which == Game.PLAY){
			CardLayout cl = (CardLayout)(this.base.getLayout());
			cl.show(this.base, "GAME");
			panel = this.gamebase;
			this.gamebase.add(this.game.getCanvas());
			panel.setFocusable(true);
			if (panel.getKeyListeners().length == 0){
				panel.addKeyListener(this.game.getListener());
			}
			panel.requestFocus();

		} else if (which == Game.PAUSED){
			CardLayout cl = (CardLayout)(this.base.getLayout());
			cl.show(this.base, "PAUSE");
			panel = this.pausebase;
			this.continuebutton.setEnabled(this.game.hasBegun());

		} else if (which == Game.INTERVAL){
			CardLayout cl = (CardLayout)(this.base.getLayout());
			cl.show(this.base, "INTERVAL");
			panel = this.intervalbase;

		} else if (which == Game.RESULTS){	
			CardLayout cl = (CardLayout)(this.base.getLayout());
			cl.show(this.base, "RESULTS");
			panel = this.resultbase;
			this.savebutton.setEnabled(this.game.hasBegun());

		} else if (which == Game.END){
			CardLayout cl = (CardLayout)(this.base.getLayout());
			cl.show(this.base, "END");
			panel = this.endbase;
			if (this.endpicturepane.getComponents().length == 0){
				JLabel label = new JLabel(this.game.getCanvas().getEndIcon());
				label.setAlignmentX(CENTER_ALIGNMENT);
				this.endpicturepane.add(label);
			}
		}

		if (panel != null){
			panel.placeAllComponents();
		}

		this.pack();
	}

	/**
	 * If canvas is not null, removes current canvas from the components of
	 * the gamepanel.
	 * */
	public void removeCanvas(){
		if (this.game.getCanvas() != null){
			Component[] components = this.gamebase.getComponents();

			for (int a = 0; a < components.length; a++){
				if (components[a] == this.game.getCanvas()){
					this.gamebase.remove(this.game.getCanvas());
				}
			}
		}
	}

	/**
	 * Updates the visible time in the bottom JLabel.
	 * 
	 * @param long minutes that are updated to the time
	 * @param long seconds that are updated to the time
	 * */
	public void updateTime(long minutes, long seconds){
		this.bottomtime.setText("TIME: " + (minutes > 9 ? minutes : "0" 
			+ minutes) + ":" + (seconds > 9 ? seconds : "0" + seconds));
	}

	/**
	 * Updates the visible points in the bottom JLabel
	 * */
	public void updatePoints(){
		this.bottompoints.setText("POINTS: " 
				+ this.game.getPlayer().getPoints());
	}

}
