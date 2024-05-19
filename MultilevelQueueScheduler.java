import java.util.LinkedList;


public class MultilevelQueueScheduler {
    private LinkedList<Process> queue0 = new LinkedList<>();
    private LinkedList<Process> queue1 = new LinkedList<>();
    private LinkedList<Process> queue2 = new LinkedList<>();
    private LinkedList<Process> queue3 = new LinkedList<>();
    private LinkedList<Process> completed = new LinkedList<>();
    private int timeQuantum;

    public void addProcess(Process process) {
        switch (process.priority) {
            case 0 -> queue0.add(process);
            case 1 -> addProcessToSJFQueue(queue1, process);
            case 2 -> addProcessToSJFQueue(queue2, process);
            case 3 -> queue3.add(process);
        }
    }

    public void setTimeQuantum(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    public void execute() {
        int time = 0;
        int queueSelector = 1;

        while (!queue0.isEmpty() || !queue1.isEmpty() || !queue2.isEmpty() || !queue3.isEmpty()) {
            time++;
            switch (queueSelector) {
                case 1 -> time = executeRoundRobin(queue0, time);
                case 2 -> time = executeSJF(queue1, time);
                case 3 -> time = executeSJF(queue2, time);
                case 4 -> time = executeFIFO(queue3, time);
            }
            queueSelector = (queueSelector % 4) + 1;
        }
        System.out.println("Finished executing... [Time taken = " + time + " units]");
        analyzeQueues();
    }

    private void addProcessToSJFQueue(LinkedList<Process> queue, Process process) {
        int i = 0;
        while (i < queue.size() && queue.get(i).burstTime <= process.burstTime) {
            i++;
        }
        queue.add(i, process);
    }

    private int executeRoundRobin(LinkedList<Process> queue, int currentTime) {
        if (queue.isEmpty()) return currentTime;

        int timeSlice = 20;
        while (timeSlice > 0 && !queue.isEmpty()) {
            Process process = queue.removeFirst();
            int executionTime = Math.min(timeSlice, timeQuantum);
            if (process.remainingBurstTime <= executionTime) {
                currentTime += process.remainingBurstTime;
                process.remainingBurstTime = 0;
                process.turnaroundTime = currentTime - process.arrivalTime;
                process.waitingTime = process.turnaroundTime - process.burstTime;
                completed.add(process);
            } else {
                process.remainingBurstTime -= executionTime;
                currentTime += executionTime;
                queue.addLast(process);
            }
            timeSlice -= executionTime;
        }
        return currentTime;
    }

    private int executeSJF(LinkedList<Process> queue, int currentTime) {
        if (queue.isEmpty()) return currentTime;

        int timeSlice = 20;
        while (timeSlice > 0 && !queue.isEmpty()) {
            Process process = queue.removeFirst();
            int executionTime = Math.min(timeSlice, process.remainingBurstTime);
            process.remainingBurstTime -= executionTime;
            currentTime += executionTime;
            if (process.remainingBurstTime == 0) {
                process.turnaroundTime = currentTime - process.arrivalTime;
                process.waitingTime = process.turnaroundTime - process.burstTime;
                completed.add(process);
            } else {
                addProcessToSJFQueue(queue, process);
            }
            timeSlice -= executionTime;
        }
        return currentTime;
    }

    private int executeFIFO(LinkedList<Process> queue, int currentTime) {
        if (queue.isEmpty()) return currentTime;

        int timeSlice = 20;
        while (timeSlice > 0 && !queue.isEmpty()) {
            Process process = queue.removeFirst();
            int executionTime = Math.min(timeSlice, process.remainingBurstTime);
            process.remainingBurstTime -= executionTime;
            currentTime += executionTime;
            if (process.remainingBurstTime == 0) {
                process.turnaroundTime = currentTime - process.arrivalTime;
                process.waitingTime = process.turnaroundTime - process.burstTime;
                completed.add(process);
            } else {
                queue.addLast(process);
            }
            timeSlice -= executionTime;
        }
        return currentTime;
    }

    private void analyzeQueues() {
        for (int i = 0; i < 4; i++) {
            int totalWait = 0;
            int totalTurnaround = 0;
            int processCount = 0;

            for (Process process : completed) {
                if (process.priority == i) {
                    processCount++;
                    totalWait += process.waitingTime;
                    totalTurnaround += process.turnaroundTime;
                }
            }

            float avgWait = (float) totalWait / processCount;
            float avgTurnaround = (float) totalTurnaround / processCount;

            System.out.printf("Queue %d: Process Count = %d, Average Wait Time = %.2f, Average Turnaround Time = %.2f%n",
                    i, processCount, avgWait, avgTurnaround);
        }
    }
}
