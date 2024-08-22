import java.io.File
import java.nio.file.FileVisitResult


//Groovy Operators
@groovy.transform.ToString
class Job {
    String roleName
    int salary
}

@groovy.transform.ToString
class Person {
    String name
    Job job
}

@groovy.transform.ToString
class Department {
    String depName = 'Platform Engineer'
    Set<Person> staff = [
            new Person(name: 'Matt', job: new Job(roleName: 'Developer', salary: 1000)),
            new Person(name: 'Ling', job: new Job(roleName: 'ML', salary: 1000)),
            new Person(name: 'Tim')
    ]
}

println new Department()
def dept = new Department()
println "Salary: ${dept.staff[2].job?.salary}"

def loggedInUser = 'Ling'
//def loggedInUser = ' '
def displayUsername = loggedInUser ?: 'Guest'
println "LoggedInUser: $displayUsername"

def l = ['black', 'white', 'red', 'green', 'blue']
l.sort { a, b ->  a <=> b}
println "l: $l"

@groovy.transform.ToString
class FoodOrder {
    String name
    BigDecimal cost

    FoodOrder(name, cost) {
        this.name = name
        this.cost = cost
    }
}
def driveThru = [
        new FoodOrder('Burger', 5.99),
        new FoodOrder('Fries', 2.99),
        new FoodOrder('Soda', 1.99)
]
driveThru.sort { a, b -> b.cost <=> a.cost }
println "driveThru: $driveThru"


def fruits = ['apple', 'banana', 'cherry']
def shoppingList = ['milk', 'cereral', *fruits]
println "shoppingList: $shoppingList"

static void capitaliseThreeString(String a, String b, String c) {
    println a.toUpperCase() + a
    println b.toUpperCase() + b
    println c.toUpperCase() + c
}
capitaliseThreeString(*fruits)


static void caplist(String... args) {
    args.each { println it.toUpperCase() + it }
}
capitaliseThreeString(*fruits)
println fruits*.toUpperCase()


(1..<5)[1..3].each { println it }
enum Weekdays {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY
}
(Weekdays.TUESDAY..Weekdays.FRIDAY).each { println it }
(1.23..5.67).each { println it }


def s ='How are you?'
println s[0..2]


//https://groovy-lang.org/groovy-dev-kit.html

//1.1. Reading files
def baseDir = System.getProperty("user.dir")

new File(baseDir, 'haiku.txt').eachLine { line, nb ->
    if (nb <= 10) {
        println "Line $nb: $line"
    }
}

def count = 0, MAXSIZE = 3
new File(baseDir,"haiku.txt").withReader { reader ->
    while (line = reader.readLine()) {
        println "Count $count: $line"
        if (++count > MAXSIZE) {
            throw new RuntimeException('Haiku should only have 3 verses')
        }
    }
}

def list = new File(baseDir, 'haiku.txt').collect { it }
if (!list.isEmpty()) {
    println list[0..Math.min(9, list.size() - 1)]
}

//1.2. Writing files
new File(baseDir,'tmp.txt').withWriter('utf-8') { writer ->
    writer.writeLine 'Into the ancient pond'
    writer.writeLine 'A frog jumps'
    writer.writeLine 'Water’s sound!'
}

new File(baseDir,'tmp.txt') << '''Into the ancient pond
A frog jumps
Water’s sound!'''


//1.3. Traversing file trees
def dir = new File(System.getProperty("user.dir"))
dir.eachFileRecurse { file ->
    println file.name
}
dir.eachFileMatch(~/.*\.txt/) { file ->
    println file.name
}

dir.traverse { file ->
    if (file.directory && file.name == 'bin') {
        FileVisitResult.TERMINATE
    } else {
        println file.name
        FileVisitResult.CONTINUE
    }
}

//1.4 Data and objects
def file = new File('data.bin')
boolean b = true
String message = 'Hello from Groovy'
// Serialize data into a file
file.withDataOutputStream { out ->
    out.writeBoolean(b)
    out.writeUTF(message)
}
// Then read it back
file.withDataInputStream { input ->
    assert input.readBoolean() == b
    assert input.readUTF() == message
}

import java.io.Serializable
def file2 = new File('data.bin')
class Person2 implements Serializable {
    String name
    int age
}

