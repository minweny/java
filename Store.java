
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.util.*;

public class Store extends JFrame {
//    create a jtable, put it in scrollpane
    DefaultTableModel data=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JTable table=new JTable(data);
    JScrollPane jScrollPane=new JScrollPane(table);

//    use a list to contain all of the web ads
    ArrayList<WebAd> cart=new ArrayList<>();

//    create buttons
    JButton addWebAd=new JButton("Add WebAd");
    JButton removeWebAd=new JButton("Remove Selected Ad");
    JButton showStats=new JButton("Show Stats");
    JButton viewAd=new JButton("View Selected Ad");
    JButton printAd=new JButton("Print Selected Ad");
    JButton removeAll=new JButton("Remove All");

//    store information about webAd price stats
    BigDecimal low,high,avg,sum=new BigDecimal("0");

//    jlabel to store the total price
    JLabel total=new JLabel("$0.00",SwingConstants.CENTER);

//    we put buttons panel in south panel, so when you resize the window, the bottom widgets will always be centered
    JPanel southPanel=new JPanel(new FlowLayout(FlowLayout.CENTER));

//    jpanel to put six buttons and one label
    JPanel buttonsPanel=new JPanel();
//    we use group layout to precisely control the location and gap of each button
    GroupLayout layout=new GroupLayout(buttonsPanel);

//    use numberformat to format bigdecimal
    static NumberFormat currencyFormat=NumberFormat.getCurrencyInstance(Locale.US);

//    use jFrame to show the window of "view selected ad"
    JFrame jFrame;

//    sound effects for buttons
    File file;
    AudioInputStream audioInputStream;
    Clip clip;

//    when web ad changes, we update the jtable
    void updateTable(){
//        clear previous jtable data
        data.setRowCount(0);

        sum=new BigDecimal("0");

//        get low price, high price and avg price
        for(int i=0;i<cart.size();i++){
            WebAd webAd=cart.get(i);
            if(i==0){
                low=new BigDecimal(webAd.getAdjustedPrice().toPlainString());
                high=new BigDecimal(webAd.getAdjustedPrice().toPlainString());
                sum=new BigDecimal(webAd.getAdjustedPrice().toPlainString());
            }else {
                if(low.compareTo(webAd.getAdjustedPrice())>0) low=new BigDecimal(webAd.getAdjustedPrice().toPlainString());
                if(high.compareTo(webAd.getAdjustedPrice())<0) high=new BigDecimal(webAd.getAdjustedPrice().toPlainString());
                sum=sum.add(webAd.getAdjustedPrice());
            }
            data.addRow(webAd.getTableRow());
        }
        if(cart.size()>0) avg=sum.divide(new BigDecimal(cart.size()+""), MathContext.DECIMAL32);
//        update new price to jlabel
        total.setText(currencyFormat.format(sum));
    }

    Store(){
//        basic information about top level jframe
        super("My WebAd Store");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        set titel for jtable
        data.setColumnIdentifiers(new String[]{"WebAd Price","Discount","Tax","Total"});

//        put scrollpane in the center
        add(jScrollPane,BorderLayout.CENTER);

        buttonsPanel.setLayout(layout);

//        create style for the total price label
        total.setPreferredSize(new Dimension(200, 100));
        total.setBackground(Color.black);
        total.setForeground(Color.green);
        total.setOpaque(true);
        total.setFont(new Font("Courier", Font.BOLD, 30));

//        we use group layout to bind the size of all buttons, so we just need to set size for a random button here
        addWebAd.setPreferredSize(new Dimension(150, 50 ));

//        create gaps for the group layout
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

//        listener for "add webAd"
        addWebAd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                cart.add(new WebAd());
                updateTable();
//              use exception handling, so the program can run successfully even if you lose those attached sound files
                try {
                    file=new File("cling_1.wav");
                    audioInputStream= AudioSystem.getAudioInputStream(file);
                    clip=AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.setMicrosecondPosition(0);
                    clip.start();
                }catch (Exception ex){
                    System.out.println(ex);
                }
            }
        });

