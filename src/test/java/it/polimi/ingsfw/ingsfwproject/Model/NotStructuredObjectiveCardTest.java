
package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class NotStructuredObjectiveCardTest {

    @Test
    void verifyObjective() throws RemoteException {
        GameServerInstance gameServerInstance=new GameServerInstance();

        Player player1 = new Player("player1", gameServerInstance, 0);
        PlayerGround ground = player1.getGround();
        GameManager manager = new GameManager();
        Game game = new Game(gameServerInstance,manager, 1, 4, player1);
        game.setUpCards();

        ground.setContentCount(Content.FUNGI_KINGDOM, 5);
        ground.setContentCount(Content.PLANT_KINGDOM,5);
        ground.setContentCount(Content.INSECT_KINGDOM, 5);
        ground.setContentCount(Content.ANIMAL_KINGDOM, 5);
        ground.setContentCount(Content.QUILL, 5);
        ground.setContentCount(Content.MANUSCRIPT, 5);
        ground.setContentCount(Content.INKWELL, 5);

        assertEquals(5, ground.getContentCount(Content.FUNGI_KINGDOM));
        assertEquals(5, ground.getContentCount(Content.PLANT_KINGDOM));
        assertEquals(5, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(5, ground.getContentCount(Content.ANIMAL_KINGDOM));
        assertEquals(5, ground.getContentCount(Content.QUILL));
        assertEquals(5, ground.getContentCount(Content.MANUSCRIPT));
        assertEquals(5, ground.getContentCount(Content.INKWELL));


        ArrayList<Card> objectiveCards = game.getObjectiveDeck().getCardList();

        for(int i = 0; i < 10000000; i++) {

            ObjectiveCard fungiNonStructuredObjectiveCard = null;

            for (Card card : objectiveCards) {
                if (card.getIdCard() == 95)
                    fungiNonStructuredObjectiveCard = (ObjectiveCard) card;
            }
            Random r = new Random();
            int randomNumber = r.nextInt(20);

            ground.setContentCount(Content.FUNGI_KINGDOM, randomNumber);

            assert fungiNonStructuredObjectiveCard != null;

            int points = fungiNonStructuredObjectiveCard.verifyObjective(ground);

            assertEquals((randomNumber / 3) * 2, points);

            ObjectiveCard plantCard = null;
            for (Card card : objectiveCards) {
                if (card.getIdCard() == 96)
                    plantCard = (ObjectiveCard) card;
            }

            ground.setContentCount(Content.PLANT_KINGDOM, randomNumber);

            assert plantCard != null;
            int points2 = plantCard.verifyObjective(ground);
            assertEquals((randomNumber / 3) * 2, points2);


            ObjectiveCard animalCard = null;

            for (Card card : objectiveCards) {
                if (card.getIdCard() == 97)
                    animalCard = (ObjectiveCard) card;
            }

            ground.setContentCount(Content.ANIMAL_KINGDOM, randomNumber);

            assert animalCard != null;
            int points3 = animalCard.verifyObjective(ground);
            assertEquals((randomNumber / 3) * 2, points3);

            ObjectiveCard insectCard = null;

            for (Card card : objectiveCards) {
                if (card.getIdCard() == 98)
                    insectCard = (ObjectiveCard) card;
            }

            ground.setContentCount(Content.INSECT_KINGDOM, randomNumber);

            assert insectCard != null;
            int points4 = insectCard.verifyObjective(ground);
            assertEquals((randomNumber / 3) * 2, points4);


            ObjectiveCard tripleSetCard = null;

            for (Card card : objectiveCards) {
                if (card.getIdCard() == 99)
                    tripleSetCard = (ObjectiveCard) card;
            }

            int x = r.nextInt(20);
            int y = r.nextInt(20);
            int z = r.nextInt(20);

            int min = Math.min(Math.min(x, y), z);

            ground.setContentCount(Content.QUILL, x);
            ground.setContentCount(Content.INKWELL, y);
            ground.setContentCount(Content.MANUSCRIPT, z);

            assert tripleSetCard != null;
            int points5 = tripleSetCard.verifyObjective(ground);

            assertEquals(min * 3, points5);

            for (Card card: objectiveCards){
                if (card.getIdCard() >= 95 && card.getIdCard() <= 98){
                    NotStructuredObjectiveCard c = (NotStructuredObjectiveCard) card;
                    assertEquals(c.toString(), "NotStructuredObjectiveCard{" +
                            "objectRequested=" + c.getObjectRequested() +
                            '}');
                }
            }


        }

    }
}
