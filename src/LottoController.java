import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.Vector;
import javax.swing.JCheckBox;

public class LottoController {

    private ConnectDatabase connDb;
    private Vector<Integer> numberList;
    private Vector<Integer> drawedList;
    private Vector<Integer> chosenList;
    private LottoForm lottoFrm;
    private int counter = 0;

    public LottoController(ConnectDatabase connDb) {
        chosenList = new Vector<>();
        drawedList = new Vector<>();
        numberList = new Vector<>();
        this.connDb = connDb;
        lottoFrm = new LottoForm();
        lottoFrm.exitBtn.addActionListener(event -> exit());
        lottoFrm.drawBtn.addActionListener(event -> drawing());
        fillNumberList();
        nubmerCheckboxes();
        lottoFrm.setVisible(true);
    }

    private void fillNumberList() {
        for (int i = 1; i < 46; i++) {
            numberList.add(i);
        }
    }

    private void nubmerCheckboxes() {
        for (Integer i = 1; i < 46; i++) {
            JCheckBox cBox = new JCheckBox();
            cBox.setText(i.toString());
            lottoFrm.centerPnl.add(cBox);

            cBox.addItemListener(event -> {
                JCheckBox check = (JCheckBox) event.getSource();
                chosenList.add(Integer.parseInt(check.getText()));

                counter++;
                if (counter == 7) {
                    lottoFrm.drawBtn.setEnabled(true);
                } else {
                    lottoFrm.drawBtn.setEnabled(false);
                }
            });
        }
    }

    public void drawing() {
        int numbers = 45;
        Random rand = new Random();

        for (int i = 0; i < 7; i++) {
            int number = rand.nextInt(numbers) + 1;
            numberList.remove(number - 1);
            numbers--;
            drawedList.add(number);
        }
        showResult();
        numbersToDatabase();
    }

    public void showResult() {
        Integer matches = 0;

        for (int i = 0; i < chosenList.size(); i++) {
            for (int j = 0; j < drawedList.size(); j++) {
                if (chosenList.get(i) == drawedList.get(j)) {
                    matches++;
                }
            }
        }

        String resultValue = lottoFrm.resultLbl.getText();
        lottoFrm.resultLbl.setText(resultValue + matches.toString());

        for (int i = 0; i < drawedList.size(); i++) {
            String drawValue = lottoFrm.drawLbl.getText();
            String number = String.valueOf(drawedList.get(i));
            lottoFrm.drawLbl.setText(drawValue + number + " ");
        }
    }

    public void numbersToDatabase() {
        Connection conn = connDb.getConnection();
        Statement stmt = null;
        String dataToDabase = "";

        for (int i = 0; i < drawedList.size(); i++) {

            if (i < drawedList.size() - 1) {
                dataToDabase += String.valueOf(drawedList.get(i)) + ":";
            } else {
                dataToDabase += String.valueOf(drawedList.get(i));
            }
        }

        String sql = "INSERT INTO drawed(draw) VALUES ('" + dataToDabase + "')";
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        connDb.closeConnect();
    }

    public void exit() {
        System.exit(0);
    }

}
