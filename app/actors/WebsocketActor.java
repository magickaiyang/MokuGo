package actors;

import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import akka.actor.*;

public class WebsocketActor extends AbstractActor {
    private final ActorRef out;

    class Packet {

    }

    public WebsocketActor(ActorRef out) {
        this.out = out;
    }

    public static Props props(ActorRef out) {
        return Props.create(WebsocketActor.class, out);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JsonNode.class, json -> {
                            out.tell("I received your message: " + json, self());
                        }
                )
                .build();
    }
}
