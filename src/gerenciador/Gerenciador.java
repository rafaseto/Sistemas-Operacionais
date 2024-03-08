/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gerenciador;

import java.util.LinkedList;

/**
 *
 * @author JoãoPaulo
 */
public class Gerenciador {

    private int tamanhoUnidadeAlocacao;
    private int tamanhoMemoria;
    private int comprimentoMemoria; //tamanho da memória em unidades de alocação, ou seja, é a quantidade de unidades de alocação que essa memória possui.
    LinkedList<SegmentoMemoria> listaEncadeada;

    public Gerenciador(int tamanhoUnidadeAlocacao, int tamanhoMemoria) {
        this.listaEncadeada = new LinkedList();
        this.tamanhoMemoria = tamanhoMemoria;
        this.tamanhoUnidadeAlocacao = tamanhoUnidadeAlocacao;

        this.comprimentoMemoria = tamanhoMemoria / tamanhoUnidadeAlocacao;

        //cria o primeiro segmento de memória, indicando que a memória está livre
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

    /* A alocação está utilizando o algorimo First Fit */
    public void alocaProcesso(Processo p) {

        //Segmento de memória para alocar o processo
        SegmentoMemoria seg = new SegmentoMemoria(true, 0, p.getComprimento());
        p.setSegmento(seg);

        //encontra a posição adequada para alocar o processo
        int indice = buscaSegmento(seg.getComprimento());

        if (indice == -1) {
            System.out.println("Não existe memória suficiente para alocar este processo de comprimento " + p.getComprimento());
            System.out.println("\n");
        } else {
            atualizaLista(indice, seg);
        }
    }

    //Toda vez que um segmento é inserido na lista, o índice dos segmentos armazenados na lista precisa ser atualizada  
    public void atualizaLista(int indice, SegmentoMemoria seg) {

        //pega o comprimento do segmento que será utilizado para alocar o processo
        int comprimento = listaEncadeada.get(indice).getComprimento();

        //se o comprimento do segmento é igual ao necessário para alocar o processo, nenhuma atualização é necessária
        if (comprimento == seg.getComprimento()) {
            //atualiza a posição inicial do novo semento
            seg.setPosicaoInicial(listaEncadeada.get(indice).getPosicaoInicial());            
            
            //insere novo segmento na lista encadeada
            listaEncadeada.set(indice, seg);
            
            //se o comprimento do segmento for maior, será necessário atualizar a posição inicial desse segmento e também o seu comprimento
        } else {
            //atualiza a posição inicial do novo semento
            seg.setPosicaoInicial(listaEncadeada.get(indice).getPosicaoInicial());
            
            //acrescenta o segmento novo
            listaEncadeada.add(indice, seg);
            //atualiza o segmento antigo, reduzindo o tamanho do comprimento dele e atualizando a posição inicial
            SegmentoMemoria segAntigo = listaEncadeada.get(indice + 1);

            segAntigo.setComprimento(segAntigo.getComprimento() - seg.getComprimento());
            segAntigo.setPosicaoInicial(seg.getPosicaoInicial() + seg.getComprimento());

            //atualiza a posição inicial dos demais segmentos existentes na lista. Se não existir mais segmentos, nada será feito.
            SegmentoMemoria aux;

            for (int a = indice + 2; a < listaEncadeada.size(); a++) {

                aux = listaEncadeada.get(a);

                aux.setPosicaoInicial(listaEncadeada.get(a - 1).getPosicaoInicial() + listaEncadeada.get(a - 1).getComprimento());
            }
        }
    }

    //Lembrem que é necessário agrupar os segmentos vazios que estão próximos
    public void agregaSegmentosVazios() {
    }

    /**
     * Este método procura por um segmento de memória na lista encadeada que
     * seja maior ou igual que o comprimento passado como parâmetro.
     *
     * @param comprimento comprimento mínimo do segmento desejado
     * @return retorna o índice do segmento para alocar o processo, ou retorna
     * -1 caso não tenha segmento com tamanho adequado.
     */
    public int buscaSegmento(int comprimento) {

        for (int a = 0; a < listaEncadeada.size(); a++) {
            //Se o comprimento do segmento for maior ou igual ao tamanho exigido pelo processo, esse será o segmento escolhido
            if (!listaEncadeada.get(a).isOcupado() && listaEncadeada.get(a).getComprimento() >= comprimento) {
                return a;
            }
        }
        return -1;
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

        //Gerenciador configurado para uma memória de 62KB e dividida em unidades de alocação de 2KB
        Gerenciador gerenciador = new Gerenciador(2, 62);

        //cria um processo que precisa de um segmento de memória com um comprimento maior ou igual a 10 para ser executado        
        Processo d = new Processo(10);
        
        Processo e = new Processo(3);
        Processo f = new Processo(1);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(d);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(e);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(f);

        gerenciador.exibeListaEncadeada();

        //Processo finalizado
        d.setOnExecution(false);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(f);

        gerenciador.exibeListaEncadeada();
    }
}
