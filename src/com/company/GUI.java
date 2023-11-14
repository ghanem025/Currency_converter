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

public class GUI{
    double amount, dollar;
    public GUI() {
        // Create hashmap to store different currencies
        HashMap<String, Integer> currencies = setStringIntegerHashMap();

        JFrame frame = new JFrame("Currency Converter");//create frame
        JPanel panel = new JPanel();

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setLayout(null);

        Set<String> keySet = currencies.keySet();//we want to isolate the Strings in the hashmap, so we make a set of strings using a hashmap keyset
        String [] country  = new String[keySet.size()];
        keySet.toArray(country);

        JTextField numberField = new JTextField();//text-field to enter the amount of money to convert
        numberField.setBounds(70, 70, 90, 20);
        panel.add(numberField);

        JComboBox cb = new JComboBox(country);//jCombobox takes an array of strings and creates a drop-down using those values.
        cb.setBounds(160, 70, 90, 20);
        panel.add(cb);

        JComboBox cb2 = new JComboBox(country);
        cb2.setBounds(265, 70, 90, 20);
        panel.add(cb2);

        JButton swpBtn = new JButton("Swap");//create button to convert the values
        swpBtn.setBounds(200, 200, 100, 30);//set position and width/height for the buttons
        panel.add(swpBtn);

        JButton convertBtn = new JButton("Convert");//create button to convert the values
        convertBtn.setBounds(30, 200, 100, 30);//set position and width/height for the buttons
        panel.add(convertBtn);

        JLabel label = new JLabel("Currency");
        label.setBounds(10, 65, 80, 25);
        panel.add(label);

        JLabel converted = new JLabel("Here is your converted currency: ");
        converted.setBounds(70, 100, 1920, 40);
        panel.add(converted);

        JLabel label2 = new JLabel("to");
        label2.setBounds(252, 65, 80, 25);
        panel.add(label2);

        swap(cb, cb2, swpBtn);
        convert(cb, cb2, converted, convertBtn, numberField);
        frame.setVisible(true);
    }

    private HashMap<String, Integer> setStringIntegerHashMap() {
        HashMap<String, Integer> currencies = new HashMap<String, Integer>();

        currencies.put("USD", 1);//each currency is mapped to a number
        currencies.put("CAD", 2);
        currencies.put("EUR", 3);
        currencies.put("GBP", 4);
        currencies.put("INR", 5);
        currencies.put("JPY", 6);
        currencies.put("AUD", 7);
        currencies.put("JOD", 8);
        currencies.put("KRW",9);
        currencies.put("CHF", 10);
        currencies.put("NZD", 11);
        return currencies;
    }

    private String getRequest(String fromcode, String toCode, double amount) throws IOException {
        DecimalFormat f = new DecimalFormat("##.##");
        String GET_URL = "https://free.currconv.com/api/v7/convert?q=" + fromcode + "_" + toCode + "&compact=ultra&apiKey=7fd79f413621cbd201fe";
        URL url = new URL(GET_URL); // set our url variable to the given URL above.
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); // open connection to receive requests
        httpURLConnection.setRequestMethod("GET"); // we want a GET request to get all the up-to-date rates of currencies
        int code = httpURLConnection.getResponseCode(); // this variable will get a response code from the GET request
        if (code == HttpURLConnection.HTTP_OK) { // if the response code is accepted, we can calculate the currency conversion
            Double exchange = getaDouble(fromcode, toCode, httpURLConnection);

            return f.format(amount * exchange) + " " + toCode;//return the exchange rate to the caller.
        } else {
            return "Error could not convert.";
        }
    }

    private Double getaDouble(String fromcode, String toCode, HttpURLConnection httpURLConnection) throws IOException {
        BufferedReader in = new BufferedReader((new InputStreamReader(httpURLConnection.getInputStream())));// read the file that was recieved from our GET request
        //this file should contain all the up-to-date exchange rates
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = in.readLine()) != null) {// read all the lines in the bufferedReader until you reach the end (reach null)
            response.append(line);
        }
        in.close();
        JSONObject obj = new JSONObject(response.toString()); // create an JSONObject to store our conversion.
        return obj.getDouble(fromcode + "_" + toCode);
    }

    public void swap(JComboBox cb, JComboBox cb2, JButton swpBtn){
        swpBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Object temp = cb.getSelectedItem();// create temp variable to swap the two boxes
                cb.setSelectedItem(cb2.getSelectedItem()); //replace box 1 with box 2 item
                cb2.setSelectedItem(temp); //replace box 2 with box 1 item
            }
        });
    }

    public void convert(JComboBox cb, JComboBox cb2, JLabel converted, JButton button, JTextField numberField) {
        button.addActionListener(new ActionListener(){//add action listener
            @Override
            public void actionPerformed (ActionEvent e){//when the convert button is pressed
                if (numberField.getText().isEmpty()) {//check if the box is empty, if it is then send an error message to the user
                    windowError();
                } else {
                    try {
                        Integer.parseInt(numberField.getText());
                        String box1, box2;
                        box1 = (String) cb.getSelectedItem(); // set a box variable to get the item in the first box (ex USA,JPY etc.)
                        System.out.println(box1);
                        box2 = (String) cb2.getSelectedItem();
                        amount = Integer.parseInt(numberField.getText());
                        try {//check if the amount entered is a number/valid input
                            converted.setText("Here is your converted currency: " + getRequest(box1, box2, amount));//calculate the conversion
                            //by calling GETrequest function
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } catch (
                            NumberFormatException f)// if it's not a valid amount we give an error message and reset the text field
                    {
                        numberField.setText("");
                        windowError2();
                    }
                }
            }
        });
    }
    private void windowError(){// pop up windowerror
        JFrame frame = new JFrame("Name Error");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "Please enter the amount you would like to convert", "Empty amount", JOptionPane.WARNING_MESSAGE);
        //this is our Optional panel that will pop up with our message.
    }
    private void windowError2(){
        JFrame frame = new JFrame("Name Error");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "Please enter a valid input (letters are not a valid input)", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        //this is our Optional panel that will pop up with our message.
    }
}