//        listener for "show stats"
        showStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//              use exception handling, so the program can run successfully even if you lose those attached sound files
                try {
                    file=new File("camera-focus-1.wav");
                    audioInputStream= AudioSystem.getAudioInputStream(file);
                    clip=AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.setMicrosecondPosition(0);
                    clip.start();
                }catch (Exception ex){
                    System.out.println(ex);
                }

                if(cart.size()==0){
                    JOptionPane.showMessageDialog(null, "No web ads.","Error",JOptionPane.ERROR_MESSAGE);
                }else {
                    String message="Low Price = "+currencyFormat.format(low)+"\nHigh Price = "+currencyFormat.format(high)+"\nAvg Price = "+currencyFormat.format(avg);
                    JOptionPane.showMessageDialog(null, message,"Summary Statistics",JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

//        listener for "remove selected ad"
        removeWebAd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//              use exception handling, so the program can run successfully even if you lose those attached sound files
                try {
                    file=new File("glass_breaking_1.wav");
                    audioInputStream= AudioSystem.getAudioInputStream(file);
                    clip=AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.setMicrosecondPosition(0);
                    clip.start();
                }catch (Exception ex){
                    System.out.println(ex);
                }

                int idx=table.getSelectedRow();
                if(idx>-1){
                    cart.remove(idx);
                    updateTable();
                }else {
                    JOptionPane.showMessageDialog(null, "No selected web ad.","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

//        listener for "print selected ad"
        printAd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
//              use exception handling, so the program can run successfully even if you lose those attached sound files
                try {
                    file=new File("coin_1.wav");
                    audioInputStream= AudioSystem.getAudioInputStream(file);
                    clip=AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.setMicrosecondPosition(0);
                    clip.start();
                }catch (Exception ex){
                    System.out.println(ex);
                }

                int idx=table.getSelectedRow();
                if(idx>-1){
                    PrinterJob printerJob=PrinterJob.getPrinterJob();
                    printerJob.setPrintable(cart.get(idx));
                    if(printerJob.printDialog()){
                        try {
                            printerJob.print();
                        }catch (PrinterException exc){
                            System.out.println("Printer dead"+exc);
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "No selected web ad.","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

//        listener for "view selected ad"
        viewAd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//              use exception handling, so the program can run successfully even if you lose those attached sound files
                try {
                    file=new File("cling_2.wav");
                    audioInputStream= AudioSystem.getAudioInputStream(file);
                    clip=AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.setMicrosecondPosition(0);
                    clip.start();
                }catch (Exception ex){
                    System.out.println(ex);
                }

                int idx=table.getSelectedRow();
                if(idx>-1){
                    jFrame=new JFrame();
                    jFrame.setTitle("WebAd "+(idx+1));
                    jFrame.add(cart.get(idx),BorderLayout.CENTER);
                    jFrame.pack();
                    jFrame.setVisible(true);
                    jFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                }else {
                    JOptionPane.showMessageDialog(null, "No selected web ad.","Error",JOptionPane.ERROR_MESSAGE);
                }

            }
        });

//        listener for "remove all"
        removeAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//              use exception handling, so the program can run successfully even if you lose those attached sound files
                try {
                    file=new File("zipper_bag_2.wav");
                    audioInputStream= AudioSystem.getAudioInputStream(file);
                    clip=AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.setMicrosecondPosition(0);
                    clip.start();
                }catch (Exception ex){
                    System.out.println(ex);
                }

//                clear the data in web ad list
                cart.clear();
                updateTable();
            }
        });

//        set group layout for buttons and label
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                                .addComponent(addWebAd)
                                .addComponent(viewAd)
                        )
                        .addGroup(layout.createParallelGroup()
                                .addComponent(removeWebAd)
                                .addComponent(printAd)
                        )
                        .addGroup(layout.createParallelGroup()
                                .addComponent(showStats)
                                .addComponent(removeAll)
                        )
                        .addComponent(total,0, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE)

        );

//        we make the size of all of the buttons exactly the same
        layout.linkSize(addWebAd,removeWebAd,showStats,viewAd,printAd,removeAll);

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup()
                                                        .addComponent(addWebAd)
                                                        .addComponent(removeWebAd)
                                                        .addComponent(showStats)
                                                )
                                                .addGroup(layout.createParallelGroup()
                                                        .addComponent(viewAd)
                                                        .addComponent(printAd)
                                                        .addComponent(removeAll)
                                                )

                                        )
                                        .addComponent(total,0, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                )

        );
        southPanel.add(buttonsPanel);
        add(southPanel,BorderLayout.SOUTH);
        setVisible(true);
    }

    public static void main(String[] args) {
        Store app=new Store();
    }
}

class WebAd extends JPanel implements Printable {
    Random random=new Random();

    static NumberFormat percentageFormat=NumberFormat.getPercentInstance();
    static NumberFormat percentageFormatWithFraction=NumberFormat.getPercentInstance();
    static NumberFormat currencyFormat=NumberFormat.getCurrencyInstance(Locale.US);

    private static BigDecimal taxRate=new BigDecimal("0.0725");

    //set the initial position of the phone structure, so we can move it easily
    int xPosOfPhonePic=200;
    int yPosOfPhonePic=50;
    String taglineText="Better Protection!";//initial tagline information

    int currentID;//product number

    Color phoneFrontPane=new Color( 0, 0, 0, 255);//set phone front pane color to black
    Color bg=new Color( 255, 204, 204, 255);//set Ad panel background color to pink

    public static BigDecimal getTaxRate() {
        return taxRate;
    }

    public static void setTaxRate(BigDecimal taxRate) {
        WebAd.taxRate = taxRate;
    }

