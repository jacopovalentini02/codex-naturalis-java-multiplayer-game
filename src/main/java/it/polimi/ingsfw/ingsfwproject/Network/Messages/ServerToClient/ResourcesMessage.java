package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Content;
import it.polimi.ingsfw.ingsfwproject.Model.ContentCounter;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.util.HashMap;

public class ResourcesMessage extends Message {
    private HashMap<Content, Integer> resources;
    private String nickname;

    public ResourcesMessage(int clientID, HashMap<Content, Integer> resources, String nickname) {
        super(clientID, MessageType.RESOURCES);
        this.resources=resources;
        this.nickname=nickname;
    }

    public HashMap<Content, Integer> getResources() {
        return resources;
    }

    public String getNickname() {
        return nickname;
    }
}
