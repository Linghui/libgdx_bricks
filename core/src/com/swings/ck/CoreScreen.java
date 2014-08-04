package com.swings.ck;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;

/*
 * 横版类游戏父类
 * 支持  角色设定，移动
 *      npc设定，响应
 */
public abstract class CoreScreen implements Screen, InputProcessor {
    private String TAG = "CoreScreen";
    
    protected static Game game;

    public static int width = 800;
    public static int height = 480;
    
    // background_xxx are used to deal with the scope of leading role can move to.
    private int background_width  = 0;
    private int background_height = 0;
    
    private   Stage rootStage;       // stage for things controlled by user, like player, menu, control button
                                     // which are relative static with screen
    private   Group bottom;

    private   Stage backgroundStage; // stage for background, like tiled map,  
    
    private boolean lockEvent = false;

    // all value below is usd to deal with view and leading role movement
    protected OrthographicCamera cam;
    
    private boolean isHided = false;
    
    public CoreScreen(Game game) {
        this(game, width, height);
    }
    
    public CoreScreen(Game game, int width, int height){
        CoreScreen.game = game;
        CoreScreen.width = width;
        CoreScreen.height = height;
        
        rootStage = new Stage();
        
        bottom = new Group();
        rootStage.addActor(bottom);
        
        backgroundStage = new Stage();
    }
    

    @Override
    public void show(){
        Gdx.input.setInputProcessor(this);
        rootStage.getRoot().setVisible(true);
        backgroundStage.getRoot().setVisible(true);
        isHided = false;
    }

    @Override
    public void resize(int width, int height) {
        
    }

    public void setBackgroundVisible( boolean visible ){
        backgroundStage.getRoot().setVisible(visible);
    }
    
    
    @Override
    public void hide() {
        rootStage.getRoot().setVisible(false);
        backgroundStage.getRoot().setVisible(false);
        isHided = true;
    }

    @Override
    public void pause() {
        Gdx.app.log(TAG, "pause");
    }

    @Override
    public void resume() {
        Gdx.app.log(TAG, "resume");
    }


    public void initCam(){

        cam = (OrthographicCamera) this.getCamera();
        cam.position.x = CoreScreen.width/2;
        cam.position.y = CoreScreen.height/2;
        
        rootStage.getCamera().position.x = CoreScreen.width/2;
        rootStage.getCamera().position.y = CoreScreen.height/2;
//        cam.viewportWidth = rootStage.getWidth() - rootStage.getGutterWidth()*2;
//        cam.viewportHeight = rootStage.getHeight() - rootStage.getGutterHeight() * 2;
    }
    
    public Vector2 toBackgroudStageCoordinates(int x, int y){
        Vector2 coords = new Vector2();
        coords.x = x;
        coords.y = y;
        return this.backgroundStage.screenToStageCoordinates(coords);
    }
    
    public Vector2 toTopStageCoordinates(int x, int y){
        Vector2 coords = new Vector2();
        coords.x = x;
        coords.y = y;
        return this.rootStage.screenToStageCoordinates(coords);
    }

    // for just one screen background picture, this is easy to use
    public void setBackGround(TextureRegion background){
        this.setBackGround(background, true);
    }
    
    public void setBackGround(TextureRegion background, boolean fitScreen){
        
        if(background == null){
            return ;
        }

        Image backgroundImg = new Image(background);
        if(fitScreen){
            backgroundImg.setWidth(rootStage.getWidth());
            backgroundImg.setHeight(rootStage.getHeight());
        }
        background_width = (int) backgroundImg.getWidth();
        background_height = (int) backgroundImg.getHeight();
        backgroundStage.addActor(backgroundImg);
    }
    
    public int getBackgroundWidth(){
        return background_width;
    }
    
    public int getBackgroundHeight(){
        return background_height;
    }
    
    public void setRootVisible(boolean visible) {
        this.rootStage.getRoot().setVisible(visible);
    }
    
    public Camera getCamera(){
        return backgroundStage.getCamera();
    }
    
    public void clickOnBackgroud(int x, int y){
        Gdx.app.log(TAG, "!!!!!x - " + x + " : y - " + y);
    }
    
    // ui like alert dialog added to this layer
    public void addActorTop(Actor actor){
        this.rootStage.addActor(actor);
        bottom.toBack();
    }
    
    // most of the main ui added to this layer
    public void addActorBottom(Actor actor){
        this.bottom.addActor(actor);
        bottom.toBack();
    }
    
    public void addActorBackground(Actor actor){
        this.backgroundStage.addActor(actor);
    }
    
    public void setBottomTouchable(Touchable touchable){
        bottom.setTouchable(touchable);
    }
    
