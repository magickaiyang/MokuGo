package actors;

import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import akka.actor.*;

public class WebsocketActor extends AbstractActor {
    private final ActorRef out;

    private static class Packet {
        public int status;
        public int color;
        public Position pos;

        public static class Position {
            public int x;
            public int y;
        }
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
                    System.out.println("Raw json: " + json.toString());

                    String name = json.findPath("name").textValue();    //test if it is a initial packet
                    if (name == null) {
                        Packet p = Json.fromJson(json, Packet.class);
                        System.out.println("status:" + p.status + " position x:" + p.pos.x);

                        //call a method of MokuGoGame instance, pass "p", get a packet instance as return  value
                        //Here we'll use a dummy for test purpose
                        Packet response=new Packet();
                        response.status=0;
                        response.pos=new Packet.Position();
                        response.pos.x=9;
                        response.pos.y=13;
                        response.color=0;
                        JsonNode responseJson = Json.toJson(response);
                        out.tell(responseJson, self());
                    } else {
                        System.out.println("New player name: " + name);

                        //Create a new MokuGoGame class and pass the new player name...
                        //call another method of MokuGoGame to put the FIRST black piece
                    }
                }
            )
            .build();
    }
}
