import java.util.Iterator;
import java.util.LinkedList;

/**
 * Bathroom monitor.
 *
 * @author Mehran
 */
public class Bathroom {
    //represent list of persons in waiting queue
    private LinkedList queueList = new LinkedList();
    //represent list of persons in bathroom
    private LinkedList inBathList = new LinkedList();

    private int noOFWomen = 0;
    private long femaleQTime = 0;
    private int noOfMen = 0;
    private long maleQTime = 0;


    //to show updates in GUI
    private GUI GUI;

    public Bathroom(GUI GUI) {
        this.GUI = GUI;
    }

    /**
     * Enters a Person into the waiting line of the bathroom
     */
    public  void  waitingInQ(Person per) throws InterruptedException {
        Person next;

        /*due to critical refrence to monitor varible the object must be locked to prevent race condition in updating queueList
         * java synchronized statement is used instead of synchronized method to prevent deadlock. Since this method need to call nextAllowed which is synchronized method.
         * Also call to waitingInQ method by a thread must not block call to nextAllowed by another thread
         * notice that next Allowed method also used by the person who wants to leave the bath to notify waiting person.
         * If both methods were synchronized the person who wants to leave had to wait for call to the waitingInQ to return which is not logically  fair.
         * Also as a result of this situation also no one can come to the queue by calling waitingInQ.
        */
        synchronized (this) {
            // Add person to the line
            queueList.add(per);
            
            //check if the person allowed to enter.
            //he/she must be first in the line and no opposite sex inside
            next = nextAllowed(null);
        }
        
        // If it is not allowed to enter wait until being notified.
        if (per != next) {
            //In order to wait or notify/notifyAll an object, you need to be holding the lock with the synchronized statement.
            //if the current thread is not the owner of the object's monitor. it is needed to synchronize on the object that is going to call wait.
            synchronized (per) {
                System.out.println(per.toString()+" waitting in the queue");
                per.wait();
            }
        }
    }

    /**
     * To check next allowed person and notify him/her
     * and Also to remove a person who used the bathroom and wants to leave, if any. Otherwise call this method with null.
     */
    public synchronized Person nextAllowed(Person per) {
        
        Person next_allowed = null;
        if (per != null) {
            // remove the person from the list of persons who are using the bathroom
            remove(per, inBathList);
            System.out.println(per.toString()+" left the bathroom"); 
            //and notify all of the people who can use the bathroom if any
            do {
                // If queue is empty.
                if (queueList.size() == 0 ) {
                    next_allowed = null;
                } else {// Otherwise find next allowed person
                    //first need to now who is  inside
                    Person insideSex = (Person) inBathList.peek();
                    //then, who is first in the queue
                    Person next = (Person) queueList.peek();
                    boolean gender; // female= false, male=true

                    // which gender, If any one is inside
                    if (insideSex != null) {
                        gender = insideSex.getGender();
                    } else { // If no one inside set the gender as the gender of the first one in the waiting queue. So he or she can enter
                        gender = next.getGender();
                    }
                    // If any one in the queue
                    if (next != null) {
                        // can enter, If next is male, and males are also inside or no one inside
                        if (next.getGender() && (gender || inBathList.size() == 0)) {
                            next_allowed = next;
                        }
                        // can enter, If next is female, and females are also inside or no one inside
                        else if (!next.getGender() && (!gender || inBathList.size() == 0)) {
                            next_allowed = next;
                        }
                        // Otherwise, notallowed to enter.
                        else {
                            next_allowed = null;
                        }
                    }
                }
                // If a person allowed to go in
                if (next_allowed != null) {
                    // Remove him/her from the waiting queue, and update info in GUI
                    if (next_allowed.getGender()) {
                        remove(next_allowed, queueList);
                        maleQTime += next_allowed.QueueTime();
                        noOfMen++;
                        GUI.update(next_allowed.getGender(), maleQTime / noOfMen, noOfMen);
                    } else {
                        remove(next_allowed, queueList);
                        femaleQTime += next_allowed.QueueTime();
                        noOFWomen++;
                        GUI.update(next_allowed.getGender(), femaleQTime / noOFWomen, noOFWomen);
                    }
                    // Add to inside bathroom list
                    inBathList.add(next_allowed);
                    System.out.println(next_allowed.toString()+" entered the bathroom ");
                    // Notify to wake him/her up
                    synchronized (next_allowed) {
                        next_allowed.notify();
                    }
                }
            
        } while(next_allowed!=null);
            
    } else { // if new person wants to check if he is allowed or not
        // If queue is empty.
        if (queueList.size() == 0 ) {
            next_allowed = null;   
        } else {// Otherwise find next allowed person
            //first need to now who is  inside
            Person insideSex = (Person) inBathList.peek();
            //then, who is first in the queue
            Person next = (Person) queueList.peek();
            boolean gender; // female= false, male=true

            // which gender, If any one is inside 
            if (insideSex != null) {
                gender = insideSex.getGender();
            }
            // If no one inside set the gender as the gender of the first one in the waiting queue. So he or she can enter 
            else {
                gender = next.getGender();
            }
            // If any one in the queue
            if (next != null) {
                // can enter, If next is male, and males are also inside or no one inside
                if (next.getGender() && (gender || inBathList.size() == 0)) {
                    next_allowed = next;
                }
                // can enter, If next is female, and females are also inside or no one inside
                else if (!next.getGender() && (!gender || inBathList.size() == 0)) {
                    next_allowed = next;
                }
                // Otherwise, notallowed to enter.
                else {
                    next_allowed = null;
                }
            }
        }
        
        // If a person allowed to go in
        if (next_allowed != null) {
            // Remove him/her from the waiting queue, and update info in GUI
            if (next_allowed.getGender()) {
                remove(next_allowed, queueList);
                maleQTime += next_allowed.QueueTime();
                noOfMen++;
                GUI.update(next_allowed.getGender(), maleQTime
                        / noOfMen, noOfMen);
            } else {
                remove(next_allowed, queueList);
                femaleQTime += next_allowed.QueueTime();
                noOFWomen++;
                GUI.update(next_allowed.getGender(), femaleQTime
                        / noOFWomen, noOFWomen);
            }
            // Add to inside bathroom list
            inBathList.add(next_allowed);
            System.out.println(next_allowed.toString()+" entered the bathroom ");
            // Notify to wake him/her up
            synchronized (next_allowed) {
                next_allowed.notify();
            }
        }
        }
        return next_allowed;
    }

    /**
     * general method to Removes a person from either inBathList list or queueList
     */
    private void remove(Person person, LinkedList list) {
        Iterator i = list.iterator();
        while (i.hasNext()) {
            if (i.next() == person) {
                i.remove();
            }
        }
    }
}
