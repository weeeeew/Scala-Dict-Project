#include <iostream>
#include <fstream>
#include <String>

#include <chrono>
using namespace std::chrono;
using std::ifstream;
using std::cout;
using std::string;

int size = 0;


bool testWord(string word, string* words) {
    int i = 0;
    while (i < size - 2) {
        if (words[i] == word) {
            return true;
        }
        i++;
    }
    return false;
}


int main() {
    std::cout << "Hello World!\n";

    ifstream infile("./dictionary.json");

    auto start = high_resolution_clock::now();

    string line = "";

    cout << "Loading Dictionary...\n";
    while (std::getline(infile, line)) {
        ++size;


    }

    infile.clear();
    infile.seekg(0);

    string* dict = new string[size];

    int index = 0;

    while (!infile.eof()) {

        string* newWord = new string;
        //cout << "Line " << index << "\n";
        std::getline(infile, *newWord);

        dict[index++] = *newWord;



    }




    

    string* allWords = new string[size-2];  // ignoring the first and last lines ('{' and '}')

    for (int i = 0; i < size; i++) {
        if (dict[i] == "}") {
            break;
        }
        if (dict[i] != "{") {
            string line = dict[i];
            int wordStart = line.find("\"");
            int wordEnd = line.find("\"", wordStart+1);
            string newWord = line.substr(wordStart + 1, wordEnd - wordStart -1);
            allWords[i - 1] = newWord;


        }
    }

    auto end = high_resolution_clock::now();


    cout << "Time to load dictionary: " << ((float)duration_cast<nanoseconds>(end - start).count()) / 1000000000 << " seconds\n\n\n";

    cout << "Testing for \"silkness\" (end)\n";
    start = high_resolution_clock::now();
    cout << testWord("silkness", allWords) << "\n";
    end = high_resolution_clock::now();
    cout << "Time to find: " << ((float)duration_cast<nanoseconds>(end - start).count()) / 1000000000 << " seconds\n\n";
    

    cout << "Testing for \"anopholes\" (beginning)\n";
    start = high_resolution_clock::now();
    cout << testWord("anopheles", allWords) << "\n";
    end = high_resolution_clock::now();
    cout << "Time to find: " << ((float)duration_cast<nanoseconds>(end - start).count()) / 1000000000 << " seconds\n";





    return 0;
}