import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;

public class Rocket_Launch_Stimulator {
    private static final int UPDATE_INTERVAL_MS = 1000; // 1 second

    // data to be displayed
    private static double destination=100;
    private static int stage = 1;
    private static double altitude = 0.0;
    private static double velocity = 0.0;
    private static double fuelLevel = 500.0;
    private static boolean preLaunchChecksPassed = false;
    

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // to enter command
        while (true) {
            System.out.println("Enter command:");
            String userInput = scanner.nextLine();

            // exit
            if (userInput.equalsIgnoreCase("Exit")) {
                System.out.println("Exiting chat...");
                break;
            }
            // display input command
            processUserInput(userInput);

            // prelaunch check
            if (userInput.equalsIgnoreCase("start_checks")) {
                if (!preLaunchChecksPassed) {
                    // Perform prelaunch check
                    boolean launchReady = performPreLaunchChecks();

                    if (launchReady) {
                        preLaunchChecksPassed = true;
                    } else {
                        System.out.println("Mission failed");
                        System.exit(0); // Terminate
                    }
                } else {
                    System.out.println("Pre-launch checks have already been started.");
                }
            }

            // launch command
            if (userInput.equalsIgnoreCase("launch")) {
                if (preLaunchChecksPassed) {
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new UpdateTask(), 0, UPDATE_INTERVAL_MS);
                } else {
                    System.out.println("Cannot launch. Pre-launch checks have not been completed or passed.");
                }
            }
            // fast forward command
            if (userInput.startsWith("fast_forward ")) {
                try {
                    int fastForwardSeconds = Integer.parseInt(userInput.split(" ")[1]);
                    if (fastForwardSeconds > 0) {
                        //execute for x second
                        startRocketDataUpdaterFastForward(fastForwardSeconds);
                    } else {
                        System.out.println("Invalid input for fast forward. Please specify a positive number of seconds.");
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Invalid input for fast forward. Please use 'fast forward x' where x is the number of seconds.");
                }
            }
            else{
                System.out.println("Enter a valid input");
            }

        }
        scanner.close();
        double[] stageThresholds = calculateStageThresholds(destination);
        stageThresholds = calculateStageThresholds(destination);
        
    }

    private static class UpdateTask extends TimerTask {
        @Override
        public void run() {
            // checks for destination
            if (altitude >= destination || fuelLevel <= 0) {
                cancel(); // timmer stop
                if (altitude == destination) {
                    System.out.println("Orbit achieved.Mission Successful");
                } else {
                    System.out.println("Mission Failed");
                }
                System.exit(0); // Terminate
            } else {

                // Simulation
                altitude += 10;
                velocity += 1000;
                fuelLevel -=5;
                System.out.println("Stage: " + stage);
                System.out.println("Fuel Level: " + fuelLevel + "%");
                System.out.println("Altitude: " + altitude + " km");
                System.out.println("Speed: " + velocity + " km/h");
                System.out.println("-------------------------");

                int newStage = calculateStage(altitude);
                if (newStage != stage) {
                    stage = newStage;
                    System.out.println("Stage " + (stage - 1) + " completed. Separating stage. Entering stage " + stage);
                }
            }
        }
        
    }
   private static int calculateStage(double currentAltitude) {
    // range for 4 stages
    double[] stageThresholds = calculateStageThresholds(destination);
    for (int i = 0; i < stageThresholds.length; i++) {
        if (currentAltitude <= stageThresholds[i]) {
            return i + 1;
        }
    }

    return stageThresholds.length + 1;
}

    private static double[] calculateStageThresholds(double maxAltitude) {
        int numStages = 4; // You can change the number of stages as needed
        double[] thresholds = new double[numStages];
        double step = maxAltitude / numStages;

        for (int i = 0; i < numStages; i++) {
            thresholds[i] = step * (i + 1);
        }

        return thresholds;
    }

   

    private static boolean performPreLaunchChecks() {

        boolean weatherCriteria = checkWeather();
        boolean Fuel_Level = checkFuel_Level();
        boolean safetyCriteria = checkSafety();

        System.out.println("Pre-Launch Criteria Assessment:");
        System.out.println("1. Weather Criteria: " + (weatherCriteria ? "Go" : "No-Go"));
        System.out.println("2. Fuel Level: " + (Fuel_Level ? "Go" : "No-Go"));
        System.out.println("3. Safety Criteria: " + (safetyCriteria ? "Go" : "No-Go"));
        
        if (altitude < 0 || velocity < 0 || fuelLevel < 0) {
            System.out.println("Negative altitude, velocity, or fuel level detected. Mission failed.");
            return false;
        }
        else if(!(weatherCriteria && Fuel_Level && safetyCriteria)) {
            System.out.println("\nOne or more criteria are not met. Launch is postponed (No-Go).");
            return false;
        }

        else {
            System.out.println("\nAll criteria are met. Go for launch.");
            return true;
        } 
    }

    private static boolean checkWeather() {
        System.out.println("Checking weather conditions...");
        // checks wheather
        return true;
    }

    private static boolean checkFuel_Level() {
        // checks fuel level
        System.out.println("Checking technical readiness...");
       double distance= destination/10;
       double fuel=distance*0.5;
        if (fuel <= fuelLevel) {
            return true;
        }
        return false;
    }

    private static boolean checkSafety() {
        // checks safety
        System.out.println("Performing safety checks...");
        return true;
    }

    private static void processUserInput(String userInput) {
        // user input command
        System.out.println("User input: " + userInput);
    }
    private static void startRocketDataUpdaterFastForward(int fastForwardSeconds) {
         if(fastForwardSeconds>=10)
       {
           altitude=destination;
           velocity=destination*1000;
           fuelLevel-=(destination*0.5);
       }
        else{
        while (fastForwardSeconds!=0)
        {
            altitude += 10;
            velocity += 1000;
            fuelLevel -= 10;
            fastForwardSeconds--;
        }
        }
        int newStage = calculateStage(altitude);
                if (newStage != stage) {
                    stage = newStage;
                }
             
                System.out.println("Stage: " + stage);
                System.out.println("Fuel Level: " + fuelLevel + "%");
                System.out.println("Altitude: " + altitude + " km");
                System.out.println("Speed: " + velocity + " km/h");
                System.out.println("-------------------------");
            

    }
      
    }

