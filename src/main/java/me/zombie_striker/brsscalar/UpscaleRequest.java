package me.zombie_striker.brsscalar;

import com.kmschr.brs.Brick;
import com.kmschr.brs.SaveData;
import com.kmschr.brs.Vec3;
import me.zombie_striker.omeggajava.JOmegga;
import me.zombie_striker.omeggajava.RPCResponse;
import me.zombie_striker.omeggajava.objects.Vector3D;

import java.util.LinkedList;

public class UpscaleRequest {


    private RPCResponse request;
    private SaveData initBuild;
    private double scalex = 1;
    private double scaley = 1;
    private double scalez = 1;
    private int brickindex = 0;

    private Vector3D center = new Vector3D(0, 0, 0);

    private boolean done = false;

    private String playername;

    public UpscaleRequest(RPCResponse response, double scale, String playername) {
        this.request = response;
        this.scalex = scale;
        this.scaley = scale;
        this.scalez = scale;
        this.playername = playername;
    }

    public UpscaleRequest(RPCResponse response, double scalex, double scaley, double scalez, String playername) {
        this.request = response;
        this.scalex = scalex;
        this.scaley = scaley;
        this.scalez = scalez;
        this.playername = playername;
    }

    public UpscaleRequest(RPCResponse response, double scale, String playername, Vector3D center) {
        this.request = response;
        this.scalex = scale;
        this.scaley = scale;
        this.scalez = scale;
        this.playername = playername;
        this.center = center;
    }

    public RPCResponse getRequest() {
        return request;
    }

    public void tick() {
        if (initBuild == null) {
            if (request.getReturnValue() != null) {
                initBuild = (SaveData) request.getReturnValue();
            }
        }
        if (initBuild == null)
            return;
        SaveData saveData = new SaveData();
        saveData.setMaterials(initBuild.getMaterials());
        saveData.setAuthor(initBuild.getAuthor());
        saveData.setBrickOwners(initBuild.getBrickOwners());
        saveData.setColors(initBuild.getColors());
        saveData.setBrickAssets(initBuild.getBrickAssets());
        copyBricks(saveData, initBuild, 30);
        if (saveData.getBricks().size() == 0) {
            JOmegga.whisper(playername, "For some reason, brick count is 0.");
            return;
        }
        JOmegga.loadSaveData(saveData.toRPCData(), 0, 0, 0, true);
        if (brickindex >= initBuild.getBricks().size()) {
            done = true;
        }
    }

