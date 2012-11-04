# TextParser-Service

TextParser-Service is a [jetty-servlet](http://jetty.codehaus.org/jetty/) making (some) functionalities of the [Stanford-Parser](http://nlp.stanford.edu/downloads/lex-parser.shtml) available through a restful interface. 
Data is returned as JSON. Supported are only "typedDependenciesCCprocessed" in English at the moment. 

## Web-Service Start
$ export PORT=5000

$ java -cp target/classes:"target/dependency/*" uk.co.jennius.textparser.Main

## Usage 
### In a browser
Enter `http::/yourdomain.com/typed_dependencies.json/?text=This is a text parser`

See [example](http://textparser-service.herokuapp.com/typed_dependencies.json/?text=This is a text parser)

### Service result
The returned elements are sorted in order of their appearance in the text.
The elements are either of type "Word" or "Filling". A "Word" is a text fragment identified by the Stanford Parser (only meaningful words). A "Word" contains dependencies to one or more other "Word"s (called governor). "Fillings" are the spaces between those text fragments. Words and Fillings allow a complete reconstruction of the original text by concatenating the elements.   

A "Word" is described by

 + text: The value at this position of the sentence (mostly a word)
 + index: The position in the sentence
 + dependencies: The depependency (relation) to a governor-word

A "Governor" is describe by

 + gov: The 'governor' on which this word depends on 
 + govIndex: The index of the 'governor' on which this value depends on  
 + relation: The type of relation between this word and the governor

A "Filling" is described by:   
 
 + text: The value at this position of the sentence (mostly space, comma, exclamation mark etc..)
 + index: The position in the sentence

### In code
If you happen to use a rest-enabled library (like ActiveResource in Rails) querying the service is straightforward (for example in Rails: `TypedDependency.find(:all, :params => { :text => text })`). 
Otherwise you have to implement the `GET /typed_dependencies.json/?text=This is a text parser` and the following JSON-deserialization by other means.

## License
This work is licensed under [GNU General Public License (v3 or later)](http://www.gnu.org/licenses/gpl-3.0.html)


## 3-party licenses

[jetty-servlet](http://jetty.codehaus.org/jetty/): Dual [licensed](http://www.eclipse.org/jetty/licenses.php) under Apache and Eclipse

[gson](http://code.google.com/p/google-gson/): Licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)

[junit](http://www.junit.org/): Licensed under the [Common Public License - v 1.0](http://www.junit.org/license)

[stanford-parser](http://nlp.stanford.edu/downloads/lex-parser.shtml): Licensed under the [GNU General Public License (v2 or later)](http://www.gnu.org/licenses/gpl-2.0.html)

[commons-lang3](http://commons.apache.org/lang/): Licensed under the [Apache License 2.0](http://commons.apache.org/lang/license.html)