Person2 pp = new Person2(name:'Bob', age:76)
// Serialize data into a file
file.withObjectOutputStream { out ->
    out.writeObject(pp)
}
// ...
// Then read it back
//file.withObjectInputStream { input ->
//    def pp2 = input.readObject()
//    assert pp2.name == pp.name
//    assert pp2.age == pp.age
//}


//1.5. Executing External Processes
//def process = "ls -l".execute()
//println "Found text ${process.text}"

def process = "ls -l".execute()
process.in.eachLine { line ->
    println line
}

//proc1 = 'ls'.execute()
//proc2 = 'tr -d o'.execute()
//proc3 = 'tr -d e'.execute()
//proc4 = 'tr -d i'.execute()
//proc1 | proc2 | proc3 | proc4
//proc4.waitFor()
//if (proc4.exitValue()) {
//    println proc4.err.text
//} else {
//    println proc4.text
//}

def sout = new StringBuilder()
def serr = new StringBuilder()
proc2 = 'tr -d o'.execute()
proc3 = 'tr -d e'.execute()
proc4 = 'tr -d i'.execute()
proc4.consumeProcessOutput(sout, serr)
proc2 | proc3 | proc4
[proc2, proc3].each { it.consumeProcessErrorStream(serr) }
proc2.withWriter { writer ->
    writer << 'testfile.groovy'
}
proc4.waitForOrKill(1000)
println "Standard output: $sout"
println "Standard error: $serr"


//2.1. Lists
//2.1.1. List literals
def list2 = [5, 6, 7, 8]
assert list2.get(2) == 7
assert list2[2] == 7
assert list2 instanceof java.util.List

def emptyList = []
assert emptyList.size() == 0
emptyList.add(5)
assert emptyList.size() == 1
println emptyList

def list1 = ['a', 'b', 'c']
//construct a new list, seeded with the same items as in list1
def llist2 = new ArrayList<String>(list1)

assert llist2 == list1 // == checks that each corresponding element is the same

// clone() can also be called
def list3 = list1.clone()
assert list3 == list1

def llist = [5, 6, 7, 8]
assert llist.size() == 4
assert llist.getClass() == ArrayList     // the specific kind of list being used

assert llist[2] == 7                     // indexing starts at 0
assert llist.getAt(2) == 7               // equivalent method to subscript operator []
assert llist.get(2) == 7                 // alternative method

llist[2] = 9
assert llist == [5, 6, 9, 8,]           // trailing comma OK

llist.putAt(2, 10)                       // equivalent method to [] when value being changed
assert llist == [5, 6, 10, 8]
assert llist.set(2, 11) == 10            // alternative method that returns old value
assert llist == [5, 6, 11, 8]

assert ['a', 1, 'a', 'a', 2.5, 2.5f, 2.5d, 'hello', 7g, null, 9 as byte]
//objects can be of different types; duplicates allowed

assert [1, 2, 3, 4, 5][-1] == 5             // use negative indices to count from the end
assert [1, 2, 3, 4, 5][-2] == 4
assert [1, 2, 3, 4, 5].getAt(-2) == 4       // getAt() available with negative index...
try {
    [1, 2, 3, 4, 5].get(-2)                 // but negative index not allowed with get()
    assert false
} catch (e) {
    assert e instanceof IndexOutOfBoundsException
}

//2.1.2. List as a boolean expression
assert ![]             // an empty list evaluates as false

//all other lists, irrespective of contents, evaluate as true
assert [1] && ['a'] && [0] && [0.0] && [false] && [null]

//2.1.3. Iterating on a list
[1, 2, 3].each {
    println "Item: $it" // `it` is an implicit parameter corresponding to the current element
}
['a', 'b', 'c'].eachWithIndex { it, i -> // `it` is the current element, while `i` is the index
    println "Item $i: $it"
}

assert [1, 2, 3].collect { it * 2 } == [2, 4, 6]

// shortcut syntax instead of collect
assert [1, 2, 3]*.multiply(2) == [1, 2, 3].collect { it.multiply(2) }

//2.1.4. Manipulating lists
def list4 = [0]
// it is possible to give `collect` the list which collects the elements
assert [1, 2, 3].collect(list4) { it * 2 } == [0, 2, 4, 6]
assert list4 == [0, 2, 4, 6]

assert [1, 2, 3].find { it > 1 } == 2           // find 1st element matching criteria
assert [1, 2, 3].findAll { it > 1 } == [2, 3]   // find all elements matching critieria
assert ['a', 'b', 'c', 'd', 'e'].findIndexOf {      // find index of 1st element matching criteria
    it in ['c', 'e', 'g']
} == 2

