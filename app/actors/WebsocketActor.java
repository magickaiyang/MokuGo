package actors;

import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import akka.actor.*;

public class WebsocketActor extends AbstractActor {
    private final ActorRef out;
    private MokuGoGame m;

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

                        //place user's counter
                        m.setBoardVal(p.pos.y, p.pos.x, 1); //1 for opponent, the backend does NOT check if the row/range is legal

                        //get response of moku AI (counter already placed)
                        Packet response=new Packet();
                        response.status=m.getGameState(); //0 for continue, 1 for opponnent win, 2 for moku win, 3 for tie
                        response.pos=new Packet.Position();
                        int[] choice = m.getMokuChoice(10); //depth>0, proportional to AI smartnesss
                        response.pos.x=choice[1];
                        response.pos.y=choice[0];
                        response.color=0; //-1 for null, 1 for opponent, 0 for moku

                        System.out.println("computer res: x:" + response.pos.x + "y: "+response.pos.y);
                        JsonNode responseJson = Json.toJson(response);
                        out.tell(responseJson, self());
                    } else {
                        System.out.println("New player name: " + name);

                        //create new game
                        this.m = new MokuGoGame(name);

                        //initialize first placement of moku
                        int firstX = 7;
                        int firstY = 7;
                        m.setBoardVal(firstX,firstY,0); //-1 for null, 1 for opponent, 0 for moku

                        //send back first placement of moku
                        Packet response=new Packet();
                        response.status=m.getGameState(); //0 for continue, 1 for opponnent win, 2 for moku win, 3 for tie
                        response.pos=new Packet.Position();
                        response.pos.x=firstX;
                        response.pos.y=firstY;
                        response.color=0; //-1 for null, 1 for opponent, 0 for moku

                        JsonNode responseJson = Json.toJson(response);
                        out.tell(responseJson, self());
                    }
                }
            )
            .build();
    }
}
