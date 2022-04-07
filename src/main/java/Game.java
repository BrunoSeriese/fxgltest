import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends GameApplication {

    private Entity  player;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(1200);
        gameSettings.setHeight(800);
    }
    @Override
    protected void initGame(){
        player = FXGL.entityBuilder()
                .at(400,400)
                .viewWithBBox("dude.png")
                .with(new CollidableComponent(true))
                .scale(0.9,0.9)
                .type(EntityTypes.PLAYER)
                .buildAndAttach();
        FXGL.getGameTimer().runAtInterval(()-> {
            int randomX = ThreadLocalRandom.current().nextInt(80,FXGL.getGameScene().getAppWidth() -80);
            int randomY = ThreadLocalRandom.current().nextInt(80,FXGL.getGameScene().getAppHeight() -80);
            FXGL.entityBuilder()
                    .at(randomX,randomY)
                    .viewWithBBox(new Circle(8,Color.BLACK))
                    .with(new CollidableComponent(true))
                    .type(EntityTypes.DEATHCIRCLE)
                    .buildAndAttach();
        }, Duration.millis(3000));



    }

    @Override
    protected void initInput(){
        FXGL.onKey(KeyCode.D, () ->{
            player.translateX(5);
        });
        FXGL.onKey(KeyCode.A, () ->{
            player.translateX(-5);
        });
        FXGL.onKey(KeyCode.W, () ->{
            player.translateY(-5);
        });
        FXGL.onKey(KeyCode.S, () ->{
            player.translateY(5);
        });
    }

    @Override
    protected void initUI(){
        Label myLabel = new Label("Oh Hello there");
        myLabel.setTranslateX(600);
        myLabel.setTranslateY(500);

        myLabel.textProperty().bind(FXGL.getWorldProperties().intProperty("kills").asString());

        FXGL.getGameScene().addUINode(myLabel);
    }
    @Override
    protected void initGameVars(Map<String, Object> vars){
        vars.put("kills",0);

    }

    @Override
    protected void initPhysics(){
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityTypes.PLAYER, EntityTypes.DEATHCIRCLE) {
            @Override
            protected void onCollision(Entity player, Entity dCircle) {
                FXGL.inc("kills", +1);
                dCircle.removeFromWorld();
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
