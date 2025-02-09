package dataStructure.non_linear.bst;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.add(5); //루트 노드
        bst.add(2);
        bst.add(9);
        bst.add(3);
        bst.add(7);
        bst.add(4);
        bst.add(8);
        bst.add(1);
        bst.add(6);

        bst.traversal();
        bst.remove(2);
//        bst.remove(10); //Not exists node
        bst.traversal();

        BinarySearchTree<Student> students = new BinarySearchTree<>();
        students.add(new Student(10, "Student1")); //루트 노드
        students.add(new Student(50, "Student2"));
        students.add(new Student(30, "Student3"));
        students.add(new Student(60, "Student4"));
        students.add(new Student(20, "Student5"));
        students.add(new Student(40, "Student6"));

        students.traversal();

        Student student = students.search(new Student(10, "Student1"));
        System.out.println("found student = " + student);
    }

    static class Student implements Comparable<Student> {
        int age;
        String name;

        public Student(int age, String name) {
            this.age = age;
            this.name = name;
        }

        @Override
        public int compareTo(Student o) {
            return this.age - o.age;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Student student)) return false;
            return age == student.age && Objects.equals(name, student.name);
        }

        @Override
        public String toString() {
            return "Student{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
        }
    }
}
