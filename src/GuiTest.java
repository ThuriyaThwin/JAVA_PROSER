/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;


import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ChainedTransformer;



import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.TestGraphs;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AnimatedPickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.LayoutScalingControl;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.RotatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ShearingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ViewScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.MultiPickedState;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.picking.ShapePickSupport;
import edu.uci.ics.jung.visualization.renderers.GradientVertexRenderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelAsShapeRenderer;

/**
 * Demonstrates 3 views of one graph in one model with one layout.
 * Each view uses a different scaling graph mouse.
 * 
 * @author Tom Nelson 
 * 
 */
public class GuiTest extends JApplet {

    /**
     * the graph
     */
    Graph<AbstractAgent,Number> graph;

    /**
     * the visual components and renderers for the graph
     */
    VisualizationViewer<AbstractAgent,Number> vv1;
    
    /**
     * the normal transformer
     */
//    MutableTransformer transformer;
    
    Dimension preferredSize = new Dimension(1000,800);
    
    final String messageOne = "The mouse wheel will scale the model's layout when activated"+
    " in View 1. Since all three views share the same layout transformer, all three views will"+
    " show the same scaling of the layout.";
    
    final String messageTwo = "The mouse wheel will scale the view when activated in"+
    " View 2. Since all three views share the same view transformer, all three views will be affected.";
    
    final String messageThree = "   The mouse wheel uses a 'crossover' feature in View 3."+
    " When the combined layout and view scale is greater than '1', the model's layout will be scaled."+
    " Since all three views share the same layout transformer, all three views will show the same "+
    " scaling of the layout.\n   When the combined scale is less than '1', the scaling function"+
    " crosses over to the view, and then, since all three views share the same view transformer,"+
    " all three views will show the same scaling.";
    
    JTextArea textArea;
    JScrollPane scrollPane;
    
    /**
     * create an instance of a simple graph in two views with controls to
     * demo the zoom features.
     * 
     */
    public GuiTest() {
        

		Problem problem = new Problem(5);
		AgentSolver solver = new AgentSolver(problem, "DBAAgent", 20000);
		solver.solve();
		
        // TODO - need to check if there is a solution 
	    solver.printV(System.out);

        // create a simple graph for the demo
        graph = new UndirectedSparseMultigraph<AbstractAgent,Number>();
    

        int max_edge_id = 0;
		for (int i = 0; i < solver.agents.length; i++) {
			graph.addVertex(solver.agents[i]);
			int neighbors[] = solver.agents[i].get_neighbors();
			for (int j = 0; j <  neighbors.length ; j++)
				graph.addEdge(max_edge_id++, solver.agents[i], solver.agents[j]);
		}
		
		
        
        	//TestGraphs.getOneComponentGraph();
        
        // create one layout for the graph
		ISOMLayout<AbstractAgent,Number> layout = new ISOMLayout<AbstractAgent,Number>(graph);
        
        // create one model that all 3 views will share
        DefaultVisualizationModel<AbstractAgent,Number> visualizationModel =
            new DefaultVisualizationModel<AbstractAgent,Number>(layout, preferredSize);
 
        // create 3 views that share the same model
        vv1 = new VisualizationViewer<AbstractAgent,Number>(visualizationModel, preferredSize);
       
        vv1.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        
        EllipseVertexShapeTransformer<Number> x = new EllipseVertexShapeTransformer<Number>();
        vv1.getRenderContext().setVertexShapeTransformer(new RectangleVertexShapeFunction<AbstractAgent>() );
        vv1.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<AbstractAgent,Number>());
        //vv1.getRenderContext().getMultiLayerTransformer().addChangeListener(vv1);

        
        // customize the renderer
        //vv1.getRenderer().setVertexRenderer(new GradientVertexRenderer<AbstractAgent,Number>(Color.gray, Color.white, true));
        //vv1.getRenderer().setVertexLabelRenderer(vlasr);

