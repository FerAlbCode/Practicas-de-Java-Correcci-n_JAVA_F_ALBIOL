public class Main {
    public static void main(String[] args) {
        Gestion gestion = new Gestion("Fernando","DAM");
        PasarJSON pasar = new PasarJSON();
        String json = pasar.pasarGestAJson(gestion);
        System.out.println(json);
        Gestion gestion1 = pasar.pasarJsonAGest(json);
        System.out.println(gestion1.toString());

    }
}