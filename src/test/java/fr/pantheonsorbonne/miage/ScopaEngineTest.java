package fr.pantheonsorbonne.miage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import fr.pantheonsorbonne.miage.enums.CardColor;
import fr.pantheonsorbonne.miage.enums.CardValue;
import fr.pantheonsorbonne.miage.game.Card;

class ScopaEngineTest {

    Queue<Card> initialRoundDeckTest = new LinkedList<>();
    Queue<Card> roundDeckTest = new LinkedList<>();
    Queue<Card> playerCardsTest = new LinkedList<>();
    Map<String, Queue<Card>> playerCollectedCardsTest = new HashMap<>();
    Queue<Card> J1collectedCards = new LinkedList<>();
    Queue<Card> J2collectedCards = new LinkedList<>();
    Queue<Card> J3collectedCards = new LinkedList<>();


    /*
     * tests on the initial round deck
     */
    @Test
    void checkGameResetIfThreeSameCardValue() {

        initialRoundDeckTest.add(new Card(CardColor.CLUB, CardValue.ACE));
        initialRoundDeckTest.add(new Card(CardColor.SPADE, CardValue.ACE));
        initialRoundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        initialRoundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));

        ScopaEngine test = new LocalScopa(Set.of());
        assertEquals(true, test.checkOverThreeSameCardValue(initialRoundDeckTest));
    }

    @Test
    void checkGameResetIfFourSameCardValue() {

        initialRoundDeckTest.add(new Card(CardColor.CLUB, CardValue.ACE));
        initialRoundDeckTest.add(new Card(CardColor.SPADE, CardValue.ACE));
        initialRoundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        initialRoundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));

        ScopaEngine test = new LocalScopa(Set.of());
        assertEquals(true, test.checkOverThreeSameCardValue(initialRoundDeckTest));
    }

    @Test
    void checkGameContinuesIfTwoSameCardValue() {

        initialRoundDeckTest.add(new Card(CardColor.CLUB, CardValue.ACE));
        initialRoundDeckTest.add(new Card(CardColor.SPADE, CardValue.THREE));
        initialRoundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        initialRoundDeckTest.add(new Card(CardColor.HEART, CardValue.TWO));

        ScopaEngine test = new LocalScopa(Set.of());
        assertEquals(false, test.checkOverThreeSameCardValue(initialRoundDeckTest));
    }

    @Test
    void checkGameContinuesIfNoSameCardValue() {

        initialRoundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        initialRoundDeckTest.add(new Card(CardColor.SPADE, CardValue.THREE));
        initialRoundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        initialRoundDeckTest.add(new Card(CardColor.HEART, CardValue.TWO));

        ScopaEngine test = new LocalScopa(Set.of());
        assertEquals(false, test.checkOverThreeSameCardValue(initialRoundDeckTest));
    }

    /*
     * tests on making a pair
     */
    @Test
    void makePairTest() {

        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));
        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));
       
        playerCardsTest.add(new Card(CardColor.HEART, CardValue.SEVEN));
        playerCardsTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.KING));

        var test = new LocalScopa(Set.of("Joueur1"));

        String carteAttendue = "7H";
        Card carteObtenue = test.makePair(playerCardsTest, roundDeckTest);

        assertEquals(carteAttendue, carteObtenue.toString());
    }

    /*
     * 
     */
    @Test
    void removeRoundDeckCardTest(){
        
        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));
        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        Card matchCardTest = new Card(CardColor.DIAMOND, CardValue.SEVEN);

        var test = new LocalScopa(Set.of("Joueur1"));

        String carteAttendue = "7D";
        Card carteObtenue = test.removeRoundDeckCard(matchCardTest, roundDeckTest);

        assertEquals(carteAttendue, carteObtenue.toString());

    }   

    /*
     * tests on bestCount method
     */
    @Test
    void onePlayerHavingBestCountTest() {

        //Joueur1's collected cards
        J1collectedCards.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));
        J1collectedCards.add(new Card(CardColor.CLUB, CardValue.SEVEN));

        //Joueur2's collected cards
        J2collectedCards.add(new Card(CardColor.SPADE, CardValue.FIVE));
        J2collectedCards.add(new Card(CardColor.HEART, CardValue.FIVE));
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.QUEEN));
        J2collectedCards.add(new Card(CardColor.CLUB, CardValue.QUEEN));

        var test = new LocalScopa(Set.of("Joueur1", "Joueur2"));
        playerCollectedCardsTest.put("Joueur1", J1collectedCards);
        playerCollectedCardsTest.put("Joueur2", J2collectedCards);

        ArrayList<String> résultatAttendu = new ArrayList<>();
        résultatAttendu.add("Joueur2");
        ArrayList<String> résultatObtenu = test.bestCount(playerCollectedCardsTest);

        assertEquals(résultatAttendu, résultatObtenu);
    }

    @Test
    void twoPlayersHavingBestCountTest() {

        //Joueur1's collected cards
        J1collectedCards.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));
        J1collectedCards.add(new Card(CardColor.CLUB, CardValue.SEVEN));
        J1collectedCards.add(new Card(CardColor.CLUB, CardValue.KING));
        J1collectedCards.add(new Card(CardColor.SPADE, CardValue.KING));

        //Joueur2's collected cards
        J2collectedCards.add(new Card(CardColor.SPADE, CardValue.FIVE));
        J2collectedCards.add(new Card(CardColor.HEART, CardValue.FIVE));
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.QUEEN));
        J2collectedCards.add(new Card(CardColor.CLUB, CardValue.QUEEN));

        //Joueur3's collected cards
        J3collectedCards.add(new Card(CardColor.SPADE, CardValue.ACE));
        J3collectedCards.add(new Card(CardColor.HEART, CardValue.ACE));

        var test = new LocalScopa(Set.of("Joueur1", "Joueur2", "Joueur3"));
        playerCollectedCardsTest.put("Joueur1", J1collectedCards);
        playerCollectedCardsTest.put("Joueur2", J2collectedCards);
        playerCollectedCardsTest.put("Joueur3", J3collectedCards);

        ArrayList<String> résultatAttendu = new ArrayList<>();
        résultatAttendu.add("Joueur2");
        résultatAttendu.add("Joueur1");
        ArrayList<String> résultatObtenu = test.bestCount(playerCollectedCardsTest);

        assertEquals(résultatAttendu, résultatObtenu);
    }

    @Test
    void onePlayerHavingMostDenierCountTest() {
        
        //Joueur1's collected cards
        J1collectedCards.add(new Card(CardColor.SPADE, CardValue.SEVEN));
        J1collectedCards.add(new Card(CardColor.CLUB, CardValue.SEVEN));
        
        //Joueur2's collected cards
        J2collectedCards.add(new Card(CardColor.SPADE, CardValue.QUEEN));
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.QUEEN));

        //Joueur3's collected cards
        J3collectedCards.add(new Card(CardColor.CLUB, CardValue.FOUR));
        J3collectedCards.add(new Card(CardColor.HEART, CardValue.FOUR));
        J3collectedCards.add(new Card(CardColor.CLUB, CardValue.KING));
        J3collectedCards.add(new Card(CardColor.SPADE, CardValue.KING));

        var test = new LocalScopa(Set.of("Joueur1", "Joueur2", "Joueur3"));
        playerCollectedCardsTest.put("Joueur1", J1collectedCards);
        playerCollectedCardsTest.put("Joueur2", J2collectedCards);
        playerCollectedCardsTest.put("Joueur3", J3collectedCards);

        ArrayList<String> expectedResult = new ArrayList<>();
        expectedResult.add("Joueur2");

        assertEquals(expectedResult, test.mostDenierCount(playerCollectedCardsTest));
    }

    @Test
    void twoPlayersHavingMostDenierCountTest() {
        
        //Joueur1's collected cards
        J1collectedCards.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        J1collectedCards.add(new Card(CardColor.SPADE, CardValue.ACE));
        J1collectedCards.add(new Card(CardColor.SPADE, CardValue.SEVEN));
        J1collectedCards.add(new Card(CardColor.CLUB, CardValue.SEVEN));
        
        //Joueur2's collected cards
        J2collectedCards.add(new Card(CardColor.SPADE, CardValue.TWO));
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.TWO));
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.FOUR));
        J2collectedCards.add(new Card(CardColor.SPADE, CardValue.FOUR));
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.QUEEN));
        J2collectedCards.add(new Card(CardColor.SPADE, CardValue.QUEEN));

        //Joueur3's collected cards
        J3collectedCards.add(new Card(CardColor.DIAMOND, CardValue.SIX));
        J3collectedCards.add(new Card(CardColor.HEART, CardValue.SIX));
        J3collectedCards.add(new Card(CardColor.CLUB, CardValue.KING));
        J3collectedCards.add(new Card(CardColor.DIAMOND, CardValue.KING));
        J3collectedCards.add(new Card(CardColor.DIAMOND, CardValue.JACK));
        J3collectedCards.add(new Card(CardColor.HEART, CardValue.JACK));

        var test = new LocalScopa(Set.of("Joueur1", "Joueur2", "Joueur3"));
        playerCollectedCardsTest.put("Joueur1", J1collectedCards);
        playerCollectedCardsTest.put("Joueur2", J2collectedCards);
        playerCollectedCardsTest.put("Joueur3", J3collectedCards);

        ArrayList<String> expectedResult = new ArrayList<>();
        expectedResult.add("Joueur3");
        expectedResult.add("Joueur2");

        assertEquals(expectedResult, test.mostDenierCount(playerCollectedCardsTest));
    }

    /*
     * tests the settebello
     */
    @Test
    void havingSettebelloTest(){

        //Joueur1's collected cards
        J1collectedCards.add(new Card(CardColor.CLUB, CardValue.SEVEN));
        J1collectedCards.add(new Card(CardColor.DIAMOND, CardValue.ACE));

        //Joueur2's collected cards
        J2collectedCards.add(new Card(CardColor.HEART, CardValue.KING));
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));

        //Joueur3's collected cards
        J3collectedCards.add(new Card(CardColor.SPADE, CardValue.ACE));

        var test = new LocalScopa(Set.of("Joueur1", "Joueur2", "Joueur3"));
        playerCollectedCardsTest.put("Joueur1", J1collectedCards);
        playerCollectedCardsTest.put("Joueur2", J2collectedCards);
        playerCollectedCardsTest.put("Joueur3", J3collectedCards);

        assertEquals("Joueur2", test.havingSettebello(playerCollectedCardsTest));
    }

    @Test
    void notHavingSettebelloTest(){

        //Joueur1's collected cards
        J1collectedCards.add(new Card(CardColor.CLUB, CardValue.SEVEN));
        J1collectedCards.add(new Card(CardColor.DIAMOND, CardValue.ACE));

        //Joueur2's collected cards
        J2collectedCards.add(new Card(CardColor.HEART, CardValue.KING));
        J2collectedCards.add(new Card(CardColor.HEART, CardValue.SEVEN));

        //Joueur3's collected cards
        J3collectedCards.add(new Card(CardColor.SPADE, CardValue.ACE));

        var test = new LocalScopa(Set.of("Joueur1", "Joueur2", "Joueur3"));
        playerCollectedCardsTest.put("Joueur1", J1collectedCards);
        playerCollectedCardsTest.put("Joueur2", J2collectedCards);
        playerCollectedCardsTest.put("Joueur3", J3collectedCards);

        assertEquals(null, test.havingSettebello(playerCollectedCardsTest));
    }

}