assert ['a', 'b', 'c', 'd', 'c'].indexOf('c') == 2  // index returned
assert ['a', 'b', 'c', 'd', 'c'].indexOf('z') == -1 // index -1 means value not in list
assert ['a', 'b', 'c', 'd', 'c'].lastIndexOf('c') == 4

assert [1, 2, 3].every { it < 5 }               // returns true if all elements match the predicate
assert ![1, 2, 3].every { it < 3 }
assert [1, 2, 3].any { it > 2 }                 // returns true if any element matches the predicate
assert ![1, 2, 3].any { it > 3 }

assert [1, 2, 3, 4, 5, 6].sum() == 21                // sum anything with a plus() method
assert ['a', 'b', 'c', 'd', 'e'].sum {
    it == 'a' ? 1 : it == 'b' ? 2 : it == 'c' ? 3 : it == 'd' ? 4 : it == 'e' ? 5 : 0
    // custom value to use in sum
} == 15
assert ['a', 'b', 'c', 'd', 'e'].sum { ((char) it) - ((char) 'a') } == 10
assert ['a', 'b', 'c', 'd', 'e'].sum() == 'abcde'
assert [['a', 'b'], ['c', 'd']].sum() == ['a', 'b', 'c', 'd']

// an initial value can be provided
assert [].sum(1000) == 1000
assert [1, 2, 3].sum(1000) == 1006

assert [1, 2, 3].join('-') == '1-2-3'           // String joining
assert [1, 2, 3].inject('counting: ') {
    str, item -> str + item                     // reduce operation
} == 'counting: 123'
assert [1, 2, 3].inject(0) { c, i ->
    c + i
} == 6


def list5 = [9, 4, 2, 10, 5]
assert list5.max() == 10
assert list5.min() == 2

// we can also compare single characters, as anything comparable
assert ['x', 'y', 'a', 'z'].min() == 'a'

// we can use a closure to specify the sorting behaviour
def list6 = ['abc', 'z', 'xyzuvw', 'Hello', '321']
assert list6.max { it.size() } == 'xyzuvw'
assert list6.min { it.size() } == 'z'

Comparator mc = { aa, bb -> aa == bb ? 0 : (aa < bb ? -1 : 1) }
def list7 = [7, 4, 9, -6, -1, 11, 2, 3, -9, 5, -13]
assert list7.max(mc) == 11
assert list7.min(mc) == -13

Comparator mc2 = { aa, bb -> aa == bb ? 0 : (Math.abs(aa) < Math.abs(bb)) ? -1 : 1 }
assert list7.max(mc2) == -13
assert list7.min(mc2) == -1
assert list7.max { aa, bb -> aa.equals(bb) ? 0 : Math.abs(aa) < Math.abs(bb) ? -1 : 1 } == -13
assert list7.min { aa, bb -> aa.equals(bb) ? 0 : Math.abs(aa) < Math.abs(bb) ? -1 : 1 } == -1


def list8 = []
assert list8.empty

list8 << 5
assert list8.size() == 1

list8 << 7 << 'i' << 11
assert list8 == [5, 7, 'i', 11]

list8 << ['m', 'o']
assert list8 == [5, 7, 'i', 11, ['m', 'o']]

//first item in chain of << is target list
assert ([1, 2] << 3 << [4, 5] << 6) == [1, 2, 3, [4, 5], 6]

//using leftShift is equivalent to using <<
assert ([1, 2, 3] << 4) == ([1, 2, 3].leftShift(4))

assert [1, 2] + 3 + [4, 5] + 6 == [1, 2, 3, 4, 5, 6]
// equivalent to calling the `plus` method
assert [1, 2].plus(3).plus([4, 5]).plus(6) == [1, 2, 3, 4, 5, 6]

def a = [1, 2, 3]
a += 4      // creates a new list and assigns it to `a`
a += [5, 6]
assert a == [1, 2, 3, 4, 5, 6]

assert [1, *[222, 333], 456] == [1, 222, 333, 456]
assert [*[1, 2, 3]] == [1, 2, 3]
assert [1, [2, 3, [4, 5], 6], 7, [8, 9]].flatten() == [1, 2, 3, 4, 5, 6, 7, 8, 9]

def list9 = [1, 2]
list9.add(3)
list9.addAll([5, 4])
assert list9 == [1, 2, 3, 5, 4]

