package simulator;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

//------###CONNECTION CLASS BUTTON DAN MİRAS###
public class Connection extends Button{
    public ArrayList<Line> line;
    public ArrayList<Connection> connecteds;
    public Component parent;
    public boolean connecting;
    public boolean connected;
    public boolean type=false;
    public int code;
    public boolean value;
    
    //Connection contructor
    public Connection(int x, int y, boolean type) 
    {
        super(x, y);
        this.type = type;//Connection tipini tanımla
        initImage();
        line = new ArrayList<Line>();//kablo dizisini
        connecteds = new ArrayList<Connection>();//bağlı bağlantıların listesi
    }
    
    //Connection tipini geri döndür
    public boolean getType()
    {
        return type;
    }
    
    //Resmi güncelle
    public void initImage()
    {
        loadImage("images\\connection.png");
        getImageDimensions();
    }
    
    //Tıklandı mı metodunu override et
    @Override
    public boolean isClicked(MouseEvent m)
    {
        Point p = new Point(x+(int)mainPanel.circuitPos.getX(), y+(int)mainPanel.circuitPos.getY());
        return m.getX()>p.x&&m.getX()<p.x+width&&m.getY()>p.y&&m.getY()<p.y+height;
    }
}