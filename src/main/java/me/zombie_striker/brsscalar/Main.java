package me.zombie_striker.brsscalar;

import me.zombie_striker.omeggajava.JOmegga;

public class Main {

    public static void main(String ... args){
        JOmegga.registerListener(new ScalarListener());
        JOmegga.init("brsscalar");

        while(JOmegga.isRunning()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
