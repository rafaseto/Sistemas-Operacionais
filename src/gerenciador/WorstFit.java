/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gerenciador;

import java.util.LinkedList;

/**
 *
 * @author Jo�oPaulo
 */
public class WorstFit {

    private int tamanhoUnidadeAlocacao;
    private int tamanhoMemoria;
    private int comprimentoMemoria;
    LinkedList<SegmentoMemoria> listaEncadeada;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public WorstFit(int tamanhoUnidadeAlocacao, int tamanhoMemoria) {
        this.listaEncadeada = new LinkedList();
        this.tamanhoMemoria = tamanhoMemoria;
        this.tamanhoUnidadeAlocacao = tamanhoUnidadeAlocacao;

        this.comprimentoMemoria = tamanhoMemoria / tamanhoUnidadeAlocacao;

        listaEncadeada.add(new SegmentoMemoria(false, 0, comprimentoMemoria));
    }

    public int getUnidadeAlocacao() {
        return tamanhoUnidadeAlocacao;
    }

    public void setUnidadeAlocacao(int unidadeAlocacao) {
        this.tamanhoUnidadeAlocacao = unidadeAlocacao;
    }

    public int getTamanhoMemoria() {
        return tamanhoMemoria;
    }

    public void setTamanhoMemoria(int tamanhoMemoria) {
        this.tamanhoMemoria = tamanhoMemoria;
    }

    /* A alocação está utilizando o algoritmo Worst Fit */
    public void alocaProcesso(Processo p) {

        SegmentoMemoria seg = new SegmentoMemoria(true, 0, p.getComprimento());
        p.setSegmento(seg);

        int indice = buscaSegmento(seg.getComprimento());

        if (indice == -1) {
            System.out.println("Não existe memória suficiente para alocar este processo de comprimento " + p.getComprimento());
            System.out.println("\n");
        } else {
            atualizaLista(indice, seg);
        }
    }

    public void atualizaLista(int indice, SegmentoMemoria seg) {

        int comprimento = listaEncadeada.get(indice).getComprimento();

        if (comprimento == seg.getComprimento()) {
            seg.setPosicaoInicial(listaEncadeada.get(indice).getPosicaoInicial());

            listaEncadeada.set(indice, seg);

        } else {
        
            seg.setPosicaoInicial(listaEncadeada.get(indice).getPosicaoInicial());

            listaEncadeada.add(indice, seg);

            SegmentoMemoria segAntigo = listaEncadeada.get(indice + 1);

            segAntigo.setComprimento(segAntigo.getComprimento() - seg.getComprimento());
            segAntigo.setPosicaoInicial(seg.getPosicaoInicial() + seg.getComprimento());

            SegmentoMemoria aux;

            for (int a = indice + 2; a < listaEncadeada.size(); a++) {

                aux = listaEncadeada.get(a);

                aux.setPosicaoInicial(
                        listaEncadeada.get(a - 1).getPosicaoInicial() + listaEncadeada.get(a - 1).getComprimento());
            }
        }
    }

    // Lembrem que é necessário agrupar os segmentos vazios que estão próximos
    public void agregaSegmentosVazios() {
        for (int i = 0; i < listaEncadeada.size() - 1; i++) {
            SegmentoMemoria seg1 = listaEncadeada.get(i);
            SegmentoMemoria seg2 = listaEncadeada.get(i + 1);
    
            if (!seg1.isOcupado() && !seg2.isOcupado()) {
                seg1.setComprimento(seg1.getComprimento() + seg2.getComprimento());
                listaEncadeada.remove(i + 1);
                i--; 
            }
        }
    }

    /**
     * Este método procura por um segmento de memória na lista encadeada que
     * tenha o maior comprimento.
     *
     * @param comprimento comprimento mínimo do segmento desejado
     * @return retorna o índice do segmento para alocar o processo.
     */
    public int buscaSegmento(int comprimento) {
        int indiceMaior = -1;
        int tamanhoMaior = 0;

        for (int a = 0; a < listaEncadeada.size(); a++) {
            // Procura o segmento com maior comprimento
            if (!listaEncadeada.get(a).isOcupado() && listaEncadeada.get(a).getComprimento() >= comprimento) {
                if (listaEncadeada.get(a).getComprimento() > tamanhoMaior) {
                    tamanhoMaior = listaEncadeada.get(a).getComprimento();
                    indiceMaior = a;
                }
            }
        }
        return indiceMaior;
    }

    public void exibeSegmentosMemoria() {

        System.out.println("-------- Segmentos de Memória -------- \n");

        for (int a = 0; a < listaEncadeada.size(); a++) {
            System.out.println("Segmento [" + a + "] --> " + listaEncadeada.get(a));
        }

        System.out.println("\n\n");
    }

    public void exibeListaEncadeada() {

        System.out.println("-------- Segmentos de Memória -------- \n");

        for (int a = 0; a < listaEncadeada.size(); a++) {
            if (listaEncadeada.size() - 1 == a) {
                System.out.print(listaEncadeada.get(a));
            } else {
                System.out.print(listaEncadeada.get(a) + " --> ");
            }
        }

        System.out.println("\n\n");
    }

    public static void main(String[] args) {

        // Gerenciador configurado para uma memória de 62KB e dividida em unidades de
        // alocação de 2KB
        WorstFit worstFit = new WorstFit(2, 62);

        // cria um processo que precisa de um segmento de memória com um comprimento
        // maior ou igual a 10 para ser executado
        Processo d = new Processo(10);

        Processo e = new Processo(3);
        Processo f = new Processo(1);

        worstFit.exibeListaEncadeada();

        worstFit.alocaProcesso(d);

        worstFit.agregaSegmentosVazios();

        worstFit.exibeListaEncadeada();

        worstFit.alocaProcesso(e);     

        worstFit.agregaSegmentosVazios();

        worstFit.exibeListaEncadeada();

        worstFit.alocaProcesso(f);

        worstFit.agregaSegmentosVazios();

        worstFit.exibeListaEncadeada();

        // Processo finalizado
        d.setOnExecution(false);

        worstFit.agregaSegmentosVazios();

        worstFit.exibeListaEncadeada();

        worstFit.alocaProcesso(f);

        worstFit.agregaSegmentosVazios();

        worstFit.exibeListaEncadeada();

        f.setOnExecution(false);

        worstFit.agregaSegmentosVazios();

        worstFit.exibeListaEncadeada();

    }
}
