package cn.itcast.test;

enum Color {
    ONE;

    static {
        System.out.println("static");
    }

    Color() {

        System.out.println("init");
    }
}

public class TestEnum {

    public static void main(String[] args) {

        System.out.println(Color.ONE);
    }

}