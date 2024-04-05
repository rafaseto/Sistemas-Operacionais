package gerenciador;

import java.util.LinkedList;

public class QuickFit {

    private int tamanhoUnidadeAlocacao;
    private int tamanhoMemoria4;
    private int tamanhoMemoria8;
    private int tamanhoMemoria12;
    private int comprimentoMemoria4;
    private int comprimentoMemoria8;
    private int comprimentoMemoria12;
    LinkedList<SegmentoMemoria> lista4;     // Lista de tamanho 4
    LinkedList<SegmentoMemoria> lista8;     // Lista de tamanho 8
    LinkedList<SegmentoMemoria> lista12;    // Lista de tamanho 12

    public QuickFit(int tamanhoUnidadeAlocacao, int tamanhoMemoria4, int tamanhoMemoria8, int tamanhoMemoria12) {
        this.tamanhoMemoria4 = tamanhoMemoria4;
        this.tamanhoMemoria8 = tamanhoMemoria8;
        this.tamanhoMemoria12 = tamanhoMemoria12;
        this.tamanhoUnidadeAlocacao = tamanhoUnidadeAlocacao;
        this.comprimentoMemoria4 = tamanhoMemoria4 / tamanhoUnidadeAlocacao;
        this.comprimentoMemoria8 = tamanhoMemoria8 / tamanhoUnidadeAlocacao;
        this.comprimentoMemoria12 = tamanhoMemoria12 / tamanhoUnidadeAlocacao;
        this.lista4 = new LinkedList<>();
        this.lista8 = new LinkedList<>();
        this.lista12 = new LinkedList<>();

        // Criando o primeiro seg. de mem. pra lista4
        lista4.add(new SegmentoMemoria(false, 0, comprimentoMemoria4));

        // Criando o primeiro seg. de mem. pra lista8
        lista8.add(new SegmentoMemoria(false, 0, comprimentoMemoria8));

        // Criando o primeiro seg. de mem. pra lista12
        lista12.add(new SegmentoMemoria(false, 0, comprimentoMemoria12));
    }

    // M�todo para alocar um processo na mem�ria
    public void alocaProcesso(Processo processo) {
        int comprimento = processo.getComprimento(); // Obt�m o comprimento do processo

        // Objeto do tipo SegmentoMemoria que representa o seg. que ser� alocado para o processo
        SegmentoMemoria segmento = new SegmentoMemoria(true, 0, comprimento);

        LinkedList<SegmentoMemoria> lista;

        // Determinando qual lista usar com base no comprimento do processo
        if (comprimento <= 4) {
            lista = lista4;
        } else if (comprimento <= 8) {
            lista = lista8;
        } else {
            lista = lista12;
        }

        int index = buscaSegmento(comprimento, lista); // Busca um segmento na lista

        // Se n�o houver espa�o suficiente na lista
        if (index == -1) {
            System.out.println("N�o existe mem�ria suficiente para alocar este processo de comprimento " + comprimento);
            System.out.println();
        } else {
            atualizaLista(index, segmento, lista); // Atualiza a lista com o novo segmento
        }
    }

    public int buscaSegmento(int comprimento, LinkedList<SegmentoMemoria> lista) {
        // Buscando um segmento de mem�ria para a aloca��o a partir do ponto que t�nhamos parado
        for (int i = 0; i < lista.size(); i++) {
            // Se o comprimento do seg. for >= ao tamanho exigido pelo processo, o seg. � escolhido
            if (
                    !lista.get(i).isOcupado()
                            && lista.get(i).getComprimento() >= comprimento
            ) {
                return i;
            }
        }
        return -1;
    }
    //Toda vez que um segmento � inserido na lista, o �ndice dos segmentos armazenados na lista precisa ser atualizada
    public void atualizaLista(int indice, SegmentoMemoria seg, LinkedList<SegmentoMemoria> lista) {

        //pega o comprimento do segmento que ser� utilizado para alocar o processo
        int comprimento = lista.get(indice).getComprimento();

        //se o comprimento do segmento � igual ao necess�rio para alocar o processo, nenhuma atualiza��o � necess�ria
        if (comprimento == seg.getComprimento()) {
            //atualiza a posi��o inicial do novo semento
            seg.setPosicaoInicial(lista.get(indice).getPosicaoInicial());

            //insere novo segmento na lista encadeada
            lista.set(indice, seg);

            //se o comprimento do segmento for maior, ser� necess�rio atualizar a posi��o inicial desse segmento e tamb�m o seu comprimento
        } else {
            //atualiza a posi��o inicial do novo semento
            seg.setPosicaoInicial(lista.get(indice).getPosicaoInicial());

            //acrescenta o segmento novo
            lista.add(indice, seg);
            //atualiza o segmento antigo, reduzindo o tamanho do comprimento dele e atualizando a posi��o inicial
            SegmentoMemoria segAntigo = lista.get(indice + 1);

            segAntigo.setComprimento(segAntigo.getComprimento() - seg.getComprimento());
            segAntigo.setPosicaoInicial(seg.getPosicaoInicial() + seg.getComprimento());

            //atualiza a posi��o inicial dos demais segmentos existentes na lista. Se n�o existir mais segmentos, nada ser� feito.
            SegmentoMemoria aux;

            for (int a = indice + 2; a < lista.size(); a++) {

                aux = lista.get(a);

                aux.setPosicaoInicial(lista.get(a - 1).getPosicaoInicial() + lista.get(a - 1).getComprimento());
            }
        }
    }

