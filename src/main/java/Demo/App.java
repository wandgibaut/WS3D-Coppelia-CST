/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Demo;

import WS3DCoppelia.WS3DCoppelia;
import WS3DCoppelia.model.Leaflet;
import WS3DCoppelia.util.Constants;
import co.nstant.in.cbor.CborException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static void main(String[] args) throws CborException, IOException {
        MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
        MemoryUsage beforeHeapMemoryUsage = mbean.getHeapMemoryUsage();

        Environment env = new Environment();
        AgentMind a = new AgentMind(env);

        long start = System.currentTimeMillis();
        long current = System.currentTimeMillis();

        while (true){
            if((System.currentTimeMillis() - current)/1000F > 5 ){
                if (Arrays.stream(env.creature.getLeaflets()).allMatch(Leaflet::isDelivered)){

                    MemoryUsage afterHeapMemoryUsage = mbean.getHeapMemoryUsage();
                    long consumed = afterHeapMemoryUsage.getUsed() -
                            beforeHeapMemoryUsage.getUsed();
                    System.out.println("Total consumed Memory:" + consumed/(1024*1024) + " MB");


                    env.rg.stop();
                    env.world.stopSimulation();
                    a.shutDown();

                    float total = (System.currentTimeMillis() - start)/1000f;
                    System.out.println("the program took " + total + " seconds!");
                    System.exit(0);
                }
                current = System.currentTimeMillis();
            }
        }

        //WS3DCoppelia world = new WS3DCoppelia(4,4);
        //try {
        //    world.startSimulation();
        //} catch (IOException ex) {
        //    Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        //} catch (CborException ex) {
        //    Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        //}
        //world.createAgent(3, 3.8f, Constants.Color.AGENT_GREEN);
        //try {
        //    Thread.sleep(30000);
        //} catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        //}
        //world.createAgent(3, 3.2f, Constants.Color.AGENT_GREEN);
        //try {
        //    Thread.sleep(30000);
        //} catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        //}
        //world.createAgent(3, 2.6f, Constants.Color.AGENT_GREEN);
        //try {
        //    Thread.sleep(30000);
        //} catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        //}
        //world.createAgent(3, 2, Constants.Color.AGENT_GREEN);
        //try {
        //    Thread.sleep(30000);
        //} catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        //}
        //world.createAgent(3, 1.4f, Constants.Color.AGENT_GREEN);
        //try {
        //    Thread.sleep(20000);
        //} catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        //}
    }
}
