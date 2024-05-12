The ServiceFactory interface defines methods for creating instances of various services. DefaultServiceFactory implements this interface and creates instances of XMLParser, RuleServiceClient, and DataTransformationClient.
The XMLParsingStrategy interface defines a strategy for parsing XML data, and DefaultXMLParsingStrategy provides a default implementation of this strategy.
The SolaceMessageListener class uses the factory pattern to create instances of services and the strategy pattern to parse XML data.
The SolaceConfig class is refactored to follow the singleton pattern, ensuring that only one instance of the SolaceConfig class is created.

+-----------------------+        +-----------------------+        +-----------------------+
|   ServiceFactory      |        |  XMLParsingStrategy   |        |     SolaceConfig      |
+-----------------------+        +-----------------------+        +-----------------------+
| + createXMLParser()   |        | + parseXML(String)     |        |   + getInstance()     |
| + createRuleService..|        +-----------------------+        +-----------------------+
| + createDataTransfo..|                 ^
+-----------------------+                 |
         ^                               |
         |                               |
         | implements                    | contains
         |                               |
+-----------------------+        +-----------------------+ 
| DefaultServiceFactory|        | DefaultXMLParsingStra | 
+-----------------------+        +-----------------------+ 
| - restTemplate       |        |                       |
| - ruleServiceUrl     |        |                       |
| - dataTransform..Url |        |                       |
| - logger             |        +-----------------------+
+-----------------------+                 ^
         ^                               |
         |                               |
         |                               |
+-----------------------+                 |
|  SolaceMessageListener|                 |
+-----------------------+                 |
| - serviceFactory      |                 |
| - xmlParsingStrategy  |                 |
+-----------------------+-----------------+
         ^
         |
         |
+-----------------------+
|      YourPojo         |
+-----------------------+
