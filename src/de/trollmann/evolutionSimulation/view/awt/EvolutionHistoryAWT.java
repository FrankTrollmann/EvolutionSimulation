package de.trollmann.evolutionSimulation.view.awt;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.trollmann.evolutionSimulation.model.entities.mutationHistory.MutationHistoryNode;
import de.trollmann.evolutionSimulation.model.physics.HasPosition;

/**
 *  a class displaying an evolution History
 * @author frank
 *
 */
public class EvolutionHistoryAWT extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * the list of creatures to display
	 */
	MutationHistoryNode evolutionHistory;
	
	/**
	 * the index of to the current creature
	 */
	MutationHistoryNode currentlyShownNode;
	
	/**
	 * map of mutation history node to their positions
	 */
	HashMap<MutationHistoryNode,HasPosition> positions = new HashMap<MutationHistoryNode, HasPosition>();
	
	
	/**
	 * list of nodes per layer
	 */
	List<List<MutationHistoryNode>> layers = new LinkedList<List<MutationHistoryNode>>();

	
	// --------------------------------
	// ------Zoom Buttons--------------
	// --------------------------------
	
	Button zoomInButton;
	Button zoomOutButton;
	
	/**
	 * canvas to draw crature on
	 */
	DrawableCanvasAWT creatureDraw;
	/**
	 * scrolling wrapper for drawable canvas
	 */
	JScrollPane scrollWrapper;
	
	
	/**
	 * canvas to draw the evolution tree on
	 */
	DrawableCanvasAWT treeDraw;
	
	/**
	 * 
	 */
	JScrollPane scrollWrapperTree;
	
	int treeDrawWidth = 400;

	/**
	 * constructor
	 * @param evolutionHistory
	 */
	public EvolutionHistoryAWT(MutationHistoryNode evolutionHistory) {
		super("EvolutionHistory");
		this.evolutionHistory = evolutionHistory;
		currentlyShownNode = evolutionHistory;
		this.setSize(800,500);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("key event: " + e.getKeyCode());
				if( e.getKeyCode() == KeyEvent.VK_W) {
					if(currentlyShownNode.getParent() != null) {
						currentlyShownNode = currentlyShownNode.getParent();
						update();
					}
				}
			}
		});
		
		// init drawing canvas
		creatureDraw = new DrawableCanvasAWT(400,400);
		scrollWrapper = new JScrollPane(creatureDraw);
		
		treeDraw = new DrawableCanvasAWT(treeDrawWidth, 400);
		treeDraw.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("clicked");
				double minDistance = Long.MAX_VALUE;
				MutationHistoryNode closest = null;
				for(Entry<MutationHistoryNode,HasPosition> entry: positions.entrySet()) {
					double thisDistance = entry.getValue().getPositionDistance(e.getX(), e.getY());
					if(thisDistance < minDistance) {
						closest = entry.getKey();
						minDistance = thisDistance;
					}
				}
				
				if(minDistance < 15) {
					currentlyShownNode = closest;
					update();
					System.out.println("found click");
				}

			}
		});
		scrollWrapperTree = new JScrollPane(treeDraw);
		scrollWrapperTree.setPreferredSize(new Dimension(treeDrawWidth, 400));
		
		
		// init zoom Button
		zoomInButton = new Button("+");
		zoomInButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				creatureDraw.increaseZoomFactor(0.2);
				update();
			}
		});
		
		zoomOutButton = new Button("-");
		zoomOutButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				creatureDraw.increaseZoomFactor(-0.2);
				update();
			}
		});
		JPanel bottomControls = new JPanel();
		bottomControls.setLayout(new BorderLayout());
		bottomControls.add(zoomInButton,BorderLayout.EAST);
		bottomControls.add(zoomOutButton,BorderLayout.WEST);
		
		
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(scrollWrapper,BorderLayout.CENTER);
		this.getContentPane().add(bottomControls,BorderLayout.SOUTH);
		this.getContentPane().add(scrollWrapperTree, BorderLayout.EAST);
		
		update();
	}
	
	/**
	 * update label and canvas
	 */
	public void update() {
//		currentLabel.setText((current + 1) + " / " + evolutionHistory.size());
		
		creatureDraw.clear();
		currentlyShownNode.getBlueprint().drawSchematicView(creatureDraw, 200, 200, 0.0);
		if(currentlyShownNode.getMutation() != null) {
			creatureDraw.drawText(Color.white, currentlyShownNode.getMutation().getDescription(), 0, 30);
		}
		creatureDraw.drawText(Color.white, "Entities: " + currentlyShownNode.getNrLivingCreatures(), 0, 60);
		creatureDraw.updateView();
		
		treeDraw.updateSize(treeDrawWidth, Math.max(500, 100 + 50*layers.size()));
		treeDraw.clear();
		drawNavigationMenu();
		treeDraw.updateView();
	}
	
	/**
	 * draw the tree of evolution histories
	 */
	public void drawNavigationMenu() {
		int maxWidth = 0;
		
		// variable initialization
		// also clear global variables
		layers.clear();
		positions.clear();
		List<MutationHistoryNode> current = new LinkedList<MutationHistoryNode>();
		current.add(evolutionHistory);
		
		// fill up layers data structure
		while(current.size()> 0) {
			List<MutationHistoryNode> previous = current;
			layers.add(current);
			maxWidth = Math.max(maxWidth, current.size());
			
			// get all next children
			current = new LinkedList<MutationHistoryNode>();
			for (MutationHistoryNode mutationHistoryNode : previous) {
				current.addAll(mutationHistoryNode.getChildren());
			}
		}
		
		// draw 
		int layerCounter = 0;
		for (List<MutationHistoryNode> layer : layers) {
			int creatureCounter = 0;
			for (MutationHistoryNode mutationHistoryNode : layer) {
				// calculate coords
				int y = 50 + 50*layerCounter;
				int x = 50 + ((treeDrawWidth - 100) / layer.size()) * creatureCounter;
				int radius = Math.min(15, (treeDrawWidth - 100)/ maxWidth/2 - 2);
				
				// save dimension for later
				HasPosition dim = new HasPosition(x,y);
				positions.put(mutationHistoryNode, dim);
				
				Color color;
				if(mutationHistoryNode.getNrLivingCreatures() == 0) {
					color = new Color(0,0,100);
				} else {
					color = new Color(0,0,255);
				}
				if(mutationHistoryNode.equals(currentlyShownNode)) {
					color = Color.yellow;
				}
				
				// draw circle
				treeDraw.drawCircle(x, y, radius, color);
				
				// draw line to parent
				if(mutationHistoryNode.getParent() != null) {
					HasPosition parentDim = positions.get(mutationHistoryNode.getParent());
					treeDraw.drawLine(dim.getX(), parentDim.getX(), dim.getY(), parentDim.getY(), Color.blue);
				}
				creatureCounter++;
			}
			layerCounter++;
		}
		
	}
	
	
	
}
