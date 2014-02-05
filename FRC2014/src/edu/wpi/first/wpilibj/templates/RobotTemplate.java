
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
    final int PWM_L_VICTOR = 2;
    final int PWM_R_VICTOR = 6;
    final int PWM_L_VICTOR_2 = 7;
    final int PWM_R_VICTOR_2 = 1;
    final double DBW = .3;
    //Choo-Choo
    final int CHOO_CHOO_VICTOR = 5;
    final double CHOO_CHOO_SPEED = 0.8;
    final int CHOO_CHOO_LIMIT = 3;
    //Arm
    final int ROLLER_VICTOR = 4;
    final double ROLLER_SPEED = 0.8;
    final int PHOTOELECTRIC_GATE = 2;
    final int SOLENOID_1 = 3;
    final int SOLENOID_2 = 4;
    final int SOLENOID_3 = 5;
    final int SOLENOID_4 = 6;
    final double ARM_TIME = .1;
    final int SWITCH_CHANNEL = 1;
    final int RELAY_CHANNEL = 7;
    //Glow
    final int GLOW_RELAY = 1;
    //Joysticks
    final int PILOT = 1;
    final int OP_CONTROL = 2;
    final int CHOO_CHOO_BUTTON = 1;
    final int ARM_UP = 4;
    final int ARM_DOWN = 3;
    final int ROLLER = 2;
    //Autonomous
    final double AUTO_WAIT_TIME = 3.2;
    final double AUTO_SPEED = 1.0;
    final int L_CHANNEL = 2;
    final int R_CHANNEL = 5;
    final int FORWARD_AXIS = 2;
    final int TURN_AXIS = 4;
    final int SLOW_BUTTON = 6;
    final int ROLLER_AUTO_TIME = 5;
    
    /* [CONSTRUCTORS] */
    // This is where we construct all of the objects we use in the code, such as Victors and DigitalInputs.
    
    //Drivetrain
    Victor L = new Victor(PWM_L_VICTOR);
    Victor R = new Victor(PWM_R_VICTOR);
    Victor L2 = new Victor(PWM_L_VICTOR_2);
    Victor R2 = new Victor(PWM_R_VICTOR_2);
    //Choo-Choo
    Victor ChooChoo = new Victor(CHOO_CHOO_VICTOR);
    
    
    // THIS CHANNEL CANNOT BE 1
    DigitalInput limitSwitch = new DigitalInput(CHOO_CHOO_LIMIT);
    
    //Arm
    Victor Roller = new Victor(ROLLER_VICTOR);
    DigitalInput Photogate = new DigitalInput(PHOTOELECTRIC_GATE);
    //Arm (Pneumatic)
    Solenoid Sol1 = new Solenoid(SOLENOID_1);
    Solenoid Sol2 = new Solenoid(SOLENOID_2);
    Solenoid Sol3 = new Solenoid(SOLENOID_3);
    Solenoid Sol4 = new Solenoid(SOLENOID_4);
    Compressor Compressor = new Compressor(SWITCH_CHANNEL, RELAY_CHANNEL);
    //Glow
    Relay Glow = new Relay(GLOW_RELAY);
    //Joysticks
    Joystick Pilot = new Joystick(PILOT);
    Joystick OpControl = new Joystick(OP_CONTROL);
    //Autonomous
    Timer timer = new Timer();
    
    /* [INITIALIZATIONS] */
    // This is where we initialize objects that are not yet constructed, 
    // such as the Driver Station and the LCDDisplay.
    
    /* [VARIABLE ASSIGNMENTS] */
    // This is where we initialize variables that will be used throughout the code.
    
    //Drivetrain
    double left, right, forward, turn;
    int rMotorSign, lMotorSign;
    boolean slow, isAuto;
    boolean armup = true;
    boolean armstate = false;
    boolean rollerstate = false;
    boolean rollermoving = false;
    
    DriverStation DvrStation = DriverStation.getInstance();
    DriverStationLCD LCDDisplay = DriverStationLCD.getInstance();
    
    
    public void robotInit(){
        
        //DvrStation = DriverStation.getInstance(); // Initialize the Driver Station.
        //LCDDisplay = DriverStationLCD.getInstance(); // Get instance of the LCD Screen.
        
    }
  
    public void autonomous() {
        
        if (isAuto){
        autonomous1();
        //autonomous2();
        }
        
    }
    
    public void autonomous1(){
    
        if (isAuto){
        Compressor.start(); // Start the Compressor.
        ChooChoo.set(CHOO_CHOO_SPEED); // Set the choo choo motor to the desired speed.
        
            if (limitSwitch.get()){ // If the limit switch is pressed:
                ChooChoo.set(0); // Set the choo choo motor 0.
            }
            
        L.set(-AUTO_SPEED); // Drive forward (all motors set to desired speed).
        L2.set(-AUTO_SPEED);
        R.set(AUTO_SPEED);
        R2.set(AUTO_SPEED);
        
        Timer.delay(AUTO_WAIT_TIME); // Wait a specified amount of time so the robot can keep driving.
        
        L.set(0); // Stop all motors so the robot stops moving.
        L2.set(0);
        R.set(0);
        R2.set(0);
        }
        
    }
    
    public void autonomous2(){
        
        if (isAuto){
        Compressor.start(); // Start the Compressor.
        ChooChoo.set(CHOO_CHOO_SPEED); // Set the choo choo motor to the desired speed.
        
        
            if (limitSwitch.get()){ // If the limit switch is pressed:
                ChooChoo.set(0); // Set the choo choo motor 0.
            }
        
        Sol1.set(false); // Move the pistons out (makes the arm go down)
        Sol3.set(false);
        Sol2.set(true);
        Sol4.set(true); 
        Timer.delay(ARM_TIME);
        Sol1.set(false); // Make the pistons stop moving.
        Sol3.set(false);
        Sol2.set(false);
        Sol4.set(false);
        
        Roller.set(ROLLER_SPEED);
        Timer.delay(ROLLER_AUTO_TIME);
        Roller.set(0);
        
        ChooChoo.set(CHOO_CHOO_SPEED); // Set the choo choo motor to the desired speed.
        
        
            if (limitSwitch.get()){ // If the limit switch is pressed:
                ChooChoo.set(0); // Set the choo choo motor 0.
            }
            
        L.set(-AUTO_SPEED); // Drive forward (all motors set to desired speed).
        L2.set(-AUTO_SPEED);
        R.set(AUTO_SPEED);
        R2.set(AUTO_SPEED);
        
        Timer.delay(AUTO_WAIT_TIME); // Wait a specified amount of time so the robot can keep driving.
        
        L.set(0); // Stop all motors so the robot stops moving.
        L2.set(0);
        R.set(0);
        R2.set(0);
        }
        
    }

    public void operatorControl() {
        
        Compressor.start(); // Start the compressor.
        Sol1.set(false); // Set all the solenoids to false (so the pistons aren't moving).
        Sol3.set(false);
        Sol2.set(false);
        Sol4.set(false);
        
        while (isOperatorControl() && isEnabled()){
            LCDDisplay.updateLCD();
            isAuto = false;
            arcadeDrive(); // Arcade-style Drivetrain
            //tankDrive(); // Tank-style Drivetrain
            //chooChoo(); // Our shooting mechanism (catapult)
            intakeArmPneumatic(); // Our intake system, but using pneumatics 
            //glow(); // Underglow to make our robot even more gorgeous
            //troubleshoot();
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
        R.set(-right); // Set the right motors to the right value,
        R2.set(-right); // but negative so the wheels go in the same direction.
        
        
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
    
    L.set(left); // Set the left motors to the left value.
    L2.set(left);
    R.set(-right); // Set the right motors to the right value,
    R2.set(-right); // but negative so the wheels spin in the same direction.
    
    }
    
    public void chooChoo(){
        
        if (OpControl.getRawButton(CHOO_CHOO_BUTTON)){ //If the shooting button is being pressed:
            ChooChoo.set(CHOO_CHOO_SPEED); // Set the choo choo motor to the desired speed.
            
            
            if (limitSwitch.get()){ // If the limit switch is pressed:
                ChooChoo.set(0); // Set the choo choo motor to 0.
                LCDDisplay.println(DriverStationLCD.Line.kUser3, 1, "Limit Switch Hit   ");
            }
            else {
                LCDDisplay.println(DriverStationLCD.Line.kUser3, 1, "Limit Switch Free");
            }
            
        }
        
    }
    
    public void intakeArmPneumatic(){
        
        if (OpControl.getRawButton(ARM_UP)){
            armstate = true;
        }
        
        else if ((!OpControl.getRawButton(ARM_UP)) && armstate) {
            toggleArm();
            armstate = false;
        }
        
        if (OpControl.getRawButton(ROLLER)){
            rollerstate = true;
        }
        
        else if ((!OpControl.getRawButton(ROLLER)) && rollerstate) {
            toggleRoller();
            rollerstate = false;
        }
        
    }
    
    public void glow(){
        
        Glow.set(Relay.Value.kForward); // Turn the LED underglow lights on.
        
    }
    
    public void toggleArm(){
        
        if (armup){
            armup = false;
            Sol1.set(false); // Move the pistons out.
            Sol3.set(false);
            Sol2.set(true);
            Sol4.set(true); 
            Timer.delay(ARM_TIME);
            Sol1.set(false); // Make the pistons stop moving.
            Sol3.set(false);
            Sol2.set(false);
            Sol4.set(false);
            LCDDisplay.println(DriverStationLCD.Line.kUser1, 1, "arm down");
            LCDDisplay.updateLCD();
            
        }
        else if (armup == false) {
            armup = true;
            Sol1.set(true); // Move the pistons in.
            Sol3.set(true);
            Sol2.set(false);
            Sol4.set(false); 
            Timer.delay(ARM_TIME);
            Sol1.set(false); // Make the pistons stop moving.
            Sol3.set(false);
            Sol2.set(false);
            Sol4.set(false);
            LCDDisplay.println(DriverStationLCD.Line.kUser1, 1, "arm up   ");
            LCDDisplay.updateLCD();
        }
        
    }
    
    public void toggleRoller(){
        
        if (rollermoving){
            rollermoving = false;
            Roller.set(0);
            LCDDisplay.println(DriverStationLCD.Line.kUser2, 1, "roller stopped");
            LCDDisplay.updateLCD();
            
        }
        else if (rollermoving == false) {
            rollermoving = true;
            Roller.set(ROLLER_SPEED);
            LCDDisplay.println(DriverStationLCD.Line.kUser2, 1, "roller started");
            LCDDisplay.updateLCD();
        }
        
    }
    
    public void LCDscreen(){
        
        
        
    }
    
    public void catcher(){
    
    }
    
    public void troubleshoot(){
        
    }
    
}

// RECENT CHANGES (2/3/14)
//
// - Added second autonomous mode and isAuto boolean to prevent autonomous from continuing during TeleOp mode
// - Changed the name of the constructed Limit Switch in hopes of it removing whatever error occured
// - Added toggle buttons for Arm and Roller.
