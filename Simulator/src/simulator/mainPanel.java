//------###-###
package simulator;

//------###IMPORTLAR###
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.Timer;

//------###ANA PANEL CLASSI###
public class mainPanel extends JPanel implements ActionListener, MouseListener{
    private int width;//Sayfa genişliği
    private int height;//Sayfa yüksekliği
    private final int div = 100;//Sayfa bölme sayısı
    private final int delay = 20;//Timer gecikmesi
    private Timer timer;//Panel Timeri
    private SimulateButton simulate;//Simüle et butonu
    private ArrayList<Component> components;//Logic devre elemanları
    private ArrayList<Component> outputs;//Devre çıkışı elemanları
    private ArrayList<Component> tbItems;//Toolbox elemanları
    public static Point circuitPos;//Devrelerin panel üzerindeki konumu
    public static Point toolboxPos;//Toolbox'ın panel üzerindeki boyutu
    private ArrayList<Point> points;//Çizim sırasında tıklanan noktalar
    private ArrayList<ScrollButton> sbcircuitlist;//Kaydırma butonu listesi
    private ArrayList<ColorPicker> colorButtons;//Renk değiştirme butonları
    private boolean drawing;//Çizim yapma değişkeni
    private Connection first;//Bağlantı sırasında ilk tıklanan devre elemanı
    private Color color;
    
    //------###ANA PANEL CONSTRUCTOR###
    public mainPanel()
    {
        initPanel();//initialise ana panel
    }
    
    //------###INITIALISE ANA PANEL###
    private void initPanel()
    {
        color = new Color(0,0,0);
        width = Simulator.width;//JFrame genişliğine eşitleme
        height = Simulator.height;//JFrame yüksekliğine eşitleme
        addMouseListener(this);//Mouse Event dinleyicisi ekle
        drawing = false;//Çizimi kapalı başlat
        simulate = new SimulateButton(width-70, height-21);//Simulate butonunu oluştur
        components = new ArrayList<Component>();//Logic devre eleman dizisini intialise et
        outputs = new ArrayList<Component>();//Çıkış elemanları dizisini initialise et
        tbItems = new ArrayList<Component>();//Toolbox itemleri dizisini intialise et
        sbcircuitlist= new ArrayList<ScrollButton>();//Kaydırma butonları elemanları dizisini
        circuitPos = new Point(0,0);//Devrelerin panel üzerindeki konumunu belirle
        points = new ArrayList<Point>();//Çizim nokatları dizisini initialise et
        colorButtons = new ArrayList<ColorPicker>();//Renk butonları dizisini intialise et
        
        sbcircuitlist.add(new ScrollButton(width/div*94, height/div*79, 0, 1, "up"));//Yukarı kaydırma butonu
        sbcircuitlist.add(new ScrollButton(width/div*94, height/div*89, 0, -1, "down"));//Aşağı kaydırma butonu
        sbcircuitlist.add(new ScrollButton(width/div*97, height/div*84, -1, 0, "right"));//Sağa kaydırma butonu
        sbcircuitlist.add(new ScrollButton(width/div*91, height/div*84, 1, 0, "left"));//Sola kaydırma butonu
        
        colorButtons.add(new ColorPicker(width/div*97, height/div*10,"red", Color.red));//Kırmızı kablo butonu
        colorButtons.add(new ColorPicker(width/div*97, height/div*14,"green", Color.green));//Yeil kablo butonu
        colorButtons.add(new ColorPicker(width/div*97, height/div*18,"blue", Color.blue));//Mavi kablo butonu
        colorButtons.add(new ColorPicker(width/div*97, height/div*22,"cyan", Color.cyan));//Cyan kablo butonu
        colorButtons.add(new ColorPicker(width/div*97, height/div*26,"magenta", Color.magenta));//Mor kablo butonu
        colorButtons.add(new ColorPicker(width/div*97, height/div*30,"yellow", Color.yellow));//Sarı kablo butonu
        colorButtons.add(new ColorPicker(width/div*97, height/div*34,"black", Color.black));//Siyah kablo butonu
        
        initToolboxItems();//Toolbox itemlerini initialise et
        initTimer();//Timeri intialise et
    }
    
