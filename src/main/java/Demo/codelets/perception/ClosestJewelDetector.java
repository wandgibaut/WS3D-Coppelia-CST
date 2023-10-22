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

package Demo.codelets.perception;


import Demo.memory.CreatureInnerSense;
import WS3DCoppelia.model.Agent;
import WS3DCoppelia.model.Leaflet;
import WS3DCoppelia.model.Thing;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author klaus
 *
 */
public class ClosestJewelDetector extends Codelet {

	private Memory knownMO;
	private Memory closestJewelMO;
	private Memory innerSenseMO;
        private Agent creature;

        private List<Thing> known;

	public ClosestJewelDetector(Agent c) {
            creature = c;
            this.name = "ClosestJewelDetector";
	}


	@Override
	public void accessMemoryObjects() {
		this.knownMO=(MemoryObject)this.getInput("KNOWN_JEWELS");
		this.innerSenseMO=(MemoryObject)this.getInput("INNER");
		this.closestJewelMO=(MemoryObject)this.getOutput("CLOSEST_JEWEL");	
	}
	@Override
	public void proc() {
        Leaflet[] leaflets = creature.getLeaflets();

        Thing closest_jewel=null;
        known = Collections.synchronizedList((List<Thing>) knownMO.getI());
        CreatureInnerSense cis = (CreatureInnerSense) innerSenseMO.getI();
        synchronized(known) {
       if(!known.isEmpty()){
        //Iterate over objects in vision, looking for the closest jewel
                    CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);
                    for (Thing t : myknown) {
                            if(closest_jewel == null){
                                    //closest_jewel = t;
                                    if (!isInAnyLeaflet(t, leaflets)){
                                        myknown.remove(t);
                                        //closest_jewel = null;
                                    }
                                    else{
                                        closest_jewel = t;
                                    }
                            }
                            else {
                                    List<Float> jewelPos = Arrays.asList(new Float[]{(float)0, (float)0});
                                    jewelPos = t.getPos();

                                    List<Float> closest_jewelPos = Arrays.asList(new Float[]{(float)0, (float)0});
                                    closest_jewelPos = closest_jewel.getPos();
                                    double Dnew = calculateDistance(jewelPos.get(0), jewelPos.get(1), cis.position.get(0), cis.position.get(1));
                                    double Dclosest= calculateDistance(closest_jewelPos.get(0), closest_jewelPos.get(1), cis.position.get(0), cis.position.get(1));
                                    if(Dnew<Dclosest){
                                            closest_jewel = t;
                                    }
                            }
        }

                    if(closest_jewel!=null){
            if(closestJewelMO.getI() == null || !closestJewelMO.getI().equals(closest_jewel)){
                                  closestJewelMO.setI(closest_jewel);
            }

        }else{
            //couldn't find any nearby jewels
                            //closest_jewel = null;
                            //closestJewelMO.setI(closest_jewel);
                            closestJewelMO.setI(null);
        }
       }
               else  { // if there are no known jewels closest_jewel must be null
                    //closest_jewel = null;
                    //closestJewelMO.setI(closest_jewel);
                    closestJewelMO.setI(null);
       }
            }
	}//end proc

    @Override
    public void calculateActivation() {}

    private boolean isInAnyLeaflet(Thing jewel , Leaflet[] leaflets){
        //boolean answer = false;
        for (Leaflet leaflet : leaflets){
            //jewel.isJewel();
            if (leaflet.getRequirements().containsKey(jewel.thingType())){
                return true;
            }
        }

        return false;
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return(Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
    }

}
