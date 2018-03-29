/*
 * see license.txt
 */
package leola.gdx.graph;

import java.util.function.Consumer;

import leola.gdx.graph.Edges.Directions;

/**
 * A node in a graph linked by {@link Edge}s.
 * 
 * @author Tony
 *
 */
public class GraphNode<E> {

    private Edges<E> edges;
    private E value;
    
    /**
     * Constructs a {@link GraphNode}.
     * 
     * @param value
     */
    public GraphNode(E value) {
        this.edges = new Edges<E>();
        this.value = value;
    }
    
    
    /**
     * Constructs a {@link GraphNode}.
     */
    public GraphNode() {
        this(null);
    }
    
    
    /**
     * Adds an {@link Edge}.
     * 
     * @param edge
     */
    public void addEdge(Directions dir, Edge<E> edge) {
        this.edges.addEdge(dir, edge);
    }
    
    /**
     * @return the value
     */
    public E getValue() {
        return this.value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(E value) {
        this.value = value;
    }

    /**
     * Get the graphs {@link Edge}s.
     * 
     * @return
     */
    public Edges<E> edges() {
        return this.edges;
    }
    
    /**
     * Finds the {@link Edge} that links this node with the
     * rightNode.
     * 
     * <p>
     * Note:  The Edge is found by testing pointer equality not Object equality (i.e., '==' and not .equals()).
     * 
     * @param rightNode - other node that is linked with this node
     * @return the edge that links the two nodes, null if no such nodes exists.
     */
    public Edge<E> getEdge(GraphNode<E> rightNode) {
        Edge<E> result = null;
        
        /* Search for the matching Edge (the one that links the right and left) */
        Edges<E> edges = edges();
        for(int i = 0; i < edges.size(); i++) {
            Edge<E> edge = edges.get(i);
            if(edge == null) {
                continue;
            }
            
            /* Notice we check if the REFERENCE equals the right node */
            if (edge.getRight() == rightNode) {
                result = edge;
                break;
            }
        }
        
        return (result);
    }
    
    /**
     * Iterates over each edge
     * 
     * @param f
     */
    public void forEachEdge(Consumer<GraphNode<E>> f) {        
        for(int i = 0; i < edges.size(); i++) {
            Edge<E> e = edges.get(i);
            if(e!=null) {
                f.accept(e.getRight());
            }
        }
    }
}
