# TextParser-Service

TextParser-Service is a [jetty-servlet](http://jetty.codehaus.org/jetty/) making (some) functionalities of the [Stanford-Parser](http://nlp.stanford.edu/downloads/lex-parser.shtml) available through a restful interface. 
Data is returned as JSON. Supported are only "typedDependenciesCCprocessed" in English at the moment. 

## Web-Service Start
$ export PORT=5000

$ java -cp target/classes:"target/dependency/*" uk.co.jennius.textparser.Main

## Usage 
### In a browser
enter http::/yourdomain.com/typed_dependencies.json/?text=This is a text parser

see following [example](http://textparser-service.herokuapp.com/typed_dependencies.json/?text=This is a text parser)

### In code
If you happen to use a rest-enabled library (like ActiveResource in Rails) querying the service is straightforward (for example in Rails: `TypedDependency.find(:all, :params => { :text => text })`). 
Otherwise you have to implement the `GET /typed_dependencies.json/?text=This is a text parser` and the following JSON-deserialization by other means.

License
-------
[jetty-servlet](http://jetty.codehaus.org/jetty/): Dual [licensed](http://www.eclipse.org/jetty/licenses.php) under Apache and Eclipse
[servlet-api]: See license [here](/licenses/servlet-api.html)
[gson]:
[junit]:
[stanford-parser]: