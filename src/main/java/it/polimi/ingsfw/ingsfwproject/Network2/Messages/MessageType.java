package it.polimi.ingsfw.ingsfwproject.Network2.Messages;

public enum MessageType {
    CREATE_GAME,
    GAME_CREATED,
    GET_GAME_LIST, //CLIENT ASKS FOR GAMES
    SEND_GAME_LIST //SERVER
}
