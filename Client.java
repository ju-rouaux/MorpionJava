public class Client {




    /**
     * //TODO
     * Retourne vrai si le client peut jouer son tour.
     * @return vrai si le client peut jouer son tour.
     */
    public boolean canPlay() {
        return true;
    }


    public static void main(String[] args) {
        GUI gui = new GUI(null);
        gui.serverButtonClicked(5);
    }
}
