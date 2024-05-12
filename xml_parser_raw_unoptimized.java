@Component
public class CarDataXMLParsingStrategy implements XMLParsingStrategy {

    @Override
    public YourPojo parseXML(String xmlData) throws XMLParsingException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlData)));

            Element rootElement = document.getDocumentElement();
            NodeList childNodes = rootElement.getChildNodes();

            // Create a list to store extracted data for array-like elements
            List<Map<String, String>> extractedDataList = new ArrayList<>();

            // Iterate through child nodes of the parent element
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // If the node represents an array-like element, extract its data
                    if (element.getNodeName().equals("Incidents")) {
                        NodeList incidentNodes = element.getElementsByTagName("Incident");

                        // Iterate through child nodes of the array-like element
                        for (int j = 0; j < incidentNodes.getLength(); j++) {
                            Node incidentNode = incidentNodes.item(j);
                            if (incidentNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element incidentElement = (Element) incidentNode;
                                Map<String, String> incidentData = new HashMap<>();

                                // Extract data for each incident
                                incidentData.put("IncidentNumber", incidentElement.getElementsByTagName("IncidentNumber").item(0).getTextContent());
                                incidentData.put("Subject", incidentElement.getElementsByTagName("Subject").item(0).getTextContent());
                                // Extract other fields as needed for each incident

                                // Add extracted incident data to the list
                                extractedDataList.add(incidentData);
                            }
                        }
                    } else {
                        // If not an array-like element, extract data as before
                        String tagName = element.getNodeName();
                        String tagValue = element.getTextContent();
                        // Store or process extracted data accordingly
                    }
                }
            }

            // Create YourPojo object and set extracted data
            YourPojo yourPojo = new YourPojo();
            yourPojo.setExtractedDataList(extractedDataList);

            return yourPojo;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XMLParsingException("Error parsing car data XML", e);
        }
    }
}
/*n this modified version:

We iterate through the child nodes of the parent element (rootElement).
If a child node represents an array-like element (e.g., <Incidents>), we further iterate through its child nodes to extract data for each item in the array.
For each item in the array, we extract the data and store it in a map (incidentData), which is then added to a list (extractedDataList).
The list of extracted data is then set into the YourPojo object.
This approach allows you to handle an array of XML tags inside a parent node effectively.*/
