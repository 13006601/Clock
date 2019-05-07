package clock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import javax.swing.SpinnerDateModel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JPanel;

public class View implements Observer{
    
    ClockPanel panel;
    JButton AddAlarm;
    PriorityQueue<Alarm> q = new SortedLinkedListPriorityQueue<>(8);
    
    public View(Model model) {
        JFrame frame = new JFrame();
        panel = new ClockPanel(model);
        //frame.setContentPane(panel);
        frame.setTitle("Java Clock");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
        // Start of border layout code
        
        // I've just put a single button in each of the border positions:
        // PAGE_START (i.e. top), PAGE_END (bottom), LINE_START (left) and
        // LINE_END (right). You can omit any of these, or replace the button
        // with something else like a label or a menu bar. Or maybe you can
        // figure out how to pack more than one thing into one of those
        // positions. This is the very simplest border layout possible, just
        // to help you get started.
        
        Container pane = frame.getContentPane();
        
        JButton button = new JButton("Add");
        pane.add(button, BorderLayout.LINE_START);
         
        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.PAGE_START);
         
       JButton button2 = new JButton("Update");
        pane.add(button2, BorderLayout.CENTER);
         
        JButton button3 = new JButton("Delete");
        pane.add(button3, BorderLayout.LINE_END);
         
        JButton button4 = new JButton("Save");
        pane.add(button4, BorderLayout.PAGE_END);
        
        // End of borderlayout code
        
        frame.pack();
        frame.setVisible(true);
        
        button.addActionListener(new ActionListener() {
            
          
            @Override
            public void actionPerformed(ActionEvent ae) {
               
                AddAlarm();

                
               
            }
    
        });
        
        
    }
    
    @Override
    public void update(Observable o, Object arg) {
        panel.repaint();
    }
    
    public void AddAlarm() {
        Date date = new Date();
        SpinnerDateModel sm =
        new SpinnerDateModel(date,null,null,Calendar.HOUR_OF_DAY);
        JSpinner spinner = new JSpinner(sm);
        JSpinner.DateEditor de = new JSpinner.DateEditor(spinner,"dd/MM HH:mm");
        spinner.setEditor(de);
        int option = JOptionPane.showOptionDialog(null, spinner, "Add alarm time", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if (option == JOptionPane.CANCEL_OPTION){
            // user hit cancel
        } else if (option == JOptionPane.OK_OPTION){
            // user entered a number
           String message = JOptionPane.showInputDialog("Alarm message",JOptionPane.INFORMATION_MESSAGE);
           String value = spinner.getValue().toString();
           //System.out.println(value);
           ///System.out.println(message);
           
         //  String text = message.substring(2, value.lastIndexOf(' '));
           String text = message.toString();
           System.out.println(text);
           Alarm alarm = new Alarm(text);
          // String P = String.parseInt(value.toString());
          String valueToString = String.valueOf(value);
          
           System.out.println("Adding " + alarm.getAlarm() + " with priority " + value);
          /* try {
                    q.add(alarm, value);
                } catch (QueueOverflowException e) {
                    
                    System.out.println("Add operation failed: " + e);
                }
*/
        }
        
        
    }
}  
