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

package Demo.memory;

import java.util.List;


/**
 *
 * @author rgudwin
 */
public class CreatureInnerSense {
    public List<Float> position;
    public double pitch;
    public double fuel;
    
    public String toString() {
        if (position != null)
            return("Position: ("+position.get(0)+","+position.get(1)+") Pitch: "+pitch+" Fuel: "+fuel);
        else 
            return("Position: null,null"+" Pitch: "+pitch+" Fuel: "+fuel);
    }
}

