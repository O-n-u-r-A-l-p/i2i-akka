package com.akka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Actor1 extends AbstractBehavior<Actor1.Greet> {

	public static final class Greet {
		
		public final String whom;
		public final ActorRef<Greeted> replyTo;

		public Greet(String whom, ActorRef<Greeted> replyTo) {
			
			this.whom = whom;
			this.replyTo = replyTo;
		}
	}

	public static final class Greeted {
		
		public final String whom;
		public final ActorRef<Greet> from;

		public Greeted(String whom, ActorRef<Greet> from) {
			
			this.whom = whom;
			this.from = from;
		}
	}

	public static Behavior<Greet> create() {
		
		return Behaviors.setup(Actor1::new);
	}

	private Actor1(ActorContext<Greet> context) {
		
		super(context);
	}

	@Override
	public Receive<Greet> createReceive() {
		
		return newReceiveBuilder().onMessage(Greet.class, this::onGreet).build();
	}

	private Behavior<Greet> onGreet(Greet command) {
		
		getContext().getLog().info("Hi from Actor1");
		command.replyTo.tell(new Greeted(command.whom, getContext().getSelf()));
		return this;
	}
}
