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

package Demo.codelets.behaviors;

import Demo.memory.CreatureInnerSense;
import WS3DCoppelia.model.Thing;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class SackClosestJewel extends Codelet {

	private Memory closestJewelMO;
	private Memory innerSenseMO;
        private Memory knownMO;
	private double reachDistance;
	private Memory handsMO;
        Thing closestJewel;
        CreatureInnerSense cis;
        List<Thing> known;

	public SackClosestJewel(double reachDistance) {
                setTimeStep(50);
		this.reachDistance=reachDistance;
                this.name = "SackClosestJewel";
	}

	@Override
	public void accessMemoryObjects() {
		closestJewelMO=(MemoryObject)this.getInput("CLOSEST_JEWEL");
		innerSenseMO=(MemoryObject)this.getInput("INNER");
		handsMO=(MemoryObject)this.getOutput("HANDS");
                knownMO = (MemoryObject)this.getOutput("KNOWN_JEWELS");
	}

	@Override
	public void proc() {
                long jewelID=0;
                closestJewel = (Thing) closestJewelMO.getI();
                cis = (CreatureInnerSense) innerSenseMO.getI();
                known = (List<Thing>) knownMO.getI();
		//Find distance between closest jewel and self
		//If closer than reachDistance, sack the jewel
		List<Object> action = new ArrayList<Object>();
		if(closestJewel != null)
		{
			float jewelX=0;
			float jewelY=0;
			try {
				jewelX=closestJewel.getPos().get(0); 
				jewelY=closestJewel.getPos().get(1); 
                                

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			float selfX=cis.position.get(0);
			float selfY=cis.position.get(1);

			double distance = calculateDistance((double)selfX, (double)selfY, (double)jewelX, (double)jewelY);
			
			try {
				if(distance<=reachDistance){ //sack it
					action.add("SACKIT");
					action.add(closestJewel);
					handsMO.setI(action);
                                        activation=1.0;
                                        DestroyClosestJewel();
				}else{
                                        action.add("NOTHING");
					handsMO.setI(action);	//nothing
                                        activation=0.0;
				}
				
//				System.out.println(message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
                        action.add("NOTHING");
                        handsMO.setI(action);	//nothing
                        activation=0.0;
		}
        //System.out.println("Before: "+known.size()+ " "+known);
        
        //System.out.println("After: "+known.size()+ " "+known);
	//System.out.println("SackClosestJewel: "+ handsMO.getInfo());	

	}
        
        @Override
        public void calculateActivation() {
        
        }
        
        public void DestroyClosestJewel() {
           int r = -1;
           int i = 0;
           synchronized(known) {
             CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);  
             for (Thing t : known) {
              if (closestJewel != null) 
                 if (Objects.equals(t, closestJewel)) r = i;
              i++;
             }   
             if (r != -1) known.remove(r);
             closestJewel = null;
           }
        }
        
        private double calculateDistance(double x1, double y1, double x2, double y2) {
            return(Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
        }

}
