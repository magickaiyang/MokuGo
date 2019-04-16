package controllers;

import play.libs.streams.ActorFlow;
import play.mvc.*;
import akka.actor.*;
import akka.stream.*;
import actors.WebsocketActor;

import javax.inject.Inject;

public class WebsocketController extends Controller {
    private final ActorSystem actorSystem;
    private final Materializer materializer;

    @Inject
    public WebsocketController(ActorSystem actorSystem, Materializer materializer) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
    }

    public WebSocket socket() {
        return WebSocket.Json.accept(request ->
                ActorFlow.actorRef(WebsocketActor::props,
                        actorSystem, materializer
                )
        );
    }
}
