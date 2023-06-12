public class PasarJSON {

    private String json;
    private Gestion gestion;

    public Gestion pasarGestAJson(String json) {

        Gson gson = new Gson();
        return this.gestion = (Gestion)gson.fromJson(json,Gestion.class);
    }

    public String pasarJsonAGest(Gestion gestion) {

        return this.json = (new Gson()).toJson(gestion);
    }
}
