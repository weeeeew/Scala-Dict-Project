import scala.io.Source
import scala.io.StdIn



object dict {
    
    def main(args: Array[String]) : Unit = {

//////////////////////  Load Dictionary  //////////////////////////////////////////////////////////

        println("Hello, world\n\n\nLoading (unorganized) dictionary...")


        // Load our words and definitions from dictionary.json as a list of items
        val startLoad = System.nanoTime     // Begin timer to find time needed to load the data



        val dict = Source.fromFile("./dictionary.json").getLines().toList

        


        // Place each word (at the beginning of each line) into a list of words, this saves us a lot of time later, but makes the code a bit more awkward
        val allwords = readJSON(dict, List())




        // End the timer and output in terms of seconds
        val loadTime = (System.nanoTime - startLoad)
        val humanLoad: Float  = loadTime.toFloat / 1000000000

        println(s"System time to load dictionary.json: $humanLoad seconds\n")


//////////////////////  Use Dictionary  ///////////////////////////////////////////////////////////


        // Print a few test variables and the time used to print them
        println("testing if \"anopheles\" is in our dictionary (beginning)")
        testWord("anopheles", allwords)

        println("testing if \"enslave\" is in our dictionary (middle)")
        testWord("enslave", allwords)

        println("testing if \"silkness\" is in our dictionary (end)")
        testWord("silkness", allwords)

        println("testing ig \"zzzahhhh\" is in our dictionary (not)")
        testWord("zzzahhhh", allwords)




        // User input loops (really odd implementation since it's functional)
        checkLoop(allwords)
        
        defineLoop(allwords, dict)




        // Test all words from a .txt, line delimited
        /*val query = Source.fromFile("./SmallData.txt").getLines().toList
        for (term <- query){
            println(term)
            testWord(term, allwords)
        }*/

    }





//////////////////////  Primary Methods  //////////////////////////////////////////////////////////

//////////////////////  Load Dictionary  //////////////////////////////////////////////////////////

    def readJSON(dictionary: List[String], wordList: List[String]) : List[String] = {
        val jsonString: String = dictionary.head

        if (jsonString == "}" ){        // done if at closing bracket
            return wordList
        }


        val tail = dictionary.tail
        //println(dictionary)

        if (jsonString == "{") {        // Ignore the first, opening bracket
            return readJSON(tail, wordList)
        }


        else{
            val wordStart = jsonString.indexOf('\"') + 1  
            val wordEnd = (jsonString.substring(wordStart).indexOf('\"') + wordStart)

            val newWord = jsonString.substring(wordStart, wordEnd)
               
            val wordListPlus = newWord :: wordList
                
            return readJSON(tail, wordListPlus)
        }
    }


///////////////////////////////////////////////////////////////////////////////////////////////////




//////////////////////  High-order Methods

    def testWord(word: String, wordList: List[String]) : Unit = {
        val startAnswer = System.nanoTime
        val bool = hasWord(word, wordList)
        val answerTime = System.nanoTime - startAnswer
        val humanAnswer: Float = answerTime.toFloat / 1000000000
        println(bool)

        println(s"That took $humanAnswer seconds\n\n")
    }

    def defineWord(word: String, validWords: List[String],  dictionary: List[String]): String = {
        if (hasWord(word, validWords)){
            return getDefinition(dictionary, findDictIndex(word, validWords, dictionary))
        }
        else {
            return s"Word \"$word\" is not defined"
        }
    }





///////////////////     Sub-Methods

    def hasWord(searchFor: String, fromList: List[String]) : Boolean = {



        if (fromList.head == searchFor){
            return true
        }
        if (fromList.tail == List()){
            return false
        }
        else {
            return hasWord(searchFor, fromList.tail)
        }


    }

    
    
    def getDefinition(dictionary: List[String], index: Int): String = {
        val line: String = dictionary(index + 1)
        val wordStart = line.indexOf(':') + 1
        return (line.substring(wordStart + 2, line.length()-1).replace("\\n", "\n"))

    }




    def findDictIndex(word:String, validWords: List[String], dictionary: List[String]): Int = {
        return dictionary.size - validWords.indexOf(word) - 3
    }




//////////////////////  User Input Loops  /////////////////////////////////////////////////////////

    def checkLoop(wordList: List[String]): Unit ={
        while (true) {
            println("What word would you like to check? Enter \':q\' to quit")
            val word: String = StdIn.readLine().toLowerCase()
            if (word == ":q"){
                return 
            }
            testWord(word, wordList)


        }



    }




    def defineLoop(wordList: List[String], dictionary: List[String]): Unit ={
        while (true) {
            println("What word would you like to define? Enter \':q\' to quit")
            val word: String = StdIn.readLine().toLowerCase()
            if (word == ":q"){
                return 
            }
            val startTime = System.nanoTime
            println(defineWord(word, wordList, dictionary))
            val humanTime = (System.nanoTime - startTime).toFloat / 1000000000
            println(s"\nThat took $humanTime seconds\n\n")
        }
    }



///////////////////////////////////////////////////////////////////////////////////////////////////
 

}






