package demo

@groovy.transform.ToString
class Job {
    String roleName
    int salary
}

class Person {
    String name
    int age
    Job job
}

@groovy.transform.ToString
class Department {
    String deptName
    Set<Person> staff = [
            new Person(name: 'Matt', age: 30, job: new Job(roleName: 'Developer', salary: 1000)),
            new Person(name: 'Ling', age: 30, job: new Job(roleName: 'MLE', salary: 1000)),
            new Person(name: 'Emily')
    ]
}

@groovy.transform.ToString
class FoodOrder {
    String name
    BigDecimal cost
    FoodOrder(name, cost) {
        this.name = name
        this.cost = cost
    }
}

enum Weekdays {
    MON, TUE, WED,
}

static void main(String[] args) {
    def s = "Hello"
    println s.toUpperCase()
    println s?.toUpperCase()

    def dept = new Department(deptName: "Engineering")
    println dept
    println "Salary: ${dept?.staff[2]?.job?.salary}"

    def driveThruOrder = [
            new FoodOrder('Burger', 3.99),
            new FoodOrder('Fries', 1.99),
            new FoodOrder('Milkshake', 2.75)
    ]

    def loggedInUser = 'Adam'
    def displayUsername = loggedInUser ?: 'Guest'

    def l = ['black', 'white', 'blue', 'red']
    println l.sort()
    println l.sort { a, b -> b <=> a }

    println driveThruOrder.sort { a, b -> a.name <=> b.name }
    println driveThruOrder.sort { a, b -> a.cost <=> b.cost }
    driveThruOrder.findAll {it.cost >= 2.0 }.each {it -> println 'Name: ' + it.name}
    driveThruOrder.find {it.cost >= 2.0 }.each {it -> println 'Name: ' + it.name}
    driveThruOrder.collectEntries {[(it.cost): it.name]}.each {println it.key}

    def fruits = ['apple', 'orange', 'pears']
    def shoppingList = ['milk', *fruits]
    println shoppingList
    println shoppingList*.toUpperCase()

    println(1.0..5.0).each { println it }
// Range from TUE to WED (exclusive)
    (Weekdays.TUE..<Weekdays.WED).each { println it }

    def ss = 'how are you?'
    println ss.getClass()

    def re = ~/S.*/
    def matcher = re.matcher('Sweet')
    println matcher.matches()

    def matcher2 = ('Suger' =~ /S.*/)
    println matcher2.matches()

    def matcher3 = ('Tuger' ==~ /S.*/)
    println matcher3

    def s1 = "Sunday is my favorite day!"
    def matchResult = (s1 =~ /(.*) is (.*) favorite day!/)
    println matchResult[0][1]
    println matchResult[0][2]

    def i_list = [1, 2, 3, 4, 10]
    println i_list[0..2]
    println i_list.each { println it }
    println i_list.eachWithIndex { int entry, int i -> println 'element: ' + i + ', entry: ' + entry }
    println i_list.findAll { it % 2 == 1 }
    println i_list.find { it % 2 == 1 }
    println i_list.min()
    println i_list.inject(0) { sum, e -> sum + e}
    println i_list.inject(0) { max, e -> Math.max(max, e)}

    def c_list = ['Tom', 'Dick', 'Harry']
    println c_list.findAll {it.startsWith('T')}
    println c_list.every {it.startsWith('T')}
    println c_list.any {it.startsWith('T')}
    println c_list.collect {it.size()}

    def map = [
            fruits: ['apple', 'banana'],
            vegetables: ['apple', 'banana']
    ]
    println map['fruits'][0]
    map.each {k, v -> println "$k = $v" }

}

