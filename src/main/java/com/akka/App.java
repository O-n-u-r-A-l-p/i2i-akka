package com.akka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class App extends AbstractBehavior<App.SayHi> {

    public static class SayHi {
        
    	public final String name;

        public SayHi(String name) {
            
        	this.name = name;
        }
    }
    
    private final ActorRef<Actor1.Greet> greeting;

    public static Behavior<SayHi> create() {
    	
        return Behaviors.setup(App::new);
    }

    private App(ActorContext<SayHi> context) {
    	
        super(context);
        greeting = context.spawn(Actor1.create(), "Actor1");
    }

    @Override
    public Receive<SayHi> createReceive() {
    	
        return newReceiveBuilder().onMessage(SayHi.class, this::sayHi).build();
    }

    private Behavior<SayHi> sayHi(SayHi command) {
    	
        ActorRef<Actor1.Greeted> replyTo = getContext().spawn(Actor2.create(), command.name);
        greeting.tell(new Actor1.Greet(command.name, replyTo));
        return this;
    }

    public static void main(String[] args) {
        
    	final ActorSystem<SayHi> Actor1 = ActorSystem.create(App.create(), "Actors");
        Actor1.tell(new App.SayHi("Actor2"));
        
        try 
        {
        } catch (Exception e) {
        }

        Actor1.terminate();
    }
}
