package simulator;

//------###NOT CLASS I COMPONENTTEN MİRAS###
public class NOT extends Component{
    
    //Not Constructor
    public NOT(int x, int y, boolean tbItem) {
        super(x, y, tbItem);
        initImage();
        out = new Connection(x+width/2, y, true);
        in1 = new Connection(x-width/2, y, false);
        out.setX(out.getX()+out.width/2);
        in1.setX(in1.getX()-in1.width/2);
        out.parent=this;
        out.code=super.code;
    }

    //Bağlantı güncelleme
    @Override
    public void updateConnect()
    {
        out.setX(x+width);
        out.setY(y+height/2-out.height/2);
        in1.setX(x-width/2+10);
        in1.setY(y+height/2-in1.height/2);
    }
    
    //Çıkış bağlantısı hesaplama
    @Override
    public void Calculate()
    {
        out.value=!in1.value;
        calculated=true;
    }
    
    //Class Adı döndürme
    @Override
    public String getClassName()
    {
        return "simulator.NOT";
    }
    
    //Resmi güncelleme
    public void initImage()
    {
        loadImage("images\\not.png");
        getImageDimensions();
    }
}