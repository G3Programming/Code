
package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;


public class RobotTemplate extends SimpleRobot{
   
        /* [CONSTANTS] */
    
        //Ports and Channels
        final int PWM_L1_VICTOR = 4;
        final int PWM_L2_VICTOR = 5;
        final int PWM_L3_VICTOR = 6;
        final int PWM_R1_VICTOR = 7;
        final int PWM_R2_VICTOR = 8;
        final int PWM_R3_VICTOR = 9;
        final int PWM_S1_VICTOR = 10;
        final int PWM_S2_VICTOR = 3;
        final int LIGHT_RELAY = 2;
        final int SHOOTER_LIMIT_SWITCH_PORT = 13;
        final int SOLENOID_1 = 6;
        final int SOLENOID_2 = 5;
        final int C_SOLENOID_1 = 1;
        final int C_SOLENOID_2 = 2;
        final int C_SOLENOID_3 = 3;
        final int C_SOLENOID_4 = 4;
        //Drivetrain
        final double DEAD_BAND_WIDTH = .25;
        final double AUTO_MOVE_SPEED = .5;
        final int AUTO_MOVE_TIME = 2;
        final int AUTO_TURN_TIME = 1;
        final int AUTO_TURN_SPEED = 1;
        //Feed
        final double FEED_TIMER_GOAL = .2486;
        final double TIMER_CORRECTION = -.03;
        final double FEEDER_TIME = .15;
        final double FEEDER_FUN_TIME = .03;
        final double POST_FEEDER_WAIT = .04;
        final double POST_3RD_SHOT = .25;
        final int SOLENOID_DELAY_TIME = 1000;
        final int SOLENOID_POST_DELAY_TIME = 500;
        //Climb
        final int CSOLENOID_DELAY_TIME = 1000;
        //Joystick
        final int PILOT = 1;
        final int OP_CONTROL = 2;
        final int AUTO_AIM = 1;
        final int FEED = 6;
        final int SHOOT = 5;
        final int SLOW_BUTTON = 5;
        final int CLIMB = 2;
        final int FORWARD_AXIS = 2;
        final int TURN_AXIS = 4;
        //Victor Power Assignments
        final double AUTO_AIM_POWER = .5;
        final double SHOOTER_POWER = .9;
        final double ARM_POWER = .85;
        final double ROLLER_POWER = .3;
        
        /* [INITIALIZATIONS] */
        
        DriverStation DvrStation;
        DriverStationLCD LcdDisplay;
        RobotDrive myRobot;
        
        /* [VARIABLE ASSIGNMENTS] */
        
        boolean isAuto;
        int autoShooting;
        double forward, turn, rval, lval, lSpeed, rSpeed;
        int rMotorSign, lMotorSign;
        boolean slow = false;
        int shootCount, shootRoundCount = 0;
        double dif, val;
        boolean feederButtonDown, wasLimitDown, wasFeedDown = false;
        boolean shooterOpen, firstTime = true;
        boolean shooterOn = false;
        
        /* [CONSTRUCTORS] */
        
