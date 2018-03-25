package simulator;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

//------###COMPONENT CLASS BUTTON DAN MİRAS###
public class Component extends Button{
    protected int firstX;//ilk x konumu
    protected int firstY;//ilk y konumu
    protected Connection in1, in2, in3, out;//Bağlantılar
    protected boolean dragging;//sürükleme değişkeni
    protected int code;//Unique code
    protected Random r;//Randomiser
    protected boolean tbItem;//Toolbox değişkeni
    protected boolean calculated;//Hesaplandı mı
    
    //Component consturctor
    public Component(int x, int y, boolean tbItem) {
        super(x, y);
        r=new Random();
        dragging=false;
        this.tbItem = tbItem;
        code=r.nextInt(1000000);//UNIQUE CODE
    }
    
    //Override ilk pozisyonu saklamak için
    @Override
    protected void getImageDimensions()
    {
        super.getImageDimensions();
        firstX=x;
        firstY=y;
    }
    
    //İlk konuma resetle
    public void resetPosition()
    {
        x=firstX;
        y=firstY;
    }
    
    //Abstract hesapla metodu
    public void Calculate(){}
    
    //Sürüklemeyi başlat
    public void DragStart()
    {
        dragging = true;
    }
    
    //Sürüklemeyi durdur
    public void DragStop()
    {
        dragging = false;
    }
        
    //Bağlantıları döndür
    public ArrayList<Connection> returnConnections()
    {
        ArrayList<Connection> connections = new ArrayList<Connection>();
        connections.add(in1);
        connections.add(in2);
        connections.add(in3);
        connections.add(out);
        
        return connections;
    }
    
    //Bağlantıları güncelle
    public void updateConnect(){}
    
    //Tıklandı mı metodunu override et
    @Override
    public boolean isClicked(MouseEvent m)
    {
        Point p = new Point(x+(int)mainPanel.circuitPos.getX(), y+(int)mainPanel.circuitPos.getY());
        if(tbItem)
        {
            return m.getX()>x&&m.getX()<x+width&&m.getY()>y&&m.getY()<y+height;
        }else{
            return m.getX()>p.x&&m.getX()<p.x+width&&m.getY()>p.y&&m.getY()<p.y+height;
        }
    }
    
    //Class ın ismini dışarıdan obje oluşturmak için çek
    public String getClassName()
    { 
        return null;
    }
    
    //Hesaplamaları sıfırla
    public void reset()
    {
        calculated=false;
        if(in1!=null)
        {
            in1.value=false;
        }
        if(in2!=null)
        {
            in2.value=false;
        }
        if(in3!=null)
        {
            in3.value=false;
        }
        if(out!=null)
        {
            out.value=false;
        }
    }
    
    //Sürükleniyor mu?
    public boolean isDragging()
    {
        return dragging;
    }
}