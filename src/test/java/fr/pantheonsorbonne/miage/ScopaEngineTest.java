package fr.pantheonsorbonne.miage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import fr.pantheonsorbonne.miage.enums.CardColor;
import fr.pantheonsorbonne.miage.enums.CardValue;
import fr.pantheonsorbonne.miage.game.Card;

class ScopaEngineTest {

    @Test
    void makePairTest() {
        Queue roundDeckTest = new LinkedList<>();
        roundDeckTest.add(new Card[]{new Card(CardColor.DIAMOND, CardValue.SEVEN), new Card(CardColor.HEART, CardValue.ACE), new Card(CardColor.CLUB, CardValue.KING), new Card(CardColor.SPADE, CardValue.SIX)});
        Queue playerCardsTest = new LinkedList<>();
        playerCardsTest.add(new Card[]{new Card(CardColor.HEART, CardValue.SEVEN), new Card(CardColor.DIAMOND, CardValue.ACE), new Card(CardColor.SPADE, CardValue.KING)});

        var test = new LocalScopa(Set.of("Joueur1"));

        String CarteAttendue = "7D";
        Card CarteObtenue = test.makePair(playerCardsTest, roundDeckTest);


        assertEquals(CarteAttendue, CarteObtenue.toString());
    }

    @Test
    void removeRoundDeckCardTest(){
        Queue roundDeckTest = new LinkedList<>();
        roundDeckTest.add(new Card[]{new Card(CardColor.DIAMOND, CardValue.SEVEN), new Card(CardColor.HEART, CardValue.ACE), new Card(CardColor.CLUB, CardValue.KING), new Card(CardColor.SPADE, CardValue.SIX)});

        Card matchCardTest = new Card(CardColor.DIAMOND, CardValue.SEVEN);

        var test = new LocalScopa(Set.of("Joueur1"));

        String CarteAttendue = "7D";
        Card CarteObtenue = test.removeRoundDeckCard(matchCardTest, roundDeckTest);

        assertEquals(CarteAttendue, CarteObtenue);

    }

    @Test
    void bestCountTest(){

        Map<String, Queue<Card>> playerCollectedCardsTest = new HashMap<>();

        Queue J1collectedCards = new LinkedList<>();
        J1collectedCards.add(new Card[]{new Card(CardColor.DIAMOND, CardValue.SEVEN), new Card(CardColor.DIAMOND, CardValue.ACE)});
        Queue J2collectedCards = new LinkedList<>();
        J1collectedCards.add(new Card[]{new Card(CardColor.SPADE, CardValue.SEVEN)});
        Queue J3collectedCards = new LinkedList<>();
        J1collectedCards.add(new Card[]{new Card(CardColor.SPADE, CardValue.ACE)});

        var test = new LocalScopa(Set.of("Joueur1", "Joueur2", "Joueur3"));
        playerCollectedCardsTest.put("Joueur1", J1collectedCards);
        playerCollectedCardsTest.put("Joueur2", J2collectedCards);
        playerCollectedCardsTest.put("Joueur3", J3collectedCards);

        ArrayList<String> résultatAttendu = new ArrayList<>();
        résultatAttendu.add("Joueur1");
        ArrayList<String> résultatObtenu = test.bestCount(playerCollectedCardsTest);

        assertEquals(résultatAttendu, résultatObtenu);
    }



   

    
}