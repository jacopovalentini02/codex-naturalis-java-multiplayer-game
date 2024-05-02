package it.polimi.ingsfw.ingsfwproject.Network2.Messages;

public enum MessageType {
    CREATE_GAME,
    GAME_JOINED,
    GET_GAME_LIST, //CLIENT ASKS FOR GAMES - SERVER
    SEND_GAME_LIST, //SERVER - CLIENT
    JOIN_GAME
}
