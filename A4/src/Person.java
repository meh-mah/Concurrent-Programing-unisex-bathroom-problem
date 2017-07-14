
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * represent a man or woman person.
 *
 * @author Mehran
 */
public class Person implements Runnable {

    protected Bathroom bath;
    //false=female, true=male
    private boolean gender;
    private int name;
    private long startTime;

    public Person(Bathroom bath, boolean gender, int name) {
        this.bath = bath;
        this.gender = gender;
        this.name = name;
    }
    
    @Override
    public String toString(){
        if (gender)
            return "man "+ this.name;
        else
            return "woman "+ this.name;
    }
    /**
     * Determines waiting time in queue.
     */
    public long QueueTime() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * get gender. true= male, false= female.
     */
    public boolean getGender() {
        return gender;
    }

    @Override
    public void run() {
        
            while (true) {
                try {
                    // Wait random amount between 1 to 3 seconds before trying to use the bathroom
                Thread.sleep((int) Math.ceil(Math.random()*3000));

                // time of entring the queue.
                startTime = System.currentTimeMillis();
                
                // go to Queue and check if allowed to enter otherwise wait until being notified.
                bath.waitingInQ(this);
                
                // when waitingInQ returns it means that you are in bathroom
                //so wait random amount between 1 to 5 seconds to simulate staying inside the bath
                Thread.sleep((int) Math.ceil(Math.random()*5000));
                // when finished notify all waiting persons who are allowed to go inside.
                bath.nextAllowed(this);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
}
