package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class MyGdxGame implements ApplicationListener {
	
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;
	static final float DEGTORAD = 0.0174533f;
	
	Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	
	private World world;
    private Body cart ;
    private Body wheel1;
    private Body wheel2;
    
    private Body axel1;
    private RevoluteJoint motor1;
    private RevoluteJointDef joint1;
    private PrismaticJoint spring1;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
	
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
		camera.viewportHeight = 320;
		camera.viewportWidth = 480;
		camera.position.set(camera.viewportWidth*.5f, camera.viewportHeight*.5f, 0f);
		camera.update();
		
		batch = new SpriteBatch();
		
		world = new World(new Vector2(0, -100), true);
		
		//GROUND
		BodyDef groundBodyDef =new BodyDef();  
		groundBodyDef.position.set(new Vector2(0, 5));  
		Body groundBody = world.createBody(groundBodyDef);  
		PolygonShape groundBox = new PolygonShape();  
		groundBox.setAsBox(camera.viewportWidth*2, 10.0f);
		groundBody.createFixture(groundBox, 0.0f); 
		groundBox.dispose();
		
		
		//CART
		 BodyDef bodyDef = new BodyDef();
         bodyDef.position.set(camera.viewportWidth/2, 100f);
         bodyDef.type = BodyType.DynamicBody;
 
         cart = world.createBody(bodyDef);
         
         FixtureDef boxDef = new FixtureDef();
         boxDef.density = .1f;
         boxDef.friction = 0.5f;
         boxDef.restitution = 0.2f;
         boxDef.filter.groupIndex = -1;
         
         PolygonShape polyShape = new PolygonShape();  
         polyShape.setAsBox(50f, 10f);
         boxDef.shape = polyShape;
         cart.createFixture(boxDef);
         
         polyShape.setAsBox(15f, 5f, new Vector2(-50f, -10f), 90*DEGTORAD);
         boxDef.shape = polyShape;
         cart.createFixture(boxDef);
         
         polyShape.setAsBox(5f, 15f, new Vector2(50f, -10f), -180*DEGTORAD);
         boxDef.shape = polyShape;
         cart.createFixture(boxDef);
         
         
         boxDef.density = 1;
		
		//axels
		axel1 = world.createBody(bodyDef);
		polyShape.setAsBox(3, 15, new Vector2(50f, -30f), -180*DEGTORAD);
		boxDef.shape = polyShape;
		axel1.createFixture(boxDef);
        
		PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
//        prismaticJointDef.bodyA = cart;
//        prismaticJointDef.bodyB = axel1;
       // prismaticJointDef.collideConnected = false;
//        prismaticJointDef.localAxisA.set(0, 1);
//        prismaticJointDef.localAnchorA.set(50, -10);
//        prismaticJointDef.localAnchorB.set(50, -10);
        prismaticJointDef.initialize(cart, axel1, axel1.getWorldCenter(), new Vector2(0, 1));
        prismaticJointDef.referenceAngle = 0*DEGTORAD;
        prismaticJointDef.enableLimit = true;
        prismaticJointDef.lowerTranslation = -5f;
        prismaticJointDef.upperTranslation = 10f;
        prismaticJointDef.enableMotor = true;
//        prismaticJointDef.maxMotorForce = -500f;//this is a powerful machine after all...
//        prismaticJointDef.motorSpeed = -99999999999f;
        spring1 = (PrismaticJoint) world.createJoint(prismaticJointDef);
//     
        
//        //WHEELS
//       // bodyDef = new BodyDef();
//        bodyDef.type = BodyType.DynamicBody;
//		bodyDef.position.set(axel1.getWorldCenter().x, axel1.getWorldCenter().y);
//		
//		wheel1 = world.createBody(bodyDef);
//		CircleShape circle = new CircleShape();
//		circle.setRadius(20f);
//		FixtureDef fixtureDef = new FixtureDef();
//		fixtureDef.shape = circle;
//		fixtureDef.density = 0.1f; 
//		fixtureDef.friction = 5f;
//		fixtureDef.restitution = 0.2f; // Make it bounce a little bit
//		wheel1.createFixture(fixtureDef);
//		circle.dispose();
//		
//		// add joints //
//		joint1 = new RevoluteJointDef();
//		joint1.enableMotor = true;
//        
//		joint1.initialize(axel1, wheel1, wheel1.getWorldCenter());
//        motor1 = (RevoluteJoint) world.createJoint(joint1);
//        motor1.setMaxMotorTorque(500f);
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {
		//Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			world = null;
			create();
			return;
		}
		
		if(spring1.getJointTranslation() > spring1.getLowerLimit()){
			Gdx.app.log("upper limit", "over limit");
		}
		
		spring1.setMaxMotorForce(Math.abs(600*spring1.getJointTranslation()));
        spring1.setMotorSpeed((spring1.getMotorSpeed()*spring1.getJointTranslation()));
        
        Gdx.app.log("translation", Float.toString(spring1.getJointTranslation()));
		
		//motor1.setMotorSpeed(Gdx.input.isTouched()? 500 : 0);
      //  motor1.setMaxMotorTorque(Gdx.input.isTouched()? 500 : 0);

        spring1.setMaxMotorForce((float) (30+Math.abs(2000*Math.pow(spring1.getJointTranslation(), 2))));
       spring1.setMotorSpeed((float) ((spring1.getMotorSpeed() - 10*spring1.getJointTranslation())*1));         
        
		
		world.step(1/60f, 6, 2);
		debugRenderer.render(world, camera.combined);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		wheel1.applyForceToCenter(new Vector2(0, 200f));
	}
}
