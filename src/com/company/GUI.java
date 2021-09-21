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
        HashMap<String, Integer> currencies = new HashMap<String, Integer>();
        currencies.put("USD", 1);
        currencies.put("CAD", 2);
        currencies.put("EUR", 3);
        currencies.put("GBP", 4);
        currencies.put("INR", 5);
        currencies.put("JPY", 6);

        JFrame frame = new JFrame("Frame");
        JPanel panel = new JPanel();

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setLayout(null);

        JButton button = new JButton("Convert");
        button.addActionListener(this);
        button.setBounds(30, 200, 100, 30);
        panel.add(button);

        money = new JTextField();
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

        Set<String> keySet = currencies.keySet();
        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);
        country = GetStringArray(listOfKeys);

        cb = new JComboBox(country);
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

    private static String sendHttpGETrequest(String fromcode, String toCode, double amount) throws IOException {
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
            System.out.println(obj);
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
    public void actionPerformed(ActionEvent e) {
        if (money.getText().equals("")) {
            Windowerror();
        }
        else {
            try
            {
                Integer.parseInt(money.getText());
                box1 = (String) cb.getSelectedItem();
                box2 = (String) cb2.getSelectedItem();
                amount = Integer.parseInt(money.getText());
                try {
                    converted.setText("Here is your converted currency: " + sendHttpGETrequest(box1, box2, amount));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            catch (NumberFormatException f)
            {
                money.setText("");
                stupidWindowerror();
            }

        }
    }
    private void Windowerror(){
        JFrame frame = new JFrame("Name Error");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "homie are you broke or something??Cause if you are then you shouldn't be using this app", "Error", JOptionPane.WARNING_MESSAGE);
    }
    private void stupidWindowerror(){
        JFrame frame = new JFrame("Name Error");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "you must be special, this is a fucking currency converter not your english essay put a fucking number in", "Error", JOptionPane.WARNING_MESSAGE);

    }

}
