package com.tanks_2d;

import static com.tanks_2d.MainGame.HEIGHT_SCREEN;
import static com.tanks_2d.MainGame.WIDTH_SCREEN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.tanks_2d.Units.Tanks.OpponentsTanks;
import com.tanks_2d.Units.Tanks.Tank;

public class CameraGame {

    private OpponentsTanks targetCamera; // вроде это за кем следит __


    OrthographicCamera camera;
    private FillViewport viewport;
    private boolean floatCamera;

    private int sizeMap;

    private Vector2 speed; // определяемая скосроть с назначением новойо точки
    private Vector2 targetPoint; // точка к которой двиать камеру

    private final int hl, wl, dw;

    float zoom = 1;

    public CameraGame(float HIDE_SCREEN, float WHIDE_SCREEN, int sm, int hl, int wl, float delta_wheile) {
        this.camera = new OrthographicCamera();
        this.viewport = new FillViewport(HIDE_SCREEN * zoom, WHIDE_SCREEN * zoom, camera);
        //  this.viewport = new FillViewport(HIDE_SCREEN, WHIDE_SCREEN, camera);
        floatCamera = false;
        targetPoint = new Vector2();
        this.sizeMap = sm;
        this.wl = wl;
        this.hl = hl;
        this.dw = (int) delta_wheile;


    }

    public void moveFloatCameraToPoint(float x, float y, float speed) {

        if(MathUtils.randomBoolean()){
            zoom +=  1 * Gdx.graphics.getDeltaTime();
        }else zoom -=  1 * Gdx.graphics.getDeltaTime();
        zoom = MathUtils.clamp(zoom,1,3);
    //    MathUtils.
        this.viewport = new FillViewport(HEIGHT_SCREEN * 3, WIDTH_SCREEN * 3, camera);


        this.targetPoint.set(x, y);
        //// System.out.println("moveFloatCameraToPoint");
        this.camera.position.sub(this.camera.position.cpy().sub(targetPoint.x, targetPoint.y, 0).scl(Gdx.graphics.getDeltaTime() * speed));
        fixBounds();
        interatorCamera();
     //   integrationCamera();

    }

    public void integrationCamera() {
        int xc, yc;

        xc = (int) camera.position.x;
        yc = (int) camera.position.y;
       // System.out.println(camera.position);
        camera.position.set(xc, yc, 1);
    }

    public void zoomCamera() {

//        if(camera.zoom > 5) camera.zoom = 5;
//        camera.zoom += 0.01;


    }

    public void deathStatus(Tank tank) {
        fixBounds();
        try {
            if (targetCamera.getPosition().x != -100000.0)
                moveFloatCameraToPoint(targetCamera.getPosition().x, targetCamera.getPosition().y, 2.3f);else{
                    if(MathUtils.randomBoolean(.05f)){
             //  System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!! createNewTargetDeathRhimcreateNewTargetDeathRhimv");
                        tank.getGsp().getCameraGame().createNewTargetDeathRhim(tank.getGsp().getTanksOther().getRandomPlayer());
                    }
            }

            // if(targetCamera.getPosition().x ==-100000.0) tank.getGsp().getCameraGame().createNewTargetDeathRhim(tank.getGsp().getTanksOther().getRandomPlayer());
        } catch (NullPointerException e) {
            tank.getGsp().getCameraGame().createNewTargetDeathRhim(tank.getGsp().getTanksOther().getRandomPlayer());
        }
       // integrationCamera();
    }

    public void createNewTargetDeathRhim(OpponentsTanks ot) {
        this.targetCamera = ot;
    }


    public void jampCameraToPoint(float x, float y) {
        //this.camera.position.set(x,y,0);
        fixBounds();
        this.camera.position.set(x, y, 0);
        interatorCamera();

    }

    public void setTargetCamera(OpponentsTanks targetCamera) { // то та кем нужно следить
        this.targetCamera = targetCamera;
    }

    //    public void jampCameraToPoint(Vector2 toPoint, speed){
//        //this.camera.position.set(x,y,0);
//        this.camera.position.set(x,y,0);
//        fixBounds();
//    }

    public void updateCamera() {

    }

    public OrthographicCamera getCamera() {
        integrationCamera();
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public FillViewport getViewport() {
        return viewport;
    }


    public void fixBounds() { // не выходила за края
        float scaledViewportWidthHalfExtent = WIDTH_SCREEN * 1.2f * 0.5f;
        float scaledViewportHeightHalfExtent = HEIGHT_SCREEN * 1.2f * 0.5f;

        // Horizontal
        if (camera.position.x < scaledViewportWidthHalfExtent)
            camera.position.x = scaledViewportWidthHalfExtent;
        else if (camera.position.x > wl - scaledViewportWidthHalfExtent)
            camera.position.x = wl - scaledViewportWidthHalfExtent;

        // Vertical
        if (camera.position.y < scaledViewportHeightHalfExtent - this.dw) //hb
            camera.position.y = scaledViewportHeightHalfExtent - this.dw;
        else if (camera.position.y > hl - scaledViewportHeightHalfExtent + this.dw)
            camera.position.y = hl - scaledViewportHeightHalfExtent + this.dw;

        interatorCamera();
    }

    public Vector3 getCameraPosition() {
        return camera.position;
    }


    private void interatorCamera(){
        this.camera.position.x = (int)this.camera.position.x;
        this.camera.position.y = (int)this.camera.position.y;
    }
}
