/*
 * This assignment represents my own work and is in accordance with the College Academic Policy
 * Melanie Roy-Plommer - Gex 2018
 */
package sudoku;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author melan
 */
public class ImageEntity implements MouseListener{    
    
    protected ArrayList<Image> images;
    
    // physical characteristics
    protected double w, h, x, y; // dimensions and position of image entities
    
    protected Dimension size = new Dimension(100,100);
    protected Point position = new Point(0,0);
    protected double orientation = 0;
    
    // temporal tracking
    protected int animationCounter;
    
    // state 
    protected boolean isVisible;
    protected boolean showingBoundingBox = false;
    protected boolean isEnabled = true;
    
    // bounding box for detecting contact
    protected Rectangle boundingBox = new Rectangle(0,0,10,10);
    
    // slide effects
    protected boolean isSliding = false;
    protected int slideCounter = 0;
    protected Point endPosition;
    
    // for ImagesEntities that have a "has-a" relationship
    protected ArrayList<ImageEntity> ownedEntities = new ArrayList<ImageEntity>();    
    
    //toolkit for loading images
    protected Toolkit toolkit;

    public ImageEntity(Dimension size, Point position, int orientation) {
        setSize(size);        
        setPosition(position);
        setOrientation(orientation);
        
        setVisible(true);
        
        w = this.size.getWidth();
        h = this.size.getHeight();
        x = position.getX();
        y = position.getY();
        
        // set up images
        toolkit = Toolkit.getDefaultToolkit();
        images = new ArrayList<Image>();               
    }
    
    // functionality 
    public void update(){
        //updated physical parameters
        w = this.size.getWidth();        
        h = this.size.getHeight();
        x = position.getX();
        y = position.getY();        
        
        animationCounter++;
        if(animationCounter >= Integer.MAX_VALUE - 2)
            animationCounter = 0;
        
        // changing position of an image if it in a sliding state
        if (isSliding){
            // if slide complete, end sliding state
            if(slideCounter <= 0){
                slideCounter = 0;
                isSliding = false;
            }
            slideCounter--; //advance frame for slide animation
            // calculate slide increment
            int dX = (int) ((endPosition.getX() - x)/slideCounter);            
            int dY = (int) ((endPosition.getY() - y)/slideCounter);
            
            // move entity and its bounding box by increment
            position.translate(dX, dY);
            boundingBox.translate(dX, dY);
            
            // apply same movement to owned Entities (maintains relative position of "parent" and "child")
            for(ImageEntity ent: ownedEntities){
                Point p = ent.getPosition();
                p.translate(dX, dY);
                ent.setPosition(p);
            }
        }
    }
    
    public void paint(Graphics2D g){
        
        AffineTransform oldAT = g.getTransform();
        g.rotate(Math.toRadians(orientation),position.getX(), position.getY());
        
        if(isVisible){
            
            g.drawImage(images.get(0), (int) (x - 0.5 * w), (int) (y - 0.5 * h), (int) w, (int)h, null);

            g.setTransform(oldAT);
            if(showingBoundingBox){
                g.draw(boundingBox);
            }        
        }        
    }
    public void reset(){;}
    
    public boolean contains(Point point){
        return boundingBox.contains(point);        
    }
    
    // effects
    
    public void slide(Point endPosition, int frames){
        if(!isSliding)
        {
            this.endPosition = endPosition;
            isSliding = true;
            slideCounter = frames;
        }    
    }
    
    public void showBoundingBox() {
        if(showingBoundingBox)
            showingBoundingBox = false;
        else
            showingBoundingBox = true;           
    }    
    
    // getters and setters
    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
        
        w = this.size.getWidth();
        h = this.size.getHeight();
        x = position.getX();
        y = position.getY();
        
        setBoundingBox(new Rectangle((int)(x-0.5*w), (int)(y-0.5*h), (int)w, (int)h));
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
        
        w = this.size.getWidth();
        h = this.size.getHeight();
        x = position.getX();
        y = position.getY();
        
        setBoundingBox(new Rectangle((int)(x-0.5*w), (int)(y-0.5*h), (int)w, (int)h));
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation % 360;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    protected void addImage(String imagePath) {
        URL url = getClass().getResource(imagePath);
        
        images.add(toolkit.getImage(url));        
    }
    
    public void setEnabled(boolean enabled){
        isEnabled = enabled;
    }
    
    public void addOwnedEntity(ImageEntity ent){
        ownedEntities.add(ent);
    }

    @Override
    public void mouseClicked(MouseEvent e) {;}

    @Override
    public void mousePressed(MouseEvent e)  {;}

    @Override
    public void mouseReleased(MouseEvent e)  {;}

    @Override
    public void mouseEntered(MouseEvent e)  {;}

    @Override
    public void mouseExited(MouseEvent e)  {;}
    
}
