package gerenciador;

import java.util.LinkedList;

public class NextFit {
    private int tamanhoUnidadeAlocacao;
    private int tamanhoMemoria;
    private int comprimentoMemoria;
    LinkedList<SegmentoMemoria> listaEncadeada;

    public NextFit(int tamanhoUnidadeAlocacao, int tamanhoMemoria) {
        this.tamanhoUnidadeAlocacao = tamanhoUnidadeAlocacao;
        this.tamanhoMemoria = tamanhoMemoria;
        this.comprimentoMemoria = tamanhoMemoria / tamanhoUnidadeAlocacao;
        this.listaEncadeada = new LinkedList<>();

        // Criando o primeiro seg. de mem.
        listaEncadeada.add(new SegmentoMemoria(false, 0, comprimentoMemoria));
    }

    // Retorna o tamanho da unidade de aloc.
    public int getTamanhoUnidadeAlocacao() {
        return tamanhoUnidadeAlocacao;
    }

    // Define o tamanho da unidade de aloc.
    public void setTamanhoUnidadeAlocacao(int tamanhoUnidadeAlocacao) {
        this.tamanhoUnidadeAlocacao = tamanhoUnidadeAlocacao;
    }

    // Retorna o tamanho da mem.
    public int getTamanhoMemoria() {
        return tamanhoMemoria;
    }

    // Define o tamanho da mem.
    public void setTamanhoMemoria(int tamanhoMemoria) {
        this.tamanhoMemoria = tamanhoMemoria;
    }
}