list9 = [1, 2]
list9.add(1, 3) // add 3 just before index 1
assert list9 == [1, 3, 2]

list9.addAll(2, [5, 4]) //add [5,4] just before index 2
assert list9 == [1, 3, 5, 4, 2]

list9 = ['a', 'b', 'z', 'e', 'u', 'v', 'g']
list9[8] = 'x' // the [] operator is growing the list as needed
// nulls inserted if required
assert list9 == ['a', 'b', 'z', 'e', 'u', 'v', 'g', null, 'x']


assert ['a','b','c','b','b'] - 'c' == ['a','b','b','b']
assert ['a','b','c','b','b'] - 'b' == ['a','c']
assert ['a','b','c','b','b'] - ['b','c'] == ['a']

def list10 = [1,2,3,4,3,2,1]
list10 -= 3           // creates a new list by removing `3` from the original one
assert list10 == [1,2,4,2,1]
assert ( list10 -= [2,4] ) == [1,1]

def list11 = ['a','b','c','d','e','f','b','b','a']
assert list11.remove(2) == 'c'        // remove the third element, and return it
assert list11 == ['a','b','d','e','f','b','b','a']

def list12= ['a','b','c','b','b']
assert list12.remove('c')             // remove 'c', and return true because element removed
assert list12.remove('b')             // remove first 'b', and return true because element removed
assert ! list12.remove('z')           // return false because no elements removed
assert list12 == ['a','b','b']

def list13 = [1,2,3,4,5,6,2,2,1]
assert list13.remove(2) == 3          // this removes the element at index 2, and returns it
assert list13 == [1,2,4,5,6,2,2,1]
assert list13.removeElement(2)        // remove first 2 and return true
assert list13 == [1,4,5,6,2,2,1]
assert ! list13.removeElement(8)      // return false because 8 is not in the list
assert list13 == [1,4,5,6,2,2,1]
assert list13.removeAt(1) == 4        // remove element at index 1, and return it
assert list13 == [1,5,6,2,2,1]

def list14= ['a',2,'c',4]
list14.clear()
assert list14 == []

assert 'a' in ['a','b','c']             // returns true if an element belongs to the list
assert ['a','b','c'].contains('a')      // equivalent to the `contains` method in Java
assert [1,3,4].containsAll([1,4])       // `containsAll` will check that all elements are found

assert [1,2,3,3,3,3,4,5].count(3) == 4  // count the number of elements which have some value
assert [1,2,3,3,3,3,4,5].count {
    it % 2 == 0                             // count the number of elements which match the predicate
} == 2
assert [1,2,4,6,8,10,12].intersect([1,3,6,9,12]) == [1,6,12]
assert [1,2,3].disjoint( [4,6,9] )
assert ![1,2,3].disjoint( [2,4,6] )


assert [6, 3, 9, 2, 7, 1, 5].sort() == [1, 2, 3, 5, 6, 7, 9]

def list15 = ['abc', 'z', 'xyzuvw', 'Hello', '321']
assert list15.sort {
    it.size()
} == ['z', 'abc', '321', 'Hello', 'xyzuvw']

def list16 = [7, 4, -6, -1, 11, 2, 3, -9, 5, -13]
assert list16.sort { aa, bb -> aa == bb ? 0 : Math.abs(aa) < Math.abs(bb) ? -1 : 1 } ==
        [-1, 2, 3, 4, 5, -6, 7, -9, 11, -13]

Comparator mc3 = { aa, bb -> aa == bb ? 0 : Math.abs(aa) < Math.abs(bb) ? -1 : 1 }

// JDK 8+ only
// list2.sort(mc)
// assert list2 == [-1, 2, 3, 4, 5, -6, 7, -9, 11, -13]

def list17 = [6, -3, 9, 2, -7, 1, 5]

Collections.sort(list17)
assert list17 == [-7, -3, 1, 2, 5, 6, 9]

Collections.sort(list17, mc3)
assert list17 == [1, 2, -3, 5, 6, -7, 9]

assert [1, 2, 3] * 3 == [1, 2, 3, 1, 2, 3, 1, 2, 3]
assert [1, 2, 3].multiply(2) == [1, 2, 3, 1, 2, 3]
assert Collections.nCopies(3, 'b') == ['b', 'b', 'b']

// nCopies from the JDK has different semantics than multiply for lists
assert Collections.nCopies(2, [1, 2]) == [[1, 2], [1, 2]] //not [1,2,1,2]

