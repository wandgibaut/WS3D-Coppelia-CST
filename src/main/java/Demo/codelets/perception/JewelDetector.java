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

import WS3DCoppelia.model.Identifiable;
import WS3DCoppelia.model.Thing;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Detect jewels in the vision field.
 * 	This class detects a number of things related to jewels, such as if there are any within reach,
 * any on sight, if they are rotten, and so on.
 * 
 * @author wander
 *
 */
public class JewelDetector extends Codelet {

        private Memory visionMO;
        private Memory knownJewelsMO;

	public JewelDetector(){
            this.name = "JewelDetector";
	}

	@Override
	public void accessMemoryObjects() {
                synchronized(this) {
		    this.visionMO=(MemoryObject)this.getInput("VISION");
                }
		this.knownJewelsMO=(MemoryObject)this.getOutput("KNOWN_JEWELS");
	}

	@Override
	public void proc() {
            CopyOnWriteArrayList<Thing> vision;
            List<Thing> known;
            synchronized (visionMO) {
               //vision = Collections.synchronizedList((List<Thing>) visionMO.getI());
               vision = new CopyOnWriteArrayList((List<Identifiable>) visionMO.getI());
               known = Collections.synchronizedList((List<Thing>) knownJewelsMO.getI());
               //known = new CopyOnWriteArrayList((List<Thing>) knownJewelsMO.getI());    
               synchronized(vision) {
                 for (Identifiable obj : vision) {
                     if (obj instanceof Thing) {
                         Thing t = (Thing) obj;
                         if (t.isJewel()) {
                             boolean found = false;
                             synchronized (known) {
                                 CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);
                                 for (Thing e : myknown)
                                     if (Objects.equals(t, e)) {
                                         found = true;
                                         break;
                                     }
                                 if (!found) known.add(t);
                             }
                         }
                     }
               
                 }
               }
            }
	}// end proc
        
        @Override
        public void calculateActivation() {
        
        }


}//end class


