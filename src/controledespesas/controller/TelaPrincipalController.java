/*
 * Este código faz parte do projeto de exemplo do minicurso: 
 * Introdução ao JavaFX, do 14º CIC da FASB
 *
 * Ministrado por: Fernando Andrauss e Andeson de Jesus
 */
package controledespesas.controller;

import controledespesas.model.Despesa;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.util.converter.DoubleStringConverter;

/**
 * Classe responsável por controlar e tratar eventos da interface definida no
 * FXML
 *
 * @author Fernando Andrauss
 */
public class TelaPrincipalController implements Initializable {

    /**
     * Declaração dos objetos que serão manipulados pelo controller
     *
     * Os objetos anotados com "@FXML" representam os componentes definidos na
     * tela_principal.fxml
     */
    @FXML
    private TextField txtDescricao, txtValor;

    @FXML
    private Button btnAdicionar, btnRemover;

    @FXML
    private CheckBox chkLegenda;

    @FXML
    private TableColumn<Despesa, String> colDescricao;

    @FXML
    private TableColumn<Despesa, Double> colValor;

    @FXML
    private TableView<Despesa> tblDespesas;

    @FXML
    private PieChart grafDespesas;

    /**
     * Declaração de Objetos de controle e armazenamento
     */
    // Objeto que representa a despesa
    private Despesa despesa;

    // Lista das despesas
    private final ObservableList<Despesa> lista = observableArrayList();

    // Lista de dados do gráfico
    private final ObservableList<PieChart.Data> listaGrafico = observableArrayList();

    // Variável que receberá dados do campo "Valor"
    private final DoubleProperty valor = new SimpleDoubleProperty(0);

    /**
     * Método inicial do construtor, esse método é executado assim que a tela é
     * chamada
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Instanciando o objeto despesa (criando uma nova despesa)
        despesa = new Despesa();

        // Configurando o campo valor
        txtValor.setTextFormatter(new TextFormatter(new DoubleStringConverter())); // Definindo seu formatador (para aceitar somente números)
        txtValor.setText("0");                                                     // Inicializando com o valor zero

        /**
         * Bindings (ligações entre propriedades para controle de dados)
         *
         * Esta seção define o comportamento de componentes e variáveis de
         * acordo com o estado de outros componentes
         */
        // Ligando a variável de valor ao conteúdo do campo "Valor"
        txtValor.textProperty().addListener((v, o, n) -> {
            try {

                valor.set(Double.parseDouble(n));

            } catch (NumberFormatException e) {
                valor.set(0);
            }
        });

        // Ligando os campos da tela ao objeto despesa
        ligarCampos();

        // Definindo quando o botão "Adicionar" estará habilitado
        btnAdicionar.disableProperty().bind( // A propriedade desabilitado será verdadeira quando:
                txtDescricao.textProperty().isEmpty() // A propriedade texto do campo descrição for vazia
                .or(valor.lessThanOrEqualTo(0.0)) // Ou quando a variável valor for menor ou igual a zero
        );

        // Definindo quando o botão "Remover" estará habilitado
        btnRemover.disableProperty().bind( // A propriedade desabilitado será verdadeira quando:
                tblDespesas.getSelectionModel() // A propriedade item selecionado da tabela de despesas 
                .selectedItemProperty() // for nula (ou seja nenhum item selecionado)
                .isNull()
        );

        // Ligando o checkbox "Legendas" à legeda do gráfico
        chkLegenda.selectedProperty().bindBidirectional(grafDespesas.legendVisibleProperty());

        /**
         * Configuração da TableView (Tabela de Despesas) e Gráfico
         *
         * Este bloco de código define a lista de dados a ser mostrada na tabela
         * e também qual propriedade será mostrada em cada coluna, além de
         * definir o conteúdo a ser exibido no gráfico
         */
        tblDespesas.setItems(lista);                                             // Vinculando a lista de despesas à tabela
        grafDespesas.setData(listaGrafico);                                      // Vinculando a liata de dados do gráfico
        
        // Vinculação das colunas
        colDescricao.setCellValueFactory((TableColumn.CellDataFeatures<Despesa, String> param) -> {
            return param.getValue().getDescricao();                              // Ligando a coluna "Descrição" à propriedade descricao da Despesa
        });

        colValor.setCellValueFactory((TableColumn.CellDataFeatures<Despesa, Double> param) -> {
            return param.getValue().getValor().asObject();                       // Ligando a coluna "Valor" à propriedade valor da Despesa
        });

        // Adicionando ação de remover ao teclar DELETE na TableView
        tblDespesas.setOnKeyPressed((evt) -> {
            // Se a tecla pressionada for "DELETE" e houver algo selecionado na tabela
            if (evt.getCode() == KeyCode.DELETE && !tblDespesas.getSelectionModel().isEmpty()) {
                AcaoRemover();
            }
        });

        /**
         * Adicionando a ação do botão remover via código
         */
        btnRemover.setOnAction((evt) -> {
            AcaoRemover();
        });

    }

    /**
     * Método de ação para o botão "Adicionar"
     */
    @FXML
    private void Adicionar() {

        // Setando os valores no objeto despesa
        despesa.getId().set(lista.size() + 1);                                   // setando o ID (Tamaho da lista + 1)

        // Adicionando os objetos nas listas
        lista.add(despesa);                                                      // Adicionando a despesa 
        listaGrafico.add(despesa.getData());                                     // Adicionando o dado do gráfico
        removerLigacao();

        // "Limpando" o objeto despesa
        despesa = new Despesa();
        ligarCampos();

        // Movendo o foco para o campo "Descrição"
        txtDescricao.requestFocus();

    }

    /**
     * Realiza a ligação dos campos com as propriedades da despesa
     */
    private void ligarCampos() {
        txtDescricao.textProperty().bindBidirectional(despesa.getDescricao());
        txtValor.getTextFormatter().valueProperty().bindBidirectional((Property) despesa.getValor());
    }

    /**
     * Remove a ligação dos campos com as propriedades da despesa
     */
    private void removerLigacao() {
        txtDescricao.textProperty().unbindBidirectional(despesa.getDescricao());
        txtValor.getTextFormatter().valueProperty().unbindBidirectional((Property) despesa.getValor());
    }

    /**
     * Esse método executa a ação de remoção de despesas
     */
    private void AcaoRemover() {

        // Criando o Alerta de confirmação
        Alert a = new Alert(
                Alert.AlertType.CONFIRMATION, // Tipo do Alerta
                "Deseja realmente remover?", // Mensagem
                ButtonType.YES, ButtonType.NO); // Botões

        a.setTitle("Confirmação");              // Título do Alerta
        a.setHeaderText("Remover");             // Cabeçalho

        // Mostrando o alerta e capturando o resultado
        a.showAndWait() // Mostra o Alerta
                .filter(resposta -> resposta.equals(ButtonType.YES)) // Verifica se foi pressionado "Sim"
                .ifPresent(resposta -> RemoverDespesa());                    // Realiza a remoção
    }

    /**
     * Método para remover a despesa selecionanda na TableView
     */
    private void RemoverDespesa() {
        Despesa d = tblDespesas.getSelectionModel().getSelectedItem();           // Obtém a despesa selecionada na tabela
        lista.remove(d);                                                         // Remove a despesa da lista
        listaGrafico.remove(d.getData());                                        // Remove o dado do gráfico
    }

}
