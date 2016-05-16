/*
 * Este código faz parte do projeto de exemplo do minicurso: 
 * Introdução ao JavaFX, do 14º CIC da FASB
 *
 * Ministrado por: Fernando Andrauss e Andeson de Jesus
 */
package controledespesas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Classe principal da aplicação, a partir daqui app é iniciado
 *
 * @author Fernando Andrauss
 */
public class ControleDespesas extends Application {
    
    @Override
    public void start(Stage palco) throws Exception {

        // Criando o carregador que irá buscar a
        // interface de usuário e trazê-la para ser manipulada via código
        FXMLLoader carregador = new FXMLLoader(getClass().getResource("/controledespesas/view/tela_principal.fxml"));

        // Carregando a interface
        AnchorPane pai = carregador.load();

        // Montagem da cena
        Scene cena = new Scene(pai);

        // Montagem do palco
        palco.setScene(cena); // setando a cena que será exibida no palco
        
        palco.setTitle("Controle de Despesas - 14º CIC"); // setando o título
        
        // Exibindo o palco
        palco.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
