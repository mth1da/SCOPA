package fr.pantheonsorbonne.miage;

import org.junit.jupiter.api.Test;

//import com.google.common.collect.Lists;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
//import java.util.stream.Collectors;

//import fr.pantheonsorbonne.miage.AppTest.LocalScopaTest;
import fr.pantheonsorbonne.miage.enums.CardColor;
import fr.pantheonsorbonne.miage.enums.CardValue;
import fr.pantheonsorbonne.miage.game.Card;

class ScopaEngineTest {

    Queue<Card> initialRoundDeckTest = new LinkedList<>();
    Queue<Card> roundDeckTest = new LinkedList<>();
    Queue<Card> playerCardsTest = new LinkedList<>();
    ArrayList<Card> pairCardsTest = new ArrayList<>();
    Map<String, Queue<Card>> playerCollectedCardsTest = new HashMap<>();
    Queue<Card> J1collectedCards = new LinkedList<>();
    Queue<Card> J2collectedCards = new LinkedList<>();
    Queue<Card> J3collectedCards = new LinkedList<>();
    Map<String, Integer> playerCollectedScopaTest = new HashMap<>();
    Queue<String> playersTest = new LinkedList<>();


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

    @Test
    void noStrategyTest(){

        roundDeckTest.add(new Card(CardColor.HEART, CardValue.TWO));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.FIVE));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        playerCardsTest.add(new Card(CardColor.HEART, CardValue.SIX));
        playerCardsTest.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        playerCardsTest.add(new Card(CardColor.SPADE, CardValue.TWO));

        var test = new LocalScopa(Set.of("Joueur1"));

        assertEquals("6H", test.noStrategy(playerCardsTest,roundDeckTest).get(0).toString());
    }
    
    /*
    @Test
    public void shouldAnswerWithTrue() {
 
        Card[] cardsJ1 = new Card[]{new Card(CardColor.DIAMOND, CardValue.ACE),new Card(CardColor.DIAMOND, CardValue.ACE), new Card(CardColor.DIAMOND, CardValue.SEVEN)};
        Card[] cardsJ2 = new Card[]{ new Card(CardColor.DIAMOND, CardValue.FIVE)};
        Deck.getRandomCards(40);
        LocalScopaTest localScopaTest = new LocalScopaTest(

        Arrays.stream((new String[]{"J1", "J2"})).collect(Collectors.toSet()), Map.of("J1", cardsJ1, "J2", cardsJ2), Lists.newArrayList(new Card(CardColor.SPADE, CardValue.SEVEN)));
        localScopaTest.play();

        Map<String, Integer> scores=localScopaTest.countPlayersScores(localScopaTest.playerCollectedCards);
        System.out.println("Scores are:"+scores);
        
    }*/

    @Test
    void processPairCardsTest(){
    	 
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.TWO));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.FIVE));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        //the player's selected card
        pairCardsTest.add(new Card(CardColor.HEART, CardValue.TWO));
        //the pair of the deck
        pairCardsTest.add(new Card(CardColor.SPADE, CardValue.TWO));
        
        var test = new LocalScopa(Set.of("Joueur1"));
        test.initCollectedAndScopaCards();
        J1collectedCards.add(new Card(CardColor.HEART, CardValue.TWO));
        J1collectedCards.add(new Card(CardColor.SPADE, CardValue.TWO));

        playerCollectedCardsTest.put("Joueur1", J1collectedCards);

        String expected = playerCollectedCardsTest.get("Joueur1").toString();
        String actual = test.processPairCards("Joueur1", pairCardsTest, roundDeckTest).get("Joueur1").toString();

        assertEquals(expected, actual);
    }


    /*
     * tests on whether a point for a scopa is given or not
     */
    @Test
    void processAScopaPointTest(){
    
        //the player's selected card
        pairCardsTest.add(new Card(CardColor.DIAMOND, CardValue.KING));
        //the pair of the deck
        pairCardsTest.add(new Card(CardColor.HEART, CardValue.KING));
        
        var test = new LocalScopa(Set.of("Joueur1"));
        test.initCollectedAndScopaCards();
        J1collectedCards.add(new Card(CardColor.DIAMOND, CardValue.KING));
        J1collectedCards.add(new Card(CardColor.HEART, CardValue.KING));

        playerCollectedScopaTest.put("Joueur1", 1);

        int expected = playerCollectedScopaTest.get("Joueur1");
        int actual = test.processScopaPoint("Joueur1", roundDeckTest).get("Joueur1");

        assertEquals(expected, actual);
    }

    @Test
    void processNoScopaPointTest(){

        //there are still cards in the round deck
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.TWO));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.ACE));

        //the player's selected card
        pairCardsTest.add(new Card(CardColor.DIAMOND, CardValue.KING));
        //the pair of the deck
        pairCardsTest.add(new Card(CardColor.HEART, CardValue.KING));
        
        var test = new LocalScopa(Set.of("Joueur1"));
        test.initCollectedAndScopaCards();
        J1collectedCards.add(new Card(CardColor.DIAMOND, CardValue.KING));
        J1collectedCards.add(new Card(CardColor.HEART, CardValue.KING));

        playerCollectedScopaTest.put("Joueur1", 0);

        int expected = playerCollectedScopaTest.get("Joueur1");
        int actual = test.processScopaPoint("Joueur1", roundDeckTest).get("Joueur1");

        assertEquals(expected, actual);
    }

    /*
     * test 
     */
    @Test
    void addRemainingCardsToTheLastPlayerCollectedCards(){
    	 
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.TWO));
        roundDeckTest.add(new Card(CardColor.CLUB, CardValue.KING));
        roundDeckTest.add(new Card(CardColor.DIAMOND, CardValue.FIVE));
        roundDeckTest.add(new Card(CardColor.SPADE, CardValue.SIX));

        var test = new LocalScopa(Set.of("Joueur1"));
        test.initCollectedAndScopaCards();

        playersTest.offer("Joueur1");

        J1collectedCards.add(new Card(CardColor.SPADE, CardValue.TWO));
        J1collectedCards.add(new Card(CardColor.CLUB, CardValue.KING));
        J1collectedCards.add(new Card(CardColor.DIAMOND, CardValue.FIVE));
        J1collectedCards.add(new Card(CardColor.SPADE, CardValue.SIX));
        playerCollectedCardsTest.put("Joueur1", J1collectedCards);

        String expected = playerCollectedCardsTest.get("Joueur1").toString();
        String actual = test.addRemainingCardsToCollected(roundDeckTest, playersTest).get("Joueur1").toString();

        assertEquals(expected, actual);
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

    @Test
    public void countPlayersScoreTest() {

        //Joueur1's collected cards
        J1collectedCards.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        J1collectedCards.add(new Card(CardColor.CLUB, CardValue.SEVEN));
        J1collectedCards.add(new Card(CardColor.HEART, CardValue.ACE));
        J1collectedCards.add(new Card(CardColor.SPADE, CardValue.SEVEN));

        //Joueur2's collected cards
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.TWO));
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));

        //Joueur3's collected cards
        J3collectedCards.add(new Card(CardColor.SPADE, CardValue.ACE));

        playerCollectedCardsTest.put("Joueur1", J1collectedCards);
        playerCollectedCardsTest.put("Joueur2", J2collectedCards);
        playerCollectedCardsTest.put("Joueur3", J3collectedCards);

        playerCollectedScopaTest.put("Joueur1", 1);
        playerCollectedScopaTest.put("Joueur2", 0);
        playerCollectedScopaTest.put("Joueur3", 0);

        var test = new LocalScopa(Set.of("Joueur1", "Joueur2", "Joueur3"));
        
        Map<String, Integer> playerScoresTest = new HashMap<>();
        playerScoresTest.put("Joueur1", 2);
        playerScoresTest.put("Joueur2", 2);
        playerScoresTest.put("Joueur3", 0);

        assertEquals(playerScoresTest, test.countPlayersScores(playerCollectedCardsTest, playerCollectedScopaTest));
    }

    @Test
    public void countPlayersScoreTestWith2BestCount() {

        //Joueur1's collected cards
        J1collectedCards.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        J1collectedCards.add(new Card(CardColor.CLUB, CardValue.SEVEN));
        J1collectedCards.add(new Card(CardColor.HEART, CardValue.ACE));

        //Joueur2's collected cards
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.TWO));
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));

        //Joueur3's collected cards
        J3collectedCards.add(new Card(CardColor.SPADE, CardValue.ACE));

        playerCollectedCardsTest.put("Joueur1", J1collectedCards);
        playerCollectedCardsTest.put("Joueur2", J2collectedCards);
        playerCollectedCardsTest.put("Joueur3", J3collectedCards);

        playerCollectedScopaTest.put("Joueur1", 1);
        playerCollectedScopaTest.put("Joueur2", 0);
        playerCollectedScopaTest.put("Joueur3", 2);

        var test = new LocalScopa(Set.of("Joueur1", "Joueur2", "Joueur3"));
        
        Map<String, Integer> playerScoresTest = new HashMap<>();
        playerScoresTest.put("Joueur1", 2);
        playerScoresTest.put("Joueur2", 3);
        playerScoresTest.put("Joueur3", 2);

        assertEquals(playerScoresTest, test.countPlayersScores(playerCollectedCardsTest, playerCollectedScopaTest));
    }

    @Test
    public void countPlayersScoreTestWith3mostDenierCount() {

        //Joueur1's collected cards
        J1collectedCards.add(new Card(CardColor.DIAMOND, CardValue.ACE));
        J1collectedCards.add(new Card(CardColor.CLUB, CardValue.SEVEN));
        J1collectedCards.add(new Card(CardColor.HEART, CardValue.ACE));

        //Joueur2's collected cards
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));

        //Joueur3's collected cards
        J3collectedCards.add(new Card(CardColor.DIAMOND, CardValue.ACE));

        playerCollectedCardsTest.put("Joueur1", J1collectedCards);
        playerCollectedCardsTest.put("Joueur2", J2collectedCards);
        playerCollectedCardsTest.put("Joueur3", J3collectedCards);

        playerCollectedScopaTest.put("Joueur1", 1);
        playerCollectedScopaTest.put("Joueur2", 0);
        playerCollectedScopaTest.put("Joueur3", 1);

        var test = new LocalScopa(Set.of("Joueur1", "Joueur2", "Joueur3"));
        
        Map<String, Integer> playerScoresTest = new HashMap<>();
        playerScoresTest.put("Joueur1", 3);
        playerScoresTest.put("Joueur2", 2);
        playerScoresTest.put("Joueur3", 2);

        assertEquals(playerScoresTest, test.countPlayersScores(playerCollectedCardsTest, playerCollectedScopaTest));
    }

    @Test
    public void countPlayersScoreTestWithHavingSettebello() {

        //Joueur1's collected cards
        J1collectedCards.add(new Card(CardColor.CLUB, CardValue.SEVEN));
        J1collectedCards.add(new Card(CardColor.HEART, CardValue.ACE));

        //Joueur2's collected cards
        J2collectedCards.add(new Card(CardColor.DIAMOND, CardValue.SEVEN));

        //Joueur3's collected cards
        J3collectedCards.add(new Card(CardColor.DIAMOND, CardValue.ACE));

        playerCollectedCardsTest.put("Joueur1", J1collectedCards);
        playerCollectedCardsTest.put("Joueur2", J2collectedCards);
        playerCollectedCardsTest.put("Joueur3", J3collectedCards);

        playerCollectedScopaTest.put("Joueur1", 0);
        playerCollectedScopaTest.put("Joueur2", 1);
        playerCollectedScopaTest.put("Joueur3", 0);

        var test = new LocalScopa(Set.of("Joueur1", "Joueur2", "Joueur3"));
        
        Map<String, Integer> playerScoresTest = new HashMap<>();
        playerScoresTest.put("Joueur1", 1);
        playerScoresTest.put("Joueur2", 3);
        playerScoresTest.put("Joueur3", 1);

        assertEquals(playerScoresTest, test.countPlayersScores(playerCollectedCardsTest, playerCollectedScopaTest));
    }
}