    private void copyBricks(SaveData output, SaveData source, int maxbricks) {
        for (int i = 0; i < maxbricks; i++) {
            if (source.getBricks().size() <= brickindex) {
                return;
            }
            Brick brick = source.getBricks().get(brickindex).clone();
            double xs = (brick.getSize().x * scalex);
            double ys = (brick.getSize().y * scaley);
            double zs = (brick.getSize().z * scalez);
            double xsa = xs;
            double ysa = ys;
            double zsa = zs;
            if (
                    (scalex < 1 && xs < 1) ||
                            (scaley < 1 && ys < 1) ||
                            (scalez < 1 && zs < 1)
            ) {
                i--;
                brickindex++;
                continue;
            }
            if (xsa < 0)
                xsa = -xsa;
            if (ysa < 0)
                ysa = -ysa;
            if (zsa < 0)
                zsa = -zsa;

            String brickname = source.getBrickAssets().get(brick.getAssetNameIndex());
            boolean scale = false;
            if (brickname.equals("PB_DefaultBrick") || (!brickname.toLowerCase().contains("micro"))) {
                if (xs > 1000 || ys > 1000 || zs > 1000) {
                    scale = true;
                }
            } else {
                if (xs > 500 || ys > 500 || zs > 500) {
                    scale = true;
                }
            }


            if (scale) {
                int bricksx = 1;
                int bricksy = 1;
                int bricksz = 1;

                int bricksxa = 1;
                int bricksya = 1;
                int bricksza = 1;
                if (brickname.equals("PB_DefaultBrick") || (!brickname.toLowerCase().contains("micro"))) {
                    bricksx = (int) ((xs + (xs < 0 ? -999:999)) / 1000);
                    if (bricksx < 1)
                        bricksx = 1;
                    bricksy = (int) ((ys+ (ys < 0 ? -999:999)) / 1000);
                    if (bricksy < 1)
                        bricksy = 1;
                    bricksz = (int) ((zs+ (zs < 0 ? -999:999)) / 1000);
                    if (bricksz < 1)
                        bricksz = 1;

                    bricksxa = (int) ((xsa + 999) / 1000);
                    if (bricksxa < 1)
                        bricksxa = 1;
                    bricksya = (int) ((ysa + 999) / 1000);
                    if (bricksya < 1)
                        bricksya = 1;
                    bricksza = (int) ((zsa + 999) / 1000);
                    if (bricksza < 1)
                        bricksza = 1;
                } else {
                    bricksx = (int) ((xs + (xs < 0 ? -499:499)) / 500);
                    if (bricksx < 1)
                        bricksx = 1;
                    bricksy = (int) ((ys + (ys < 0 ? -499:499)) / 500);
                    if (bricksy < 1)
                        bricksy = 1;
                    bricksz = (int) ((zs + (zs < 0 ? -499:499)) / 500);
                    if (bricksz < 1)
                        bricksz = 1;


                    bricksxa = (int) ((xsa + 499) / 500);
                    if (bricksxa < 1)
                        bricksxa = 1;
                    bricksya = (int) ((ysa + 499) / 500);
                    if (bricksya < 1)
                        bricksya = 1;
                    bricksza = (int) ((zsa + 499) / 500);
                    if (bricksza < 1)
                        bricksza = 1;
                }

                try {
                    for (double xx = 1; xx < bricksxa + 1; xx++) {
                        for (double yy = 1; yy < bricksya + 1; yy++) {
                            for(double zz = 1; zz < bricksza + 1; zz++) {
                               if(xx != 1 && xx != bricksxa &&
                                       yy != 1 && yy != bricksya &&
                                       zz != 1 && zz != bricksza
                               )
                                   continue;

                                Brick brick1 = brick.clone();
                                int xsi = (int) (xs / bricksx);
                                int ysi = (int) (ys / bricksy);
                                int zsi = (int) (zs / bricksz);
                                int xsia = (int) (xsa / bricksxa);
                                int ysia = (int) (ysa / bricksya);
                                int zsia = (int) (zsa / bricksza);
                               // JOmegga.broadcast(xsi+" "+ysi+" "+zsi);
                                brick1.setSize(xsia, ysia, zsia);

                                int xxp =(int) ((brick.getPosition().x)+((xx*2*xsi) * (brick.getPosition().x<0?-1:1)));
                                int yyp = (int)((brick.getPosition().y)+((yy*2*ysi) * (brick.getPosition().y<0?-1:1)));
                                int zzp = (int)((brick.getPosition().z)+(zz*2*zsi));
                                brick1.setPosition(xxp, yyp, zzp);
                                JOmegga.broadcast(xxp+" "+yyp+" "+zzp);
                                output.getBricks().add(brick1);
                                brickindex++;
                            }
                        }

                    }
                }catch(Exception e){
                    JOmegga.log(e.getMessage()+" : ERR");
                    for(StackTraceElement s : e.getStackTrace()){
                        JOmegga.log(s.getFileName()+" "+s.getMethodName()+" "+s.getLineNumber());
                    }
                }
            } else {
                int xsi = (int) xs;
                int ysi = (int) ys;
                int zsi = (int) zs;
                brick.setSize(xsi, ysi, zsi);
                Vec3 position = brick.getPosition();
                double z = ((position.z - center.getZ()) * scalez);
                double y = ((position.y - center.getY()) * scaley);
                double x = ((position.x - center.getX()) * scalex);
                if (brickname.equals("PB_DefaultBrick") || (!brickname.toLowerCase().contains("micro"))) {
                    if (((int) y) % 10 != 0)
                        y = ((int) (y / 10)) * 10;
                    if (((int) x) % 10 != 0)
                        x = ((int) (x / 10)) * 10;
                    if (((int) z) % 4 != 0)
                        z = ((int) (z / 4)) * 4;
                } else {
                    if (((int) x) % 2 != 0)
                        x = ((int) (x / 2)) * 2;
                    if (((int) y) % 2 != 0)
                        y = ((int) (y / 2)) * 2;
                    if (((int) z) % 2 != 0)
                        z = ((int) (z / 2)) * 2;
                }
                if (z < 0)
                    z = -z;

                brick.setPosition((int) x, (int) y, (int) z);
                output.getBricks().add(brick);
                brickindex++;
            }
        }
    }

    public boolean done() {
        return done;
    }

    public String getPlayerName() {
        return playername;
    }
}
