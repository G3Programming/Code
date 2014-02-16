
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
    /*final int PWM_L_VICTOR = 6;
    final int PWM_R_VICTOR = 10;
    final int PWM_L_VICTOR_2 = 1; // should be 7
    final int PWM_R_VICTOR_2 = 9; // should be 1
    final int PWM_L_VICTOR_3 = 5; 
    final int PWM_R_VICTOR_3 = 3;*/
    final int PWM_L_VICTOR = 5;
    final int PWM_L_VICTOR_2 = 6;
    final int PWM_R_VICTOR = 10;
    final int PWM_R_VICTOR_2 = 9; 
    final int PWM_R_VICTOR_3 = 4;
    final int PWM_L_VICTOR_3 = 3; 
    final double DBW = .3;
    
    //Choo-Choo
    final int CHOO_CHOO_VICTOR = 2; //should be 2
    final double CHOO_CHOO_SPEED = 0.8;
    final int CHOO_CHOO_LIMIT = 2;
    final double CHOO_CHOO_DELAY_TIME = .5;
    //Arm
    final int ROLLER_VICTOR = 7;
    final double ROLLER_SPEED_STOPPED = 0.5;
    final double ROLLER_SPEED_MOVING = .7;
    final int SOLENOID_1 = 1;
    final int SOLENOID_2 = 2;
    final int SOLENOID_3 = 7;
    final int SOLENOID_4 = 8;
    final double ARM_TIME = .1;
    final int SWITCH_CHANNEL = 14;
    final int RELAY_CHANNEL = 3;
    //Glow
    final int GLOW_RELAY = 1;
    //Joysticks
    final int PILOT = 1;
    final int OP_CONTROL = 2;
    final int CHOO_CHOO_BUTTON = 1;
    final int ARM_TOGGLE = 4;
    final int CATCH = 3;
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
    //Catch
    final int C_SOLENOID_1 = 5; //maybe 3
    final int C_SOLENOID_2 = 6; //maybe 4
    final double CATCH_TIME = .3;
    
    /* [CONSTRUCTORS] */
    // This is where we construct all of the objects we use in the code, such as Victors and DigitalInputs.
    
    //Drivetrain
    Victor L = new Victor(PWM_L_VICTOR);
    Victor R = new Victor(PWM_R_VICTOR);
    Victor L2 = new Victor(PWM_L_VICTOR_2);
    Victor R2 = new Victor(PWM_R_VICTOR_2);
    Victor L3 = new Victor (PWM_L_VICTOR_3);
    Victor R3 = new Victor (PWM_R_VICTOR_3);
    //Choo-Choo
    Victor ChooChoo = new Victor(CHOO_CHOO_VICTOR);
    
    // THIS CHANNEL CANNOT BE 1
    DigitalInput limitSwitch = new DigitalInput(CHOO_CHOO_LIMIT);
    
    //Arm
    Victor Roller = new Victor(ROLLER_VICTOR);
    //Arm (Pneumatic)
    Solenoid Sol1 = new Solenoid(SOLENOID_1);
    Solenoid Sol2 = new Solenoid(SOLENOID_2);
    Solenoid Sol3 = new Solenoid(SOLENOID_3);
    Solenoid Sol4 = new Solenoid(SOLENOID_4);
    Compressor onCompressor = new Compressor(SWITCH_CHANNEL, RELAY_CHANNEL);
    //Relay compressor = new Relay(RELAY_CHANNEL);
    //DigitalInput switcher = new DigitalInput(SWITCH_CHANNEL);
    
    
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
    
    /* [INITIALIZATIONS] */
    // This is where we initialize objects that are not yet constructed, 
    // such as the Driver Station and the LCDDisplay.
    
    /* [VARIABLE ASSIGNMENTS] */
    // This is where we initialize variables that will be used throughout the code.
    
    //Drivetrain
    double left, right, forward, turn;
    int rMotorSign, lMotorSign;
    boolean slow;
    boolean armup = true;
    boolean armstate = false;
    boolean rollerstate = false;
    boolean rollermoving = false;
    boolean robotmoving = false;
    boolean catchstate = false;
    boolean catchopen = false;
    
    DriverStation DvrStation = DriverStation.getInstance();
    DriverStationLCD LCDDisplay = DriverStationLCD.getInstance();
    
    
    
  
    public void autonomous() {
        
        //isAuto = true;
        
        //if (isAuto){
            //autonomous1();
            //autonomous2();
            //compress();
        //}
        
        
    }
    
    public void autonomous1(){
    
        //if (isAuto){
        //Compressor.start(); // Start the Compressor.
        ChooChoo.set(CHOO_CHOO_SPEED); // Set the choo choo motor to the desired speed.
        
            if (limitSwitch.get() == false){ // If the limit switch is pressed:
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
        //}
        
    }
    
    public void autonomous2(){
        
        //if (isAuto){
        //Compressor.start(); // Start the Compressor.
        ChooChoo.set(CHOO_CHOO_SPEED); // Set the choo choo motor to the desired speed.
        
        
            if (limitSwitch.get()){ // If the limit switch is pressed:
                ChooChoo.set(0); // Set the choo choo motor 0.
            }
        
        Sol2.set(false); // Move the pistons out (makes the arm go down)
        Sol4.set(false);
        Sol1.set(true);
        Sol3.set(true); 
        Timer.delay(ARM_TIME);
        Sol2.set(false); // Make the pistons stop moving.
        Sol4.set(false);
        Sol1.set(false);
        Sol3.set(false);
        
        Roller.set(ROLLER_SPEED_STOPPED);
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
        //}
        
    }

    public void operatorControl() {
        
        /*Compressor.start(); // Start the compressor.
        Sol1.set(false); // Set all the solenoids to false (so the pistons aren't moving).
        Sol3.set(false);
        Sol2.set(false);
        Sol4.set(false);*/
        onCompressor.start();
        //compressor.set(Relay.Value.kForward);
        while (isOperatorControl() && isEnabled()){
            
            LCDDisplay.updateLCD();
            //isAuto = false;
            arcadeDrive(); // Arcade-style Drivetrain
            //tankDrive(); // Tank-style Drivetrain
            //chooChoo(); // Our shooting mechanism (catapult)
            intakeArmPneumatic(); // Our intake system, but using pneumatics 
            //glow(); // Underglow to make our robot even more gorgeous
            catcher();
            troubleshoot();
            if ((R.get() >= DBW || R2.get() >= DBW || R.get() <= -DBW || R2.get() <= -DBW) || (L.get() >= DBW || L2.get() >= DBW || L.get() <= -DBW || L2.get() <= -DBW)) {
                robotmoving = true;
            }
            else {
                robotmoving = false;
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
        R.set(-right); // Set the right motors to the right value,
        R2.set(-right); // but negative so the wheels go in the same direction.
        //L3.set(left);
        //R3.set(-right);
        
        
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
    L3.set(left);
    R3.set(-right);
    
    
    }
    
    public void chooChoo(){
        
        if (OpControl.getRawButton(CHOO_CHOO_BUTTON)){ //If the shooting button is being pressed:
            ChooChoo.set(CHOO_CHOO_SPEED); // Set the choo choo motor to the desired speed.
            
            
            if (limitSwitch.get() == false){ // If the limit switch is pressed:
                /*ChooChoo.set(-CHOO_CHOO_SPEED);
                Timer.delay(CHOO_CHOO_DELAY_TIME);*/
                ChooChoo.set(0); // Set the choo choo motor to 0.
                LCDDisplay.println(DriverStationLCD.Line.kUser3, 1, "Limit Switch Hit   ");
            }
            else if (limitSwitch.get() == true){
                LCDDisplay.println(DriverStationLCD.Line.kUser3, 1, "Limit Switch Free   ");
            }
            
        }
        
    }
    
    public void intakeArmPneumatic(){
        
        if (OpControl.getRawButton(ARM_TOGGLE)){
            armstate = true;
        }
        
        else if ((!OpControl.getRawButton(ARM_TOGGLE)) && armstate) {
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
            Sol2.set(false); // Move the pistons out.
            Sol4.set(false);
            Sol1.set(true);
            Sol3.set(true); 
            Timer.delay(ARM_TIME);
            Sol2.set(false); // Make the pistons stop moving.
            Sol4.set(false);
            Sol1.set(false);
            Sol3.set(false);
            LCDDisplay.println(DriverStationLCD.Line.kUser1, 1, "Arm Down   ");
            LCDDisplay.updateLCD();
            
        }
        else if (armup == false) {
            armup = true;
            Sol2.set(true); // Move the pistons in.
            Sol4.set(true);
            Sol1.set(false);
            Sol3.set(false); 
            Timer.delay(ARM_TIME);
            Sol2.set(false); // Make the pistons stop moving.
            Sol4.set(false);
            Sol1.set(false);
            Sol3.set(false);
            LCDDisplay.println(DriverStationLCD.Line.kUser1, 1, "Arm Up   ");
            LCDDisplay.updateLCD();
        }
        
    }
    
    public void toggleRoller(){
        
        if (rollermoving){
            rollermoving = false;
            Roller.set(0);
            LCDDisplay.println(DriverStationLCD.Line.kUser2, 1, "Roller Stopped            ");
            LCDDisplay.updateLCD();
        }
        else if (rollermoving == false && robotmoving == false) {
            rollermoving = true;
            Roller.set(ROLLER_SPEED_STOPPED);
            LCDDisplay.println(DriverStationLCD.Line.kUser2, 1, "Roller Started Slower    ");
            LCDDisplay.updateLCD();
        }
        else if (rollermoving == false && robotmoving == true){
            rollermoving = true;
            Roller.set(ROLLER_SPEED_MOVING);
            LCDDisplay.println(DriverStationLCD.Line.kUser2, 1, "Roller Started Faster    ");
            LCDDisplay.updateLCD();
        }
        
    }
    
  
    
    public void catcher(){
        
        if (OpControl.getRawButton(CATCH)){
            catchstate = true;
        }
        
        else if ((!OpControl.getRawButton(CATCH)) && catchstate) {
            toggleCatch();
            catchstate = false;
        }
    }
    
    public void toggleCatch(){
        if (catchopen == false){
            catchopen = true;
            cSolenoid2.set(false); // Move the pistons out.
            cSolenoid1.set(true);
            Timer.delay(CATCH_TIME);
            cSolenoid2.set(false); // Make the pistons stop moving.
            cSolenoid1.set(false);
            toggleArm();
            LCDDisplay.println(DriverStationLCD.Line.kUser4, 1, "Catcher Open    ");
            LCDDisplay.updateLCD();
            
        }
        else if (catchopen) {
            catchopen = false;
            cSolenoid2.set(true); // Move the pistons in.
            cSolenoid1.set(false);
            Timer.delay(ARM_TIME);
            cSolenoid2.set(false); // Make the pistons stop moving.
            cSolenoid1.set(false);
            
            LCDDisplay.println(DriverStationLCD.Line.kUser4, 1, "Catcher Closed     ");
            LCDDisplay.updateLCD();
            
            toggleArm();
        }
    }
    
    public void troubleshoot(){
        /*if (switcher.get() == false){
            LCDDisplay.println(DriverStationLCD.Line.kUser6, 1, "Switch False   ");
        }
        else if (switcher.get() == true){
            LCDDisplay.println(DriverStationLCD.Line.kUser6, 1, "Switch True    ");
        }*/
        if (onCompressor.getPressureSwitchValue()){
            LCDDisplay.println(DriverStationLCD.Line.kUser6, 1, "True     ");
        }
        else if (onCompressor.getPressureSwitchValue() == false){
            LCDDisplay.println(DriverStationLCD.Line.kUser6, 1, "False    ");
        }
        
    }
    
    public void compress(){
        //onCompressor.start();
    }
    
}

// THINGS TO DO:
// - Intake with Pneumatics
// - Autonomous mode
