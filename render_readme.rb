require 'github/markup'
html = GitHub::Markup.render('README.md', File.read("README.md"))
File.open("README.html", 'w') {|f| f.write(html) }
