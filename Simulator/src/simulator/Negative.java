package simulator;

//------###NEGATIVE CLASS I COMPONENTTEN MİRAS###
public class Negative extends Component{
    
    //Negative constructor
    public Negative(int x, int y, boolean tbItem) {
        super(x, y, tbItem);
        initImage();
        out = new Connection(x+width/2, y, true);
        out.setX(out.getX()+out.width/2);
        out.parent=this;
        out.code=super.code;
    }

    //Bağlantı güncelleme
    @Override
    public void updateConnect()
    {
        out.setX(x+width);
        out.setY(y+height/2-out.height/2);
    }
    
    //Çıkış bağlantısı hesaplama
    @Override
    public void Calculate()
    {
        out.value=false;
        calculated=true;
    }
    
    //Class Adı döndürme
    @Override
    public String getClassName()
    {
        return "simulator.Negative";
    }
    
    //Resmi güncelleme
    public void initImage()
    {
        loadImage("images\\neg.png");
        getImageDimensions();
    }
}