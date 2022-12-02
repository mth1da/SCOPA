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

        roundDeckTest.add(new Card(CardColor.HEART, CardValue.SEVEN));
        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.FOUR));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));
       
        playerCardsTest.add(new Card(CardColor.CLUB, CardValue.THREE));
        playerCardsTest.add(new Card(CardColor.HEART, CardValue.FOUR));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.KING));

        var test = new LocalScopa(Set.of("Joueur1"));

        String carteAttendue = "4H";
        Card carteObtenue = (test.makePair(playerCardsTest, roundDeckTest)).get(0);

        assertEquals(carteAttendue, carteObtenue.toString());
    }

    @Test
    void cantMakePairTest() {

        roundDeckTest.add(new Card(CardColor.HEART, CardValue.SEVEN));
        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.FOUR));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));
       
        playerCardsTest.add(new Card(CardColor.CLUB, CardValue.THREE));
        playerCardsTest.add(new Card(CardColor.HEART, CardValue.QUEEN));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.KING));

        var test = new LocalScopa(Set.of("Joueur1"));

        assertEquals(true, test.makePair(playerCardsTest, roundDeckTest).isEmpty());
    }

    @Test
    void makePairWithoutSettebelloAndDenierTest() {

        roundDeckTest.add(new Card(CardColor.HEART, CardValue.SEVEN));
        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));
       
        playerCardsTest.add(new Card(CardColor.CLUB, CardValue.THREE));
        playerCardsTest.add(new Card(CardColor.HEART, CardValue.FOUR));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.KING));

        var test = new LocalScopa(Set.of("Joueur1"));

        String carteAttendue = "KS";
        Card carteObtenue = test.makePair(playerCardsTest, roundDeckTest).get(0);

        assertEquals(carteAttendue, carteObtenue.toString());
    }

    /*
     * tests on settebello strategy when the settebello is in the round deck
     */
    @Test
    void makingPairWithSettebelloInDeckStrategyTest(){

        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        playerCardsTest.add(new Card(CardColor.HEART, CardValue.SEVEN));
        playerCardsTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.KING));

        var test = new LocalScopa(Set.of("Joueur1"));

        assertEquals("7H", test.settebelloInDeckStrategy(playerCardsTest,roundDeckTest).get(0).toString());
    }

    @Test
    void cantMakePairWithSettebelloInDeckStrategyTest(){

        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        playerCardsTest.add(new Card(CardColor.HEART, CardValue.THREE));
        playerCardsTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.KING));

        var test = new LocalScopa(Set.of("Joueur1"));

        assertEquals(true, test.settebelloInDeckStrategy(playerCardsTest,roundDeckTest).isEmpty());
    }

    /*
     * tests on settebello strategy when the settebello is in the player's hand
     */
    @Test
    void makingPairWithSettebelloInHandStrategyTest(){

        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.HEART, CardValue.SEVEN));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        playerCardsTest.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));
        playerCardsTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.KING));

        var test = new LocalScopa(Set.of("Joueur1"));

        assertEquals("7D", test.settebelloInHandStrategy(playerCardsTest,roundDeckTest).get(0).toString());
    }

    @Test
    void cantMakePairWithSettebelloInHandStrategyTest(){

        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.QUEEN));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        playerCardsTest.add(new Card(CardColor.HEART, CardValue.THREE));
        playerCardsTest.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.KING));

        var test = new LocalScopa(Set.of("Joueur1"));

        assertEquals(true, test.settebelloInDeckStrategy(playerCardsTest,roundDeckTest).isEmpty());
    }

    /*
     * tests on the denier strategy when there's a denier card in the deck
     */
    @Test
    void makingPairWithDenierInDeckStrategyTest(){

        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.FIVE));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        playerCardsTest.add(new Card(CardColor.HEART, CardValue.SEVEN));
        playerCardsTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.FIVE));

        var test = new LocalScopa(Set.of("Joueur1"));

        assertEquals("5S", test.denierCardInDeckStrategy(playerCardsTest,roundDeckTest).get(0).toString());
    }

    @Test
    void cantMakePairWithDenierInDeckStrategyTest(){

        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.FIVE));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        playerCardsTest.add(new Card(CardColor.HEART, CardValue.SEVEN));
        playerCardsTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.KING));

        var test = new LocalScopa(Set.of("Joueur1"));

        assertEquals(true, test.denierCardInDeckStrategy(playerCardsTest,roundDeckTest).isEmpty());
    }

    /*
     * tests on the denier strategy when the denier card is in the player's hand
     */
    @Test
    void makingPairWithDenierInHandStrategyTest(){

        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.FIVE));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        playerCardsTest.add(new Card(CardColor.HEART, CardValue.SEVEN));
        playerCardsTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.FIVE));

        var test = new LocalScopa(Set.of("Joueur1"));

        assertEquals("1D", test.denierCardInHandStrategy(playerCardsTest,roundDeckTest).get(0).toString());
    }

    @Test
    void cantMakePairWithDenierInHandStrategyTest(){

        roundDeckTest.add(new Card(CardColor.HEART, CardValue.TWO));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.FIVE));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        playerCardsTest.add(new Card(CardColor.HEART, CardValue.SEVEN));
        playerCardsTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.KING));

        var test = new LocalScopa(Set.of("Joueur1"));

        assertEquals(true, test.denierCardInHandStrategy(playerCardsTest,roundDeckTest).isEmpty());
    }

    /*
     * 
     */
    @Test
    void removeRoundDeckCardTest(){

        Card matchCardTest = new Card(CardColor.SPADE, CardValue.SIX);
        
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.SEVEN));
        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));
        roundDeckTest.add(matchCardTest);

        var test = new LocalScopa(Set.of("Joueur1"));

        assertEquals(matchCardTest, test.removeRoundDeckCard(matchCardTest, roundDeckTest));

    }   

    @Test
    void removeRoundDeckNullTest(){

        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));
        roundDeckTest.add(new Card(CardColor.HEART, CardValue.ACE));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        Card matchCardTest = new Card(CardColor.DIAMOND, CardValue.FIVE);

        var test = new LocalScopa(Set.of("Joueur1"));

        Card carteAttendue = null;
        Card carteObtenue = test.removeRoundDeckCard(matchCardTest, roundDeckTest);

        assertEquals(carteAttendue, carteObtenue);

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

        ArrayList<String> resultatAttendu = new ArrayList<>();
        resultatAttendu.add("Joueur2");
        ArrayList<String> resultatObtenu = test.bestCount(playerCollectedCardsTest);

        assertEquals(resultatAttendu, resultatObtenu);
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

        ArrayList<String> resultatAttendu = new ArrayList<>();
        resultatAttendu.add("Joueur2");
        resultatAttendu.add("Joueur1");
        ArrayList<String> resultatObtenu = test.bestCount(playerCollectedCardsTest);

        assertEquals(resultatAttendu, resultatObtenu);
    }

    /*
     * tests on mostDenierCount method
     */
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
     * tests on the settebello
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