//2.2. Maps
def map = [name: 'Gromit', likes: 'cheese', id: 1234]
assert map.get('name') == 'Gromit'
assert map.get('id') == 1234
assert map['name'] == 'Gromit'
assert map['id'] == 1234
assert map instanceof java.util.Map

def emptyMap = [:]
assert emptyMap.size() == 0
emptyMap.put("foo", 5)
assert emptyMap.size() == 1
assert emptyMap.get("foo") == 5

def aa = 'Bob'
def ages = [aa: 43]
assert ages['Bob'] == null // `Bob` is not found
assert ages['aa'] == 43     // because `a` is a literal!

ages = [(aa): 43]            // now we escape `a` by using parenthesis
assert ages['Bob'] == 43   // and the value is found!

def mapA = [
        simple : 123,
        complex: [a: 1, b: 2]
]
def map2 = mapA.clone()
assert map2.get('simple') == mapA.get('simple')
assert map2.get('complex') == mapA.get('complex')
map2.get('complex').put('c', 3)
assert mapA.get('complex').get('c') == 3

def mapB = [name: 'Gromit', likes: 'cheese', id: 1234]
assert mapB.name == 'Gromit'     // can be used instead of map.get('name')
assert mapB.id == 1234

def emptyMapB = [:]
assert emptyMapB.size() == 0
emptyMapB.foo = 5
assert emptyMapB.size() == 1
assert emptyMapB.foo == 5


def mapC = [name: 'Gromit', likes: 'cheese', id: 1234]
assert mapC.class == null
assert mapC.get('class') == null
assert mapC.getClass() == LinkedHashMap // this is probably what you want
mapC = [1      : 'a',
       (true) : 'p',
       (false): 'q',
       (null) : 'x',
       'null' : 'z']
assert mapC.containsKey(1) // 1 is not an identifier so used as is
assert mapC.true == null
assert mapC.false == null
assert mapC.get(true) == 'p'
assert mapC.get(false) == 'q'
assert mapC.null == 'z'
assert mapC.get(null) == 'x'

def mapD = [
        Bob  : 42,
        Alice: 54,
        Max  : 33
]

// `entry` is a map entry
mapD.each { entry ->
    println "Name: $entry.key Age: $entry.value"
}

// `entry` is a map entry, `i` the index in the map
mapD.eachWithIndex { entry, i ->
    println "$i - Name: $entry.key Age: $entry.value"
}

// Alternatively you can use key and value directly
mapD.each { key, value ->
    println "Name: $key Age: $value"
}

// Key, value and i as the index in the map
mapD.eachWithIndex { key, value, i ->
    println "$i - Name: $key Age: $value"
}


//2.2.4. Manipulating maps
//Adding or removing elements
def defaults = [1: 'a', 2: 'b', 3: 'c', 4: 'd']
def overrides = [2: 'z', 5: 'x', 13: 'x']

def result = new LinkedHashMap(defaults)
result.put(15, 't')
result[17] = 'u'
result.putAll(overrides)
assert result == [1: 'a', 2: 'z', 3: 'c', 4: 'd', 5: 'x', 13: 'x', 15: 't', 17: 'u']

def m = [1:'a', 2:'b']
assert m.get(1) == 'a'
m.clear()
assert m == [:]

def key = 'some key'
def mapE = [:]
def gstringKey = "${key.toUpperCase()}"
mapE.put(gstringKey,'value')
assert mapE.get('SOME KEY') == null

//Keys, values and entries
def mapF = [1:'a', 2:'b', 3:'c']
def entries = mapF.entrySet()
entries.each { entry ->
    assert entry.key in [1,2,3]
    assert entry.value in ['a','b','c']
}
def keys = mapF.keySet()
assert keys == [1,2,3] as Set

//Filtering and searching
def people = [
1: [name:'Bob', age: 32, gender: 'M'],
2: [name:'Johnny', age: 36, gender: 'M'],
3: [name:'Claire', age: 21, gender: 'F'],
4: [name:'Amy', age: 54, gender:'F']
]

def bob = people.find { it.value.name == 'Bob' } // find a single entry
def females = people.findAll { it.value.gender == 'F' }

// both return entries, but you can use collect to retrieve the ages for example
def ageOfBob = bob.value.age
def agesOfFemales = females.collect {
    it.value.age
}

assert ageOfBob == 32
assert agesOfFemales == [21,54]

