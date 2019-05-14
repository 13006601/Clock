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
    /**
     * This section is represent Clock panel, JButtons and global variables as integers, long and string
     * also, here is function for the making new Sorted Array Priority Queue with size 30 possible items. 
     */
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
        /**
         * I make a Graphical User Interface window not resizable  
         * just not allow user to brake visual after resize the window. 
         */
        frame.setResizable(false);
        /**
         * Action listener for the button one (Add) 
         * hold the function Add Alarm
         * catch QueueOverflowException
         * catch ParseExeption and QueueUnderflowException
         */
        button.addActionListener(new ActionListener() {
          
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    AddAlarm();
                } catch (QueueOverflowException e) {
                    System.out.println("Add operation failed: " + e);
                } catch (ParseException | QueueUnderflowException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }   
            }
    
        });
        /**
         * Action listener for the button 3 (Delete)
         * hold the function Remove Alarm
         * catch QueueUnderflowException
         */
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
        /**
         * Action listener for the button 2 (Update)
         * hold the function Update Alarm
         * catch QueueUnderfloeException,NullPointerExeption and QueueOverflowExpection
         */
         button2.addActionListener(new ActionListener() {
          
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    UpdateAlarm();
                } catch (QueueUnderflowException | NullPointerException | QueueOverflowException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    /**
     * update function for the repaint the clock panel.
     * also, used for the check time function
     * @param o
     * @param arg 
     * catch QueueUnderflowException
     */
    @Override
    public void update(Observable o, Object arg) {
        panel.repaint();
        try {
                checkTime();
            
        } catch (QueueUnderflowException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    /**
     * check Time function
     * this function is used to checking priority queue
     * and finding matches between current time in milliseconds 
     * and time of the alarm, when they the same run the alarm trigger
     * @throws QueueUnderflowException 
     */
    public void checkTime() throws QueueUnderflowException{
       Date date = Calendar.getInstance().getTime();
        long t = date.getTime();
        System.out.print(q.isEmpty());
          if(!q.isEmpty()){
            try {
                //System.out.println("Epoch :"+t/1000+"\n Pri :"+q.RtnPriority()+"\n"); // TESTOUTPUT Epoch and priority
                if(t/1000 == q.RtnPriority()){ // if epoch time == priority (Datetime in number) then 
                    AlarmTrigger(); // trigger alarm
                }
            } catch (QueueUnderflowException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
    }
    /**
     * Add alarm function this function make a new date with the calendar
     * after function run it create JSpinner where user can choose a time and date
     * after user press ok this data become a priority item and stored in the 
     * sorted array
     * if user press cancel spinner will close
     * after user set time will appear option dialog box where user will be able to input an alarm message
     * time of the alarm is converted into milliseconds to put it as a priority for sorted array 
     * all the data form here is send to the Alarm class epoch
     * @throws QueueOverflowException
     * @throws ParseException
     * @throws QueueUnderflowException 
     */
    public void AddAlarm() throws QueueOverflowException, ParseException, QueueUnderflowException {
        
        Date date = Calendar.getInstance().getTime();
        //System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
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
     
           //System.out.println("Text is     "+message);
           Alarm alarm = new Alarm(hours, minutes, Day, Month, message);
           //System.out.println("Alarm details: " + message);
           System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
           
           System.out.println(Integer.toString(hours)+" "+Integer.toString(minutes)+" "+Integer.toString(Day)+" "+Integer.toString(Month));
           priority = alarm.epoch(Integer.toString(hours),Integer.toString(minutes),Integer.toString(Day),Integer.toString(Month));
            
            Date toMS;
             toMS = sp;
           
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) spinner.getValue());
            long millis = (long) toMS.getTime();
           //System.out.println("Time in milliseconds:    "+millis);
          q.add(alarm, priority);
          //System.out.println("The whole queue order is - -" + q); 
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
    public void UpdateAlarm()throws QueueUnderflowException, NullPointerException, QueueOverflowException{
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
                                try {
                                    AddAlarm();
                                    } catch (QueueOverflowException e) {
                                    System.out.println("Add operation failed: " + e);
                                    } catch (ParseException | QueueUnderflowException ex) {
                                     Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                                    }
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
