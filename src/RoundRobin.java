import java.util.*;

public class RoundRobin {
    public int n  , qt;
    int currentTime = 0 ;
    int contextSwitching ;
    public process processes[] ;
    public Queue<Integer>readyqueue = new ArrayDeque<>();
    ArrayList<process> showInOrder = new ArrayList<>();////// Gantt chart array
    ArrayList<Integer> sequenceOfTime = new ArrayList<>();////// time at each process array + context



    public RoundRobin(int n , int qt , int cs){ ////////// constructor
        this.n   = n ;
        this.qt  = qt ;
        contextSwitching = cs ;

        processes = new process[n];
        for(int i = 0 ; i < n ; i++){
            processes[i] = new process() ;
            processes[i].pid = i+1 ;
        }

        readyqueue.add(0); // initially, pushing the first process which arrived first
        processes[0].inQueue = true ;

         sequenceOfTime.add(0); /// initial time in the process time array is 0

    }
  ///////////////////////// Sort functions
    public process[] sortByArivalTime(process[] processes){
        for(int i = 0 ; i < n;  i++){
            for(int j = 0 ; j < n-1; j++){
                if(processes[j].arrivalTime > processes[j+1].arrivalTime){
                    process temp ;
                    temp = processes[j]  ;
                    processes[j] = processes[j+1];
                    processes[j+1] = temp ;
                }
            }
        }
        return processes ;
    }
    public process[] sortByID(process[] processes){
        for(int i = 0 ; i < n;  i++){
            for(int j = 0 ; j < n-1; j++){
                if(processes[j].pid > processes[j+1].pid){
                    process temp ;
                    temp = processes[j]  ;
                    processes[j] = processes[j+1];
                    processes[j+1] = temp ;
                }
            }
        }
        return processes ;
    }
///////////////////////////////////////
    public void input() {
        System.out.print("process name : ");
        for(int i = 0; i < n; i++)
            processes[i].name= Main.sc.next();

        System.out.print("Enter the burst time of the processes : ");
        for(int i = 0; i < n; i++){
            processes[i].burstTime= Main.sc.nextInt();
            processes[i].burstTimeRemaining = processes[i].burstTime;
        }
        System.out.print("Enter the arrival time of the processes : ");
        for(int i = 0; i < n; i++)
            processes[i].arrivalTime= Main.sc.nextInt();


    }

//////////////////////////////////////

    public void checkForNewArrival(){
        for (int i = 0; i < n; i++)
        {
            process p = processes[i];
            // checking if any processes has arrived
            // if so, push them in the ready Queue.
            if (p.arrivalTime <= currentTime && !p.inQueue && !p.isComplete)
            {
                p.inQueue = true;
                readyqueue.add(i);
            }
        }
    }
    public void updateQueue(){

        int i = readyqueue.element();
        readyqueue.remove();
        process currentProcess = processes[i] ;
        showInOrder.add(currentProcess);
        // if the process is going to be finished executing,
        // when it's remaining burst time is less than time quantum
        // mark it completed and increment the current time
        if (currentProcess.burstTimeRemaining <= qt)
        {
            currentProcess.isComplete = true;
            currentTime += currentProcess.burstTimeRemaining;
            currentProcess.completionTime = currentTime;
            currentProcess.waitingTime = (currentProcess.completionTime + contextSwitching) - currentProcess.arrivalTime - currentProcess.burstTime;
            currentProcess.turnaroundTime = currentProcess.waitingTime + currentProcess.burstTime  ;

            if (currentProcess.waitingTime < 0)
                currentProcess.waitingTime = 0;

            currentProcess.burstTimeRemaining = 0;

            // if all the processes are not yet inserted in the queue,
            // then check for new arrivals

             checkForNewArrival();

        }
        else
        {
            // the process is not done yet. But it's going to be pre-empted
            // since one quantum is used
            // but first subtract the time the process used so far
            currentProcess.burstTimeRemaining -= qt;
            currentTime += qt;
            // if all the processes are not yet inserted in the queue,
            // then check for new arrivals
            checkForNewArrival();
            // insert the incomplete process back into the queue
            readyqueue.add(i);
        }
         sequenceOfTime.add(currentTime); /// add the current time of every process
         currentTime+=contextSwitching ;
         sequenceOfTime.add(currentTime); /// add the current time + context switching


    }
    /////// apply the algorithm here
    public void run(){
        processes = sortByArivalTime(processes); /// sort by arrival time before starting
        while(!readyqueue.isEmpty()){
            updateQueue();
        }
    }
   ///////
    public void display(){

        double avgWaitingTime = 0;
        double avgTurntaroundTime = 0;
        // sort the processes array by processes.PID

        processes = sortByID(processes);
        for (var i : processes)
        {
            System.out.println("process " + i.pid + "    name:" + i.name+"    Waiting Time: "
                    +i.waitingTime + "   Turnaround Time: " + i.turnaroundTime );

            avgWaitingTime +=     i.waitingTime;
            avgTurntaroundTime += i.turnaroundTime;
        }
        System.out.print("\nAverage Waiting Time: ");
        System.out.println(avgWaitingTime / n);

        System.out.print("Average Turnaround Time: ");
        System.out.println(avgTurntaroundTime / n);

        System.out.print("Gantt chart / process execution order : ");
        for (var i : showInOrder){
            System.out.print(i.name + " ");
        }
        int ctr = 1 ;
        System.out.print('\n');
        for (var i : sequenceOfTime){
            if(ctr % 3 == 0){
                System.out.print( "   " +i + "   " );
            }

            else
               System.out.print(i + ",");
            ctr++;

        }
    }
}