    private BigDecimal unitPrice=new BigDecimal(random.nextInt(100)+"."+random.nextInt(100));
    private BigDecimal discountRate=new BigDecimal("0."+random.nextInt(5)+random.nextInt(10));

//    public BigDecimal getUnitPrice() {
//        return unitPrice;
//    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getAdjustedPrice() {
        return adjustedPrice;
    }

    public void setAdjustedPrice(BigDecimal adjustedPrice) {
        this.adjustedPrice = adjustedPrice;
    }

    private BigDecimal adjustedPrice=unitPrice.multiply(new BigDecimal("1").subtract(discountRate)).multiply(new BigDecimal("1").add(taxRate));

    WebAd(){
        percentageFormatWithFraction.setMinimumFractionDigits(2);
        currentID=random.nextInt(1000)+1000;
//        set the size, so we can view or print it easily
        setSize(320, 320);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(320, 320);
    }

//    use this method to populate jtable
    String[] getTableRow(){
        return new String[]{currencyFormat.format(unitPrice),percentageFormat.format(discountRate),percentageFormatWithFraction.format(taxRate.doubleValue()),currencyFormat.format(adjustedPrice)};
    }

    @Override
    public void paintComponent(Graphics g1d) {
        super.paintComponent(g1d);
        Graphics2D g = (Graphics2D) g1d;
//set background color to pink
        g.setColor(bg);
        g.fillRect(0, 0, 320, 320);
//draw title
        g.setColor( new Color( 0, 0, 0, 255) );

        g.setFont( new Font("Monospaced", 1, 20) );
        g.drawString("Smart Phone Case Company", 0, 20);
//draw the phone
        g.setColor( new Color(139, 139, 139, 255) );
        g.fillRoundRect(xPosOfPhonePic-7, yPosOfPhonePic-7, 100+14, 150+14, 15, 15);
//draw phone silent, volume up and down, and close screen buttons
        g.fillRoundRect(xPosOfPhonePic-8, yPosOfPhonePic+10, 1, 10, 1, 1);
        g.fillRoundRect(xPosOfPhonePic-8, yPosOfPhonePic+30, 1, 20, 1, 1);
        g.fillRoundRect(xPosOfPhonePic-8, yPosOfPhonePic+55, 1, 20, 1, 1);

        g.fillRoundRect(xPosOfPhonePic+100+7, yPosOfPhonePic+30, 2, 20, 0, 0);
//draw the phone front pane
        g.setColor( new Color(211, 211, 211, 255) );
        g.fillRoundRect(xPosOfPhonePic-5, yPosOfPhonePic-5, 100+10, 150+10, 15, 15);

        g.setColor(phoneFrontPane);
        g.fillRoundRect(xPosOfPhonePic, yPosOfPhonePic, 100, 150, 15, 15);

        g.setColor( new Color(211, 211, 211, 255) );
        g.fillRoundRect(xPosOfPhonePic+20, yPosOfPhonePic, 60, 12, 15, 15);
        g.fillRect(xPosOfPhonePic+20, yPosOfPhonePic, 60, 6);
//draw the phone voice and light listener
        g.setColor( new Color( 0, 0, 0, 255) );
        g.fillRoundRect(xPosOfPhonePic+40-4, yPosOfPhonePic+3, 20, 3, 0, 0);
        g.fillOval(xPosOfPhonePic+40-4+20+4, yPosOfPhonePic+2, 5, 5);

        g.setColor( new Color( 255, 255, 255, 255) );
        g.drawLine(xPosOfPhonePic+5, yPosOfPhonePic+145, xPosOfPhonePic+95, yPosOfPhonePic+5);

        g.setColor( new Color( 0, 0, 0, 255) );
        g.fillRect(xPosOfPhonePic+20, yPosOfPhonePic+60, 60, 30);
        g.setColor( new Color( 255, 255, 255, 255) );
        g.setFont( new Font("Monospaced", 0, 15) );
        g.drawString("6.1-inch", xPosOfPhonePic+20, yPosOfPhonePic+80);
//draw other texts
        g.setColor( new Color( 0, 0, 0, 255) );
        g.setFont( new Font("Monospaced", 2, 15) );
        g.drawString(taglineText, xPosOfPhonePic-80, yPosOfPhonePic+180);
        g.drawString("Product No."+currentID, 5, 100);

        g.drawString("Only "+currencyFormat.format(adjustedPrice), 10, 150);
        g.drawString("(Adjusted Price)", 10, 170);
        g.setFont( new Font("Monospaced", 0, 15) );
        g.drawString("The fictitious phone case company", 10, 290);
        g.drawString("www.smartphonecase.com", 10, 310);
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        System.out.println("printing");
        if(pageIndex>0){
            return NO_SUCH_PAGE;
        }
        Graphics2D graphics2D=(Graphics2D)graphics;
//        change the printing margin
        graphics2D.translate(pageFormat.getImageableX()+50, pageFormat.getImageableY()+50);
        paint(graphics2D);
        return PAGE_EXISTS;
    }
}
