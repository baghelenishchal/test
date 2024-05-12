import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CarDataXMLParsingStrategy implements XMLParsingStrategy {

    @Override
    public YourPojo parseXML(String xmlData) throws XMLParsingException {
        try (StringReader stringReader = new StringReader(xmlData)) {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(stringReader);

            List<Map<String, String>> incidents = new ArrayList<>();
            Map<String, String> extractedData = new HashMap<>();
            String currentElement = null;

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        currentElement = reader.getLocalName();
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (currentElement != null) {
                            String value = reader.getText();
                            extractedData.put(currentElement, value);
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (reader.getLocalName().equals("Incident")) {
                            incidents.add(new HashMap<>(extractedData)); // Add a copy of extracted data
                            extractedData.clear(); // Clear data for the next incident
                        }
                        break;
                    default:
                        break;
                }
            }

            YourPojo yourPojo = new YourPojo();
            yourPojo.setExtractedDataList(incidents);

            return yourPojo;
        } catch (XMLStreamException e) {
            throw new XMLParsingException("Error parsing car data XML", e);
        }
    }
}

/*
    In this optimized version:

We use the streaming API (XMLStreamReader) to process XML events sequentially without loading the entire document into memory.
We minimize object creation by reusing maps (extractedData) for storing extracted data and incidents.
We avoid redundant method calls and string concatenation inside the loop.
Exception handling is optimized to catch specific exceptions (XMLStreamException) for better error handling.
These optimizations should improve the performance and readability of the XML parsing logic. */
