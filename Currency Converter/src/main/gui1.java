package main;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class gui1 implements ActionListener {
    JButton convertButton = new JButton("Convert");
    JButton resetButton = new JButton("Reset");
    String[] currencies = { "USD", "CAD", "EUR", "HKD", "INR"};
    JTextField amount = new JTextField();
    JLabel heading = new JLabel("WELCOME TO CURRENCY CONVERTER");
    JLabel amountUser = new JLabel("ENTER AMOUNT : ");
    JLabel from = new JLabel("FROM CURRENCY : ");
    JLabel to = new JLabel("TO CURRENCY : ");
    final JComboBox<String> cbFrom = new JComboBox<String>(currencies);
    final JComboBox<String> cbTo = new JComboBox<String>(currencies);
    JFrame frame = new JFrame();
    JLabel l1 = new JLabel("ENTER DATA AND PRESS CONVERT !!!");
    JLabel l2 = new JLabel(" ");

    public void gui(){
        l1.setBounds(160,380,300,25);
        frame.add(l1);

        l2.setBounds(160,420,300,25);
        frame.add(l2);
        heading.setBounds(150,10,300,40);

        amountUser.setBounds(50,100,200,25);
        from.setBounds(50,150,200,25);
        to.setBounds(50,200,200,25);

        convertButton.setBounds(150,250,100,50);
        convertButton.addActionListener(this::actionPerformed);

        resetButton.setBounds(300,250,100,50);
        resetButton.addActionListener(this::actionPerformed);

        amount.setBounds(300,100,200,25);
        cbFrom.setBounds(300,150,200,25);
        cbTo.setBounds(300,200,200,25);
        frame.add(convertButton);
        frame.add(resetButton);

        frame.add(heading);

        frame.add(amountUser);
        frame.add(from);
        frame.add(to);

        frame.add(amount);
        frame.add(cbFrom);
        frame.add(cbTo);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,500);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private static double sendHttpGETRequest(String fromCode, String toCode, double amount) throws IOException {
        String GET_URL = "https://freecurrencyapi.net/api/v2/latest?apikey=7ad31140-4d1c-11ec-8245-f79d72fb9915&base_currency=" + fromCode;
        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK){//success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }in.close();

            JSONObject obj = new JSONObject(response.toString());
            Double exchangeRate = obj.getJSONObject("data").getDouble(toCode);
            System.out.println("");
            //System.out.println(amount +" "+ fromCode + " = " + amount*exchangeRate +" "+ toCode);
            return amount*exchangeRate;
        }
        else{
            System.out.println("GET request failed");
        }
        return amount;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton){
            amount.setText("");
        }

        if (e.getSource() == convertButton){
            String amountEntered = amount.getText();
            double amountentered = Double.parseDouble(amountEntered);

            try {
                double a = sendHttpGETRequest((String) cbFrom.getSelectedItem(), (String) cbTo.getSelectedItem(), amountentered);

                l2.setText(amountEntered + " "+ (String) cbFrom.getSelectedItem()+" = "+a+" " +cbTo.getSelectedItem());
                l1.setText("1 "+(String) cbFrom.getSelectedItem() +" = "+a/amountentered+" " +cbTo.getSelectedItem());

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }

        
    }
}
