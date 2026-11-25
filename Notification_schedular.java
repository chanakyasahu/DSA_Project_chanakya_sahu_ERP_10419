import java.util.*;
import java.time.LocalTime;

public class Notification_schedular {

    static Scanner sc = new Scanner(System.in);
    static int id = 101;

    // Notification class
    static class Notification {
        int id;
        LocalTime time;
        String message;
        int interval;

        Notification(int id, LocalTime time,
                     String message, int interval) {
            this.id = id;
            this.time = time;
            this.message = message;
            this.interval = interval;
        }
    }

 
    static PriorityQueue<Notification> pq =
        new PriorityQueue<>(
            Comparator.comparing(n -> n.time));

   
    static Queue<Notification> ready = new LinkedList<>();

    
    static Notification[] cq = new Notification[50];
    static int count = 0;

    // Schedule notification
    static void schedule() {
        System.out.print("Enter time (HH:mm): ");
        LocalTime time;

        try {
            time = LocalTime.parse(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid time!");
            return;
        }

        System.out.print("Enter message: ");
        String msg = sc.nextLine();

        Notification n =
            new Notification(id++, time, msg, 0);

        pq.add(n);

        System.out.println("Notification scheduled: N" + n.id);
    }

    // Process due notifications
    static void process() {
        System.out.print("Enter current time (HH:mm): ");
        LocalTime now;

        try {
            now = LocalTime.parse(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid time!");
            return;
        }


        while (!pq.isEmpty() &&
               !pq.peek().time.isAfter(now)) {
            ready.add(pq.poll());
        }

        if (ready.isEmpty()) {
            System.out.println("No notification ready.");
            return;
        }

        while (!ready.isEmpty()) {
            Notification n = ready.poll();

            System.out.println("Notification sent: N" +
                n.id + " | " + n.message);

            // Recurring reminder
            for (int i = 0; i < count; i++) {
                if (cq[i].id == n.id) {
                    n.time = now.plusMinutes(n.interval);
                    pq.add(n);
                    System.out.println("Next reminder: " + n.time);
                }
            }
        }
    }

    // Add recurring reminder
    static void recurring() {
        if (count == 50) {
            System.out.println("Circular Queue Full!");
            return;
        }

        System.out.print("Enter time (HH:mm): ");
        LocalTime time;

        try {
            time = LocalTime.parse(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid time!");
            return;
        }

        System.out.print("Enter message: ");
        String msg = sc.nextLine();

        System.out.print("Enter interval in minutes: ");

        try {
            int interval = Integer.parseInt(sc.nextLine());

            if (interval <= 0) {
                System.out.println("Invalid interval!");
                return;
            }

            Notification n =
                new Notification(id++, time, msg, interval);

            cq[count++] = n;
            pq.add(n);

            System.out.println("Recurring reminder added: N" + n.id);

        } catch (Exception e) {
            System.out.println("Invalid interval!");
        }
    }

    // View pending notifications
    static void view() {
        ArrayList<Notification> list = new ArrayList<>(pq);
        list.sort(Comparator.comparing(n -> n.time));

        if (list.isEmpty()) {
            System.out.println("No pending notifications.");
            return;
        }

        for (Notification n : list) {
            System.out.println("N" + n.id + " | " +
                n.time + " | " + n.message);
        }
    }

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n=== NOTIFICATION SCHEDULER ===");
            System.out.println("1. Schedule Notification");
            System.out.println("2. Process Notifications");
            System.out.println("3. View Pending Notifications");
            System.out.println("4. Add Recurring Reminder");
            System.out.println("5. Exit");
            System.out.println();

            System.out.print("Enter choice: ");
            String ch = sc.nextLine();

            switch (ch) {
                case "1": schedule(); break;
                case "2": process(); break;
                case "3": view(); break;
                case "4": recurring(); break;
                case "5":
                    System.out.println("Program Closed.");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}