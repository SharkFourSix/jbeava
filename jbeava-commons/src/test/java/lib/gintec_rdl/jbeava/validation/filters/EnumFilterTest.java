package lib.gintec_rdl.jbeava.validation.filters;

import junit.framework.TestCase;
import lib.gintec_rdl.jbeava.validation.FieldResolver;
import lib.gintec_rdl.jbeava.validation.Jbeava;
import lib.gintec_rdl.jbeava.validation.Options;
import lib.gintec_rdl.jbeava.validation.ValidationResults;
import lib.gintec_rdl.jbeava.validation.annotations.Filter;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnumFilterTest extends TestCase {

    private Logger logger;

    public void setUp() throws Exception {
        BasicConfigurator.configure();
        logger = LoggerFactory.getLogger(EnumFilterTest.class);
    }

    public enum Suit {
        Hearts,
        Diamonds,
        Clubs,
        Spades
    }

    static public class Card {
        @Filter(label = "Card suit", filters = {"required", "enum(lib.gintec_rdl.jbeava.validation.filters.EnumFilterTest$Suit)"})
        private Suit suit;

        public Card(Suit suit) {
            this.suit = suit;
        }

        public Suit getSuit() {
            return suit;
        }

        public void setSuit(Suit suit) {
            this.suit = suit;
        }
    }

    static class DealerFieldResolver extends FieldResolver {

        @Override
        public Object getField(String fieldName) {
            return "Spades";
        }
    }

    public void testFilter() {
        Card card;
        ValidationResults results;
        DealerFieldResolver resolver;

        card = new Card(Suit.Hearts);
        resolver = new DealerFieldResolver();
        results = Jbeava.validate(Card.class, resolver, Options.defaults().instance(card));

        assertTrue(results.success());
        assertEquals(card.getSuit(), Suit.Spades);
    }
}