    //------###TIMERI INITIALISE ET###
    private void initTimer()
    {
        timer = new Timer(delay, this);//Timer intialise
        timer.start();//Timer başlat
    }
    
    //------###TOOLBOX ITEMLERINI INITIALISE ET###
    private void initToolboxItems()
    {
        //0, 0 noktasından a mesafe aşağıda b mesafe sağda c mesafe artarak yerleştir
        int a=80 , b=60, c=45;
        tbItems.add(new AND2(b, a, true));
        a+=c;
        tbItems.add(new OR2(b, a, true));
        a+=c;
        tbItems.add(new NAND2(b, a, true));
        a+=c;
        tbItems.add(new NOR2(b, a, true));
        a+=c;
        tbItems.add(new XOR2(b, a, true));
        a+=c;
        tbItems.add(new NOT(b, a, true));
        a+=c;
        tbItems.add(new AND3(b, a, true));
        a+=c;
        tbItems.add(new OR3(b, a, true));
        a+=c;
        tbItems.add(new Positive(b, a, true));
        a+=c;
        tbItems.add(new Negative(b, a, true));
        a+=c;
        tbItems.add(new Output(b, a, true));
        
    }
    
    //------###ÇİZİMLERİ YAPAN METODU OVERRIDE###
    @Override
    public void paintComponent(Graphics g)
    {
        updateDragging();//Sürüklemeleri güncelle
        super.paintComponent(g);//Paneli temizle
        drawBackground(g);//Arkaplanı çiz
        drawCircuits(g);//Devre elemanlarını çiz
        drawForeground(g);//Ön planı çiz
        drawButtons(g);//Butonları çiz
    }
    
    //------###ARKAPILANI ÇİZ###
    private void drawBackground(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;//Graphics2D obje
        g2d.setColor(new Color(0,0,0,25));//Renk seçimi
        
        //Arkaplana kareli ekran çiz
        for(int a=0; a<50; a++)
        {
            g2d.drawLine(0, a*height/50, width, a*height/50);
        }
        for(int b=0; b<100; b++)
        {
            g2d.drawLine(b*width/100, 0, b*width/100, height);
        }
        g2d.setStroke(new BasicStroke(3));
        g2d.setPaint(new Color(0, 255, 0, 50));
        for(int a=0; a<height; a+=12)
        {
            for(int b=0; b<width; b+=12)
            {
                g2d.drawLine(b, a, b, a);
            }
        }
        
        
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(255, 0, 0, 25));
        
        //Ekrana panelin 0,0 kabul ettiğin noktasında bir artı işareti çiz
        g2d.drawLine(
            -5000+(int)circuitPos.getX(),
            height/div*50+(int)circuitPos.getY(),
            5000+(int)circuitPos.getX(),
            height/div*50+(int)circuitPos.getY());
        
        g2d.setColor(new Color(0, 0, 255, 25));
        
