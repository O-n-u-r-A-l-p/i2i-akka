package com.akka;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Actor2 extends AbstractBehavior<Actor1.Greeted> {

	public static Behavior<Actor1.Greeted> create() {
		
		return Behaviors.setup(Actor2::new);
	}

	private Actor2(ActorContext<Actor1.Greeted> context) {
		
		super(context);
	}

	@Override
	public Receive<Actor1.Greeted> createReceive() {
		
		return newReceiveBuilder().onMessage(Actor1.Greeted.class, this::onGreeted).build();
	}

	private Behavior<Actor1.Greeted> onGreeted(Actor1.Greeted message) {
		
		getContext().getLog().info("Hi from Actor2");
		return this;
	}
}