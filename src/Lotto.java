public class Lotto {
    public static void main(String[] args) throws Exception {
        ConnectDatabase connDb = new ConnectDatabase();
        LottoController lottoCtrl = new LottoController(connDb);
    }
}
