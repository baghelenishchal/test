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
