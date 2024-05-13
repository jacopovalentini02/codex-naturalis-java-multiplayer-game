package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Content;
import it.polimi.ingsfw.ingsfwproject.Model.ContentCounter;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.HashMap;

public class ResourcesMessage extends ServerToClientMessage implements Serializable {
    private final HashMap<Content, Integer> resources;
    private final String nickname;

    public ResourcesMessage(int clientID, HashMap<Content, Integer> resources, String nickname) {
        super(clientID);
        this.resources=resources;
        this.nickname=nickname;
    }

    public HashMap<Content, Integer> getResources() {
        return resources;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setResourcesForPlayer(nickname, resources);
        client.getView().notifyResourcesUpdate(nickname, resources);
    }
}