    public void setBackgroundTouchable(Touchable touchable){
        backgroundStage.getRoot().setTouchable(touchable);
    }
    
    public void setScreenTouchable(Touchable touchable){
        rootStage.getRoot().setTouchable(touchable);
        backgroundStage.getRoot().setTouchable(touchable);
        bottom.setTouchable(touchable);
    }
    
    public boolean isScreenTouchable(){
        if ( rootStage.getRoot().getTouchable() != Touchable.enabled
                || backgroundStage.getRoot().getTouchable() != Touchable.enabled 
                || bottom.getTouchable() != Touchable.enabled ){
            return false;
        } else {
            return true;
        }
    }
    
    
    public Actor findActorBackground(int id){
        return this.findActorBackground(id + "");
    }
    
    public Actor findActorBackground(String name){
        return this.backgroundStage.getRoot().findActor(name);
    }
    
    public void removeActorBackground(Actor actor){
        this.backgroundStage.getRoot().removeActor(actor);
        if(actor instanceof Disposable ){
            ((Disposable) actor).dispose();
        }
    }
    
    public void clearActorBackground(){
        this.backgroundStage.clear();
    }

    public void removeActorBackground(int id){
        this.removeActorBackground(id + "");
    }
    
    public void removeActorBackground(String name){
//        Gdx.app.log(TAG, "removeActorBackground(String name) = " + name);
        this.removeActorBackground(this.backgroundStage.getRoot().findActor(name));
    }
    
    public Game getGame(){
        return game;
    }
    
    public float getRootWidth(){
        return rootStage.getWidth();
    }
    
    public float getRootHeight(){
        return rootStage.getHeight();
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D,
                 GL20.GL_TEXTURE_MAG_FILTER,
                 GL20.GL_LINEAR);
        
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D,
                 GL20.GL_TEXTURE_MIN_FILTER,
                 GL20.GL_LINEAR);


        backgroundStage.act(delta);
        rootStage.act(delta);
        backgroundStage.draw();
        rootStage.draw();
    }

    public void renderTableDebug(){
        Table.drawDebug(rootStage);
    }

    @Override
    public void dispose() {
//        Gdx.app.log(TAG, "dispose");
        rootStage.clear();
        rootStage.dispose();
        backgroundStage.clear();
        backgroundStage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == 4  // back button
                || (keycode == 131 && Gdx.app.getType() == ApplicationType.Desktop) // button esc , for desktop back testing
                ){ 
            this.callBack();
            return false;
        } else if ( keycode == 3 ){
            this.callHome();
            return false;
        }

        if(!rootStage.keyDown(keycode)){
            return this.backgroundStage.keyDown(keycode);
        }
        
        return true;
    }

    protected void callHome() {
        
    }

    protected void callBack() {
        
    }

    @Override
    public boolean keyUp(int keycode) {

        if(!rootStage.keyUp(keycode)){
            return this.backgroundStage.keyUp(keycode);
        }
        
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        
        if(!rootStage.keyTyped(character)){
            return this.backgroundStage.keyTyped(character);
        }
        return true;
    }
    
    public void disableBackground(){
        this.backgroundStage.getRoot().setTouchable(Touchable.disabled);
    }
    
    public void enableBackground(){
        this.backgroundStage.getRoot().setTouchable(Touchable.enabled);
    }

    public void disableEvent(){
        this.lockEvent = true;
    }
    
    public void enableEvent(){
        this.lockEvent = false;
    }
    
    public boolean isEventLock(){
        return this.lockEvent;
    }
    
    public boolean isHided(){
        return this.isHided;
    }
    
    
    public Stage getRootStage() {
        return rootStage;
    }

    public Stage getBackgroundStage() {
        return backgroundStage;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if( lockEvent){
            Gdx.app.log(TAG, "touchDown  lockEvent");
            return true;
        }
        
//        Gdx.app.log(TAG, "x - " + x + " : y - " + y);
        if(!rootStage.touchDown(x, y, pointer, button)){
            
            if( this.backgroundStage.getRoot().isVisible() ){

                if(!this.backgroundStage.touchDown(x, y, pointer, button)){
                    
                    this.clickOnBackgroud(x,y);
                }   
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if( lockEvent){
            return true;
        }
        
        if(!rootStage.touchUp(x, y, pointer, button)){
            return backgroundStage.touchUp(x, y, pointer, button);
        }
        
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        if( lockEvent){
            Gdx.app.log(TAG, "locking!!");
            return true;
        }
        
        if( ! rootStage.touchDragged(x, y, pointer) ){
            backgroundStage.touchDragged(x, y, pointer);
            return false;
        }
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

}

