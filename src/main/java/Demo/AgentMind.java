package Demo;

/*****************************************************************************
 * Copyright 2007-2015 DCA-FEEC-UNICAMP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *    Klaus Raizer, Andre Paraense, Ricardo Ribeiro Gudwin
 *****************************************************************************/

import Demo.codelets.behaviors.*;
import Demo.codelets.perception.ClosestJewelDetector;
import Demo.codelets.perception.JewelDetector;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.Mind;
import WS3DCoppelia.model.Thing;
import Demo.codelets.motor.HandsActionCodelet;
import Demo.codelets.motor.LegsActionCodelet;
import Demo.codelets.perception.AppleDetector;
import Demo.codelets.perception.ClosestAppleDetector;
import Demo.codelets.sensors.InnerSense;
import Demo.codelets.sensors.Vision;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import Demo.memory.CreatureInnerSense;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rgudwin
 */
public class AgentMind extends Mind {
    
    private static int creatureBasicSpeed=3;
    private static double reachDistance=0.5;
    public ArrayList<Codelet> behavioralCodelets = new ArrayList<Codelet>();
    
    public AgentMind(Environment env) {
        super();
                
        // Create CodeletGroups and MemoryGroups for organizing Codelets and Memories
        createCodeletGroup("Sensory");
        createCodeletGroup("Motor");
        createCodeletGroup("Perception");
        createCodeletGroup("Behavioral");
        createMemoryGroup("Sensory");
        createMemoryGroup("Motor");
        createMemoryGroup("Working");

        // Declare Memory Objects
        Memory legsMO;  // This Memory is going to be a MemoryContainer
        Memory handsMO;
        Memory visionMO;
        Memory innerSenseMO;
        Memory closestAppleMO;
        Memory knownApplesMO;
        Memory knownJewelsMO;
        Memory closestJewelMO;
                
                //Initialize Memory Objects
        legsMO=createMemoryContainer("LEGS");
        registerMemory(legsMO,"Motor");

        List<Object> hand_action = Collections.synchronizedList(new ArrayList<Object>());
		handsMO=createMemoryObject("HANDS", hand_action);
        registerMemory(handsMO,"Motor");

        List<Thing> vision_list = Collections.synchronizedList(new ArrayList<Thing>());
		visionMO=createMemoryObject("VISION",vision_list);
        registerMemory(visionMO,"Sensory");

        CreatureInnerSense cis = new CreatureInnerSense();
		innerSenseMO=createMemoryObject("INNER", cis);
        registerMemory(innerSenseMO,"Sensory");

        closestAppleMO=createMemoryObject("CLOSEST_APPLE", null);
        registerMemory(closestAppleMO,"Working");

        List<Thing> knownApples = Collections.synchronizedList(new ArrayList<Thing>());
        knownApplesMO=createMemoryObject("KNOWN_APPLES", knownApples);
        registerMemory(knownApplesMO,"Working");

        closestJewelMO = createMemoryObject("CLOSEST_JEWEL", null);
        registerMemory(closestJewelMO, "Working");

        List<Thing> knownJewels = Collections.synchronizedList(new ArrayList<Thing>());
        knownJewelsMO = createMemoryObject("KNOWN_JEWELS", knownJewels);
        registerMemory(knownJewelsMO,"Working");

                
 		// Create Sensor Codelets	
		Codelet vision=new Vision(env.creature);
		        vision.addOutput(visionMO);
                insertCodelet(vision); //Creates a vision sensor
                registerCodelet(vision,"Sensory");
		
		Codelet innerSense=new InnerSense(env.creature);
		        innerSense.addOutput(innerSenseMO);
                insertCodelet(innerSense); //A sensor for the inner state of the creature
                registerCodelet(innerSense,"Sensory");
		
		// Create Actuator Codelets
		Codelet legs=new LegsActionCodelet(env.creature);
		        legs.addInput(legsMO);
                insertCodelet(legs);
                registerCodelet(legs,"Motor");

		Codelet hands=new HandsActionCodelet(env.creature);
		hands.addInput(handsMO);
                insertCodelet(hands);
                registerCodelet(hands,"Motor");
		
		// Create Perception Codelets
        Codelet ad = new AppleDetector();
                ad.addInput(visionMO);
                ad.addOutput(knownApplesMO);
                insertCodelet(ad);
                registerCodelet(ad,"Perception");

        Codelet jd = new JewelDetector();
                jd.addInput(visionMO);
                jd.addOutput(knownJewelsMO);
                insertCodelet(jd);
                registerCodelet(jd,"Perception");
                
		Codelet closestAppleDetector = new ClosestAppleDetector(env.creature);
                closestAppleDetector.addInput(knownApplesMO);
                closestAppleDetector.addInput(innerSenseMO);
                closestAppleDetector.addOutput(closestAppleMO);
                insertCodelet(closestAppleDetector);
                registerCodelet(closestAppleDetector,"Perception");

        Codelet closestJewelDetector = new ClosestJewelDetector(env.creature);
                closestJewelDetector.addInput(knownJewelsMO);
                closestJewelDetector.addInput(innerSenseMO);
                closestJewelDetector.addOutput(closestJewelMO);
                insertCodelet(closestJewelDetector);
                registerCodelet(closestJewelDetector,"Perception");
		
		// Create Behavior Codelets
		Codelet goToClosestApple = new GoToClosestApple(creatureBasicSpeed,reachDistance, env.creature);
                goToClosestApple.addInput(closestAppleMO);
                goToClosestApple.addInput(innerSenseMO);
                goToClosestApple.addOutput(legsMO);
                insertCodelet(goToClosestApple);
                registerCodelet(goToClosestApple,"Behavioral");
                behavioralCodelets.add(goToClosestApple);

        Codelet goToClosestJewel = new GoToClosestJewel(creatureBasicSpeed,reachDistance, env.creature);
                goToClosestJewel.addInput(closestJewelMO);
                goToClosestJewel.addInput(innerSenseMO);
                goToClosestJewel.addOutput(legsMO);
                insertCodelet(goToClosestJewel);
                registerCodelet(goToClosestJewel,"Behavioral");
                behavioralCodelets.add(goToClosestJewel);

		Codelet eatApple=new EatClosestApple(reachDistance);
                eatApple.addInput(closestAppleMO);
                eatApple.addInput(innerSenseMO);
                eatApple.addOutput(handsMO);
                eatApple.addOutput(knownApplesMO);
                insertCodelet(eatApple);
                registerCodelet(eatApple,"Behavioral");
                behavioralCodelets.add(eatApple);

        Codelet sackClosestJewel = new SackClosestJewel(reachDistance);
                sackClosestJewel.addInput(closestJewelMO);
                sackClosestJewel.addInput(innerSenseMO);
                sackClosestJewel.addOutput(handsMO);
                sackClosestJewel.addOutput(knownJewelsMO);
                insertCodelet(sackClosestJewel);
                registerCodelet(sackClosestJewel,"Behavioral");
                behavioralCodelets.add(sackClosestJewel);

        Codelet forage=new Forage();
		        forage.addInput(knownApplesMO);
                forage.addInput(knownJewelsMO);
                forage.addInput(innerSenseMO);
                forage.addOutput(legsMO);
                insertCodelet(forage);
                registerCodelet(forage,"Behavioral");
                behavioralCodelets.add(forage);
                
                // sets a time step for running the codelets to avoid heating too much your machine
                for (Codelet c : this.getCodeRack().getAllCodelets())
                    c.setTimeStep(200);
		
		// Start Cognitive Cycle
		start(); 
    }             
    
}
