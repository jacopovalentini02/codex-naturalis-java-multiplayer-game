package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Content;
import it.polimi.ingsfw.ingsfwproject.Model.ContentCounter;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Message sent from server to client to update the resources counter of a player.
 */
public class ResourcesMessage extends ServerToClientMessage implements Serializable {
    private final HashMap<Content, Integer> resources;
    private final String nickname;

    /**
     * Constructs a ResourcesMessage with the specified client ID, resources map, and player nickname.
     *
     * @param clientID  the ID of the client receiving the message
     * @param resources a map representing the updated resources of the player
     * @param nickname  the nickname of the player whose resources are updated
     */
    public ResourcesMessage(int clientID, HashMap<Content, Integer> resources, String nickname) {
        super(clientID);
        this.resources=resources;
        this.nickname=nickname;
    }

    /**
     * Sets the updated resources for the specified player in the client's virtual view.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setResourcesForPlayer(nickname, resources);
        client.getView().notifyResourcesUpdate(nickname, resources);
    }
}