        /*
        // customize the render context
        vv1.getRenderContext().setVertexLabelTransformer(
        		// this chains together Transformers so that the html tags
        		// are prepended to the toString method output
        		new ChainedTransformer<Number,String>(new Transformer[]{
        		new ToStringLabeller<Number>(),
        		new Transformer<Number,String>() {
					public String transform(Number input) {
						return "<html><center>x<p>"+input;
					}}}));
        */
        vv1.setBackground(Color.white);

        
        // create one pick support for all 3 views to share
       // GraphElementAccessor<AbstractAgent,Number> pickSupport = new ShapePickSupport<AbstractAgent,Number>(vv1);
       //vv1.setPickSupport(pickSupport);


        // create one picked state for all 3 views to share
        
       // PickedState<Number> pes = new MultiPickedState<Number>();
       // MultiPickedState<AbstractAgent> pvs = new MultiPickedState<AbstractAgent>();
       // vv1.setPickedVertexState(pvs);
       // vv1.setPickedEdgeState(pes);

        
        // set an edge paint function that shows picked edges
 //       vv1.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<Number>(pes, Color.black, Color.red));
//       vv1.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<AbstractAgent>(pvs, Color.red, Color.yellow));
        
        // add default listener for ToolTips
 //       vv1.setVertexToolTipTransformer(new ToStringLabeller<AbstractAgent>());
        
        Container content = getContentPane();
        JPanel panel = new JPanel(new GridLayout(1,0));
        
        final JPanel p1 = new JPanel(new BorderLayout());
        
        p1.add(new GraphZoomScrollPane(vv1));
       /*
        JButton h1 = new JButton("?");
        h1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText(messageOne);
                JOptionPane.showMessageDialog(p1, scrollPane, 
                        "View 1", JOptionPane.PLAIN_MESSAGE);
            }});
*/
        
        // create a GraphMouse for each view
        // each one has a different scaling plugin
        /*
        DefaultModalGraphMouse gm1 = new DefaultModalGraphMouse() {
            protected void loadPlugins() {
                pickingPlugin = new PickingGraphMousePlugin();
                animatedPickingPlugin = new AnimatedPickingGraphMousePlugin();
                translatingPlugin = new TranslatingGraphMousePlugin(InputEvent.BUTTON1_MASK);
                scalingPlugin = new ScalingGraphMousePlugin(new LayoutScalingControl(), 0);
                rotatingPlugin = new RotatingGraphMousePlugin();
                shearingPlugin = new ShearingGraphMousePlugin();

                add(scalingPlugin);
                setMode(Mode.TRANSFORMING);
            }
        };

        
        vv1.setGraphMouse(gm1);


        vv1.setToolTipText("<html><center>MouseWheel Scales Layout</center></html>");
 
*/                
       JPanel flow = new JPanel();
//        flow.add(h1);
//        flow.add(gm1.getModeComboBox());
//        p1.add(flow, BorderLayout.SOUTH);//
        flow = new JPanel();
         
        panel.add(p1);
        content.add(panel);
        

    }
    
    
    /**
     * A demo class that will make vertices larger if they represent
     * a collapsed collection of original vertices
     * @author Tom Nelson
     *
     * @param <V>
     */
    class ClusterVertexSizeFunction<V> implements Transformer<V,Integer> {
    	int size;
        public ClusterVertexSizeFunction(Integer size) {
            this.size = size;
        }

        public Integer transform(V v) {
            if(v instanceof Graph) {
                return 30;
            }
            return size;
        }
    }
    
    class RectangleVertexShapeFunction<V> extends EllipseVertexShapeTransformer<V> {

    	RectangleVertexShapeFunction() {
            setSizeTransformer(new ClusterVertexSizeFunction<V>(20));
        }
        @Override
        public Shape transform(V v) {
            if(v instanceof Graph) {
                int size = ((Graph)v).getVertexCount(); 
                return factory.getRectangle(v);
            }
            return super.transform(v);
        }
    }
 
    

    /**
     * a driver for this demo
     */
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new GuiTest());
        f.pack();
        f.setVisible(true);
    }
}
