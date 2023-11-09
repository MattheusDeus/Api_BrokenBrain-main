package com.brokenbrain.reabnext.gpt.model;

import com.brokenbrain.reabnext.treino.model.Treino;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;


@Entity
@Table(name = "TB_GPT")
public class GPT {

    @Id
    @GeneratedValue(generator = "SQ_GPT", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SQ_GPT", sequenceName = "SQ_GPT")
    @Column(name = "ID_GPT")
    private
    Long idGpt;

    @Column(name = "API_ID")
    private
    String id;

    @Column(name = "OBJ_GPT")
    private
    String object;

    @Column(name = "DT_CRIACAO")
    private
    LocalDate created;

    @Column(name = "NM_MODEL")
    private
    String model;

    // - Relacionamento de Muitos para Um (N:1) De: Treino(N) para GPT(1)
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
            name = "ID_TREINO",
            referencedColumnName = "ID_TREINO",
            foreignKey = @ForeignKey(name = "FK_GPT_TREINO")
    )
    private Treino treino;

    @Column(length = 3000)
    private String choices;

    @Embedded
    private Usage usage;

    @Column(name = "INPUT_GPT",  length = 3000)
    private final String inputGpt = """
            Gere uma lista com uma rotina de %s dias de treino de fiseoterapia (cada dia sendo um item da lista) a partir de %s para uma pessoa com deficiencia ( %s ) em reabilitacao, pesando %,.0f Kg, com %,.2f metros de altura e com %s anos de idade.""";

    @Column(name = "PROMPT_GPT")
    private String inputGptPrompt;

    @Column(name = "output",  length = 3000)
    private String outputGpt;
    
    public GPT() {
    }

    public GPT(Treino treino){
        this.treino = treino;
    }

    public GPT(String id, String object, LocalDate created, String model, Treino treino, String choices) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.treino = treino;
        this.choices = choices;
    }

    // - getInputGptPrompt é o método que gera a String que será colocada como input na API da OpenAI para Gerar o treino
    public String getInputGptPrompt() {
        String stringFormatada = String.format(inputGpt,
                treino.getQtdDias(),
                treino.getDtInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                treino.getPaciente().getDescDeficiencia(),
                treino.getPaciente().getPeso(),
                treino.getPaciente().getAltura(),
                Year.now().minusYears(treino.getPaciente().getDtNasc().getYear()));
        return stringFormatada;
    }

    public String getOutputGpt() {
        return outputGpt;
    }

    public GPT setOutputGpt(String output) {
        this.outputGpt = output;
        return this;
    }

    public Treino getTreino() {
        return treino;
    }

    public GPT setTreino(Treino treino) {
        this.treino = treino;
        return this;
    }

    public Long getIdGpt() {
        return idGpt;
    }

    public GPT setIdGpt(Long code) {
        this.idGpt = code;
        return this;
    }

    public String getId() {
        return id;
    }

    public GPT setId(String id) {
        this.id = id;
        return this;
    }

    public String getObject() {
        return object;
    }

    public GPT setObject(String object) {
        this.object = object;
        return this;
    }

    public LocalDate getCreated() {
        return created;
    }

    public GPT setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    public String getModel() {
        return model;
    }

    public GPT setModel(String model) {
        this.model = model;
        return this;
    }

    public String getChoices() {
        return choices;
    }

    public GPT setChoices(String choices) {
        this.choices = choices;
        return this;
    }

    public Usage getUsage() {
        return usage;
    }

    public GPT setUsage(Usage usage) {
        this.usage = usage;
        return this;
    }

    public String getInputGpt() {
        return inputGpt;
    }

    public GPT setInputGptPrompt(String inputGptPrompt) {
        this.inputGptPrompt = inputGptPrompt;
        return this;
    }


    @Override
    public String toString() {
        return "GPT{" +
                "idGpt=" + idGpt +
                ", id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", created=" + created +
                ", model='" + model + '\'' +
                ", treino=" + treino +
                ", choices='" + choices + '\'' +
                ", usage=" + usage +
                ", inputGpt='" + inputGpt + '\'' +
                ", inputGptPrompt='" + inputGptPrompt + '\'' +
                ", outputGpt='" + outputGpt + '\'' +
                '}';
    }
}
