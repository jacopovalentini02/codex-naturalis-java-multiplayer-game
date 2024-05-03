package it.polimi.ingsfw.ingsfwproject.Network.Messages;

public enum MessageType {
    FIRST_MESSSAGE,
    CREATE_GAME,
    GAME_JOINED,
    GET_GAME_LIST, //CLIENT ASKS FOR GAMES - SERVER
    SEND_GAME_LIST, //SERVER - CLIENT
    JOIN_GAME,

    INVALID_NUM_OF_PLAYERS,

    NICK_ALREADY_TAKEN,

    GAME_FULL,

    GAME_NOT_EXISTS,

    INVALID_REQUEST,

}
