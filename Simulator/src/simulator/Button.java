package simulator;

import java.awt.event.MouseEvent;

//------###BUTTON CLASS SPRITE DAN MİRAS###
public class Button extends Sprite{
    
    //Button constructor
    public Button(int x, int y) {
        super(x, y);
    }
    
    //get button x
    @Override
    public int getX()
    {
        return x;
    }
    
    //get button y
    @Override
    public int getY()
    {
        return y;
    }
    
    //set button x
    @Override
    public void setX(int x)
    {
        this.x = x;
    }
    
    //set button y
    @Override
    public void setY(int y)
    {
        this.y = y;
    }
}