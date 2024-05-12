// Abstract Factory Pattern for creating parsers
public interface ParserFactory {
    XMLParser createXMLParser(MessageType messageType);
}

@Component
public class DefaultParserFactory implements ParserFactory {

    @Override
    public XMLParser createXMLParser(MessageType messageType) {
        switch (messageType) {
            case CAR_DATA:
                return new CarDataXMLParser();
            case BIKE_DATA:
                return new BikeDataXMLParser();
            case BICYCLE_DATA:
                return new BicycleDataXMLParser();
            case AIRPLANE_DATA:
                return new AirplaneDataXMLParser();
            default:
                throw new IllegalArgumentException("Unsupported message type: " + messageType);
        }
    }
}

// Strategy Pattern for parsing XML data
public interface XMLParsingStrategy {
    YourPojo parseXML(String xmlData) throws XMLParsingException;
}

@Component
public class CarDataXMLParsingStrategy implements XMLParsingStrategy {

    @Override
    public YourPojo parseXML(String xmlData) throws XMLParsingException {
        // Parsing logic for car data XML structure
        return null;
    }
}

@Component
public class BikeDataXMLParsingStrategy implements XMLParsingStrategy {

    @Override
    public YourPojo parseXML(String xmlData) throws XMLParsingException {
        // Parsing logic for bike data XML structure
        return null;
    }
}

@Component
public class BicycleDataXMLParsingStrategy implements XMLParsingStrategy {

    @Override
    public YourPojo parseXML(String xmlData) throws XMLParsingException {
        // Parsing logic for bicycle data XML structure
        return null;
    }
}

@Component
public class AirplaneDataXMLParsingStrategy implements XMLParsingStrategy {

    @Override
    public YourPojo parseXML(String xmlData) throws XMLParsingException {
        // Parsing logic for airplane data XML structure
        return null;
    }
}

// Composite Pattern for representing message types
public enum MessageType {
    CAR_DATA,
    BIKE_DATA,
    BICYCLE_DATA,
    AIRPLANE_DATA
}

// Solace Message Listener using Abstract Factory and Strategy patterns
@Component
public class SolaceMessageListener {

    private ParserFactory parserFactory;

    public SolaceMessageListener(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    @JmsListener(destination = "${solace.queue.car}")
    public void receiveCarDataMessage(String message) {
        processMessage(message, MessageType.CAR_DATA);
    }

    @JmsListener(destination = "${solace.queue.bike}")
    public void receiveBikeDataMessage(String message) {
        processMessage(message, MessageType.BIKE_DATA);
    }

    // Similar methods for other message types and queues

    private void processMessage(String message, MessageType messageType) {
        try {
            XMLParser xmlParser = parserFactory.createXMLParser(messageType);
            YourPojo data = xmlParser.parseXML(message);
            // Further processing based on the message type
        } catch (XMLParsingException e) {
            logger.error("Error parsing XML: " + e.getMessage(), e);
        }
    }
}

// Factory Method Pattern for creating parsers based on XML structure
public interface XMLParser {
    YourPojo parseXML(String xmlData) throws XMLParsingException;
}

public class CarDataXMLParser implements XMLParser {
    @Override
    public YourPojo parseXML(String xmlData) throws XMLParsingException {
        // Parsing logic for car data XML structure
        return null;
    }
}

// Similar implementations for other XML parsers
