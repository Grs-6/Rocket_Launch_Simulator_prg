import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;

public class Rocket_Launch_Stimulator {
    private static final int UPDATE_INTERVAL_MS = 1000; // 1 second

    // data to be displayed
    private static int stage = 1;
    private static double altitude = 0.0;
    private static double velocity = 0.0;
    private static double fuelLevel = 100.0;
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
                    boolean launchReady = performPreLaunchChecks(scanner);

                    if (launchReady) {
                        preLaunchChecksPassed = true;
                    } else {
                        System.out.println("Launch postponed due to failed pre-launch checks.");
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
            if (userInput.startsWith("fast forward ")) {
                try {
                    int fastForwardSeconds = Integer.parseInt(userInput.split(" ")[2]);
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

        }
        scanner.close();
    }

    private static class UpdateTask extends TimerTask {
        @Override
        public void run() {
            // checks for destination
            if (altitude >= 100 || fuelLevel < 0) {
                cancel(); // timmer stop
                if (altitude == 100) {
                    System.out.println("Orbit achieved.Mission Successful");
                } else {
                    System.out.println("Mission Failed");
                }
                System.exit(0); // Terminate
            } else {

                // Simulation
                altitude += 10;
                velocity += 1000;
                fuelLevel -= 10;
                System.out.println("Stage: " + stage);
                System.out.println("Fuel Level: " + fuelLevel + "%");
                System.out.println("Altitude: " + altitude + " km");
                System.out.println("Speed: " + velocity + " km/h");
                System.out.println("-------------------------");

                int newStage = calculateStage(altitude);
                if (newStage != stage) {
                    stage = newStage;
                    System.out
                            .println("Stage " + (stage - 1) + " completed. Separating stage. Entering stage " + stage);
                }
            }
        }

        private int calculateStage(double currentAltitude) {
            // range for 4 stages
            double[] stageThresholds = { 25, 50, 75, 100 };
            for (int i = 0; i < stageThresholds.length; i++) {
                if (currentAltitude <= stageThresholds[i]) {
                    return i + 1;
                }
            }

            return stageThresholds.length + 1;
        }
    }

    private static boolean performPreLaunchChecks(Scanner scanner) {

        boolean weatherCriteria = checkWeather();
        boolean Fuel_Level = checkFuel_Level();
        boolean safetyCriteria = checkSafety();

        System.out.println("Pre-Launch Criteria Assessment:");
        System.out.println("1. Weather Criteria: " + (weatherCriteria ? "Go" : "No-Go"));
        System.out.println("2. Fuel Level: " + (Fuel_Level ? "Go" : "No-Go"));
        System.out.println("3. Safety Criteria: " + (safetyCriteria ? "Go" : "No-Go"));

        if (weatherCriteria && Fuel_Level && safetyCriteria) {
            System.out.println("\nAll criteria are met. Go for launch.");
            return true;
        } else {
            System.out.println("\nOne or more criteria are not met. Launch is postponed (No-Go).");
            return false;
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
        if (fuelLevel == 100) {
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
        long startTime = System.currentTimeMillis();
        long lastUpdateTime = startTime;

        while (true) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = (currentTime - startTime) / 1000; // seconds

            if (elapsedTime >= fastForwardSeconds) {
                //to display
                new UpdateTask().run();
                break;
            }
        }
    }
}
