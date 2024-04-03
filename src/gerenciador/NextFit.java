package gerenciador;

import java.util.LinkedList;

public class NextFit {
    private int tamanhoUnidadeAlocacao;
    private int tamanhoMemoria;
    private int comprimentoMemoria;
    LinkedList<SegmentoMemoria> listaEncadeada;
    int indiceLivre;    // posição que se encontra um espaço livre

    public NextFit(int tamanhoUnidadeAlocacao, int tamanhoMemoria) {
        this.tamanhoUnidadeAlocacao = tamanhoUnidadeAlocacao;
        this.tamanhoMemoria = tamanhoMemoria;
        this.comprimentoMemoria = tamanhoMemoria / tamanhoUnidadeAlocacao;
        this.listaEncadeada = new LinkedList<>();
        this.indiceLivre = -1;

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

    public void alocaProcesso(Processo processo) {
        // Objeto do tipo SegmentoMemoria que representa o seg. que será alocado para o processo
        SegmentoMemoria segmento = new SegmentoMemoria(true, 0, processo.getComprimento());

        processo.setSegmento(segmento);     // 'segmento' é atribuído ao processo
        int comprimento = segmento.getComprimento();    // comprimento do segmento

        int indiceAloc = -1;     // representa a posição inicial onde iremos alocar o processo

        // Buscando um segmento de memória para a alocação
        for (int i = indiceLivre + 1; i < listaEncadeada.size(); i++) {
            // Se o comprimento do seg. for >= ao tamanho exigido pelo processo, o seg. é escolhido
            if (
                    !listaEncadeada.get(i).isOcupado()
                    && listaEncadeada.get(i).getComprimento() >= comprimento
            ) {
                indiceAloc = i;

                // Próximo índice livre será depois desse segmento
                indiceLivre = indiceAloc + comprimento;

                // Verificando se passamos do tamanho da lista
                if (indiceLivre >= listaEncadeada.size()) {
                    indiceLivre = 0;    // retornamos pro início da lista
                }
                break;
            }
        }

        if (indiceAloc == -1) {
            System.out.println("Não existe memória suficiente para alocar este processo de comprimento " + processo.getComprimento());
            System.out.println("\n");
        } else {
            atualizaLista(indiceAloc, segmento);
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
}
