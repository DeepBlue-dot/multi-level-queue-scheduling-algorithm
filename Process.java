public class Process {
    int priority;
    int processId;
    int burstTime;
    int arrivalTime;
    int remainingBurstTime;
    int waitingTime;
    int turnaroundTime;

    Process(int processId, int priority, int burstTime, int arrivalTime) {
        this.processId = processId;
        this.priority = priority;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.remainingBurstTime = burstTime;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
    }
}