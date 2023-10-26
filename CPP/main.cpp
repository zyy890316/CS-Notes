#include <iostream>
#include <utility>
#include <vector>
#include <string>
#include <fstream>

using namespace std;

class Student{
    string m_first = "First";
    string m_last = "Last";
    int m_id = 0;
    float m_avg = 0.0;

public:
    Student() {};

    Student(string first, string last, int id, float avg)
    : m_first(std::move(first))
    , m_last(std::move(last))
    , m_id(id)
    , m_avg (avg)
    {
    }

    string getFirstName() const {
        return m_first;
    }

    string getLastName() const {
        return m_last;
    }

    int getID() const {
        return m_id;
    }

    float getAverage() const {
        return m_avg;
    }

    void printInfo() const {
        std::cout << "First Name: " << m_first << std::endl;
        std::cout << "Last Name: " << m_last << std::endl;
        std::cout << "ID: " << m_id << std::endl;
        std::cout << "Average: " << m_avg << std::endl;
        std::cout << std::endl;
    }

};

class Course{
    string m_name = "Course";
    vector<Student> m_students;

public:
    Course() {}

    Course(const string& name)
        : m_name(name){
    }

    void addStudent(const Student& s){
        m_students.push_back(s);
    }

    const vector<Student>& getStudents() const{
        return m_students;
    }

    void loadFromFile(const string& fileName){
        ifstream fin(fileName);
        string temp;

        string first, last;
        int id;
        float avg;

        cout << "loadFromFile running" << endl;
        while(fin >> first){
            fin >> last >> id >> avg;
            addStudent(Student(first, last, id, avg));
        }
    }

    void printStudents() const{
        for(const Student& s : m_students){
            s.printInfo();
        }
    }
};

int main(int argc, char * argv[]) {
    vector<string> msg;
    msg.reserve(5); // Preallocate memory for 5 elements

    msg = {"Hello", "C++", "World", "from", "Clion!"};

    vector<int> vec;
    vec.push_back(42);
    vec.push_back(10);


    std::vector<Student> students;
    Course c("COMP 4300");
    c.loadFromFile("students.txt");
    c.printStudents();

    for(auto& vecVal : vec){
        cout << vecVal << endl;
    }

    for (const string& word : msg) {
        cout << word << " ";
    }
    cout << endl;

    return 0;
}
