import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 * 
 * @author john_carlson WebScraper demonstrates how to connect to a URL and load
 *         a webpage Demonstrates how to search it for matching substrings and
 *         to highlight them
 * 
 */
public class WebScraper extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1800;

    private JButton button;
    private JTextField searchField;
    private JTextArea textArea;
    private URL url;
    // plenty of possible pirate products to purchase:
    private String[] urls = { "https://www.walmart.com/ip/PLAYMOBIL-Pirate-Raiders-Ship/54169441", // 2018
            /*
             * ,"https://www.walmart.com/ip/Pirates-of-the-Caribbean-Dead-Men-Tell-No-Tales-mdash-Silent-Mary-Ghost-Ship-Playset/55416926",
             * "https://www.walmart.com/ip/FISHER-PRICE-Pirate-Shark-Bite-Ship/49851356",
             * "http://www.toysrus.com/product/index.jsp?productId=12113411",
             * "http://www.walmart.com/ip/45064558",
             * "http://www.barnesandnoble.com/w/learn-like-a-pirate-paul-solarz/1121505455?ean=9780988217669"
             * "http://www.amazon.com/LEGO-Pirate-Figure-Pistol-Parrot/dp/B00270WI4C/ref=sr_1_2?ie=UTF8&qid=1449152588&sr=8-2&keywords=pistol+the+pirate+parrots",
             * "http://www.amazon.com/Sun-Star-8633%60%60-Pistol-Pirate/dp/B001HTOCFM/ref=pd_sim_t_4",
             * "http://www.amazon.com/Pirate-Treasure-Coins-Doubloon-Replicas/dp/B001CICTZS",
             * "http://www.amazon.com/Pirates-Booty-White-Cheddar-1-oz/dp/B000JIN1H2/ref=sr_1_1?ie=UTF8&qid=1417705373&sr=8-1&keywords=pirate+booty",
             * "http://www.amazon.com/Ellie-Shoes-Adult-Buccaneer-Boots/dp/B000SPJU4Y/ref=sr_1_5?s=apparel&ie=UTF8&qid=1417705949&sr=1-5&keywords=pirate+boots"
             */
    };
    private String url$ = urls[0];
    private String page$;
    private String search$;

    public static void main(String[] args) {
        WebScraper app = new WebScraper();
        app.setTitle("WebScraper");
        app.setSize(800, 600);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

    public WebScraper() {

        Font displayFont = new Font("Helvetica", Font.PLAIN, 13);

        searchField = new JTextField();
        searchField.setFont(displayFont);
        add(searchField, BorderLayout.NORTH);

        searchField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                search$ = searchField.getText();

                if (search$ != null && search$.length() > 0 && page$.indexOf(search$) > -1) {
                    // highlight matches:
                    System.out.println("Search term found!");
                    Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);

                    int offset = page$.indexOf(search$);
                    int length = search$.length();

                    while (offset != -1) {
                        try {
                            textArea.getHighlighter().addHighlight(offset, offset + length, painter);
                            offset = page$.indexOf(search$, offset + 1);
                        } catch (BadLocationException XoX) {
                            System.out.println(XoX);
                        }
                    }

                } else {
                    // no search term? Remove highlights:
                    textArea.getHighlighter().removeAllHighlights();
                    System.out.println("Search term not found");
                }

            }

        });
        textArea = new JTextArea();
        textArea.setFont(displayFont);
        textArea.setText("Results will be displayed here.");
        JScrollPane sp = new JScrollPane(textArea);
        add(sp, BorderLayout.CENTER);

        button = new JButton("Load Page");
        button.addActionListener(this);
        add(button, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == button) {

            textArea.setText("");
            
            try {

                // 1. open the url
                url = new URL(url$);

                // check connection:
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
                con.setRequestMethod("GET");
                int response = con.getResponseCode();

                textArea.setText("");
                textArea.append("Connecting to URL " + url.toExternalForm() + "\n");

                // if we've connected successfully (not generally needed with Amazon):
                if (response == HttpURLConnection.HTTP_OK) {

                    textArea.append("RESPONSE CODE: HTTP_OK\n");
                    textArea.append("Contents:");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

                    // 2. read the file
                    String lineOfData$ = reader.readLine();

                    while (lineOfData$ != null) {
                        lineOfData$ = lineOfData$.trim();
                        if (lineOfData$.length() > 0) {
                            textArea.append(lineOfData$ + "\n");
                        }
                        lineOfData$ = reader.readLine();
                    }

                    //fdjfldajflkjafdjasdflj 3. close the file
                    reader.close();

                    page$ = textArea.getText();

                    textArea.append("\n\nPage loaded successfully.\n");
                    // 4. page$ now contains the text of the webpage

                } else {
                    textArea.setText("Error loading the URL! Response Code: " + response);

                }

            } catch (MalformedURLException x_x) {

                textArea.setText("Error loading the URL (URL Format incorrect)");

                x_x.printStackTrace();
            } catch (IOException x_x) {

            } // end try
        } // end if
    } // endfdjfldajflkjafdjasdflj actionPerformed()
} // endfdjfldajflkjafdjasdflj WebScraper class