        //Joystick
        Joystick Pilot = new Joystick(PILOT);
        Joystick OpControl = new Joystick(OP_CONTROL);
        //Drivetrain
        Victor L1Drive = new Victor(PWM_L1_VICTOR);
        Victor L2Drive = new Victor(PWM_L2_VICTOR);
        Victor L3Drive = new Victor(PWM_L3_VICTOR);
        Victor R1Drive = new Victor(PWM_R1_VICTOR);
        Victor R2Drive = new Victor(PWM_R2_VICTOR);
        Victor R3Drive = new Victor(PWM_R3_VICTOR);
        //Feed
        DigitalInput limitSwitch;
        Relay feedRelay;
        Timer feederTimer;
        //Shooter
        Victor shooterFront = new Victor(2, PWM_S1_VICTOR);
        Victor shooterBack = new Victor(2, PWM_S2_VICTOR);
        Relay lightRelay = new Relay(LIGHT_RELAY);
        //Climb
        Solenoid solenoid1 = new Solenoid(SOLENOID_1);
        Solenoid solenoid2 = new Solenoid(SOLENOID_2);
        Solenoid cSolenoid1 = new Solenoid(C_SOLENOID_1);
        Solenoid cSolenoid2 = new Solenoid(C_SOLENOID_2);
        Solenoid cSolenoid3 = new Solenoid(C_SOLENOID_3);
        Solenoid cSolenoid4 = new Solenoid(C_SOLENOID_4);
       
  
    public void autonomous() {
        
        isAuto = true; // Confirms that this is Autonomous mode.
        autoShooting = 0; // Sets the shooting count during autonomous to zero.
        
        shooterFront.set(SHOOTER_POWER); // Turn on both the shooter motors to the desired power.
        shooterBack.set(SHOOTER_POWER);
        lightRelay.set(Relay.Value.kForward); // Turns on the aiming light (photon cannon).
        
        Timer.delay(4); // Delays the shooting for four seconds to let the shooter motors get up to speed.
        
        while (autoShooting < 6){ // Feeds the frisbees 6 times.
            feed();
        }
        
        isAuto = false; // Returns the boolean isAuto to false
        lightRelay.set(Relay.Value.kOff); // Turns off the aiming light (photon cannon).
        shooterFront.set(0); // Turn off both of the shooter motors.
        shooterBack.set(0);
        
        L1Drive.set(-AUTO_MOVE_SPEED); // Sets the drivetrain motors to the autonomous speed.
        L2Drive.set(-AUTO_MOVE_SPEED); // (Robot drives backwards)
        L3Drive.set(-AUTO_MOVE_SPEED);
        R1Drive.set(AUTO_MOVE_SPEED);
        R2Drive.set(AUTO_MOVE_SPEED);
        R3Drive.set(AUTO_MOVE_SPEED);
        Timer.delay(AUTO_MOVE_TIME);
        
        L1Drive.set(0); // Sets the drivetrain motors to zero.
        L2Drive.set(0); // (Robot stops moving)
        L3Drive.set(0);
        R1Drive.set(0);
        R2Drive.set(0);
        R3Drive.set(0);
        
        L1Drive.set(AUTO_TURN_SPEED); // Sets the drivetrain motors to the turn speed.
        L2Drive.set(AUTO_TURN_SPEED); // (Robot turns around)
        L3Drive.set(AUTO_TURN_SPEED);
        R1Drive.set(AUTO_TURN_SPEED);
        R2Drive.set(AUTO_TURN_SPEED);
        R3Drive.set(AUTO_TURN_SPEED);
        Timer.delay(AUTO_TURN_TIME);
        
        L1Drive.set(0); // Sets the drivetrain motors to zero.
        L2Drive.set(0); // (Robot stops moving)
        L3Drive.set(0);
        R1Drive.set(0);
        R2Drive.set(0);
        R3Drive.set(0);
  
    }
    
