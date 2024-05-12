// Factory Method for creating instances
public interface ServiceFactory {
    XMLParser createXMLParser();
    RuleServiceClient createRuleServiceClient();
    DataTransformationClient createDataTransformationClient();
}

@Component
public class DefaultServiceFactory implements ServiceFactory {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${rule.management.service.url}")
    private String ruleServiceUrl;

    @Value("${data.transformation.service.url}")
    private String dataTransformationServiceUrl;

    @Autowired
    private Logger logger;

    @Override
    public XMLParser createXMLParser() {
        return new XMLParser();
    }

    @Override
    public RuleServiceClient createRuleServiceClient() {
        return new RuleServiceClient(restTemplate, ruleServiceUrl, logger);
    }

    @Override
    public DataTransformationClient createDataTransformationClient() {
        return new DataTransformationClient(restTemplate, dataTransformationServiceUrl, logger);
    }
}

// Strategy Pattern for parsing XML data
public interface XMLParsingStrategy {
    YourPojo parseXML(String xmlData) throws XMLParsingException;
}

@Component
public class DefaultXMLParsingStrategy implements XMLParsingStrategy {

    @Override
    public YourPojo parseXML(String xmlData) throws XMLParsingException {
        // Parsing logic here
        return null;
    }
}

// Solace Message Listener using Factory and Strategy patterns
@Component
public class SolaceMessageListener {

    private ServiceFactory serviceFactory;
    private XMLParsingStrategy xmlParsingStrategy;

    public SolaceMessageListener(ServiceFactory serviceFactory, XMLParsingStrategy xmlParsingStrategy) {
        this.serviceFactory = serviceFactory;
        this.xmlParsingStrategy = xmlParsingStrategy;
    }

    @JmsListener(destination = "${solace.queue1}")
    public void receiveMessageFromQueue1(String message) {
        try {
            XMLParser xmlParser = serviceFactory.createXMLParser();
            YourPojo data = xmlParsingStrategy.parseXML(message);
            RuleServiceClient ruleServiceClient = serviceFactory.createRuleServiceClient();
            DataTransformationClient dataTransformationClient = serviceFactory.createDataTransformationClient();

            if (data != null && ruleServiceClient.applyRules(data)) {
                dataTransformationClient.sendData(data);
            }
        } catch (XMLParsingException e) {
            logger.error("Error parsing XML: " + e.getMessage(), e);
        }
    }
}

// Singleton Pattern for Solace Configuration
@Configuration
public class SolaceConfig {

    private static final SolaceConfig INSTANCE = new SolaceConfig();

    private SolaceConfig() {
        // Private constructor to prevent instantiation
    }

    public static SolaceConfig getInstance() {
        return INSTANCE;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        // Configuration logic here
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }
}
