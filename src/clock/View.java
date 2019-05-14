package clock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
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

public class View implements Observer{
    
    ClockPanel panel;
    JButton AddAlarm;
    JButton RemoveAlarm;
    JButton UpdateAlarm;
    int hours, minutes, currhrs, currmins;
    long priority;
    String message;
    AlarmQueue<Alarm> q = new SortedArrayPriorityQueue<>(30);
    
    public View(Model model) {
        JFrame frame = new JFrame();
        panel = new ClockPanel(model);
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
        
        button3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                try { 
                    RemoveAlarm();
                } catch (QueueUnderflowException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
    });
        
         button2.addActionListener(new ActionListener() {
          
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    UpdateAlarm();
                } catch (QueueUnderflowException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NullPointerException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    @Override
    public void update(Observable o, Object arg) {
        panel.repaint();
        try {
                checkTime();
            
        } catch (QueueUnderflowException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void checkTime() throws QueueUnderflowException{
       Date date = Calendar.getInstance().getTime();

        //Alarm.epoch(currhrs, currmins, curMth, curYr) // Call epoch to get Long date time value 
        long t = date.getTime();
        
        System.out.print(q.isEmpty());
          if(!q.isEmpty()){
            try {
                System.out.println("Epoch :"+t/1000+"\n Pri :"+q.RtnPriority()+"\n"); // TESTOUTPUT Epoch and priority
                if(t/1000 == q.RtnPriority()){ // if epoch time == priority (Datetime in number) then 
                    
                    AlarmTrigger(); // trigger alarm
                }
            } catch (QueueUnderflowException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
    }
    
    public void AddAlarm() throws QueueOverflowException, ParseException, QueueUnderflowException {
        
       Date date = Calendar.getInstance().getTime();
       System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        SpinnerDateModel sm =
        new SpinnerDateModel(date,null,null,Calendar.HOUR_OF_DAY);
        JSpinner spinner = new JSpinner(sm);
        JSpinner.DateEditor de = new JSpinner.DateEditor(spinner," HH:mm yy/MM/dd");
        spinner.setEditor(de);
        int option = JOptionPane.showOptionDialog(null, spinner, "Add alarm time", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if (option == JOptionPane.CANCEL_OPTION){
            // user hit cancel
        } else if (option == JOptionPane.OK_OPTION){
           
            // user entered a number
            
            /**
             * On OK, message and time gets grabbed
             */
         
           message = JOptionPane.showInputDialog("Alarm message",JOptionPane.INFORMATION_MESSAGE);
           Date value = date;
           Date sp = (Date)spinner.getValue();
           LocalDate localDate = sp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int Month = localDate.getMonthValue();
            int Day = localDate.getDayOfMonth();
           hours = sp.getHours();
           minutes = sp.getMinutes();
     
           System.out.println("Text is     "+message);
           Alarm alarm = new Alarm(hours, minutes, Day, Month, message);
           System.out.println("Alarm details: " + message);
           System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
           
           System.out.println(Integer.toString(hours)+" "+Integer.toString(minutes)+" "+Integer.toString(Day)+" "+Integer.toString(Month));
           priority = alarm.epoch(Integer.toString(hours),Integer.toString(minutes),Integer.toString(Day),Integer.toString(Month));
            
            Date toMS;
             toMS = sp;
           
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) spinner.getValue());
            long millis = (long) toMS.getTime();
           System.out.println("Time in milliseconds:    "+millis);
           
           
          q.add(alarm, priority);
          System.out.println("The whole queue order is - -" + q); 
          System.out.println("Alarm details: " + message); 

        }
    }
    
    public void AlarmTrigger() throws QueueUnderflowException {
        JOptionPane.showMessageDialog(null,message);
        q.remove();
        System.out.println(q.toString());
       
        Date dt = Calendar.getInstance().getTime();
   }
    
    public void RemoveAlarm() throws QueueUnderflowException, NullPointerException{
            //loop through storage, add item x to list; draw list
            if(q.isEmpty()){
             JOptionPane.showMessageDialog(null,"No Alarms Exist");
            }
            else{
                    JFrame comboFrame = new JFrame();
                    comboFrame.setTitle("Alarm Removal");
                    comboFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                 System.out.println(" q to string is:   "+q.toString());
                    
                    final JComboBox combo = new JComboBox(q.GetAlarms());
                    final JButton btnRemove = new JButton("Remove Alarm");
                    btnRemove.addActionListener(new ActionListener() {
          
                        @Override
                        public void actionPerformed(ActionEvent ae) {

                            try {
                                int sel = combo.getSelectedIndex();
                                System.out.println(sel);
                                q.removeSelAlarm(sel);
                                if(!q.isEmpty()){
                                    
                                    combo.addItem(q.GetAlarms());
                                }else{
                                    JOptionPane.showMessageDialog(null,"No Alarms Exist");
                                    combo.removeAllItems();
                                }
                            } catch (QueueUnderflowException ex) {
                                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                   comboFrame.setPreferredSize(new Dimension(200, 100));
                   comboFrame.add(combo,BorderLayout.CENTER);
                   comboFrame.pack();
                   comboFrame.add(btnRemove,BorderLayout.SOUTH);
                   comboFrame.setVisible(true); 
            }
    }
    public void UpdateAlarm()throws QueueUnderflowException, NullPointerException{
           
            if(q.isEmpty()){
                
             JOptionPane.showMessageDialog(null,"No Alarms Exist");
             
            }
            else{
                    JFrame comboFrame = new JFrame();
                    comboFrame.setTitle("Alarm Removal");
                    comboFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                 System.out.println(" q to string is:   "+q.toString());
                    
                    final JComboBox combo = new JComboBox(q.GetAlarms());
                    final JButton btnRemove = new JButton("Remove Alarm");
                    btnRemove.addActionListener(new ActionListener() {
          
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            try {
                                int sel = combo.getSelectedIndex();
                                System.out.println(sel);
                                q.removeSelAlarm(sel);
                                if(!q.isEmpty()){
                                    combo.addItem(q.GetAlarms());
                                }else{
                                    JOptionPane.showMessageDialog(null,"No Alarms Exist");
                                    combo.removeAllItems();
                                }
                            } catch (QueueUnderflowException ex) {
                                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                   comboFrame.setPreferredSize(new Dimension(200, 100));
                   comboFrame.add(combo,BorderLayout.CENTER);
                   comboFrame.pack();
                   comboFrame.add(btnRemove,BorderLayout.SOUTH);
                   comboFrame.setVisible(true); 
            }
       }
} 
