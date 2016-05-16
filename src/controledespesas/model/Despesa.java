/*
 * Este código faz parte do projeto de exemplo do minicurso: 
 * Introdução ao JavaFX, do 14º CIC da FASB
 *
 * Ministrado por: Fernando Andrauss e Andeson de Jesus
 */
package controledespesas.model;

import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.chart.PieChart;

/**
 * Classe que representa o modelo de dados (como serão representados os dados no
 * sistema)
 *
 * @author Fernando Andrauss
 */
public class Despesa {

    // Propriedades da despesa (encapsuladas)
    private IntegerProperty id = new SimpleIntegerProperty(-1);
    private StringProperty descricao = new SimpleStringProperty("");
    private DoubleProperty valor = new SimpleDoubleProperty(0.0);
    private PieChart.Data data;

    // Construtor padrão
    public Despesa() {
        // Inicialização do dado do gráfico
        data = new PieChart.Data("", 0);

        // Registrando um ouvinte para observar alterações na propriedade descrição
        descricao.addListener((v, o, n) -> {
            data.setName(n); // Refletindo as alterações no dado do gráfico
        });

        // Registrando um ouvinte para observar alterações na propriedade valor
        valor.addListener((v, o, n) -> {
            data.setPieValue(n.doubleValue()); // Refletindo as alterações no dado do gráfico
        });

    }

    // Abaixo estão os métodos de acesso às propriedades (getters e setters)
    public IntegerProperty getId() {
        return id;
    }

    public void setId(IntegerProperty id) {
        this.id = id;
    }

    public StringProperty getDescricao() {
        return descricao;
    }

    public void setDescricao(StringProperty descricao) {
        this.descricao = descricao;
    }

    public DoubleProperty getValor() {
        return valor;
    }

    public void setValor(DoubleProperty valor) {
        this.valor = valor;
    }

    public PieChart.Data getData() {
        return data;
    }

    public void setData(PieChart.Data data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Despesa other = (Despesa) obj;
        if (!Objects.equals(this.id.get(), other.id.get())) {
            return false;
        }
        return true;
    }

}