// but you could also use a key/pair value as the parameters of the closures
def agesOfMales = people.findAll { id, person ->
    person.gender == 'M'
}.collect { id, person ->
    person.age
}
assert agesOfMales == [32, 36]

// `every` returns true if all entries match the predicate
assert people.every { id, person ->
    person.age > 18
}

// `any` returns true if any entry matches the predicate

assert people.any { id, person ->
    person.age == 54
}

//Grouping
assert ['a', 7, 'b', [2, 3]].groupBy {
    it.class
} == [(String)   : ['a', 'b'],
      (Integer)  : [7],
      (ArrayList): [[2, 3]]
]

assert [
        [name: 'Clark', city: 'London'], [name: 'Sharma', city: 'London'],
        [name: 'Maradona', city: 'LA'], [name: 'Zhang', city: 'HK'],
        [name: 'Ali', city: 'HK'], [name: 'Liu', city: 'HK'],
].groupBy { it.city } == [
        London: [[name: 'Clark', city: 'London'],
                 [name: 'Sharma', city: 'London']],
        LA    : [[name: 'Maradona', city: 'LA']],
        HK    : [[name: 'Zhang', city: 'HK'],
                 [name: 'Ali', city: 'HK'],
                 [name: 'Liu', city: 'HK']],
]

//2.3. Ranges
// an inclusive range
def range = 5..8
assert range.size() == 4
assert range.get(2) == 7
assert range[2] == 7
assert range instanceof java.util.List
assert range.contains(5)
assert range.contains(8)

// lets use a half-open range
range = 5..<8
assert range.size() == 3
assert range.get(2) == 7
assert range[2] == 7
assert range instanceof java.util.List
assert range.contains(5)
assert !range.contains(8)

//get the end points of the range without using indexes
range = 1..10
assert range.from == 1
assert range.to == 10


// an inclusive range
def rangeString = 'a'..'d'
assert rangeString.size() == 4
assert rangeString.get(2) == 'c'
assert rangeString[2] == 'c'
assert rangeString instanceof java.util.List
assert rangeString.contains('a')
assert rangeString.contains('d')
assert !rangeString.contains('e')


for (i in 1..10) {
    println "Hello ${i}"
}

(1..10).each { i ->
    println "Hello ${i}"
}


def years = 1..30
years.each { year ->
    switch (year) {
        case 1..10:
            interestRate = 0.076
            println interestRate
            break
        case 11..25:
            interestRate = 0.052
            println interestRate
            break
        default:
            interestRate = 0.037
            println interestRate
    }
}


//2.4. Syntax enhancements for collections

def listOfMaps = [['a': 11, 'b': 12], ['a': 21, 'b': 22]]
assert listOfMaps.a == [11, 21] //GPath notation
assert listOfMaps*.a == [11, 21] //spread dot notation

listOfMaps = [['a': 11, 'b': 12], ['a': 21, 'b': 22], null]
assert listOfMaps*.a == [11, 21, null] // caters for null values
assert listOfMaps*.a == listOfMaps.collect { it?.a } //equivalent notation
// But this will only collect non-null values
assert listOfMaps.a == [11,21]

assert [ 'z': 900, *: ['a': 100, 'b': 200], 'a': 300] == ['a': 300, 'b': 200, 'z': 900]
//spread map notation in map definition
assert [*: [3: 3, *: [5: 5]], 7: 7] == [3: 3, 5: 5, 7: 7]

def f = { [1: 'u', 2: 'v', 3: 'w'] }
assert [*: f(), 10: 'zz'] == [1: 'u', 2: 'v', 3: 'w', 10: 'zz']

// spread map notation in function arguments
f = { map_1 -> map_1.c }
assert f(*: ['a': 10, 'b': 20, 'c': 30], 'e': 50) == 30

//f = { m, i, j, k -> [m, i, j, k] }
////using spread map notation with mixed unnamed and named arguments
//assert f('e': 100, *[4, 5], *: ['a': 10, 'b': 20, 'c': 30], 6) ==
//        [["e": 100, "b": 20, "c": 30, "a": 10], 4, 5, 6]

assert [1, 3, 5] == ['a', 'few', 'words']*.size()
class Person_2 {
    String name
    int age
}
def persons = [new Person_2(name:'Hugo', age:17), new Person_2(name:'Sandra',age:19)]
assert [17, 19] == persons*.age


