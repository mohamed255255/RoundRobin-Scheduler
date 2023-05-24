import java.util.*;

public class Main {
    public static  Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter the number of processes : ");
        int n = sc.nextInt();
        System.out.print("Enter Quantum time : ");
        int q = sc.nextInt();
        System.out.print("Enter Context Switching time : ");
        int cs = sc.nextInt();
        RoundRobin rr = new RoundRobin(n , q , cs); ///
        //// enter arrival list / burst time list
       rr.input();
       rr.run();
       rr.display();




    }
}