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
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/** 
 * 
 * @author klaus
 * 
 * 
 */

public class Forage extends Codelet {
    
        private Memory knownApplesMO;
		private Memory knownJewelsMO;
		private Memory innerSenseMO;
		private CreatureInnerSense cis;
        private List<Long> knownApples;
		private List<Long> knownJewels;
        private MemoryContainer legsMO;


	/**
	 * Default constructor
	 */
	public Forage(){
            this.name = "Forage";
	}

	@Override
	public void proc() {
            knownApples = (List<Long>) knownApplesMO.getI();
			knownJewels = (List<Long>) knownJewelsMO.getI();


            if ((knownApples.isEmpty() && cis.fuel < 300) || (knownJewels.isEmpty() && cis.fuel > 300)) {
		JSONObject message=new JSONObject();
			try {
				message.put("ACTION", "FORAGE");
                                activation=1.0;
				legsMO.setI(message.toString(),activation,name);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            }
            else activation=0.0;
            JSONObject message=new JSONObject();
            message.put("ACTION", "FORAGE");
            legsMO.setI(message.toString(),activation,name);		
	}

	@Override
	public void accessMemoryObjects() {
        knownApplesMO = this.getInput("KNOWN_APPLES");
		knownJewelsMO = this.getInput("KNOWN_JEWELS");
		innerSenseMO = this.getInput("INNER");
        legsMO = (MemoryContainer)this.getOutput("LEGS");

		cis = (CreatureInnerSense) innerSenseMO.getI();
	}
        
        @Override
        public void calculateActivation() {
            
        }


}
