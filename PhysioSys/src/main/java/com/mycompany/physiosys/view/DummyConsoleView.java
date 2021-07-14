package com.mycompany.physiosys.view;

import com.mycompany.physiosys.model.knowledge.KB;
import com.mycompany.physiosys.model.knowledge.items.ChoiceItem;
import com.mycompany.physiosys.model.knowledge.items.KnowledgeItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.awt.*;

import static java.awt.BorderLayout.*;

public class DummyConsoleView implements View, ActionListener {
    JFrame frame;
    JPanel toPanel, bottomPanel, midPanel, midPanel2;
    JLabel label1, labelQuestion, labeltip;
    JTextArea textAreaTip, textAreaQues;
    JButton button, button2;
    JRadioButton[] radioButtons;


    public DummyConsoleView(){
        frame = new JFrame();
        label1 = new JLabel("Physiotherapist Knowledge System Helper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        toPanel = new JPanel();

        toPanel.setBackground(Color.LIGHT_GRAY);
        toPanel.setForeground(Color.WHITE);
        toPanel.setFont(new Font("TimesRoman", Font.PLAIN, 30));

        bottomPanel = new JPanel();
        midPanel = new JPanel();
        midPanel2= new JPanel();


        toPanel.add(label1);
        frame.getContentPane().add(NORTH,toPanel);

        //buttons configurations
        button = new JButton("Next");
        button.addActionListener(this);

        button2 = new JButton("Done");
        button2.addActionListener(this);

        bottomPanel.add(button);
        bottomPanel.add(button2);
        frame.getContentPane().add(SOUTH, bottomPanel);

        labelQuestion = new JLabel("Question to be asked");
        midPanel.add(labelQuestion);
        textAreaTip = new JTextArea("Tips goes here");
        midPanel2.setSize(600/3, 400*3/5);
        textAreaTip.setLineWrap(true);
        textAreaTip.setWrapStyleWord(true);
        textAreaTip.setOpaque(false);
        textAreaTip.setEditable(false);
        midPanel2.setBackground(Color.LIGHT_GRAY);
        midPanel2.add(textAreaTip);
        midPanel2.setVisible(true);

        frame.getContentPane().add(midPanel2,BorderLayout.LINE_START);

        ButtonGroup buttonGroup = new ButtonGroup();
        radioButtons = new JRadioButton[4];

        for(int i=0; i<4;i++){
            radioButtons[i] = new JRadioButton("Button" + i , false);
            midPanel.add(radioButtons[i], LINE_START);
            buttonGroup.add(radioButtons[i]);

        }

        frame.getContentPane().add(CENTER,midPanel);
        frame.setVisible(true);

    }

    public synchronized void waitingForActionSend(ChoiceItem csi){
        while(!waiting) {
            try {
                wait();
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
        waiting = false;
        this.newItem = csi;
        notifyAll();
    }

    public synchronized KnowledgeItem waitingForActionRecieve(){
        while(waiting){
            try{wait();
            }catch (InterruptedException e ){
                Thread.currentThread().interrupt();
            }
        }
        waiting = true;
        notifyAll();
        return newItem;

    }

    private boolean waiting = true;
    private ChoiceItem csi;
    private KnowledgeItem newItem;

    @Override
    public <T> KnowledgeItem<T> inquire(KnowledgeItem<T> item) {
        Scanner in = new Scanner(System.in);
        System.out.println("[view] Find out which question to ask");
        System.out.println(item.getQuestion());
        labelQuestion.setText(item.getQuestion());
        if (item.hasTip()) {
            System.out.println("TIP:" + item.getTip());
            textAreaTip.setText(item.getTip());
            textAreaTip.setPreferredSize(midPanel2.getSize());

        }else {
            textAreaTip.setText("Tips goes here:");
        }
        switch (item.getOrigin()) {
            case CHOICESELECTION:
                System.out.println("[view]  Choice selection question selected");
                this.csi = (ChoiceItem) item;
                int i = 0;
                for (String option : csi.getOptions()) {
                    System.out.println(i + ": " + option);
                    radioButtons[i].setText(option);
                    radioButtons[i].setVisible(true);
                    i++;
                }
                while(i<=3) {
                    radioButtons[i].setVisible(false);
                    i++;
                }
                // wait function needed
                System.out.println("[view] waiting for action to be performed");

                return waitingForActionRecieve();
        }
        return null;
    }

    @Override
    public void showResult(KnowledgeItem goal, KB KB) {
        if (goal == null) {
            System.out.println("No goal could be reached");
        } else {
            int i=0;
            while(i<=3) {
                radioButtons[i].setVisible(false);
                i++;
            }
            System.out.println(goal.getGoalResponse());
            labelQuestion.setText(goal.getGoalResponse());
            button.setVisible(false);
            //button2.setText("Start a New");
            frame.repaint();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int i=0;
        if("Next".equals(e.getActionCommand())) {
            System.out.println("[view] Next pressed");
            for (JRadioButton choice: radioButtons) {
                if (choice.isSelected()) {
                    csi.setValue(i);
                    break;
                }
                i++;
            }
            waitingForActionSend(csi);
        }

        if("Done".equals(e.getActionCommand())){
            System.exit(0);
        }

    }

}
