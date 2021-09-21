package com.company;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;


public class GUI implements ActionListener {
    private JTextField money;
    private JTextField money2;
    String country[];
    JComboBox cb;
    JComboBox cb2;
    double amount, dollar;
    JLabel converted;
    String box1;
    String box2;
    int i;
    DecimalFormat f = new DecimalFormat("##.##");

    public GUI() {
        HashMap<String, Integer> currencies = new HashMap<String, Integer>();//create hashmap to store different currncies
        currencies.put("USD", 1);
        currencies.put("CAD", 2);
        currencies.put("EUR", 3);
        currencies.put("GBP", 4);
        currencies.put("INR", 5);
        currencies.put("JPY", 6);

        JFrame frame = new JFrame("Frame");//create Jframe
        JPanel panel = new JPanel();

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setLayout(null);

        JButton button = new JButton("Convert");//create button to convert the values
        button.addActionListener(this);//add actionlistener
        button.setBounds(30, 200, 100, 30);//set position and width/height for the buttons
        panel.add(button);

        money = new JTextField();//textfield to enter the amount of money to convert
        money.setBounds(70, 70, 90, 20);
        panel.add(money);

        JLabel label = new JLabel("Currency");
        label.setBounds(10, 65, 80, 25);
        panel.add(label);

        converted = new JLabel("Here is your converted currency: ");
        converted.setBounds(70, 100, 1920, 40);
        panel.add(converted);

        JLabel label2 = new JLabel("to");
        label2.setBounds(252, 65, 80, 25);
        panel.add(label2);

        Set<String> keySet = currencies.keySet();//we want to isolate the Strings in the hashmap, so we make a set of strings
        //using a hashmap keyset
        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);// add the key set to a string arraylist
        country = GetStringArray(listOfKeys);//turn arraylist into an array of strings for the JComboBox

        cb = new JComboBox(country);//jCombobox takes an array of strings and creates a drop down using those values.
        cb.setBounds(160, 70, 90, 20);
        panel.add(cb);

        cb2 = new JComboBox(country);
        cb2.setBounds(265, 70, 90, 20);
        panel.add(cb2);

        frame.setVisible(true);
    }

    public static String[] GetStringArray(ArrayList<String> arr) {
        String str[] = new String[arr.size()]; //create an array with the same size of our given arraylist
        Object[] objArr = arr.toArray(); //convert arraylist into an object
        int i = 0;
        for (Object obj : objArr) {
            str[i++] = (String) obj;
        }
        return str;
    }

    private static String GETrequest(String fromcode, String toCode, double amount) throws IOException {
        DecimalFormat f = new DecimalFormat("##.##");
        String GET_URL = "https://free.currconv.com/api/v7/convert?q=" + fromcode + "_" + toCode + "&compact=ultra&apiKey=7fd79f413621cbd201fe";
        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int code = httpURLConnection.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader((new InputStreamReader(httpURLConnection.getInputStream())));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            JSONObject obj = new JSONObject(response.toString());
            Double exchange = obj.getDouble(fromcode + "_" + toCode);

            return f.format(amount * exchange) + " " + toCode;
        } else {
            return "no work sadge";
        }
    }

    public static void main(String[] args) throws IOException {
        new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {//when the convert button is pressed
        if (money.getText().equals("")) {//check if the box is empty, if it is then send an error message to the user
            Windowerror();
        }
        else {
            try
            {
                Integer.parseInt(money.getText());
                box1 = (String) cb.getSelectedItem();
                box2 = (String) cb2.getSelectedItem();
                amount = Integer.parseInt(money.getText());
                try {//check if the amount entered is a number/valid input
                    converted.setText("Here is your converted currency: " + GETrequest(box1, box2, amount));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            catch (NumberFormatException f)// if it's not a valid amount we give an error message and reset the text field
            {
                money.setText("");
                stupidWindowerror();
            }
        }
    }
    private void Windowerror(){// pop up windowerror
        JFrame frame = new JFrame("Name Error");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "Please enter the amount you would like to convert", "Empty amount", JOptionPane.WARNING_MESSAGE);
    }
    private void stupidWindowerror(){
        JFrame frame = new JFrame("Name Error");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "Please enter a valid input (letters are not a valid input)", "Invalid Input", JOptionPane.WARNING_MESSAGE);

    }

}
