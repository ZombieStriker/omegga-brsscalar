package me.zombie_striker.brsscalar;

import me.zombie_striker.omeggajava.JOmegga;
import me.zombie_striker.omeggajava.events.ChatCommandEvent;
import me.zombie_striker.omeggajava.events.EventHandler;
import me.zombie_striker.omeggajava.events.Listener;
import me.zombie_striker.omeggajava.events.TickEvent;

import javax.swing.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ScalarListener implements Listener {

    private List<UpscaleRequest> requests = new LinkedList<>();

    @EventHandler
    public void onChat(ChatCommandEvent event) {
        if (event.getCommand().equals("brsscalar:save")) {
            if(!JOmegga.getPlayer(event.getPlayername()).isHost()){
                JOmegga.whisper(event.getPlayername(),"Only the host can use this command.");
                return;
            }
            if (event.getArgs().length < 1) {
                JOmegga.whisper(event.getPlayername(), "You need to provide the name of the save ");
                return;
            }
            JOmegga.save(event.getArgs()[0]);
            JOmegga.whisper(event.getPlayername(),"Saving map to \""+event.getArgs()[0]+"\"");
        }else if (event.getCommand().equals("brsscalar")) {
            if(!JOmegga.getPlayer(event.getPlayername()).isHost()){
                JOmegga.whisper(event.getPlayername(),"Only the host can use this command.");
                return;
            }
            if (event.getArgs().length < 1) {
                JOmegga.whisper(event.getPlayername(), "You need to provide the name of the save you wish to upscale (Or use ~ to use the current map.)");
                return;
            }
            String upscaleName = event.getArgs()[0];
            if (event.getArgs().length < 2) {
                JOmegga.whisper(event.getPlayername(), "Please provide the scale you wish to scale the bricks by (use whole numbers).");
                return;
            }
            double scalarx = Double.parseDouble(event.getArgs()[1]);
            double scalary = scalarx;
            double scalarz = scalarx;
            if(event.getArgs().length >= 4){
                scalary = Double.parseDouble(event.getArgs()[2]);
                scalarz = Double.parseDouble(event.getArgs()[3]);
            }
            if (scalarx == 0|| scalary == 0 || scalarz==0) {
                JOmegga.whisper(event.getPlayername(), "The scalar provided is 0.");
                return;
            }
            if(upscaleName.equals("~")){
                upscaleName = "brsscalar_current";
                JOmegga.save(upscaleName);
            }else if (!new File(JOmegga.getOmeggaDir(), "/data/Saved/Builds/" + upscaleName + ".brs").exists()) {
                JOmegga.whisper(event.getPlayername(), upscaleName + ".brs does not exist in the /Builds/ folder.");
                return;
            }
            requests.add(new UpscaleRequest(JOmegga.readSaveData(upscaleName), scalarx,scalary,scalarz, event.getPlayername()));
            JOmegga.whisper(event.getPlayername(), "Upscaling \"" + upscaleName + "\" by " + scalarx+(scalarx!=scalary||scalarx!=scalarz?", "+scalary+", "+scalarz:"") + ".");
        }
    }

    @EventHandler
    public void onTick(TickEvent event) {
        if (requests.size() > 0)
            for (UpscaleRequest request : new LinkedList<>(requests)) {
                request.tick();
                if (request.done()) {
                    JOmegga.whisper(request.getPlayerName(), "Done upscaling.");
                    requests.remove(request);
                }
            }
    }
}