def text = 'nice cheese gromit!'
def x = text[2]
assert x == 'c'
assert x.class == String
def sub = text[5..10]
assert sub == 'cheese'
def listB = [10, 11, 12, 13]
def answer = listB[2,3]
assert answer == [12,13]

listC = 100..200
sub = listC[1, 3, 20..25, 33]
assert sub == [101, 103, 120, 121, 122, 123, 124, 125, 133]

listD = ['a','x','x','d']
listD[1..2] = ['b','c']
assert listD == ['a','b','c','d']

text = "nice cheese gromit!"
x = text[-1]
assert x == "!"

def name = text[-7..-2]
assert name == "gromit"

text = "nice cheese gromit!"
name = text[3..1]
assert name == "eci"

def evens = new int[]{2, 4, 6}           // alt syntax 1
assert evens instanceof int[]

def odds = [1, 3, 5] as int[]            // alt syntax 2
assert odds instanceof int[]

// empty array examples
Integer[] emptyNums = []
assert emptyNums instanceof Integer[] && emptyNums.size() == 0

def emptyStrings = new String[]{}        // alternative syntax 1
assert emptyStrings instanceof String[] && emptyStrings.size() == 0

var emptyObjects = new Object[0]         // alternative syntax 2
assert emptyObjects instanceof Object[] && emptyObjects.size() == 0


//3. Working with arrays

Integer[] nums = [5, 6, 7, 8]
assert nums[1] == 6
assert nums.getAt(2) == 7                // alternative syntax
assert nums[-1] == 8                     // negative indices
assert nums instanceof Integer[]

int[] primes = [2, 3, 5, 7]              // primitives
assert primes instanceof int[]

String[] vowels = ['a', 'e', 'i', 'o', 'u']
var res = ''
vowels.each {
    res += it
}
assert res == 'aeiou'
res = ''
vowels.eachWithIndex { v, i ->
    res += v * i         // index starts from 0
}
assert res == 'eiiooouuuu'


int[] int_nums = [1, 2, 3]
def doubled = int_nums.collect { it * 2 }
assert doubled == [2, 4, 6] && doubled instanceof List
def tripled = int_nums*.multiply(3)
assert tripled == [3, 6, 9] && doubled instanceof List

assert int_nums.any{ it > 2 }
assert int_nums.every{ it < 4 }
assert int_nums.average() == 2
assert int_nums.min() == 1
assert int_nums.max() == 3
assert int_nums.sum() == 6
assert int_nums.indices == [0, 1, 2]
assert int_nums.swap(0, 2) == [3, 2, 1] as int[]


//4. Working with legacy Date/Calendar types
import static java.util.Calendar.*

def cal = Calendar.instance
cal[YEAR] = 2000
cal[MONTH] = JANUARY
cal[DAY_OF_MONTH] = 1
assert cal[DAY_OF_WEEK] == SATURDAY


def utc = TimeZone.getTimeZone('UTC')
Date date = Date.parse("yyyy-MM-dd HH:mm", "2010-05-23 09:01", utc)

def prev = date - 1
def next = date + 1

def diffInDays = next - prev
assert diffInDays == 2

int cnt = 0
prev.upto(next) { cnt++ }
assert cnt == 3

def orig = '2000-01-01'
def newYear = Date.parse('yyyy-MM-dd', orig)
assert newYear[DAY_OF_WEEK] == SATURDAY
assert newYear.format('yyyy-MM-dd') == orig
assert newYear.format('dd/MM/yyyy') == '01/01/2000'

def new_Year = Date.parse('yyyy-MM-dd', '2000-01-01')
def new_YearsEve = new_Year.copyWith(
        year: 1999,
        month: DECEMBER,
        dayOfMonth: 31
)
assert new_YearsEve[DAY_OF_WEEK] == FRIDAY


//5. Working with Date/Time types
//5.1. Formatting and parsing
import java.time.*
import java.time.temporal.*

def local_date = LocalDate.parse('Jun 3, 04', 'MMM d, yy')
assert local_date == LocalDate.of(2004, Month.JUNE, 3)

def local_time = LocalTime.parse('4:45', 'H:mm')
assert local_time == LocalTime.of(4, 45, 0)

def local_offsetTime = OffsetTime.parse('09:47:51-1234', 'HH:mm:ssZ')
assert local_offsetTime == OffsetTime.of(9, 47, 51, 0, ZoneOffset.ofHoursMinutes(-12, -34))

