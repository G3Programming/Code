
package edu.wpi.first.wpilibj.templates;

//NOTE: This code is not complete yet. There are still bits to add, like complexity with the limit switch
//      (Graham, you'll remember the limit switch from last year and how much of a b**** it was) and the 
//      photoelectric sensor to sense when the ball is in the parameters of the arm (so that the roller can
//      then begin to roll automatically).
//
//      Something else that still needs to be added is another autonomous mode (hooked up with a physical
//      switch on the robot that will allow us to move between the two). This autonomous mode will consist of
//      basically the same code except that after we shoot the ball in our catapult we will pick up another
//      ball before moving (with the photoelectric sensor checking to see if we have it) and the robot will
//      shoot that ball before moving forward just like the first autonomous mode.
//
//      When editing this program, PLEASE PLEASE PLEASE do not and I repeat DO NOT delete anything. If you must
//      change something, please comment out the code that was there before but do not alter it in any way, shape,
//      or form in case we need to revert to it. All final deletions will be decided by me.
//
//      Thanks!
//
//      -Alex

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class RobotTemplate extends SimpleRobot {
   
    /*[Constants]*/
    
    //Drivetrain
    final int R1_DT_VICTOR = 1;
    final int R2_DT_VICTOR = 2;
    final int R3_DT_VICTOR = 3;
    final int L1_DT_VICTOR = 4;
    final int L2_DT_VICTOR = 5;
    final int L3_DT_VICTOR = 6;
    final double DBW = 0.1;
    //Choo-Choo
    final int CHOO_CHOO_VICTOR = 7;
    final double CHOO_CHOO_SPEED = 0.8;
    final int CHOO_CHOO_LIMIT = 1;
    //Arm
    final int ARM1_VICTOR = 8;
    final int ARM2_VICTOR = 9;
    final int ARM1_POT = 2;
    final int ARM2_POT = 3;
    final int ROLLER_VICTOR = 10;
    final double ROLLER_SPEED = 0.8;
    final double P_CONSTANT = 1;
    final double I_CONSTANT = 1;
    final double D_CONSTANT = 1;
    final int PHOTOELECTRIC_GATE = 2;
    //Glow
    final int GLOW_RELAY = 1;
    //Joysticks
    final int PILOT = 1;
    final int OP_CONTROL = 2;
    final int L_CHANNEL = 2;
    final int R_CHANNEL = 5;
    final int CHOO_CHOO_BUTTON = 2;
    final int ARM_UP = 4;
    final int ARM_DOWN = 1;
    //Autonomous
    final double AUTO_WAIT_TIME = 3.2;
    final double AUTO_SPEED = 1.0;
    
    /*[Constructors]*/
    
    //Drivetrain
    Victor R1DT = new Victor(R1_DT_VICTOR);
    Victor R2DT = new Victor(R2_DT_VICTOR);
    Victor R3DT = new Victor(R3_DT_VICTOR);
    Victor L1DT = new Victor(L1_DT_VICTOR);
    Victor L2DT = new Victor(L2_DT_VICTOR);
    Victor L3DT = new Victor(L3_DT_VICTOR);
    //Choo-Choo
    Victor ChooChoo = new Victor(CHOO_CHOO_VICTOR);
    DigitalInput Limit = new DigitalInput(CHOO_CHOO_LIMIT);
    //Arm
    Victor Arm1 = new Victor(ARM1_VICTOR);
    Victor Arm2 = new Victor(ARM2_VICTOR);
    AnalogChannel Pot1 = new AnalogChannel(ARM1_POT);
    AnalogChannel Pot2 = new AnalogChannel(ARM2_POT);
    PIDController PID1 = new PIDController(P_CONSTANT, I_CONSTANT, D_CONSTANT, Pot1, Arm1);
    PIDController PID2 = new PIDController(P_CONSTANT, I_CONSTANT, D_CONSTANT, Pot2, Arm2);
    Victor Roller = new Victor(ROLLER_VICTOR);
    DigitalInput Photogate = new DigitalInput(PHOTOELECTRIC_GATE);
    //Glow
    Relay Glow = new Relay(GLOW_RELAY);
    //Joysticks
    Joystick Pilot = new Joystick(PILOT);
    Joystick OpControl = new Joystick(OP_CONTROL);
    //Autonomous
    Timer timer = new Timer();
    
    /*[Variable Assignments]*/
    
    //Drivetrain
    double rValue;
    double lValue;
    //Arm
    double voltageGoal1;
    double voltageGoal2;
    
    public void autonomous() {
        ChooChoo.set(CHOO_CHOO_SPEED); //Set the choo choo motor to the desired speed
            if (Limit.get()){ //If the limit switch is pressed:
                ChooChoo.set(0); //Set the choo choo motor 0.
            }
            
        L1DT.set(-AUTO_SPEED); //Drive forward (all motors set to desired speed)
        L2DT.set(-AUTO_SPEED);
        L3DT.set(-AUTO_SPEED);
        R1DT.set(AUTO_SPEED);
        R2DT.set(AUTO_SPEED);
        R3DT.set(AUTO_SPEED);
        
        Timer.delay(AUTO_WAIT_TIME); //Wait a specified amount of time so robot can keep driving
        
        L1DT.set(0); //Stop all motors so robot stops moving.
        L2DT.set(0);
        L3DT.set(0);
        R1DT.set(0);
        R2DT.set(0);
        R3DT.set(0);
    }

    public void operatorControl() {

        while (isOperatorControl() && isEnabled()){
            drive(); //Operates the drivetrain (tank drive) with two analog sticks
            chooChoo(); //Our shooting mechanism (catapult)
            intakeArm(); //Our intake system (passive roller on a moving arm)
            glow(); //Underglow to make our robot even more gorgeous
        }
    }
    
    public void test() {
    
    }
    
    public void drive() {
        lValue = Pilot.getRawAxis(L_CHANNEL); //Gets the value from the left analog stick.
        rValue = Pilot.getRawAxis(R_CHANNEL); //Gets the value from the right analog stick.
        
        if ((lValue < DBW) && (lValue > -DBW)){ //If the value from the left analog stick is within a certain small range close to 0:
            L1DT.set(0); //Set each of the left victors to 0.
            L2DT.set(0);
            L3DT.set(0);
        }
        if ((rValue < DBW) && (rValue > -DBW)){ //If the value from the right analog stick is within a certain small range close to 0:
            R1DT.set(0); //Set each of the right victors to 0.
            R2DT.set(0);
            R3DT.set(0);
        }
   
        //Set each of the victors to its designated value from the analog stick.
        L1DT.set(-lValue);
        L2DT.set(-lValue);
        L3DT.set(-lValue);
        R1DT.set(rValue);
        R2DT.set(rValue);
        R3DT.set(rValue);
    }
    
    public void chooChoo(){
        if (OpControl.getRawButton(CHOO_CHOO_BUTTON)){ //If the shooting button is being pressed:
            ChooChoo.set(CHOO_CHOO_SPEED); //Set the choo choo motor to the desired speed
            if (Limit.get()){ //If the limit switch is pressed:
                ChooChoo.set(0); //Set the choo choo motor 0.
            }
        }
        
    }
    
    public void intakeArm(){
        PID1.enable(); //Enable both PID Controllers.
        PID2.enable();
        if (OpControl.getRawButton(ARM_UP)){ //If the Arm Up button is pressed:
            PID1.setSetpoint(voltageGoal1); //Set the arm to the desired angle (in voltage)
        }
        if (OpControl.getRawButton(ARM_DOWN)){ //If the Arm Down button is pressed:
            PID2.setSetpoint(voltageGoal2); //Set the arm to the desired angle (in voltage)
        }
        if (Photogate.get() == false){
            Roller.set(ROLLER_SPEED); //Set the roller to the desired speed.
        }
        if (Photogate.get())
        {
            Roller.set(0);
        }
    }
    
    public void glow(){
        Glow.set(Relay.Value.kForward); //Turn the LED underglow lights on.
    }
    
}
