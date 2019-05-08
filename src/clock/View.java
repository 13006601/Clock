package clock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SpinnerDateModel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JPanel;

public class View implements Observer{
    
    ClockPanel panel;
    JButton AddAlarm;
    AlarmQueue<Alarm> q = new SortedArrayPriorityQueue<>(8);
    
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
        frame.setResizable(false);
        
        button.addActionListener(new ActionListener() {
          
            @Override
            public void actionPerformed(ActionEvent ae) {
               
                try {
                    AddAlarm();
                } catch (QueueOverflowException e) {
                    System.out.println("Add operation failed: " + e);
                } catch (ParseException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                } catch (QueueUnderflowException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }   
            }
    
        });
        
        
    }
    
    @Override
    public void update(Observable o, Object arg) {
        panel.repaint();
    }
    
    public void AddAlarm() throws QueueOverflowException, ParseException, QueueUnderflowException {
        
       Date date = Calendar.getInstance().getTime();
       System.out.println("Date is    "+date.toString());
        SpinnerDateModel sm =
        new SpinnerDateModel(date,null,null,Calendar.HOUR_OF_DAY);
        JSpinner spinner = new JSpinner(sm);
        JSpinner.DateEditor de = new JSpinner.DateEditor(spinner,"HH:mm");
        spinner.setEditor(de);
        int option = JOptionPane.showOptionDialog(null, spinner, "Add alarm time", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if (option == JOptionPane.CANCEL_OPTION){
            // user hit cancel
        } else if (option == JOptionPane.OK_OPTION){
           
            // user entered a number
            
            /**
             * On OK, message and time gets grabbed
             */
           String message = JOptionPane.showInputDialog("Alarm message",JOptionPane.INFORMATION_MESSAGE);
           Date value = date;
             //value = (Date) ;
                   
          
           int hours = value.getHours();
           int minutes = value.getMinutes();
           int day = value.getDay();
           int month = value.getMonth();
           System.out.println("Text is     "+message);
           Alarm alarm = new Alarm(hours, minutes, message);
           
           int priority = alarm.epoch(Integer.toString(hours),Integer.toString(minutes),Integer.toString(day),Integer.toString(month));
          
            System.out.println("Date/Time entered by user is     "+value);
            System.out.println("Hours are:    "+hours);
            System.out.println("Minutes are:    "+minutes);
            System.out.println("Hours are:    "+day);
            System.out.println("Minutes are:    "+month);
            

            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) spinner.getValue());
           // System.out.println("Calendar - Time in milliseconds : " + calendar.getTimeInMillis());
            int millis = (int) calendar.getTimeInMillis();
           System.out.println("Time in milliseconds:    "+millis);
          // System.out.println("Adding " + item.getItem() + " with priority " + priority);
          // q.add(item,priority);
           
          q.add(alarm,priority );
          
          //q.head();
          
          
          System.out.println("The head of the queue is :  "  + q.head().toString());
          System.out.println("Date " +date);
           
           

        }

        /*
        System.currentTimeMillis();
        SimpleDateModel formatter= new SimpleDateModel("yyyy-MM-dd 'at' HH:mm:ss z");  
        Date date = new Date(System.currentTimeMillis());  
        System.out.println(formatter.format(date)); 
*/
    }
}  
