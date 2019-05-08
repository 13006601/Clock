package clock;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SortedLinkedListPriorityQueue<T> implements PriorityQueue<T> {
     
    /*
     this is the node whare is all the data stored
    */
   private  ListNode<T> front;
   
   /*
    creating a new node wit the given size
   */
    
    public SortedLinkedListPriorityQueue(int size)
    {
        front = null;
        
        
    }
    /*
      this function is scan in order to find the null nods and retun data 
      about that to user
    */
    
    @Override
    public boolean isEmpty() 
    {
        return front == null;
    }
    
    /*
     this is the head what is serching list for the node on the top of the list
     and return data about tha node to user, because this is hte sorted linked list
     nod on the top of the list is nod wuth the highest priority number.
    */
    @Override
    public T head() throws QueueUnderflowException 
    {
       if(isEmpty())
       {
           throw new QueueUnderflowException();
       }
       
       return front.getMessage();
    }
    
    /*
     this is the function what add the item in the sorted inked list, aslo this function 
    is sorting nods and palce the in list in the order of from highest priority number
    to loves.
    */
    @Override
    public void add(T message, int cal)
    {
        /*
         this if statement is checking if the queue is empty if it empty the function inserted 
        a new node, but if it not empty function go futher in order to find an empty spot
        for the new node.
        */
       if(isEmpty()){
           
           front = new ListNode<>(message,cal,front);
           
        }else{
           
           /*
            the second nod value.
           */
            ListNode<T> currentNode = front;
         
           /*
             a hile method what checking if the 
            currentNod is not null.
            */
           while(currentNode != null){
               /*
               an if statment what check if the nod priroity number is 
               lower the priority number of the nod what abow him.
               if the nod priority number is lower the nod will be stored 
               after the nod with the higher priroity number.
               */
               if(currentNode.cal < cal){
                   front = new ListNode<>(message,cal,front.next);
                   break;
                } // end if;
               /*
               this else if statment is check that the current node priority is
               higher thet priority of hte loacl node  
               */
                else if(currentNode.cal > cal)
                {
                    /*
                    if statment what checking if the current nod nex is 
                    equal to null. if it true then function pace this nod 
                    on that position in the list.
                    */
                    if(currentNode.next == null)
                    {
                      currentNode.next = new ListNode<>(message,cal,currentNode.next);
                      break;
                      
                    } // end if;
                    else
                    {
                       /*
                         another if statment what checking currentNode.next priroity
                         and similar it with hte priroity of the second nod .
                        */
                      if(currentNode.next.getTime() < cal){
                          currentNode.next = new ListNode<>(message,cal,currentNode.next);
                      } //end if;
                     } // end esle;
                } // end else if;
                
               currentNode = currentNode.getNext();
            } // end while; 
        } // end else;
} // end of function; 

    /*
    this function is removing the first item in the list, because this is a sorted list 
    the first item on the list is alway a item with the highest priority.
    */
    @Override
    public void remove() throws QueueUnderflowException 
    {
        if(isEmpty())
        {
            throw new QueueUnderflowException();
        }
        front = front.getNext();
    }
    
     /*
     this function is an output what represnt to user the return result of 
    a list with the items and they priority number what was inserted by user.
    */
    
    @Override
    public String toString()
    {
        String result = "LinkStact: size = " + size();
        result += ", contents = [";
        for(ListNode<T> node = front; node != null; node = node.getNext())
        {
            if(node != front)
            {
                result += ", ";
            }
            result += node.getMessage();
        }
        result += "]  isEmpty() =  " + isEmpty();
        if(!isEmpty())
        {
            try {
                result += ", head() = "  +head();
            } catch (QueueUnderflowException ex) {
                Logger.getLogger(SortedLinkedListPriorityQueue.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /*
     represent the capocity of the sorted linked list
    where is the size sored.
    */
    private int size()
    {
        ListNode<T> node = front;
        int result = 0;
        while (node != null)
        {
            result = result + 1;
            node = node.getNext();
        }
        
        return result;
    }

   
    
}

