
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class RobotTemplate extends SimpleRobot {
    
    /* [CONSTANTS] */ 
    // This is where we declare all of our constants for the Drivetrain, Choo-Choo, 
    // Arm, Glow, Joysticks, and Autonomous.
    
    //Drivetrain
    final int PWM_L_VICTOR = 10;
    final int PWM_R_VICTOR = 5; 
    final int PWM_L_VICTOR_2 = 9; 
    final int PWM_R_VICTOR_2 = 6; 
    final double DBW = .3;
    final double DRIFT_CONSTANT = .7;
    
    //Choo-Choo
    final int CHOO_CHOO_VICTOR = 2;
    final double CHOO_CHOO_SPEED = -1;
    final int CHOO_CHOO_LIMIT = 2;
    final double CHOO_CHOO_TIME = .4;
    final double CHOO_CHOO_STOP = .05;
    final double CHOO_CHOO_DELAY = .2;
    
    //Arm
    final int ROLLER_VICTOR = 7;
    final double ROLLER_SPEED_STOPPED = 1.0;
    final int SOLENOID_1 = 5; 
    final int SOLENOID_2 = 6; 
    final int SOLENOID_3 = 7;
    final int SOLENOID_4 = 8;
    final double ARM_TIME = .1;
    final int SWITCH_CHANNEL = 14;
    final int RELAY_CHANNEL = 3;
    
    //Glow
    final int GLOW_RELAY = 2;
    
    //Joysticks
    final int PILOT = 1;
    final int OP_CONTROL = 2;
    final int CHOO_CHOO_BUTTON = 1;
    final int ARM_TOGGLE = 4;
    final int CATCH = 3;
    final int ROLLER = 2;
    final int ROLLER_BACKWARDS = 6;
    final int FANCY_SHOOT_BUTTON = 6;
    final int PANIC_BUTTON = 5;
    
    //Autonomous
    final double AUTO_WAIT_TIME = 1;
    final double AUTO_SHOOT_DRIVE_TIME = .6;
    final double AUTO_SPEED = 1.0;
    final int L_CHANNEL = 2;
    final int R_CHANNEL = 5;
    final int FORWARD_AXIS = 2;
    final int TURN_AXIS = 4;
    final int SLOW_BUTTON = 6;
    final double ROLLER_AUTO_TIME = .3;
    final int AUTO_FEED_TIME = 3;
    final int AUTO_ARM_DOWN_TIME = 2;
    
    //Catch
    final int C_SOLENOID_1 = 1; 
    final int C_SOLENOID_2 = 2; 
    final double CATCH_TIME = .3;
    
    //Safety
    final double CHOO_CHOO_SAFETY = .1;
    final double SAFETY_TIME = 139.5;
    
    /* [CONSTRUCTORS] */
    // This is where we construct all of the objects we use in the code, such as Victors and DigitalInputs.
    
    //Drivetrain
    Victor L = new Victor(PWM_L_VICTOR);
    Victor R = new Victor(PWM_R_VICTOR);
    Victor L2 = new Victor(PWM_L_VICTOR_2);
    Victor R2 = new Victor(PWM_R_VICTOR_2);
    
    //Choo-Choo
    Victor ChooChoo = new Victor(CHOO_CHOO_VICTOR);
    DigitalInput limitSwitch = new DigitalInput(CHOO_CHOO_LIMIT); // This channel cannot be 1
    
    //Arm
    Victor Roller = new Victor(ROLLER_VICTOR);
    //Arm (Pneumatic)
    Solenoid Sol1 = new Solenoid(SOLENOID_1);
    Solenoid Sol2 = new Solenoid(SOLENOID_2);
    Solenoid Sol3 = new Solenoid(SOLENOID_3);
    Solenoid Sol4 = new Solenoid(SOLENOID_4);
    Compressor onCompressor = new Compressor(SWITCH_CHANNEL, RELAY_CHANNEL);
    
    //Glow
    Relay Glow = new Relay(GLOW_RELAY);
    
    //Joysticks
    Joystick Pilot = new Joystick(PILOT);
    Joystick OpControl = new Joystick(OP_CONTROL);
    
    //Autonomous
    Timer timer = new Timer();
    
    //Catch
    Solenoid cSolenoid1 = new Solenoid(C_SOLENOID_1);
    Solenoid cSolenoid2 = new Solenoid(C_SOLENOID_2);
    //Safety
    Timer matchTimer = new Timer();
    
    /* [INITIALIZATIONS] */
    // This is where we initialize objects that are not yet constructed, 
    // such as the Driver Station and the LCDDisplay.
    
    DriverStation DvrStation = DriverStation.getInstance();
    DriverStationLCD LCDDisplay = DriverStationLCD.getInstance();
    
    /* [VARIABLE ASSIGNMENTS] */
    // This is where we initialize variables that will be used throughout the code.
    
    //Values for Drivetrain/Shooter/Intake
    double left, right, forward, turn;
    int rMotorSign, lMotorSign;
    boolean slow, isAuto;
    boolean armup = false;
    boolean armstate = false;
    boolean rollerstate = false;
    boolean rollermoving = false;
    boolean catchstate = false;
    boolean catchopen = false;
    boolean shootercocked = true;
    boolean rollerbackstate = false;
    boolean panicButton = false;
    
    public void autonomous() {
        
        isAuto = true; // It is the autonomous mode.
        if (isAuto){  // If it is the autonomous mode:
            autonomous1(); // Run the autonomous mode.
            compress(); // Start the compressor.
            glow(); // Make the robot look soopah sexy.
            limitSwitch(); // Monitors the status of the limit switch.
        }
        
    }
    
    public void autonomous1(){
    
        if (isAuto){ // If it is the autonomous mode:
            L.set(-AUTO_SPEED); // Drive forward (all motors set to desired speed).
            L2.set(-AUTO_SPEED);
            R.set(AUTO_SPEED);
            R2.set(AUTO_SPEED);
            Timer.delay(AUTO_SHOOT_DRIVE_TIME); // Wait a specified amount of time so the robot can keep driving.
            L.set(0); // Stop all motors so the robot stops moving.
            L2.set(0);
            R.set(0);
            R2.set(0);
            shoot(); // Shoot the ball.
            LCDDisplay.println(DriverStationLCD.Line.kUser6, 1, "Shot ball    ");
            LCDDisplay.updateLCD();
            toggleArm(); // Move the arm up.
            L.set(AUTO_SPEED); // Drive backward (all motors set to desired speed).
            L2.set(AUTO_SPEED);
            R.set(-AUTO_SPEED);
            R2.set(-AUTO_SPEED);
            Timer.delay(AUTO_WAIT_TIME); // Wait a specified amount of time so the robot can keep driving.
            L.set(0); // Stop all motors so the robot stops moving.
            L2.set(0);
            R.set(0);
            R2.set(0);
        }
        
    }

    public void operatorControl() {
        
        matchTimer.start(); // Start the match timer.
        Sol1.set(false); // Set all the solenoids to false (so the pistons aren't moving).
        Sol3.set(false);
        Sol2.set(false);
        Sol4.set(false);
        while (isOperatorControl() && isEnabled()){ // During the tele-op mode:
            LCDDisplay.updateLCD(); // Update the LCD Screen.
            isAuto = false; // It is not the autonomous mode.
            arcadeDrive(); // Arcade-style Drivetrain
            //tankDrive(); // Tank-style Drivetrain
            intakeArm(); // Our intake system, but using pneumatics 
            glow(); // Underglow to make our robot even more gorgeous
            catcher(); // Our catching system with pneumatics
            limitSwitch(); // Monitors the status of the limit switch
            compress();
            shooterPanic();
            if(panicButton == false){ // Checks to see if panic button has been pressed and lets shooter run if not toggled
                chooChoo();// Our shooting mechanism (catapult)
            }
            if (matchTimer.get() >= SAFETY_TIME && matchTimer.get() <= (SAFETY_TIME+2)) {// Waits until the end of the match
                                                                                          // to disable the robot and fixes the
                                                                                          // shooter+catcher+arm positions
                safety();
            }
        }
        
    }
    
    public void tankDrive(){
        
        left = Pilot.getRawAxis(L_CHANNEL); // Set the left value equal to the value from the left analog stick.
        right = Pilot.getRawAxis(R_CHANNEL); // Set the right value equal to the value from the right analog stick.
        
        if ((left < DBW) && (left > -DBW)){ // If the left value is in this interval:
            L.set(0); // Set all the left motors to 0.
            L2.set(0);
        }
        
        if ((right < DBW) && (right > -DBW)){ // If the right value is in this interval:
            R.set(0); // Set all the right motors to 0.
            R2.set(0);
        }
   
        L.set(left); // Set the left motors to the left value.
        L2.set(left);
        R.set(-right); // Set the right motors to the right value, but negative so the wheels go in the same direction.
        R2.set(-right); 
        
    }
    
    public void arcadeDrive(){
        
        forward = Pilot.getRawAxis(FORWARD_AXIS); // The forward axis is equal to the y-axis of the left analog stick.
        turn = Pilot.getRawAxis(TURN_AXIS); // The turn axis is equal to the x-axis of the right analog stick.
        right = forward + turn; // The right value is equal to the foward value plus the turn value.
        left = forward - turn; // The left value is equal to the forward value minus the turn value.
    
        if (-DBW < right && right < DBW){ // If the right value is in this interval:
            right = 0; // Set the right motors to 0.
        }
    
        if (-DBW < left && left < DBW){ // If the left value is in this interval:
            left = 0; // Set the left motors to 0.
        }
    
        if (right < 0){ // If the right value is less than zero:
            rMotorSign = -1; // The right motor sign is equal to negative one.
        } 
    
        else { // If the right value is not less than zero:
            rMotorSign = 1; // The right motor sign is equal to one.
        }
    
        if (left < 0){ // If the left value is less than zero:
            lMotorSign = -1; // The left motor sign is equal to negative one.
        } 
    
        else { // If the left value is not less than zero:
            lMotorSign = 1; // The left motor sign is equal to one.
        }
    
        right = right*right*rMotorSign; // We square the left and right values in order to get a smoother drivetrain.
        left = left*left*lMotorSign; // This is a very simple version of a PID controller.
    
        if (right > 1){ // If the right value ends up being greater than one:
            right = 1; // Set the right value equal to one.
        }
    
        if (left > 1){ // If the left value ends up being greater than one:
            left = 1; // Set the left value equal to one.
        }
    
        if (right < -1){ // If the right value ends up being less than negative one:
            right = -1; // Set the right value equal to negative one.
        }
    
        if (left < -1){ // If the left value ends up being less than negative one:
            left = -1; // Set the left value equal to negative one.
        }
    
        if (Pilot.getRawButton(SLOW_BUTTON)){ // If the slow button is being pressed:
            slow = true; // Set the slow boolean to true.
        } 
    
        else { // If the slow button is not being pressed:
            slow = false; // Set the slow boolean to false.
        }
    
        if (slow){ // If the slow boolean is true:
            right = right * .35; // The right and left motors will have 35% of their usual power.
            left = left * .35;
        }
        
        right = right*DRIFT_CONSTANT;
        
        L.set(left); // Set the left motors to the left value.
        L2.set(left);
        R.set(-right); // Set the right motors to the right value, but negative so the wheels spin in the same direction.
        R2.set(-right); 
    
    }
    
    public void chooChoo(){
        
        if (OpControl.getRawButton(CHOO_CHOO_BUTTON)){ // If the shooting button is being pressed:
            shoot(); // Shoot the ball.
        }
        if (Pilot.getRawButton(FANCY_SHOOT_BUTTON)){
            shoot(); // Shoot the ball.
        }
        
    }
    
    public void shoot(){
        
        if (!armup){ // If the arm is up:
            toggleArm();
        }
        ChooChoo.set(CHOO_CHOO_SPEED); // Move the catapult.
        shootercocked = false;
        Timer.delay(CHOO_CHOO_TIME);
        ChooChoo.set(0);
        Timer.delay(CHOO_CHOO_STOP);
        ChooChoo.set(CHOO_CHOO_SPEED);
    }
   
    public void limitSwitch(){
        
        if (limitSwitch.get() == false && (OpControl.getRawButton(CHOO_CHOO_BUTTON) == false) && (Pilot.getRawButton(FANCY_SHOOT_BUTTON) == false) && (shootercocked == false)){ // If the limit switch is pressed:
            Timer.delay(CHOO_CHOO_DELAY);
            ChooChoo.set(0); // Set the choo choo motor to 0.
            LCDDisplay.println(DriverStationLCD.Line.kUser3, 1, "LIMIT SWITCH: Hit   ");
            shootercocked = true;
        }
        else if (limitSwitch.get() == false && shootercocked && (OpControl.getRawButton(CHOO_CHOO_BUTTON)) || (Pilot.getRawButton(FANCY_SHOOT_BUTTON))){
            shoot();
        }
        else {
            LCDDisplay.println(DriverStationLCD.Line.kUser3, 1, "LIMIT SWITCH: Free   ");
        }
        
    }
        
    public void intakeArm(){
        
        if (OpControl.getRawButton(ARM_TOGGLE)){
            armstate = true;
        }
        
        else if ((!OpControl.getRawButton(ARM_TOGGLE)) && armstate) {
            toggleArm(); // Change the status of the arm (up/down).
            armstate = false;
        }
        
        if (OpControl.getRawButton(ROLLER)){
            rollerstate = true;
        }
        
        else if ((!OpControl.getRawButton(ROLLER)) && rollerstate) {
            toggleRoller(); // Change the status of the roller (moving/not moving).
            rollerstate = false;
        }
           if (OpControl.getRawButton(ROLLER_BACKWARDS)){
            rollerbackstate = true;
        }
        else if ((!OpControl.getRawButton(ROLLER_BACKWARDS)) && rollerbackstate){
            toggleRollerBack();
            rollerbackstate = false;
        }     
    }
    
    public void glow(){
        
        Glow.set(Relay.Value.kForward); // Turn the LED underglow lights on, and sexify the robot.
        
    }
    
    public void toggleArm(){
        
        if (armup){ // If the arm is up:
            armup = false; // Move the arm down (pistons go out).
            Sol2.set(false); 
            Sol4.set(false);
            Sol1.set(true);
            Sol3.set(true); 
            Timer.delay(ARM_TIME);
            Sol2.set(false); 
            Sol4.set(false);
            Sol1.set(false);
            Sol3.set(false);
            LCDDisplay.println(DriverStationLCD.Line.kUser1, 1, "Arm Up   ");
            LCDDisplay.updateLCD();
        }
        else if (armup == false) { // If the arm is down:
            armup = true; // Move the arm up (pistons go in).
            Sol2.set(true); 
            Sol4.set(true);
            Sol1.set(false);
            Sol3.set(false); 
            Timer.delay(ARM_TIME);
            Sol2.set(false); 
            Sol4.set(false);
            Sol1.set(false);
            Sol3.set(false);
            LCDDisplay.println(DriverStationLCD.Line.kUser1, 1, "Arm Down   ");
            LCDDisplay.updateLCD();
        }
        
    }
    
    public void toggleRoller(){
        
        if (rollermoving){ // If the roller is moving:
            rollermoving = false; // Stop the roller.
            Roller.set(0);
            LCDDisplay.println(DriverStationLCD.Line.kUser2, 1, "Roller Stopped            ");
            LCDDisplay.updateLCD();
        }
        else if (rollermoving == false) { // If the roller is not moving:
            rollermoving = true; // Start the roller.
            Roller.set(ROLLER_SPEED_STOPPED);
            LCDDisplay.println(DriverStationLCD.Line.kUser2, 1, "Roller Started    ");
            LCDDisplay.updateLCD();
        }
        
    }
    
    public void toggleRollerBack(){
        
        if (rollermoving){ // If the roller is moving:
            rollermoving = false; // Stop the roller.
            Roller.set(0);
            LCDDisplay.println(DriverStationLCD.Line.kUser2, 1, "ROLLER: Stopped            ");
            LCDDisplay.updateLCD();
        }
        else if (rollermoving == false) { // If the roller is not moving:
            rollermoving = true; // Start the roller.
            Roller.set(-ROLLER_SPEED_STOPPED);
            LCDDisplay.println(DriverStationLCD.Line.kUser2, 1, "ROLLER: Started Backwards   ");
            LCDDisplay.updateLCD();
        }
        
    }  
    
    public void catcher(){
        
        if (OpControl.getRawButton(CATCH)){
            catchstate = true;
        }
        else if ((!OpControl.getRawButton(CATCH)) && catchstate) {
            toggleCatch(); // Change the status of the catcher (open/closed).
            catchstate = false;
        }
        
    }
    
    public void toggleCatch(){
        
        if (catchopen == false){ // If the catcher is closed:
            catchopen = true; // Open the catcher.
            cSolenoid2.set(false); 
            cSolenoid1.set(true);
            Timer.delay(CATCH_TIME);
            cSolenoid2.set(false); 
            cSolenoid1.set(false);
            if (armup == false){ // If the arm is up:
                toggleArm(); // Move the arm down.
            }
            LCDDisplay.println(DriverStationLCD.Line.kUser4, 1, "Catcher Open    ");
            LCDDisplay.updateLCD();
            
        }
        else if (catchopen) { // If the catcher is open:
            catchopen = false; // Close the catcher.
            cSolenoid2.set(true); 
            cSolenoid1.set(false);
            Timer.delay(ARM_TIME);
            cSolenoid2.set(false); 
            cSolenoid1.set(false);
            if (armup == true){ // If the arm is down:
                toggleArm(); // Move the arm up.
            }
            LCDDisplay.println(DriverStationLCD.Line.kUser4, 1, "Catcher Closed     ");
            LCDDisplay.updateLCD();
        }
    }
    
    public void compress(){
        
        onCompressor.start(); // Start the compressor.
        if (onCompressor.getPressureSwitchValue()){
            LCDDisplay.println(DriverStationLCD.Line.kUser5, 1, "COMPRESSOR: Off     ");
        }
        else if (onCompressor.getPressureSwitchValue() == false){
            LCDDisplay.println(DriverStationLCD.Line.kUser5, 1, "COMPRESSOR: On    ");
        }        
    
    }
    
    public void safety(){
        
        ChooChoo.set(CHOO_CHOO_SPEED); // Move the catapult up a bit.
        Timer.delay(CHOO_CHOO_SAFETY);
        ChooChoo.set(0);
        
        if (catchopen){ // If the catcher is open, close the catcher.
            toggleCatch();
        }
        else if (armup){
            toggleArm();
        }
        
    }
    
    public void shooterPanic(){
        
        if(OpControl.getRawButton(PANIC_BUTTON)){
            
            if(panicButton){
                
                panicButton = false;
            }
            else{
                
                panicButton = true;
            }
            
        }
        
    }
    
}

// THINGS TO DO:
// - Test Safety Function Again
// - Test Choo-Choo
// - Tune Autonomous Mode
