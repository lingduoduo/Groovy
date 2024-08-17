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
          new Person(name: 'Ling', age: 30, job: new Job(roleName: 'MLE', salary: 1000))
  ]
}

static void main(String[] args) {
  def s = "Hello"
  println s.toUpperCase()
  println s?.toUpperCase()

  def dept = new Department(deptName: "Engineering")
  println dept
  println "Salary: ${dept.staff[1].job.salary}"
}