# TextParser-Service

TextParser-Service is a [jetty-servlet](http://jetty.codehaus.org/jetty/) making (some) functionalities of the [Stanford-Parser](http://nlp.stanford.edu/downloads/lex-parser.shtml) available through a restful interface. 
Data is returned as JSON. Supported are only "typedDependenciesCCprocessed" in English at the moment. 

## Web-Service Start
$ export PORT=5000

$ java -cp target/classes:"target/dependency/*" uk.co.jennius.textparser.Main

## Usage 
### In a browser
enter `http::/yourdomain.com/typed_dependencies.json/?text=This is a text parser`

see following [example](http://textparser-service.herokuapp.com/typed_dependencies.json/?text=This is a text parser)

### In code
If you happen to use a rest-enabled library (like ActiveResource in Rails) querying the service is straightforward (for example in Rails: `TypedDependency.find(:all, :params => { :text => text })`). 
Otherwise you have to implement the `GET /typed_dependencies.json/?text=This is a text parser` and the following JSON-deserialization by other means.

## License
This work is licensed under [GNU General Public License (v3 or later)](http://www.gnu.org/licenses/gpl-3.0.html)


## 3-party licenses

[jetty-servlet](http://jetty.codehaus.org/jetty/): Dual [licensed](http://www.eclipse.org/jetty/licenses.php) under Apache and Eclipse

servlet-api: See license [here](/Enceradeira/textparser_service/licenses/servlet-api.html)

[gson](http://code.google.com/p/google-gson/): Licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)

[junit](http://www.junit.org/): Licensed under the [Common Public License - v 1.0](http://www.junit.org/license)

[stanford-parser](http://nlp.stanford.edu/downloads/lex-parser.shtml): Licensed under the [GNU General Public License (v2 or later)](http://www.gnu.org/licenses/gpl-2.0.html)