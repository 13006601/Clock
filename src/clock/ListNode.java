  package clock;

  /*
   this is a node what holding data about item, item priority number and  
  node link to the next node, nodes are using in the 
  ordered and unorderd link lists.
  */
public class ListNode<T> {
    private final T message;
    public final int time;
    public ListNode<T> next;
   
    
    public ListNode (T message, int time, ListNode<T> next)
    {
        this.message = message;
        this.time = time;
        this.next = next;
        
    }

    /*
     return item data
    */
    public T getMessage()
    {
        return message;
    }
    /*
      return item priority number
    */
    public int getTime()
    {
        return time;
    }
    /*
     return node link
    */
    public ListNode<T> getNext()
    {
        return next;
    }
    
   
 
}

