*Short description of the applications:
The program is implementing the unisex bathroom problem. There is a bathroom that can be used by any number of men or women at the same time, but opposite sex cannot be inside simultaneously.

Each person is represented as a thread, which waits for a random amount of time than enters the queue for bathroom. At this time thread checks if it can use bathroom otherwise it waits. After going inside each person (thread) sleeps for random amount of time to simulate the use of bathroom and upon exit notifies all waiting threads, which are allowed to go inside.

The monitor class Bathroom has two methods. For the waitingInQ method java, synchronized statement is used instead of synchronized method to prevent deadlock. Since this method needs to call nextAllowed method, which is synchronized method. In addition, call to waitingInQ method by a thread must not block call to nextAllowed by another thread (As nextAllowed method also used by the person who wants to leave the bath to notify waiting person.).

If both methods were synchronized the person who wanted to leave had to wait for call to the waitingInQ to return, which is not logically fair. Also this could be cause of deadlock if person who called the waitingInQ method had to wait, so method would not return until being notified. On the other hand notify() itself can only happen in nextAllowed method which is now blocked by waitingInQ method (). Because of the situation no one could come to the queue by calling waitingInQ, no one could leave bathroom, and no one could enter bathroom.

To achieve fairness it is considered that everyone must eventually get a chance of using the bathroom. If the bathroom is in use by a sex, the other member of the same sex cannot enter the bathroom unless they are first in the line. Without such strategy, the bathroom would be on occupation of one of the genders and the other gender will be denied. For example, if a man staying first in the queue and a woman is inside the other woman who are behind the first man cannot enter. Therefore, prevention of denial helped to improve fairness. The individual can only enter the bathroom as the order they arrived. However, this fairness is at the cost of performance. The performance of the program calculated as the average waiting time shown in GUI. Shorter waiting time means better performance.

*Description of command-line parameters: 
There is no command line parameter.
The number of women who may want to use the bathroom is 15.
The number of men who may want to use the bathroom is 15.

*Instructions to build and to run the application:
// to compile
Javac Bathroom.java 
Javac Person.java     
Javac GUI.java        
//to run
Java GUI 
