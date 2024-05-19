import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MultilevelQueueScheduler scheduler = new MultilevelQueueScheduler();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int processCount = scanner.nextInt();

        for (int i = 0; i < processCount; i++) {
            System.out.print("Enter priority (0-3) for process " + (i + 1) + ": ");
            int priority = scanner.nextInt();
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            int burstTime = scanner.nextInt();

            Process process = new Process(i + 1, priority, burstTime, 0);
            scheduler.addProcess(process);
        }

        System.out.print("Enter the time quantum for Round Robin: ");
        int timeQuantum = scanner.nextInt();
        scheduler.setTimeQuantum(timeQuantum);

        System.out.println("Executing...");
        scheduler.execute();
        scanner.close();
    }
}
