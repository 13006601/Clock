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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.management.Query.value;
import javax.swing.SpinnerDateModel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JPanel;

public class View implements Observer{
    
    ClockPanel panel;
    JButton AddAlarm;
    int priority, hours, minutes, currhrs, currmins;
    AlarmQueue<Alarm> q = new SortedArrayPriorityQueue<>(30);
    
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
      
        try {
                checkTime();
            
        } catch (QueueUnderflowException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void checkTime() throws QueueUnderflowException{
       Date date = Calendar.getInstance().getTime();
        //currhrs = date.getHours();
        //currmins = date.getMinutes();
        //int curMth = date.getMonth();
        //int curYr = date.getYear();

        //Alarm.epoch(currhrs, currmins, curMth, curYr) // Call epoch to get Long date time value 
        long t = date.getTime();
        
        //int epoch = (int)t/1000;
        
       //if((currhrs == hours)&&(currmins == minutes)){
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
       System.out.println("Date is    "+date.toString());
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
         
           String message = JOptionPane.showInputDialog("Alarm message",JOptionPane.INFORMATION_MESSAGE);
           Date value = date;
           Date sp = (Date)spinner.getValue();
             //value = (Date) ;
           LocalDate localDate = sp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int Month = localDate.getMonthValue();
            int Day = localDate.getDayOfMonth();
          System.out.println("spinner date is   : "+sp);
           hours = sp.getHours();
           minutes = sp.getMinutes();
           //int day = sp.getDay();
           //int month = sp.getMonth();
           System.out.println("Text is     "+message);
           Alarm alarm = new Alarm(hours, minutes, Day, Month, message);
           //int priority = Integer.parseInt(input.substring(input.lastIndexOf(' ') + 1));
           
           System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
           
           System.out.println(Integer.toString(hours)+" "+Integer.toString(minutes)+" "+Integer.toString(Day)+" "+Integer.toString(Month));
           priority = alarm.epoch(Integer.toString(hours),Integer.toString(minutes),Integer.toString(Day),Integer.toString(Month));
         

            System.out.println("Date/Time entered by user is     "+sp);
            System.out.println("Hours are:    "+hours);
            System.out.println("Minutes are:    "+minutes);
            System.out.println("Day are:    "+Day);
            System.out.println("Month are:    "+Month);
            
            Date toMS;
             toMS = sp;
           
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) spinner.getValue());
           // System.out.println("Calendar - Time in milliseconds : " + calendar.getTimeInMillis());
            long millis = (long) toMS.getTime();
           System.out.println("Time in milliseconds:    "+millis);
          //System.out.println("Adding " + AlarmTime.getItem() + " with priority " + priority);
          // q.add(item,priority);
           //int priority = Date toMS;
          q.add(alarm,priority);
          
          //q.head();
          
          
          System.out.println("The head of the queue is :  "  + q.head().toString());
          System.out.println(alarm+","+priority);
          System.out.println("The whole queue order is - -" + q); 
           

        }

        /*
        System.currentTimeMillis();
        SimpleDateModel formatter= new SimpleDateModel("yyyy-MM-dd 'at' HH:mm:ss z");  
        Date date = new Date(System.currentTimeMillis());  
        System.out.println(formatter.format(date)); 
*/
    }
    
    /**
     *
     * @throws QueueUnderflowException
     * @throws clock.AlarmQueue
     * @throws clock.AlarmQueue
     * @throws clock.AlarmQueue
     * @throws clock.AlarmQueue
     * @throws clock.AlarmQueue
     * @throws clock.AlarmQueue
     * @throws AlarmQueue
     */
    public void AlarmTrigger() throws QueueUnderflowException {
        
         JOptionPane.showMessageDialog(null,q);
        q.remove();
        System.out.println(q.toString());
        /*
        
       int priority = ;
       
       System.out.print(priority); 
        */
        //q.remove();
        Date dt = Calendar.getInstance().getTime();
      //  int currentHours = dt.getHours();
      //  int currentMinutes = dt.getMinutes();
       // int priority = 0;
        //long hours = TimeUnit.MILLISECONDS.toHours(priority);
       // long minutes = TimeUnit.MILLISECONDS.toMinutes(priority);
        
      /*  if( hours == currentHours && minutes == currentMinutes){
            JOptionPane.showMessageDialog(null,q);
            q.remove();

        }
       q.remove();
        */
       // if(priority == (int) dt.getTime() ){
            //System.out.println("trigger alarm");
            //q.remove();
        //}


   }
    
}  