    public void operatorControl() {
        
        isAuto = false; // Confirms that this is not during Autonomous mode.
        
        while (isOperatorControl() && isEnabled()){
            drive(); // Arcade-style Drivetrain
            shoot(); // Starts and Stops the shooting motors
            feed(); // Pneumatic piston feeds the frisbees
            climb(); // Pneumatic pistons move the climber up and down
        }

    }
    
public void drive(){
    
    forward = Pilot.getRawAxis(FORWARD_AXIS); // Set the value of forward equal to the values from the y-axis of the left analog stick.
    turn = Pilot.getRawAxis(TURN_AXIS); // Set the value of turn equal to the values from the x-axis of the right analog stick.
    rval = forward + turn; // The right value is equal to the forward value plus the turn value.
    lval = forward - turn; // The left value is equal to the forward value minus the turn value.
    
    if (-DEAD_BAND_WIDTH < rval && rval < DEAD_BAND_WIDTH){ // If the right value is in this interval:
        rval = 0; // Set the right value to zero.
    }
    
    if (-DEAD_BAND_WIDTH < lval && lval < DEAD_BAND_WIDTH){ // If the left value is in this interval:
        lval = 0; // Set the left value to zero.
    }
    
    if (rval < 0){ // If the right value is negative:
        rMotorSign = -1; // Set the right motor sign to negative one.
    } 
    
    else { // If the right value is not negative:
        rMotorSign = 1; // Set the right motor sign to positive one.
    }
    
    if (lval < 0){ // If the left value is negative:
        lMotorSign = -1; //Set the left motor sign to negative one.
    } 
    
    else { // If the left value is not negative:
        lMotorSign = 1; // Set the left motor sign to positive one.
    }
    
    rval = rval*rval*rMotorSign; // We square the right and left values and multiply them by their signs to get a smooth drivetrain.
    lval = lval*lval*lMotorSign; // This is a sort of simplified PID controller.
    
    if (rval > 1){ // If the right value is greater than one:
        rval = 1; // Set the right value equal to one.
    }
    
    if (lval > 1){ // If the left value is greater than one:
        lval = 1; // Set the left value equal to one.
    }
    
    if (rval < -1){ // If the right value is less than negative one:
        rval = -1; // Set the right value equal to negative one.
    }
    
    if (lval < -1){ // If the left value is less than negative one:
        lval = -1; // Set the left value equal to negative one.
    }
    
    if (Pilot.getRawButton(SLOW_BUTTON)){ // If the slow button is being pressed:
        slow = true; // Set the slow boolean equal to true.
    } 
    
    else { // If the slow button is not being pressed:
        slow = false; // Set the slow boolean equal to false.
    }
    
    if (slow){ // If the slow boolean is equal to true:
        rval = rval * .35; // Set the right and left motors speeds to 35% of their current power.
        lval = lval * .35;
    }
    
    L1Drive.set(-lval); // Set the left motors to the left speed.
    L2Drive.set(-lval);
    L3Drive.set(-lval);
    R1Drive.set(rval); // Set the right motors to the right speed.
    R2Drive.set(rval);
    R3Drive.set(rval);
    
}
public void shoot(){
    
    if (OpControl.getRawButton(SHOOT)){ // If the shooter button is being pressed:
        shooterOn = true; // The shooterOn boolean is equal to true.
    } 
    
    else { // If the shooter button is not being pressed:
        shooterOn = false; // The shooterOn boolean is equal to false.
    }
    
    if (shooterOn){ // If the shooterOn boolean is equal to true:
        shooterFront.set(SHOOTER_POWER); // Set each of the shooter motors to the desired speed.
        shooterBack.set(SHOOTER_POWER);
        lightRelay.set(Relay.Value.kForward); // Turn on the aiming light (photon cannon).
    } 
    
    else { // If the shooterOn boolean is equal to false:
        shooterFront.set(0); // Set each of the shooter motors to zero.
        shooterBack.set(0);
        lightRelay.set(Relay.Value.kOff); // Turn off the aiming light (photon cannon).
    }
    
}
public void feed() {
    
    if (OpControl.getRawButton(FEED) || isAuto) { // If the feed button is being pressed or it is during Autonomous mode:
        autoShooting ++; // Increase the autoShooting count by one.
        
        solenoid1.set(true); // Move the piston out and wait a certain amount of time.
        Timer.delay(SOLENOID_DELAY_TIME);
        solenoid1.set(false); // Move the piston in and wait a certain amount of time.
        solenoid2.set(true);
        Timer.delay(SOLENOID_DELAY_TIME);
        solenoid2.set(false); // Make the pistons stop moving.
    }
    
}
public void climb(){
    
    if (OpControl.getRawButton(CLIMB)) { // If the climb button is being pressed:
        cSolenoid1.set (true); // Move both pistons out for a certain amount of time.
        cSolenoid3.set (true);
        Timer.delay (CSOLENOID_DELAY_TIME);
        cSolenoid1.set (false); // Move both pistons in for a certain amount of time.
        cSolenoid2.set (true);
        cSolenoid3.set (false);
        cSolenoid4.set (true);
        Timer.delay (CSOLENOID_DELAY_TIME);
        cSolenoid2.set (false); // Make the pistons stop moving.
        cSolenoid4.set (false);
    }
    
}

}
