/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.*;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends RobotBase {
    private Subsystem robotDrive;
    
        final int PWM_L1_VICTOR = 4;
        final int PWM_L2_VICTOR = 5;
        final int PWM_L3_VICTOR = 6;
        final int PWM_R1_VICTOR = 7;
        final int PWM_R2_VICTOR = 8;
        final int PWM_R3_VICTOR = 9;
        final int PWM_S1_VICTOR = 10;
        final int PWM_S2_VICTOR = 3;
        final int LIGHT_RELAY = 1;
        //final int FEED_RELAY = 1;
        final int SHOOTER_LIMIT_SWITCH_PORT = 13;
        final int SOLENOID_1 = 6;
        final int SOLENOID_2 = 5;
        final int C_SOLENOID_1 = 1;
        final int C_SOLENOID_2 = 2;
        
        //Drive Train
        final double DEAD_BAND_WIDTH = .25;
        final double AUTO_MOVE_SPEED = .5;
        final int AUTO_MOVE_TIME = 1;
        final double AUTO_TURN_TIME = .75;
        final int AUTO_TURN_SPEED = 1;
        
        //Feed
        final double FEED_TIMER_GOAL = .2486;
        final double TIMER_CORRECTION = -.03;
        final double FEEDER_TIME = .15;
        final double FEEDER_FUN_TIME = .03;
        final double POST_FEEDER_WAIT = .04;
        final double POST_3RD_SHOT = .25;
        final double SOLENOID_DELAY_TIME = .25;
        final double AUTO_SOLENOID_DELAY_TIME = 1;
        final int SOLENOID_POST_DELAY_TIME = 500;
        
        //Climb
        final int CSOLENOID_DELAY_TIME = 10;
        
        //Joystick Assignments
        final int PILOT = 1;
        final int OP_CONTROL = 2;
        
        //Button Assignments
        final int AUTO_AIM = 1;
        final int FEED = 6;
        final int SHOOT = 5;
        final int SLOW_BUTTON = 5;
        final int CLIMB_UP = 2;
        final int CLIMB_DOWN = 1;
        
        //Axis Assignments
        final int FORWARD_AXIS = 2;
        final int TURN_AXIS = 4;
        
        //Victor Power Assignments
        final double AUTO_AIM_POWER = .5;
        final double SHOOTER_POWER = .9;
        final double ARM_POWER = .85;
        
        DigitalInput limitSwitch;
        Relay feedRelay;
        Timer feederTimer;
         DriverStation DvrStation;
        DriverStationLCD LcdDisplay;
        RobotDrive myRobot;
        
        boolean isAuto;
        int autoShooting;
        Joystick Pilot;
        Joystick OpControl;
        double forward, turn, rval, lval, lSpeed, rSpeed;
        int rMotorSign, lMotorSign;
        boolean slow;
    int shootCount, shootRoundCount;
    double dif, val;
    boolean feederButtonDown, wasLimitDown, wasFeedDown;
        boolean shooterOpen;
        boolean firstTime;
    Victor L1Drive, L2Drive, L3Drive, R1Drive, R2Drive, R3Drive, shooterFront, shooterBack;
    Relay lightRelay;
    boolean shooterOn,isAutomode, autoDone;
    Solenoid solenoid1, solenoid2, cSolenoid1, cSolenoid2, cSolenoid3, cSolenoid4;
    public RobotTemplate(){
        
        
        Pilot = new Joystick(PILOT);
        OpControl = new Joystick(OP_CONTROL);
        
        autoDone = false;
        slow = false;
        
         L1Drive = new Victor(PWM_L1_VICTOR);
         L2Drive = new Victor(PWM_L2_VICTOR);
         L3Drive = new Victor(PWM_L3_VICTOR);
         R1Drive = new Victor(PWM_R1_VICTOR);
         R2Drive = new Victor(PWM_R2_VICTOR);
         R3Drive = new Victor(PWM_R3_VICTOR);
        
        
        shootCount = 0;
        shootRoundCount = 0;
        
        feederButtonDown = false;
        wasLimitDown = false;
        wasFeedDown = false;
        shooterOpen = true;
        firstTime = true;
        
        shooterFront = new Victor(PWM_S1_VICTOR);
        shooterBack = new Victor(PWM_S2_VICTOR);
        lightRelay = new Relay(LIGHT_RELAY);
        solenoid1 = new Solenoid(SOLENOID_1);
        solenoid2 = new Solenoid(SOLENOID_2);
        cSolenoid1 = new Solenoid(C_SOLENOID_1);
        cSolenoid2 = new Solenoid(C_SOLENOID_2);
       
        
    }
    
    public void autonomous(){
        //isAuto = true;
        //autoShooting = 0;
        shooterFront.set(-SHOOTER_POWER);
        shooterBack.set(-SHOOTER_POWER);
        lightRelay.set(Relay.Value.kForward);
        Timer.delay(4);
        solenoid1.set(true);
        solenoid2.set(false);
        Timer.delay(SOLENOID_DELAY_TIME);
        solenoid2.set(true);
        solenoid1.set(false);
        Timer.delay(AUTO_SOLENOID_DELAY_TIME);
        solenoid1.set(true);
        solenoid2.set(false);
        Timer.delay(SOLENOID_DELAY_TIME);
        solenoid2.set(true);
        solenoid1.set(false);
        Timer.delay(AUTO_SOLENOID_DELAY_TIME);
        solenoid1.set(true);
        solenoid2.set(false);
        Timer.delay(SOLENOID_DELAY_TIME);
        solenoid2.set(true);
        solenoid1.set(false);
        Timer.delay(AUTO_SOLENOID_DELAY_TIME);
        solenoid1.set(true);
        solenoid2.set(false);
        Timer.delay(SOLENOID_DELAY_TIME);
        solenoid2.set(true);
        solenoid1.set(false);
        
        //isAuto = false;
        lightRelay.set(Relay.Value.kOff);
        
        shooterFront.set(0);
        shooterBack.set(0);
        Timer.delay(1);
        
        L1Drive.set(-AUTO_MOVE_SPEED);
        L2Drive.set(-AUTO_MOVE_SPEED);
        L3Drive.set(-AUTO_MOVE_SPEED);
        R1Drive.set(AUTO_MOVE_SPEED);
        R2Drive.set(AUTO_MOVE_SPEED);
        R3Drive.set(AUTO_MOVE_SPEED);
        Timer.delay(AUTO_MOVE_TIME);
        
        L1Drive.set(0);
        L2Drive.set(0);
        L3Drive.set(0);
        R1Drive.set(0);
        R2Drive.set(0);
        R3Drive.set(0);
        
        L1Drive.set(AUTO_TURN_SPEED);
        L2Drive.set(AUTO_TURN_SPEED);
        L3Drive.set(AUTO_TURN_SPEED);
        R1Drive.set(AUTO_TURN_SPEED);
        R2Drive.set(AUTO_TURN_SPEED);
        R3Drive.set(AUTO_TURN_SPEED);
        Timer.delay(AUTO_TURN_TIME);
        
        L1Drive.set(0);
        L2Drive.set(0);
        L3Drive.set(0);
        R1Drive.set(0);
        R2Drive.set(0);
        R3Drive.set(0);
        Timer.delay(5);
    }
    public void startCompetition(){
        
        //autonomous();
        while(true){
            isAutomode = isAutonomous();
            if(isAutomode){
                autonomous();
            }
            //solenoid1.set(false);
        //solenoid2.set(true);
          
            drive();
            shoot();
            feed();
            climb();
        }
    }
    
    /*public void init(){
        robotDrive = new Drive();
        robotDrive.init();
    }*/
    /*
    public void startCompetition() {
     
        
        //autonomous();
        
        while(true){
            //solenoid1.set(false);
        //solenoid2.set(true);
          
            drive();
            shoot();
            feed();
            climb();
        }
    }
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    
    public void drive(){
    
    forward = Pilot.getRawAxis(FORWARD_AXIS);
    turn = -(Pilot.getRawAxis(TURN_AXIS));
    rval = forward + turn;
    lval = forward - turn;
    
    if (-DEAD_BAND_WIDTH < rval && rval < DEAD_BAND_WIDTH){
        rval = 0;
    }
    if (-DEAD_BAND_WIDTH < lval && lval < DEAD_BAND_WIDTH){
        lval = 0;
    }
    if (rval < 0){
        rMotorSign = -1;
    } else {
        rMotorSign = 1;
    }
    if (lval < 0){
        lMotorSign = -1;
    } else {
        lMotorSign = 1;
    }
    
    rval = rval*rval*rMotorSign;
    lval = lval*lval*lMotorSign;
    
    if (rval > 1){
        rval = 1;
    }
    if (lval > 1){
        lval = 1;
    }
    if (rval < -1){
        rval = -1;
    }
    if (lval < -1){
        lval = -1;
    }
    
    if (Pilot.getRawButton(SLOW_BUTTON)){
        slow = true;
    } else {
        slow = false;
    }
    if (slow){
        rval = rval * .35;
        lval = lval * .35;
    }
    
    L1Drive.set(-lval);
    L2Drive.set(-lval);
    L3Drive.set(-lval);
    R1Drive.set(rval);
    R2Drive.set(rval);
    R3Drive.set(rval);
    
}
public void shoot(){
    
    if (OpControl.getRawButton(SHOOT)){
        shooterOn = true;
    } else {
        shooterOn = false;
    }
    
    if (shooterOn){
        shooterFront.set(-SHOOTER_POWER);
        shooterBack.set(-SHOOTER_POWER);
        lightRelay.set(Relay.Value.kForward);
    } else {
        shooterFront.set(0);
        shooterBack.set(0);
        lightRelay.set(Relay.Value.kOff);
    }
}
public void feed() {
    /*if ((OpControl.getRawButton(FEED) || isAuto) && (feederButtonDown == false || isAuto) && shooterOpen) {
        if (isAuto) {
            Timer.delay(POST_FEEDER_WAIT);
        }
        shootCount ++;
        feedRelay.set(Relay.Value.kForward);
        feederButtonDown = true;
        shooterOpen = false;
        firstTime = false;
        feederTimer.reset();
        feederTimer.start();
    }
    
    else if (limitSwitch.get() == false && wasLimitDown == false && feederTimer.get() == FEEDER_TIME){
        feedRelay.set(Relay.Value.kReverse);
        Timer.delay(FEEDER_FUN_TIME/10);
        feedRelay.set(Relay.Value.kOff);
        feederButtonDown = false;
        shooterOpen = true;
        autoShooting ++;
        Timer.delay(POST_FEEDER_WAIT);    
    }
    
    if (limitSwitch.get() == false) {
        wasLimitDown = true;
    } else {
        wasLimitDown = false;
    }
    if (OpControl.getRawButton(FEED)){
        wasFeedDown = true;
    } else {
        wasFeedDown = false;
    }*/
    if (OpControl.getRawButton(FEED) || isAuto) {
        
        solenoid1.set(true);
        solenoid2.set(false);
        Timer.delay(SOLENOID_DELAY_TIME);
        solenoid2.set(true);
        solenoid1.set(false);
        autoShooting ++;
    }
}
public void climb(){
    if (OpControl.getRawButton(CLIMB_UP)) {
        cSolenoid1.set(true);
        cSolenoid2.set(false);
    }
    if (OpControl.getRawButton(CLIMB_DOWN)) {
        cSolenoid1.set(false);
        cSolenoid2.set(true);
    }
}

    
}            