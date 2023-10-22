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
import WS3DCoppelia.model.Agent;
import WS3DCoppelia.model.Thing;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import org.json.JSONException;
import org.json.JSONObject;

public class GoToClosestJewel extends Codelet {

	private Memory closestJewelMO;
	private Memory selfInfoMO;
	private MemoryContainer legsMO;
	private int creatureBasicSpeed;
	private double reachDistance;
        private Agent creature;

	public GoToClosestJewel(int creatureBasicSpeed, double reachDistance, Agent c) {
                creature = c;
		this.creatureBasicSpeed=creatureBasicSpeed;
		this.reachDistance=reachDistance;
                this.name = "GoToClosestJewel";
	}

	@Override
	public void accessMemoryObjects() {
		closestJewelMO=(MemoryObject)this.getInput("CLOSEST_JEWEL");
		selfInfoMO=(MemoryObject)this.getInput("INNER");
		legsMO=(MemoryContainer)this.getOutput("LEGS");
	}

	@Override
	public void proc() {
		// Find distance between creature and closest jewel
		//If far, go towards it
		//If close, stops

                Thing closestJewel = (Thing) closestJewelMO.getI();
                CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();

		if(closestJewel != null)
		{
			float jewelX=0;
			float jewelY=0;
			try {
                                jewelX = closestJewel.getPos().get(0);
                                jewelY = closestJewel.getPos().get(1);

			} catch (Exception e) {
				e.printStackTrace();
			}

			float selfX=cis.position.get(0);
			float selfY=cis.position.get(1);

			double distance = calculateDistance((double)selfX, (double)selfY, (double)jewelX, (double)jewelY);
			JSONObject message=new JSONObject();
			try {
				if(distance>reachDistance){ //Go to it
                                        message.put("ACTION", "GOTO");
					message.put("X", jewelX);
					message.put("Y", jewelY);
                                        message.put("SPEED", creatureBasicSpeed);
                                        activation=1.0;

				}else{//Stop
					message.put("ACTION", "GOTO");
					message.put("X", jewelX);
					message.put("Y", jewelY);
                                        message.put("SPEED", 0.0);
                                        activation=0.5;
				}
				legsMO.setI(message.toString(),activation,name);
			} catch (JSONException e) {
				e.printStackTrace();
			}	
		}
                else {
                    activation=0.0;
                    legsMO.setI("",activation,name);
                }
                
	}//end proc
        
        @Override
        public void calculateActivation() {
        
        }
        
        private double calculateDistance(double x1, double y1, double x2, double y2) {
            return(Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
        }

}
