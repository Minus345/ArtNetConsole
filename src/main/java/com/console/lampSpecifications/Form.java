package com.console.lampSpecifications;

import com.console.LampActions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Form {

    JFrame frame;
    JTextField name;
    int channelAmount;
    JLabel lblChannels;
    List<JLabel> allChannelsLabels;
    List<JTextField> allChannelsInputs;

    JButton createBtn;

    public Form() {
        channelAmount = 0;
        allChannelsLabels = new ArrayList<>();
        allChannelsInputs = new ArrayList<>();
        frame = new JFrame();
        frame.setTitle("Create Lamp");
        frame.setResizable(false);
        frame.setBounds(100, 100, 500, 500);
        frame.getContentPane().setLayout(null);

        name = new JTextField();
        name.setBounds(128, 28, 86, 20);
        frame.getContentPane().add(name);
        name.setColumns(10);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);

        JLabel lblName = new JLabel("Name");
        lblName.setBounds(65, 31, 46, 14);
        frame.getContentPane().add(lblName);

        JButton addChannel = new JButton();
        addChannel.setBounds(128+20, 28*2, 86, 20);
        addChannel.setText("Add channel");
        addChannel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // your actions
                addChannel();
            }
        });
        frame.getContentPane().add(addChannel);

        lblChannels = new JLabel("Channels: " + channelAmount);
        lblChannels.setBounds(65, 31*2, 100, 14);
        frame.getContentPane().add(lblChannels);

        createBtn = new JButton();
        createBtn.setText("Create");
        createBtn.setBounds(128, 28*(3+channelAmount), 86, 20);
        createBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //your actions
                try {
                    frame.setVisible(false);
                    frame.dispose();
                    save();
                } catch (IOException | InterruptedException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        frame.getContentPane().add(createBtn);
        addChannel();
    }

    public void save() throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Saved :D");
        LampActions.selectLamp();
    }

    public void addChannel(){
        channelAmount++;
        lblChannels.setText("Channels: " + channelAmount);
        JTextField jTextField = new JTextField();
        jTextField.setBounds(128, 28*(2+channelAmount), 86, 20);
        frame.getContentPane().add(jTextField);
        JLabel label = new JLabel();
        label.setText("Channel " + channelAmount);
        label.setBounds(65, 28*(2+channelAmount), 80, 14);
        frame.getContentPane().add(label);
        allChannelsInputs.add(jTextField);
        allChannelsLabels.add(label);
        createBtn.setBounds(128, 28*(3+channelAmount), 86, 20);
    }

}
