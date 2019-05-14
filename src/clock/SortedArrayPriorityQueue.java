package clock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the PriorityQueue ADT using a sorted array for storage.
 *
 * Because Java does not allow generic arrays (!), this is implemented as an
 * array of Object rather than of PriorityItem&lt;T&gt;, which would be natural.
 * Array elements accessed then have to be cast to PriorityItem&lt;T&gt; before
 * using their getItem() or getPriority() methods.
 * 
 * This is an example of Java's poor implementation getting in the way. Java
 * fanboys will no doubt explain at length why it has to be this way, but note
 * that Eiffel allows it because Eiffel generics were done right from the start,
 * rather than being tacked on as an afterthought and limited by issues of
 * backward compatibility. Humph!
 * 
 * @param <T> The type of things being stored.
 */
public class SortedArrayPriorityQueue<T> implements AlarmQueue<T> {
    
    /**
     * Where the data is actually stored.
     */
    private final Object[] storage;

    /**
     * The size of the storage array.
     */
    private final int capacity;

    /**
     * The index of the last item stored.
     *
     * This is equal to the item count minus one.
     */
    private int tailIndex;

    /**
     * Create a new empty queue of the given size.
     *
     * @param size
     */
    public SortedArrayPriorityQueue(int size) {
        storage = new Object[size];
        capacity = size;
        tailIndex = -1;
    }

    @Override
    public T head() throws QueueUnderflowException {
        if (isEmpty()) {
            throw new QueueUnderflowException();
        } else {
            return ((AlarmTime<T>) storage[0]).getItem();
        }
    }

    @Override
    public void add(T item, long priority) throws QueueOverflowException {
        tailIndex = tailIndex + 1;
        if (tailIndex >= capacity) {
            /* No resizing implemented, but that would be a good enhancement. */
            tailIndex = tailIndex - 1;
            throw new QueueOverflowException();
        } else {
            /* Scan backwards looking for insertion point */
            int i = tailIndex;
            while (i > 0 && ((AlarmTime<T>) storage[i - 1]).getPriority() > priority) {
                storage[i] = storage[i - 1];
                i = i - 1;
            }
            storage[i] = new AlarmTime<>(item, priority);
        }
    }

    @Override
    public void remove() throws QueueUnderflowException {
        if (isEmpty()) {
            throw new QueueUnderflowException();
        } else {
            for (int i = 0; i < tailIndex; i++) {
                
                storage[i] = storage[i + 1];
              
            }
            tailIndex = tailIndex - 1;
        }
    }
    
    @Override
    public void removeSelAlarm(int sel) throws QueueUnderflowException {
        System.out.println("Sel value "+sel);
        if (isEmpty()) {
            throw new QueueUnderflowException();
        } else {
                            
            for ( int x = 0; x > tailIndex; x++){
                if (x >= sel){
                    if(!(x+1 > tailIndex)){
                        storage[x] = storage[x+1];
                        System.out.println("Rmoveing "+x);
                    }
                }
            }
            tailIndex = tailIndex -1;
            
            
            System.out.println(isEmpty());
        }
        
    }
  

    @Override
    public boolean isEmpty() {
        return tailIndex < 0;
    }
    
    @Override
    public long RtnPriority() throws QueueUnderflowException {
        if (isEmpty()) {
            throw new QueueUnderflowException();
        } else {
            return ((AlarmTime<T>) storage[0]).getPriority();
        }
    }

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i <= tailIndex; i++) {
            if (i > 0) {
                result = result + " - ";
            }
            result = result + storage[i];
        }
        result = result + "]";
        return result;
    }
    
    @Override
    public int ReturnCapacity() throws QueueUnderflowException{
        if (isEmpty()) {
            throw new QueueUnderflowException();
        } else {
            return (capacity);
        }    }

       @Override
    public int ReturnTailIndex() throws QueueUnderflowException{
        if (isEmpty()) {
            throw new QueueUnderflowException();
        } else {
            return (tailIndex);
        }    }
    
    @Override
    public String[] GetAlarms() throws NullPointerException{
{
        System.out.println("Getting Alarms ***********");
        String[] ArrStr = new String[tailIndex+1];
        
                System.out.println("building array Alarms ***********"+tailIndex);
        
                System.out.println("P "+((AlarmTime<T>) storage[0]).getPriority());
        try{
        for(int x = 0; x < tailIndex+1;x++){
            
            System.out.println("Running loop");
            long a = ((AlarmTime<T>) storage[x]).getPriority();
                        
            Date d = new Date(a*1000L);
            
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            f.setTimeZone(TimeZone.getTimeZone("GMT+1"));
            
            String fd = f.format(d);
             
            System.out.println("Alarm "+x+" "+fd);
            ArrStr[x] = fd;
            System.out.println("Number of elemts "+x);      
        }
        }catch(NullPointerException e){
            e.getMessage();
        }
        return ArrStr;
        } 
    }


     
    
   
    
}
