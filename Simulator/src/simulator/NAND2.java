package simulator;

//------###NAND 2 CLASS I COMPONENTTEN MİRAS###
public class NAND2 extends Component{

    //Nand2 Constructor
    public NAND2(int x, int y, boolean tbItem) {
        super(x, y, tbItem);
        initImage();
        in1 = new Connection(x-width/2, y-height/2, false);
        in2 = new Connection(x-width/2, y+height/2, false);
        out = new Connection(x+width/2, y, true);
        in1.setX(in1.getX()-in1.width/2);
        in2.setX(in2.getX()-in2.width/2);
        out.setX(out.getX()+out.width/2);
        in1.parent=this;
        in2.parent=this;
        out.parent=this;
        in1.code=super.code;
        in2.code=super.code;
        out.code=super.code;
    }

    //Bağlantıları güncelleme
    @Override
    public void updateConnect()
    {
        in1.setX(x-in1.width);
        in1.setY(y-in1.height/2);
        in2.setX(x-in2.width);
        in2.setY(y+height/2+in2.height);
        out.setX(x+width);
        out.setY(y+height/2-out.height/2);
    }
    
    //Çıkış bağlantısını hesaplama
    @Override
    public void Calculate()
    {
        out.value=!(in1.value&&in2.value);
        calculated = true;
    }
    
    //Class adı döndürme
    @Override
    public String getClassName()
    {
        return "simulator.NAND2";
    }
    
    //Resmi güncelleme
    public void initImage()
    {
        loadImage("images\\nand2.png");
        getImageDimensions();
    }
}