        g2d.drawLine(
            width/div*59+(int)circuitPos.getX(),
            -5000+(int)circuitPos.getY(),
            width/div*59+(int)circuitPos.getX(),
            5000+(int)circuitPos.getY());
    }
    
    //------###ÖNPLANI ÇİZ###
    private void drawForeground(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;//Graphics2D obje
        g2d.setColor(new Color(50, 50, 50));//Renk seçimi
        
        //Çizim yapılacak kısım harici her yeri boya
        Area foreground = new Area(new Rectangle(0, 0, width, height));
        Area substract = new Area(new Rectangle(width/div*10, height/div*7, width/div*89, height/div*86));
        foreground.subtract(substract);
        g2d.fill(foreground);
    }
    
    //------###BUTONLARI ÇİZ###
    private void drawButtons(Graphics g)
    {
        
        //Butonlar görünürse çiz
        Graphics2D g2d = (Graphics2D) g;
        
        if(simulate.isVisible())
        {
            g2d.drawImage(simulate.getImage(), simulate.x, simulate.y, this);
        }
        
        for(ColorPicker cp : colorButtons)
        {
            if(cp.isVisible())
            {
                g2d.drawImage(cp.getImage(), cp.getX(), cp.getY(), this);
            }
        }
        
        for(ScrollButton s : sbcircuitlist)
        {
            if(s.isVisible())
            {
                g2d.drawImage(s.getImage(), s.getX(), s.getY(), this);
            }
        }
        
        for(Component c : tbItems)
        {
            if(c.isVisible())
            {
                g2d.drawImage(c.getImage(), c.getX(), c.getY(), this);
            }
        }
    }
    
    //Logic Devreleri çiz
    private void drawCircuits(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;//Graphics2D obje
        
        /*Ekrandaki her logic devre elemanı için;
        -devrenin kendisini
        -devre üzerindeki bağlantı noktalarını
        -bağlantı noktalarının sahip olduğu line değişkenlerini
        ekrana çiz.
        */
        for(Component c : components)
        {
            if(c.isVisible())
            {
                g2d.drawImage(c.getImage(), c.getX()+(int)circuitPos.getX(), c.getY()+(int)circuitPos.getY(), this);
                
                for(Connection con : c.returnConnections())
                {
                    if(con!=null)
                    {
                        g2d.drawImage(con.getImage(), con.getX()+(int)circuitPos.getX(), con.getY()+(int)circuitPos.getY(), this);
                        
                        if(con.line!=null&&con.type==false)
                        {
                            //Çizgi çizimi
                            for(Line l : con.line)
                            {
                                for(int a = 0; a<l.getPoints().size()-1; a++)
                                {
                                    g2d.setColor(l.color);
                                    g2d.setStroke(new BasicStroke(3));
                                    g2d.drawLine(
                                    (int)l.getPoints().get(a).getX()+circuitPos.x,
                                    (int)l.getPoints().get(a).getY()+circuitPos.y,
                                    (int)l.getPoints().get(a+1).getX()+circuitPos.x,
                                    (int)l.getPoints().get(a+1).getY()+circuitPos.y);
                                }
                            }
                            //Nokta çizimi
                            for(Line l : con.line)
                            {
                                for(int a = 0; a<l.getPoints().size(); a++)
                                {
                                    g2d.setColor(new Color(200, 0, 255));
                                    g2d.setStroke(new BasicStroke(5));
                                    g2d.drawLine(
                                    (int)l.getPoints().get(a).getX()+circuitPos.x,
                                    (int)l.getPoints().get(a).getY()+circuitPos.y,
                                    (int)l.getPoints().get(a).getX()+circuitPos.x,
                                    (int)l.getPoints().get(a).getY()+circuitPos.y);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        
        g2d.setColor(new Color(200, 0, 255));//Çizgi rengini değiştir
        g2d.setStroke(new BasicStroke(3));//Çizgi kalınlığını değiştir
        
        //Çizim aktifken çizilen çigileri ekrana çiz
        for(int a = 0; a<points.size()-1; a++)
        {
            g2d.drawLine(
                    (int)(points.get(a).getX()+circuitPos.getX()),
                    (int)(points.get(a).getY()+circuitPos.getY()),
                    (int)(points.get(a+1).getX()+circuitPos.getX()),
                    (int)(points.get(a+1).getY()+circuitPos.getY()));
        }
        
        //Çizim yapılıyorken son noktadan mouse'a çizgi çiz
        if(drawing)
        {
            Point p = MouseInfo.getPointerInfo().getLocation();
            p.x = (int) ((int) (p.getX()-this.getLocationOnScreen().x+6));
            p.y = (int) (p.getY()-this.getLocationOnScreen().y)+6;
            p.x=((int)p.x/(width/div))*width/div;
            p.y=((int)p.y/(height/div*2)*2)*height/div;
            
            g2d.drawLine(points.get(points.size()-1).x+circuitPos.x, points.get(points.size()-1).y+circuitPos.y, p.x, p.y);
        }
    }
    
    //------###SÜRÜKLEME GÜNCELLE###
    private void updateDragging()
    {
        /*Her logic devre elemanı için;
        -sürükleme açıksa sürükle
        -bağlantı noktalarını güncelle
        -bağlantı noktalarının bağlantılarını düşür
        -bağlantı noktalarının çizgilerini temizle
        */
        for(Component c : components)
        {
            if(c.isDragging())
            {
                Point p = MouseInfo.getPointerInfo().getLocation();
                if(p.getX()-this.getLocationOnScreen().getX()>width/div*13){
                    if(p.getX()-this.getLocationOnScreen().getX()<width/div*96){
                        if(p.getY()-this.getLocationOnScreen().getY()>height/div*10){
                            if(p.getY()-this.getLocationOnScreen().getY()<height/div*90){
                                c.setX((int) (p.x-this.getLocationOnScreen().x-c.width/2-circuitPos.getX()));
                                c.setY((int) (p.y-this.getLocationOnScreen().y-c.height/2-circuitPos.getY()));
                                c.updateConnect();
                                
                                ArrayList<Connection> Acon = new ArrayList<Connection>();
                                Acon.addAll(c.returnConnections());
                                for(Connection con : Acon )
                                {
                                    if(con!=null&&con.line!=null)
                                    {
                                        if(con.type==true)
                                        {
                                            for(Connection conned : con.connecteds)
                                            {
                                                conned.line.clear();
                                            }
                                        }
                                        
                                        if(con.type==false)
                                        {
                                            for(Connection conned : con.connecteds)
                                            {
                                                Iterator<Line> iter = conned.line.iterator();
                                                while(iter.hasNext())
                                                {
                                                    Line line = iter.next();
                                                    if(conned.line.indexOf(line)<conned.line.size())
                                                    {
                                                        iter.remove();
                                                        conned.connected=false;
                                                    }
                                                }
                                            }
                                        }
                                        
                                        con.line.clear();
                                        con.connected=false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        //Toolbox itemleri sürüklemeleri aktifse sürükle
        for(Component c: tbItems)
        {
            if(c.isDragging())
            {
                Point p = MouseInfo.getPointerInfo().getLocation();
                c.setX((int) (p.x-this.getLocationOnScreen().x-c.width/2));
                c.setY((int) (p.y-this.getLocationOnScreen().y-c.height/2));
                c.updateConnect();
            }
        }
    }
    
    //------###TIMER SONUCU EKRANI YENIDEN ÇİZ###
    @Override
    public void actionPerformed(ActionEvent e) {repaint();}
    
    @Override
    public void mouseClicked(MouseEvent e) {}
    
    //------###MOUSE BASILDIĞINDA###
    @Override
    public void mousePressed(MouseEvent e) 
    {
        //Mouse sol tuşu basıldığında
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            //Simulate butonu basılduysa çıkışı hesapla
            if(simulate.isClicked(e))
            {
                CalculateOutput();
            }
            
            //renk tuşlarından birine basıldıysa çizgi rengini değiştir
            for(ColorPicker cp : colorButtons)
            {
                if(cp.isClicked(e))
                {
                    color = cp.getColor();
                }
            }
            
            //Kaydırma tuşuna basıldıysa ekranı kaydır
            for(ScrollButton s : sbcircuitlist)
            {
                if(s.isClicked(e))
                {
                    circuitPos.x += (int) s.direction.getX()*12;
                    circuitPos.y += (int) s.direction.getY()*12;
                }
            }
            
            //Logic devre elemanına basıldıysa elemanı kaydırmayı aktifleştir
            for(Component c : components)
            {
                if(c.isClicked(e))
                {
                    c.DragStart();
                }
            }
            
            //Tolbox elemanına basıldıysa elemanı kaydırmayı aktifleştir
            for(Component c : tbItems)
            {
                if(c.isClicked(e))
                {
                    c.DragStart();
                }
            }
            
            //Komponentin çıkış bağlantısına tıklandıysa
            for(Component c : components)
            {
                ArrayList<Connection> cons = c.returnConnections();
                for(Connection con : cons)
                {
                    if(con!=null&&con.isClicked(e))
                    {
                        /*Eğer ilk devre elemanı ise;
                        -noktayı ekle
                        -first değişkenine bu bağlantı noktasını at
                        -çizimi başat
                        */
                        if(!drawing&&con.connecting==false&&con.type==true)
                        {
                            points.add(new Point(con.getX()+con.width/2, con.getY()+con.height/2));
                            drawing=true;
                            first = con;
                            break;
                        
                        /*Eğer ikinci devre elemanının bağlantı noktasına tıklandıysa;
                        -kabloyu tamamla,
                        -bağlantıyı tamamla
                        -bağlanan değişkenleri sakla
                        -çizimi bitir
                        */
                        
                        } else if(drawing&&con.getType()==false&&con!=first&&con.connected==false&&con.code!=first.code){
                            Point p = MouseInfo.getPointerInfo().getLocation();
                            p.x = (int) (p.getX()-circuitPos.x-this.getLocationOnScreen().x);
                            p.y = (int) (p.getY()-circuitPos.y-this.getLocationOnScreen().y);
                            points.add(new Point((int) p.getX(), (int) p.getY()));
                            ArrayList<Point> point = new ArrayList<Point>();
                            point.addAll(points);
                            Line line = new Line(point, first, first.parent, color);
                            con.line.add(line);
                            con.connecteds.add(first);
                            con.connected=true;
                            first.line.add(line);
                            first.connecteds.add(con);
                            drawing=false;
                            points.clear();
                        }
                    }
                    
                    //eğer çizim aktifse ve bağlantıya henüz tıklanmadıysa çizim noktaları ekle (kareli alana hizala)
                    if(drawing)
                    {
                        Point p = MouseInfo.getPointerInfo().getLocation();
                        p.x = (int) ((int) (p.getX()-this.getLocationOnScreen().x+6));
                        p.y = (int) (p.getY()-this.getLocationOnScreen().y)+6;
                        p.x=((int)p.x/(width/div))*width/div;
                        p.y=((int)p.y/(height/div*2)*2)*height/div;
                        p.x-=circuitPos.x;
                        p.y-=circuitPos.y;
                        points.add(p);
                    }
                }
            }
        }
        
        //Sağ tık basılırsa
        if(e.getButton() == MouseEvent.BUTTON3)
        {
            //çizim aktifse çizimi bitir ve noktaları sil
            if(drawing)
            {
                points.clear();
                drawing=false;
                first=null;
            }
            
            //Componentin giriş bağlantısına sağ tıklandıysa girişe bağlı diğer bütün çıkış bağlantılarını ilk haline döndür
            for(Component c: components)
            {
                for(Connection con: c.returnConnections())
                {
                    if(con!=null&&con.line!=null)
                    {
                        if(con.isClicked(e))
                        {
                            if(con.type==false)
                            {
                                con.line.clear();
                                con.connecteds.clear();
                                con.connected=false;
                            }
                        }
                    }
                }
            }
            
            /*Komponente sağ tıklandıysa;
            -componenti sil
            -komponentin bağlantı noktalarını sil
            -bağlantı noktalarına tanımlı kabloları sil
            */
            Iterator<Component> iter = components.iterator();
            while(iter.hasNext())
            {
                Component c = new Component(0, 0, false);
                if((c=iter.next()).isClicked(e))
                {
                    ArrayList<Connection> Acon = new ArrayList<Connection>();
                    Acon.addAll(c.returnConnections());
                    for(Connection con : Acon)
                    {
                        if(con!=null&&con.line!=null)
                        {
                            if(con.type==true)
                            {
                                for(Connection conned : con.connecteds)
                                {
                                    conned.line.clear();
                                    conned.connecteds.clear();
                                    conned.connected=false;
                                }
                            }
                            if(con.type==false)
                            {
                                for(Connection conned : con.connecteds)
                                {
                                    for(Line line : con.line)
                                    {
                                        conned.line.remove(conned.line.indexOf(line)).reset();
                                    }
                                    conned.connecteds.clear();
                                    conned.connected= false;
                                }
                            }
                            con.line.clear();
                        }
                    }
                    iter.remove();
                    for(Component out : outputs)
                    {
                        out.reset();
                        out.Calculate();
                    }
                    
                    //Logic eleman outputsa output listesinden çıkar
                    if(c.getClassName().equals("simulator.Output"))
                    {
                        outputs.remove(c);
                    }
                }
            }
        }
    }

    //------###ÇIKIŞLARI HESAPLA###
    private void CalculateOutput()
    {
        resetOutput();//Çıkışları sıfırla
        
        /*Her çıkış için recursive olarak bağlı elemanlar bulunup başlangıçtaki input değeriyle geriye dönülür
        devre elemanlarının her birine en az bir input bağlı değilse kablolama hatası kabul edilir.
        */
        for(Component c : outputs)
        {
            for(Line l : c.in1.line)
            {
                ArrayList<Component> outway = new ArrayList<Component>();
                outway.add(c);
                
                int result = Calculate.recCalculate(c, outway);
                if(result==0)
                {
                    debugOutputs();
                }else{
                    System.out.println("Kablolama Hatası!");
                    for(Component out : outputs)
                    {
                        out.reset();
                        out.Calculate();
                    }
                }
            }
        }
    }
    
    //Çıkış bağlantıları sıfırlanır. Outputlar sıfırlanır.
    private void resetOutput()
    {
        for(Component c : components)
        {
            c.reset();
            if(c.getClassName().equals("simulator.Positive")||c.getClassName().equals("simulator.Negative"))
            {
                c.Calculate();
            }
        }
        
        for(Component c : outputs)
        {
            c.reset();
            c.Calculate();
        }
    }
    
    
    //------###LOGİC DEVRE ELEMANLARININ OUTPUTUNU YAZDIRMA###
    private void debugOutputs()
    {
        for(Component c : components)
        {
            if(c!=null &&c.out!=null)
            {
                System.out.println(c.code +" "+c.getClassName()+" "+c.out.value);
            }
        }
        
        for(Component c : outputs)
        {
            if(c!=null&&c.in1!=null)
            {
                System.out.println(c.code +" "+c.getClassName()+" "+c.in1.value);
            }
        }
    }
    
    //------###MOUSE BASMASI KALDIRILDIĞINDA###
    @Override
    public void mouseReleased(MouseEvent e) 
    {
        //Sol tık kaldırıldığında
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            //Sürüklemeyi durdur
            for(Component c : components)
            {
                if(c.isClicked(e))
                {
                    c.DragStop();
                }
            }
            
            /*Eğer toolbox elemanına tıklanıyorsa;
            -eleman çizim ekranı sınırları içerisindeyken serbest 
            bırakıldığında elemanın aynı classına sahip bir obje
            oluşturulup components dizisine ekleniyor.
            */
            for(Component c : tbItems)
            {
                if(c.isClicked(e))
                {
                    c.DragStop();
                    c.resetPosition();
                    try {
                        Point p = MouseInfo.getPointerInfo().getLocation();
                        if(p.getX()-this.getLocationOnScreen().getX()>width/div*13){
                            if(p.getX()-this.getLocationOnScreen().getX()<width/div*96){
                                if(p.getY()-this.getLocationOnScreen().getY()>height/div*10){
                                    if(p.getY()-this.getLocationOnScreen().getY()<height/div*90){
                                        p.x=(int) (p.getX()-this.getLocationOnScreen().x-circuitPos.x);
                                        p.y=(int) (p.getY()-this.getLocationOnScreen().y-circuitPos.y);
                                        
                                        Class<?> cl = Class.forName(c.getClassName());//Class'ın adı ile class tanımlama
                                        Constructor<?> cons = cl.getConstructors()[0];//Objeyi class constructoru ile initialise etme
                                        Object object = cons.newInstance(new Object[]{(int)p.getX(), (int)p.getY(), false});//Devre elemanı olan objeyi oluşturma
                                        
                                        components.add((Component) object);
                                        if("simulator.Output".equals(c.getClassName()))
                                        {
                                            outputs.add((Component) object);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (ClassNotFoundException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
                        Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}