def local_dateTime = ZonedDateTime.parse('2017/07/11 9:47PM Pacific Standard Time', 'yyyy/MM/dd h:mma zzzz')
assert local_dateTime == ZonedDateTime.of(
        LocalDate.of(2017, 7, 11),
        LocalTime.of(21, 47, 0),
        ZoneId.of('America/Los_Angeles')
)


//5.2. Manipulating date/time
def aprilFools = LocalDate.of(2018, Month.APRIL, 1)

def nextAprilFools = aprilFools + Period.ofDays(365) // add 365 days
assert nextAprilFools.year == 2019

def idesOfMarch = aprilFools - Period.ofDays(17) // subtract 17 days
assert idesOfMarch.dayOfMonth == 15
assert idesOfMarch.month == Month.MARCH

def next_AprilFools = aprilFools + 365 // add 365 days
def ides_OfMarch = aprilFools - 17 // subtract 17 days

def mars = LocalTime.of(12, 34, 56) // 12:34:56 pm

def thirtySecondsToMars = mars - 30 // go back 30 seconds
assert thirtySecondsToMars.second == 26

def period = Period.ofMonths(1) * 2 // a 1-month period times 2
assert period.months == 2

def duration = Duration.ofSeconds(10) / 5// a 10-second duration divided by 5
assert duration.seconds == 2

def year = Year.of(2000)
--year // decrement by one year
assert year.value == 1999

def offsetTime = OffsetTime.of(0, 0, 0, 0, ZoneOffset.UTC) // 00:00:00.000 UTC
offsetTime++ // increment by one second
assert offsetTime.second == 1

def duration_sec = Duration.ofSeconds(-15)
def negated = -duration_sec
assert negated.seconds == 15


//5.3. Interacting with date/time values

def date_local = LocalDate.of(2018, Month.MARCH, 12)
assert date_local[ChronoField.YEAR] == 2018
assert date_local[ChronoField.MONTH_OF_YEAR] == Month.MARCH.value
assert date_local[ChronoField.DAY_OF_MONTH] == 12
assert date_local[ChronoField.DAY_OF_WEEK] == DayOfWeek.MONDAY.value

def period_local = Period.ofYears(2).withMonths(4).withDays(6)
assert period_local[ChronoUnit.YEARS] == 2
assert period_local[ChronoUnit.MONTHS] == 4
assert period_local[ChronoUnit.DAYS] == 6


def start = LocalDate.now()
def end = start + 6 // 6 days later
(start..end).each { d ->
    println d.dayOfWeek
}

def start_loc = LocalDate.now()
def end_loc = start + 6 // 6 days later
start_loc.upto(end_loc) { n ->
    println n.dayOfWeek
}

def start_date = LocalDate.of(2018, Month.MARCH, 1)
def end_date = start_date + 1 // 1 day later

int iterationCount = 0
start_date.upto(end_date, ChronoUnit.MONTHS) { n ->
    println n
    ++iterationCount
}

assert iterationCount == 1

def newYears = LocalDate.of(2018, Month.JANUARY, 1)
def april_Fools = LocalDate.of(2018, Month.APRIL, 1)

def period1 = newYears >> aprilFools
assert period1 instanceof Period
assert period1.months == 3

def duration2 = LocalTime.NOON >> (LocalTime.NOON + 30)
assert duration2 instanceof Duration
assert duration2.seconds == 30

//6. Handy utilities
def config = new ConfigSlurper().parse('''
    app.date = new Date()  
    app.age  = 42
    app {                  
        name = "Test${42}"
    }
''')

assert config.app.date instanceof Date
assert config.app.age == 42
assert config.app.name == 'Test42'


def config2 = new ConfigSlurper().parse('''
    app.date = new Date()
    app.age  = 42
    app.name = "Test${42}"
''')

assert config2.test != null

def config3 = new ConfigSlurper().parse('''
    app."person.age"  = 42
''')

assert config3.app."person.age" == 42

def config4 = new ConfigSlurper('development').parse('''
  environments {
       development {
           app.port = 8080
       }

       test {
           app.port = 8082
       }

       production {
           app.port = 80
       }
  }
''')

assert config4.app.port == 8080


def slurper = new ConfigSlurper()
slurper.registerConditionalBlock('myProject', 'developers')

def config5 = slurper.parse('''
  sendMail = true

  myProject {
       developers {
           sendMail = false
       }
  }
''')

assert !config5.sendMail

