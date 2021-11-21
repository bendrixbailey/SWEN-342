public class TestWoolieRunner {
    public static void main(String[] args) {
        Bridge b1 = new Bridge();

        Woolie w1 = new Woolie("Bob1", 5, "Merctran", b1);
        Woolie w2 = new Woolie("Bob2", 3, "Sicstine", b1);
        Woolie w3 = new Woolie("Bob3", 8, "Merctran", b1);
        Woolie w4 = new Woolie("Bob4", 4, "Merctran", b1);
        Woolie w5 = new Woolie("Bob5", 6, "Sicstine", b1);
        Woolie w6 = new Woolie("Bob6", 3, "Sicstine", b1);

        w1.start();
        w2.start();
        w3.start();
        w4.start();
        w5.start();
        w6.start();
    }
}