    // M�todo para agregar os segmentos vazios das tr�s listas
    public void agregaSegmentosVazios() {
        agregaSegmentosVazios(lista4);

        agregaSegmentosVazios(lista8);

        agregaSegmentosVazios(lista12);
    }

    // M�todo para agregar os segmentos vazios de uma lista espec�fica
    private void agregaSegmentosVazios(LinkedList<SegmentoMemoria> lista) {
        // Itera sobre os segmentos da lista
        for (int i = 0; i < lista.size() - 1; i++) {
            SegmentoMemoria seg1 = lista.get(i);
            SegmentoMemoria seg2 = lista.get(i + 1);

            // Verificando se ambos os segmentos est�o vazios
            if (!seg1.isOcupado() && !seg2.isOcupado()) {
                // Agrupando em um �nico segmento
                seg1.setComprimento(seg1.getComprimento() + seg2.getComprimento());
                lista.remove(i + 1); // Removendo segmento vazio
                i--;
            }
        }
    }

    public void exibeSegmentosMemoria() {

        System.out.println("-------- Segmentos de Mem�ria -------- \n");

        System.out.println("Lista 4 \n");
        for (int a = 0; a < lista4.size(); a++) {
            System.out.println("Segmento [" + a + "] --> " + lista4.get(a));
        }

        System.out.println("\n\n");

        System.out.println("Lista 8 \n");
        for (int a = 0; a < lista8.size(); a++) {
            System.out.println("Segmento [" + a + "] --> " + lista8.get(a));
        }

        System.out.println("\n\n");

        System.out.println("Lista 12 \n");
        for (int a = 0; a < lista12.size(); a++) {
            System.out.println("Segmento [" + a + "] --> " + lista12.get(a));
        }

        System.out.println("\n\n");
    }

    public void exibeListaEncadeada() {

        System.out.println("-------- Segmentos de Mem�ria -------- \n");

        System.out.println("Lista 4 \n");
        for (int a = 0; a < lista4.size(); a++) {
            if (lista4.size() - 1 == a) {
                System.out.print(lista4.get(a));
            } else {
                System.out.print(lista4.get(a) + " --> ");
            }
        }
        System.out.println("\n\n");

        System.out.println("Lista 8 \n");
        for (int a = 0; a < lista8.size(); a++) {
            if (lista8.size() - 1 == a) {
                System.out.print(lista8.get(a));
            } else {
                System.out.print(lista8.get(a) + " --> ");
            }
        }
        System.out.println("\n\n");

        System.out.println("Lista 12 \n");
        for (int a = 0; a < lista12.size(); a++) {
            if (lista12.size() - 1 == a) {
                System.out.print(lista12.get(a));
            } else {
                System.out.print(lista12.get(a) + " --> ");
            }
        }
        System.out.println("\n\n");
    }

    public static void main(String[] args) {
        // Configura��o do QuickFit com 3 listas
        QuickFit gerenciador = new QuickFit(2, 8, 16, 24); // Mem�ria de 64KB para cada tamanho de segmento

        // Processos com diferentes tamanhos
        Processo processo1 = new Processo(2);  // Comprimento 2
        Processo processo2 = new Processo(2);  // Comprimento 2
        Processo processo3 = new Processo(8);  // Comprimento 8

        // Exibe o estado da mem�ria antes da aloca��o
        gerenciador.exibeListaEncadeada();

        // Aloca os processos
        gerenciador.alocaProcesso(processo1);
        gerenciador.alocaProcesso(processo2);
        gerenciador.alocaProcesso(processo3);

        // Exibe o estado da mem�ria ap�s a aloca��o
        gerenciador.exibeListaEncadeada();
    }
}